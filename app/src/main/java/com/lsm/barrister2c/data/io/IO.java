package com.lsm.barrister2c.data.io;

import com.lsm.barrister2c.data.entity.Account;
import com.lsm.barrister2c.data.entity.Ad;
import com.lsm.barrister2c.data.entity.Barrister;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.ConsumeDetail;
import com.lsm.barrister2c.data.entity.LawApp;
import com.lsm.barrister2c.data.entity.LearningItem;
import com.lsm.barrister2c.data.entity.Message;
import com.lsm.barrister2c.data.entity.OrderDetail;
import com.lsm.barrister2c.data.entity.OrderItem;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.data.entity.Version;

import java.util.List;

/**
 * Created by lvshimin on 16/5/11.
 */
public class IO {

    public static final String SERVER = "http://119.254.167.200:8080/lawerservice/";//GTC.测试
    //	public static final String SERVER = "http://10.0.0.25:8080";//高荣威


    public static final String URL_LOGOUT = SERVER + "logout.do";

    public static final String URL_LOGIN = SERVER + "login.do";

    public static final String URL_GET_BANK_INFO = "http://apis.baidu.com/datatiny/cardinfo/cardinfo";
    public static final String TEST = "http://www.baidu.com";

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
        public Account.BankCard bankCard;
    }


    public static final String URL_GET_USER_HOME = SERVER + "userHome.do";

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
        public List<BusinessArea> caseTypeList;//领域列表;
        public List<BusinessType> businessTypeList;//业务列表;
        public List<LawApp> lawAppList;//法律应用大全列表;
    }

    public static final String URL_FEEDBACK = SERVER + "addFeedback.do";

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
        public List<OrderItem> orderItems;
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
    public static final String URL_GET_CASETYPE_LIST = SERVER + "caseTypeList.do";

    /**
     * 领域类型列表
     */
    public static class GetCaseTypeListResult extends Action.CommonResult {
        public List<BusinessArea> caseTypeList;
        public int total;
    }

    //业务类型列表
    public static final String URL_GET_BUSINESSTYPE_LIST = SERVER + "businessTypeList.do";

    /**
     * 业务类型列表
     */
    public static class GetBusinessTypeListResult extends Action.CommonResult {
        public List<BusinessType> businessTypeList;
        public int total;
    }

    //应用大全列表
    public static final String URL_GET_LAWAPP_LIST = SERVER + "lawAppList.do";

    /**
     * 应用大全列表
     */
    public static class GetLawAppListResult extends Action.CommonResult {
        public List<LawApp> lawAppList;
        public int total;
    }


    //律师列表
    public static final String URL_GET_BARRISTER_LIST = SERVER + "barristerList.do";

    /**
     * 律师列表
     */
    public static class GetBarristerListResult extends Action.CommonResult {
        public List<Barrister> barristerList;
        public int total;
    }
}