package com.lsm.barrister2c.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.List;

/**
 * 债券债务列表
 * <p>
 * String type;//债类型
 * float money;//债的金额
 * String desc;//债务描述
 * <p>
 * String addTime;//上传时间
 * String updateTime;//更新时间
 * <p>
 * String creditDebtTime;//债形成的时间
 * String creditDebtStatus;//债的状态
 */
public class CreditDebtListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<CreditDebtInfo> items;
    private final LoadMoreListener mListener;

    public CreditDebtListAdapter(List<CreditDebtInfo> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_debt, parent, false);

            holder = new ItemHolder(view);

            return holder;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);

            return holder = new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemHolder) {

            ItemHolder temp = (ItemHolder) holder;
            temp.bind(items.get(position));

        } else if (holder instanceof FooterViewHolder) {

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

        } else {
            return TYPE_ITEM;

        }
    }

    @Override
    public int getItemCount() {
        return items == null || items.isEmpty() ? 0 : (items.size() + 1);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public CreditDebtInfo mItem;

        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goCreditDebtDetailActivity(v.getContext(), mItem);
                }
            });
        }

        public void bind(CreditDebtInfo item) {
            mItem = item;

            String type;
            if (item.getType().equals(CreditDebtInfo.TYPE_CONTRACT)) {
                type = "合同欠款";
            } else if (item.getType().equals(CreditDebtInfo.TYPE_BORROW_MONEY)) {
                type = "借款";
            } else if (item.getType().equals(CreditDebtInfo.TYPE_TORT)) {
                type = "侵权";
            } else if (item.getType().equals(CreditDebtInfo.TYPE_LABOR_DISPUTES)) {
                type = "劳动与劳务";
            } else {
                type = "其它";
            }

            //类型
            aq.id(R.id.tv_credit_type).text(type);

            String status;
            if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
                status = "未起诉";
            } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
                status = "诉讼中";
            } else if (item.getStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
                status = "执行中";
            } else {
                status = "已过时效";
            }

            //状态
            aq.id(R.id.tv_credit_status).text(status);

            aq.id(R.id.tv_credit_desc).text(item.getDesc());
            aq.id(R.id.tv_credit_money).text(String.valueOf(item.getMoney()));
            aq.id(R.id.tv_credit_time).text(item.getUpdateTime());
            aq.id(R.id.tv_credit_addtime).text(item.getAddTime());

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
