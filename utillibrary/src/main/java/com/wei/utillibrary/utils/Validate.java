package com.wei.utillibrary.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

/**
 * Simple validation methods. Designed for jsoup internal use
 */
public final class Validate {

	private Validate() {
	}

	/**
	 * Validates that the object is not null
	 * 
	 * @param obj
	 *            object to test
	 */
	public static void notNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object must not be null");
	}

	/**
	 * Validates that the object is not null
	 * 
	 * @param obj
	 *            object to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void notNull(Object obj, String msg) {
		if (obj == null)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * Validates that the value is true
	 * 
	 * @param val
	 *            object to test
	 */
	public static void isTrue(boolean val) {
		if (!val)
			throw new IllegalArgumentException("Must be true");
	}

	/**
	 * Validates that the value is true
	 * 
	 * @param val
	 *            object to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void isTrue(boolean val, String msg) {
		if (!val)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * Validates that the value is false
	 * 
	 * @param val
	 *            object to test
	 */
	public static void isFalse(boolean val) {
		if (val)
			throw new IllegalArgumentException("Must be false");
	}

	/**
	 * Validates that the value is false
	 * 
	 * @param val
	 *            object to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void isFalse(boolean val, String msg) {
		if (val)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * Validates that the array contains no null elements
	 * 
	 * @param objects
	 *            the array to test
	 */
	public static void noNullElements(Object[] objects) {
		noNullElements(objects, "Array must not contain any null objects");
	}

	/**
	 * Validates that the array contains no null elements
	 * 
	 * @param objects
	 *            the array to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void noNullElements(Object[] objects, String msg) {
		for (Object obj : objects)
			if (obj == null)
				throw new IllegalArgumentException(msg);
	}

	/**
	 * Validates that the string is not empty
	 * 
	 * @param string
	 *            the string to test
	 */
	public static void notEmpty(String string) {
		if (string == null || string.length() == 0)
			throw new IllegalArgumentException("String must not be empty");
	}

	/**
	 * Validates that the string is not empty
	 * 
	 * @param string
	 *            the string to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void notEmpty(String string, String msg) {
		if (string == null || string.length() == 0)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * Cause a failure.
	 * 
	 * @param msg
	 *            message to output.
	 */
	public static void fail(String msg) {
		throw new IllegalArgumentException(msg);
	}

	public static void validateAccount(String value) throws Exception {
		if (value == null || value.length() < 6) {
			throw new Exception("请输入正确的手机号");
		}
	}

	public static void validatePassword(String value) throws Exception {
		if (TextUtils.isEmpty(value)) {
			throw new Exception("密码不能为空");
		}
		if (value.length() < 6 || value.length() > 30) {
			throw new Exception("密码长度有误，应为（6- 30位），请重新输入");
		}
	}

	public static void validatePasswordAgain(String value1, String value2) throws Exception {
		validatePassword(value1);
		if (!value1.equals(value2)) {
			throw new Exception("两次密码不一致");
		}
	}

	public static void validatePhone(String value) throws Exception {
		if((value == null) || ("".equals(value))){
			throw new Exception("手机号码不能为空");
		}
		//0：开头为1，其他都为任意数字	1：默认表达式
		int select = 0;
		String regular = null;
		String hint    = null;
		switch(select){
		case 0:
			regular = "^1[0-9]{10}$";
			hint    = "请输入正确的电话，如13000000000";
			break;
		
		case 1:
			regular = "(^((13[0-9])|^(15[^4,\\D])|(18[0,5-9]))\\d{8}) | (^([0-9]{4}|[0-9]{3})-([0-9]{7,8})) | (^([0-9]{7,8}))$"; //(!value.matches("^([0-9]{11})|(([0-9]{4}|[0-9]{3})-([0-9]{7,8}))$"))
			hint    = "请输入正确的电话，如13000000或020-12345678";
			break;
		}
		
		if(!value.matches(regular)) 
		{
			throw new Exception(hint);
		}
	}

	/**
	 * 校验手机号码和座机号码
	 * @param value
	 * @throws Exception
     */
	public static void validatePhoneAndTelephone(String value) throws Exception {
		if((value == null) || ("".equals(value))){
			throw new Exception("电话号码不能为空");
		}
		//0：开头为1，其他都为任意数字	1：默认表达式
		int select = 0;
		String regular = null;
		String hint    = null;
		switch(select){
			case 0:
				//         for 手机        for 固话
				regular = "(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";
				hint    = "请输入正确的电话，如13000000或020-12345678";
				break;

			default:
				regular = "^1[0-9]{10}$";
				hint    = "请输入正确的电话，如13000000000";
				break;
		}

		if(!value.matches(regular)) {
			throw new Exception(hint);
		}
	}

	public static void validatePSTNPhone(String value) throws Exception {
		if (!value.matches("(^(0[0-9]{2,3})?[-]{0,1}([2-9][0-9]{6,7})+([0-9]{1,4})?$)|(^1[0-9]{10}$)")) {
			throw new Exception("号码格式错误,请重输！");
		}
	}

	public static void validateEmpty(String prefix, String value) throws Exception {
		if (value == null || value.length() == 0) {
			throw new Exception(prefix + "不能为空");
		}
	}
	
	public static void validateEquals(String prefix, String param1, String param2) throws Exception {
		if(!param1.equals(param2)){
			throw new Exception(prefix + "错误！");
		}
	}

	/**
	 * 判断昵称<br>
	 * <p>
	 * 主要判断是否为空 、出去空格后，不能为空
	 * </p>
	 * 
	 * @param prefix
	 *            该EditText的标题
	 * @param value
	 *            判断的字符串
	 * @throws Exception
	 */
	public static void validateNickName(String prefix, String value) throws Exception {
		if (value != null && value.length() != 0) {
			if (value.trim().length() == 0) {
				throw new Exception(prefix + "不能只含空格");
			}
		} else
			throw new Exception(prefix + "不能为空");
	}

	public static void validateInteger(String prefix, String value) throws Exception {
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			throw new Exception(prefix + "格式必须为整数");
		}
	}

	public static void validatePort(String prefix, String value) throws Exception {
		try {
			int port = Integer.parseInt(value);
			if (port < 0 || port > 65535) {
				throw new Exception(prefix + "不合法(0~65535)");
			}
		} catch (Exception e) {
			throw new Exception(prefix + "不合法(0~65535)");
			//throw new Exception(prefix + "格式必须为整数");
		}
	}

	public static void validateMomey(String value) throws Exception {
		try {
			if (!value.matches("^((\\d{1,3})(\\.\\d{1,2})?)$|(0\\.0?([1-9]\\d?))$"))
				throw new Exception("运费输入有误");
			Double.parseDouble(value);
		} catch (Exception e) {
			throw new Exception("运费输入有误");
		}
		/*
		try{
			String[] temp = value.split("\\.");
			if(temp.length != 2)
				throw new Exception("运费输入有误");
			if(temp[0].length()<=0)
				throw new Exception("运费输入有误");
			if(temp[1].length()>2)
				throw new Exception("运费输入有误");
		}catch(Exception e){
			try {
		        Integer.parseInt(value);
		    }catch (Exception e1){
		    	throw new Exception("运费输入有误");
		    }
		}*/
	}

	public static void validateURL(String value) throws Exception {
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		if (!value.matches(regex)) {
			throw new Exception("地址输入错误");
		}
	}

	public static void validateIPAddress(String value) throws Exception {
		if (!value.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))
			throw new Exception("服务器地址输入错误");
	}

	public static void validataDate(String value) throws Exception {
		//验证格式
		if (!value.matches("^\\d{4}\\-\\d{1,2}\\-\\d{1,2}$"))
			throw new Exception("日期输入错误！");
		//验证有效性
		String[] temp = value.split("\\-");
		try {
			if (!((2000 <= Integer.valueOf(temp[0])) && (Integer.valueOf(temp[0]) <= 3000))) {
				throw new Exception(temp[0] + "年份无效");
			}
			if (!((1 <= Integer.valueOf(temp[1])) && (Integer.valueOf(temp[1]) <= 12))) {
				throw new Exception(temp[1] + "月份无效");
			}
			if (!((1 <= Integer.valueOf(temp[2])) && (Integer.valueOf(temp[2]) <= 31))) {
				throw new Exception(temp[2] + "日期无效");
			}
		} catch (Exception e) {
			throw new Exception("日期无效");
		}
	}
	
	public static void validataTime(String value) throws Exception {
		//验证格式
		if (!value.matches("^\\d{1,2}\\:\\d{1,2}$"))
			throw new Exception("日期输入错误！");
	}

	public static boolean isNotEmptyAndNull(String target) {
		if (null != target && !"".equals(target)) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmptyAndNull(Editable text) {
		if (null != text) {
			if (!text.toString().equals("")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmptyAndNull(CharSequence text) {
		if (null != text) {
			if (!text.toString().equals("")) {
				return true;
			}
		}
		return false;
	}

	private static String invalid_char[] = { "`", "%", "[", "]", "+", "$", "{", "}", "\\", "/", ":", "*", "?", "\"", "<", ">", "|", ";", "\n", ",", ".", ":", ";", "!", "'", "@", "&", "^", "……", "，",
			"。", "、", "：", "；", "?", "!", "“", "”", "‘", "’", "(", ")", "…", "#", "*", "~", "?", "!", "？", "！", "（", "）" };
	/**
	 * 过滤非法字符的过滤器
	 */
	public static InputFilter[] defaultNamefilter = new InputFilter[] { new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			for (int i = 0; i < invalid_char.length; i++) {
				if (source.toString().indexOf(invalid_char[i]) >= 0) {
					Log.d("Validate", "非法字符" + invalid_char[i]);
					return "";
				}
			}
			return null;
		}

	} };

}
