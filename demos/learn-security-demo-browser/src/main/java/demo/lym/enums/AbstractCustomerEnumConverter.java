package demo.lym.enums;

import javax.persistence.AttributeConverter;

/**
 * @param <ATTR> 实体类中枚举的类型（需实现{@link CustomerPersistentEnum} 接口）
 * @param <PERSIST_TYPE>   保存到数据库的数据类型
 * @author peter
 * date: 2019-05-15 16:59
 */
public abstract class AbstractCustomerEnumConverter<ATTR extends Enum<ATTR> & CustomerPersistentEnum<PERSIST_TYPE>, PERSIST_TYPE> implements AttributeConverter<ATTR, PERSIST_TYPE> {

    private final Class<ATTR> clazz;

    public AbstractCustomerEnumConverter(Class<ATTR> clazz) {
        this.clazz = clazz;
    }

    @Override
    public PERSIST_TYPE convertToDatabaseColumn(ATTR attribute) {
        return attribute != null ? attribute.toPersistent() : null;
    }

    @Override
    public ATTR convertToEntityAttribute(PERSIST_TYPE dbData) {
        if (dbData == null) {
            return null;
        }

        ATTR[] enums = clazz.getEnumConstants();

        for (ATTR e : enums) {
            if (e.toPersistent().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException("枚举转化异常。枚举【" + clazz.getSimpleName() + "】,数据库库中的值为：【" + dbData + "】");
    }

}