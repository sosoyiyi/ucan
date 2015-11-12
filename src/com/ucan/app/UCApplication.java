package com.ucan.app;

import java.io.File;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ucan.app.common.UCAppManager;
import com.ucan.app.common.utils.FileAccessor;

public class UCApplication extends Application {
	private static UCApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		UCAppManager.setContext(instance);
		FileAccessor.initFileAccess();
		initImageLoader();
	}

	/**
	 * 单例，返回一个实例
	 * 
	 * @return
	 */
	public static UCApplication getInstance() {
		if (instance == null) {
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
}
