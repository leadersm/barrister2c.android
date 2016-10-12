package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 */
public class GetOnlineBizUserListReq extends Action{

    String bizTypeId;
    int page = 1;
    public static final int pageSize = 10000;

    public GetOnlineBizUserListReq(Context context, String bizId) {
        super(context);
        this.bizTypeId = bizId;
        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));
        params("bizTypeId",bizTypeId);

    }

    @Override
    public String getName() {
        return GetOnlineBizUserListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_ONLINE_BIZ_USER_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        IO.GetOnlineBizUserListResult result = getFromGson(json,new TypeToken<IO.GetOnlineBizUserListResult>(){});
        if(result!=null && result.resultCode==200){
            onSafeCompleted(result);
        }
        return result;
    }

    @Override
    public int method() {
        return GET;
    }
}
