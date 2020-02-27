package demo.lym.enums;

public enum Sex implements IntPersistEnum {

    MAN(0, "男"),
    WMAN(1, "女");

    private int code;
    private String msg;

    Sex(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public Integer toPersist() {
        return code;
    }


    public static class Converter extends AbstractIntEnumConverter<Sex> {

        public Converter() {
            super(Sex.class);
        }

        @Override
        protected Sex getDefault() {
            return MAN;
        }
    }

}