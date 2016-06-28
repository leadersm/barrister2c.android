package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/6/17.
 * 收藏item
 */
public class FavoriteItem implements Serializable{

    public static final String TYPE_BARRISTER = "favorite.type.barrister";

    String id;//如果是律师，就是律师id
    String type;//收藏类型
    String title;
    String desc;
    String url;//如果是收藏的其他东西
    String thumb;//如果律师就是律师头像 ,如果是收藏的其他（如学习中心）就是缩略图 TBD
    String addTime;//添加收藏的时间；

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
