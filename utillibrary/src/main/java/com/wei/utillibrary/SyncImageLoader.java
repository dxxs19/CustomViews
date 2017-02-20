/**
 * 异步加载图片类
 */
package com.wei.utillibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;


import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 异步加载图片
 * 
 * @author WEI
 *
 */
public class SyncImageLoader {

	private static ImageLoader imageLoader;
	/**
	 * 默认缓存图片显示
	 */
	private static DisplayImageOptions options;
	/**
	 * 头像圆角图片显示
	 */
	private static DisplayImageOptions headOptions;
	/**
	 * 头像原始图片显示
	 */
	private static DisplayImageOptions headOptionsNoRound;
	private static ImageLoaderConfiguration config;

	private static SyncImageLoader instance;

	private SyncImageLoader() {

	}

	public static SyncImageLoader getInstance(Context context) {
		if (instance == null) {
			instance = new SyncImageLoader();
			File cacheDir = StorageUtils.getCacheDirectory(context);
			Log.i("ImageLoader", cacheDir.getAbsolutePath());
			config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
			// 设置缓存的详细信息，最好不要设置这
					.threadPoolSize(5)
					// default 线程池内加载的数量
					.threadPriority(Thread.NORM_PRIORITY - 1)
					// default
					.tasksProcessingOrder(QueueProcessingType.FIFO)
					// default
					.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(10 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
					.memoryCacheSize(10 * 1024 * 1024) //
					.memoryCacheSizePercentage(13).imageDownloader(new BaseImageDownloader(context)) // default
					.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
					.writeDebugLogs() // Remove for release app
					.build(); // 开始构建

			// 配置一些图片显示的选项
			options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();//R.drawable.upload_photos_normal

			// 配置一些图片显示的选项
			headOptions = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)/*.displayer(new RoundedBitmapDisplayer(ImageConstant.DEFAULT_ROUND_PIX))*/.build();// 设置成圆角图片

			// 配置一些图片显示的选项
			headOptionsNoRound = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();// 设置成圆角图片

//			// 配置一些图片显示的选项
//			options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.pictures_no_big).showImageForEmptyUri(R.drawable.pictures_no_big).cacheInMemory(true)
//					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();//R.drawable.upload_photos_normal
//
//			// 配置一些图片显示的选项
//			headOptions = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.avatar_placeholder).showImageForEmptyUri(R.drawable.avatar_placeholder).cacheInMemory(true).cacheOnDisk(true)
//					.bitmapConfig(Bitmap.Config.RGB_565)/*.displayer(new RoundedBitmapDisplayer(ImageConstant.DEFAULT_ROUND_PIX))*/.build();// 设置成圆角图片
//
//			// 配置一些图片显示的选项
//			headOptionsNoRound = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.avatar_placeholder).showImageForEmptyUri(R.drawable.avatar_placeholder).cacheInMemory(true)
//					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();// 设置成圆角图片

			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
		return instance;

	}

	public void displayImage(ImageView imgView, String url, DisplayImageOptions options, ImageLoadingListener mLoadingListener) {
		imageLoader.displayImage(url, imgView, options, mLoadingListener);
	}

	public void displayImage(ImageView imgView, String url, ImageLoadingListener mLoadingListener) {
		imageLoader.displayImage(url, imgView, options, mLoadingListener);
	}

	public void displayImage(ImageView imgView, String url) {
		imageLoader.displayImage(url, imgView, options);
	}

	/**
	 * 显示头像
	 * 
	 * @param imgView
	 * @param url
	 */
	public void displayHeadImage(ImageView imgView, String url) {
		imageLoader.displayImage(url, imgView, headOptions);
	}

	/**
	 * 获取头像位图
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap loadHeadImage(String url) {
		return imageLoader.loadImageSync(url, headOptionsNoRound);
	}

	public Bitmap loadImage(String url) {
		return imageLoader.loadImageSync(url, options);
	}

	/**
	 * 缓存到内存，用于群聊合成图片<br>
	 * 路径[imageUri]_[width]x[height]
	 * 
	 * @param key
	 * @param bm
	 * @return
	 */
	public boolean saveInMemory(String key, Bitmap bm) {
		key = key + "_800x800";//[imageUri]_[width]x[height]
		return imageLoader.getMemoryCache().put(key, bm);
	}

	/**
	 * 从内存加载，用于群聊合成图片 路径<br>
	 * [imageUri]_[width]x[height]
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getInMemory(String key) {
		key = key + "_800x800";
		return imageLoader.getMemoryCache().get(key);
	}
}
