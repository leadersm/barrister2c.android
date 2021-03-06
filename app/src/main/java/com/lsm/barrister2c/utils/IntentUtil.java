package com.lsm.barrister2c.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class IntentUtil {
    /**
     * android获取一个用于打开HTML文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getHtmlFileIntent(String filename) {
        Uri uri = Uri.parse(filename).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(filename).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * android获取一个用于打开图片文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getImageFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * android获取一个用于打开PDF文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getPdfFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * android获取一个用于打开文本文件的intent
     *
     * @return
     * @filename filename
     * @filename filenameBoolean
     */
    public static Intent getTextFileIntent(String filename, boolean filenameBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (filenameBoolean) {
            Uri uri1 = Uri.parse(filename);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(filename));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
     * android获取一个用于打开音频文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getAudioFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * android获取一个用于打开视频文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getVideoFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * android获取一个用于打开CHM文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getChmFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * android获取一个用于打开Word文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getWordFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * android获取一个用于打开Excel文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getExcelFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * android获取一个用于打开PPT文件的intent
     *
     * @return
     * @filename filename
     */
    public static Intent getPptFileIntent(String filename) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;

    }

    public static Intent getNetworkSettingsIntent() {
        Intent intent = new Intent("android.settings.WIFI_SETTINGS");
        return intent;
    }

    public static Intent getInstallIntent() {
        Uri uri = Uri.fromParts("package", "com.tianwen.jjrb", null);
        Intent intent = new Intent(Intent.ACTION_PACKAGE_ADDED, uri);
        return intent;
    }

}


