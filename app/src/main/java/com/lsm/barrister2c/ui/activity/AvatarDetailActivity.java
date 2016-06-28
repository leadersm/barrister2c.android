package com.lsm.barrister2c.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.app.UpdateUserInfoReq;
import com.lsm.barrister2c.data.io.app.UploadUserIconReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.utils.DLog;
import com.lsm.barrister2c.utils.FileUtils;

import java.io.File;
import java.util.HashMap;

/****
 * 用户详情信息页
 */
public class AvatarDetailActivity extends BaseActivity implements UserHelper.UserActionListener{

    private static final String TAG = AvatarDetailActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_CAMERA = 0x10001;

    AQuery aq;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_detail);
        aq = new AQuery(this);

        setupToolbar();

        user = AppConfig.getUser(this);

        if(user==null){
            DLog.d(TAG,"没有找到用户。。。");
            return;
        }

        DLog.d(TAG,user.toString());

        init();

        UserHelper.getInstance().addOnUserActionListener(this);

    }

    private void init() {
        if(!TextUtils.isEmpty(user.getUserIcon())){
            SimpleDraweeView userIconView = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
            userIconView.setImageURI(Uri.parse(user.getUserIcon()));
        }

        //头像
        aq.id(R.id.btn_user_icon).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        aq.id(R.id.tv_userdetail_nickname).text(user.getNickname());
        //擅长
        aq.id(R.id.btn_user_nickname).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 昵称
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this, User.KEY_NICKNAME, user.getNickname());
            }
        });


        aq.id(R.id.tv_userdetail_gender).text(TextUtils.isEmpty(user.getGender())?getString(R.string.default_gender):user.getGender());
        //擅长
        aq.id(R.id.btn_user_gender).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 性别
                showGenderDialog();
            }
        });

        aq.id(R.id.tv_userdetail_phone).text(user.getPhone());

        //电话
        aq.id(R.id.btn_user_phone).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 电话
            }
        });

        aq.id(R.id.tv_userdetail_email).text(user.getEmail());

        //邮箱
        aq.id(R.id.btn_user_email).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 邮箱
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this,User.KEY_EMAIL, user.getEmail());
            }
        });


        aq.id(R.id.tv_userdetail_area).text(user.getArea());
        //地区
        aq.id(R.id.btn_user_area).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 地区（省+市）
                UIHelper.goModifyInfoActivity(AvatarDetailActivity.this,User.KEY_AREA, user.getArea());
            }
        });
    }

    private void updateUserInfo() {

        if(!TextUtils.isEmpty(user.getUserIcon())){
            SimpleDraweeView userIconView = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
            userIconView.setImageURI(Uri.parse(user.getUserIcon()));
        }

        aq.id(R.id.tv_userdetail_nickname).text(user.getNickname());

        aq.id(R.id.tv_userdetail_gender).text(TextUtils.isEmpty(user.getGender())?getString(R.string.default_gender):user.getGender());

        aq.id(R.id.tv_userdetail_phone).text(user.getPhone());

        aq.id(R.id.tv_userdetail_email).text(user.getEmail());

        aq.id(R.id.tv_userdetail_area).text(user.getArea());
    }

    /**
     * 显示性别选择对话框
     */
    protected void showGenderDialog() {

        final String[] items = new String[]{getString(R.string.male), getString(R.string.female)};

        new AlertDialog.Builder(this).setTitle(R.string.user_gender).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                doModifyGender(items[which]);
            }


        }).create().show();

    }

    /**
     * 修改性别
     *
     * @param item
     */
    private void doModifyGender(String item) {

        HashMap<String,String> params = new HashMap<>();
        params.put(User.KEY_GENDER,item);

        new UpdateUserInfoReq(this, params).execute(new Action.Callback<User>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onCompleted(User user) {

                if (user != null) {
                    AvatarDetailActivity.this.user = user;
                    AppConfig.setUser(getApplicationContext(), user);
                    aq.id(R.id.tv_userdetail_gender).text(user.getGender());
                }

            }
        });
    }



    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_user_detail);
    }


    @Override
    protected void onResume() {
        super.onResume();
        user = AppConfig.getUser(this);
    }

    /**
     * 显示选择照片对对话框
     * 从相册选择，拍照
     */
    private void showChoosePicDialog() {
        final String[] items = new String[]{getString(R.string.pick_img_from_album), getString(R.string.take_photo)};

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_choose_pic)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        doSelectChooseMode(which);
                    }

                }).create().show();
    }


    /**
     * 从相册选择or拍照
     *
     * @param which
     */
    protected void doSelectChooseMode(int which) {
        tempFile = new File(AppConfig.getDir(Constants.imageDir), "temp.jpg");

        if (which == 0) {//从相册选择
            pickFromGallery();
        } else {//拍照
            pickFromCamera();
        }
    }

    /*
     * 从相册获取
     */
    private void pickFromGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_GALLERY
        startActivityForResult(intent, Constants.REQUEST_CODE_GALLERY);
    }

    File tempFile;

    /*
     * 从相机获取
     */
    private void pickFromCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("====>请求相机权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }

        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (AppConfig.isSDCardEnabled()) {


            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        } else {
            UIHelper.showToast(getApplicationContext(), R.string.tip_sdcard_error);
            return;
        }
        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_CAPTURE
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

                pickFromCamera();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

                UIHelper.showToast(getApplicationContext(),"您已拒绝相机使用权限，如果需要请在系统安全设置中手动开启。");

            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DLog.d(TAG, "onActivityResult:" + resultCode + ",requestCode:" + requestCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);
                } else {
                    DLog.e(TAG, "没有返回图片");
                }

            } else if (requestCode == Constants.REQUEST_CODE_CAPTURE) {
                // 从相机返回的数据
                if (AppConfig.isSDCardEnabled()) {
                    crop(Uri.fromFile(tempFile));
                } else {
                    UIHelper.showToast(getApplicationContext(), R.string.tip_sdcard_error);
                }

            } else if (requestCode == Constants.REQUEST_CODE_CROP) {
                // 从剪切图片返回的数据

                SimpleDraweeView avatar = (SimpleDraweeView) findViewById(R.id.image_userdetail_icon);
                avatar.setImageURI(Uri.fromFile(tempFile));

                try {
                    upload(tempFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(requestCode == Constants.REQUEST_CODE_FROM_USER_DETAIL){

                String key = data.getStringExtra(Constants.KEY);
                String content = data.getStringExtra(Constants.KEY_CONTENT);

                switch (key){
                    case User.KEY_AREA:
                        user.setArea(content);
                        aq.id(R.id.tv_userdetail_area).text(user.getArea());
                        break;
                    case User.KEY_EMAIL:
                        user.setEmail(content);
                        aq.id(R.id.tv_userdetail_email).text(user.getEmail());
                        break;
                    case User.KEY_NICKNAME:
                        user.setNickname(content);
                        aq.id(R.id.tv_userdetail_nickname).text(user.getNickname());
                        break;
                }

                AppConfig.setUser(getApplicationContext(),user);

                UserHelper.getInstance().updateUserInfo();

            }
        } else {
            DLog.e(TAG, "onActivityResult failed...");
        }

    }

    private void upload(File file) throws Exception {

        new UploadUserIconReq(this,file).execute(new Action.Callback<User>(){

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                DLog.e(TAG, msg);
                //失败，提示是否重新上传
                uploadFailed();
            }

            @Override
            public void onCompleted(User result) {
                if(!TextUtils.isEmpty(result.getUserIcon())){
                    //上传成功
                    FileUtils.deleteFile(tempFile);

                    User user = AppConfig.getUser(getApplicationContext());

                    user.setUserIcon(result.getUserIcon());

                    AppConfig.setUser(AvatarDetailActivity.this, user);

                    //通知主页左菜单更换用户头像
                    UserHelper.getInstance().updateUserInfo();

                }
            }
        });

    }

    private void uploadFailed() {
        //失败，提示是否重新上传
        new AlertDialog.Builder(AvatarDetailActivity.this)
                .setTitle(R.string.title_dialog_upload_failed)
                .setMessage(R.string.msg_dialog_upload_failed)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    upload(tempFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }).create().show();
    }

    /*
     * 剪切图片
     */
    private void crop(final Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 144);
        intent.putExtra("outputY", 144);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);

        // 开启一个带有返回值的Activity，请求码为REQUEST_CODE_CROP
        startActivityForResult(intent, Constants.REQUEST_CODE_CROP);
    }


    @Override
    public void onSSOLoginCallback(User user) {

    }

    @Override
    public void onLoginCallback(User user) {

    }

    @Override
    public void onLogoutCallback() {

    }

    @Override
    public void onUpdateUserInfo() {
        this.user = AppConfig.getUser(this);
        updateUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserHelper.getInstance().removeListener(this);
    }
}
