package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

/**
 * Created by lvshimin on 16/10/10.
 * 40.+ 查询接口（已审核过的,显示部分信息）
 *   searchCreditDebtList
 *   提交方式：post
 *   参数:userId,verifyCode,page,pageSize, key, keyType（公司名称company、机构代码licenseNum、姓名name、身份证 idNum）,userType (credit//债权人,debt//债务人)
 *   返回值：resultCode，resultMsg ，List<CreditDebtInfo> list
 *   备注：客户端查询页面》输入框 key》查询实体类型：单选框keyType（公司名称、机构代码、姓名、身份证）》查询类型：单选框userType（credit//债权人,debt//债务人）》查询按钮
 */
public class SearchCreditDebtListReq extends Action {

    public static final String KEY_COMPANY = "company";
    public static final String KEY_LICENSENUM = "licenseNum";
    public static final String KEY_NAME = "name";
    public static final String KEY_IDNUM = "idNum";

    int page;
    int pageSize = 20;

    String key;
    String keyType;//（公司名称company、机构代码licenseNum、姓名name、身份证 idNum）,
    String userType;//(credit//债权人,debt//债务人)

    public SearchCreditDebtListReq(Context context, int page, String key, String keyType, String userType) {
        super(context);
        this.page = page;
        this.key = key;
        this.keyType = keyType;
        this.userType = userType;

        addUserParams();

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));

        if (!TextUtils.isEmpty(key))
            params("key", key);
        if (!TextUtils.isEmpty(keyType))
            params("keyType", keyType);
        if (!TextUtils.isEmpty(userType))
            params("userType", userType);

    }

    @Override
    public String getName() {
        return SearchCreditDebtListReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_SEARCH_CREDITDEBTLIST;
    }

    @Override
    public CommonResult parse(String json) throws Exception {

        IO.CreditDebtListResult result = getFromGson(json, new TypeToken<IO.CreditDebtListResult>() {
        });
        if (result != null && result.resultCode == 200) {
            onSafeCompleted(result);
        }
        return result;
    }

    @Override
    public int method() {
        return POST;
    }
}
