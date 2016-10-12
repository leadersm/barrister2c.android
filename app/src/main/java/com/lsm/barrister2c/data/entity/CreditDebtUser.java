package com.lsm.barrister2c.data.entity;

/**
 * Created by lvshimin on 16/9/25.
 * 债权人、债务人
 */
public class CreditDebtUser {


    public static final String TYPE_CREDIT = "credit";//债权人
    public static final String TYPE_DEBT = "debt";//债务人

    String id;
    String type;
    String company;//单位
    String name;//姓名(个人)
    String address;//联系地址
    String phone;//个人电话
    String companyPhone;//公司电话
    String licenseNuber;//信用代码
    String ID_number;//身份证号码

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getLicenseNuber() {
        return licenseNuber;
    }

    public void setLicenseNuber(String licenseNuber) {
        this.licenseNuber = licenseNuber;
    }

    public String getID_number() {
        return ID_number;
    }

    public void setID_number(String ID_number) {
        this.ID_number = ID_number;
    }
}
