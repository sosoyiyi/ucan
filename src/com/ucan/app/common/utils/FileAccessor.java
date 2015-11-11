/*
 *  Copyright (c) 2015 The UCAN project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing 优侃互动  Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.ucantalk.cn
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.ucan.app.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import com.ucan.app.UCApplication;
import com.ucan.app.R;

/**
 * 文件操作工具类
 * Created by Eric on 2015/11/11.
 */
public class FileAccessor {


    public static final String TAG = FileAccessor.class.getName();
    public static String EXTERNAL_STOREPATH = getExternalStorePath();
    public static final String APPS_ROOT_DIR = getExternalStorePath() + "/UCAN_app";
    public static final String EXPORT_DIR = getExternalStorePath() + "/UCAN_app/IM";
    public static final String CAMERA_PATH = getExternalStorePath() + "/DCIM/UCAN_app";
    public static final String TACK_PIC_PATH = getExternalStorePath()+ "/UCAN_app/.tempchat";
    public static final String IMESSAGE_VOICE = getExternalStorePath() + "/UCAN_app/voice";
    public static final String IMESSAGE_IMAGE = getExternalStorePath() + "/UCAN_app/image";
    public static final String IMESSAGE_AVATAR = getExternalStorePath() + "/UCAN_app/avatar";
    public static final String IMESSAGE_FILE = getExternalStorePath() + "/UCAN_app/file";
    public static final String LOCAL_PATH = APPS_ROOT_DIR + "/config.txt";


    /**
     * 初始化应用文件夹目录
     */
    public static void initFileAccess() {
        File rootDir = new File(APPS_ROOT_DIR);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        File imessageDir = new File(IMESSAGE_VOICE);
        if (!imessageDir.exists()) {
            imessageDir.mkdir();
        }

        File imageDir = new File(IMESSAGE_IMAGE);
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }

        File fileDir = new File(IMESSAGE_FILE);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File avatarDir = new File(IMESSAGE_AVATAR);
        if (!avatarDir.exists()) {
            avatarDir.mkdir();
        }
    }

    public static String getAppKey() {
        if (isExistExternalStore()) {
            String content = readContentByFile(LOCAL_PATH);
            if (content != null) {
                try {
                    String result = content.split(",")[0];
                    if(result != null && result.contains("appkey=")) {
                        return result.replace("appkey=" , "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return getConfig(ECPreferenceSettings.SETTINGS_APPKEY);
    }

    public static String getAppToken() {
        if (isExistExternalStore()) {
            String content = readContentByFile(LOCAL_PATH);
            if (content != null) {
                try {
                    String result = content.split(",")[1];
                    if(result != null && result.contains("apptoken=")) {
                        return result.replace("apptoken=" , "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return getConfig(ECPreferenceSettings.SETTINGS_TOKEN);
    }

    private static String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId(), (String) settings.getDefaultValue());
        return value;
    }

    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());
                }
                return sb.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 获取语音文件存储目录
     * @return
     */
    public static File getVoicePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(IMESSAGE_VOICE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 头像
     * @return
     */
    public static File getAvatarPathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(IMESSAGE_AVATAR);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }



    /**
     * 获取文件目录
     * @return
     */
    public static File getFilePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(IMESSAGE_FILE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 返回图片存放目录
     * @return
     */
    public static File getImagePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(IMESSAGE_IMAGE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 获取文件名
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {

        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start + 1, pathName.length());
        }
        return pathName;

    }

    /**
     * 外置存储卡的路径
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * /data/data/com.UCAN_app.bluetooth/files
     *
     * @return
     */
    public static String getAppContextPath() {
        return UCApplication.getInstance().getFilesDir().getAbsolutePath();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getFileUrlByFileName(String fileName) {
        return FileAccessor.IMESSAGE_IMAGE + File.separator + FileAccessor.getSecondLevelDirectory(fileName)+ File.separator + fileName;
    }

    /**
     *
     * @param filePaths
     */
    public static void delFiles(ArrayList<String> filePaths) {
        for(String url : filePaths) {
            if(!TextUtils.isEmpty(url))
                delFile(url);
        }
    }


    public static boolean delFile(String filePath){
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return true;
        }

        return file.delete();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getSecondLevelDirectory(String fileName) {
        if(TextUtils.isEmpty(fileName) || fileName.length() < 4) {
            return null;
        }

        String sub1 = fileName.substring(0, 2);
        String sub2 = fileName.substring(2, 4);
        return sub1 + File.separator + sub2;
    }

    /**
     *
     * @param root
     * @param srcName
     * @param destName
     */
    public static void renameTo(String root , String srcName , String destName) {
        if(TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName) || TextUtils.isEmpty(destName)){
            return;
        }

        File srcFile = new File(root + srcName);
        File newPath = new File(root + destName);

        if(srcFile.exists()) {
            srcFile.renameTo(newPath);
        }
    }

    public static File getTackPicFilePath() {
        File localFile = new File(getExternalStorePath()+ "/UCAN_app/.tempchat" , "temp.jpg");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            LogUtil.e("hhe", "SD卡不存在");
            localFile = null;
        }
        return localFile;
    }
}