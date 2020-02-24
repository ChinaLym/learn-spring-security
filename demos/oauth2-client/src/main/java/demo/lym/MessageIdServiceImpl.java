package demo.lym;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLongArray;

public class MessageIdServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageIdServiceImpl.class);
    //最大的MachineId，1024个
    private static final long MAX_MACHINE_ID = 1023L;
    //AtomicLongArray 环的大小，可保存256毫秒内，每个毫秒数上一次的MessageId，时间回退的时候依赖与此
    private static final int CAPACITY = 1 << 8;
    // 时间戳在messageId中右移的位数
    private static final int TIMESTAMP_SHIFT_COUNT = 22;
    // 机器码在messageId中左移的位数
    private static final int MACHINE_ID_SHIFT_COUNT = 12;
    // 序列号的掩码 2^12 4096
    private static final long SEQUENCE_MASK = 4095L;

    //messageId ，开始的时间戳，start the world，世界初始之日
    private static long START_THE_WORLD_MILLIS;
    //机器码变量
    private long machineId;
    // messageId环，解决时间回退的关键，亦可在多线程情况下减少毫秒数切换的竞争
    private AtomicLongArray messageIdCycle = new AtomicLongArray(CAPACITY);

    static {
        try {
            //使用一个固定的时间作为start the world的初始值
            START_THE_WORLD_MILLIS = SimpleDateFormat.getDateTimeInstance().parse("2020-01-01 00:00:00").getTime();
        } catch (ParseException e) {
            throw new RuntimeException("init start the world millis failed", e);
        }
    }

    /**
     * 本机的machineId
     */
    public MessageIdServiceImpl(int unionId){
        if (machineId == 0L) {
            machineId = unionId;
        }
        //获取的machineId 不能超过最大值
        if (machineId <= 0L || machineId > MAX_MACHINE_ID) {
            throw new RuntimeException("the machine id is out of range,it must between 1 and 1023");
        }
    }
    /**
     * 核心实现的代码
     */
    public long genMessageId() {
        do {
            // 获取当前时间戳，此时间戳是当前时间减去start the world的毫秒数
            long timestamp = System.currentTimeMillis() - START_THE_WORLD_MILLIS;
            // 获取当前时间在messageIdCycle 中的下标，用于获取环中上一个MessageId
            int index = (int)(timestamp & 255);
            long messageIdInCycle = messageIdCycle.get(index);
            //通过在messageIdCycle 获取到的messageIdInCycle，计算上一个MessageId的时间戳
            long timestampInCycle = messageIdInCycle >> TIMESTAMP_SHIFT_COUNT;
            // 如果timestampInCycle 并没有设置时间戳，或时间戳小于当前时间，认为需要设置新的时间戳
            if (messageIdInCycle == 0 || timestampInCycle < timestamp) {
                long messageId = timestamp << TIMESTAMP_SHIFT_COUNT | machineId << MACHINE_ID_SHIFT_COUNT;
                // 使用CAS的方式保证在该条件下，messageId 不被重复
                //todo set一个 atomicLong
                if (messageIdCycle.compareAndSet(index, messageIdInCycle, messageId)) {
                    return messageId;
                }
                LOGGER.debug("messageId cycle CAS1 failed");
            }
            // 如果当前时间戳与messageIdCycle的时间戳相等，使用环中的序列号+1的方式，生成新的序列号
            // 如果发生了时间回退的情况，（即timestampInCycle > timestamp的情况）那么不能也更新messageIdCycle 的时间戳，使用Cycle中MessageId+1
            if (timestampInCycle >= timestamp) {
                long sequence = messageIdInCycle & SEQUENCE_MASK;
                if (sequence >= SEQUENCE_MASK) {
                    // 达到一秒内序列号上限
                    LOGGER.debug("over sequence mask :{}", sequence);
                    Thread.yield();
                    continue;
                }
                long messageId = messageIdInCycle + 1L;
                // 使用CAS的方式保证在该条件下，messageId 不被重复
                if (messageIdCycle.compareAndSet(index, messageIdInCycle, messageId)) {
                    return messageId;
                }
                LOGGER.debug("messageId cycle CAS2 failed");
            }
            // 整个生成过程中，采用的spinLock
        } while (true);
    }

}