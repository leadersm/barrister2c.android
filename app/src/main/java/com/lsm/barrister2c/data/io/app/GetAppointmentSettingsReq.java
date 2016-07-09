package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 获取我的预约设置接口
 * getMyAppointmentSettings
 *   提交方式：post
 *   参数:userId,verifyCode,date (当天)
 *   返回值：resultCode，resultMsg , List<AppointmentSetting> appointmentSettings (从当天开始七天的36个时段设置数据)；
 *   备注：6：00~24：00    36个时间段；
 */
public class GetAppointmentSettingsReq extends Action {

    String date;
    String id;//barrister id

    public GetAppointmentSettingsReq(Context context,String id, String date) {
        super(context);
        this.date = date;
        this.id = id;

        params("date",date);
        params("lawyerId",id);

        addUserParams();

    }

    @Override
    public String getName() {
        return GetAppointmentSettingsReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_APPOINTMENT_SETTINGS;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetAppointmentSettingsResult result = getFromGson(json, new TypeToken<IO.GetAppointmentSettingsResult>() {});//Test.getAppointmentSettingsResult();//

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result);

            }

            return result;

        }

        return null;
    }

    @Override
    public int method() {
        return POST;
    }
}
