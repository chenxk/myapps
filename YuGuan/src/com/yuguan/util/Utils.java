package com.yuguan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.yuguan.bean.AccountInfo;

public class Utils {

	public static String rootPaht = "http://192.168.10.106:8080/yuqiu";
	// ��ȡ��
	public static String countyUrl = rootPaht + "/util/county.htm?r=" + Math.random() + "&cid=";
	// ��ȡ�ͼƬ
	public static String activityImg = rootPaht + "/images/action/";
	// ��ȡ�û�ͷ��
	public static String userImg = rootPaht + "/images/user/";
	// ��ȡ���л
	public static String activityUrl = rootPaht + "/action/actionlist.htm?r=" + Math.random() + "&pg=";
	// ��ȡ�������Ϣ
	public static String actionInfoUrl = rootPaht + "/action/actioninfo.htm?r=" + Math.random() + "&aid=";
	// ��ȡ������û�
	public static String getBaoMingUsersUrl = rootPaht + "/action/baominguser.htm?r=" + Math.random() + "&aid=";
	// ��ȡ�����
	public static String getCommentUrl = rootPaht + "/action/getcomment.htm?r=" + Math.random() + "&aid=";
	// �û���¼
	public static String loginUrl = rootPaht + "/member/login.htm?r=" + Math.random() + "&yanzhengma=343&username=";
	// ��������
	public static String putCommentUrl = rootPaht + "/action/putcomment.htm?r=" + Math.random() + "&";
	// �����������
	public static String baomingtUrl = rootPaht + "/action/baoming.htm?r=" + Math.random() + "&uid=";
	// ����ղز���
	public static String shouCangUrl = rootPaht + "/util/collect.htm?r=" + Math.random() + "&type=2&itemId=";
	// ��ȡ���г���
	public static String mallListUrl = rootPaht + "/mall/malllist.htm?r=" + Math.random() + "&pg=";
	// ��ȡ������Ϣ
	public static String mallInfoUrl = rootPaht + "/mall/mallinfo.htm?r=" + Math.random() + "&mid=";
	// ��ȡ����ͼƬ
	public static String mallPicsUrl = rootPaht + "/mall/imginfo.htm?r=" + Math.random() + "&mid=";
	// ����ͼƬ
	public static String mallPicUrl = rootPaht + "/images/mall/";
	// �ղس���
	public static String mallShouCangUrl = rootPaht + "/util/collect.htm?type=1&r="+Math.random();
	// �������� mid="+mid+"&commentid="+commentId+"&
	public static String mallCommentUrl = rootPaht + "/mall/getcomment.htm?r="+Math.random() + "&mid=";
	// ���ݻ�����Ϣ
	public static String mallEnvInfoUrl = rootPaht + "/mall/envinfo.htm?r="+Math.random() + "&mid=";
	// ��ӳ������� "mid="+mid+"&uid="+uid+"&score="+score+"&textcontent="+text;
	public static String putMallCommentUrl = rootPaht + "/mall/putcomment.htm";
	// �����û� cid="+cid+"&pn="+pn+"&
	public static String friendsUrl = rootPaht + "/friend/friends.htm?r="+Math.random();
	// �û���Ϣ
	public static String friendInfoUrl = rootPaht + "/friend/friends.htm?r="+Math.random();
	
	public static int TIMEOUT = 5000;
	public static int cid = 173;
	public static AccountInfo loginInfo;
	private Context context;

	
	public static String getNowTime(){
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA);
		
		return	mSimpleDateFormat.format(new Date());
	}
	
	
	
	public static long getTimeByString(String str){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA);
		Date dates = new Date(str);
		
		return 0;
	}
	
	
	
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
