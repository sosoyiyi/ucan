package com.ucan.app;

import java.io.File;
import java.io.InvalidClassException;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ucan.app.base.manager.UCAppManager;
import com.ucan.app.common.enums.UCPreferenceSettings;
import com.ucan.app.common.utils.CrashHandler;
import com.ucan.app.common.utils.FileAccessor;
import com.ucan.app.common.utils.LogUtil;
import com.ucan.app.common.utils.UCPreferences;

public class UCApplication extends Application {
	private static UCApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		UCAppManager.setContext(instance);
		FileAccessor.initFileAccess();
		initImageLoader();
		setChattingContactId();
		CrashHandler.getInstance().init(this);
	}

	/**
	 * 保存当前的聊天界面所对应的联系人、方便来消息屏蔽通知
	 */
	private void setChattingContactId() {
		try {
			UCPreferences.savePreference(
					UCPreferenceSettings.SETTING_CHATTING_CONTACTID, "", true);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单例，返回一个实例
	 * 
	 * @return
	 */
	public static UCApplication getInstance() {
		if (instance == null) {
            LogUtil.w("[UCApplication] instance is null.");
        }
		return instance;
	}

	private void initImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "ucan/image");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(1)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				// .denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(UCAppManager.md5FileNameGenerator)
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(
						new UnlimitedDiscCache(cacheDir, null,
								UCAppManager.md5FileNameGenerator))// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public boolean getAlphaSwitch() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			boolean b = appInfo.metaData.getBoolean("ALPHA");
			LogUtil.w("[UCApplication - getAlpha] Alpha is: " + b);
			return b;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 返回配置文件的日志开关
	 * 
	 * @return
	 */
	public boolean getLoggingSwitch() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			boolean b = appInfo.metaData.getBoolean("LOGGING");
			LogUtil.w("[UCApplication - getLogging] logging is: " + b);
			return b;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}
