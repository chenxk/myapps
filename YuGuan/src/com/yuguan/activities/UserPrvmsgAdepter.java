package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.UserPrvmsgBean;
import com.yuguan.util.HttpPostUtil;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.Utils;

public class UserPrvmsgAdepter extends BaseAdapter {

	private List<UserPrvmsgBean> coll;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private Context ctx;
	private View parent;

	public UserPrvmsgAdepter(Context context, List<UserPrvmsgBean> coll,
			View parent) {
		this.coll = coll;
		this.ctx = context;
		this.parent = parent;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.friendmsg, null);
			RoundImageView userImage = (RoundImageView) convertView
					.findViewById(R.id.userImage);
			TextView msgId = (TextView) convertView.findViewById(R.id.msgId);
			TextView friendmsgId = (TextView) convertView
					.findViewById(R.id.friendmsgId);
			TextView friendmsgname = (TextView) convertView
					.findViewById(R.id.friendmsgname);
			TextView friendContent = (TextView) convertView
					.findViewById(R.id.friendContent);
			TextView msgSendTime = (TextView) convertView
					.findViewById(R.id.msgSendTime);
			LinearLayout huifu = (LinearLayout) convertView
					.findViewById(R.id.huifu);
			LinearLayout deleteMsg = (LinearLayout) convertView
					.findViewById(R.id.deleteMsg);

			holder.msgId = msgId;
			holder.friendContent = friendContent;
			holder.friendmsgId = friendmsgId;
			holder.friendmsgname = friendmsgname;
			holder.huifu = huifu;
			holder.deleteMsg = deleteMsg;
			holder.msgSendTime = msgSendTime;
			holder.userImage = userImage;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
			final UserPrvmsgBean bean = coll.get(position);
			if (bean.getType() == 1) {
				holder.huifu.setVisibility(View.VISIBLE);
			} else {
				holder.huifu.setVisibility(View.INVISIBLE);
			}
			holder.msgId.setText(bean.getId() + "");
			holder.friendContent.setText(bean.getText());
			holder.friendmsgId.setText(bean.getUid() + "");
			holder.friendmsgname.setText(bean.getUname());
			holder.msgSendTime.setText(bean.getFtime());

			String url = Utils.userImg + bean.getUpic();
			holder.userImage.setTag(url);
			mImageLoader.loadImage(url, this, holder.userImage);

			holder.huifu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					doSendMsg(bean.getUname(), bean.getUid());
				}
			});

			holder.deleteMsg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// doSendMsg(bean.getUname(), bean.getUid());
					deleteMsg(bean.getId(), bean.getType());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return convertView;
	}

	
	public void setconvertViewGone(long id){
		UserPrvmsgBean temp = null;
		for(UserPrvmsgBean bean : coll){
			if(bean.getId() == id){
				temp = bean;
				break;
			}
		}
		if(temp != null){
			coll.remove(temp);
			this.notifyDataSetChanged();
		}
	}
	
	
	public void deleteMsg(final long id, int type) {
		String url = Utils.delPriMsgUrl + "&type=" + type + "&msgid=" + id;
		new Thread(new HttpUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("DELETE_PRIMSG");
				if (result.length() > 0 && !result.equals("服务访问失败")) {
					showSomeThing("删除成功!");
					setconvertViewGone(id);
				}
			}
		}, "DELETE_PRIMSG")).start();
	}

	// 发信息
	private LayoutInflater inflater;
	private View layout;
	private PopupWindow menuWindow;
	private boolean menu_display = false;
	private EditText editText;
	private LinearLayout sendMsg;
	private TextView sendFriendName;
	private LinearLayout cancelMsg;

	public void doSendMsg(String name, final long friendId) {

		if (menu_display == false) {
			// 获取LayoutInflater实例
			try {
				inflater = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.sendmessage, null);
				// 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(false);
				menuWindow.setBackgroundDrawable(new BitmapDrawable());
				menuWindow
						.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				menuWindow.showAsDropDown(layout); // 设置弹出效果
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(parent, Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
				// 监听触屏事件
				menuWindow.setTouchInterceptor(new View.OnTouchListener() {
					public boolean onTouch(View view, MotionEvent event) {
						return false;
					}
				});

				// 如何获取我们main中的控件呢？也很简单
				// mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				sendMsg = (LinearLayout) layout.findViewById(R.id.sendMsg);
				sendFriendName = (TextView) layout
						.findViewById(R.id.sendFriendName);
				sendFriendName.setText("发送给：" + name);
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
						String url = Utils.sendPriMsgUrl + "tuid=" + friendId
								+ "&fuid=" + Utils.loginInfo.getId();

						NameValuePair content = new BasicNameValuePair("msg",
								msg);
						HttpPostUtil postUtil = new HttpPostUtil(url,
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										super.handleMessage(msg);
										Bundle data = msg.getData();
										String result = data
												.getString("LOGINAJAX");
										if (result != null
												&& !"服务访问失败".equals(result)) {
											try {
												JSONObject json = new JSONObject(
														result);
												String status = json
														.getString("status");
												if (status.equals("suc")) {
													menuWindow.dismiss(); // 响应点击事件之后关闭Menu
													showSomeThing("私信发送成功!");
												} else {
													showSomeThing("私信发送失败!");
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										} else {
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
				imm = (InputMethodManager) layout.getContext()
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		})).start();
		;
	}

	public void showSomeThing(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}

	/** 存放控件 */
	public final class ViewHolder {
		public RoundImageView userImage;
		public TextView friendmsgId;
		public TextView friendmsgname;
		public TextView friendContent;
		public TextView msgSendTime;
		public LinearLayout huifu;
		public LinearLayout deleteMsg;
		public TextView msgId;
	}

}
