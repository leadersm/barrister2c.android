package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 我的订单列表接口
 * <p/>
 * myOrderList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize,type(IM即时,APPOINTMENT预约)
 *   返回值：resultCode，resultMsg , List<OrderItem> orderItems 订单列表 , total（总数，翻页计算用）；
 *   备注：无
 */
public class GetBarristerListReq extends Action{

    public static final String TYPE_IM = "IM";
    public static final String TYPE_APPOINTMENT = "APPOINTMENT";

    String type;
    int page;

    public static int pageSize = 20;

    public GetBarristerListReq(Context context, int page, String type) {
        super(context);
        this.page = page;
        this.type = type;

        params("type",type);
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

        addUserParams();
    }

    @Override
    public String getName() {
        return GetBarristerListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.TEST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetBarristerListResult result = Test.getBarristerResult(20);//getFromGson(json,new TypeToken<IO.GetBarristerListResult>(){});

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
        return POST;
    }
}
