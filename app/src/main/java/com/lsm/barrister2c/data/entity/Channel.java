package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * 学习中心频道
 */
public class Channel implements Serializable {
    public String id;
    public String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}