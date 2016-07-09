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
import com.lsm.barrister2c.data.db.Favorite;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.List;

/**
 * 收藏列表适配器
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<Favorite> items;
    private final LoadMoreListener mListener;

    public FavoriteAdapter(List<Favorite> items, LoadMoreListener loadMoreListener) {
        this.items = items;
        mListener = loadMoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
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

        public Favorite mItem;

        AQuery aq;

        public ViewHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goBarristerDetailActivity(v.getContext(),mItem.getId());
                }
            });

            aq.id(R.id.gridview_goodat).gone();
        }

        public void bind(Favorite item) {
            mItem = item;
            aq.id(R.id.tv_item_barrister_nickname).text(item.getTitle());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_barrister_item_usericon).getView();
            if(!TextUtils.isEmpty(item.getThumb())){
                userIconView.setImageURI(Uri.parse(item.getThumb()));
            }

        }


    }
}
