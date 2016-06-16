package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/5/8.
 * 预约设置
 * key:date yyyy-MM-dd
 * value:0,1,0,1……对应36个时段的状态
 */
public class AppointmentSetting implements Serializable{

    String date;
    String settings;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }
}
