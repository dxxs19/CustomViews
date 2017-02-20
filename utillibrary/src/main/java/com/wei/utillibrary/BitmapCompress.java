package com.wei.utillibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.format.DateFormat;

import org.jivesoftware.smack.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public final class BitmapCompress {

	/**
	 * 计算图片的缩放值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmap(String filePath, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 缩放 Bitmap
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

	/**
	 * 把bitmap转换成String
	 */
	public static String bitmapToString(String filePath, int quality) {

		Bitmap bm = getSmallBitmap(filePath,480,800);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeBytes(b);
	}

	/**
	 * 把Base64 String转换成bitmap
	 * 
	 * @param base64
	 *            String
	 * @param quality
	 *            转换后的图片精度，100为原图
	 * @return
	 */
	public static String StringToBitmap(String base64, int quality) {

		File path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/xmpp/");
		if (!path.exists())
			path.mkdirs();
		String name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ "-" + UUID.randomUUID().toString() + ".jpg";
		String filename = path.getAbsolutePath() + "/" + name;

		byte[] buffer = org.jivesoftware.smack.util.Base64.decode(base64);
		Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

		// 保存图片
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(filename);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.flush();
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
}
