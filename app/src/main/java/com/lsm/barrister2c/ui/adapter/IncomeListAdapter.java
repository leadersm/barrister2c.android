package com.lsm.barrister2c.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.ConsumeDetail;

import java.util.List;

/**
 * 学习中心适配器
 */
public class IncomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<ConsumeDetail> items;
    private final LoadMoreListener mListener;

    public IncomeListAdapter(List<ConsumeDetail> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_detail, parent, false);

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
        public final View mView;
        public ConsumeDetail mItem;
        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            aq = new AQuery(view);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(ConsumeDetail item) {
            mItem = item;

            String type = "其他";
            if(item.getType().equals(ConsumeDetail.TYPE_GET_MONEY)){
                type = "提现";
            }else if(item.getType().equals(ConsumeDetail.TYPE_ORDER)){
                type = "订单收入";
            }else if(item.getType().equals(ConsumeDetail.TYPE_REWARD)){
                type = "打赏";
            }

            aq.id(R.id.tv_item_income_cash).text(type);
            aq.id(R.id.tv_item_income_date).text(item.getDate());
            aq.id(R.id.tv_item_income_serial_num).text(item.getSerialNum());
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

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
