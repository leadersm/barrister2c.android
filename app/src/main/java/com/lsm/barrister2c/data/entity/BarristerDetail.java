package com.lsm.barrister2c.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvshimin on 16/5/19.
 * -------------律师详情--------
 * 头像
 * 姓名
 * 评分
 * 最近服务次数
 * 律师事务所
 * 工作年限
 * 专长
 * 律师可预约的时段
 * 律师当前接单状态
 */
public class BarristerDetail implements Serializable{

    public static final String ORDER_STATUS_CAN = "can";//可以接单
    public static final String ORDER_STATUS_NOT = "can_not";//不可以接单

    String id;
    String userIcon;//头像
    String name;//姓名
    float rating;//评分
    int recentServiceTimes;//最近服务次数
    String area;
    String company;//律所
    String workYears;//工作开始时间（yyyy-MM-dd），根据这个时间计算工作年限
    List<BusinessArea> bizAreas;//擅长领域

    String intro;//简介

//    * 律师可预约的时段
    List<AppointmentSetting> appointmentSettings;
//    * 律师当前接单状态
    String status;

    float price = 15.0f;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

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

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public List<BusinessArea> getBizAreas() {
        return bizAreas;
    }

    public void setBizAreas(List<BusinessArea> bizAreas) {
        this.bizAreas = bizAreas;
    }

    public List<AppointmentSetting> getAppointmentSettings() {
        return appointmentSettings;
    }

    public void setAppointmentSettings(List<AppointmentSetting> appointmentSettings) {
        this.appointmentSettings = appointmentSettings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
