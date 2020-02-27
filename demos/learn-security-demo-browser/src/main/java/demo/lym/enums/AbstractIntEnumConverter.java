package demo.lym.enums;

import javax.persistence.AttributeConverter;
import javax.validation.constraints.NotNull;

/**
 * @param <ENUM_ATTR> 实体类中枚举的类型（需实现{@link IntPersistEnum} 接口）
 */
public abstract class AbstractIntEnumConverter<ENUM_ATTR extends Enum<ENUM_ATTR> & IntPersistEnum> implements AttributeConverter<ENUM_ATTR, Integer> {

    private final Class<ENUM_ATTR> clazz;

    public AbstractIntEnumConverter(Class<ENUM_ATTR> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Integer convertToDatabaseColumn(ENUM_ATTR attribute) {
        return attribute != null ? attribute.toPersist()
                : getDefault().toPersist();
    }

    @Override
    public ENUM_ATTR convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            // 存储时候用 default，因此这里也应该是 default
            throw new IllegalStateException("dbData is null");
            // return getDefault();
        }

        ENUM_ATTR[] enums = clazz.getEnumConstants();

        for (ENUM_ATTR e : enums) {
            if (e.toPersist().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException("枚举转化异常。枚举【" + clazz.getSimpleName() + "】,数据库库中的值为：【" + dbData + "】");
    }

    @NotNull
    protected abstract ENUM_ATTR getDefault();
}