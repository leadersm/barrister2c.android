package com.lsm.barrister2c.ui;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.ui.activity.AddOrderStarActivity;
import com.lsm.barrister2c.ui.activity.AuthActivity;
import com.lsm.barrister2c.ui.activity.AvatarDetailActivity;
import com.lsm.barrister2c.ui.activity.BarristerDetailActivity;
import com.lsm.barrister2c.ui.activity.BarristerListActivity;
import com.lsm.barrister2c.ui.activity.ExpertListActivity;
import com.lsm.barrister2c.ui.activity.GetMoneyActivity;
import com.lsm.barrister2c.ui.activity.LoginActivity;
import com.lsm.barrister2c.ui.activity.MainActivity;
import com.lsm.barrister2c.ui.activity.ModifyAvaterActivity;
import com.lsm.barrister2c.ui.activity.MyAccountActivity;
import com.lsm.barrister2c.ui.activity.MyBankCardActivity;
import com.lsm.barrister2c.ui.activity.MyFavoriteActivity;
import com.lsm.barrister2c.ui.activity.MyOrdersActivity;
import com.lsm.barrister2c.ui.activity.OnLineBizUserListActivity;
import com.lsm.barrister2c.ui.activity.OrderDetailActivity;
import com.lsm.barrister2c.ui.activity.SearchBarristerActivity;
import com.lsm.barrister2c.ui.activity.SetBankCardActivity;
import com.lsm.barrister2c.ui.activity.SettingsActivity;
import com.lsm.barrister2c.ui.activity.UploadCaseActivity;
import com.lsm.barrister2c.ui.activity.WebViewActivity;
import com.lsm.barrister2c.ui.activity.creditdebt.CreditDebtDetailActivity;
import com.lsm.barrister2c.ui.activity.creditdebt.CreditDebtListActivity;
import com.lsm.barrister2c.ui.activity.creditdebt.UploadCreditDebtActivity;
import com.lsm.barrister2c.wxapi.WXPayEntryActivity;

import java.util.ArrayList;

import static com.baidu.location.h.i.U;

public class UIHelper {

    public static PopupWindow createPopupWindow(View windowView, int animstyle,
                                                int w, int h) {
        final PopupWindow window = new PopupWindow(windowView, w, h);

        window.setAnimationStyle(animstyle);
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        windowView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                if ((event.getAction() == MotionEvent.ACTION_DOWN)
                        && ((x < 0) || (x >= v.getWidth()) || (y < 0) || (y >= v
                        .getHeight()))) {
                    window.dismiss();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    window.dismiss();
                    return true;
                } else {
                    return v.onTouchEvent(event);
                }
            }
        });
        windowView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (window.isShowing()) {
                            window.dismiss();
                        }
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        ArrayList<View> focusables = windowView.getFocusables(View.FOCUSABLES_ALL);
        if (focusables != null)
            for (View temp : focusables) {
                temp.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // TODO Auto-generated method stub
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                                if (window.isShowing()) {
                                    window.dismiss();
                                }
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        return window;
    }

    public static AlertDialog createDialogWindow(Context context, View windowView, int animstyle,
                                                 int w, int h) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();

        dialog.setView(windowView);
        Window window = dialog.getWindow();
        LayoutParams params = new LayoutParams(w, h);

        return dialog;
    }

    public static void showFeedBack(Activity act) {
        // TODO Auto-generated method stub

    }

    public static final String KEY_ACTORS = "actor";
    public static final String KEY_DIRECTORS = "directors";
    protected static final String TAG = UIHelper.class.getSimpleName();

    /**
     * 隐藏手机键盘
     *
     * @param context
     * @param edt
     */
    public static void hideIM(Context context, View edt) {
        try {
            InputMethodManager im = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {

        }
    }

    public static void showIM(Context context, View edt, int flag) {
        try {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {
                im.showSoftInputFromInputMethod(windowToken, flag);
            }
        } catch (Exception e) {

        }
    }

    public static void goBarristerListAcitivity(Context context, String type, BusinessType bizType, BusinessArea bizArea) {
        Intent intent = new Intent(context, BarristerListActivity.class);

        intent.putExtra(BarristerListActivity.KEY_TYPE, type);

        if (bizType != null) {
            intent.putExtra(BarristerListActivity.KEY_BIZ_TYPE, bizType);
        }

        if (bizArea != null) {
            intent.putExtra(BarristerListActivity.KEY_BIZ_AREA, bizArea);
        }

        context.startActivity(intent);
    }


    public static void showWebview(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到内置浏览器
     *
     * @param context
     * @param url
     */
    public static void goWebViewActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.KEY_TITLE, title);
        intent.putExtra(WebViewActivity.KEY_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转关于-我们，用户说明……
     *
     * @param context
     * @param title
     * @param url
     */
    public static void goDocActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.KEY_TITLE, title);
        intent.putExtra(Constants.KEY_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showToast(Context context, int res) {
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static void goSettingsActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public static void goLoginActivity(Activity ctx) {
        Intent intent = new Intent(ctx, LoginActivity.class);
        ctx.startActivity(intent);
    }

    /**
     * 修改用户信息
     *
     * @param activity
     * @param key
     */
    public static void goModifyInfoActivity(Activity activity, String key, String value) {
        Intent intent = new Intent(activity, ModifyAvaterActivity.class);
        intent.putExtra(Constants.KEY, key);
        intent.putExtra(Constants.KEY_USER_INFO_VALUE, value);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_FROM_USER_DETAIL);
    }


    public static void goMainActivity(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void goBankcardActivity(Context ctx, Account.BankCard bankCard) {
        Intent intent = new Intent(ctx, MyBankCardActivity.class);
        intent.putExtra(MyBankCardActivity.KEY, bankCard);
        ctx.startActivity(intent);
    }

    public static void goSetBankcardActivity(Activity ctx) {
        Intent intent = new Intent(ctx, SetBankCardActivity.class);
        ctx.startActivity(intent);
    }

    public static void goUserDetailActivity(Context ctx) {
        Intent intent = new Intent(ctx, AvatarDetailActivity.class);
        ctx.startActivity(intent);
    }

    public static void goGetMoneyActivity(Activity ctx) {
        Intent intent = new Intent(ctx, GetMoneyActivity.class);
        ctx.startActivity(intent);
    }

    public static void goMyFavorite(Activity activity) {
        Intent intent = new Intent(activity, MyFavoriteActivity.class);
        activity.startActivity(intent);
    }

    public static void goMyOrdersActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, MyOrdersActivity.class);
        activity.startActivity(intent);
    }

    public static void goBarristerDetailActivity(Context activity, Barrister barrister) {
        Intent intent = new Intent(activity, BarristerDetailActivity.class);
        intent.putExtra(BarristerDetailActivity.KEY, barrister);
        activity.startActivity(intent);
    }

    public static void goBarristerDetailActivity(Context activity, String id) {
        Intent intent = new Intent(activity, BarristerDetailActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 充值页面
     *
     * @param activity
     */
    public static void goRechargeActivity(Context activity) {
        Intent intent = new Intent(activity, WXPayEntryActivity.class);
//        Intent intent = new Intent(activity, RechargeActivity.class);
        activity.startActivity(intent);
    }

    public static void goAddOrderStarActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, AddOrderStarActivity.class);
        intent.putExtra("id", id);
        activity.startActivityForResult(intent, Constants.REQUEST_ADD_ORDER_STAR);
    }

    public static void goOrderDetailActivity(Context context, String id) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public static void goMyAccountActivity(Context context) {
        Intent intent = new Intent(context, MyAccountActivity.class);
        context.startActivity(intent);
    }

    public static void goSearchActivity(Context context) {
        Intent intent = new Intent(context, SearchBarristerActivity.class);
        context.startActivity(intent);
    }

    public static void goOnLineUserListAcitivity(Context context, BusinessType item) {
        Intent intent = new Intent(context, OnLineBizUserListActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    public static void goUploadCaseActivity(Context context) {
        Intent intent = new Intent(context, UploadCaseActivity.class);
        context.startActivity(intent);
    }

    /**
     * 开启QQ与指定q号聊天
     *
     * @param context
     * @param qq
     */
    public static void startQQ(Context context, String qq) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void showCallView(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void goWebAuthActivity(Context context) {
        Intent intent = new Intent(context, AuthActivity.class);
        context.startActivity(intent);
    }

    public static void goExpertListAcitivity(Context context) {
        Intent intent = new Intent(context, ExpertListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 账款库列表,搜索,添加
     *
     * @param context
     */
    public static void goCreditListAcitivity(Context context) {
        Intent intent = new Intent(context, CreditDebtListActivity.class);
        context.startActivity(intent);
    }


    public static void goAddCreditDebtActivity(Context context) {
        Intent intent = new Intent(context, UploadCreditDebtActivity.class);
        context.startActivity(intent);
    }

    public static void goCreditDebtDetailActivity(Context context, CreditDebtInfo mItem) {
        Intent intent = new Intent(context, CreditDebtDetailActivity.class);
        intent.putExtra("item", mItem);
        context.startActivity(intent);
    }
}
