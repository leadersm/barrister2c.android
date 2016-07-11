package com.lsm.barrister2c.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AudioHelper;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.CallHistory;
import com.lsm.barrister2c.data.entity.OrderDetail;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetOrderDetailReq;
import com.lsm.barrister2c.data.io.app.MakeCallReq;
import com.lsm.barrister2c.data.io.app.RequestCancelOrderReq;
import com.lsm.barrister2c.data.io.app.RewardOrderReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.CallHistoryAdapter;
import com.lsm.barrister2c.utils.DateFormatUtils;
import com.lsm.barrister2c.utils.OrderUtils;
import com.lsm.barrister2c.utils.TextHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    AQuery aq;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setupToolbar();

        id = getIntent().getStringExtra("id");

        aq = new AQuery(this);
        aq.id(R.id.btn_call_make).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMakeCall();
            }
        });

        mAddapter = new CallHistoryAdapter(this,calls);

        ListView listView = aq.id(R.id.listview_callhistory).adapter(mAddapter).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放？
            }
        }).getListView();

        listView.setEmptyView(getLayoutInflater().inflate(R.layout.empty_call_history,null));

        loadOrderDetail();
    }

    CallHistoryAdapter mAddapter;
    List<CallHistory> calls = new ArrayList<>();

    /**
     * 获取订单详情
     */
    private void loadOrderDetail() {
        new GetOrderDetailReq(this,id).execute(new Action.Callback<IO.GetOrderDetailResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetOrderDetailResult result) {
                bindOrderDetail(result.orderDetail);
            }
        });
    }


    OrderDetail mDetail;
    private void bindOrderDetail(OrderDetail mDetail) {
        this.mDetail = mDetail;

        String status = OrderUtils.getStatusString(mDetail.getStatus());
        int statusColor = OrderUtils.getStatusColor(mDetail.getStatus());

        //status
        aq.id(R.id.tv_order_status).text(status).textColor(statusColor);

        //订单号
        aq.id(R.id.tv_order_num).text("订单号："+(TextUtils.isEmpty(mDetail.getOrderNo())?"":mDetail.getOrderNo()));

        //下单时间~预约时间
        String time = null;
        if(mDetail.getType().equals(OrderItem.TYPE_APPOINTMENT)){
            time = mDetail.getStartTime() + "~" + mDetail.getEndTime();
        }else{
            time = mDetail.getPayTime();
        }

        aq.id(R.id.tv_order_time).text(time);

        //支付金额
        aq.id(R.id.tv_order_payment).text("￥"+mDetail.getPaymentAmount());

        //案件类型
        aq.id(R.id.tv_order_case_type).text("案例类型:"+(TextUtils.isEmpty(mDetail.getCaseType())?"未知案例类型":mDetail.getCaseType()));

        //备注
        aq.id(R.id.tv_order_remark).text("备注："+mDetail.getRemarks());

        //小结
        if(TextUtils.isEmpty(mDetail.getLawFeedback())){
            aq.id(R.id.tv_order_summary).gone();

        }else {
            aq.id(R.id.tv_order_summary).text("律师总结："+mDetail.getLawFeedback()).visible();
        }

        //评论
        if(TextUtils.isEmpty(mDetail.getComment())){
            aq.id(R.id.tv_order_comment).gone();
        }else {
            aq.id(R.id.tv_order_comment).text("用户评论："+mDetail.getComment()).visible();
        }

        //律师姓名
        aq.id(R.id.tv_order_nickname).text(mDetail.getBarristerNickname());

        //律师电话
        aq.id(R.id.tv_order_phone_number).text(TextHandler.getHidePhone(mDetail.getBarristerPhone()));

        //头像
        SimpleDraweeView sdv = (SimpleDraweeView) aq.id(R.id.image_order_custom_icon).getView();
        if(!TextUtils.isEmpty(mDetail.getBarristerIcon())){
            sdv.setImageURI(Uri.parse(mDetail.getBarristerIcon()));
        }

        List<CallHistory> callHistories = mDetail.getCallHistories();

        if(callHistories !=null && !callHistories.isEmpty()){

            aq.id(R.id.layout_order_cancel_handle).gone();

            calls.clear();
            calls.addAll(callHistories);
            mAddapter.notifyDataSetChanged();

        }else{

            //show empty?? 没有通话记录
            calls.clear();
            mAddapter.notifyDataSetChanged();

            String endTime = mDetail.getEndTime();
            Date end = DateFormatUtils.parse(endTime);
            Date now = new Date();
            //没有通话记录，并且已经过了处理时间,待处理的订单，可以选择取消
            if(now.after(end) && mDetail.getStatus().equals(OrderDetail.STATUS_WAITING)){
                aq.id(R.id.layout_order_cancel_handle).visible();
                aq.id(R.id.btn_order_request_cancel).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doRequestCancel();
                    }
                });

            }else{
                aq.id(R.id.layout_order_cancel_handle).gone();
            }

        }

        if(mDetail.getStatus().equals(OrderDetail.STATUS_DONE)){

            //已评价
            if(!TextUtils.isEmpty(mDetail.getIsStart()) && mDetail.getIsStart().equals(OrderDetail.ISSTART_YES)){
                aq.id(R.id.layout_order_done).gone();
            }else{
                //订单已经完成，可以评价
                aq.id(R.id.layout_order_done).visible();
                aq.id(R.id.btn_order_star).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIHelper.goAddOrderStarActivity(OrderDetailActivity.this,id);
                    }
                });
            }

            aq.id(R.id.btn_order_reward).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRewardDialog();
                }
            }).visible();
        }else{

            //订单没有完成，不能进行评价
            aq.id(R.id.layout_order_done).gone();

            aq.id(R.id.btn_order_reward).gone();
        }


    }

    float money = 0;

    /**
     * 打赏，对话框
     * 1元，5元,10，15元,30，50元，100元，200元
     */
    private void showRewardDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_reward,null);

        final String[] items = getResources().getStringArray(R.array.reward_money);
        final AQuery aQuery = new AQuery(view);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择打赏金额")
                .setView(view)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Editable editable = aQuery.id(R.id.et_reward_money).getEditable();
                        if(TextUtils.isEmpty(editable)){
                            UIHelper.showToast(getApplicationContext(),"请输入金额");
                            return;
                        }
                        money = Float.parseFloat(editable.toString());
                        doReward(money);
                    }
                }).create();

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,items);
        aQuery.id(R.id.gridview).adapter(aa).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                String item = items[position];
                aQuery.id(R.id.et_reward_money).text(item);
                doReward(Float.parseFloat(item.replace("元","")));
            }
        });

        dialog.show();
    }

    boolean isRewarding = false;
    /**
     * 打赏
     * @param money
     */
    private void doReward(final float money) {

        //余额检查
        Account account = UserHelper.getInstance().getAccount();

        float remaining = account.getRemainingBalance();

        if (remaining < money) {
            new AlertDialog.Builder(OrderDetailActivity.this).setTitle("您的账户余额不足请充值").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UIHelper.goRechargeActivity(OrderDetailActivity.this);
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {

            if(isRewarding)
                return;

            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage(String.format(Locale.CHINA,"确定打赏%.1f元？",money))
                    .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new RewardOrderReq(OrderDetailActivity.this,id,money).execute(new Action.Callback<Boolean>() {
                        @Override
                        public void progress() {
                            isRewarding = true;
                        }

                        @Override
                        public void onError(int errorCode, String msg) {
                            isRewarding = false;

                            UIHelper.showToast(getApplicationContext(),"打赏失败:"+msg);
                        }

                        @Override
                        public void onCompleted(Boolean aBoolean) {
                            isRewarding = false;

                            UIHelper.showToast(getApplicationContext(),"打赏成功，感谢您的支持！");
                        }
                    });
                }
            }).create().show();
        }

    }

    /**
     * 请求取消订单
     */
    private void doRequestCancel() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确定要取消订单么？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RequestCancelOrderReq(OrderDetailActivity.this,id).execute(new Action.Callback<Boolean>() {
                            @Override
                            public void progress() {

                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                UIHelper.showToast(getApplicationContext(),"请求失败");
                            }

                            @Override
                            public void onCompleted(Boolean aBoolean) {

                                new AlertDialog.Builder(OrderDetailActivity.this)
                                        .setTitle("提示")
                                        .setMessage("申请成功,待律师同意后系统将给您退款")
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();

                                loadOrderDetail();

                            }
                        });
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    boolean isWaitingCall = false;
    /**
     * 打电话
     */
    private void doMakeCall() {

        if(isWaitingCall){
            return;
        }

        if(!checkCanMakeCall()){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_invalid_status_makecall));
            return;
        }

        new MakeCallReq(OrderDetailActivity.this,id).execute(new Action.Callback<String>() {
            @Override
            public void progress() {
                isWaitingCall = true;
                progressDialog.setMessage(getString(R.string.tip_wait_for_call));
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                isWaitingCall = false;
                UIHelper.showToast(getApplicationContext(),"拨号失败:"+msg);
            }

            @Override
            public void onCompleted(String s) {

                progressDialog.setMessage("请等待回拨");
                progressDialog.show();

                AQUtility.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isWaitingCall = false;
                    }
                },60*1000);//60s等待回拨
            }
        });

    }

    /**
     * 检查是否可以拨打电话
     * @return
     */
    private boolean checkCanMakeCall() {

        if(mDetail==null)
            return false;

        //订单状态（请求取消、已取消、已完成）不能打电话
        if(!mDetail.getStatus().equals(OrderDetail.STATUS_DOING)//进行中
                && !mDetail.getStatus().equals(OrderDetail.STATUS_WAITING)){//待处理
            return false;
        }
        //当前时间进入预约时间
        String startTime = mDetail.getStartTime();
        Date start = DateFormatUtils.parse(startTime);
        Date now = new Date();

        //还没有到预约时间
        if(now.before(start))
            return false;

        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_order_detail);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK && requestCode== Constants.REQUEST_ADD_ORDER_STAR){
            loadOrderDetail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioHelper.getInstance().clearListener();
        AudioHelper.getInstance().release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        loadOrderDetail();
    }
}
