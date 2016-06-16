package com.lsm.barrister2c.data.entity;

import java.io.Serializable;

/**
 * 用户
 */
public class User implements Serializable {

    public static final String KEY_GENDER = "gender";
    public static final String KEY_AGE = "age";
    public static final String KEY_EMAIL = "mail";
    public static final String KEY_GOOD_AT = "goodAt";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_NICKNAME = "nickName";
    public static final String KEY_USERICON = "userIcon";
    public static final String KEY_INTRODUCTION = "introduction";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_AREA = "area";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PUSHID = "pushId";
    public static final String KEY_COMPANY = "company";

    String id;//用户id
    String nickname;//昵称
    String userIcon;//用户头像
    String name;//姓名
    String phone;//电话
    String email;//邮箱
    String age;//年龄：60后，70后，80后，90后，00后
    String introduction;//自我介绍
    String gender;//性别
    String address;//通信地址
    String state;//省、州
    String city;//市
    String area;//地区
    String location;//暂时无用，保留字段；位置信息 经纬度：x，y，逗号分隔
    String pushId;//推送id

    String verifyCode;//验证码 ，动态密码，每次用户相关操作需带此参数。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
