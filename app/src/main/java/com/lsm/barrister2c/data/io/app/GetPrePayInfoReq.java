package com.lsm.barrister2c.data.io.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lsm.barrister2c.data.io.Request;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lvshimin on 16/3/28.
 */
public class GetPrePayInfoReq extends Request{


    public GetPrePayInfoReq(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GetPrePayInfoReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return null;
    }

    @Override
    public HashMap<String, Object> params() {
        return null;
    }

    @Override
    public PayReq parse(String json) throws Exception {

        JSONObject jsonObject = new JSONObject(json);

        if(null != jsonObject && !jsonObject.has("retcode") ){
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId			= jsonObject.getString("appid");
            req.partnerId		= jsonObject.getString("partnerid");
            req.prepayId		= jsonObject.getString("prepayid");
            req.nonceStr		= jsonObject.getString("noncestr");
            req.timeStamp		= jsonObject.getString("timestamp");
            req.packageValue	= jsonObject.getString("package");
            req.sign			= jsonObject.getString("sign");
            req.extData			= "app data"; // optional

            return req;
        }else{
            Log.d("PAY_GET", "返回错误" + jsonObject.getString("retmsg"));
            Toast.makeText(context, "返回错误"+jsonObject.getString("retmsg"), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    public HashMap<String, String> headers() {
        return null;
    }

    @Override
    public boolean isGTCReq() {
        return false;
    }
}
