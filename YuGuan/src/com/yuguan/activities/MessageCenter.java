package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

import com.yuguan.bean.FriendBean;
import com.yuguan.bean.user.SysNoticeBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MessageCenter extends Activity implements OnClickListener {

	
	private TextView sysMsgText;
	private TextView friendsMsgText;
	private LinearLayout sysMsg;
	private LinearLayout friendsMsg;
	private TextView friendReqText;
	private TextView sportReqText;
	private LinearLayout friendReq;
	private LinearLayout sportReq;
	private ViewPager messagePager;
	
	/** 系统消息 */
	private RefreshListView allMallsList;
	private SysNoticeAdepter mallAdapter;
	private List<SysNoticeBean> malls = new ArrayList<SysNoticeBean>();
	private boolean allMallsListIsLoad = false;
	private int curMallPage = 1;
	// 排序方式
	private int curMallSr = 0;
	// 区域ID
	private int curMallRg = 0;
	private final String KEY_MALL_JSON = "KEY_MALL_JSON";
	private String mallJson = InitValue.sysnotice;
	@SuppressLint("HandlerLeak")
	private Handler mallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_MALL_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				mallJson = result;
				malls.clear();
				getMallDataFromJson();
				mallAdapter.notifyDataSetChanged();
			}
		}
	};
	
	
	/** 好友私信 */
	private RefreshListView allFriendsList;
	private UserAdepter friendAdepter;
	private List<FriendBean> friends = new ArrayList<FriendBean>();
	private boolean allFriendsListIsLoad = false;
	private int curFriendPage = 1;
	// 排序方式
	private int curFriendSr = 0;
	// 区域ID
	private int curFriendRg = 0;
	private final String KEY_FRIEND_JSON = "KEY_FRIEND_JSON";
	private String friendJson = InitValue.primsg;

	@SuppressLint("HandlerLeak")
	private Handler friendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_FRIEND_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				friendJson = result;
				friends.clear();
				getFriendDataFromJson();
				friendAdepter.notifyDataSetChanged();
			}
		}
	};
	
	
	/** 好友申请 */
	private RefreshListView allFriendsReqList;
	private UserAdepter friendsReqAdepter;
	private List<FriendBean> friendsReq = new ArrayList<FriendBean>();
	private boolean allFriendsReqListIsLoad = false;
	private int curFriendsReqPage = 1;
	// 排序方式
	private int curFriendsReqr = 0;
	// 区域ID
	private int curFriendsReqRg = 0;
	private final String KEY_FRIENDREQ_JSON = "KEY_FRIENDREQ_JSON";
	private String friendsReqJson = InitValue.friendsreq;

	@SuppressLint("HandlerLeak")
	private Handler FriendsReqHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_FRIENDREQ_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				friendsReqJson = result;
				friendsReq.clear();
				getFriendDataFromJson();
				friendsReqAdepter.notifyDataSetChanged();
			}
		}
	};
	
	
	/** 活动邀请 */
	private RefreshListView allSportReqList;
	private UserAdepter sportReqAdepter;
	private List<FriendBean> sportReqs = new ArrayList<FriendBean>();
	private boolean allSportReqListIsLoad = false;
	private int curSportReqPage = 1;
	// 排序方式
	private int curSportReqr = 0;
	// 区域ID
	private int curSportReqRg = 0;
	private final String KEY_SPORTREQ_JSON = "KEY_SPORTREQ_JSON";
	private String sportReqJson = InitValue.sportReq;

	@SuppressLint("HandlerLeak")
	private Handler sportReqHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_SPORTREQ_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				sportReqJson = result;
				sportReqs.clear();
				getFriendDataFromJson();
				sportReqAdepter.notifyDataSetChanged();
			}
		}
	};
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.messageinfo);

			initView();
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
		

		messagePager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							initMallView();
						}

						if (position == 1) {
							initFriendView();
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
					}
				});

		sysMsg.setOnClickListener(new MyOnClickListener(0));
		friendsMsg.setOnClickListener(new MyOnClickListener(1));

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.message_sys, null);
		View view2 = mLi.inflate(R.layout.message_pri, null);
		// View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// views.add(view4);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				try {
					((ViewPager) container).addView(views.get(position));

					if (position == 0) {
						initMallView();
					}
				} catch (Exception e) {
					showSomeThing(e.toString() + " WWWW");
				}

				return views.get(position);
			}
		};

		messagePager.setAdapter(mPagerAdapter);

	}

	protected void getFriendDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(friendJson);
			JSONArray jsonArray = jsonObject.getJSONArray("frds");
			if (jsonArray.length() == 0) {
				showSomeThing("没有更多数据");
				allFriendsList.setLastData(true);
				return;
			} else {
				allFriendsList.setLastData(false);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				FriendBean bean = FriendBean.getBeanFromJson(json);
				if(Utils.loginInfo == null){
					if(bean != null)
						friends.add(bean);
				}else{
					if(bean != null && Utils.loginInfo.getId() != bean.getUid())
						friends.add(bean);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	protected void getMallDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(mallJson);
			JSONArray jsonArray = jsonObject.getJSONArray("msgs");
			if (jsonArray.length() == 0) {
				showSomeThing("没有更多数据");
				allMallsList.setLastData(true);
				return;
			} else {
				allMallsList.setLastData(false);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				SysNoticeBean bean = SysNoticeBean.getBeanByJson(json);
				if(bean != null)
				malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initMallView() {
		if (allMallsListIsLoad == false) {
			try {
				allMallsList = (RefreshListView) findViewById(R.id.messageSysList);
				allMallsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getMallInfo(view);
							}
						});
				
				allMallsList.setOnRefreshListener(new RefreshListView.RefreshListener() {

					@Override
					public Object refreshing() {
						return null;
					}

					@Override
					public void refreshed(Object obj) {
						/**/
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							mallJson = result;
							malls.clear();
							getMallDataFromJson();
							allMallsList.setLastRow(false);
							allMallsList.setLoading(false);
							mallAdapter.notifyDataSetChanged();
						}else{
							showSomeThing(result);
						}
						allMallsList.setSelection(1);
					}

					@Override
					public void more() {
						if(!allMallsList.isLastData()){
							++ curMallPage;
							String url = Utils.mallListUrl + curMallPage + "&rg=" + curMallRg + "&sr=" + curMallSr;
							allMallsList.setUrl(url);
						}else{
							showSomeThing("加载到最后一条了");
						}
					}

					@Override
					public void setUrl() {
						curMallPage = 1;
						String url = Utils.mallListUrl + curMallPage + "&rg=" + curMallRg + "&sr=" + curMallSr;
						allMallsList.setUrl(url);
					}

					@Override
					public void loaded(Object obj) {
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							mallJson = result;
							getMallDataFromJson();
							mallAdapter.notifyDataSetChanged();
							allMallsList.setLoading(false);
							
						}else{
							showSomeThing(result);
						}
						allMallsList.removeFootView();
					}
				});

				initMallList();
				String url = Utils.mallListUrl + curMallPage + "&rg=" + curMallRg + "&sr=" + curMallSr;
				new Thread(new HttpUtil(url,mallHandler, KEY_MALL_JSON)).start();
				
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allMallsListIsLoad = true;
		}
	}
	
	private void initMallList() {
		/**/
		getMallDataFromJson();
		try {
			allMallsList.setAdapter(null);
			/**/
			if(malls.size() < 5){
				allMallsList.removeHeaderView();
				allMallsList.removeFootView();
			}else{
				allMallsList.addHeaderView();
				allMallsList.addFootView();
			}
			mallAdapter = new SysNoticeAdepter(this, malls);
			allMallsList.setAdapter(mallAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFriendView() {
		if (allFriendsListIsLoad == false) {
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.messagePriList);
				allFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getFriendInfo(view);
							}
						});
				
				allFriendsList.setOnRefreshListener(new RefreshListView.RefreshListener() {

					@Override
					public Object refreshing() {
						return null;
					}

					@Override
					public void refreshed(Object obj) {
						/**/
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							friendJson = result;
							friends.clear();
							getFriendDataFromJson();
							allFriendsList.setLastRow(false);
							allFriendsList.setLoading(false);
							friendAdepter.notifyDataSetChanged();
						}else{
							showSomeThing(result);
						}
						allFriendsList.setSelection(1);
					}

					@Override
					public void more() {
						if(!allFriendsList.isLastData()){
							++ curFriendPage;
							String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curFriendPage;
							allFriendsList.setUrl(url);
						}else{
							showSomeThing("加载到最后一条了");
						}
					}

					@Override
					public void setUrl() {
						curFriendPage = 1;
						String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curFriendPage;
						allFriendsList.setUrl(url);
					}

					@Override
					public void loaded(Object obj) {
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							friendJson = result;
							getFriendDataFromJson();
							friendAdepter.notifyDataSetChanged();
							allFriendsList.setLoading(false);
							
						}else{
							showSomeThing(result);
						}
						allFriendsList.removeFootView();
					}
				});

				initFriendList();
				String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curFriendPage;
				new Thread(new HttpUtil(url,friendHandler, KEY_FRIEND_JSON)).start();
				
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allFriendsListIsLoad = true;
		}
	}

	private void initFriendList() {
		getFriendDataFromJson();
		try {
			allFriendsList.setAdapter(null);
			/**/
			if(friends.size() < 5){
				allFriendsList.removeHeaderView();
				allFriendsList.removeFootView();
			}else{
				allFriendsList.addHeaderView();
				allFriendsList.addFootView();
			}
			friendAdepter = new UserAdepter(this, friends);
			allFriendsList.setAdapter(friendAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
		
	}
	
	
	public void getFriendInfo(View v) {
		try {
			Intent intent = new Intent(MessageCenter.this, FriendInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}
	
	public void getMallInfo(View v) { // 小黑 对话界面
		try {
			// 系统通知  sysmsginfo.xml
			// 好友添加  friendapplyinfo.xml
			// 活动邀请  sportapplyinfo.xml
			TextView idView = (TextView)v.findViewById(R.id.mallId);
			int id = Integer.parseInt(idView.getText().toString());
			Intent intent = new Intent(MessageCenter.this, MallInfo.class);
			Bundle bundle = new Bundle();
			bundle.putInt("mallId", id);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}
	
	private void initView() {
		friendsMsgText = (TextView) findViewById(R.id.friendsMsgText);
		sysMsgText = (TextView) findViewById(R.id.sysMsgText);
		sysMsg = (LinearLayout) findViewById(R.id.sysMsg);
		friendsMsg = (LinearLayout) findViewById(R.id.friendsMsg);
		friendReqText = (TextView) findViewById(R.id.friendReqText);
		sportReqText = (TextView) findViewById(R.id.sportReqText);
		friendReq = (LinearLayout) findViewById(R.id.friendReq);
		sportReq = (LinearLayout) findViewById(R.id.sportReq);
		messagePager = (ViewPager) findViewById(R.id.messagePager);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sysMsg:
			initMallView();
			break;
		case R.id.friendsMsg:
			initFriendView();
			break;
		case R.id.msgcenter_back:
			doBack(v);
			break;
		case R.id.msgcenter_login:
			doLogin(v);
			break;

		default:
			break;
		}
	}


	public void doBack(View v) {
		this.finish();
	}

	public void doLogin(View v) {
		Intent intent = new Intent(MessageCenter.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	/**
	 * 点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			messagePager.setCurrentItem(index);
			if (index == 0) {
				// friendsMsgText.set
			} else {

			}
		}
	};
}
