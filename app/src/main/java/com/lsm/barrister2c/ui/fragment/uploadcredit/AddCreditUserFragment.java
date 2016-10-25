package com.lsm.barrister2c.ui.fragment.uploadcredit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.androidquery.AQuery;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.ui.UIHelper;

/**
 * Created by lvshimin on 16/10/12.
 * 填写债权人
 */
public class AddCreditUserFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_credit_user,container,false);
        init(view);
        return view;
    }

    AQuery aq;
    RadioGroup groupType;
    private void init(View view) {

        groupType = (RadioGroup) view.findViewById(R.id.group_type);

        aq = new AQuery(view);

        aq.id(R.id.rb_type_user).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.layout_user).visible();
                aq.id(R.id.layout_company).gone();
            }
        });

        aq.id(R.id.rb_type_company).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.layout_user).gone();
                aq.id(R.id.layout_company).visible();
            }
        });

    }

    String type = CreditDebtUser.TYPE_CREDIT;

    String name;//姓名(个人)
    String phone;//个人电话
    String ID_number;//身份证号码

    String company;//单位
    String companyPhone;//公司电话
    String licenseNuber;//信用代码

    String address;//联系地址

    public boolean prepared(){

        int typeId = groupType.getCheckedRadioButtonId();

        if(typeId == R.id.rb_type_user){

            Editable nameEditable = aq.id(R.id.et_user_name).getEditable();
            Editable userPhoneEditable = aq.id(R.id.et_user_phone).getEditable();
            Editable idNumEditable = aq.id(R.id.et_user_id_num).getEditable();

            if(TextUtils.isEmpty(nameEditable)){
                UIHelper.showToast(getContext(),"请填写债权人联系人");
                return false;
            }

            if(TextUtils.isEmpty(userPhoneEditable)){
                UIHelper.showToast(getContext(),"请填写债权人电话");
                return false;
            }

            if(TextUtils.isEmpty(idNumEditable)){
                UIHelper.showToast(getContext(),"请填写债权人身份证号码");
                return false;
            }

            name = nameEditable.toString();
            phone = userPhoneEditable.toString();
            ID_number = idNumEditable.toString();

        }else{

            Editable companyEditable = aq.id(R.id.et_company_name).getEditable();
            Editable companyPhoneEditable = aq.id(R.id.et_company_phone).getEditable();
            Editable companyNumEditable = aq.id(R.id.et_company_liscense_num).getEditable();

            if(TextUtils.isEmpty(companyEditable)){
                UIHelper.showToast(getContext(),"请填写债权人公司名称");
                return false;
            }

            if(TextUtils.isEmpty(companyPhoneEditable)){
                UIHelper.showToast(getContext(),"请填写债权人公司电话");
                return false;
            }

            if(TextUtils.isEmpty(companyNumEditable)){
                UIHelper.showToast(getContext(),"请填写债权人公司信用代码");
                return false;
            }

            company = companyEditable.toString();
            companyPhone = companyPhoneEditable.toString();
            licenseNuber = companyNumEditable.toString();
        }

        Editable addressEditable = aq.id(R.id.et_address).getEditable();

        if(TextUtils.isEmpty(addressEditable)){
            UIHelper.showToast(getContext(),"请填写债权人联系地址");
            return false;
        }

        address = addressEditable.toString();

        return true;
    }


    public CreditDebtUser getCreditUser(){

        CreditDebtUser user = new CreditDebtUser();

        user.setType(type);

        user.setName(name);
        user.setPhone(phone);
        user.setID_number(ID_number);

        user.setCompany(company);
        user.setCompanyPhone(companyPhone);
        user.setLicenseNuber(licenseNuber);

        user.setAddress(address);

        return user;
    }

}
