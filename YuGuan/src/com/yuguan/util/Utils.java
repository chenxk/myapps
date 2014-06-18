package com.yuguan.util;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {

	public static String rootPaht = "http://192.168.10.106:8080/yuqiu";
	public static String countyUrl = rootPaht + "/util/county.htm?cid=";
	public static String activityImg = rootPaht + "/images/action/";
	public static String userImg = rootPaht + "/images/user/";
	public static String activityUrl = rootPaht + "/action/actionlist.htm?rg=0&sr=0&pg=";
	public static String mallInfoUrl = rootPaht + "/action/actioninfo.htm?aid=";
	public static String getBaoMingUsersUrl = rootPaht + "/action/baominguser.htm?aid=";
	public static String getCommentUrl = rootPaht + "/action/getcomment.htm?aid=";
	public static String loginUrl = rootPaht + "/member/login.htm?r=3&yanzhengma=343&username=";
	
	public static int TIMEOUT = 5000;
	public static JSONObject loginInfo;
	private Context context;

	public Utils() {
	}

	public Utils(Context context) {
		this.context = context;
	}

	public void showSomeThing(String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @return
	 */
	public boolean isWifiConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @return
	 */
	public boolean isMobileConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * Android 监控网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 很多朋友在android开发中，都会遇到手机网络类型判断，因为就目前的android平台手机来说：可能会存在4中状态
	 * 1.无网络（这种状态可能是因为手机停机，网络没有开启，信号不好等原因） 2.使用WIFI上网 3.CMWAP（中国移动代理） 4.CMNET上网
	 * 这四种状态，如果没有网络，肯定是无法请求Internet了，如果是wap就需要为手机添加中国移动代理，关于为手机添加中国移动的代理！
	 * 
	 * Email vipa1888@163.com QQ:840950105 获取当前的网络状态 -1：没有网络
	 * 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			// Log.e("networkInfo.getExtraInfo()",
			// "networkInfo.getExtraInfo() is "+networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}
}
