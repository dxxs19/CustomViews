package com.wei.utillibrary.constant;

import com.wei.utillibrary.R;

public class ImageConstant {

	/**
	 * 头像圆角
	 */
	public static final boolean USE_ROUND_CONNER = true;
	/**
	 * 圆角范围
	 */
	public static final int DEFAULT_ROUND_PIX = 20;

	private ImageConstant() {
	}

	//	public static final String SYS_SMARTMEDICAL_APK_DOWNLOAD_FOLDER = "smartMedicalApk";
	public static final int SYS_NO_WIDTH = -1;
	public static final String SYS_CACHE_IMAGE_FILE_EXT = ".tmp";
	public static final int DEFAULT_TAG_URL = R.string.app_name - 1; // 减一是防止与其它TAG的值相同
	public static final int DEFAULT_BITMAP_ROUND_CORNER_RADIAN = 5;
	public static final String SYS_HEAD_IMAGE_TEMP_FILE_NAME = "headImage";
	public static final String SYS_GENERAL_IMAGE_TEMP_FILE_NAME = "generalImage";

	/** 默认头像图标 **/
	public static final int DEFAULT_AVATAR_RESID = 0 ;//R.drawable.avatar_placeholder;
	/** 默认群聊图标 **/
	public static final int DEFAULT_CHATROOM_LOGO_RESID = 0; //R.drawable.avatar_placeholder;
}
