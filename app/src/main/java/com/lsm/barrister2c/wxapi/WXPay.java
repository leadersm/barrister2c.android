package com.lsm.barrister2c.wxapi;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.GetWXPrePayInfoReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.utils.DLog;
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


    public void pay(String goodsName,String goodsInfo,float money){


        if(api == null)
        {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, false);

            api.registerApp(Constants.WX_APP_ID);
        }

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(!isPaySupported){
            Toast.makeText(context, "您的手机不支持微信分享", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "不支持微信分享");
//            finish();
            return ;
        }

        new GetWXPrePayInfoReq(context,goodsName,goodsInfo,money).execute(new Action.Callback<PayReq>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(context,"创建订单失败："+msg);
            }

            @Override
            public void onCompleted(PayReq payReq) {

                if(payReq!=null){
                    api.sendReq(payReq);
                }else{
                    DLog.e(TAG,"返回错误");
                }
            }
        });

    }

    public WXPay init(Activity activity) {
        this.context = activity;
        return instance;
    }


}
