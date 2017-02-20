/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wei.utillibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utilities and constants related to app preferences.
 */
public class PrefUtils {
	private static final String TAG = "PrefUtils";

	/**
	 * 定义Pref key常量 转移到com.hori.smartcommunity.constant.PreferenceConstants
	 */
	/*public static final String PREF_LOCAL_TIMES = "pref_local_times";
	//服务器地址
	public static final String PREF_SERVER_ADDRESS = "pref_server_address";
	//服务器端口
	public static final String PREF_SERVER_PORT = "pref_server_port";
	//账号
	public static final String PREF_ACCOUNT = "pref_account";
	//密码
	public static final String PREF_PASSWORD = "pref_password";*/

	public static void remove(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(key).commit();
	}

	/**
	 * 初始化应用的pref数据
	 * 
	 * @param context
	 */
	public static void init(final Context context) {
		/*        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		        int conferenceYear = sp.getInt(PREF_CONFERENCE_YEAR, 0);
		        if (conferenceYear != Config.CONFERENCE_YEAR) {
		            sp.edit().clear().putInt(PREF_CONFERENCE_YEAR, Config.CONFERENCE_YEAR).commit();
		        }*/
	}

	public static String getString(Context context, String key, String defVal) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, defVal);
	}

	public static int getInt(Context context, String key, int defVal) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(key, defVal);
	}

	public static long getLong(Context context, String key, long defVal) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getLong(key, defVal);
	}

	public static int getInt(Context context, String key) {
		return getInt(context, key, 0);
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putString(key, value).commit();
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putInt(key, value).commit();
	}

	public static void putLong(Context context, String key, long value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putLong(key, value).commit();
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putBoolean(key, value).commit();

	}

	public static String getTarget(Context context, String key, String defValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, "");
	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, defValue);
	}

	public static void registerOnSharedPreferenceChangeListener(final Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.registerOnSharedPreferenceChangeListener(listener);
	}

	public static void unregisterOnSharedPreferenceChangeListener(final Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.unregisterOnSharedPreferenceChangeListener(listener);
	}


}
