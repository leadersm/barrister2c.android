package com.lsm.barrister2c.data.io.app;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/5/8.
 * 搜索列表接口
 * <p/>
 * myOrderList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize,type(IM即时,APPOINTMENT预约)
 *   返回值：resultCode，resultMsg , List<OrderItem> orderItems 订单列表 , total（总数，翻页计算用）；
 *   备注：无
 */
public class SearchBarristerReq extends Action{

    int page;

    public static int pageSize = 20;

    public SearchBarristerReq(Context context,String lawyerName, int page) {
        super(context);
        this.page = page;

        params("page",String.valueOf(page));
        params("pageSize",String.valueOf(pageSize));

        if(!TextUtils.isEmpty(lawyerName)){
            params("lawyerName",lawyerName);
        }
//        addUserParams();
    }

    @Override
    public String getName() {
        return SearchBarristerReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_GET_BARRISTER_LIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.GetBarristerListResult result = getFromGson(json,new TypeToken<IO.GetBarristerListResult>(){});//Test.getBarristerResult(20);//

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
