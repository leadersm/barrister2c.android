package com.lsm.barrister2c.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.BusinessArea;

import java.util.List;

/**
 * Created by lvshimin on 16/8/14.
 */
public class BarristerBizAreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BusinessArea> bizAreas;

    public BarristerBizAreaAdapter(List<BusinessArea> bizAreas) {
        this.bizAreas = bizAreas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barrister_biz, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.bind(bizAreas.get(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return bizAreas == null ? 0 : bizAreas.size();

    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public BusinessArea item;

        AQuery aq;

        public ItemHolder(View itemView) {
            super(itemView);
            aq = new AQuery(itemView);
        }

        public void bind(BusinessArea item){
            this.item = item;
            aq.id(R.id.tv_item_biz_name).text(item.getName());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_item_casetype_icon).getView();
            if(!TextUtils.isEmpty(item.getIcon())){
                userIconView.setImageURI(Uri.parse(item.getIcon()));
            }

        }

    }
}
