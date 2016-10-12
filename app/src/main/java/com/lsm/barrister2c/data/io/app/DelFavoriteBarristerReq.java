package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 取消收藏律师接口
   addFavoriteBarrister
   提交方式：post
   参数:userId,verifyCode,lawyerId 律师id
   返回值：resultCode，resultMsg ；
   备注：
 */
public class DelFavoriteBarristerReq extends Action {

    String id;//barrister id

    public DelFavoriteBarristerReq(Context context, String id) {
        super(context);
        this.id = id;

        params("lawyerId",id);

        addUserParams();

    }

    @Override
    public String getName() {
        return DelFavoriteBarristerReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_DEL_FAVORITE_BARRISTER;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        CommonResult result = getFromGson(json, new TypeToken<CommonResult>() {});

        if(result!=null){

            if(result.resultCode == 200){

                onSafeCompleted(true);

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
