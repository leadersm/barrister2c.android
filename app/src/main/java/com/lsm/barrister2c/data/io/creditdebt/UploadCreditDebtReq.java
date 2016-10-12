package com.lsm.barrister2c.data.io.creditdebt;

import android.content.Context;

import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.CreditDebtUser;
import com.lsm.barrister2c.data.io.Action;
import com.lsm.barrister2c.data.io.IO;

import java.io.File;

/**
 * Created by lvshimin on 16/10/10.
 * 35.+上传接口(债券、债务信息)
 *   uploadCreditDebt
 *   提交方式：post
 *   参数:userId,verifyCode,CreditDebtInfo （所有字段）
 *   返回值：resultCode，resultMsg
 *   备注：
 */
public class UploadCreditDebtReq extends Action {


    CreditDebtInfo data;

//    String type;//债类型
//    float money;//债的金额
//    String desc;//债务描述
//    String creditDebtTime;//债形成的时间
//    String creditDebtStatus;//债的状态
//
//    String proofName;//凭证类型名称(合同、协议、欠条等)
//    String proof;//凭证
//    String judgeDocumentName;//判决文书类型名称(判决书、调解书、仲裁书)
//    String judgeDocument;//判决文书
//
//    CreditDebtUser creditUser;//债权人
//    CreditDebtUser debtUser;//债务人

    public UploadCreditDebtReq(Context context, CreditDebtInfo data, File proof, File judgeDocument) {
        super(context);
        this.data = data;

        addUserParams();

        if(proof!=null)
            addFile("proof",proof);

        if(judgeDocument!=null)
            addFile("judgeDocument",judgeDocument);


        params("money",String.valueOf(data.getMoney()));
        params("desc",data.getDesc());
        params("creditDebtTime",data.getCreditDebtTime());
        params("creditDebtStatus",data.getCreditDebtStatus());
        params("proofName",data.getProofName());
        params("judgeDocumentName",data.getJudgeDocumentName());

//        String type;
//        String name;//姓名(个人)
//        String phone;//个人电话
//        String ID_number;//身份证号码
//
//        String company;//单位
//        String companyPhone;//公司电话
//        String licenseNuber;//信用代码
//
//        String address;//联系地址

        //===================债权人====================================
        CreditDebtUser creditUser = data.getCreditUser();

        params("creditName",creditUser.getName());
        params("creditPhone",creditUser.getPhone());
        params("creditID_number",creditUser.getID_number());
        params("creditCompany",creditUser.getCompany());
        params("creditCompanyPhone",creditUser.getCompanyPhone());
        params("creditLicenseNuber",creditUser.getLicenseNuber());
        params("creditAddress",creditUser.getAddress());

        //======================债务人=================================
        CreditDebtUser debtUser = data.getDebtUser();

        params("debtName",debtUser.getName());
        params("debtPhone",debtUser.getPhone());
        params("debtID_number",debtUser.getID_number());
        params("debtCompany",debtUser.getCompany());
        params("debtCompanyPhone",debtUser.getCompanyPhone());
        params("debtLicenseNuber",debtUser.getLicenseNuber());
        params("debtAddress",debtUser.getAddress());

    }

    @Override
    public String getName() {
        return UploadCreditDebtReq.class.getSimpleName();
    }

    @Override
    public String url() {
        return IO.URL_UPLOAD_CREDITDEBT;
    }

    @Override
    public CommonResult parse(String json) throws Exception {
        return parseCommonResult(json);
    }

    @Override
    public int method() {
        return POST;
    }
}
