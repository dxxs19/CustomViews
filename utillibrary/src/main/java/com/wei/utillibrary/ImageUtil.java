package com.wei.utillibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wei.utillibrary.constant.ImageConstant;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

public class ImageUtil{

	/**
	 * 写图片文件
	 * 
	 * @throws IOException
	 */
	public static void saveImage(Context context, String fileName, Bitmap bitmap) throws IOException {
		saveImage(context, fileName, bitmap, 100);
	}

	public static void saveImage(Context context, String fileName, Bitmap bitmap, int quality) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}

	/**
	 * 获取bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 将图片设置为圆角
	 */
	public static Bitmap convert2RoundCorner(Bitmap bitmap, int roundPx) {
		if (bitmap == null)
			return null;
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xFFCFCFCF;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		//safeRecycle(bitmap);
		return output;
	}

	/**
	 * 获取bitmap
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap uri2Bitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	/**
	 * 这个是头像
	 * 
	 * @param picUrl
	 * @param imageView
	 * @param defaultResid
	 * @param context
	 */
	public static void loadHeadImageFromServer(String picUrl, ImageView imageView, int defaultResid, Context context) {
		imageView.setTag(ImageConstant.DEFAULT_TAG_URL, picUrl);
		//new HeadImageDownloadTask(imageView, picUrl, defaultResid, context).execute(picUrl, imageView, 0);

		SyncImageLoader.getInstance(context).displayHeadImage(imageView, picUrl);
	}

	/**
	 * 显示普通图片
	 * 
	 * @param imageView
	 * @param picUrl
	 * @param defaultResid
	 * @param context
	 */
	public static void showImage(ImageView imageView, String picUrl, int defaultResid, Context context) {
		imageView.setTag(ImageConstant.DEFAULT_TAG_URL, picUrl);
		//new ShowImageTask(imageView, picUrl, defaultResid, context).execute();
		if (TextUtils.isEmpty(picUrl)) {
			imageView.setImageResource(defaultResid);
		} else {
			SyncImageLoader.getInstance(context).displayImage(imageView, picUrl);
		}
	}

	/**
	 * 显示普通图片
	 * 
	 * @param imageView
	 * @param picUrl
	 * @param defaultResid
	 * @param context
	 */
	/*public static void showImage(ImageView imageView, String picUrl, int defaultResid, Context context, ShowImageTask.OnImageDownload listener) {
		imageView.setTag(ImageConstant.DEFAULT_TAG_URL, picUrl);
		ShowImageTask task = new ShowImageTask(imageView, picUrl, defaultResid, context);
		task.setDownloadListener(listener);
		task.execute();
	}*/

	/**
	 * 显示缩略图片
	 * 
	 * @param imageView
	 * @param picUrl
	 * @param defaultResid
	 * @param context
	 */
	public static void showPreviewImage(ImageView imageView, String picUrl, int defaultResid, Context context, int maxL) {
		//Log.d("ImageUtil", "showPreviewImage....");
		imageView.setTag(ImageConstant.DEFAULT_TAG_URL, picUrl);
		//new ShowImageTask(imageView, picUrl, defaultResid, context, maxL).execute();
		if (TextUtils.isEmpty(picUrl)) {
			imageView.setImageResource(defaultResid);
		} else {
			SyncImageLoader.getInstance(context).displayImage(imageView, picUrl);
		}
	}

	// 商品详情中图片大小的计算
	public static int getNewProductDetailPicHeightByWidth(int picWidth) {
		int widthDefine = 640;
		int heightDefine = 480;
		int picHeight = picWidth * heightDefine / widthDefine;
		return picHeight;
	}

	// bitmap 和drawable 的互转
	public static Drawable convertBitmap2Drawable(Bitmap bmp, Context context) {
		BitmapDrawable bmpDrawable = new BitmapDrawable(context.getResources(), bmp);
		return (Drawable) bmpDrawable;
	}

	public static Bitmap convertDrawable2Bitmap(Drawable drawable, Context context) {
		int drawableWidth = drawable.getIntrinsicWidth();
		int drawableHeight = drawable.getIntrinsicHeight();
		int width = dip2px(context, drawableWidth);
		int height = dip2px(context, drawableHeight);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scaledDensity);
	}

	public static Bitmap getImageWithGivenHeight(Bitmap bmpOriginal, int height) {
		if (bmpOriginal == null)
			return null;

		int width = bmpOriginal.getWidth();
		Bitmap bmpDest = Bitmap.createBitmap(bmpOriginal, 0, 0, width, height);
		bmpOriginal.recycle();
		return bmpDest;
	}

	/**
	 * 将网络图片变为Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url) {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();

		InputStream stream = null;
		try {
			stream = new URL(url).openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = getBytes(stream);
		// 这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 480 * 800);
		// end
		opts.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		return bmp;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 数据流转成btyle[]数组
	 * 
	 * @param is
	 * @return
	 */
	private static byte[] getBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int len = 0;
		try {
			while ((len = is.read(b, 0, 2048)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	/**
	 * 根据设置最大的长度和宽带，根据需要保持比例的缩放图片 注意，会释放原先的大图bitmap
	 * 
	 * @param context
	 * @param bitmap
	 * @param maxL
	 *            最大的长度/宽度
	 * @return
	 */
	public static Bitmap toSuitableImage(Context context, Bitmap bitmap, int maxL) {
		if (bitmap == null) {
			return null;
		}
		int rawW = bitmap.getWidth();
		int rawH = bitmap.getHeight();

		int newW = rawW;
		int newH = rawH;

		if (rawW >= rawH && rawW > maxL) {//横向
			newW = maxL;
			newH = newW * rawH / rawW;

		} else if (rawW < rawH && rawH > maxL) {//纵向
			newH = maxL;
			newW = newH * rawW / rawH;
		}

		Bitmap newBitmap = null;

		if (newW != rawW && newH != rawH) {//需要进行缩图
			newBitmap = BitmapCompress.zoomBitmap(bitmap, newW, newH);
			safeRecycle(bitmap);
		} else {
			newBitmap = bitmap;
		}
		return newBitmap;
	}

	/**
	 * 根据设置最大的长度和宽带，根据需要保持比例的缩放图片 注意，会释放原先的大图bitmap 优化读取过程，先读大小，再按倍率压缩,只能是整数倍... 适合加载超大图片
	 * 
	 * @param context
	 * @param path
	 * @param maxL
	 *            最大的长度/宽度
	 * @return
	 */
	public static Bitmap toSuitableImage(Context context, String path, int maxL) {
		//Log.d("ImageUtil", "maxL:" + maxL);

		BitmapFactory.Options options = new BitmapFactory.Options();
		// 获取这个图片的宽和高，注意此处的bitmap为null
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		int rawH = options.outHeight;
		int rawW = options.outWidth;

		//Log.d("ImageUtil", "rawH:" + rawH);
		//Log.d("ImageUtil", "rawW:" + rawW);

		int newW = rawW;
		int newH = rawH;

		int inSampleSize = 1;

		if (rawW >= rawH && rawW > maxL) {//横向
			newW = maxL;
			newH = newW * rawH / rawW;
			//inSampleSize = Math.round((float) rawW / (float) newW);//四舍五入
			//inSampleSize = (int) Math.floor((double) rawW / (double) newW);//向下取整
			inSampleSize = (int) Math.ceil((double) rawW / (double) newW);//向上取整

		} else if (rawW < rawH && rawH > maxL) {//纵向
			newH = maxL;
			newW = newH * rawW / rawH;
			//inSampleSize = Math.round((float) rawH / (float) newH);//四舍五入
			//inSampleSize = (int) Math.floor((double) rawH / (double) newH);//向下取整
			inSampleSize = (int) Math.ceil((double) rawH / (double) newH);//向上取整

		}

		//Log.d("ImageUtil", "inSampleSize:" + inSampleSize);

		Bitmap newBitmap = null;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		options.inJustDecodeBounds = false;
		options.inSampleSize = inSampleSize;
		newBitmap = BitmapFactory.decodeFile(path, options);

		//Log.d("ImageUtil", "newBitmapW:" + newBitmap.getWidth());
		//Log.d("ImageUtil", "newBitmapH:" + newBitmap.getHeight());

		safeRecycle(bitmap);
		return newBitmap;
	}

	public static void safeRecycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	/**
	 * 生成二维码图片
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createQRCodeBitmap(String content, int width, int height)
	{
		if (null == content || "".equals(content) || content.length() < 1) {
			return null;
		}
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (bitMatrix.get(i, j)) {
						pixels[i * width + j] = 0xff000000;
					}
					else
					{
						pixels[i * width + j] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
