package com.lsm.barrister2c.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.LawApp;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;
import com.lsm.barrister2c.data.io.app.GetBarristerListReq;
import com.lsm.barrister2c.data.io.app.GetLawAppListReq;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.adapter.LawAppAdapter;
import com.lsm.barrister2c.utils.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 * 咨询:即时，预约
 * 中国法律应用大全
 */
public class FaxianFragment extends Fragment {

    private static final String TAG = FaxianFragment.class.getSimpleName();

    LawAppAdapter mLawAppListAdapter;
    GridLayoutManager mLawAppListLayoutManager;
    RecyclerView mLawAppListView;

    public FaxianFragment() {
    }

    public static FaxianFragment newInstance() {
        FaxianFragment fragment = new FaxianFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    AQuery aq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faxian, container, false);

        init(view);
        return view;
    }

    List<LawApp> items = new ArrayList<>();

    private void init(View view) {

        aq = new AQuery(view);

        aq.id(R.id.btn_faxian_appointment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goBarristerListAcitivity(v.getContext(), GetBarristerListReq.TYPE_APPOINTMENT,null,null);
            }
        });
        aq.id(R.id.btn_faxian_im).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.goBarristerListAcitivity(v.getContext(), GetBarristerListReq.TYPE_IM,null,null);
            }
        });

        mLawAppListView = (RecyclerView) view.findViewById(R.id.recyclerview_faxian_apps);
        mLawAppListLayoutManager = new GridLayoutManager(getActivity(), 3);
        mLawAppListAdapter = new LawAppAdapter(items);
        mLawAppListView.setItemAnimator(new DefaultItemAnimator());
        mLawAppListView.setLayoutManager(mLawAppListLayoutManager);
        mLawAppListView.setAdapter(mLawAppListAdapter);

        loadLawApps();
    }

    private void loadLawApps() {
        new GetLawAppListReq(getContext()).execute(new Action.Callback<IO.GetLawAppListResult>() {
            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getContext(),msg);
            }

            @Override
            public void onCompleted(IO.GetLawAppListResult result) {

                if (result.lawAppList != null) {

                    FaxianFragment.this.items.clear();
                    FaxianFragment.this.items.addAll(result.lawAppList);

                    mLawAppListAdapter.notifyDataSetChanged();
                }else {
                    DLog.e(TAG,"lawAppList is null");
                }
            }
        });

    }


}
