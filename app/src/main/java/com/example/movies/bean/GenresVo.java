package com.example.movies.bean;

public class GenresVo {
    private Integer id;
    private Integer img;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GenresVo(Integer id, Integer img, String name) {
        this.id = id;
        this.img = img;
        this.name = name;
    }
}
