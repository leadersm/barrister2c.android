package com.lsm.barrister2c.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
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
            temp.bind(items.get(position), position);

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


    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    private int selectIndex;


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public CreditDebtInfo item;

        AQuery aq;

        public ItemHolder(View view) {
            super(view);
            aq = new AQuery(view);
            aq.clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goCreditDebtDetailActivity(v.getContext(), item);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (editable) {

                        if (item.getStatus().equals("published")) {
                            return false;
                        }

                        view.showContextMenu();
                        setSelectIndex(position);
                        return true;
                    }
                    return false;
                }
            });

            view.setOnCreateContextMenuListener(this);
        }

        int position;

        public void bind(CreditDebtInfo item, int position) {
            this.position = position;
            this.item = item;

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
            aq.id(R.id.tv_credit_type).text("债务类型：" + type);

            String creditStatus;

            if (!TextUtils.isEmpty(item.getCreditDebtStatus())) {

                if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_NOT_SUE)) {
                    creditStatus = "未起诉";
                } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_SUING)) {
                    creditStatus = "诉讼中";
                } else if (item.getCreditDebtStatus().equals(CreditDebtInfo.CREDIT_DEBT_STATUS_JUDGING)) {
                    creditStatus = "执行中";
                } else {
                    creditStatus = "已过时效";
                }

            } else {
                creditStatus = "未起诉";
            }

            //状态
            aq.id(R.id.tv_credit_status).text("状态：" + creditStatus);

            aq.id(R.id.tv_credit_desc).text(item.getDesc());

            String money = (isCenterList ? "债务金额" : "金额") + "：<font color=\"#ff0000\">" + item.getMoney() + "</font>";
            aq.id(R.id.tv_credit_money).text(Html.fromHtml(money));

            aq.id(R.id.tv_credit_time).text("形成时间：" + (TextUtils.isEmpty(item.getCreditDebtTime()) ? "未知" : item.getCreditDebtTime()));

            aq.id(R.id.tv_credit_addtime).text("添加时间：" + item.getAddTime());

            if (editable) {
                String status = "";
                int color = aq.getContext().getResources().getColor(R.color.yellow02);
                if (item.getStatus().equals("unpublished")) {
                    status = "未发布";
                    color = aq.getContext().getResources().getColor(R.color.yellow02);
                } else if (item.getStatus().equals("published")) {
                    status = "已发布";
                    color = aq.getContext().getResources().getColor(R.color.yellowGreen02);
                }

                aq.id(R.id.tv_status).text(status).textColor(color);

            } else {
                aq.id(R.id.tv_status).gone();
            }

            CreditDebtUser debtUser = item.getDebtUser();

            if (debtUser != null) {

                System.out.println(debtUser.toString());

                aq.id(R.id.layout_debt_user).visible();

                if (TextUtils.isEmpty(debtUser.getName())) {
                    aq.id(R.id.tv_debt_user_name).gone();
                } else {

                    String debtUserName = "联系人:<font color=\"#2bacfe\">" + (TextUtils.isEmpty(debtUser.getName()) ? "未知" : debtUser.getName()) + "</font>";
                    aq.id(R.id.tv_debt_user_name).text(Html.fromHtml(debtUserName)).visible();

                }

                if (!TextUtils.isEmpty(debtUser.getPhone())) {
                    aq.id(R.id.tv_debt_user_phone).text("电话:" + debtUser.getPhone()).visible();
                } else {
                    aq.id(R.id.tv_debt_user_phone).gone();
                }

                if (!TextUtils.isEmpty(debtUser.getID_number())) {
                    aq.id(R.id.tv_debt_user_id_number).text("身份证号码:" + debtUser.getID_number()).visible();
                } else {
                    aq.id(R.id.tv_debt_user_id_number).gone();
                }

                if (!TextUtils.isEmpty(debtUser.getCompany())) {
                    String company = "公司名称:<font color=\"#2bacfe\">" + (TextUtils.isEmpty(debtUser.getCompany()) ? "未知" : debtUser.getCompany()) + "</font>";

                    aq.id(R.id.tv_debt_user_company).text(Html.fromHtml(company)).visible();
                } else {
                    aq.id(R.id.tv_debt_user_company).gone();
                }


                if (!TextUtils.isEmpty(debtUser.getCompanyPhone())) {
                    aq.id(R.id.tv_debt_company_phone).text("公司电话:" + debtUser.getCompanyPhone()).visible();

                } else {
                    aq.id(R.id.tv_debt_company_phone).gone();
                }

                if (!TextUtils.isEmpty(debtUser.getLicenseNuber())) {
                    aq.id(R.id.tv_debt_liscense_number).text("信用代码:" + debtUser.getLicenseNuber()).visible();
                } else {
                    aq.id(R.id.tv_debt_liscense_number).gone();
                }

                if (!TextUtils.isEmpty(debtUser.getLicenseNuber())) {
                    aq.id(R.id.tv_debt_address).text("联系地址:" + debtUser.getAddress()).visible();
                } else {
                    aq.id(R.id.tv_debt_address).gone();
                }

            } else {
                aq.id(R.id.layout_debt_user).gone();
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("操作");
//            menu.add(0, 0, 0, "修改");
            menu.add(0, 1, 0, "删除");
        }

    }

    public void setCenterList(boolean centerList) {
        isCenterList = centerList;
    }

    boolean isCenterList = false;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    boolean editable = false;


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
