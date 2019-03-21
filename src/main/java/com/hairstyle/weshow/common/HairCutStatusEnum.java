package com.hairstyle.weshow.common;

public enum HairCutStatusEnum {
    COMEING(1, "进店"),
    SHAMPOO(2, "洗发"),
    HAIRCUT(3, "理发"),
    PERM(4, "烫发"),
    HAIRDYE(5, "染发"),
    GETOUT(6, "离店");



    private Integer code;
    private String info;

    public static HairCutStatusEnum valueOf(int code) {
        switch (code) {
            case 1:
                return COMEING;
            case 2:
                return SHAMPOO;
            case 3:
                return HAIRCUT;
            case 4:
                return PERM;
            case 5:
                return HAIRDYE;
            case 6:
            	return GETOUT;
        }
        return null;
    }

    public static String getInfo(Integer code) {
        if (null == code) {
            return "";
        }
        switch (code) {
            case 1:
                return COMEING.getInfo();
            case 2:
                return SHAMPOO.getInfo();
            case 3:
                return HAIRCUT.getInfo();
            case 4:
                return PERM.getInfo();
            case 5:
                return HAIRDYE.getInfo();
            case 6:
            	return GETOUT.getInfo();
        }
        return "";
    }
    
    HairCutStatusEnum(int code, String info) {
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
