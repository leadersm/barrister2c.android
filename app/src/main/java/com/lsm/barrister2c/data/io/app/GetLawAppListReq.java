package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.Test;

/**
 * Created by lvshimin on 16/5/8.
 * 学习中心列表接口
 * <p/>
 * getStudyList
 *   提交方式：get
 *   参数:page,pageSize
 *   返回值：resultCode，resultMsg , List<Item> items；total
 *   备注：无
 */
public class GetLawAppListReq extends Action{

    public GetLawAppListReq(Context context) {
        super(context);

        addUserParams();
    }

    @Override
    public String getName() {
        return GetLawAppListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.TEST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetLawAppListResult result = Test.getLawAppListResult(12);//getFromGson(json, new TypeToken<IO.GetLawAppListResult>() {});

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
