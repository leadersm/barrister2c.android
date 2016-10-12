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

    public static final String TYPE_ALL = "ALL";//全部
    public static final String TYPE_APPOINTMENT = "APPOINTMENT";//预约
    public static final String TYPE_IM = "IM";//即时

    String id;
    String userIcon;//头像
    String name;//姓名
    float rating;//评分
    int recentServiceTimes;//最近服务次数
    String area;
    String company;//律所
    String workYears;//工作开始时间（yyyy-MM-dd），根据这个时间计算工作年限
    List<BusinessArea> bizAreas;//擅长领域
    List<BusinessType> bizTypes;//擅长业务

    String intro;//简介

//  律师当前接单状态
    String orderStatus;

    float priceIM;//即时咨询价格 ,一次
    float priceAppointment;//预约咨询 每个时段价格


    String secretaryQQ;
    String secretaryPhone;

    public String getSecretaryQQ() {
        return secretaryQQ;
    }

    public void setSecretaryQQ(String secretaryQQ) {
        this.secretaryQQ = secretaryQQ;
    }

    public String getSecretaryPhone() {
        return secretaryPhone;
    }

    public void setSecretaryPhone(String secretaryPhone) {
        this.secretaryPhone = secretaryPhone;
    }

    int isExpert = 0;
    public int getIsExpert() {
        return isExpert;
    }

    public void setIsExpert(int isExpert) {
        this.isExpert = isExpert;
    }

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


    public float getPriceIM() {
        return priceIM;
    }

    public void setPriceIM(float priceIM) {
        this.priceIM = priceIM;
    }

    public float getPriceAppointment() {
        return priceAppointment;
    }

    public void setPriceAppointment(float priceAppointment) {
        this.priceAppointment = priceAppointment;
    }

    public List<BusinessType> getBizTypes() {
        return bizTypes;
    }

    public void setBizTypes(List<BusinessType> bizTypes) {
        this.bizTypes = bizTypes;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
