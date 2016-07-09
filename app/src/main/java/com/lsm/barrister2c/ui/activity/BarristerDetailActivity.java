package com.lsm.barrister2c.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.db.Favorite;
import com.lsm.barrister2c.data.db.FavoriteDao;
import com.lsm.barrister2c.data.db.UserDbService;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BarristerDetail;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.AddFavoriteBarristerReq;
import com.lsm.barrister2c.data.io.app.DelFavoriteBarristerReq;
import com.lsm.barrister2c.data.io.app.GetBarristerDetailReq;
import com.lsm.barrister2c.data.io.app.GetMyAccountReq;
import com.lsm.barrister2c.data.io.app.MakeOrderReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.fragment.AppointmentPickDialogFragment;
import com.lsm.barrister2c.utils.DLog;

import java.util.Locale;


/**
 * 律师详情
 * 律师简介
 * 即时咨询：
 * 预约咨询：律师预约时间表，勾选时段，下单
 */
public class BarristerDetailActivity extends BaseActivity implements AppointmentPickDialogFragment.Callback{

    private static final String TAG = BarristerDetailActivity.class.getSimpleName();
    public static String KEY = "barrister.item";

    Barrister barrister;

    String id;

    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrister_detail);
        aq = new AQuery(this);

        setupToolbar();

        init();

        loadBarristerDetail();
    }

    private void init() {

        barrister = (Barrister) getIntent().getSerializableExtra(KEY);

        if(barrister!=null){
            id = barrister.getId();
            bindBarrister();
        }else{
            id = getIntent().getStringExtra("id");
        }

        isFavorite = UserDbService.getInstance(this).getFavoriteAction().contains(FavoriteDao.Properties.Id.eq("barrister"+id));

        aq.id(R.id.btn_detail_appointment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登陆检查
                User user = AppConfig.getUser(getApplicationContext());
                if (user == null) {
                    UIHelper.goLoginActivity(BarristerDetailActivity.this);
                    return;
                }
                //时段选择
                showPickAppointmentDialog();
            }
        });

        aq.id(R.id.btn_detail_im).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登陆检查
                User user = AppConfig.getUser(getApplicationContext());
                if (user == null) {
                    UIHelper.goLoginActivity(BarristerDetailActivity.this);
                    return;
                }

                if(barristerDetail==null){
                    DLog.e(TAG,"加载详情失败");
                    return;
                }

                float money = barristerDetail.getPriceIM();

                String imOrderInfo = String.format(Locale.CHINA, "您将购买即时咨询服务，需要支付%.1f元", money);

                tryToPay(imOrderInfo,money,money, null);

            }
        });
    }


    private void bindBarrister() {
        aq.id(R.id.tv_detail_area).text(barrister.getArea());
        aq.id(R.id.tv_detail_comment_count).text("评价:").gone();
        aq.id(R.id.tv_detail_company).text(barrister.getCompany());
        aq.id(R.id.tv_detail_favorite_count).text("收藏").clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFavorite();
            }
        }).checked(isFavorite);


        //擅长领域
        if(barrister.getBizAreas()==null || barrister.getBizAreas().isEmpty()){
            aq.id(R.id.gridview_goodat).gone();
        }else{
            ArrayAdapter<BusinessArea> aa = new ArrayAdapter<BusinessArea>(this,R.layout.item_biz_area
                    ,R.id.tv_item_biz_name,barrister.getBizAreas()){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    BusinessArea item = getItem(position);
                    TextView tv = (TextView) view.findViewById(R.id.tv_item_biz_name);
                    tv.setText(item.getName());
                    return view;
                }
            };

            aq.id(R.id.gridview_goodat).adapter(aa).visible();
        }


//        aq.id(R.id.tv_detail_intro).text(barrister.getIntro());
        aq.id(R.id.tv_detail_nickname).text(barrister.getName());
        aq.id(R.id.tv_detail_service_times).text("服务（"+0 + "）次");
        aq.id(R.id.tv_detail_year).text(barrister.getWorkYears()+"年");


    }

    private void bindBarristerDetail() {
        aq.id(R.id.tv_detail_area).text(barristerDetail.getArea());
        aq.id(R.id.tv_detail_comment_count).text("评价").gone();
        aq.id(R.id.tv_detail_company).text(barristerDetail.getCompany());
        aq.id(R.id.tv_detail_favorite_count).text("收藏").clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFavorite();
            }
        }).checked(isFavorite);


        //擅长领域
        if(barristerDetail.getBizAreas()==null || barristerDetail.getBizAreas().isEmpty()){
            aq.id(R.id.gridview_goodat).gone();
        }else{
            ArrayAdapter<BusinessArea> aa = new ArrayAdapter<BusinessArea>(this,R.layout.item_biz_area
                    ,R.id.tv_item_biz_name,barristerDetail.getBizAreas()){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    BusinessArea item = getItem(position);
                    TextView tv = (TextView) view.findViewById(R.id.tv_item_biz_name);
                    tv.setText(item.getName());
                    return view;
                }
            };

            aq.id(R.id.gridview_goodat).adapter(aa).visible();
        }


//        aq.id(R.id.tv_detail_intro).text(barristerDetail.getIntro());
        aq.id(R.id.tv_detail_nickname).text(barristerDetail.getName());
        aq.id(R.id.tv_detail_service_times).text("服务（"+barristerDetail.getRecentServiceTimes() + "）次");
        aq.id(R.id.tv_detail_year).text(barristerDetail.getWorkYears()+"年");


    }

    boolean isFavorite = false;

    /**
     * 收藏
     */
    private void doFavorite() {
        if(isFavorite){
            //取消收藏
            new DelFavoriteBarristerReq(this,id).execute(new Action.Callback<Boolean>() {
                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {
                    UIHelper.showToast(getApplicationContext(),"取消收藏失败");
                    aq.id(R.id.tv_detail_favorite_count).checked(true);
                }

                @Override
                public void onCompleted(Boolean aBoolean) {
                    UserDbService.getInstance(getApplicationContext()).getFavoriteAction().delete(FavoriteDao.Properties.Id.eq("barrister"+id));
                    isFavorite = false;
                    aq.id(R.id.tv_detail_favorite_count).checked(false);
                }
            });
        }else{
            //添加收藏
            new AddFavoriteBarristerReq(this,id).execute(new Action.Callback<Boolean>() {

                @Override
                public void progress() {

                }

                @Override
                public void onError(int errorCode, String msg) {
                    aq.id(R.id.tv_detail_favorite_count).checked(false);
                }

                @Override
                public void onCompleted(Boolean aBoolean) {

                    Favorite favorite = new Favorite();

                    String name = barrister==null?barristerDetail.getName():barrister.getName();

                    String userIcon = barrister == null? barristerDetail.getUserIcon():barrister.getUserIcon();

                    favorite.setId("barrister"+id);
                    favorite.setTitle(name);
                    favorite.setThumb(userIcon);
                    favorite.setType(Favorite.TYPE_BARRISTER);

                    UserDbService.getInstance(getApplicationContext()).getFavoriteAction().save(favorite);

                    isFavorite = true;

                    aq.id(R.id.tv_detail_favorite_count).checked(true);
                }
            });

        }

        aq.id(R.id.tv_detail_favorite_count).checked(isFavorite);
    }

    /**
     * 尝试支付（系统内支付）
     * @param money
     * @param dateSettings
     */
    private void tryToPay(final String orderInfo, final float money,final float price, final String dateSettings) {
        //余额检查
        Account account = UserHelper.getInstance().getAccount();

        float remaining = account.getRemainingBalance();

        if (remaining < money) {
            new AlertDialog.Builder(BarristerDetailActivity.this).setTitle("您的账户余额不足请充值").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UIHelper.goRechargeActivity(BarristerDetailActivity.this);
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {

            View view = getLayoutInflater().inflate(R.layout.dialog_remarks,null);
            final EditText et_remarks = (EditText) view.findViewById(R.id.et_dialog_remarks);
            TextView tv = (TextView) view.findViewById(R.id.tv_dialog_msg);
            String message = String.format(Locale.CHINA, getString(R.string.tip_price), money);

            tv.setText(message);
            //对话框提示，将扣费
            new AlertDialog.Builder(BarristerDetailActivity.this)
                    .setTitle(getString(R.string.pmt))
//                    .setMessage(message)
                    .setView(view)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doMakeOrder(orderInfo,money,price,et_remarks.getText().toString(),dateSettings);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    /**
     * 预约时间选择对话框
     */
    private void showPickAppointmentDialog() {
        float priceAppointment = barristerDetail==null?0:barristerDetail.getPriceAppointment();
        AppointmentPickDialogFragment.getInstance(id,priceAppointment).show(getSupportFragmentManager(),"dialog");
    }


    BarristerDetail barristerDetail;
    /**
     * 加载律师详情
     */
    private void loadBarristerDetail() {
       new GetBarristerDetailReq(this,id).execute(new Action.Callback<IO.GetBarristerDetailResult>() {
           @Override
           public void progress() {

           }

           @Override
           public void onError(int errorCode, String msg) {

           }

           @Override
           public void onCompleted(IO.GetBarristerDetailResult result) {
               barristerDetail = result.detail;
               bindBarristerDetail();
           }
       });
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_barrister_detail);

    }

    @Override
    public void onAppointmentOrderPrepared(String orderInfo, int count, String dateSettings) {

        if(barristerDetail==null){
            DLog.e(TAG,"获取律师信息失败，无法创建订单。");
            return;
        }

        float money = count * barristerDetail.getPriceAppointment();
        float price = barristerDetail.getPriceAppointment();

        orderType = OrderItem.TYPE_APPOINTMENT;

        tryToPay(orderInfo,money,price,dateSettings);

    }

    MakeOrderReq mMakeOrderReq;

    private String orderType = OrderItem.TYPE_IM;
    /**
     * 提交订单，
     * date，日期
     * position 时间段索引位置,
     * time，时间段
     */
    private void doMakeOrder(final String orderInfo, float money, float price, String remarks, String appointmentSettings) {

        if (mMakeOrderReq != null && mMakeOrderReq.isLoading()) {
            UIHelper.showToast(getApplicationContext(), "正在提交中，请稍候...");
            return;
        }

        mMakeOrderReq = new MakeOrderReq(this,id, orderInfo,money,price,remarks,appointmentSettings);
        mMakeOrderReq.execute(new Action.Callback<Boolean>() {

            ProgressDialog progressDialog;

            @Override
            public void progress() {
                progressDialog = new ProgressDialog(BarristerDetailActivity.this);
                progressDialog.setMessage("请稍候...");
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(), "提交订单失败：" + msg);
            }

            @Override
            public void onCompleted(Boolean result) {

                progressDialog.dismiss();

                String message;
                if(orderType.equals(OrderItem.TYPE_IM)){
                    message = getString(R.string.tip_order_success_im);
                }else {
                    message = getString(R.string.tip_order_success_appointment);
                }

                new AlertDialog.Builder(BarristerDetailActivity.this)
                        .setTitle(message)
                        .setMessage(getString(R.string.tip_check_myorders))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(orderType.equals(OrderItem.TYPE_APPOINTMENT)){
                                    UIHelper.goMyOrdersActivity(BarristerDetailActivity.this);
                                }

                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                 }).create().show();

                syncAccount();

            }
        });

    }

    /**
     * 订单创建成功，同步账户信息
     */
    private void syncAccount() {

        new GetMyAccountReq(this).execute(new Action.Callback<IO.GetAccountResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {

            }

            @Override
            public void onCompleted(IO.GetAccountResult result) {

                UserHelper.getInstance().setAccount(result.account);
                UserHelper.getInstance().updateAccount();

            }
        });
    }
}
