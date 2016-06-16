package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/19.
 * -------------法律顾问--------
 * 头像
 * 姓名
 * 评分
 * 最近服务次数
 * 律师事务所
 * 工作年限
 * 专长
 */
public class Barrister implements Serializable{


    String id;
    String userIcon;//头像
    String name;//姓名
    float rating;//评分
    int recentServiceTimes;//最近服务次数
    String area;
    String company;//律所
    String workingStartYear;//工作开始时间（yyyy-MM-dd），根据这个时间计算工作年限
    String goodAt;//擅长领域

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRecentServiceTimes() {
        return recentServiceTimes;
    }

    public void setRecentServiceTimes(int recentServiceTimes) {
        this.recentServiceTimes = recentServiceTimes;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWorkingStartYear() {
        return workingStartYear;
    }

    public void setWorkingStartYear(String workingStartYear) {
        this.workingStartYear = workingStartYear;
    }

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }
}
