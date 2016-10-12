package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

import java.io.File;

/**
 * Created by lvshimin on 16/10/10.
 * 38.+更新接口(债权、债务信息)
 *   updateCreditDebt
 *   提交方式：post
 *   参数:userId,verifyCode,id,CreditDebtInfo ..
 *   返回值：resultCode，resultMsg
 *   备注：
 */
public class UpdateCreditDebtReq extends Action {

    String id;
    CreditDebtInfo data;

    public UpdateCreditDebtReq(Context context, String id, CreditDebtInfo data, File proof,File judgeDocument) {
        super(context);
        this.id = id;
        this.data = data;

        if(proof!=null)
            addFile("proof",proof);

        if(judgeDocument!=null)
            addFile("judgeDocument",judgeDocument);

        addUserParams();

        params("","");

    }

    @Override
    public String getName() {
        return UpdateCreditDebtReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPDATE_CREDITDEBT;
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
