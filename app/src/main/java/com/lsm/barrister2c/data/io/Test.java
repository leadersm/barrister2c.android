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
import com.lsm.barrister2c.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by lvshimin on 16/5/27.
 */
public class Test {


    /**
     * 我的账户
     * @return
     */
    public static IO.GetAccountResult getMyAccount(){
        IO.GetAccountResult result = new IO.GetAccountResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        Account account = new Account();
        account.setBankCardBindStatus(Account.CARD_STATUS_BOUND);
        account.setRemainingBalance("1000");
        account.setTotalConsume("1500");
        Account.BankCard bankCard = new Account.BankCard();
        bankCard.setBankCardAddress("富力城支行");
        bankCard.setLogoName("icon_bank_zs.png");
        bankCard.setCardType("储蓄卡");
        bankCard.setBankCardNum("6225 8801 4204 7506");
        bankCard.setBankCardName("招商银行");
        bankCard.setBankPhone("13671057132");
        account.setBankCard(bankCard);

        result.account = account;

        return result;
    }




    public static IO.LoginResult getLoginResult(){
        IO.LoginResult result = new IO.LoginResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        User user = new User();
        user.setId("1");
        user.setEmail("78783606@qq.com");
        user.setArea("北京市，朝阳区");
        user.setAddress("北京市，西直门，D10-11");
        user.setAge("30");
        user.setGender("男");
        user.setVerifyCode("867920");
        user.setPhone("13671057132");
        user.setLocation("39,107");
        user.setName("世民");
        user.setNickname("The Fox");
        user.setState("北京市");
        user.setCity("朝阳区");
        user.setUserIcon("");

        result.user = user;

        return result;
    }

    public static IO.GetVerifyCodeResult getVerifyCodeResult(){
        IO.GetVerifyCodeResult result = new IO.GetVerifyCodeResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        result.verifyCode = "";
        return result;
    }


    public static IO.BindBankcardResult getBindcardResult(){
        IO.BindBankcardResult result = new IO.BindBankcardResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        Account.BankCard bankCard = new Account.BankCard();
        bankCard.setBankPhone("13671057132");
        bankCard.setBankCardName("招商银行");
        bankCard.setBankCardNum("6225880142047506");
        bankCard.setCardType("储蓄卡");
        bankCard.setBankCardAddress("招商银行北京分行富丽城支行");
        return result;
    }



    public static IO.HomeResult getHomeResult(){
        IO.HomeResult result = new IO.HomeResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        result.caseTypeList = getCaseTypeListResult(10).caseTypeList;
        result.businessTypeList = getBusinessTypeListResult(6).businessTypeList;
        result.lawAppList = getLawAppListResult(12).lawAppList;

        return  result;
    }


    public static IO.GetAppVersionResult getAppVersionResult(){
        IO.GetAppVersionResult result = new IO.GetAppVersionResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        Version version = new Version();
        version.setUrl("");
        version.setVersionCode(10);
        version.setVersionInfo("更新测试");
        version.setVersionName("1.0.1");
        result.version = version;
        return  result;
    }


    public static List<Ad> getAds(){
        List<Ad> ads = new ArrayList<>();
        for(int i=0;i<3;i++){
            Ad temp = new Ad();
            temp.setTitle("test");
            temp.setDate(DateFormatUtils.format(new Date()));
            temp.setImage("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2539625029,4256537847&fm=80");
            temp.setUrl("http://cnews.chinadaily.com.cn/2016-05/27/content_25498712.htm");
            ads.add(temp);
        }
        return ads;
    }

    public static IO.GetLunboAdsResult getLunoAdsResult(){
        IO.GetLunboAdsResult result = new IO.GetLunboAdsResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        result.ads = getAds();
        return  result;
    }


    public static IO.GetMyMsgsResult getMyMsgsResult(){
        IO.GetMyMsgsResult result = new IO.GetMyMsgsResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<Message> msgs = new ArrayList<>();

        for(int i=0;i<20;i++){
            Message msg = new Message();
            msg.setId(""+i);
            msg.setContent("系统即将重启。。。。");
            msg.setTitle("系统消息");
            msg.setType(Message.TYPE_SYSTEM);
            msgs.add(msg);
        }

        result.msgs = msgs;
        result.total = 20;

        return result;
    }

    public static IO.GetMyOrdersResult getMyOrdersResult(int count) {
        IO.GetMyOrdersResult result = new IO.GetMyOrdersResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        List<OrderItem> orderItems = new ArrayList<>();
        for(int i=0;i<count;i++){
            OrderItem item = new OrderItem();
            item.setUserIcon("");
            item.setId(""+i);
            item.setType(OrderItem.TYPE_APPOINTMENT);
            item.setDate("2016-05-29");
            item.setClientPhone("13671057132");
            item.setStatus(OrderDetail.STATUS_WAITING);
            item.setNickname("The Fox");
            item.setCaseType("1");
            orderItems.add(item);
        }
        result.orderItems = orderItems;

        return result;
    }

    public static IO.GetOrderDetailResult getOrderDetailResult(){
        IO.GetOrderDetailResult result = new IO.GetOrderDetailResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId("1");
        orderDetail.setCaseType("1");
        orderDetail.setType(OrderItem.TYPE_APPOINTMENT);
        orderDetail.setStatus(OrderDetail.STATUS_WAITING);
        orderDetail.setCustomerIcon("");
        orderDetail.setCustomerNickname("昔日少年");
        orderDetail.setPayTime("2016-05-30 10:19:33");
        orderDetail.setRemarks("备注：我被强奸了。。。哈哈哈");
        orderDetail.setOrderNo(String.valueOf(System.currentTimeMillis()));
        orderDetail.setCustomerPhone("13671057132");
        result.orderDetail = orderDetail;
        return result;
    }


    public static IO.GetStudyListResult getStudyListResult(){
        IO.GetStudyListResult result = new IO.GetStudyListResult();

        result.resultCode = 200;
        result.resultMsg = "success";

        List<LearningItem> items = new ArrayList<>();

        String date = DateFormatUtils.format(new Date());

        for(int i=0;i<20;i++){
            LearningItem item = new LearningItem();
            item.setId(""+i);
            item.setDate(date);
            item.setTag("微电影");
            item.setThumb("");
            item.setTitle("学习中心标题测试");
            item.setUrl("http://www.baidu.com");
            items.add(item);

        }

        result.items = items;
        result.total = 20;

        return result;
    }


    public static IO.GetUpdateUserResult GetUpdateUserResult(){
        IO.GetUpdateUserResult result = new IO.GetUpdateUserResult();

        result.resultCode = 200;
        result.resultMsg = "success";

        User user = new User();
        user.setId("1");
        user.setPhone("13671057132");
        user.setEmail("78783606@qq.com");
        user.setArea("北京市，朝阳区");
        user.setAddress("北京市，西直门，D10-11");
        user.setAge("30");
        user.setGender("男");
        user.setVerifyCode("867920");
        user.setLocation("39,107");
        user.setName("世民");
        user.setNickname("The Fox");
        user.setState("北京市");
        user.setCity("朝阳区");
        user.setUserIcon("");

        result.user = user;
        return result;
    }

    public static IO.GetBankInfoResult getBankInfoResult(){
        IO.GetBankInfoResult result = new IO.GetBankInfoResult();
        result.resultCode = 200;
        result.resultMsg = "success";

        IO.GetBankInfoResult.Data data = new IO.GetBankInfoResult.Data();
        data.cardtype="贷记卡";
        data.cardlength="16";
        data.cardprefixnum="518710";
        data.cardname="MASTER信用卡";
        data.bankname="招商银行信用卡中心";
        data.banknum="03080010";

        result.status = Account.CARD_STATUS_BOUND;

        result.data = data;

        return result;
    }

    public static IO.GetConsumeDetailListResult getConsumeDetailListResult(){
        IO.GetConsumeDetailListResult result = new IO.GetConsumeDetailListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<ConsumeDetail> items = new ArrayList<>();
        for(int i=0;i<10;i++){
            ConsumeDetail item = new ConsumeDetail();
            item.setId(String.valueOf(i));
            item.setSerialNum(String.valueOf(System.currentTimeMillis()));
            item.setDate(DateFormatUtils.format(new Date()));
            item.setMoney("100");
            item.setOrderId(String.valueOf(1));
            item.setType(ConsumeDetail.TYPE_ORDER);
            items.add(item);
        }
        result.consumeDetails = items;
        result.total = 20;

        return result;
    }



    /**
     * caseType列表
     * @return
     */
    public static IO.GetCaseTypeListResult getCaseTypeListResult(int count){
        IO.GetCaseTypeListResult result = new IO.GetCaseTypeListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<BusinessArea> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            BusinessArea cs = new BusinessArea();
            cs.setId(String.valueOf(i));
            cs.setName("知识产权");
            cs.setDesc("知识产权相关");
            cs.setIcon("");
            list.add(cs);
        }
        result.caseTypeList = list;
        result.total = 10;
        return result;
    }

    /**
     * businessType列表
     * @return
     */
    public static IO.GetBusinessTypeListResult getBusinessTypeListResult(int count){
        IO.GetBusinessTypeListResult result = new IO.GetBusinessTypeListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<BusinessType> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            BusinessType cs = new BusinessType();
            cs.setId(String.valueOf(i));
            cs.setName("知识产权");
            cs.setDesc("知识产权相关");
            cs.setIcon("");
            list.add(cs);
        }
        result.businessTypeList = list;
        result.total = 10;
        return result;
    }

    /**
     * businessType列表
     * @return
     */
    public static IO.GetLawAppListResult getLawAppListResult(int count){
        IO.GetLawAppListResult result = new IO.GetLawAppListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<LawApp> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            LawApp cs = new LawApp();
            cs.setId(String.valueOf(i));
            cs.setName("中国法律");
            cs.setDesc("中国法律法规");
            cs.setIcon("");
            list.add(cs);
        }
        result.lawAppList = list;
        result.total = 10;
        return result;
    }

    public static IO.GetBarristerListResult getBarristerResult(int count) {
        IO.GetBarristerListResult result = new IO.GetBarristerListResult();
        result.resultCode = 200;
        result.resultMsg = "success";
        List<Barrister> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            Barrister cs = new Barrister();
            cs.setId(String.valueOf(i));
            cs.setName("律师"+i);
            cs.setRating(new Random().nextInt(5));
            cs.setUserIcon("");
            cs.setRecentServiceTimes(10);
            cs.setWorkingStartYear("1996-10-01");
            cs.setArea("北京市-大兴区");
            cs.setCompany("xx律师事务所");
            cs.setGoodAt("金融|交通事故|财产纠纷");
            list.add(cs);
        }
        result.barristerList = list;
        result.total = 10;
        return result;
    }
}
