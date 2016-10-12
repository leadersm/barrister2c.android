package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/8/20.
 *  网页授权接口
 */
public class WebAuthReq extends Action{


    public WebAuthReq(Context context) {
        super(context);
        addUserParams();
    }

    @Override
    public String getName() {
        return WebAuthReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_WEB_AUTH;
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
