package com.lsm.barrister2c.data.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PUSH_MESSAGE.
 */
public class PushMessage {

	
	//=================COMMON==================================================
    public static final String TYPE_FORCE_UPDATE = "msg.type.update.app.force";//更新APP
    public static final String TYPE_CLEAR_APP_DATA = "msg.type.app.clear";//清除APP数据
    public static final String TYPE_UPLOAD_PUSHID = "msg.type.app.upload.pushId";//上传pushId
    //===========================================================================
    public static final String TYPE_ORDER_STATUS = "type.order.status.changed";//订单状态改变
    public static final String TYPE_RECEIVE_STAR = "type.receive.star";//收到评价
    public static final String TYPE_ORDER_REWARD = "type.receive.order.reward";//收到打赏
    public static final String TYPE_ORDER_MONEY = "type.receive.order.money";//订单服务费到账
    public static final String TYPE_ORDER_BACK_MONEY = "type.receive.back.money";//订单退款到账
    public static final String TYPE_RECHARGE = "type.recharge";//充值
    public static final String TYPE_GET_MONEY = "type.getmoney";//提现请求已处理，注意查收
    public static final String TYPE_SYSTEM_MSG = "type.system.msg";//系统消息
    public static final String TYPE_VERIFY = "type.verify.msg";//律师认证通知
    public static final String TYPE_LEARNING = "type.learning.item";//学习中心
    public static final String TYPE_WEB_AUTH = "type.web.auth";//网站授权
    public static final String TYPE_ONLINE_ORDER = "type.online.order";//在线订单消息

    
    /** Not-null value. */
    private String id;
    /** Not-null value. */
    private String type;
    private String content;
    private String contentId;
    private String date;
    private Boolean read;

    public PushMessage() {
    }

    public PushMessage(String id) {
        this.id = id;
    }

    public PushMessage(String id, String type, String content, String contentId, String date, Boolean read) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.contentId = contentId;
        this.date = date;
        this.read = read;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

}
