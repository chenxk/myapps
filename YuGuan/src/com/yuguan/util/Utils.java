package com.yuguan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import cn.buaa.myweixin.Login;

import com.yuguan.bean.AccountInfo;
import com.yuguan.bean.FriendBean;

public class Utils {

	public static String rootPaht = "http://192.168.10.100:8080/yuqiu";//http://www.yuguanwang.com	
	// 获取区
	public static String countyUrl = rootPaht + "/util/county.htm?r=" + Math.random() + "&cid=";
	// 获取活动图片
	public static String activityImg = rootPaht + "/images/action/";
	// 获取用户头像
	public static String userImg = rootPaht + "/images/user/";
	// 获取所有活动
	public static String activityUrl = rootPaht + "/action/actionlist.htm?r=" + Math.random() + "&pg=";
	// 获取单个活动信息
	public static String actionInfoUrl = rootPaht + "/action/actioninfo.htm?r=" + Math.random() + "&aid=";
	// 获取活动报名用户
	public static String getBaoMingUsersUrl = rootPaht + "/action/baominguser.htm?r=" + Math.random() + "&aid=";
	// 获取活动评价
	public static String getCommentUrl = rootPaht + "/action/getcomment.htm?r=" + Math.random() + "&aid=";
	// 用户登录
	public static String loginUrl = rootPaht + "/member/login.htm?r=" + Math.random() + "&yanzhengma=343&username=";
	// 发表评论
	public static String putCommentUrl = rootPaht + "/action/putcomment.htm?r=" + Math.random() + "&";
	// 点击报名操作
	public static String baomingtUrl = rootPaht + "/action/baoming.htm?r=" + Math.random() + "&uid=";
	// 点击收藏操作
	public static String shouCangUrl = rootPaht + "/util/collect.htm?r=" + Math.random() + "&type=2&itemId=";
	// 获取所有场馆
	public static String mallListUrl = rootPaht + "/mall/malllist.htm?r=" + Math.random() + "&pg=";
	// 获取场馆信息
	public static String mallInfoUrl = rootPaht + "/mall/mallinfo.htm?r=" + Math.random() + "&mid=";
	// 获取所有图片
	public static String mallPicsUrl = rootPaht + "/mall/imginfo.htm?r=" + Math.random() + "&mid=";
	// 场馆图片
	public static String mallPicUrl = rootPaht + "/images/mall/";
	// 收藏场馆
	public static String mallShouCangUrl = rootPaht + "/util/collect.htm?type=1&r="+Math.random();
	// 场馆评论 mid="+mid+"&commentid="+commentId+"&
	public static String mallCommentUrl = rootPaht + "/mall/getcomment.htm?r="+Math.random() + "&mid=";
	// 场馆环境信息
	public static String mallEnvInfoUrl = rootPaht + "/mall/envinfo.htm?r="+Math.random() + "&mid=";
	// 添加场馆评论 "mid="+mid+"&uid="+uid+"&score="+score+"&textcontent="+text;
	public static String putMallCommentUrl = rootPaht + "/mall/putcomment.htm";
	// 所有用户 cid="+cid+"&pn="+pn+"&
	public static String friendsUrl = rootPaht + "/friend/friends.htm?r="+Math.random();
	// 用户信息
	public static String friendInfoUrl = rootPaht + "/friend/friends.htm?r="+Math.random();
	// 发私信
	public static String sendPriMsgUrl = rootPaht + "/user/sendprvmsg.htm?";
	// 删除私信
	public static String delPriMsgUrl = rootPaht + "/user/delprvmsg.htm?r="+Math.random();
	// 添加好友
	public static String addFriendUrl = rootPaht + "/user/addfriend.htm?r="+Math.random();
	// 获取个人信息
	public static String getAccountInfoUrl = rootPaht + "/app/userinfo.htm?uid=";
	// 获取消息个数 {"actinvite":0,"addfrd":0,"notice":0,"prvmsg":0,"uid":28}
	public static String getMessageCountUrl = rootPaht + "/user/noticenum.htm?r="+Math.random();;
	// 上传文件
	public static String uploadFileUrl = rootPaht + "/fileUpload";
	// 系统消息  uid=28&msgid=-1	
	public static String getSysMsgUrl = rootPaht + "/user/sysnotice.htm?r="+Math.random();
	// 删除系统消息
	public static String delSysMsgUrl = rootPaht + "/user/delnotice.htm?r="+Math.random();
	// 好友私信  uid=28&type=1&msgid=-1  type = 2 是自己
	public static String getPriMsgUrl = rootPaht + "/user/prvmsg.htm?r="+Math.random();
	// 好友申请 uid=28&msgid=-1
	public static String getFriReqUrl = rootPaht + "/user/friendrequest.htm?r="+Math.random();
	// 处理好友申请  	uid="+uid+"&fuid="+fuid+"&type="+type+"
	public static String acceptFriReqUrl = rootPaht + "/user/acceptfriend.htm?r="+Math.random();
	// 活动邀请 uid=28&msgid=-1	
	public static String getSportReqUrl = rootPaht + "/user/actinvitemsg.htm?r="+Math.random();
	// 处理活动申请  	msgid=-1	
	public static String acceptSportReqUrl = rootPaht + "/user/delactinvitemsg.htm?r="+Math.random();
	// 组织的活动	uid="+uid+"&type="+type+"&aid="+aid+"
	public static String myOrgActionsUrl = rootPaht + "/user/orgactionlist.htm?r="+Math.random();
	// 参与的活动	uid="+uid+"&type="+type+"&aid="+aid+"
	public static String myJoinActionsUrl = rootPaht + "/user/actactionlist.htm?r="+Math.random();
	// 评分活动和球馆	"aid="+aid+"&uid="+uid+"&actscore="+actScore+"&acttext="+actText+"&mallscore="+mallScore+"&malltext="+mallText
	public static String putactreviewUrl = rootPaht + "/user/putactreview.htm?r="+Math.random();
	// 组织者评分
	public static String putorgreviewUrl = rootPaht + "/user/putorgreview.htm?jsonobj=";
	
	public static int TIMEOUT = 5000;
	public static int cid = 173;
	// 
	public static int uid = 0;
	public static AccountInfo loginInfo;
	public static FriendBean self;
	public static Bitmap selfPic;
	public static JSONObject msgCount;
	// 活动邀请
	public static int  actinvite;
	// 好友申请
	public static int  addfrd;
	// 系统通知
	public static int  notice;
	// 球友私信
	public static int  prvmsg;
	private Context context;
	
	public static final String USERCHANGEIMAGE = "USERCHANGEIMAGE";
	public static final String NEWUSERIMAGE = "NEWUSERIMAGE";

	
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
	
	
	public static void doCheckLogin(Context context){
		Intent intent = new Intent(context, Login.class);
		
		if(loginInfo != null){
			intent = new Intent(context, com.yuguan.activities.AccountInfo.class);
		}
		context.startActivity(intent);
	}
	
}
