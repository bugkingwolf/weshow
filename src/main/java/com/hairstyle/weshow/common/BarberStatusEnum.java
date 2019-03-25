package com.hairstyle.weshow.common;

public enum BarberStatusEnum {
    RESTING(1, "休息中"),
    FREEING(2, "空闲中"),
    HAIRCUTING(3, "理发中");


    private Integer code;
    private String info;

    public static BarberStatusEnum valueOf(int code) {
        switch (code) {
            case 1:
                return RESTING;
            case 2:
                return FREEING;
            case 3:
                return HAIRCUTING;
        }
        return null;
    }

    public static String getInfo(Integer code) {
        if (null == code) {
            return "";
        }
        switch (code) {
            case 1:
                return RESTING.getInfo();
            case 2:
                return FREEING.getInfo();
            case 3:
                return HAIRCUTING.getInfo();
        }
        return "";
    }

    BarberStatusEnum(int code, String info) {
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
