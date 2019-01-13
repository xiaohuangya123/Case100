package com.xhj.case01_pic_cache.entity;

/**
 * Author: Created by XHJ on 2019/1/11.
 * 种一棵树最好的时间是十年前，其次是现在。
 */
public class PicObj {
    private String name;
    private int imageId;

    public PicObj(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
