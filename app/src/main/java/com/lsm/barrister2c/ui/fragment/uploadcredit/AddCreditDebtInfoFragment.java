package com.lsm.barrister2c.ui.fragment.uploadcredit;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.AppConfig;
import com.lsm.barrister2c.app.Constants;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.module.pick.RxImageConverters;
import com.lsm.barrister2c.module.pick.RxImagePicker;
import com.lsm.barrister2c.module.pick.Sources;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.utils.DateFormatUtils;
import com.lsm.barrister2c.utils.FileUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lvshimin on 16/10/12.
 * 填写债权债务信息
 */
public class AddCreditDebtInfoFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_credit_add_info, container, false);
        init(view);
        return view;
    }

    AQuery aq;
    RadioGroup groupType, groupStatus;
    RadioGroup groupProofType, groupJudgeType;

    private void init(View view) {
        aq = new AQuery(view);
        groupType = (RadioGroup) view.findViewById(R.id.group_type);
        groupStatus = (RadioGroup) view.findViewById(R.id.group_status);
        groupProofType = (RadioGroup) view.findViewById(R.id.group_proof);
        groupJudgeType = (RadioGroup) view.findViewById(R.id.group_judge);

        //凭证图片
        aq.id(R.id.btn_proof_image).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = CHOOSE_PROOF_FILE;
                showChoosePicDialog();
            }
        });

        //判决书文件
        aq.id(R.id.btn_judge_image).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = CHOOSE_JUDGE_FILE;
                showChoosePicDialog();
            }
        });

        groupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_status_suing){
                    //执行中隐藏凭证
                    aq.id(R.id.layout_image_proof).gone();
                    aq.id(R.id.layout_image_judge).visible();
                }else{
                    //其他隐藏判决书
                    aq.id(R.id.layout_image_proof).visible();
                    aq.id(R.id.layout_image_judge).gone();
                }
            }
        });

        aq.id(R.id.et_credit_time).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });


        createTime = DateFormatUtils.format(new Date(),"yyyy-MM-dd");

        aq.id(R.id.et_credit_time).text(createTime);

    }

    Calendar cal;
    private void showDateTimePicker() {

        cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                Date time = cal.getTime();
                String date = DateFormatUtils.format(time,"yyyy-MM-dd");

                Log.d("onDatePick","pickdate:"+date);
                aq.id(R.id.et_credit_time).text(date);

                createTime = date;
            }
        }, year, month, dayOfMonth).show();
    }

    String type;
    String status;
    String desc;
    String money;
    String createTime;

    String proofName;
    String judgeDocumentName;

    /**
     * 提交前检查
     *
     * @return
     */
    public boolean prepared() {

        //债类型
        int typeId = groupType.getCheckedRadioButtonId();

        if (typeId == R.id.rb_type_borrow_money) {
            type = CreditDebtInfo.TYPE_BORROW_MONEY;
        } else if (typeId == R.id.rb_type_contract) {
            type = CreditDebtInfo.TYPE_CONTRACT;
        } else if (typeId == R.id.rb_type_labor_disputes) {
            type = CreditDebtInfo.TYPE_LABOR_DISPUTES;
        } else if (typeId == R.id.rb_type_tort) {
            type = CreditDebtInfo.TYPE_TORT;
        } else {
            type = CreditDebtInfo.TYPE_OTHER;
        }

        //债状态
        int statusId = groupStatus.getCheckedRadioButtonId();

        if (statusId == R.id.rb_status_not_sue) {
            status = CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE;
        } else if (statusId == R.id.rb_status_judging) {
            status = CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING;
        } else if (statusId == R.id.rb_status_suing) {
            status = CreditDebtInfo.CREDIT_DEBT_STATUS_SUING;
        } else {
            status = CreditDebtInfo.CREDIT_DEBT_STATUS_OUT_OF_DATE;
        }

        //凭证名称
        int proofId = groupProofType.getCheckedRadioButtonId();

        if (proofId == R.id.rb_proof_hetong) {
            proofName = CreditDebtInfo.PROOF_TYPE_HETONG;
        } else if (proofId == R.id.rb_proof_qiantiao) {
            proofName = CreditDebtInfo.PROOF_TYPE_QIANTIAO;
        } else if (proofId == R.id.rb_proof_xieyi) {
            proofName = CreditDebtInfo.PROOF_TYPE_XIEYI;
        } else {
            proofName = CreditDebtInfo.PROOF_TYPE_QITA;
        }

        //判决书名称
        int judgeId = groupJudgeType.getCheckedRadioButtonId();

        if (judgeId == R.id.rb_judge_panjueshu) {
            judgeDocumentName = CreditDebtInfo.JUDGE_TYPE_PANJUESHU;
        } else if (judgeId == R.id.rb_judge_tiaojieshu) {
            judgeDocumentName = CreditDebtInfo.JUDGE_TYPE_TIAOJIESHU;
        } else if (judgeId == R.id.rb_judge_zhongcaishu) {
            judgeDocumentName = CreditDebtInfo.JUDGE_TYPE_ZHONGCAISHU;
        } else {
            judgeDocumentName = CreditDebtInfo.JUDGE_TYPE_QITA;
        }

        Editable descEditable = aq.id(R.id.et_credit_desc).getEditable();
        Editable moneyEditable = aq.id(R.id.et_credit_money).getEditable();

        if (TextUtils.isEmpty(moneyEditable)) {
            UIHelper.showToast(getContext(), "请填写金额");
            return false;
        }

        if (TextUtils.isEmpty(descEditable)) {
            UIHelper.showToast(getContext(), "请填写描述信息");
            return false;
        }

        if (TextUtils.isEmpty(createTime)) {
            UIHelper.showToast(getContext(), "请填写日期");
            return false;
        }

        Date create = DateFormatUtils.parse(createTime, "yyyy-MM-dd");
        Date now = new Date();

        if(create.after(now)){
            UIHelper.showToast(getActivity(),"创建时间不正确");
            return false;
        }

        money = moneyEditable.toString();

        desc = descEditable.toString();

        return true;
    }

    public CreditDebtInfo getCreditDebtInfo() {

        CreditDebtInfo info = new CreditDebtInfo();
        info.setType(type);
        info.setCreditDebtStatus(status);
        info.setDesc(desc);
        info.setMoney(Float.parseFloat(money));
        info.setCreditDebtTime(createTime);

        info.setProofName(proofName);
        info.setJudgeDocumentName(judgeDocumentName);

        return info;
    }

    public File getProofFile() {
        return proofFile;
    }

    public File getJudgeFile() {
        return judgeFile;
    }

    File mPickingFile = null;

    /**
     * 显示选择照片对对话框
     * 从相册选择，拍照
     */
    private void showChoosePicDialog() {
        // TODO Auto-generated method stub
        final String[] items = new String[]{getString(R.string.pick_img_from_album), getString(R.string.take_photo)};

        new AlertDialog.Builder(getActivity())
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

        mPickingFile = new File(AppConfig.getDir(Constants.imageDir), "CN_DLS_BANK_CARD.jpg");

        if (which == 0) {//从相册选择

            RxImagePicker.with(getActivity()).requestImage(Sources.GALLERY)
                    .flatMap(new Func1<Uri, Observable<File>>() {
                        @Override
                        public Observable<File> call(Uri uri) {
                            return RxImageConverters.uriToFile(getContext(), uri, createTempFile());
                        }
                    }).subscribe(new Action1<File>() {

                @Override
                public void call(File file) {
                    handleResult(file);
                }
            });

        } else {//拍照

            RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA)
                    .flatMap(new Func1<Uri, Observable<File>>() {
                        @Override
                        public Observable<File> call(Uri uri) {
                            return RxImageConverters.uriToFile(getActivity(), uri, createTempFile());
                        }
                    }).subscribe(new Action1<File>() {

                @Override
                public void call(File file) {
                    handleResult(file);
                }
            });

        }
    }

    private File createTempFile() {
        return new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpeg");
    }

    int choose = 0;

    private static int CHOOSE_PROOF_FILE = 0;
    private static int CHOOSE_JUDGE_FILE = 1;

    /**
     * 选完照片显示出来
     */
    private void handleResult(File file) {
        new BmTask(file).execute();
    }

    File proofFile;
    File judgeFile;

    private void showFile(File file) {

        if (choose == CHOOSE_PROOF_FILE) {

            proofFile = file;
            SimpleDraweeView image = (SimpleDraweeView) aq.id(R.id.image_proof).getView();
            image.setImageURI(Uri.fromFile(file));

        } else {

            judgeFile = file;
            SimpleDraweeView image = (SimpleDraweeView) aq.id(R.id.image_judge).getView();
            image.setImageURI(Uri.fromFile(file));

        }
    }

    class BmTask extends AsyncTask<Void, Void, Bitmap> {

        File file;
        Bitmap bitmap;

        ProgressDialog progressDialog;

        public BmTask(File file) {
            this.file = file;

            progressDialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("正在处理图片...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            float targetRatio = 0;

            if (height > 1920 || width > 1080) {

//            targetHeight = height = 1920;
//            targetWidth = (int) (height/ratio);

                if (height > 1920) {
                    targetRatio = (float) 1920 / height;
                } else if (width > 1080) {
                    targetRatio = (float) 1080 / width;
                }

                return FileUtils.ratio(bitmap, targetRatio);
//                return FileUtils.ratio(bitmap,targetRatio*width,targetRatio*height);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);

            if (bm != null) {

                FileUtils.saveImageFile(bm, AppConfig.getDir(Constants.imageDir), file.getName(), new FileUtils.FileCallback() {
                    @Override
                    public void onFileCallback(File file) {

                        bitmap = null;
                        progressDialog.dismiss();
                        showFile(file);
                    }
                });

            } else {
                progressDialog.dismiss();
                showFile(file);
                bitmap = null;

            }

        }
    }
}
