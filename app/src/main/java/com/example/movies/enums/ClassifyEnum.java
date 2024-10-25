package com.example.movies.enums;

import java.util.ArrayList;
import java.util.List;

public enum ClassifyEnum {
    Collect ("收藏", 0),
    History("历史", 1),
    ;

    // 成员变量
    private String desc;
    private int code;

    // 构造方法
    private ClassifyEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (ClassifyEnum c : ClassifyEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取列表
     * @return
     */
    public static List<String> getNameList() {
        List<String> list = new ArrayList<>();
        for (ClassifyEnum statusEnum : ClassifyEnum.values()) {
            list.add(statusEnum.getDesc());
        }
        return list;
    }
    public static List<Integer> getCodeList() {
        List<Integer> list = new ArrayList<>();
        for (ClassifyEnum statusEnum : ClassifyEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}



