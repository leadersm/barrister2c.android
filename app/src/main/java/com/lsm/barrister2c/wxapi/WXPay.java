package com.lsm.barrister2c.wxapi;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.MD5Tools;
import com.lsm.barrister2c.data.entity.WxPrepayInfo;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付
 */
public class WXPay {


    private static final String TAG = WXPay.class.getSimpleName();

    private IWXAPI api;


    Activity context;

    private static WXPay instance = null;

    private WXPay(){

    }
    public static WXPay getInstance(){
        if(instance == null){

            instance = new WXPay() ;

        }
        return instance;
    }


    public void pay(WxPrepayInfo info){


        if(api == null)
        {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(context, null);

            api.registerApp(Constants.WX_APP_ID);

        }

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(!isPaySupported){
            Toast.makeText(context, "您的手机不支持微信分享", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "不支持微信分享");
//            finish();
            return ;
        }

        String sign = reSign(info);

        PayReq req = new PayReq();
        req.appId = info.getAppid();
        req.nonceStr = info.getNoncestr();
        req.packageValue = info.getPackageValue();
        req.partnerId = info.getPartnerid();
        req.prepayId = info.getPrepayid();
        req.timeStamp = info.getTimestamp();

        req.sign = sign;

        boolean result = api.sendReq(req);

        System.out.println("====>req.send.result:"+result);

    }

    private String reSign(WxPrepayInfo info) {
        String stringA ="appid="+info.getAppid()
                +"&noncestr="+info.getNoncestr()
                +"&package="+info.getPackageValue()
                +"&partnerid="+info.getPartnerid()
                +"&prepayid="+info.getPrepayid()
                +"&timestamp="+info.getTimestamp();

        String stringSignTemp= stringA+"&key=8aac25361765227616fed5718daa3653";

        String sign= MD5Tools.MD5(stringSignTemp).toUpperCase();

        return sign;
    }

    public WXPay init(Activity activity) {
        this.context = activity;

        if(api == null)
        {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(context, null);

            boolean register = api.registerApp(Constants.WX_APP_ID);
            System.out.println("====>register.wx:"+register);

        }

        return instance;
    }


}
