package com.wei.utillibrary.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/*
 * 网络助手
 * 权限：<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class NetUtil {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;
	public static final int NETWORN_ETHERNET = 3;

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info != null) {
			State state = info.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_WIFI;
			}
		}
		info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info != null) {
			State state = info.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_MOBILE;
			}
		}
		info = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		if (info != null) {
			State state = info.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_ETHERNET;
			}
		}

		return NETWORN_NONE;

		/*
		 * // Wifi State state =
		 * connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		 * .getState();
		 * 
		 * 
		 * 
		 * 
		 * if (state == State.CONNECTED || state == State.CONNECTING) { return
		 * NETWORN_WIFI; }
		 * 
		 * // 3G state =
		 * connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		 * .getState(); if (state == State.CONNECTED || state ==
		 * State.CONNECTING) { return NETWORN_MOBILE; }
		 * 
		 * // ethernet state =
		 * connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
		 * .getState(); if (state == State.CONNECTED || state ==
		 * State.CONNECTING) { return NETWORN_ETHERNET; } return NETWORN_NONE;
		 */

	}
}
