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
public class RewardOrderReq extends Action{

    String orderId;

    float money;

    public RewardOrderReq(Context context, String orderId,float money) {
        super(context);
        this.orderId = orderId;
        this.money = money;

        params("orderId",orderId);
        params("money",money+"");

        addUserParams();

    }

    @Override
    public String getName() {
        return RewardOrderReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_REWARD_ORDER;
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
