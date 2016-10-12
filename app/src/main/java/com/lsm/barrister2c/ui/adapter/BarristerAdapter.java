package com.lsm.barrister2c.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 律师列表
 */
public class BarristerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Barrister> items;
    private final LoadMoreListener mListener;

    public BarristerAdapter(List<Barrister> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barrister, parent, false);

            holder = new ItemHolder(view);

            return holder;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);

            return holder = new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemHolder){

            ItemHolder temp = (ItemHolder) holder;
            temp.bind(items.get(position));

        }else if (holder instanceof FooterViewHolder) {

            FooterViewHolder footer = (FooterViewHolder) holder;

            if (mListener.hasMore()) {
                footer.showLoadMore();
                mListener.onLoadMore();
            } else {
                footer.showNoMore();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {

            // 最后一个item设置为footerView
            return TYPE_FOOTER;

        }else {
            return TYPE_ITEM;

        }
    }

    @Override
    public int getItemCount() {
        return items == null || items.isEmpty() ? 0 : (items.size() + 1);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public Barrister mItem;

        AQuery aq;

        RecyclerView mRecyclerView;
        LinearLayoutManager mLayoutManager;
        List<BusinessArea> bizAreas = new ArrayList<>();
        BarristerBizAreaAdapter mAdapter;
        public ItemHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goBarristerDetailActivity(v.getContext(),mItem);
                }
            });

            mAdapter = new BarristerBizAreaAdapter(bizAreas);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.gridview_goodat);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new BarristerBizAreaAdapter(bizAreas);
            mRecyclerView.setAdapter(mAdapter);

        }

        public void bind(Barrister item) {
            mItem = item;
            aq.id(R.id.tv_item_barrister_company).text(item.getCompany());
            aq.id(R.id.tv_item_barrister_nickname).text(item.getName());
//            aq.id(R.id.tv_item_barrister_goodat).text(item.getGoodAt());
            aq.id(R.id.tv_item_barrister_area).text(item.getArea());

            String workYears = item.getWorkYears();
            if(TextUtils.isEmpty(workYears)){
                aq.id(R.id.tv_item_barrister_year).gone();
            }else{
                aq.id(R.id.tv_item_barrister_year).text(workYears + "年").visible();
            }

            RatingBar rb = (RatingBar) aq.id(R.id.ratingbar_item_barrister).getView();
            rb.setRating(item.getRating());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_barrister_item_usericon).getView();
            if(!TextUtils.isEmpty(item.getUserIcon())){
                userIconView.setImageURI(Uri.parse(item.getUserIcon()));
            }else{
                userIconView.setImageURI(null);
            }

//            System.out.println(">>>"+item.getName()+","+item.getBizAreas());

            //擅长领域
            if(item.getBizAreas()==null || item.getBizAreas().isEmpty()){
                aq.id(R.id.gridview_goodat).gone();
            }else{

                aq.id(R.id.gridview_goodat).visible();

                bizAreas.clear();
                bizAreas.addAll(item.getBizAreas());
                mAdapter.notifyDataSetChanged();

            }

            if(item.getIsExpert() == 1){
                aq.id(R.id.tv_item_barrister_expert).visible();
            }else{
                aq.id(R.id.tv_item_barrister_expert).gone();
            }

        }

    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View view;

        public FooterViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void showNoMore() {
            // TODO Auto-generated method stub
            TextView tv = (TextView) view.findViewById(R.id.tv_item_loadingmore);

            view.findViewById(R.id.progress_item_loadingmore).setVisibility(View.GONE);

            tv.setText("没有更多了");
        }

        public void showLoadMore() {
            TextView tv = (TextView) view.findViewById(R.id.tv_item_loadingmore);

            view.findViewById(R.id.progress_item_loadingmore).setVisibility(View.VISIBLE);

            tv.setText("加载中,请稍候...");
        }

    }

}
