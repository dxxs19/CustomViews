package com.wei.utillibrary.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

public class FileUtil {
	public static File getFileCacheFolder(Context context) {
		return getFileCacheFolder(Environment.DIRECTORY_PICTURES, null, context);
	}

	public static File getFileCacheFolder(String folder, Context context) {
		return getFileCacheFolder(Environment.DIRECTORY_PICTURES, folder, context);
	}

	public static File getDownloadFileFolder(Context context) {
		return getFileCacheFolder(Environment.DIRECTORY_DOWNLOADS, null, context);
	}

	public static File getImageCacheFolder(Context context) {
		String sdcardPath = getSdcardImageCachePath(context);
		String internalStoragePath = getInternalStorageImageCachePath(context);
		return getCacheFolder(sdcardPath, internalStoragePath);
	}

	public static File getFileCacheFolder(String type, String folder, Context context) {
		String sdcardPath = getSdcardFilePath(type, folder, context);
		String internalStoragePath = getInternalStorageFilePath(folder, context);
		return getCacheFolder(sdcardPath, internalStoragePath);
	}

	private static String getSdcardFilePath(String type, String folder, Context context) {
		String path = null;
		File cachefolder = context.getExternalFilesDir(type);
		if (cachefolder != null) {
			path = cachefolder.getAbsolutePath();
			if (folder != null) {
				path += (File.separator + folder);
			}
		}
		return path;
	}

	private static String getInternalStorageFilePath(String folder, Context context) {
		String path = context.getFilesDir().getAbsolutePath();
		if (folder != null) {
			path += (File.separator + folder);
		}
		return path;
	}

	private static String getSdcardImageCachePath(Context context) {
		String path = null;
		File folder = context.getExternalCacheDir();
		if (folder != null) {
			path = folder.getAbsolutePath();
		}
		return path;
	}

	private static String getInternalStorageImageCachePath(Context context) {
		return context.getCacheDir().getAbsolutePath();
	}

	private static File getCacheFolder(String sdcardPath, String internalStoragePath) {
		File cacheFolder = null;
		if (isSDCardAvailable() && sdcardPath != null) {

			cacheFolder = new File(sdcardPath);
		} else {

			cacheFolder = new File(internalStoragePath);
		}

		if (!cacheFolder.exists()) {
			cacheFolder.mkdirs();
		}
		return cacheFolder;
	}

	private static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	public static void deleteAllFile(Context context) {

		File imageFolder = getImageCacheFolder(context);
		delFilesByFolder(imageFolder);

		File commonFolder = getFileCacheFolder(context);
		delFilesByFolder(commonFolder);
	}

	public static void delFilesByFolder(final File folder) {
		if (folder == null)
			return;

		new Thread() {
			public void run() {
				delFiles(folder);
			}
		}.start();
	}

	private static void delFiles(File folder) {
		if (folder == null)
			return;

		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				delFiles(file);
			} else {
				file.delete();
			}
		}
	}

	public static void delFilesByFilename(final String theFilename, final File folder, final Context context) {
		if (theFilename == null || folder == null)
			return;

		new Thread() {
			public void run() {
				File[] files = folder.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						if (theFilename.equals(filename))
							return true;
						else
							return false;
					}
				});
				for (File file : files) {
					file.delete();
				}
			}
		}.start();
	}

	public static File findFileByUrl(String fileName, File folder) {
		if (fileName == null || folder == null)
			return null;

		File file = new File(folder, fileName);
		return file;
	}

	public static String getMimeType(String filename) {
		final String DEFAULT_TYPE = "*/*";
		if (filename == null) {
			return DEFAULT_TYPE;
		}

		int index = filename.indexOf(".");
		if (index == -1) {
			return DEFAULT_TYPE;
		}

		String ext = filename.substring(index + 1);
		if ("apk".equals(ext)) {
			return "application/vnd.android.package-archive";
		} else if ("doc".equals(ext) || "docx".equals(ext)) {
			return "application/msword";
		} else if ("xls".equals(ext) || "xlsx".equals(ext)) {
			return "application/msexcel";
		} else if ("ppt".equals(ext) || "pptx".equals(ext)) {
			return "application/mspowerpoint";
		} else if ("pdf".equals(ext)) {
			return "application/pdf";
		} else if ("bmp".equals(ext) || "gif".equals(ext) || "jpeg".equals(ext) || "jpg".equals(ext) || "png".equals(ext)) {
			return "image/*";
		} else if ("amr".equals(ext) || "mp3".equals(ext) || "m3u".endsWith(ext) || "m4p".equals(ext) || "m4a".equals(ext) || "m4b".equals(ext) || "ape".equals(ext) || "mp2".equals(ext)
				|| "wav".equals(ext)) {
			return "audio/*";
		} else if ("3gp".equals(ext) || "asf".equals(ext) || "avi".equals(ext) || "m4u".equals(ext) || "m4v".equals(ext) || "mov".equals(ext) || "mp4".equals(ext) || "mpe".equals(ext)
				|| "mpeg".equals(ext) || "mpg".equals(ext) || "mpg4".equals(ext) || "rmvb".equals(ext)) {
			return "video/*";
		} else if ("java".equals(ext) || "conf".equals(ext) || "htm".equals(ext) || "html".equals(ext) || "shtml".equals(ext) || "log".equals(ext) || "prop".equals(ext) || "txt".equals(ext)
				|| "xml".equals(ext) || "js".equals(ext) || "css".equals(ext) || "jsp".equals(ext) || "bak".equals(ext) || "properties".equals(ext)) {
			return "text/*";
		} else {
			return DEFAULT_TYPE;
		}
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}
}
