package com.lsm.barrister2c.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.LearningItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LearningItem} and makes a call to the
 * specified {@link LoadMoreListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BarristerAdapter extends RecyclerView.Adapter<BarristerAdapter.ViewHolder> {

    private final List<Barrister> items;
    private final LoadMoreListener mListener;

    public BarristerAdapter(List<Barrister> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barrister, parent, false);
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

        public Barrister mItem;

        AQuery aq;

        public ViewHolder(View view) {
            super(view);
            aq = new AQuery(view);
        }

        public void bind(Barrister item) {
            mItem = item;
            aq.id(R.id.tv_item_barrister_company).text(item.getCompany());
            aq.id(R.id.tv_item_barrister_nickname).text(item.getName());
            aq.id(R.id.tv_item_barrister_goodat).text(item.getGoodAt());
            aq.id(R.id.tv_item_barrister_area).text(item.getArea());
            RatingBar rb = (RatingBar) aq.id(R.id.ratingbar_item_barrister).getView();
            rb.setRating(item.getRating());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_barrister_item_usericon).getView();
            if(!TextUtils.isEmpty(item.getUserIcon())){
                userIconView.setImageURI(Uri.parse(item.getUserIcon()));
            }

        }


    }
}
