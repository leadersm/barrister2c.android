package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 * 支付在线业务订单
 */
public class PayOnlineOrderReq extends Action{
    String orderId;

    public PayOnlineOrderReq(Context context, String orderId) {
        super(context);
        this.orderId = orderId;

        params("orderId",orderId);

        addUserParams();
    }

    @Override
    public String getName() {
        return PayOnlineOrderReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_PAY_ONLINE_ORDER;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        return parseCommonResult(json);
    }

    @Override
    public int method() {
        return POST;
    }
}
