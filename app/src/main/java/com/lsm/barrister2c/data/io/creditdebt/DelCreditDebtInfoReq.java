package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/10/10.
 * 37.+删除接口
 *   delCreditDebtInfo
 *   提交方式：post
 *   参数:userId,verifyCode,id
 *   返回值：resultCode，resultMsg
 *   备注：我上传列表，长按对象删除（审核通过不让删除）
 */

public class DelCreditDebtInfoReq extends Action {

    String id;

    public DelCreditDebtInfoReq(Context context, String id) {
        super(context);
        this.id = id;
        addUserParams();
    }

    @Override
    public String getName() {
        return DelCreditDebtInfoReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_DEL_CREDITDEBTINFO;
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
