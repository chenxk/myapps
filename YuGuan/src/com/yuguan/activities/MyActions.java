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
import cn.buaa.myweixin.ChatActivity;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.UserActActionBean;
import com.yuguan.bean.user.UserOrgActionBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.SuperViewPager;
import com.yuguan.util.Utils;

public class MyActions extends Activity implements OnClickListener {

	private LinearLayout sysMsg;
	private TextView sysMsgText;
	private TextView friendsMsgText;
	private LinearLayout friendsMsg;
	private SuperViewPager messagePager;

	/** 所有场馆 */
	private RefreshListView allMallsList;
	private MyActivityAdapter mallAdapter;
	private List<UserActActionBean> malls = new ArrayList<UserActActionBean>();
	private boolean allMallsListIsLoad = false;
	private int actaid = -1;
	private int acttype = 0;
	/** 所有好友 */
	private RefreshListView allFriendsList;
	private MyActivityAdapter friendAdepter;
	private List<UserOrgActionBean> friends = new ArrayList<UserOrgActionBean>();
	private boolean allFriendsListIsLoad = false;
	private int orgaid = -1;
	private int orgtype = 0;

	private final String KEY_MALL_JSON = "KEY_MALL_JSON";
	private final String KEY_FRIEND_JSON = "KEY_FRIEND_JSON";
	private String mallJson = InitValue.myjoinactions;
	private String friendJson = InitValue.myorgactions;

	private int uid;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.myactions);
		uid = this.getIntent().getExtras().getInt("uid");
		initView();

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
		View view1 = mLi.inflate(R.layout.myaction_in, null);
		View view2 = mLi.inflate(R.layout.myaction_out, null);
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
			JSONArray jsonArray = jsonObject.getJSONArray("acts");
			int count = jsonArray.length();
			if (count >= 10) {
				orgaid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allFriendsList.setLastData(false);
				allFriendsList.addFootView();
			} else {
				allFriendsList.setLastData(true);
				allFriendsList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				UserOrgActionBean bean = UserOrgActionBean.getBeanByJson(json);
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
			JSONArray jsonArray = jsonObject.getJSONArray("acts");
			int count = jsonArray.length();
			if (count >= 10) {
				actaid = ((JSONObject) jsonArray.get(9)).getInt("id");
				allMallsList.setLastData(false);
				allMallsList.addFootView();
			} else {
				allMallsList.setLastData(true);
				allMallsList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				UserActActionBean bean = UserActActionBean.getBeanByJson(json);
				if (bean != null)
					malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initMallView() {
		if (allMallsListIsLoad == false) {
			try {
				allMallsList = (RefreshListView) findViewById(R.id.actionInList);
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
									String url = Utils.myJoinActionsUrl
											+ "&uid=" + uid + "&type="
											+ acttype + "&aid=" + actaid;
									allMallsList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								actaid = -1;
								String url = Utils.myJoinActionsUrl + "&uid="
										+ uid + "&type=" + acttype + "&aid="
										+ actaid;
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
				String url = Utils.myJoinActionsUrl + "&uid=" + uid + "&type="
						+ acttype + "&aid=" + actaid;
				new Thread(new HttpUtil(url, mallHandler, KEY_MALL_JSON))
						.start();

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
			if (malls.size() < 5) {
				allMallsList.removeHeaderView();
				allMallsList.removeFootView();
			} else {
				allMallsList.addHeaderView();
				allMallsList.addFootView();
			}
			mallAdapter = new MyActivityAdapter(this, malls);
			allMallsList.setAdapter(mallAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFriendView() {
		if (allFriendsListIsLoad == false) {
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.actionOutList);
				allFriendsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getMallInfo(view);
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
									String url = Utils.myOrgActionsUrl
											+ "&uid=" + uid + "&type="
											+ orgtype + "&aid=" + orgaid;
									allFriendsList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								orgaid = -1;
								String url = Utils.myOrgActionsUrl + "&uid="
										+ uid + "&type=" + orgtype + "&aid="
										+ orgaid;
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
				String url = Utils.myOrgActionsUrl + "&uid=" + uid + "&type="
						+ orgtype + "&aid=" + orgaid;
				new Thread(new HttpUtil(url, friendHandler, KEY_FRIEND_JSON))
						.start();

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
			if (friends.size() < 5) {
				allFriendsList.removeHeaderView();
				allFriendsList.removeFootView();
			} else {
				allFriendsList.addHeaderView();
				allFriendsList.addFootView();
			}
			friendAdepter = new MyActivityAdapter(this, friends, 0);
			allFriendsList.setAdapter(friendAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}

	}

	public void getFriendInfo(View v) {
		try {
			Intent intent = new Intent(MyActions.this, FriendInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	public void getMallInfo(View v) { // 小黑 对话界面
		try {
			// 系统通知 sysmsginfo.xml
			// 好友添加 friendapplyinfo.xml
			// 活动邀请 sportapplyinfo.xml
			TextView idView = (TextView) v.findViewById(R.id.myactionId);
			int id = Integer.parseInt(idView.getText().toString());
			Intent intent = new Intent(MyActions.this, ChatActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("actionId", id);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	private void initView() {
		sysMsgText = (TextView) findViewById(R.id.myaction_inText);
		friendsMsgText = (TextView) findViewById(R.id.myaction_outText);
		sysMsg = (LinearLayout) findViewById(R.id.myaction_in);
		friendsMsg = (LinearLayout) findViewById(R.id.myaction_out);
		messagePager = (SuperViewPager) findViewById(R.id.myactionPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myaction_in:
			initMallView();
			break;
		case R.id.myaction_out:
			initFriendView();
			break;
		case R.id.myaction_back:
			doBack(v);
			break;
		case R.id.myaction_login:
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
		Intent intent = new Intent(MyActions.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	public void setFontStyle(int index) {
		if (index == 0) {
			sysMsgText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_red_20);
			friendsMsgText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_20);
		} else {
			sysMsgText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_20);
			friendsMsgText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_red_20);
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
