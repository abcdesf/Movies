package com.example.movies.enums;

public enum CommentEnum {

    A0 ("无评分", 0f),
    A1  ("很差", 1f),
    A2 ("较差", 2f),
    A3 ("还行", 3f),
    A4 ("推荐", 4f),
    A5 ("力荐", 5f),

    ;

    // 成员变量
    private String desc;
    private float code;

    // 构造方法
    private CommentEnum(String desc, float code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(float code) {
        for (CommentEnum c : CommentEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    public static float getCode(String name) {
        for (CommentEnum c : CommentEnum.values()) {
            if (c.getDesc().equals(name)) {
                return c.code;
            }
        }
        return 0f;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getCode() {
        return code;
    }

    public void setCode(float code) {
        this.code = code;
    }


}



