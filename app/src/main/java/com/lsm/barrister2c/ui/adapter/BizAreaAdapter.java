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
import com.lsm.barrister2c.data.entity.LearningItem;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LearningItem} and makes a call to the
 * specified {@link LoadMoreListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BizAreaAdapter extends RecyclerView.Adapter<BizAreaAdapter.ViewHolder> {

    private final List<BusinessArea> items;

    public BizAreaAdapter(List<BusinessArea> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_area, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public BusinessArea mItem;

        AQuery aq;

        public ViewHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goBarristerListAcitivity(v.getContext(),"all",null,mItem);
                }
            });
        }

        public void bind(BusinessArea item) {
            mItem = item;
            aq.id(R.id.tv_item_biz_name).text(item.getName());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_item_casetype_icon).getView();
            if(!TextUtils.isEmpty(item.getIcon())){
                userIconView.setImageURI(Uri.parse(item.getIcon()));
            }

        }


    }
}
