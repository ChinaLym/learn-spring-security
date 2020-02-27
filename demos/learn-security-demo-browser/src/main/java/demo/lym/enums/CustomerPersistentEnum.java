package demo.lym.enums;

public interface CustomerPersistentEnum<PERSIST_TYPE> {

    /**
     * 转成成数据库里存储的值
     */
    PERSIST_TYPE toPersistent();

}