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

    public SearchCreditDebtListReq(Context context, int page) {
        super(context);
        this.page = page;

        addUserParams();

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));
    }

    //金额范围，
    String startMoney;
    String endMoney;
    //时间范围
    String startDate;
    String endDate;
    //债状态，
    String creditDebtStatus;
    // area地区
    String area;

    public SearchCreditDebtListReq(Context context, int page, String key, String keyType, String userType, String startMoney, String endMoney, String startDate, String endDate, String creditDebtStatus, String area) {
        super(context);
        this.page = page;
        this.key = key;
        this.keyType = keyType;
        this.userType = userType;
        this.startMoney = startMoney;
        this.endMoney = endMoney;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creditDebtStatus = creditDebtStatus;
        this.area = area;

        addUserParams();

        params("page", String.valueOf(page));
        params("pageSize", String.valueOf(pageSize));

        if (!TextUtils.isEmpty(key))
            params("key", key);
        if (!TextUtils.isEmpty(keyType))
            params("keyType", keyType);
        if (!TextUtils.isEmpty(userType))
            params("userType", userType);
        //高级条件
        if (!TextUtils.isEmpty(startMoney))
            params("startMoney", startMoney);
        if (!TextUtils.isEmpty(endMoney))
            params("endMoney", endMoney);
        if (!TextUtils.isEmpty(startDate))
            params("startDate", startDate);
        if (!TextUtils.isEmpty(endDate))
            params("endDate", endDate);
        if (!TextUtils.isEmpty(creditDebtStatus))
            params("creditDebtStatus", creditDebtStatus);
        if (!TextUtils.isEmpty(area))
            params("area", area);

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
