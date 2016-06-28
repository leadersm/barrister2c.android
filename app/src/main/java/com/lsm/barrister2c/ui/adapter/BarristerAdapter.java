package com.lsm.barrister2c.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.LearningItem;
import com.lsm.barrister2c.ui.UIHelper;

import java.util.ArrayList;
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

        List<BusinessArea> bizAreas = new ArrayList<>();
        ArrayAdapter<BusinessArea> aa;
        public ViewHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goBarristerDetailActivity(v.getContext(),mItem);
                }
            });

            aa = new ArrayAdapter<BusinessArea>(view.getContext(),R.layout.item_biz_area
                    ,R.id.tv_item_biz_name,bizAreas){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(R.id.tv_item_biz_name);
                    tv.setText(mItem.getBizAreas().get(position).getName());
                    return view;
                }
            };


            aq.id(R.id.gridview_goodat).adapter(aa);
        }

        public void bind(Barrister item) {
            mItem = item;
            aq.id(R.id.tv_item_barrister_company).text(item.getCompany());
            aq.id(R.id.tv_item_barrister_nickname).text(item.getName());
//            aq.id(R.id.tv_item_barrister_goodat).text(item.getGoodAt());
            aq.id(R.id.tv_item_barrister_area).text(item.getArea());
            aq.id(R.id.tv_item_barrister_year).text(item.getWorkYears()+"年");
            RatingBar rb = (RatingBar) aq.id(R.id.ratingbar_item_barrister).getView();
            rb.setRating(item.getRating());

            SimpleDraweeView userIconView = (SimpleDraweeView) aq.id(R.id.image_barrister_item_usericon).getView();
            if(!TextUtils.isEmpty(item.getUserIcon())){
                userIconView.setImageURI(Uri.parse(item.getUserIcon()));
            }

            //擅长领域
            if(item.getBizAreas()==null || item.getBizAreas().isEmpty()){
                aq.id(R.id.gridview_goodat).gone();
            }else{

                bizAreas.clear();
                bizAreas.addAll(item.getBizAreas());
                aa.notifyDataSetChanged();

                aq.id(R.id.gridview_goodat).visible();
            }

        }


    }
}
