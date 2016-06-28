package com.lsm.barrister2c.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.AppointmentSetting;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetAppointmentSettingsReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.utils.DLog;
import com.lsm.barrister2c.utils.DateFormatUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lvshimin on 16/6/19.
 */
public class AppointmentPickDialogFragment extends DialogFragment {

    private static final String TAG = AppointmentPickDialogFragment.class.getSimpleName();

    ViewPager viewPager;
    DatePagerAdapter mAdapter;

    List<AppointmentSetting> data = new ArrayList<>();
    SmartTabLayout viewPagerTab;

    String id;

    public static AppointmentPickDialogFragment getInstance(String id) {
        AppointmentPickDialogFragment fragment = new AppointmentPickDialogFragment();
        Bundle b = new Bundle();
        b.putString("id", id);
        fragment.setArguments(b);
        return fragment;
    }

    String[] hourStrs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        hourStrs = getResources().getStringArray(R.array.hours);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        data.clear();
        data = null;
        
        myPick.clear();

    }

    /**
     * 加载预约设置
     */
    private void loadAppointmentSettings() {

        final String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");

        new GetAppointmentSettingsReq(getActivity(), id, today).execute(new Action.Callback<IO.GetAppointmentSettingsResult>() {

            @Override
            public void progress() {
                aq.id(R.id.progressbar_dialog).visible();
            }

            @Override
            public void onError(int errorCode, String msg) {
                aq.id(R.id.progressbar_dialog).gone();
                if(isAdded()){
                    UIHelper.showToast(getContext(), msg);
                }
            }

            @Override
            public void onCompleted(IO.GetAppointmentSettingsResult result) {
                aq.id(R.id.progressbar_dialog).gone();

                if (result != null && result.appointmentSettings != null) {

                    if(isAdded()){
                        new DateSettingsTask(today, result.appointmentSettings).execute();
                    }
                }
            }
        });
    }


    class DateSettingsTask extends AsyncTask<Void, Void, List<AppointmentSetting>> {

        String today;
        List<AppointmentSetting> temp;

        public DateSettingsTask(String today, List<AppointmentSetting> temp) {
            this.today = today;
            this.temp = temp;
        }

        @Override
        protected List<AppointmentSetting> doInBackground(Void... params) {

            int size = temp.size();

            if (size < 7) {
                String lastDay = temp.size() < 1 ? today : temp.get(temp.size() - 1).getDate();
                Date lastDate = DateFormatUtils.parse(lastDay, "yyyy-MM-dd");

                for (int i = 0; i < 7 - size; i++) {
                    String settings = "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
                    Date date = new Date(lastDate.getTime() + (i + 1) * 24 * 3600 * 1000);
                    String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
                    DLog.d(TAG,"自动补全日期:" + dateStr);
                    AppointmentSetting tempSettings = new AppointmentSetting();
                    tempSettings.setDate(dateStr);
                    tempSettings.setSettings(settings);
                    temp.add(tempSettings);
                }
            }

            for (int i = 0; i < temp.size(); i++) {

                AppointmentSetting item = temp.get(i);
                String[] flags = item.getSettings().split(",");

                List<AppointmentSetting.HourItem> hours = new ArrayList<>();

                for (int j = 0; j < flags.length; j++) {
                    AppointmentSetting.HourItem hour = new AppointmentSetting.HourItem();
                    hour.setEnable(flags[j].equals("1"));
                    hour.setSoldOut(flags[j].equals("2"));
                    hour.setHour(hourStrs[12 + j]);
                    hours.add(hour);
                }

                item.setHours(hours);

            }

            return temp;
        }

        @Override
        protected void onPostExecute(List<AppointmentSetting> result) {
            super.onPostExecute(result);

            try {

                if(isAdded()){

                    data.clear();
                    data.addAll(result);
                    mAdapter.notifyDataSetChanged();
                    viewPagerTab.setViewPager(viewPager);

                    temp = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = -1;
        attributes.height = -2;

        dialog.setTitle(R.string.pick_appointment_time);

        return dialog;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


    AQuery aq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_barrister_detail_appointment, container, false);

        aq = new AQuery(view);

        mAdapter = new DatePagerAdapter(getChildFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(mAdapter);

        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        loadAppointmentSettings();

        aq.id(R.id.btn_dialog_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                showEnsureDialog();
            }
        });

        return view;
    }

    float total;
    /**
     * 确定购买？？天+时段+金额
     */
    private void showEnsureDialog() {
        
        if(myPick.isEmpty()){
            UIHelper.showToast(getContext(),getString(R.string.tip_no_pick));
            return;
        }

        total = 0;

        StringBuffer sb = new StringBuffer();

        for (String date : myPick.keySet()) {

            HashMap<Integer,String> temp = myPick.get(date);

            sb.append(date + "：");

            for (int position : temp.keySet()) {

                String hour = temp.get(position);
                sb.append(hour + ",");
                total += 5;
            }

            sb.append("\n\n");

        }

        sb.append("共计："+total+"元");

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ensure, null);

        new AlertDialog.Builder(getContext()).setTitle("确定购买以下时间段的预约服务？").setMessage(sb.toString())//.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCommit(total);
                    }
                }).setNegativeButton(R.string.repick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void doCommit(float total) {
        JSONArray jsonArray = new JSONArray();

        JSONArray dateSettings = new JSONArray();

        try {

            JSONObject obj = null;
            JSONObject settings = null;

            StringBuffer times;
            for (String date : AppointmentPickDialogFragment.myPick.keySet()) {

                HashMap<Integer,String> temp = AppointmentPickDialogFragment.myPick.get(date);

                for (int position : temp.keySet()) {

                    obj = new JSONObject();

                    obj.put("date", date);
                    obj.put("index", position);
                    obj.put("time", temp.get(position));

                    jsonArray.put(obj);

                    settings = new JSONObject();

                    settings.put("date",date);
                    times = new StringBuffer();
                    for(int i=0;i<36;i++){
                        if(position == i){
                            times.append("1,");
                        }else {
                            times.append("0,");
                        }
                    }

                    settings.put("value", times.toString());

                    dateSettings.put(settings);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        callback.onAppointmentOrderPrepared(jsonArray.toString(),total,dateSettings.toString());
    }

    /**
     * @author lsm
     * @date 2015-4-6
     */
    public class DatePagerAdapter extends FragmentStatePagerAdapter {

        public DatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getDate();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = DateFragment.newInstance(data.get(position));

            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }

    public static HashMap<String,HashMap<Integer,String>> myPick = new HashMap<>();

    public static class DateFragment extends Fragment {

        public DateFragment() {
        }

        public static DateFragment newInstance(AppointmentSetting date) {
            DateFragment fragment = new DateFragment();
            Bundle args = new Bundle();
            args.putSerializable("daySettings", date);
            fragment.setArguments(args);
            return fragment;
        }

        AppointmentSetting daySettings;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            daySettings = (AppointmentSetting) getArguments().getSerializable("daySettings");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_date, container, false);
            init(view);
            return view;
        }

        GridView mGridView;
        HalfHourAdapter mHourAdapter;

        private void init(View view) {

            mGridView = (GridView) view.findViewById(R.id.gridView);
            mHourAdapter = new HalfHourAdapter(getActivity(), R.layout.item_half_hour, daySettings.getHours());
            mGridView.setAdapter(mHourAdapter);
        }

        HashMap<Integer,String> hourPics = new HashMap<>();

        public void onHourClick(int position, AppointmentSetting.HourItem hour){

            if(hour.isEnable()){

                hourPics.put(position,hour.getHour());

                if(!myPick.containsKey(daySettings.getDate())){
                    myPick.put(daySettings.getDate(),hourPics);
                }

            }else{

                hourPics.remove(position);

                if(hourPics.isEmpty()){
                    myPick.remove(daySettings.getDate());
                }
            }

        }

        class HalfHourAdapter extends ArrayAdapter {

            public HalfHourAdapter(Context context, int resource, List objects) {
                super(context, resource, objects);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                final AQuery holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_half_hour, parent, false);
                    holder = new AQuery(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (AQuery) convertView.getTag();
                }

                final AppointmentSetting.HourItem hour = (AppointmentSetting.HourItem) getItem(position);

                ///状态 "1" 可接受预约，未选中，可用
                holder.id(R.id.cb_item_half_hour)
                        .enabled(hour.isEnable())
                        .checked(hour.isSoldOut())
                        .text(hour.getHour()).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hour.setEnable(holder.id(R.id.cb_item_half_hour).isChecked());

                        onHourClick(position,hour);

                    }
                });

                return convertView;
            }
        }
    }


    Callback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof Callback){
            callback = (Callback) activity;
        }
    }

    public interface Callback{
        public void onAppointmentOrderPrepared(String orderInfo, float price, String dateSettings);
    }


}
