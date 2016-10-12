package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/10/10.
 * 41.+债权债务信息详情接口
 *   creditDebtInfoDetail
 *   提交方式：post
 *   参数:userId,verifyCode,id
 *   返回值：resultCode，resultMsg, CreditDebtInfo detail
 *   备注：人的详情接口，这个人下面的所有债权债务信息
 */
public class CreditDebtInfoDetailReq extends Action {

    String id;

    public CreditDebtInfoDetailReq(Context context, String id) {
        super(context);
        this.id = id;
        addUserParams();
    }

    @Override
    public String getName() {
        return CreditDebtInfoDetailReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_CREDITDEBTINFODETAIL;
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
