package com.lsm.barrister2c.data.io;

import com.lsm.barrister2c.data.db.Favorite;
import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.Ad;
import com.lsm.barrister2c.data.entity.AppointmentSetting;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BarristerDetail;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.Channel;
import com.lsm.barrister2c.data.entity.ConsumeDetail;
import com.lsm.barrister2c.data.entity.CreditDebtInfo;
import com.lsm.barrister2c.data.entity.LawApp;
import com.lsm.barrister2c.data.entity.LearningItem;
import com.lsm.barrister2c.data.entity.Message;
import com.lsm.barrister2c.data.entity.OnlineBizUser;
import com.lsm.barrister2c.data.entity.OrderDetail;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.entity.Version;
import com.lsm.barrister2c.data.entity.WxPrepayInfo;

import java.util.List;

/**
 * Created by lvshimin on 16/5/11.
 */
public class IO {

    public static final String TEST = "http://www.baidu.com";

    public static final String SERVER = "http://app.dls.com.cn:8080/clientservice/";//GTC.测试
//    public static final String SERVER = "http://10.0.0.64:8080/clientservice/";//高荣威

    public static final String URL_LOGOUT = SERVER + "logout.do";

    public static final String URL_LOGIN = SERVER + "login.do";

    public static final String URL_GET_BANK_INFO = "http://apis.baidu.com/datatiny/cardinfo/cardinfo";
    public static final String URL_RECHARGE = SERVER + "barristerDetail.do";

    public static final String URL_PREPAY_INFO = SERVER + "wxPrepayInfo.do";
    public static final String URL_ALI_PREPAY_INFO = SERVER + "aliPrepayInfo.do";
    public static final String URL_GET_BARRISTER_DETAIL = SERVER +"barristerDetail.do";
    public static final String URL_ADD_FAVORITE_BARRISTER = SERVER + "addFavoriteBarrister.do";
    public static final String URL_DEL_FAVORITE_BARRISTER = SERVER + "delFavoriteBarrister.do";
    public static final String URL_GET_MY_FAVORITE = SERVER + "myFavoriteList.do";
    public static final String URL_MAKE_DEAL = SERVER + "placeOrder.do";
    public static final String URL_ADD_ORDER_STAR = SERVER + "addOrderStar.do";
    public static final String URL_UPLOAD_CASE = SERVER + "uploadCase.do";
    public static final String URL_PAY_ONLINE_ORDER = SERVER + "payOnlineOrder.do";
    public static final String URL_WEB_AUTH = SERVER + "webAuth.do";
    public static final String URL_ONLINE_BIZ_USER_LIST = SERVER + "onlineBizUserList.do";
    public static final String URL_BUY_CREDITDEBTDETAIL = SERVER + "buyCreditDebtDetail.do";
    public static final String URL_CREDITDEBTINFODETAIL = SERVER + "creditDebtInfoDetail.do";
    public static String URL_DEL_CREDITDEBTINFO = SERVER + "delCreditDebtInfo.do";
    public static String URL_MY_PURCHASEDCREDITDEBTLIST = SERVER + "myPurchasedCreditDebtList.do";
    public static String URL_MY_UPLOADCREDITDEBTLIST = SERVER + "myUploadCreditDebtList.do";
    public static String URL_SEARCH_CREDITDEBTLIST = SERVER + "searchCreditDebtList.do";
    public static String URL_UPDATE_CREDITDEBT = SERVER +  "updateCreditDebt.do";
    public static String URL_UPLOAD_CREDITDEBT = SERVER + "uploadCreditDebt.do";



    /**
     * Created by lvshimin on 16/10/10.
     * 39.+我购买的列表接口
     *   myPurchasedCreditDebtList
     *   提交方式：post
     *   参数:userId,verifyCode,page,pageSize
     *   返回值：resultCode，resultMsg ，List<CreditDebtInfo> list
     *   备注：
     */
    /**
     * Created by lvshimin on 16/10/10.
     * 40.+ 查询接口（已审核过的,显示部分信息）
     *   searchCreditDebtList
     *   提交方式：post
     *   参数:userId,verifyCode,page,pageSize, key, keyType（公司名称company、机构代码licenseNum、姓名name、身份证 idNum）,userType (credit//债权人,debt//债务人)
     *   返回值：resultCode，resultMsg ，List<CreditDebtInfo> list
     *   备注：客户端查询页面》输入框 key》查询实体类型：单选框keyType（公司名称、机构代码、姓名、身份证）》查询类型：单选框userType（credit//债权人,debt//债务人）》查询按钮
     */
    public static class CreditDebtListResult extends Action.CommonResult {
        public int total;
        public List<CreditDebtInfo> list;
    }

    /**
     * Created by lvshimin on 16/10/10.
     * 41.+债权债务信息详情接口
     *   creditDebtInfoDetail
     *   提交方式：post
     *   参数:userId,verifyCode,id
     *   返回值：resultCode，resultMsg, CreditDebtInfo detail
     *   备注：人的详情接口，这个人下面的所有债权债务信息
     */
    /**
     * Created by lvshimin on 16/10/10.
     * 42.+下单接口(返回债权债务信息详情)
     *   buyCreditDebtDetail
     *   提交方式：post
     *   参数:userId,verifyCode,id（债券债务信息记录id）
     *   返回值：resultCode，resultMsg,CreditDebtInfo detail
     *   备注：购买成功直接返回详细信息（债券债务人的全部详细信息）
     */
    public static class CreditDebtDetailResult extends Action.CommonResult{
        public CreditDebtInfo detail;
    }

    public static class PrePayResult extends Action.CommonResult {
        public WxPrepayInfo wxPrepayInfo;
    }

    public static class AliPrePayResult extends Action.CommonResult {
        public String aliPrepayInfo;//支付信息
    }

    /**
     * 登陆返回结果
     */
    public static class LoginResult extends Action.CommonResult {
        public User user;
    }

    public static final String URL_GET_VERIFY_CODE = SERVER + "getVerifyCode.do";

    /**
     * 验证码返回结果
     */
    public static class GetVerifyCodeResult extends Action.CommonResult {
        public String verifyCode;
    }


    public static final String URL_BIND_BANK_CARD = SERVER + "bindBankCard.do";

    /**
     * 绑定银行卡
     */
    public static class BindBankcardResult extends Action.CommonResult {
        public Account account;
    }


    public static final String URL_GET_USER_HOME = SERVER + "appHome.do";

    /**
     * HOME
     * 返回值：resultCode，resultMsg ，
     * remainingBalance余额，
     * totalExpanse总支出，
     * List<BusinessArea> caseTypeList领域列表；
     * List<BusinessType> businessTypeList业务列表；
     * List<LawApp> lawAppList 法律应用大全列表;
     */
    public static class HomeResult extends Action.CommonResult {
        public List<BusinessArea> bizAreas;//领域列表;
        public List<BusinessType> bizTypes;//业务列表;
        public List<Ad> list;
    }

    public static final String URL_FEEDBACK = SERVER + "feedback.do";

    public static final String URL_REQUEST_CANCEL_ORDER = SERVER + "requestCancelOrder.do";
    public static final String URL_REWARD_ORDER = SERVER + "rewardOrder.do";

    public static final String URL_GET_APP_VERSION = SERVER + "getLatestVersion.do";

    /**
     * 获取程序版本更新信息
     */
    public static class GetAppVersionResult extends Action.CommonResult {
        public Version version;
    }

    public static final String URL_HOME_LUNBO_ADS = SERVER + "lunboAds.do";

    public static class GetLunboAdsResult extends Action.CommonResult {
        public List<Ad> ads;
    }


    public static final String URL_GET_MONEY = SERVER + "getMoney.do";

    public static final String URL_GET_MY_ACCOUNT = SERVER + "myAccount.do";

    public static class GetAccountResult extends Action.CommonResult {
        public Account account;
    }

    public static final String URL_GET_MY_MSGS = SERVER + "getMyMsgs.do";

    /**
     * 我的消息列表
     */
    public static class GetMyMsgsResult extends Action.CommonResult {
        public List<Message> msgs;
        public int total;
    }

    public static final String URL_GET_ORDER_LIST = SERVER + "myOrderList.do";

    /**
     * 我的订单列表接口
     */
    public static class GetMyOrdersResult extends Action.CommonResult {
        public List<OrderItem> orders;
        public int total;
    }

    public static final String URL_GET_ORDER_DETAIL = SERVER + "orderDetail.do";

    /**
     * 订单详情接口
     */
    public static class GetOrderDetailResult extends Action.CommonResult {
        public OrderDetail orderDetail;
    }

    public static final String URL_GET_STUDY_LIST = SERVER + "getStudyList.do";

    /**
     * 学习中心列表
     */
    public static class GetStudyListResult extends Action.CommonResult {
        public List<LearningItem> items;
        public int total;
    }

    public static final String URL_MAKE_CALL = SERVER + "makeCall.do";
//    public static class MakeOrderResult extends Action.CommonResult {
//        public OrderDetail orderDetail;
//    }

    public static final String URL_UPDATE_USER = SERVER + "updateUserInfo.do";

    /**
     * 更新用户信息
     */
    public static class GetUpdateUserResult extends Action.CommonResult {
        public User user;
    }

    /**
     * 上传用户头像
     */
    public static final String URL_UPLOAD_USERICON = SERVER + "uploadUserIcon.do";

    public static class UploadUserIconResult extends Action.CommonResult {
        public User user;
    }

    public static class GetBankInfoResult extends Action.CommonResult {

        public String status;
        public Data data;

        public static class Data {
            public String cardtype;//": "贷记卡",
            public String cardlength;//": 16,
            public String cardprefixnum;//": "518710",
            public String cardname;//": "MASTER信用卡",
            public String bankname;//": "招商银行信用卡中心",
            public String banknum;//": "03080010"

            @Override
            public String toString() {
                return "Data{" +
                        "cardtype='" + cardtype + '\'' +
                        ", cardlength='" + cardlength + '\'' +
                        ", cardprefixnum='" + cardprefixnum + '\'' +
                        ", cardname='" + cardname + '\'' +
                        ", bankname='" + bankname + '\'' +
                        ", banknum='" + banknum + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "GetBankInfoResult{" +
                    "status='" + status + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public static final String URL_GET_CONSUME_DETAIL_LIST = SERVER + "getConsumeDetailList.do";

    /**
     * 消费记录列表
     */
    public static class GetConsumeDetailListResult extends Action.CommonResult {
        public List<ConsumeDetail> consumeDetails;
        public int total;
    }

    //领域类型列表
    public static final String URL_GET_CASETYPE_LIST = SERVER + "bizAreas.do";

    /**
     * 领域类型列表
     */
    public static class GetCaseTypeListResult extends Action.CommonResult {
        public List<BusinessArea> bizAreas;
        public int total;
    }

    //业务类型列表
    public static final String URL_GET_BUSINESSTYPE_LIST = SERVER + "bizTypes.do";

    /**
     * 业务类型列表
     */
    public static class GetBusinessTypeListResult extends Action.CommonResult {
        public List<BusinessType> bizTypes;
        public int total;
    }

    //应用大全列表
    public static final String URL_GET_LAWAPP_LIST = SERVER + "getLegalApplictions.do";

    /**
     * 应用大全列表
     */
    public static class GetLawAppListResult extends Action.CommonResult {
        public List<LawApp> legalList;
    }


    //律师列表
    public static final String URL_GET_BARRISTER_LIST = SERVER + "barristerList.do";

    /**
     * 律师列表
     */
    public static class GetBarristerListResult extends Action.CommonResult {
        public List<Barrister> items;
        public String docUrl;
        public int total;
    }

    public static final String URL_GET_APPOINTMENT_SETTINGS = SERVER + "getAppointmentSettings.do";

    public static class GetAppointmentSettingsResult extends Action.CommonResult {
        public List<AppointmentSetting> appointmentSettings;
    }

    public static final String URL_BIZ_TYPE_AREA_LIST = SERVER + "bizAreaAndBizTypeList.do";

    public static class GetBizTypeAreaListResult extends Action.CommonResult{
        public List<BusinessArea> bizAreas ;//领域列表;
        public List<BusinessType> bizTypes ;//业务类型;
    }

    public static class GetBarristerDetailResult extends Action.CommonResult{
        public BarristerDetail detail;
    }

    public static final String URL_GET_CHANNEL_LIST = SERVER + "getStudyChannelList.do";
    public static class GetChannelListResult extends Action.CommonResult{
        public List<Channel> items;
    }


    public static class GetMyFavoriteListResult extends Action.CommonResult {
        public List<Favorite> favoriteItemList;
        public int total;
    }


    public static class GetOnlineBizUserListResult extends Action.CommonResult {
        public List<OnlineBizUser> list;
        public int total;
    }

}