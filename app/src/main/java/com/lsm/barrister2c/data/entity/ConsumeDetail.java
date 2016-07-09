package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/20.
 * 支出详情
 */
public class ConsumeDetail implements Serializable{

    public static final String TYPE_ORDER = "income.type.order";//订单
    public static final String TYPE_GET_MONEY = "income.type.getmoney";//提现
    public static final String TYPE_REWARD = "income.type.reward";//打赏
    public static final String TYPE_RECHARGE = "consume.type.recharge";//充值

    String id;
    String serialNum;//流水号
    String money;//金额
    String orderId;//订单id，如果有
    String type;//类型：接受订单、提现、
    String date;//日期


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
