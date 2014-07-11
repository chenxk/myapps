package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

import com.yuguan.bean.FriendBean;
import com.yuguan.util.HttpPostUtil;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.Utils;

public class FriendInfo extends Activity {

	private LayoutInflater inflater;
	private View layout;
	private PopupWindow menuWindow;
	private boolean menu_display = false;
	private LinearLayout sendMsg;
	private TextView friendName;
	private TextView friendAddress2;
	private TextView sendFriendName;
	private RoundImageView friendImg;
	private ImageView friendSex;
	private TextView xinyu;
	private TextView fenshu;
	private TextView level;
	private TextView levelfenshu;
	private LinearLayout cancelMsg;
	private EditText editText;
	private FriendBean bean;
	private ImageLoader mImageLoader;
	private int friendId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.friendinfo);
			mImageLoader = new ImageLoader(getApplicationContext());
			bean = (FriendBean) getIntent().getSerializableExtra("friendBean");
			initView();
			initValue();
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

	}

	private void initView() {
		friendName = (TextView) findViewById(R.id.friendName);
		friendImg = (RoundImageView) findViewById(R.id.friendImg);
		friendSex = (ImageView) findViewById(R.id.friendSex2);
		friendAddress2 = (TextView) findViewById(R.id.friendAddress2);
		xinyu = (TextView) findViewById(R.id.xinyustars);
		fenshu = (TextView) findViewById(R.id.xinyufenshu);
		level = (TextView) findViewById(R.id.level);
		levelfenshu = (TextView) findViewById(R.id.levelfenshu);
	}

	private void initValue() {
		if (bean != null) {
			friendName.setText(bean.getName());
			String url = Utils.userImg + bean.getPic();
			friendImg.setTag(url);
			mImageLoader.loadImage(url, null, friendImg);
			friendId = bean.getUid();
			friendAddress2.setText(bean.getAddr());
			if (bean.getSex() == 0) {
				friendSex.setImageResource(R.drawable.boy_48);
			} else {
				friendSex.setImageResource(R.drawable.girl_48);
			}

			if (bean.getStar() == 0) {
				xinyu.setText("");
			} else {
				String s = "☆";
				for (int i = 0; i < bean.getStar(); i++) {
					s += s;
				}
				xinyu.setText(s);
			}
			fenshu.setText(bean.getRpu() + "");
			level.setText(bean.getLevel());
			levelfenshu.setText(bean.getSkill() + "");
		}
	}

	public void doSendMsg(View v) {

		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		
		if(Utils.loginInfo.getId() == friendId){
			showSomeThing("不能给自己发私信...");
			return;
		}
		
		if (menu_display == false) {
			// 获取LayoutInflater实例
			try {
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.sendmessage, null);
				// 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(false);
				menuWindow.setBackgroundDrawable(new BitmapDrawable());
				menuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				menuWindow.showAsDropDown(layout); //设置弹出效果
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				View parent = findViewById(R.id.mainfriendinfo);
				parent.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (menu_display) {
							menuWindow.dismiss();
							menu_display = false;
						}
					}
				});
				
				menuWindow.showAtLocation(parent, Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
				//监听触屏事件
				menuWindow.setTouchInterceptor(new View.OnTouchListener() {
					public boolean onTouch(View view, MotionEvent event) {	
						return false;								
					}
				});
				
				// 如何获取我们main中的控件呢？也很简单
				// mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				sendMsg = (LinearLayout) layout.findViewById(R.id.sendMsg);
				sendFriendName = (TextView) layout.findViewById(R.id.sendFriendName);
				sendFriendName.setText("发送给：" + bean.getName());
				cancelMsg = (LinearLayout) layout.findViewById(R.id.cancelMsg);
				editText = (EditText) layout.findViewById(R.id.msgcontent);
				popupInputMethodWindow();

				// 下面对每一个Layout进行单击事件的注册吧。。。
				// 比如单击某个MenuItem的时候，他的背景色改变
				// 事先准备好一些背景图片或者颜色
				sendMsg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String msg = editText.getText().toString();
						if (msg.length() == 0) {
							showSomeThing("请输入信息");
							return;
						}
						String url = Utils.sendPriMsgUrl + "tuid=" + friendId + "&fuid=" + Utils.loginInfo.getId();
						
						NameValuePair content = new BasicNameValuePair("msg",msg);
						HttpPostUtil postUtil = new HttpPostUtil(url, new Handler(){
							 @Override
							public void handleMessage(Message msg) {
								 super.handleMessage(msg);
									Bundle data = msg.getData();
									String result = data.getString("LOGINAJAX");
									if (result != null && !"服务访问失败".equals(result)) {
										try {
											JSONObject json = new JSONObject(result);
											String status = json.getString("status");
											if(status.equals("suc")){
												menuWindow.dismiss(); // 响应点击事件之后关闭Menu
												showSomeThing("私信发送成功!");
											}else{
												showSomeThing("私信发送失败!");
											}
										} catch (JSONException e) {
											e.printStackTrace();
										} 
									}else{
										showSomeThing("私信发送失败!");
									}
							}
						 }, "LOGINAJAX");
						
						List<NameValuePair> pairs = new ArrayList<NameValuePair>();
						pairs.add(content);
						postUtil.setPairs(pairs);
						
						new Thread(postUtil).start();
						
					}
				});

				cancelMsg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						editText.setText("");
						menuWindow.dismiss();
						menu_display = false;
					}
				});
			} catch (Exception e) {
				showSomeThing(e.toString());
			}
			menu_display = true;
		} else {
			// 如果当前已经为显示状态，则隐藏起来
			menuWindow.dismiss();
			menu_display = false;
		}
	}
	
	private InputMethodManager imm;
	private void popupInputMethodWindow() {  
	    new Thread((new Runnable() {  
	        @Override  
	        public void run() {  
	            imm = (InputMethodManager) layout.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);  
	            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	        }  
	    })).start();;  
	} 

	public void doAddFriend(View v) {
		
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		
		String url = Utils.addFriendUrl + "&tuid=" + friendId + "&fuid=" + Utils.loginInfo.getId();
		new Thread(new HttpUtil(url, new Handler(){
			 @Override
			public void handleMessage(Message msg) {
				 super.handleMessage(msg);
					Bundle data = msg.getData();
					String result = data.getString("ADDFRIENDJAX");
					if (result != null && !"服务访问失败".equals(result)) {
						try {
							JSONObject json = new JSONObject(result);
							String status = json.getString("status");
							if(status.equals("suc")){
								showSomeThing("申请发送成功，请等待对方验证！");
							}else if(status.equals("over")){
								showSomeThing("亲爱的球友，您的好友数已经到达系统上限，不能再主动添加好友，请谅解！");
							}else{
								showSomeThing("申请发送失败，请稍后再试");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} 
					}else{
						showSomeThing("申请发送失败，请稍后再试");
					}
			 }}
		 ,"ADDFRIENDJAX")).start();
		
	}

	public void dofriendinfoBack(View v) {
		this.finish();
	}

	public void dofriendinfoLogin(View v) {
		Intent intent = new Intent(FriendInfo.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
}
