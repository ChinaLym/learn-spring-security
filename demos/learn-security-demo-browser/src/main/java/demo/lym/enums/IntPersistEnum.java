package demo.lym.enums;

import javax.validation.constraints.NotNull;

public interface IntPersistEnum {

    /**
     * 转成成数据库里存储的值
     */
    @NotNull Integer toPersist();

}