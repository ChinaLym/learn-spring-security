package demo.lym.enums;

public enum PayStatus implements CustomerPersistentEnum<Integer> {

    NOT_PAY(0, "未支付"),
    PAID(1, "已支付");

    private int code;
    private String msg;

    PayStatus(int code, String msg) {
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
    public Integer toPersistent() {
        return code;
    }


    public static class Converter extends AbstractCustomerEnumConverter<PayStatus, Integer> {

        public Converter() {
            super(PayStatus.class);
        }
    }

}