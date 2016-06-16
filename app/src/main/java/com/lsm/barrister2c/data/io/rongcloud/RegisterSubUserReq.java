package com.lsm.barrister2c.data.io.rongcloud;

import android.content.Context;
import android.util.Base64;

import com.lsm.barrister2c.app.MD5Tools;
import com.lsm.barrister2c.data.io.Request;
import com.lsm.barrister2c.rongcloud.ECHelper;
import com.lsm.barrister2c.utils.DLog;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lvshimin on 16/3/26.
 */
public class RegisterSubUserReq extends Request {

    String TAG = "RegisterSubUserReq";

    String friendlyName ;

    String Authorization;
    String SigParameter;

    public RegisterSubUserReq(Context context, String friendlyName) {
        super(context);
        this.friendlyName = friendlyName;

        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));

        String sid = ECHelper.ACCOUNT_SID;

        Authorization = Base64.encodeToString((sid + ":"+date).getBytes(),Base64.DEFAULT);

        SigParameter = MD5Tools.MD5(ECHelper.ACCOUNT_SID + ECHelper.AUTH_TOKEN + date);

        DLog.d(TAG, "Authorization:" + Authorization);

        DLog.d(TAG,"SigParameter:"+SigParameter);

    }

//    public void execute(com.zhy.http.okhttp.callback.Callback callback){
//
//        try {
//
//            String url = ECHelper.SERVER + "/2016-03-16/Accounts/"+ECHelper.ACCOUNT_SID+"/SubAccounts?sig="+SigParameter;
//
////        Accept:application/json;
////        Content-Type:application/json;charset=utf-8;
////        Authorization:ZmY4MDgwODEzYzM3ZGE1MzAxM2M4MDRmODA3MjAwN2M6MjAxMzAyMDExNTABCDE=
//
//            OkHttpUtils.post().url(url).addParams("appId", ECHelper.WX_APP_ID).addParams("friendlyName",friendlyName)
//            .addHeader("Content-Type", "application/json;charset=utf-8")
//                    .addHeader("Authorization", Authorization)
//                    .addHeader("Accept", "application/json")
//                    .build().execute(callback);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public String getName() {
        return RegisterSubUserReq.class.getName();
    }

    @Override
    public String url() {
        String url = ECHelper.SERVER + "/2016-03-16/Accounts/"+ECHelper.ACCOUNT_SID+"/SubAccounts?sig="+SigParameter;
        return url;
    }

    @Override
    public HashMap<String, Object> params() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("appId", ECHelper.APP_ID);
        params.put("friendlyName",friendlyName);
        return params;
    }

    @Override
    public HashMap<String, String> headers() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        headers.put("Authorization", Authorization);
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String parse(String json) throws Exception {
        return json;
    }



    @Override
    public boolean isGTCReq() {
        return false;
    }
}
