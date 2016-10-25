package com.lsm.barrister2c.ui.activity.creditdebt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.creditdebt.BuyCreditDebtDetailReq;
import com.lsm.barrister2c.data.io.creditdebt.CreditDebtInfoDetailReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.BaseActivity;

import java.util.Locale;

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
                if(errorCode==605){
                    //未购买
                    aq.id(R.id.btn_buy).visible();

                    aq.id(R.id.tv_toolbar_title).text(R.string.buy_credit_detail);

                    showBuyDialog();
                }
            }

            @Override
            public void onCompleted(IO.CreditDebtDetailResult result) {
                aq.id(R.id.tv_toolbar_title).text(R.string.detail);
                bind(result);
            }
        });

        bindHide();

        aq.id(R.id.btn_buy).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBuyDialog();
            }
        }).invisible();

    }

    /**
     * 提示购买
     */
    private void showBuyDialog() {
        new AlertDialog.Builder(CreditDebtDetailActivity.this).setTitle(R.string.tip)
                .setMessage(String.format(Locale.CHINA,"购买此信息详情需要支付:%.2f元",item.getPrice()))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doBuy();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 购买
     */
    private void doBuy() {
        Account account = UserHelper.getInstance().getAccount();
        if (account == null) {
            UIHelper.showToast(getApplicationContext(), "获取账户信息失败，请重新登录");
            return;
        }

        if (account.getRemainingBalance() < item.getPrice()) {
            //余额不足，请充值
            new AlertDialog.Builder(CreditDebtDetailActivity.this)
                    .setTitle("您的账户余额不足请充值")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UIHelper.goRechargeActivity(CreditDebtDetailActivity.this);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

            return;
        }

        new BuyCreditDebtDetailReq(CreditDebtDetailActivity.this, item.getId())
                .execute(new Action.Callback<IO.CreditDebtDetailResult>() {
                    @Override
                    public void progress() {

                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        UIHelper.showToast(getApplicationContext(),"购买失败："+msg);
                    }

                    @Override
                    public void onCompleted(IO.CreditDebtDetailResult result) {
                        aq.id(R.id.btn_buy).invisible();
                        UIHelper.showToast(getApplicationContext(),"购买成功");
                        bind(result);
                    }
                });
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

        if (!TextUtils.isEmpty(item.getCreditDebtStatus())) {
            if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
                status = "未起诉";
            } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
                status = "诉讼中";
            } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
                status = "执行中";
            } else {
                status = "已过时效";
            }
        } else {
            status = "未起诉";
        }


        //状态
        aq.id(R.id.tv_credit_status).text("状态：" + status);

        aq.id(R.id.tv_credit_desc).text("描述信息:******");//item.getDesc()
        aq.id(R.id.tv_credit_money).text("金额：" + String.valueOf(item.getMoney()));
        aq.id(R.id.tv_credit_time).text("形成时间：" + item.getUpdateTime());
        aq.id(R.id.tv_credit_addtime).text("添加时间：" + item.getAddTime());

        //债权人信息============
        aq.id(R.id.tv_credit_user_name).text("联系人:" + "******");
        aq.id(R.id.tv_credit_user_phone).text("电话:" + "******");
        aq.id(R.id.tv_credit_user_id_number).text("联系人身份证号码:" + "******");
        aq.id(R.id.tv_credit_user_company).text("公司名称:" + "******");
        aq.id(R.id.tv_credit_company_phone).text("公司电话:" + "******");
        aq.id(R.id.tv_credit_liscense_number).text("信用代码:" + "******");
        aq.id(R.id.tv_credit_address).text("联系地址:" + "******");
        //债务人信息============
        aq.id(R.id.tv_debt_user_name).text("联系人:" + "******");
        aq.id(R.id.tv_debt_user_phone).text("电话:" + "******");
        aq.id(R.id.tv_debt_user_id_number).text("联系身份证号码:" + "******");
        aq.id(R.id.tv_debt_user_company).text("公司名称:" + "******");
        aq.id(R.id.tv_debt_company_phone).text("公司电话:" + "******");
        aq.id(R.id.tv_debt_liscense_number).text("信用代码:" + "******");
        aq.id(R.id.tv_debt_address).text("联系地址:" + "******");


    }


    public static final String TYPE_CONTRACT = "TYPE_CONTRACT";//合同欠款
    public static final String TYPE_BORROW_MONEY = "TYPE_BORROW_MONEY";//借款
    public static final String TYPE_TORT = "TYPE_TORT";//侵权
    public static final String TYPE_LABOR_DISPUTES = "TYPE_LABOR_DISPUTES";//劳动与劳务
    public static final String TYPE_OTHER= "TYPE_OTHER";//其它

    /**
     * 展示全部信息
     *
     * @param result
     */
    private void bind(IO.CreditDebtDetailResult result) {
        //类型
        String t = result.detail.getType();
        String type;
        if(t.equals(CreditDebtInfo.TYPE_CONTRACT)){
            type = "合同欠款";
        }else if(t.equals(CreditDebtInfo.TYPE_BORROW_MONEY)){
            type = "借款";
        }else if(t.equals(CreditDebtInfo.TYPE_TORT)){
            type = "侵权";
        }else if(t.equals(CreditDebtInfo.TYPE_LABOR_DISPUTES)){
            type = "劳动与劳务";
        }else{
            type = "其他";
        }

        aq.id(R.id.tv_credit_type).text(type);

        String status;
        if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
            status = "未起诉";
        } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
            status = "诉讼中";
        } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
            status = "执行中";
        } else {
            status = "已过时效";
        }

        //状态
        aq.id(R.id.tv_credit_status).text(status);

        aq.id(R.id.tv_credit_desc).text("描述信息:"+ result.detail.getDesc());//item.getDesc()
        aq.id(R.id.tv_credit_money).text("金额：" +String.valueOf(item.getMoney()));
        aq.id(R.id.tv_credit_time).text(item.getUpdateTime());
        aq.id(R.id.tv_credit_addtime).text(item.getAddTime());

        //债权人信息============
        CreditDebtUser creditUser = result.detail.getCreditUser();
        if (creditUser != null) {

            aq.id(R.id.tv_credit_user_name).text("联系人:" + creditUser.getName());

            if(!TextUtils.isEmpty(creditUser.getPhone())){
                aq.id(R.id.tv_credit_user_phone).text("电话:" + creditUser.getPhone());

            }else{
                aq.id(R.id.tv_credit_user_phone).gone();
            }
            if(!TextUtils.isEmpty(creditUser.getID_number())){
                aq.id(R.id.tv_credit_user_id_number).text("联系身份证号码:" + creditUser.getID_number());

            }else{
                aq.id(R.id.tv_credit_user_id_number).gone();
            }
            if(!TextUtils.isEmpty(creditUser.getCompany())){
                aq.id(R.id.tv_credit_user_company).text("公司名称:" + creditUser.getCompany());

            }else{
                aq.id(R.id.tv_credit_user_company).gone();
            }

            if(!TextUtils.isEmpty(creditUser.getCompanyPhone())){
                aq.id(R.id.tv_credit_company_phone).text("公司电话:" + creditUser.getCompanyPhone());
            }else{
                aq.id(R.id.tv_credit_company_phone).gone();
            }

            if(!TextUtils.isEmpty(creditUser.getLicenseNuber())){
                aq.id(R.id.tv_credit_liscense_number).text("信用代码:" + creditUser.getLicenseNuber());

            }else{
                aq.id(R.id.tv_credit_liscense_number).gone();
            }


            aq.id(R.id.tv_credit_address).text("联系地址:" + creditUser.getAddress());
        }

        //债务人信息============
        CreditDebtUser debtUser = result.detail.getDebtUser();
        if (creditUser != null) {
            aq.id(R.id.tv_debt_user_name).text("联系人:" + debtUser.getName());

            if(!TextUtils.isEmpty(debtUser.getPhone())){
                aq.id(R.id.tv_debt_user_phone).text("电话:" + debtUser.getPhone());
            }else{
                aq.id(R.id.tv_debt_user_phone).gone();
            }

            if(!TextUtils.isEmpty(debtUser.getID_number())){
                aq.id(R.id.tv_debt_user_id_number).text("联系身份证号码:" + debtUser.getID_number());
            }else{
                aq.id(R.id.tv_debt_user_id_number).gone();
            }

            if(!TextUtils.isEmpty(debtUser.getCompany())){
                aq.id(R.id.tv_debt_user_company).text("公司名称:" + debtUser.getCompany());
            }else{
                aq.id(R.id.tv_debt_user_company).gone();
            }


            if(!TextUtils.isEmpty(debtUser.getCompanyPhone())){
                aq.id(R.id.tv_debt_company_phone).text("公司电话:" + debtUser.getCompanyPhone());

            }else{
                aq.id(R.id.tv_debt_company_phone).gone();
            }

            if(!TextUtils.isEmpty(debtUser.getLicenseNuber())){
                aq.id(R.id.tv_debt_liscense_number).text("信用代码:" + debtUser.getLicenseNuber());
            }else{
                aq.id(R.id.tv_debt_liscense_number).gone();
            }

            aq.id(R.id.tv_debt_address).text("联系地址:" + debtUser.getAddress());
        }

    }


}
