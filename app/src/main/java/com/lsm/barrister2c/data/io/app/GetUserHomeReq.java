package com.lsm.barrister2c.data.io.app;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 用户首页接口
 * <p/>
 * userHome
 *   提交方式：post
 *   参数:userId,verifyCode
 *   resultCode，resultMsg, List<Ad> ads 轮播广告, List<BusinessArea> bizAreas 案件类型（咨询范围）列表,List<BusinessType> bizTypes 业务类型列表
 *   备注：（页面切换）前台获取更新首页信息。
 */
public class GetUserHomeReq extends Action{

    public GetUserHomeReq(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GetUserHomeReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_USER_HOME;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        final IO.HomeResult result = getFromGson(json,new TypeToken<IO.HomeResult>(){});//Test.getHomeResult();//

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
        return GET;
    }
}
