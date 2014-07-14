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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

import com.yuguan.bean.FriendBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MyFriends extends Activity implements OnClickListener {

	/** 所有好友 */
	private RefreshListView allFriendsList;
	private UserAdepter friendAdepter;
	private List<FriendBean> friends = new ArrayList<FriendBean>();
	private boolean allFriendsListIsLoad = false;
	private int curFriendPage = 1;
	private String friendJson = InitValue.friendJson;
	private String KEY_FRIEND_JSON = "KEY_FRIEND_JSON";

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
		setContentView(R.layout.myfriends);
		initFriendView();
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
				if (bean != null)
					friends.add(bean);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initFriendView() {
		if (allFriendsListIsLoad == false) {
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.myfrdsListView);
				allFriendsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getFriendInfo(view);
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
									++curFriendPage;
									String url = Utils.friendsUrl + "&cid="
											+ Utils.cid + "&pn="
											+ curFriendPage;
									allFriendsList.setUrl(url);
								} else {
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								curFriendPage = 1;
								String url = Utils.friendsUrl + "&cid="
										+ Utils.cid + "&pn=" + curFriendPage;
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
				String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn="
						+ curFriendPage;
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
			friendAdepter = new UserAdepter(this, friends);
			allFriendsList.setAdapter(friendAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}

	}

	public void getFriendInfo(View v) {
		try {
			TextView frd = (TextView) v.findViewById(R.id.friendId);
			int id = Integer.parseInt(frd.getText().toString());
			Intent intent = new Intent(MyFriends.this, FriendInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		Intent intent = new Intent(MyFriends.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
}
