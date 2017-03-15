package com.wei.utillibrary.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.StatFs;

import com.wei.utillibrary.constant.ImageConstant;
import com.wei.utillibrary.utils.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class ImageCache{

	private static final int SPACE_REMAIN_MB = 100;
	private volatile static ImageCache instance;
	private HashMap<String, SoftReference<Bitmap>> mapMemoryBitmaps;
	private File cacheFolder;//缓存文件
	private Context context;

	public static ImageCache getInstance(Context context) {
		if (instance == null) {
			synchronized (ImageCache.class) {
				if (instance == null) {
					instance = new ImageCache(context);
				}
			}
		}
		return instance;
	}

	private ImageCache(Context context) {
		this.context = context;
		mapMemoryBitmaps = new HashMap<String, SoftReference<Bitmap>>();
	}

	private boolean isFreeSpaceNotEnough() {
		try {
			StatFs sf = new StatFs(cacheFolder.getAbsolutePath());
			int availableBlocks = sf.getAvailableBlocks();
			int blockSize = sf.getBlockSize();
			long freeSpace = (long) availableBlocks * blockSize;
			int freeSpaceMB = (int) (freeSpace / 1024 / 1024);
			return freeSpaceMB < SPACE_REMAIN_MB;
		} catch (Exception e) {
			return false;
		}
	}

	public File cache(String filew, String url, Bitmap bmp, CompressFormat format) {
		if (bmp == null)
			return null;
		if (filew.equals(ImageConstant.SYS_GENERAL_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getImageCacheFolder(context);
			}

		} else if (filew.equals(ImageConstant.SYS_HEAD_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getFileCacheFolder(context);
			}
		}
		//System.out.println("cacheFolder--AbsolutePath---"+cacheFolder.getAbsolutePath());
		//System.out.println("cacheFolder--Path---"+cacheFolder.getPath());
		if (isFreeSpaceNotEnough()) {
			FileUtil.delFilesByFolder(cacheFolder);
		}
		String fileName = getFilename(url);
		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bmp);
		mapMemoryBitmaps.put(fileName, softBitmap);

		File file = FileUtil.findFileByUrl(fileName, cacheFolder);
		if (file.exists()) {
			file.delete();
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bmp.compress(format, 80, fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static String getFilename(String url) {
		if (url == null)
			return null;

		int index = url.lastIndexOf("/");
		String filename = url.substring(index + 1);
		return filename;
	}

	public Bitmap getCache(String folderType, String url) {
		Bitmap bitmap = null;
		String fileName = getFilename(url);
		SoftReference<Bitmap> softBitmap = mapMemoryBitmaps.get(fileName);
		if (softBitmap != null) {
			bitmap = softBitmap.get();
		}

		if (bitmap != null && !bitmap.isRecycled()) {
			return bitmap;
		}

		if (folderType.equals(ImageConstant.SYS_GENERAL_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getFileCacheFolder(context);
			}
		} else if (folderType.equals(ImageConstant.SYS_HEAD_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getImageCacheFolder(context);
			}

		}
		File file = FileUtil.findFileByUrl(fileName, cacheFolder);
		if (file == null || !file.exists()) {
			return null;
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		SoftReference<Bitmap> softBitmap2 = new SoftReference<Bitmap>(bitmap);
		mapMemoryBitmaps.put(fileName, softBitmap2);

		return bitmap;
	}

	public Bitmap getPreview(String folderType, String url, int maxL) {
		Bitmap bitmap = null;
		String fileName = getFilename(url);
		String key = fileName + "/" + maxL;

		SoftReference<Bitmap> softBitmap = mapMemoryBitmaps.get(key);
		if (softBitmap != null) {
			bitmap = softBitmap.get();
		}

		if (bitmap != null && !bitmap.isRecycled()) {
			return bitmap;
		}

		if (folderType.equals(ImageConstant.SYS_GENERAL_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getFileCacheFolder(context);
			}
		} else if (folderType.equals(ImageConstant.SYS_HEAD_IMAGE_TEMP_FILE_NAME)) {
			if (cacheFolder == null) {
				cacheFolder = FileUtil.getImageCacheFolder(context);
			}

		}
		File file = FileUtil.findFileByUrl(fileName, cacheFolder);
		if (file == null || !file.exists()) {
			return null;
		}

		/*FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		bitmap = ImageUtil.toSuitableImage(context, file.getAbsolutePath(), maxL);

		SoftReference<Bitmap> softBitmap2 = new SoftReference<Bitmap>(bitmap);
		mapMemoryBitmaps.put(key, softBitmap2);

		return bitmap;
	}

}
