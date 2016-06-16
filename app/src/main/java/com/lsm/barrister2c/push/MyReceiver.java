package com.lsm.barrister2c.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.androidquery.util.AQUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yty.minerva.R;
import com.yty.minerva.app.AppConfig;
import com.yty.minerva.app.AppManager;
import com.yty.minerva.app.Constants;
import com.yty.minerva.app.VersionHelper;
import com.yty.minerva.data.db.Channel;
import com.yty.minerva.data.db.ChannelDao;
import com.yty.minerva.data.db.DbService;
import com.yty.minerva.data.db.PushMessage;
import com.yty.minerva.data.db.UserDbService;
import com.yty.minerva.data.db.VoteResultDao;
import com.yty.minerva.data.io.Expire;
import com.yty.minerva.data.io.Request;
import com.yty.minerva.data.io.gtc.GetChannelListReq;
import com.yty.minerva.data.io.gtc.GetNewsDetailReq;
import com.yty.minerva.helper.MsgHelper;
import com.yty.minerva.helper.UserHelper;
import com.yty.minerva.ui.activity.AuthorDetailActivity;
import com.yty.minerva.ui.activity.NewsDetailActivity;
import com.yty.minerva.ui.activity.TopicDetailActivity;
import com.yty.minerva.ui.activity.WebViewActivity;
import com.yty.minerva.utils.DLog;
import com.yty.minerva.utils.FileUtils;

import java.io.File;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    /**
     * 接收消息回调
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        DLog.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

            Log.d(TAG, "pushId:" + regId);

            Constants.PUSH_ID = regId;

            AppConfig.getInstance().setPushId(regId);

            //向服务器发送注册设备请求
            UserHelper.getInstance().uploadPushId(context, regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            DLog.d(TAG, "自定义的消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            handleCustomMessage(context, bundle);

        }
    }

    /**
     * 打印所有的 intent extra 数据
     *
     * @param bundle
     * @return
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 处理推送消息,判断类型，判断显示类型
     * 新闻：根据新闻id发送获取新闻Item请求，成功后提示notification
     * 活动:
     * 版本更新：
     *
     * @param context
     * @param bundle
     */
    private void handleCustomMessage(Context context, Bundle bundle) {

//		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		DLog.i(TAG, message);

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        DLog.i(TAG, extras);

        if (!PushUtil.isEmpty(extras)) {
            try {
                Gson gson = new Gson();

                //解析
                PushMessage msg = gson.fromJson(extras, new TypeToken<PushMessage>() {
                }.getType());

                if (msg.getType().equals(PushMessage.TYPE_NEWS)) {

                    //处理
                    handleNewsType(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_SYSTEM_MSG)) {

                    //处理
                    handleNewsType(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_UPDATE_NEWS)) {

                    //在线编辑新闻，清除该条新闻缓存
                    handleUpdateNewsCMD(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_FORCE_UPDATE)) {
                    //强制更新
                    AppConfig.getInstance().setForceUpdate(true);

                    if (AppManager.isMainActivityRunning()) {
                        VersionHelper.instance().check(AppManager.getAppManager().currentActivity(), true);
                    }

                } else if (msg.getType().equals(PushMessage.TYPE_AUTHOR_REPLY_MSG)) {

                    //小编回复消息
                    handleReplyMsg(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_CLEAR_APP_DATA)) {

                    //清除数据
                    handleClearDataCMD(context, msg);

                } else if (msg.getType().equals(PushMessage.TYPE_UPLOAD_PUSHID)) {

                    handleUploadPushIdCMD(context, msg);

                } else {
                    DLog.w(TAG, "未定义的消息类型");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理上传pushId命令
     *
     * @param msg
     */
    private void handleUploadPushIdCMD(Context context, PushMessage msg) {

        String pushId = AppConfig.getInstance().getPushId(context);

        if (TextUtils.isEmpty(pushId)) {
            JPushInterface.getRegistrationID(context);
        } else {
            UserHelper.getInstance().uploadPushId(context, pushId);
        }
    }

    /**
     * 清除数据
     *
     * @param context
     * @param msg
     */
    private void handleClearDataCMD(final Context context, PushMessage msg) {
        new Thread() {
            public void run() {

                try {
                    AppConfig.removeUser(context);
                    AppConfig.clearMemCache();
                    AQUtility.cleanCacheAsync(context, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 跟帖消息
     *
     * @param context
     * @param msg
     */
    private void handleReplyMsg(Context context, PushMessage msg) {
        // TODO Auto-generated method stub
        DLog.d(TAG, "有新消息");
        //播放声音
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(context, notification);
//        r.play();

        //未读 +1
        AppConfig.getInstance().increaseUnreadCount();

        //消息小红点提示
        MsgHelper.MsgListener msgListener = MsgHelper.getInstance().getMsgListener();
        if (msgListener != null) {
            msgListener.onReceiveMessage();
        }

        //通知
        String id = msg.getId();
        String title = msg.getTitle();
        String digest = msg.getDigest();
        String type = msg.getType();
        String contentId = msg.getContentId();
        String icon = msg.getIcon();

        //消息入库
        UserDbService.getInstance(context).getPushMessageAction().save(msg);

        showNewsNotification(context, title, digest, icon, type, contentId);


    }

    /**
     * 在线编辑，清除该条新闻的缓存
     *
     * @param context
     * @param msg
     */
    private void handleUpdateNewsCMD(Context context, PushMessage msg) {

        File cacheDir = AQUtility.getCacheDir(context);

        String id = msg.getContentId();

        //普通新闻
        String url = new GetNewsDetailReq(context, id).url();

        File cacheFile = AQUtility.getCacheFile(cacheDir, url);

        if (cacheFile != null && cacheFile.exists()) {

            FileUtils.deleteFile(cacheFile);
            return;

        }

        //组图新闻
//		url = new GetPicSetNewsDetailReq(context, id).url();
//
//		cacheFile = AQUtility.getCacheFile(cacheDir, url);
//
//		if(cacheFile!=null && cacheFile.exists()){
//
//			FileUtils.deleteFile(cacheFile);
//
//			return;
//
//		}

        //检查是否有投票信息，如果有则清掉
//		String json = AppConfig.getInstance().get("vote"+id);
//		if(json!=null){
//			AppConfig.getInstance().remove("vote"+id);
//		}

        boolean voted = UserDbService.getInstance(context).getVoteResultAction()
                .contains(VoteResultDao.Properties.Id.eq("vote" + id));

        if (voted) {
            UserDbService.getInstance(context).getVoteResultAction().delete(VoteResultDao.Properties.Id.eq("vote" + id));
        }

    }

    /**
     * 处理频道下线消息
     *
     * @param context
     * @param msg
     */
    private void handlerOfflineChannel(Context context, PushMessage msg) {
        // TODO 1.如果用户已关注该频道，则从数据库中移除
        String channelId = msg.getContentId();
        if (channelId.equals("1")) {
            DLog.w(TAG, "头条不能下线");
            return;
        }

        DLog.d(TAG, "推送消息，频道下线：" + channelId);

        new GetChannelListReq(context, 1)
                .setRefresh(true)
                .setExpire(Expire.DAY)
                .execute(null);

        UserDbService.getInstance(context).getChannelAction().delete(ChannelDao.Properties.Id.eq(channelId));
    }

    /**
     * 更新频道
     *
     * @param context
     * @param msg
     */
    private void handleUpdateChannelCMD(final Context context, PushMessage msg) {

        DLog.d(TAG, "推送消息，更新频道");

        new GetChannelListReq(context, 1)
                .setRefresh(true)
                .setRetry(3)
                .setExpire(Expire.DAY)
                .execute(new Request.Callback<List<Channel>>() {

                    @Override
                    public void progress() {
                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                    }

                    @Override
                    public void onCompleted(final List<Channel> t) {
                        if (t != null) {

                            new Thread() {
                                public void run() {
                                    DbService.getInstance(context).getChannelAction().save(t);

                                    List<Channel> userChannels = UserDbService.getInstance(context).getChannelAction().loadAll();

                                    loop:
                                    for (Channel userChannel : userChannels) {

                                        for (Channel temp : t) {

                                            if (userChannel.getId().equals(temp.getId())) {

                                                userChannel.setName(temp.getName());

                                                UserDbService.getInstance(context).getChannelAction().save(userChannel);

                                                continue loop;
                                            }
                                        }
                                    }
                                }
                            }.start();


                        }
                    }
                });
    }

    /**
     * 处理其他类型的消息
     *
     * @param msg
     */
    private void handleOtherType(Context context, PushMessage msg) {

    }

    /**
     * 处理新闻类型消息
     * 1.根据id 发送查询请求
     * 2.显示Notification
     *
     * @param msg
     */
    private void handleNewsType(final Context context, final PushMessage msg) {

        String id = msg.getId();
        String title = msg.getTitle();
        String digest = msg.getDigest();
        String type = msg.getType();
        String contentId = msg.getContentId();
        String icon = msg.getIcon();

        //消息入库
        UserDbService.getInstance(context).getPushMessageAction().save(msg);

        showNewsNotification(context, title, digest, icon, type, contentId);

//        new GetNewsItemReq(context, id)
//                .setRetry(3)//重试3次
//                .execute(new Request.Callback<NewsItem>() {
//
//                    @Override
//                    public void progress() {
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String msg) {
//                    }
//
//                    @Override
//                    public void onCompleted(NewsItem item) {
//
//                        if (item != null) {
//
//
//
//                        }
//
//                    }
//                });

    }


    /**
     * 显示消息
     *
     * @param context
     */
    public void showNewsNotification(Context context, String title, String digest, String icon, String type, String contentId) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent targetIntent = null;

        if (type.equals(PushMessage.TYPE_TOPIC)) {

            //推送消息是专题新闻
            targetIntent = new Intent(context, TopicDetailActivity.class);
            targetIntent.putExtra(Constants.KEY_ID, contentId);
            targetIntent.putExtra(Constants.KEY_TOPIC_TYPE, Constants.TOPIC_TYPE_NEWS);

        } else if (type.equals(PushMessage.TYPE_AD)) {

            //推送消息是广告
            targetIntent = new Intent(context, WebViewActivity.class);
            targetIntent.putExtra(WebViewActivity.KEY_TITLE, title);
            targetIntent.putExtra(WebViewActivity.KEY_URL, digest);

        } else if (type.equals(PushMessage.TYPE_NEWS)) {

            //推送消息是普通新闻
            targetIntent = new Intent(context, NewsDetailActivity.class);
            targetIntent.putExtra(Constants.KEY_ID, contentId);
            targetIntent.putExtra(Constants.KEY_THUMB, icon);

        } else if (type.equals(PushMessage.TYPE_SYSTEM_MSG)) {


        } else if (type.contains(PushMessage.TYPE_AUTHOR_REPLY_MSG)) {

            //推送消息是普通新闻
            targetIntent = new Intent(context, AuthorDetailActivity.class);

            String authorId = contentId;//小编id
            String authorName = title;//小编昵称
            String authorIcon = icon;

            targetIntent.putExtra(AuthorDetailActivity.KEY_ID, contentId);
            targetIntent.putExtra(AuthorDetailActivity.KEY_NAME, authorName);
            targetIntent.putExtra(AuthorDetailActivity.KEY_ICON, icon);
            targetIntent.putExtra(AuthorDetailActivity.KEY_FROM_PUSH, true);

        }


        PendingIntent contentIntent = null;

        if (targetIntent != null) {
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (AppManager.isMainActivityRunning()) {
                //程序运行中，弹出消息防止闪屏
                contentIntent = PendingIntent.getActivity(context, ++Constants.NOTIFICATION_REQUEST_CODE, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            } else {

                //程序未运行，弹出消息后，退出消息详情返回主页面
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                // Adds the back stack
                stackBuilder.addParentStack(NewsDetailActivity.class);

                stackBuilder.addNextIntent(targetIntent);

                contentIntent = stackBuilder.getPendingIntent(++Constants.NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

                //程序未启动，加载用户频道
//			List<Channel> userChannels = ChannelHelper.getInstance().getUserChannels();
//			if(userChannels==null || userChannels.isEmpty()){
//				ChannelHelper.getInstance().init(context);
//			}

            }
        }


        title = title.replaceAll("&quot;", "\"");

        if (!AppManager.isMainActivityRunning() && !AppConfig.getInstance().isPushMsgOfflineEnabled()) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.os.Build.VERSION.SDK_INT > 20 ? R.drawable.ic_launcher : R.drawable.ic_launcher);
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        builder.setTicker(title);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(Html.fromHtml(title));
        builder.setContentText(Html.fromHtml(digest));

        if (contentIntent != null)
            builder.setContentIntent(contentIntent);

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(digest));

        Notification notification = builder.build();

        manager.notify(Constants.NOTIFICATION_REQUEST_CODE, notification);

    }


}
