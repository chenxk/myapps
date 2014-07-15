package cn.buaa.myweixin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuguan.activities.CommentAdepter;
import com.yuguan.activities.RefreshListView;
import com.yuguan.bean.CommentBean;
import com.yuguan.bean.UserBean;
import com.yuguan.util.HttpPostUtil;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

/**
 * 
 */
public class ChatActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ScrollView screen;
	private TextView title;
	private ImageView image;
	private TextView actionHouse;
	private TextView actionAdress;
	private TextView actionCreater;
	private TextView actionTel;
	private TextView actionPrice;
	private TextView actionBegin;
	private TextView actionEnd;
	private TextView actionContext;
	private TextView baomingEndTime;
	private TextView baoming;
	private ImageView loveImg;
	private TextView shoucang;
	private TextView userCount;
	private LinearLayout usersInfoScrollView;

	private String mallInfoJson = InitValue.actionInfoJson;
	private String userInfoJson = InitValue.actionInfoJson;
	private String commentJson = InitValue.commentJson;
	private int actionId;
	private int commentId = 0;
	private ImageLoader mImageLoader;
	private RefreshListView commentList;
	private CommentAdepter commentAdepter;
	
	private List<CommentBean> comments = new ArrayList<CommentBean>();

	private final String KEY_MALLINFO_JSON = "KEY_MALLINFO_JSON";
	@SuppressLint("HandlerLeak")
	private Handler mallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_MALLINFO_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				mallInfoJson = result;
			}
			getMallInfoFromJson();
		}
	};

	private final String KEY_USERINFO_JSON = "KEY_USERINFO_JSON";
	@SuppressLint("HandlerLeak")
	private Handler userHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_USERINFO_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				userInfoJson = result;
			}
			getUserInfoFromJson();
		}
	};
	
	private final String KEY_COMMENT_JSON = "KEY_COMMENT_JSON";
	@SuppressLint("HandlerLeak")
	private Handler commentHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				Bundle data = msg.getData();
				String result = data.getString(KEY_COMMENT_JSON);
				if (result != null && !"服务访问失败".equals(result)) {
					commentJson = result;
				}
				initCommentListView();
			} catch (Exception e) {
				showSomeThing(e.toString());
			}
			
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//init();
	}
	
	public void init(){
		actionId = getIntent().getIntExtra("actionId", 0);
		mImageLoader = new ImageLoader(getApplicationContext());
		setContentView(R.layout.chat_xiaohei);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		initView();

		new Thread(new HttpUtil(Utils.actionInfoUrl + actionId, mallHandler,
				KEY_MALLINFO_JSON)).start();
		new Thread(new HttpUtil(Utils.getBaoMingUsersUrl + actionId,
				userHandler, KEY_USERINFO_JSON)).start();
		new Thread(new HttpUtil(Utils.getCommentUrl + actionId +"&commentid=" + commentId,
				commentHandler, KEY_COMMENT_JSON)).start();
	}
	

	public void doBaoMin(View v) {
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		String s = baoming.getText().toString();
		if(s.equals("活动已结束")){
			showSomeThing(s);
			return;
		}
		if(s.equals("已报名")){
			showSomeThing(s);
			return;
		}
		String url = Utils.baomingtUrl + Utils.loginInfo.getId() + "&aid" + actionId;
		new Thread(new HttpUtil(url, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("BAOMING");
				if (result != null && !"服务访问失败".equals(result)) {
					try {
						JSONObject json = new JSONObject(result);
						if(json.getString("status").equals("suc")){
							baoming.setText("已报名");
							showSomeThing("报名成功!");
							new Thread(new HttpUtil(Utils.getBaoMingUsersUrl + actionId,
									userHandler, KEY_USERINFO_JSON)).start();
						}else{
							showSomeThing("报名失败!");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					showSomeThing("服务访问失败!");
				}
			}
		}, "BAOMING")).start();
		
	}

	public void doShoucang(View v) {
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		String s = shoucang.getText().toString();
		
		if(s.equals("已收藏")){
			showSomeThing("已收藏");
			return;
		}
		String url = Utils.shouCangUrl + actionId + "&uid" + Utils.loginInfo.getId();
		new Thread(new HttpUtil(url, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub success
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("SHOUCANG");
				if (result != null && !"服务访问失败".equals(result)) {
					try {
						// {"itemId":"13","status":"success","type":"2","uid":"28"}
						JSONObject json = new JSONObject(result);
						//showSomeThing(result);
						if(json.getString("status").equals("success")){
							//shoucang.setText("已收藏");
							loveImg.setImageResource(R.drawable.love_48);
							showSomeThing("收藏成功!");
						}else{
							showSomeThing("收藏失败!");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					showSomeThing("服务访问失败!");
				}
			}
		}, "SHOUCANG")).start();
	}
	

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	public void getMallInfoFromJson() {
		// 活动详情和报名用户列表：
		try {
			JSONObject json = new JSONObject(mallInfoJson);
			if (json != null) {
				JSONObject actionInfo = json.getJSONObject("actionInfo");
				if (actionInfo == null)
					return;
				title.setText(actionInfo.getString("aName"));
				actionHouse.setText(actionInfo.getString("mName"));
				actionAdress.setText(actionInfo.getString("mAddress"));
				actionCreater.setText(actionInfo.getString("poster"));
				actionTel.setText(actionInfo.getString("phone"));
				actionPrice.setText(actionInfo.getString("price"));
				String stime = actionInfo.getString("sTime");
				actionBegin.setText(stime);
				actionEnd.setText(actionInfo.getString("eTime"));
				actionContext.setText(actionInfo.getString("desc"));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
					Date date = sdf.parse(stime + ":00");
					Date now = new Date(System.currentTimeMillis());
					if(now.after(date)){
						baoming.setText("活动已结束");
						baomingEndTime.setText("");
					}else{
						baoming.setText("立即报名");
						baomingEndTime.setText(stime + "截止");
					}
				} catch (Exception e) {
					showSomeThing(e.toString());
				}
				
				String url = Utils.activityImg
						+ actionInfo.getString("backPic");
				mImageLoader.loadImage(url, null, image);
				
			}
		} catch (JSONException e) {
			showSomeThing(e.toString());
		}
	}

	public void getUserInfoFromJson() {
		try {
			JSONObject mallJson = new JSONObject(userInfoJson);
			if (mallJson != null) {
				JSONArray users = mallJson.getJSONArray("users");
				if (users != null && users.length() > 0) {
					int count = users.length();
					userCount.setText(count + "");
					usersInfoScrollView.removeAllViews();
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					for (int i = 0; i < count; i++) {
						JSONObject json = (JSONObject) users.get(i);
						UserBean bean = UserBean.getBeanFromJson(json);
						if (bean != null) {
							UserImage user = new UserImage(getApplicationContext());
							user.initData(bean);
							if(Utils.loginInfo != null && bean.getUid() == Utils.loginInfo.getId()){
								baoming.setText("已报名");
							}
							usersInfoScrollView.addView(user, i, params);
						}else{
							showSomeThing("user is null");
						}
					}
				}
			}
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}
	
	public void getCommentFromJson() {
		try {
			JSONObject mallJson = new JSONObject(commentJson);
			if (mallJson != null) {
				
				JSONArray users = mallJson.getJSONArray("comments");
				if (users != null && users.length() > 0) {
					int count = users.length();
					if(count >= 10){
						commentId = ((JSONObject)users.get(9)).getInt("id");
						commentList.setLastData(false);
						commentList.addFootView();
					}else{
						commentList.setLastData(true);
						commentList.removeFootView();
					}
					for (int i = 0; i < count; i++) {
						JSONObject json = (JSONObject) users.get(i);
						CommentBean bean = CommentBean.getCommentBean(json);
						if (bean != null) {
							comments.add(bean);
						}else{
							showSomeThing("CommentInfo is null");
						}
					}
				}
			}
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}
	
	
	public void setCommentListHeight(){
		ViewGroup.LayoutParams params = commentList.getLayoutParams();  
		int totalHeight = 0;  
		if(comments.size() < 5){
			 for (int i = 0; i < commentAdepter.getCount(); i++) {  
	            View listItem = commentAdepter.getView(i, null, commentList);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }
	        params.height = totalHeight + (commentList.getDividerHeight() * (commentAdepter.getCount() - 1));  
		}else{
	        params.height = 600;  
		}
		commentList.setLayoutParams(params);
	}
	
	public void initCommentListView(){
		getCommentFromJson();
		
		if(comments.size() == 0){
			commentList.setVisibility(View.GONE);
			return;
		}
		
		commentList.setVisibility(View.VISIBLE);
		commentAdepter = new CommentAdepter(this, comments);
		try {
			commentList.setAdapter(commentAdepter);
			setCommentListHeight();
			commentList.setOnRefreshListener(new RefreshListView.RefreshListener() {

				@Override
				public Object refreshing() {
					return null;
				}

				@Override
				public void setUrl() {
					commentId = 0;
					String url = Utils.getCommentUrl + actionId +"&commentid=" + commentId;
					commentList.setUrl(url);
				}
				
				@Override
				public void refreshed(Object obj) {
					/**/
					if (obj == null) {
						return;
					}

					String result = obj.toString();
					if (result.length() > 0 && !result.equals("服务访问失败")) {
						commentJson = result;
						comments.clear();
						getCommentFromJson();
						commentList.setLastRow(false);
						commentList.setLoading(false);
						commentAdepter.notifyDataSetChanged();
					}else{
						showSomeThing(result);
					}
					commentList.setSelection(1);
				}

				@Override
				public void more() {
					if(!commentList.isLastData()){
						String url = Utils.getCommentUrl + actionId +"&commentid=" + commentId;
						commentList.setUrl(url);
					}else{
						showSomeThing("加载到最后一条了");
					}
				}

				@Override
				public void loaded(Object obj) {
					if (obj == null) {
						return;
					}

					String result = obj.toString();
					if (result.length() > 0 && !result.equals("服务访问失败")) {
						commentJson = result;
						getCommentFromJson();
						commentAdepter.notifyDataSetChanged();
						commentList.setLoading(false);
					}else{
						showSomeThing(result);
					}
					commentList.removeFootView();
				}
			});
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}

	public void initView() {
		screen = (ScrollView) findViewById(R.id.scrollView);
		screen.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		title = (TextView) findViewById(R.id.mallTit);
		image = (ImageView) findViewById(R.id.mallImage);
		actionHouse = (TextView) findViewById(R.id.actionHouse);
		actionAdress = (TextView) findViewById(R.id.actionAdress);
		actionCreater = (TextView) findViewById(R.id.actionCreater);
		actionTel = (TextView) findViewById(R.id.actionTel);
		actionPrice = (TextView) findViewById(R.id.actionPrice);
		actionBegin = (TextView) findViewById(R.id.actionBegin);
		actionEnd = (TextView) findViewById(R.id.actionEnd);
		actionContext = (TextView) findViewById(R.id.actionContext);
		commentList = (RefreshListView) findViewById(R.id.commentList);
		commentList.setInterup(true);
		commentList.setParent(screen);
		baoming = (TextView) findViewById(R.id.baoming);
		loveImg = (ImageView) findViewById(R.id.loveImg);
		shoucang = (TextView) findViewById(R.id.shoucang);
		baomingEndTime = (TextView) findViewById(R.id.baomingEndTime);
		userCount = (TextView) findViewById(R.id.usrNum);
		usersInfoScrollView = (LinearLayout) findViewById(R.id.usersInfo);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		
	}


	// 设置标题栏右侧按钮的作用
	public void doLogin(View v) {
		Intent intent = new Intent(ChatActivity.this, Login.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "点击了功能按钮",
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			 send(v);
			break;
		case R.id.btn_back:
			finish();
			break;
		}
		
		
	}

	public void chat_back(View v) {
		this.finish();
	}

	public void send(View v) {
		String contString = mEditTextContent.getText().toString();
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		if (contString.length() > 0) {
			
			String url = Utils.putCommentUrl + "aid=" + actionId + "&uid=" + Utils.loginInfo.getId();
			
			NameValuePair content = new BasicNameValuePair("textcontent",contString);
			HttpPostUtil postUtil = new HttpPostUtil(url, new Handler(){
				 @Override
				public void handleMessage(Message msg) {
					 super.handleMessage(msg);
						Bundle data = msg.getData();
						String result = data.getString("LOGINAJAX");
						if (result != null && !"服务访问失败".equals(result)) {
							try {
								// {"aid":"13","status":"suc","textcontent":"sfsdfdfsds;","uid":"28"}
								JSONObject json = new JSONObject(result);
								String status = json.getString("status");
								if(status.equals("suc")){
									if(commentAdepter == null){
										new Thread(new HttpUtil(Utils.getCommentUrl + actionId +"&commentid=" + commentId,
												commentHandler, KEY_COMMENT_JSON)).start();
									}else{
										CommentBean bean = new CommentBean();
										String comment = json.getString("textcontent");
										//comment = URLEncoder.encode(comment, "utf-8");
										bean.setComment(comment);
										bean.setId(0);
										bean.setPostTime(Utils.getNowTime());
										bean.setUid(Utils.loginInfo.getId());
										bean.setuName(Utils.loginInfo.getUserName());
										comments.add(0, bean);
										commentAdepter.notifyDataSetChanged();
										setCommentListHeight();
										commentList.setSelection(1);
									}
								}else{
									showSomeThing("评论发布失败!");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} 
						}else{
							showSomeThing("评论提交失败!");
						}
				}
			 }, "LOGINAJAX");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(content);
			postUtil.setPairs(pairs);
			
			new Thread(postUtil).start();
			
		}else{
			showSomeThing("请输入评论内容");
		}
		mEditTextContent.setText("");
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  
		mEditTextContent.setCursorVisible(false);//失去光标
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	public void head_xiaohei(View v) { // 标题栏 返回按钮
		Intent intent = new Intent(ChatActivity.this, InfoXiaohei.class);
		startActivity(intent);
	}
}