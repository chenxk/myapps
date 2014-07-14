package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.UserBean;
import com.yuguan.bean.user.UserOrgActionBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class PingFenResultActivity extends Activity {

	private TextView pingfenName;
	private LinearLayout submitScore;
	private LinearLayout cancleScore;
	private RefreshListView allFrdScoreList;
	private TextView guanjun;
	private TextView yajun;
	private TextView jijun;
	private UserOrgActionBean bean;
	private PingFenResultActivity instance;
	private JSONObject resjson = new JSONObject();
	// 记录打过分的用户
	private List<JSONObject> scoredUsers = new ArrayList<JSONObject>();
	
	private String userInfoJson = InitValue.actionInfoJson;
	private final String KEY_USERINFO_JSON = "KEY_USERINFO_JSON";
	private UserScoreAdepter adepter;
	private List<UserBean> users = new ArrayList<UserBean>();
	@SuppressLint("HandlerLeak")
	private Handler userHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_USERINFO_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				userInfoJson = result;
				users.clear();
				getUserInfoFromJson();
				setCommentListHeight();
				adepter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//init();
	}

	public void init() {
		setContentView(R.layout.pingfenres);
		
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		bean = (UserOrgActionBean) this.getIntent().getSerializableExtra(
				"orgActionBean");
		try {
			resjson.put("aid", bean.getAid());
			resjson.put("guan", 0);
			resjson.put("ya", 0);
			resjson.put("ji", 0);
			resjson.put("nodes", new JSONArray());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();
		initFriendScoreView();
	}

	public void initView() {
		pingfenName = (TextView) findViewById(R.id.pingfenresName);
		allFrdScoreList = (RefreshListView) findViewById(R.id.allFrdScoreList);
		guanjun = (TextView) findViewById(R.id.guanjun);
		yajun = (TextView) findViewById(R.id.yajun);
		jijun = (TextView) findViewById(R.id.jijun);
		submitScore = (LinearLayout) findViewById(R.id.submitResScore);
		cancleScore = (LinearLayout) findViewById(R.id.cancleResScore);

		submitScore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doSubmit();
			}
		});
		
		
		cancleScore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				instance.finish();
			}
		});
		if (bean != null) {
			pingfenName.setText(bean.getAname());
		}
	}

	

	public void getUserInfoFromJson() {
		try {
			JSONObject mallJson = new JSONObject(userInfoJson);
			if (mallJson != null) {
				JSONArray users = mallJson.getJSONArray("users");
				if (users != null && users.length() > 0) {
					int count = users.length();
					for (int i = 0; i < count; i++) {
						JSONObject json = (JSONObject) users.get(i);
						UserBean bean = UserBean.getBeanFromJson(json);
						if (bean != null) {
							this.users.add(bean);
						} else {
							showSomeThing("user is null");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showSomeThing(e.toString());
		}
	}

	private void initFriendScoreView() {
		try {

			allFrdScoreList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							doClickInfo(view);
						}
					});

			allFrdScoreList
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
							if (result.length() > 0 && !result.equals("服务访问失败")) {
								userInfoJson = result;
								users.clear();
								getUserInfoFromJson();
								allFrdScoreList.setLastRow(false);
								allFrdScoreList.setLoading(false);
								adepter.notifyDataSetChanged();
							} else {
								showSomeThing(result);
							}
							allFrdScoreList.setSelection(1);
						}

						@Override
						public void more() {
							if (!allFrdScoreList.isLastData()) {
								String url = Utils.getBaoMingUsersUrl
										+ bean.getAid();
								allFrdScoreList.setUrl(url);
							} else {
								showSomeThing("加载到最后一条了");
							}
						}

						@Override
						public void setUrl() {
							String url = Utils.getBaoMingUsersUrl
									+ bean.getAid();
							allFrdScoreList.setUrl(url);
						}

						@Override
						public void loaded(Object obj) {
							if (obj == null) {
								return;
							}

							String result = obj.toString();
							if (result.length() > 0 && !result.equals("服务访问失败")) {
								userInfoJson = result;
								users.clear();
								getUserInfoFromJson();
								adepter.notifyDataSetChanged();
								allFrdScoreList.setLoading(false);
							} else {
								showSomeThing(result);
							}
							allFrdScoreList.removeFootView();
						}
					});

			initFriendList();

			new Thread(new HttpUtil(Utils.getBaoMingUsersUrl + bean.getAid(),
					userHandler, KEY_USERINFO_JSON)).start();

		} catch (Exception e) {
			e.printStackTrace();
			showSomeThing(e.toString());
		}

	}

	protected void doClickInfo(View view) {

	}
	
	

	private void initFriendList() {
		getUserInfoFromJson();
		try {
			allFrdScoreList.setAdapter(null);
			/**/
			if (users.size() < 5) {
				allFrdScoreList.removeHeaderView();
				allFrdScoreList.removeFootView();
			} else {
				allFrdScoreList.addHeaderView();
				allFrdScoreList.addFootView();
			}
			adepter = new UserScoreAdepter(this, users);
			adepter.setSelectUser(new UserScoreAdepter.SelectUser() {

				@Override
				public void select(UserBean bean, int jb, int score) {
					// TODO Auto-generated method stub
					if (score == 0) {
						showSomeThing("你忘记给" + bean.getName() + "打分了哦..");
						return;
					}
					
					String guan = guanjun.getText().toString().trim();
					String ya = yajun.getText().toString().trim();
					String ji = jijun.getText().toString().trim();
					String name = bean.getName();
					if (jb == 1) {
						guanjun.setText(name);
						addJson("guan", bean.getUid());
						
						if(ya.equals(name)){
							yajun.setText("未选择");
							addJson("ya", 0);
						}
						if(ji.equals(name)){
							jijun.setText("未选择");
							addJson("ji", 0);
						}
						
					} else if (jb == 2) {
						yajun.setText(name);
						addJson("ya", bean.getUid());
						
						if(guan.equals(name)){
							guanjun.setText("未选择");
							addJson("guan", 0);
						}
						if(ji.equals(name)){
							jijun.setText("未选择");
							addJson("ji", 0);
						}
					} else if(jb == 3){
						jijun.setText(name);
						addJson("ji", bean.getUid());
						
						if(guan.equals(name)){
							guanjun.setText("未选择");
							addJson("guan", 0);
						}
						
						if(ya.equals(name)){
							yajun.setText("未选择");
							addJson("ya", 0);
						}
					}
					try {
						addScoredUser(bean, score);
					} catch (Exception e) {
						e.printStackTrace();
						showSomeThing(e.toString());
					}
					
				}
			});
			allFrdScoreList.setAdapter(adepter);
			setCommentListHeight();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			showSomeThing(e.toString());
		}

	}
	
	
	private void addJson(String key,int value){
		try {
			resjson.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			showSomeThing(e.toString());
		}
		
	}
	
	private void  addScoredUser(UserBean bean,int score) throws Exception{
		int size = scoredUsers.size();
		int count = 0;
		for(JSONObject bn : scoredUsers){
			if(bn.getInt("uid") == bean.getUid()){
				bn.put("score", score);
				break;
			}else{
				 count++;
			}
		}
		if(count == size){
			JSONObject json = new JSONObject();
			json.put("uid", bean.getUid());
			json.put("score", score);
			scoredUsers.add(json);
		}
	}
	
	protected void doSubmit() {
		
		int guanid = 0;
		try {
			guanid = resjson.getInt("guan");
		} catch (JSONException e1) {
			e1.printStackTrace();
			showSomeThing("你忘记选择冠军了哦!");
			return;
		}
		if(guanid == 0){
			showSomeThing("你忘记选择冠军了哦!");
			return;
		}
		
		showSomeThing(users.size() + " " + scoredUsers.size());
		
		if(users.size() != scoredUsers.size()){
			showSomeThing("还有用户没有评分哦~");
			return;
		}
		
		JSONArray array = new JSONArray(scoredUsers);
		
		try {
			resjson.put("nodes", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		showSomeThing(resjson.toString());
		
		
		if(true){
			return;
		}
		
		String url = Utils.putorgreviewUrl + resjson.toString();
		new Thread(new HttpUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("GETACCOUNTINFO");
				if (result != null && !"服务访问失败".equals(result)) {
					showSomeThing("分手提交成功!");
					instance.finish();
				}else{
					showSomeThing("服务访问失败");
				}
			}
		}, "GETACCOUNTINFO")).start();
		
	}

	public void setCommentListHeight() {
		ViewGroup.LayoutParams params = allFrdScoreList.getLayoutParams();
		int totalHeight = 0;
		for (int i = 0; i < adepter.getCount(); i++) {
			View listItem = adepter.getView(i, null, allFrdScoreList);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		params.height = totalHeight
				+ (allFrdScoreList.getDividerHeight() * (adepter.getCount() - 1));
		allFrdScoreList.setLayoutParams(params);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	public void doPingfenResBack(View v) {
		this.finish();
	}

	public void doPingfenResLogin(View v) {
	}
}
