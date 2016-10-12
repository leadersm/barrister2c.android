package com.lsm.barrister2c.ui.activity;

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
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.PayOnlineOrderReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.utils.OrderUtils;

import java.util.Locale;

/**
 * Created by lvshimin on 16/8/20.
 */
public class OnlineOrderDetailActivity extends BaseActivity{


    OrderItem item;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order);

        setupToolbar();

        item = (OrderItem) getIntent().getSerializableExtra("item");
        aq = new AQuery(this);

        bind();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_online_detail);
    }

    private void bind() {

        String status = OrderUtils.getPayStatusString(item.getPayStatus());
        int statusColor = OrderUtils.getPayStatusColor(item.getPayStatus());

        //status
        aq.id(R.id.tv_order_status).text(status).textColor(statusColor);

        //订单号
        aq.id(R.id.tv_order_num).text("订单号："+(TextUtils.isEmpty(item.getOrderNo())?"":item.getOrderNo()));

        //下单时间
        aq.id(R.id.tv_order_time).text(item.getDate());

        //支付金额
        aq.id(R.id.tv_order_payment).text("￥" + item.getPaymentAmount());

        //案件类型
        aq.id(R.id.tv_order_case_type).text("类型:在线业务咨询");

        //律师姓名
//        aq.id(R.id.tv_order_nickname).text(mDetail.getBarristerNickname());

        //律师电话
        aq.id(R.id.tv_order_phone_number).text("点击拨打电话");//.text(TextHandler.getHidePhone(mDetail.getBarristerPhone()));

        //头像
//        SimpleDraweeView sdv = (SimpleDraweeView) aq.id(R.id.image_order_custom_icon).getView();
//        if(!TextUtils.isEmpty(item.getBarristerIcon())){
//            sdv.setImageURI(Uri.parse(mDetail.getBarristerIcon()));
//        }

        if(item.getPayStatus().equals("0")){//待支付

            aq.id(R.id.btn_order_pay).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付
                    tryToPay();
                }
            }).visible();

        }else{//已支付
            aq.id(R.id.btn_order_pay).gone();
        }

        //客服qq
        aq.id(R.id.tv_online_qq).text(item.getName()+":"+item.getQq());
        aq.id(R.id.btn_order_qq).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //QQ
                UIHelper.startQQ(OnlineOrderDetailActivity.this,item.getQq());
            }
        });

        //客服电话
        aq.id(R.id.tv_online_phone).text(item.getName()+":"+item.getPhone());
        aq.id(R.id.btn_order_phone).clicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //打电话
                UIHelper.showCallView(OnlineOrderDetailActivity.this,item.getPhone());
            }
        });
    }

    boolean isLoading = false;

    private void tryToPay() {

        String msg = String.format(Locale.CHINA,getString(R.string.fmt_pay_online),item.getPaymentAmount());

        new AlertDialog.Builder(this).setTitle(R.string.tip).setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        doPayOnline();

                    }}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();


    }

    private void doPayOnline() {

        Account account = UserHelper.getInstance().getAccount();

        if(account==null){
            UIHelper.showToast(getApplicationContext(),"获取账户信息失败.");
            return;
        }

        float remainingBalance =  account.getRemainingBalance();
        if(remainingBalance<50.0f){
            UIHelper.showToast(getApplicationContext(),"您的账户余额不足，请充值");
            return;
        }

        if(isLoading)
            return;

        new PayOnlineOrderReq(this,item.getId()).execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                isLoading = true;
            }

            @Override
            public void onError(int errorCode, String msg) {
                isLoading = false;
                UIHelper.showToast(getApplicationContext(),"支付失败:"+msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                isLoading = false;
                UIHelper.showToast(getApplicationContext(),"支付成功");
                updatePayStatus();
            }
        });
    }

    private void updatePayStatus() {
        item.setPayStatus("1");
        String status = OrderUtils.getPayStatusString(item.getPayStatus());
        int statusColor = OrderUtils.getPayStatusColor(item.getPayStatus());

        //status
        aq.id(R.id.tv_order_status).text(status).textColor(statusColor);

        aq.id(R.id.btn_order_pay).gone();
    }
}
