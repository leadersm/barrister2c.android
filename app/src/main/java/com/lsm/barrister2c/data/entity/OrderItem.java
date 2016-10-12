package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 订单列表Item
 */
public class OrderItem implements Serializable{

    public static final String TYPE_IM = "IM";//即时
    public static final String TYPE_APPOINTMENT = "APPOINTMENT";//预约
    public static final String TYPE_ONLINE = "ONLINE";//在线业务咨询

    String id;
    String type;//订单类型：即时、预约、在线咨询
    String userIcon;//用户头像
    String name;//用户昵称
    String date;//日期
    String status;//订单状态
    String caseType;//案件类型：财产纠纷，离婚，……
    String phone;

    //=======================
    String qq;//在线咨询-客服qq
    String payStatus;//支付状态，0,1 0未支付，1已支付
    double paymentAmount;//支付金额
    String orderNo;//订单号

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
