package com.lsm.barrister2c.data.io.app;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 打电话接口
 * 提交方式：post
   参数：userId,verifyCode,orderId
   返回值：resultCode，resultMsg；
   备注：后台查订单中的律师和用户电话给双方建立通话
 */
public class MakeOrderReq extends Action{

    String orderInfo;
    float price;
    String remarks;

    public MakeOrderReq(Context context, String orderInfo, float price, String remarks,String settings) {
        super(context);
        this.orderInfo = orderInfo;
        this.price = price;
        this.remarks = remarks;

        params("orderInfo",orderInfo);
        params("price",price+"");
        params("orderContent",remarks);

        if(!TextUtils.isEmpty(settings)){
            params("appointmentDate",settings);
        }

        addUserParams();
    }

    @Override
    public String getName() {
        return MakeOrderReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_MAKE_CALL;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.MakeOrderResult result = getFromGson(json, new TypeToken<IO.MakeOrderResult>() {});

        if(result!=null){

            if(result.resultCode == 200 && result.orderDetail!=null){

                onSafeCompleted(result.orderDetail);

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
