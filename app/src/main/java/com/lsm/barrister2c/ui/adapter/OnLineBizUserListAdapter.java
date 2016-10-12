package com.lsm.barrister2c.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.app.UserHelper;
import com.lsm.barrister2c.data.entity.OnlineBizUser;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.List;

/**
 * 在线业务人员列表适配
 */
public class OnLineBizUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<OnlineBizUser> items;
    private final LoadMoreListener mListener;

    public OnLineBizUserListAdapter(List<OnlineBizUser> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_barrister, parent, false);

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

        public OnlineBizUser mItem;

        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //qq or 电话咨询

                }
            });

            aq.id(R.id.btn_item_call_qq).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User user = UserHelper.getInstance().getUser(v.getContext());

                    if(user ==null){
                        UIHelper.goLoginActivity((Activity) v.getContext());
                        return;
                    }

                    if(mItem==null || TextUtils.isEmpty(mItem.getQq()))
                        return;

                    UIHelper.startQQ(v.getContext(),mItem.getQq());
                }
            });

            aq.id(R.id.btn_item_call_phone).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User user = UserHelper.getInstance().getUser(v.getContext());

                    if(user ==null){
                        UIHelper.goLoginActivity((Activity) v.getContext());
                        return;
                    }

                    if(mItem==null || TextUtils.isEmpty(mItem.getPhone()))
                        return;

                    UIHelper.showCallView(v.getContext(),mItem.getPhone());
                }
            });

        }

        public void bind(OnlineBizUser item) {
            mItem = item;
            //简介
            aq.id(R.id.tv_item_barrister_intro).text(item.getIntro());
            //姓名
            aq.id(R.id.tv_item_barrister_nickname).text(item.getName());
            //头像
            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_barrister_item_usericon).getView();
            if(!TextUtils.isEmpty(item.getIcon())){
                userIconView.setImageURI(Uri.parse(item.getIcon()));
            }else{
                userIconView.setImageURI(null);
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
