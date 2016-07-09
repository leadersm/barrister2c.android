package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * requestCancelOrder
   提交方式：post
   参数:userId,verifyCode,orderId
   返回值：resultCode，resultMsg
   备注：过了预约时间没有通话记录，用户选择取消订单，后台将订单状态变为 “请求取消”
 */
public class RequestCancelOrderReq extends Action{

    String orderId;

    public RequestCancelOrderReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId",orderId);

        addUserParams();

    }

    @Override
    public String getName() {
        return RequestCancelOrderReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_REQUEST_CANCEL_ORDER;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        if(result!=null && result.resultCode==200){
            onSafeCompleted(true);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
