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
	 * �ж��Ƿ�����������
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
	 * �ж�WIFI�����Ƿ����
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
	 * �ж�MOBILE�����Ƿ����
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
	 * ��ȡ��ǰ�������ӵ�������Ϣ
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
	 * Android �������״̬
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
	 * �ܶ�������android�����У����������ֻ����������жϣ���Ϊ��Ŀǰ��androidƽ̨�ֻ���˵�����ܻ����4��״̬
	 * 1.�����磨����״̬��������Ϊ�ֻ�ͣ��������û�п������źŲ��õ�ԭ�� 2.ʹ��WIFI���� 3.CMWAP���й��ƶ����� 4.CMNET����
	 * ������״̬�����û�����磬�϶����޷�����Internet�ˣ������wap����ҪΪ�ֻ�����й��ƶ���������Ϊ�ֻ�����й��ƶ��Ĵ���
	 * 
	 * Email vipa1888@163.com QQ:840950105 ��ȡ��ǰ������״̬ -1��û������
	 * 1��WIFI����2��wap����3��net����
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
