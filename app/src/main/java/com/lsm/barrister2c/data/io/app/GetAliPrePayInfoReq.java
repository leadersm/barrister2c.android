package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/3/28.
 * 获取微信预充值信息
 */
public class GetAliPrePayInfoReq extends Action{

    String goodsName;//商品名称
    String goodsInfo;//商品信息
    float money;

    public GetAliPrePayInfoReq(Context context, String goodsName, String goodsInfo, float money) {
        super(context);
        this.goodsInfo = goodsInfo;
        this.goodsName = goodsName;
        this.money = money;

//        params("goodsInfo",goodsInfo);
//        params("goodsName",goodsName);

        params("goodsInfo","CN.DLS.APP2C");
        params("goodsName","CN.DLS.APP2C");

        params("money",String.valueOf(money));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetAliPrePayInfoReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_ALI_PREPAY_INFO;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.AliPrePayResult result = getFromGson(json,new TypeToken<IO.AliPrePayResult>(){});
        if(result.resultCode == 200){
            onSafeCompleted(result.aliPrepayInfo);
            return result;
        }

//        JSONObject jsonObject = new JSONObject(json);
//
//        if(null != jsonObject && !jsonObject.has("retcode") ){
//            PayReq req = new PayReq();
//            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//            req.appId			= jsonObject.getString("appid");
//            req.partnerId		= jsonObject.getString("partnerid");
//            req.prepayId		= jsonObject.getString("prepayid");
//            req.nonceStr		= jsonObject.getString("noncestr");
//            req.timeStamp		= jsonObject.getString("timestamp");
//            req.packageValue	= jsonObject.getString("package");
//            req.sign			= jsonObject.getString("sign");
//            req.extData			= "app data"; // optional
//
//            return req;
//        }else{
//            Log.d("PAY_GET", "返回错误" + jsonObject.getString("retmsg"));
//            Toast.makeText(context, "返回错误"+jsonObject.getString("retmsg"), Toast.LENGTH_SHORT).show();
//        }

        return null;
    }

    @Override
    public int method() {
        return POST;
    }

}
