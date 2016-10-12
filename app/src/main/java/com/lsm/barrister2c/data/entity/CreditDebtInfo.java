package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * Created by lvshimin on 16/9/25.
 * 债券债务信息
 */
public class CreditDebtInfo implements Serializable{

    //债状态
    public static final String CREDIT_DEBT_STATUS_NOT_SUE = "CREDIT_DEBT_STATUS_NOT_SUE";//未起诉
    public static final String CREDIT_DEBT_STATUS_SUING = "CREDIT_DEBT_STATUS_SUING";//诉讼中
    public static final String CREDIT_DEBT_STATUS_JUDGING = "CREDIT_DEBT_STATUS_JUDGING";//执行中
    public static final String CREDIT_DEBT_STATUS_OUT_OF_DATE = "CREDIT_DEBT_STATUS_OUT_OF_DATE";//已过时效

    //债类型
    public static final String TYPE_CONTRACT = "TYPE_CONTRACT";//合同欠款
    public static final String TYPE_BORROW_MONEY = "TYPE_BORROW_MONEY";//借款
    public static final String TYPE_TORT = "TYPE_TORT";//侵权
    public static final String TYPE_LABOR_DISPUTES = "TYPE_LABOR_DISPUTES";//劳动与劳务
    public static final String TYPE_OTHER= "TYPE_OTHER";//其它

    //凭证类型
    public static final String PROOF_TYPE_HETONG = "hetong";//合同
    public static final String PROOF_TYPE_XIEYI = "xieyi";//协议
    public static final String PROOF_TYPE_QIANTIAO = "qiantiao";//欠条
    public static final String PROOF_TYPE_QITA = "qita";//其他

    //判决文书类型
    public static final String JUDGE_TYPE_PANJUESHU = "panjueshu";//判决书
    public static final String JUDGE_TYPE_TIAOJIESHU = "tiaojieshu";//调解书
    public static final String JUDGE_TYPE_ZHONGCAISHU = "zhongcaishu";//仲裁书
    public static final String JUDGE_TYPE_QITA = "qita";//其他

    String id;
    String type;//债类型
    float money;//债的金额
//    String creditUserId;//债权人id --改成债券人对象
//    String debtUserId;//债务人id--改成债务人对象
    String desc;//债务描述
    String addTime;//上传时间
    String updateTime;//更新时间
    String creditDebtTime;//债形成的时间
    String creditDebtStatus;//债的状态
    String status;//信息状态,发布(审核通过)、未发布(刚上传)、已删除(垃圾信息)

    String proofName;//凭证类型名称(合同、协议、欠条等)
    String proof;//凭证
    String judgeDocumentName;//判决文书类型名称(判决书、调解书、仲裁书)
    String judgeDocument;//判决文书

    CreditDebtUser creditUser;//债权人
    CreditDebtUser debtUser;//债务人

    double price;//购买金额:上传成功后,后台管理员定价(建议根据债务金额百分比定价)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreditDebtTime() {
        return creditDebtTime;
    }

    public void setCreditDebtTime(String creditDebtTime) {
        this.creditDebtTime = creditDebtTime;
    }

    public String getCreditDebtStatus() {
        return creditDebtStatus;
    }

    public void setCreditDebtStatus(String creditDebtStatus) {
        this.creditDebtStatus = creditDebtStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProofName() {
        return proofName;
    }

    public void setProofName(String proofName) {
        this.proofName = proofName;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getJudgeDocumentName() {
        return judgeDocumentName;
    }

    public void setJudgeDocumentName(String judgeDocumentName) {
        this.judgeDocumentName = judgeDocumentName;
    }

    public String getJudgeDocument() {
        return judgeDocument;
    }

    public void setJudgeDocument(String judgeDocument) {
        this.judgeDocument = judgeDocument;
    }

    public CreditDebtUser getCreditUser() {
        return creditUser;
    }

    public void setCreditUser(CreditDebtUser creditUser) {
        this.creditUser = creditUser;
    }

    public CreditDebtUser getDebtUser() {
        return debtUser;
    }

    public void setDebtUser(CreditDebtUser debtUser) {
        this.debtUser = debtUser;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
