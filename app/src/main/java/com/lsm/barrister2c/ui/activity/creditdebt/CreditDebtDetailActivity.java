package com.lsm.barrister2c.ui.activity.creditdebt;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.creditdebt.CreditDebtInfoDetailReq;
import com.lsm.barrister2c.ui.activity.BaseActivity;

import static android.R.attr.type;

/**
 * Created by lvshimin on 16/10/10.
 * 详情
 * String type;//债类型
 * float money;//债的金额
 * String desc;//债务描述
 * <p>
 * String addTime;//上传时间
 * String updateTime;//更新时间
 * <p>
 * String creditDebtTime;//债形成的时间
 * String creditDebtStatus;//债的状态
 * String status;//信息状态,发布(审核通过)、未发布(刚上传)、已删除(垃圾信息)
 * <p>
 * String proofName;//凭证类型名称(合同、协议、欠条等)
 * String proof;//凭证
 * String judgeDocumentName;//判决文书类型名称(判决书、调解书、仲裁书)
 * String judgeDocument;//判决文书
 * <p>
 * CreditDebtUser creditUser;//债权人
 * CreditDebtUser debtUser;//债务人
 */
public class CreditDebtDetailActivity extends BaseActivity {

    AQuery aq;

    CreditDebtInfo item;

    boolean isPurchased = false;//是否已购买

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_detail);
        aq = new AQuery(this);

        item = (CreditDebtInfo) getIntent().getSerializableExtra("item");

        setupToolbar();

        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_credit_detail);
    }

    CreditDebtInfoDetailReq detailReq;

    private void init() {

        detailReq = new CreditDebtInfoDetailReq(this, item.getId());
        detailReq.execute(new Action.Callback<IO.CreditDebtDetailResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
//                UIHelper.showToast(getApplicationContext(),msg);
            }

            @Override
            public void onCompleted(IO.CreditDebtDetailResult result) {
                bind(result);
            }
        });

        bindHide();

    }

    /**
     * 展示部分信息
     */
    private void bindHide() {
        String type;
        if (item.getType().equals(CreditDebtInfo.TYPE_CONTRACT)) {
            type = "合同欠款";
        } else if (item.getType().equals(CreditDebtInfo.TYPE_BORROW_MONEY)) {
            type = "借款";
        } else if (item.getType().equals(CreditDebtInfo.TYPE_TORT)) {
            type = "侵权";
        } else if (item.getType().equals(CreditDebtInfo.TYPE_LABOR_DISPUTES)) {
            type = "劳动与劳务";
        } else {
            type = "其它";
        }

        //类型
        aq.id(R.id.tv_credit_type).text(type);

        String status;
        if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
            status = "未起诉";
        } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
            status = "诉讼中";
        } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
            status = "执行中";
        } else {
            status = "已过时效";
        }

        //状态
        aq.id(R.id.tv_credit_status).text(status);

        aq.id(R.id.tv_credit_desc).text("描述信息:******");//item.getDesc()
        aq.id(R.id.tv_credit_money).text(String.valueOf(item.getMoney()));
        aq.id(R.id.tv_credit_time).text(item.getUpdateTime());
        aq.id(R.id.tv_credit_addtime).text(item.getAddTime());

        //债权人信息============
        aq.id(R.id.tv_credit_user_name).text("联系人:" + "******");
        aq.id(R.id.tv_credit_user_phone).text("电话:" + "******");
        aq.id(R.id.tv_credit_user_id_number).text("联系身份证号码:" + "******");
        aq.id(R.id.tv_credit_user_company).text("公司名称:" + "******");
        aq.id(R.id.tv_credit_company_phone).text("公司电话:" + "******");
        aq.id(R.id.tv_credit_liscense_number).text("信用代码:" + "******");
        //债务人信息============
        aq.id(R.id.tv_debt_user_name).text("联系人:" + "******");
        aq.id(R.id.tv_debt_user_phone).text("电话:" + "******");
        aq.id(R.id.tv_debt_user_id_number).text("联系身份证号码:" + "******");
        aq.id(R.id.tv_debt_user_company).text("公司名称:" + "******");
        aq.id(R.id.tv_debt_company_phone).text("公司电话:" + "******");
        aq.id(R.id.tv_debt_liscense_number).text("信用代码:" + "******");
    }

    /**
     * 展示全部信息
     *
     * @param result
     */
    private void bind(IO.CreditDebtDetailResult result) {
        //类型
        aq.id(R.id.tv_credit_type).text(type);

        String status;
        if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
            status = "未起诉";
        } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
            status = "诉讼中";
        } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
            status = "执行中";
        } else {
            status = "已过时效";
        }

        //状态
        aq.id(R.id.tv_credit_status).text(status);

        aq.id(R.id.tv_credit_desc).text("描述信息:******");//item.getDesc()
        aq.id(R.id.tv_credit_money).text(String.valueOf(item.getMoney()));
        aq.id(R.id.tv_credit_time).text(item.getUpdateTime());
        aq.id(R.id.tv_credit_addtime).text(item.getAddTime());

        //债权人信息============
        CreditDebtUser creditUser = result.detail.getCreditUser();
        if (creditUser != null) {
            aq.id(R.id.tv_credit_user_name).text("联系人:" + creditUser.getName());
            aq.id(R.id.tv_credit_user_phone).text("电话:" + creditUser.getPhone());
            aq.id(R.id.tv_credit_user_id_number).text("联系身份证号码:" + creditUser.getID_number());
            aq.id(R.id.tv_credit_user_company).text("公司名称:" + creditUser.getCompany());
            aq.id(R.id.tv_credit_company_phone).text("公司电话:" + creditUser.getCompanyPhone());
            aq.id(R.id.tv_credit_liscense_number).text("信用代码:" + creditUser.getLicenseNuber());
        }

        //债务人信息============
        CreditDebtUser debtUser = result.detail.getDebtUser();
        if (creditUser != null) {
            aq.id(R.id.tv_debt_user_name).text("联系人:" + debtUser.getName());
            aq.id(R.id.tv_debt_user_phone).text("电话:" + debtUser.getPhone());
            aq.id(R.id.tv_debt_user_id_number).text("联系身份证号码:" + debtUser.getID_number());
            aq.id(R.id.tv_debt_user_company).text("公司名称:" + debtUser.getCompany());
            aq.id(R.id.tv_debt_company_phone).text("公司电话:" + debtUser.getCompanyPhone());
            aq.id(R.id.tv_debt_liscense_number).text("信用代码:" + debtUser.getLicenseNuber());
        }

    }


}
