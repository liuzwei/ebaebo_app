package com.app.ebaebo.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;

/**
 * 1.sd 卡 相关
 * 2.网络相关
 * 3.分辨率dp 与 px互转
 * @author dds
 *
 */
public class PhoneEnvUtil {
	
	public static final int NETTYPE_WIFI = 1;
	public static final int NETTYPE_CMWAP = 2;
	public static final int NETTYPE_CMNET = 3;

	/**
	 * 检测网络是否可用
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}


}
