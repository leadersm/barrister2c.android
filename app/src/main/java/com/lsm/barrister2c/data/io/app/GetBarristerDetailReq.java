package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 订单详情接口
 * <p/>
 * OrderDetail
 *   提交方式：post
 *   参数:userId,orderId,verifyCode
 *   返回值：resultCode，resultMsg , orderDetail；
 *   备注：无
 */
public class GetBarristerDetailReq extends Action{

    String id;

    public GetBarristerDetailReq(Context context, String id) {
        super(context);
        this.id = id;
        params("id",id);
    }

    @Override
    public String getName() {
        return GetBarristerDetailReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_BARRISTER_DETAIL;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.GetBarristerDetailResult result = getFromGson(json, new TypeToken<IO.GetBarristerDetailResult>() {});//Test.getBarristerDetailResult();//

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(result);

            }

            return result;

        }

        return null;
    }

    @Override
    public int method() {
        return GET;
    }
}
