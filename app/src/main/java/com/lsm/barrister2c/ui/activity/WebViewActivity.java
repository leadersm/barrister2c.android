package com.lsm.barrister2c.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.utils.DLog;

public class WebViewActivity extends BaseActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";

    WebView webView;
    ProgressBar progress;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String url = getIntent().getStringExtra(KEY_URL);
        title = getIntent().getStringExtra(KEY_TITLE);

        DLog.d(TAG,"url:"+url);

        setupToolbar();

        initWebView(url);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    /**
     * 初始化WebView
     *
     * @param url
     */
    public void initWebView(String url) {
        webView = (WebView) findViewById(R.id.webView);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {


            // 设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                DLog.d(TAG, "progress:" + newProgress);
                if(newProgress==100){
                    progress.setVisibility(View.GONE);
                }else{
                    progress.setVisibility(View.VISIBLE);
                }

                progress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });

        webView.loadUrl(url);
    }


    @Override
    protected void onDestroy() {
        webView.getSettings().setBuiltInZoomControls(false);
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onDestroy();
     }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                webView.goBack();// 这里如果不想他一直返回上一页的话，可以直接关闭这个activity
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause(); // 暂停网页中正在播放的视频
        }
    }

}
