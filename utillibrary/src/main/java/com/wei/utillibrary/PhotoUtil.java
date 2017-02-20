package com.wei.utillibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PhotoUtil {

	private static final String IMAGE_TYPE = "image/*";

	/**
	 * 打开照相机
	 * 
	 * @param activity
	 *            当前的activity
	 * @param requestCode
	 *            拍照成功时activity forResult 的时候的requestCode
	 * @param photoFile
	 *            拍照完毕时,图片保存的位置
	 */
	public static void openCamera(Activity activity, int requestCode, File photoFile) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		try {
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "无法使用拍照功能！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 本地照片调用
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void openPhotos(Activity activity, int requestCode) {
		if (openPhotosNormal(activity, requestCode) && openPhotosBrowser(activity, requestCode) && openPhotosFinally(activity))
			;
	}

	/**
	 * PopupMenu打开本地相册.
	 */
	private static boolean openPhotosNormal(Activity activity, int actResultCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
		try {
			activity.startActivityForResult(intent, actResultCode);

		} catch (android.content.ActivityNotFoundException e) {

			return true;
		}

		return false;
	}

	/**
	 * 打开其他的一文件浏览器,如果没有本地相册的话
	 */
	private static boolean openPhotosBrowser(Activity activity, int requestCode) {
		Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
									// "image/*";
		Intent wrapperIntent = Intent.createChooser(intent, null);
		try {
			activity.startActivityForResult(wrapperIntent, requestCode);
		} catch (android.content.ActivityNotFoundException e1) {
			return true;
		}
		return false;
	}

	/**
	 * 这个是找不到相关的图片浏览器,或者相册
	 */
	private static boolean openPhotosFinally(Context context) {
		Toast.makeText(context, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG).show();
		return false;
	}

	/**
	 * 获取从本地图库返回来的时候的URI解析出来的文件路径
	 * 
	 * @return
	 */
	public static String getPhotoPathByLocalUri(Context context, Intent data) {
		if((null==context) || (null == data)){
			Log.v(context.getClass().getSimpleName(), "null==context or null==data");
			return null;
		}
		Uri selectedImage = data.getData();
		Log.v(context.getClass().getSimpleName(), "uri=" + selectedImage);
		if(null == selectedImage){
			Log.v(context.getClass().getSimpleName(), "null == selectedImage");
			return null;
		}
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		if(null == cursor){
			Log.v(context.getClass().getSimpleName(), "null == cursor");
			return null;
		}
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	/**
	 * 从一个大的图片文件获取一个适度缩小的图片文件,文件名为新生成的uuid.jpg。默认最大高/宽为800px
	 * 
	 * @param context
	 * @param mPictureTempFile
	 * @return
	 */
//	public static File getSuitablePhoto(Context context, File mPictureTempFile)
//	{
//		Bitmap bmp = BitmapFactory.decodeFile(mPictureTempFile.getAbsolutePath());
//
//		// change by WEI:部分手机拍照后会旋转一定角度，需要将旋转后的图片挪正
//		Bitmap normalBmp = bmp;
//		int degree = getBitmapDegree(mPictureTempFile.getPath());
//		if (degree > 0)
//		{
//            normalBmp = rotateBitmapByDegree(bmp, degree);
//		}
//		// change end
//
//		Bitmap smallBmp = ImageUtil.toSuitableImage(context, normalBmp, 800);
//		File file = FileCache.getInstance().putImage(UUID.randomUUID().toString() + ".jpg", smallBmp);
//		ImageUtil.safeRecycle(bmp);
//		ImageUtil.safeRecycle(smallBmp);
//		return file;
//	}

	/**
	 * 将图片按照指定的角度进行旋转
	 * @param bmp    需要旋转的图片
	 * @param degree 指定的旋转角度
     * @return   旋转后的图片
     */
	public static Bitmap rotateBitmapByDegree(Bitmap bmp, int degree)
	{
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		if (bmp != null && !bmp.isRecycled())
		{
			bmp.recycle();
		}
		return bitmap;
	}

	/**
	 * 获取图片的旋转角度
	 * @param path 图片绝对路径
	 * @return  图片的旋转角度
     */
	public static int getBitmapDegree(String path)
	{
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
