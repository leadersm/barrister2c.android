package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * myFavoriteList
   提交方式：post
   参数:userId,verifyCode，page,pageSize
   返回值：resultCode，resultMsg ；List<FavoriteItem> favoriteItemList
   备注：
 */
public class GetMyFavoriteListReq extends Action{

    int page = 1;
    public static int pageSize = 20;

    public GetMyFavoriteListReq(Context context, int page) {
        super(context);
        this.page = page;

        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

        addUserParams();

    }

    @Override
    public String getName() {
        return GetMyFavoriteListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_MY_FAVORITE;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetMyFavoriteListResult result = getFromGson(json, new TypeToken<IO.GetMyFavoriteListResult>() {});

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
