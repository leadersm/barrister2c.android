package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * +订单评分
 *   addOrderStar
 *   提交方式：post
 *   参数:userId,verifyCode,orderId,star,comment
 *   返回值：resultCode，resultMsg
 *   备注：
 */
public class AddOrderStarReq extends Action {

    String orderId;
    float star;
    String comment;

    public AddOrderStarReq(Context context, String orderId, float star, String comment) {
        super(context);
        this.orderId = orderId;
        this.star = star;
        this.comment = comment;

        params("orderId", orderId);
        params("star", star+"");
        params("comment", comment);

        addUserParams();

    }

    @Override
    public String getName() {
        return AddOrderStarReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_ADD_ORDER_STAR;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        CommonResult result = parseCommonResult(json);
        if (result != null && result.resultCode == 200) {
            onSafeCompleted(true);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
