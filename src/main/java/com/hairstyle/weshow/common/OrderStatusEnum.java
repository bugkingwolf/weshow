package com.hairstyle.weshow.common;

public enum OrderStatusEnum {
    WAIT_PAY(1, "待付款"),
    WATI_CONFIRM(2, "待理发师确认"),
    APPOINMENT_SUCCESS(3, "预约成功"),
    REFUSE(4, "理发师拒绝"),
    SUCCESS(5, "订单完成");


    private Integer code;
    private String info;

    public static OrderStatusEnum valueOf(int code) {
        switch (code) {
            case 1:
                return WAIT_PAY;
            case 2:
                return WATI_CONFIRM;
            case 3:
                return APPOINMENT_SUCCESS;
            case 4:
                return REFUSE;
            case 5:
                return SUCCESS;
        }
        return null;
    }

    public static String getInfo(Integer code) {
        if (null == code) {
            return "";
        }
        switch (code) {
            case 1:
                return WAIT_PAY.getInfo();
            case 2:
                return WATI_CONFIRM.getInfo();
            case 3:
                return APPOINMENT_SUCCESS.getInfo();
            case 4:
                return REFUSE.getInfo();
            case 5:
                return SUCCESS.getInfo();
        }
        return "";
    }

    OrderStatusEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
