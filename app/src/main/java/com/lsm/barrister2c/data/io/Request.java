package com.lsm.barrister2c.data.io;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.VersionHelper;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.ui.activity.BaseActivity;
import com.lsm.barrister2c.utils.AsyncUtils;
import com.lsm.barrister2c.utils.DLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public abstract class Request {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 设置网络请求参数
     */
    static {
        AQUtility.setDebug(Constants.DEBUG);
        AjaxCallback.setTimeout(15000);// timeout 15s
        AjaxCallback.setNetworkLimit(MAXIMUM_POOL_SIZE);
    }

    public String TAG = getName();

    public Request(Context context) {
        this.context = context;
    }

    int retry = 0;

    public Request setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    private long expire = 0;

    public Request setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    private boolean refresh = false;

    public boolean isRefresh() {
        return refresh;
    }

    public Request setRefresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    private boolean loading = false;

    public boolean isLoading() {
        return loading;
    }

    public abstract String getName();//请求名称

    public abstract String url();//请求url

    public abstract HashMap<String, Object> params();//post 参数

    public abstract <T> T parse(String json) throws Exception;//解析

    public abstract HashMap<String, String> headers();

    public abstract boolean isGTCReq();

    /**
     * Title: Request.java
     * Description:请求回调
     *
     * @param <T>
     * @author lsm
     * @date 2015-5-13
     */
    public interface Callback<T> {
        void progress();

        void onError(int errorCode, String msg);

        void onCompleted(T t);
    }

    public boolean saveCookie = false;

    public Context context;

    /**
     * 执行请求
     *
     * @param callback
     */
    public <T> void execute(final Callback<T> callback) {

        loading = true;

        if (callback != null) {

            try {
                callback.progress();
            } catch (Exception e) {

            }
        }

        String url = url();

        AjaxCallback<String> cb = new AjaxCallback<String>() {

            @Override
            public void callback(String url, String json, final AjaxStatus status) {

                loading = false;

                if (status.getCode() == 200) {

                    DLog.d(TAG, "url:" + url);
                    DLog.i(TAG, json);

                    //解析
                    if (callback != null) {
                        new ParseTask<T>(json, callback, status).executeOnExecutor(AsyncUtils.THREAD_POOL_EXECUTOR);
                    } else {
                        DLog.e(TAG, "data callback is null");
                    }

                } else {

                    if (callback != null) {
                        try {
                            switch (status.getCode()) {
                                case -101:
                                    //网络原因出错
                                    callback.onError(status.getCode(), context.getString(R.string.tip_loading_error_network));
                                    break;
                                case 500:
                                    //服务器出错
                                    callback.onError(status.getCode(), context.getString(R.string.tip_loading_error_server));
                                    break;
                                default:
                                    //其他原因
                                    callback.onError(status.getCode(), status.getMessage());
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }

                    DLog.e(TAG, "url:" + url);
                    DLog.e(TAG, "wxPrepayInfo: code:" + status.getCode() + " ;message:" + status.getMessage());
                    status.invalidate();
                }
                DLog.v(TAG, "====" + getName() + "=========本次连接耗时" + status.getDuration() + "毫秒============");
            }
        };

        cb.refresh(refresh);

        //post params
        if (params() != null && params().size() > 0) {
            cb.params(params());
        }

        HashMap<String, String> headers = headers();

        if (headers != null && !headers.isEmpty()) {
            cb.headers(headers);
        }

        cb.retry(retry);

        if (aq == null)
            aq = new AQuery(context);

        if (expire != 0)
            aq.ajax(url, String.class, expire, cb);
        else
            aq.ajax(url, String.class, cb);


    }

    AQuery aq;

    public void cancel() {

        if (aq != null) {

            DLog.d(TAG, getName() + ".ajaxCancel!!!");

            aq.ajaxCancel();

        }

    }

    public static HashMap<String, String> headers = new HashMap<>();

    public static HashMap<String, String> getHeaders(Context context) {
        //deviceId:X-DEVICE-NUM String(0,32]
        headers.put("X-DEVICE-NUM", Constants.deviceId);

//		应用版本:X-VERSION String(0,32]
        String versionName = VersionHelper.instance().getVersionName();
        headers.put("X-VERSION", "android-" + versionName);

//		加密key:X-KEY String(0,64]，deviceId+VersionNum-->AES(code)->Base64
//        headers.put("X-KEY", "");
//        DLog.i(TAG, "X-KEY:" + "");

//		设备型号:X-TYPE String(0,128]
        headers.put("X-TYPE", Build.MODEL);

//		系统:X-SYSTEM String(0,32]
        headers.put("X-SYSTEM", "android");

//		系统:X-SYSTEM-VERSION String(0,32]
        headers.put("X-SYSTEM-VERSION", String.valueOf(Build.VERSION.SDK_INT));

        //屏幕大小
        headers.put("X-SCREEN", Constants.screenSize);

        //渠道
        headers.put("X-MARKET", String.valueOf(Constants.MARKET));

//		userId:X-USERID INTEGER  登陆后调用
        User user = AppConfig.getUser(context);
        if (user != null) {
            headers.put("X-UID", user.getId());
        }

        String cookie = AppConfig.getInstance().getCookie();

        if (!TextUtils.isEmpty(cookie)) {

            headers.put("Cookie", cookie);

            headers.put("sessionId", cookie);
        }

        return headers;
    }


    /**
     * 字符串url编码
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * Title: Request.java
     * Description: 解析任务
     *
     * @param <T>
     * @author lsm
     * @date 2015-5-5
     */
    class ParseTask<T> extends AsyncTask<Void, Void, Integer> {

        String result;
        Callback<T> callback;
        AjaxStatus status;

        T t;

        public ParseTask(String result, Callback<T> callback, AjaxStatus status) {
            super();
            this.result = result;
            this.callback = callback;
            this.status = status;
        }

        @Override
        protected Integer doInBackground(Void... arg0) {


            try {

                t = parse(result);
                return 200;

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                result = null;
                status.close();
                System.gc();
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

//			DLog.i(TAG, "parse completed");

            try {

                if (result != 200) {

                    callback.onError(result, String.valueOf(result));

                    return;

                }

                //Activity 触发的请求并且Activity已经关闭则不再传递数据，释放Activity
                if (context != null && context instanceof BaseActivity) {

                    BaseActivity mActivity = (BaseActivity) context;

                    if (mActivity.isFinished()) {

                        context = null;

                        System.gc();

                        return;
                    }
                }

                callback.onCompleted(t);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public <T> T getFromGson(String json, TypeToken<T> typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeToken.getType());
    }

    public int getTotalPage(int total, int pageSize) {
        return (int) Math.ceil((float) total / pageSize);
    }

}
