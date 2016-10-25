package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/10/10.
 * 36.+ 我上传的列表接口
 *   myUploadCreditDebtList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize
 *   返回值：resultCode，resultMsg ，List<CreditDebtInfo> list
 *   备注：
 */
public class MyUploadCreditDebtListReq extends Action {

    int page;
    int pageSize = 20;

    public MyUploadCreditDebtListReq(Context context, int page) {
        super(context);
        this.page = page;

        addUserParams();

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));

    }

    @Override
    public String getName() {
        return MyUploadCreditDebtListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_MY_UPLOADCREDITDEBTLIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.CreditDebtListResult result = getFromGson(json,new TypeToken<IO.CreditDebtListResult>(){});
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
