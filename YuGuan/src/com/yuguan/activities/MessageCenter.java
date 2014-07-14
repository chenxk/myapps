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

import com.yuguan.bean.user.FrdRequestMsgBean;
import com.yuguan.bean.user.HuodongInviteMsgBean;
import com.yuguan.bean.user.SysNoticeBean;
import com.yuguan.bean.user.UserPrvmsgBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MessageCenter extends Activity implements OnClickListener {

	private LinearLayout parent;
	private TextView sysMsgText;
	private TextView friendsMsgText;
	private LinearLayout sysMsg;
	private LinearLayout friendsMsg;
	private TextView friendReqText;
	private TextView sportReqText;
	private LinearLayout friendReq;
	private LinearLayout sportReq;
	private ViewPager messagePager;
	private TextView reciveMsg;
	private LinearLayout friendMsgBtn;
	private TextView sendMsg;
	private LinearLayout myMsgBtn;
	private List<TextView> list = new ArrayList<TextView>();

	/** 系统消息 */
	private RefreshListView allMallsList;
	private SysNoticeAdepter mallAdapter;
	private List<SysNoticeBean> malls = new ArrayList<SysNoticeBean>();
	private boolean allMallsListIsLoad = false;
	private int noticemsgid = -1;
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
	private UserPrvmsgAdepter friendAdepter;
	private List<UserPrvmsgBean> friends = new ArrayList<UserPrvmsgBean>();
	private boolean allFriendsListIsLoad = false;
	private int friendmsgid = -1;
	private int friendtype = 1;
	private final String KEY_FRIEND_JSON = "KEY_FRIEND_JSON";
	private String friendJson = InitValue.primsg;
	private int uid;

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
	private FrdRequestAdepter friendsReqAdepter;
	private List<FrdRequestMsgBean> friendsReq = new ArrayList<FrdRequestMsgBean>();
	private boolean allFriendsReqListIsLoad = false;
	private int frdreqmsgid = -1;
	private final String KEY_FRIENDREQ_JSON = "KEY_FRIENDREQ_JSON";
	private String friendsReqJson = InitValue.friendsreq;

	@SuppressLint("HandlerLeak")
	private Handler friendsReqHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_FRIENDREQ_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				friendsReqJson = result;
				friendsReq.clear();
				getFrdReqDataFromJson();
				friendsReqAdepter.notifyDataSetChanged();
			}
		}
	};

	/** 活动邀请 */
	private RefreshListView allSportReqList;
	private SportRequestAdepter sportReqAdepter;
	private List<HuodongInviteMsgBean> sportReqs = new ArrayList<HuodongInviteMsgBean>();
	private boolean allSportReqListIsLoad = false;
	private int sportmsgid = -1;
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
				getSportReqDataFromJson();
				sportReqAdepter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.messageinfo);
			uid = this.getIntent().getExtras().getInt("uid");
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
						setFontStyle(position);
						if (position == 0) {
							initMallView();
						}

						if (position == 1) {
							initFriendMsgView();
						}

						if (position == 2) {
							initFrdReqView();
						}

						if (position == 3) {
							initSportReqView();
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
		friendReq.setOnClickListener(new MyOnClickListener(2));
		sportReq.setOnClickListener(new MyOnClickListener(3));

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.message_sys, null);
		View view2 = mLi.inflate(R.layout.message_pri, null);
		View view3 = mLi.inflate(R.layout.friendreqlist, null);
		View view4 = mLi.inflate(R.layout.sportreqlist, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
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
			JSONArray jsonArray = jsonObject.getJSONArray("msgs");
			int count = jsonArray.length();
			if (count >= 10) {
				friendmsgid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allFriendsList.setLastData(false);
				allFriendsList.addFootView();
			} else {
				allFriendsList.setLastData(true);
				allFriendsList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				UserPrvmsgBean bean = UserPrvmsgBean.getBeanByJson(json);
				bean.setType(friendtype);
				if (bean != null)
					friends.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void getMallDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(mallJson);
			JSONArray jsonArray = jsonObject.getJSONArray("msgs");
			int count = jsonArray.length();
			if (count >= 10) {
				noticemsgid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allMallsList.setLastData(false);
				allMallsList.addFootView();
			} else {
				allMallsList.setLastData(true);
				allMallsList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				SysNoticeBean bean = SysNoticeBean.getBeanByJson(json);
				if (bean != null)
					malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void getFrdReqDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(friendsReqJson);
			JSONArray jsonArray = jsonObject.getJSONArray("msgs");
			int count = jsonArray.length();
			if (count >= 10) {
				frdreqmsgid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allFriendsReqList.setLastData(false);
				allFriendsReqList.addFootView();
			} else {
				allFriendsReqList.setLastData(true);
				allFriendsReqList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				FrdRequestMsgBean bean = FrdRequestMsgBean.getBeanByJson(json);
				if (bean != null)
					friendsReq.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void getSportReqDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(sportReqJson);
			JSONArray jsonArray = jsonObject.getJSONArray("msgs");
			int count = jsonArray.length();
			if (count >= 10) {
				sportmsgid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allSportReqList.setLastData(false);
				allSportReqList.addFootView();
			} else {
				allSportReqList.setLastData(true);
				allSportReqList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				HuodongInviteMsgBean bean = HuodongInviteMsgBean
						.getBeanByJson(json);
				if (bean != null)
					sportReqs.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initSportReqView() {
		if (allSportReqListIsLoad == false) {
			try {
				allSportReqList = (RefreshListView) findViewById(R.id.sportReqList);
				allSportReqList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// getMallInfo(view);
							}
						});

				allSportReqList
						.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									sportReqJson = result;
									sportReqs.clear();
									getSportReqDataFromJson();
									allSportReqList.setLastRow(false);
									allSportReqList.setLoading(false);
									sportReqAdepter.notifyDataSetChanged();
								} else {
									showSomeThing(result);
								}
								allSportReqList.setSelection(1);
							}

							@Override
							public void more() {
								if (!allSportReqList.isLastData()) {
									String url = Utils.getSportReqUrl + "&uid="
											+ uid + "&msgid=" + sportmsgid;
									allSportReqList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								sportmsgid = -1;
								String url = Utils.getSportReqUrl + "&uid="
										+ uid + "&msgid=" + sportmsgid;
								allSportReqList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									sportReqJson = result;
									getSportReqDataFromJson();
									sportReqAdepter.notifyDataSetChanged();
									allSportReqList.setLoading(false);

								} else {
									showSomeThing(result);
								}
								allSportReqList.removeFootView();
							}
						});

				initSportReqList();
				String url = Utils.getSportReqUrl + "&uid=" + uid + "&msgid="
						+ sportmsgid;
				new Thread(
						new HttpUtil(url, sportReqHandler, KEY_SPORTREQ_JSON))
						.start();

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allSportReqListIsLoad = true;
		}
	}

	private void initSportReqList() {
		getSportReqDataFromJson();
		try {
			allSportReqList.setAdapter(null);
			if (sportReqs.size() < 5) {
				allSportReqList.removeHeaderView();
				allSportReqList.removeFootView();
			} else {
				allSportReqList.addHeaderView();
				allSportReqList.addFootView();
			}
			sportReqAdepter = new SportRequestAdepter(this, sportReqs);
			allSportReqList.setAdapter(sportReqAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFrdReqView() {
		if (allFriendsReqListIsLoad == false) {
			try {
				allFriendsReqList = (RefreshListView) findViewById(R.id.friendReqList);
				allFriendsReqList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// getMallInfo(view);
							}
						});

				allFriendsReqList
						.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									friendsReqJson = result;
									friendsReq.clear();
									getFrdReqDataFromJson();
									allFriendsReqList.setLastRow(false);
									allFriendsReqList.setLoading(false);
									friendsReqAdepter.notifyDataSetChanged();
								} else {
									showSomeThing(result);
								}
								allFriendsReqList.setSelection(1);
							}

							@Override
							public void more() {
								if (!allFriendsReqList.isLastData()) {
									String url = Utils.getFriReqUrl + "&uid="
											+ uid + "&msgid=" + frdreqmsgid;
									allFriendsReqList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								frdreqmsgid = -1;
								String url = Utils.getFriReqUrl + "&uid=" + uid
										+ "&msgid=" + frdreqmsgid;
								allFriendsReqList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									friendsReqJson = result;
									getFrdReqDataFromJson();
									friendsReqAdepter.notifyDataSetChanged();
									allFriendsReqList.setLoading(false);

								} else {
									showSomeThing(result);
								}
								allFriendsReqList.removeFootView();
							}
						});

				initFriendReqList();
				String url = Utils.getFriReqUrl + "&uid=" + uid + "&msgid="
						+ frdreqmsgid;
				new Thread(new HttpUtil(url, friendsReqHandler,
						KEY_FRIENDREQ_JSON)).start();

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allFriendsReqListIsLoad = true;
		}
	}

	private void initFriendReqList() {
		getFrdReqDataFromJson();
		try {
			allFriendsReqList.setAdapter(null);
			if (friendsReq.size() < 5) {
				allFriendsReqList.removeHeaderView();
				allFriendsReqList.removeFootView();
			} else {
				allFriendsReqList.addHeaderView();
				allFriendsReqList.addFootView();
			}
			friendsReqAdepter = new FrdRequestAdepter(this, friendsReq);
			allFriendsReqList.setAdapter(friendsReqAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFriendMsgView() {
		if (allFriendsListIsLoad == false) {
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.messagePriList);
				friendMsgBtn = (LinearLayout) findViewById(R.id.friendMsgBtn);
				myMsgBtn = (LinearLayout) findViewById(R.id.myMsgBtn);
				reciveMsg = (TextView) findViewById(R.id.reciveMsg);
				sendMsg = (TextView) findViewById(R.id.sendMsg);
				friendMsgBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						reciveMsg.setTextAppearance(getApplicationContext(),
								R.style.fontstyle_white_18);
						sendMsg.setTextAppearance(getApplicationContext(),
								R.style.fontstyle_black_18);
						friendtype = 1;
						loadFrdPriMsg();
					}
				});

				myMsgBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						reciveMsg.setTextAppearance(getApplicationContext(),
								R.style.fontstyle_black_18);
						sendMsg.setTextAppearance(getApplicationContext(),
								R.style.fontstyle_white_18);
						friendtype = 2;
						loadFrdPriMsg();
					}
				});

				allFriendsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// getFriendInfo(view);
							}
						});

				allFriendsList
						.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									friendJson = result;
									friends.clear();
									getFriendDataFromJson();
									allFriendsList.setLastRow(false);
									allFriendsList.setLoading(false);
									friendAdepter.notifyDataSetChanged();
								} else {
									showSomeThing(result);
								}
								allFriendsList.setSelection(1);
							}

							@Override
							public void more() {
								if (!allFriendsList.isLastData()) {
									String url = Utils.getPriMsgUrl + "&type="
											+ friendtype + "&uid=" + uid
											+ "&msgid=" + friendmsgid;
									allFriendsList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								friendmsgid = -1;
								String url = Utils.getPriMsgUrl + "&type="
										+ friendtype + "&uid=" + uid
										+ "&msgid=" + friendmsgid;
								allFriendsList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									friendJson = result;
									getFriendDataFromJson();
									friendAdepter.notifyDataSetChanged();
									allFriendsList.setLoading(false);
								} else {
									showSomeThing(result);
								}
								allFriendsList.removeFootView();
							}
						});

				initFriendList();
				loadFrdPriMsg();

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allFriendsListIsLoad = true;
		}
	}

	private void loadFrdPriMsg() {
		friendmsgid = -1;
		String url = Utils.getPriMsgUrl + "&type=" + friendtype + "&uid=" + uid
				+ "&msgid=" + friendmsgid;
		new Thread(new HttpUtil(url, friendHandler, KEY_FRIEND_JSON)).start();
	}

	private void initFriendList() {
		getFriendDataFromJson();
		try {
			allFriendsList.setAdapter(null);
			/**/
			if (friends.size() < 5) {
				allFriendsList.removeHeaderView();
				allFriendsList.removeFootView();
			} else {
				allFriendsList.addHeaderView();
				allFriendsList.addFootView();
			}
			friendAdepter = new UserPrvmsgAdepter(this, friends, parent);
			allFriendsList.setAdapter(friendAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}

	}

	private void initMallView() {
		if (allMallsListIsLoad == false) {
			try {
				allMallsList = (RefreshListView) findViewById(R.id.messageSysList);
				allMallsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getMallInfo(view);
							}
						});

				allMallsList
						.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									mallJson = result;
									malls.clear();
									getMallDataFromJson();
									allMallsList.setLastRow(false);
									allMallsList.setLoading(false);
									mallAdapter.notifyDataSetChanged();
								} else {
									showSomeThing(result);
								}
								allMallsList.setSelection(1);
							}

							@Override
							public void more() {
								if (!allMallsList.isLastData()) {
									String url = Utils.getSysMsgUrl + "&uid="
											+ uid + "&msgid=" + noticemsgid;
									allMallsList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								noticemsgid = -1;
								String url = Utils.getSysMsgUrl + "&uid=" + uid
										+ "&msgid=" + noticemsgid;
								allMallsList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0
										&& !result.equals("服务访问失败")) {
									mallJson = result;
									getMallDataFromJson();
									mallAdapter.notifyDataSetChanged();
									allMallsList.setLoading(false);

								} else {
									showSomeThing(result);
								}
								allMallsList.removeFootView();
							}
						});

				initMallList();
				String url = Utils.getSysMsgUrl + "&uid=" + uid + "&msgid="
						+ noticemsgid;
				new Thread(new HttpUtil(url, mallHandler, KEY_MALL_JSON))
						.start();

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allMallsListIsLoad = true;
		}
	}

	private void initMallList() {
		getMallDataFromJson();
		try {
			allMallsList.setAdapter(null);
			if (malls.size() < 5) {
				allMallsList.removeHeaderView();
				allMallsList.removeFootView();
			} else {
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

	public void getFriendInfo(View v) {
		try {
			Intent intent = new Intent(MessageCenter.this, FriendInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

	}

	public void getMallInfo(View v) { // 小黑 对话界面
		try {
			// 系统通知 sysmsginfo.xml
			// 好友添加 friendapplyinfo.xml
			// 活动邀请 sportapplyinfo.xml
			showSomeThing("已读");
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	private void initView() {
		parent = (LinearLayout) findViewById(R.id.parentView);
		friendsMsgText = (TextView) findViewById(R.id.friendsMsgText);
		sysMsgText = (TextView) findViewById(R.id.sysMsgText);
		sysMsg = (LinearLayout) findViewById(R.id.sysMsg);
		friendsMsg = (LinearLayout) findViewById(R.id.friendsMsg);
		friendReqText = (TextView) findViewById(R.id.friendReqText);
		sportReqText = (TextView) findViewById(R.id.sportReqText);
		friendReq = (LinearLayout) findViewById(R.id.friendReq);
		sportReq = (LinearLayout) findViewById(R.id.sportReq);
		messagePager = (ViewPager) findViewById(R.id.messagePager);

		list.add(sysMsgText);
		list.add(friendsMsgText);
		list.add(friendReqText);
		list.add(sportReqText);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sysMsg:
			initMallView();
			break;
		case R.id.friendsMsg:
			initFriendMsgView();
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

	public void setFontStyle(int index) {
		for (int i = 0; i < list.size(); i++) {
			TextView view = list.get(i);
			if (index == i) {
				view.setTextAppearance(getApplicationContext(),
						R.style.fontstyle_red_16);
			} else {
				view.setTextAppearance(getApplicationContext(),
						R.style.fontstyle_16);
			}
		}
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
			setFontStyle(index);
		}
	};
}
