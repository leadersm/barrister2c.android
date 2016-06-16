package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 收入明细列表接口
 * <p/>
 * getIncomeDetailList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize
 *   返回值：resultCode，resultMsg , List<IncomeDetail> incomeDetails 收入|支出列表；
 *   备注：点击跳转订单详情
 */
public class GetConsumeDetailListReq extends Action{

    int page = 1;
    int pageSize = 20;

    public GetConsumeDetailListReq(Context context, int page) {
        super(context);
        this.page = page;
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));
        addUserParams();
    }

    @Override
    public String getName() {
        return GetConsumeDetailListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_CONSUME_DETAIL_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetConsumeDetailListResult result = Test.getConsumeDetailListResult();//getFromGson(json, new TypeToken<IO.GetIncomeDetailListResult>() {});

        if(result!=null){

            if(result.resultCode == 200 ){

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
