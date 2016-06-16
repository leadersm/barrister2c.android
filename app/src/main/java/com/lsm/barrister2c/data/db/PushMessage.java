package com.lsm.barrister2c.data.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PUSH_MESSAGE.
 */
public class PushMessage {

    public static final String TYPE_SYSTEM_MSG = "msg.type.system";//系统消息
    public static final String TYPE_NEWS = "msg.type.news";//新闻
    public static final String TYPE_FORCE_UPDATE = "msg.type.update.app.force";//更新APP
    public static final String TYPE_ORDER_MSG = "msg.type.order";//订单消息
    public static final String TYPE_CLEAR_APP_DATA = "msg.type.app.clear";//清除APP数据
    public static final String TYPE_UPLOAD_PUSHID = "msg.type.app.upload.pushId";//上传pushId
    public static final String TYPE_AD = "msg.type.ad";//广告


    /** Not-null value. */
    private String id;
    /** Not-null value. */
    private String type;
    private String icon;
    private String title;
    private String digest;
    private String contentId;
    private String date;
    private Boolean read;
    private Boolean deleteFlag;

    public PushMessage() {
    }

    public PushMessage(String id) {
        this.id = id;
    }

    public PushMessage(String id, String type, String icon, String title, String digest, String contentId, String date, Boolean read, Boolean deleteFlag) {
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.title = title;
        this.digest = digest;
        this.contentId = contentId;
        this.date = date;
        this.read = read;
        this.deleteFlag = deleteFlag;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getType() {
        return type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}