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
import com.lsm.barrister2c.ui.activity.AvatarDetailActivity;
import com.lsm.barrister2c.ui.activity.BarristerListActivity;
import com.lsm.barrister2c.ui.activity.DocActivity;
import com.lsm.barrister2c.ui.activity.GetMoneyActivity;
import com.lsm.barrister2c.ui.activity.LoginActivity;
import com.lsm.barrister2c.ui.activity.MainActivity;
import com.lsm.barrister2c.ui.activity.ModifyAvaterActivity;
import com.lsm.barrister2c.ui.activity.MyBankCardActivity;
import com.lsm.barrister2c.ui.activity.MyFavoriteActivity;
import com.lsm.barrister2c.ui.activity.MyOrdersActivity;
import com.lsm.barrister2c.ui.activity.SetBankCardActivity;
import com.lsm.barrister2c.ui.activity.SettingsActivity;
import com.lsm.barrister2c.ui.activity.WebViewActivity;
import com.lsm.barrister2c.ui.adapter.BarristerAdapter;

import java.util.ArrayList;

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

    public static void goBarristerListAcitivity(Context context, String type) {
        Intent intent = new Intent(context, BarristerListActivity.class);
        intent.putExtra(BarristerListActivity.KEY_TYPE, type);
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
     * @param file
     */
    public static void goDocActivity(Context context, String title, String file) {
        Intent intent = new Intent(context, DocActivity.class);
        intent.putExtra(Constants.KEY_TITLE, title);
        intent.putExtra(Constants.KEY_FILE, file);
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
     * @param activity
     * @param key
     */
    public static void goModifyInfoActivity(Activity activity, String key, String value) {
        Intent intent = new Intent(activity,ModifyAvaterActivity.class);
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
        intent.putExtra(MyBankCardActivity.KEY,bankCard);
        ctx.startActivity(intent);
    }

    public static void goSetBankcardActivity(Context ctx) {
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
}
