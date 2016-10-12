package com.lsm.barrister2c.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.AddOrderStarReq;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.Locale;


/**
 * 反馈页
 */
public class AddOrderStarActivity extends BaseActivity {

    AQuery aq;

    AddOrderStarReq mReq;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_star);
        setupToolbar();

        id = getIntent().getStringExtra("id");

        aq = new AQuery(this);
        aq.id(R.id.btn_feedback_commit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        RatingBar rb = (RatingBar) findViewById(R.id.rartingbar_add_order_star);
        assert rb!=null;
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    aq.id(R.id.tv_rating).text(String.format(Locale.CHINA,"%.1f",rating));
                }
            }
        });
    }

    private void doCommit() {

        String content = aq.id(R.id.et_feedback_content).getEditable().toString();
        if(TextUtils.isEmpty(content)){
            UIHelper.showToast(getApplicationContext(),getString(R.string.tip_empty_feedback));
            return;
        }

        RatingBar rb = (RatingBar) findViewById(R.id.rartingbar_add_order_star);

        float rating = rb.getRating();

        mReq = new AddOrderStarReq(this,id,rating,content);

        mReq.execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),"评价失败："+msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                //评价完成。
                setResult(RESULT_OK);
                finish();

            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.feedback_order);
    }
}
