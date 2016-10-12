package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/10/10.
 * 42.+下单接口(返回债权债务信息详情)
 *   buyCreditDebtDetail
 *   提交方式：post
 *   参数:userId,verifyCode,id（债券债务信息记录id）
 *   返回值：resultCode，resultMsg,CreditDebtInfo detail
 *   备注：购买成功直接返回详细信息（债券债务人的全部详细信息）
 */
public class BuyCreditDebtDetailReq extends Action {

    String id;

    public BuyCreditDebtDetailReq(Context context, String id) {
        super(context);
        this.id = id;
        addUserParams();
    }

    @Override
    public String getName() {
        return BuyCreditDebtDetailReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_BUY_CREDITDEBTDETAIL;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.CreditDebtDetailResult result = getFromGson(json,new TypeToken<IO.CreditDebtDetailResult>(){});
        if(result!=null && result.resultCode == 200){
            onSafeCompleted(result);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
