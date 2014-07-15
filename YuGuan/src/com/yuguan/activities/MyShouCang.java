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
import android.util.Log;
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

import com.yuguan.bean.user.ActionCollectBean;
import com.yuguan.bean.user.MallCollectBean;
import com.yuguan.bean.user.UserActActionBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MyShouCang extends Activity implements OnClickListener {

	private LinearLayout sportcoll;
	private TextView sportcollText;
	private TextView mallcollText;
	private LinearLayout mallcoll;
	private ViewPager messagePager;
	
	private RefreshListView sportcollList;
	private SportCollectAdepter sportcollAdapter;
	private List<ActionCollectBean> sports = new ArrayList<ActionCollectBean>();
	private boolean allSportcollListIsLoad = false;
	private int aid = -1;
	private int curSportcollPage = 1;
	private int curSportcollSr = 0;
	private int curSportcollRg = 0;
	
	private RefreshListView mallcollList;
	private MallCollectAdepter mallcollAdepter;
	private List<MallCollectBean> malls = new ArrayList<MallCollectBean>();
	private boolean allMallcollListIsLoad = false;
	private int mallid = -1;
	private int curMallcollPage = 1;
	private int curMallcollSr = 0;
	private int curMallcollRg = 0;
	
	private final String KEY_SPORT_JSON = "KEY_SPORT_JSON";
	private final String KEY_MALL_JSON = "KEY_MALL_JSON";
	private String sportcollJson = InitValue.activityJson;
	private String mallcollJson = InitValue.mallJson;
	
	private int uid;

	@SuppressLint("HandlerLeak")
	private Handler mallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_SPORT_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				sportcollJson = result;
				sports.clear();
				getMallDataFromJson();
				sportcollAdapter.notifyDataSetChanged();
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler friendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_MALL_JSON);
			if (result.length() > 0 && !result.equals("服务访问失败")) {
				mallcollJson = result;
				malls.clear();
				getFriendDataFromJson();
				mallcollAdepter.notifyDataSetChanged();
			}
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	/** 
     * Activity被系统杀死后再重建时被调用. 
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity. 
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后. 
     */  
    @Override  
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("onRestoreInstanceState", "onRestoreInstanceState called." + savedInstanceState);  
        init();
    }  
	private void init(){
		setContentView(R.layout.myshoucang);
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

		sportcoll.setOnClickListener(new MyOnClickListener(0));
		mallcoll.setOnClickListener(new MyOnClickListener(1));

		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.shoucang_action, null);
		View view2 = mLi.inflate(R.layout.shoucang_mall, null);
		// View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// views.add(view4);
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
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(mallcollJson);
			JSONArray jsonArray = jsonObject.getJSONArray("malls");
			int count = jsonArray.length();
			if (count >= 10) {
				mallid = ((JSONObject) jsonArray.get(9)).getInt("id");
				mallcollList.setLastData(false);
				mallcollList.addFootView();
			} else {
				mallcollList.setLastData(true);
				mallcollList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				MallCollectBean bean = MallCollectBean.getBeanByJson(json);
				if (bean != null)
					malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void getMallDataFromJson() {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(sportcollJson);
			JSONArray jsonArray = jsonObject.getJSONArray("actions");
			int count = jsonArray.length();
			if (count >= 10) {
				aid = ((JSONObject) jsonArray.get(9)).getInt("id");
				sportcollList.setLastData(false);
				sportcollList.addFootView();
			} else {
				sportcollList.setLastData(true);
				sportcollList.removeFootView();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				ActionCollectBean bean = ActionCollectBean.getBeanByJson(json);
				if (bean != null)
					sports.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void initMallView() {
		if (allSportcollListIsLoad == false) {
			try {
				sportcollList = (RefreshListView) findViewById(R.id.messageSysList);
				sportcollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getMallInfo(view);
							}
						});
				
				sportcollList.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
							sportcollJson = result;
							sports.clear();
							getMallDataFromJson();
							sportcollList.setLastRow(false);
							sportcollList.setLoading(false);
							sportcollAdapter.notifyDataSetChanged();
						}else{
							showSomeThing(result);
						}
						sportcollList.setSelection(1);
					}

					@Override
					public void more() {
						if(!sportcollList.isLastData()){
							++ curSportcollPage;
							String url = Utils.mallListUrl + curSportcollPage + "&rg=" + curSportcollRg + "&sr=" + curSportcollSr;
							sportcollList.setUrl(url);
						}else{
							showSomeThing("加载到最后一条了");
						}
					}

					@Override
					public void setUrl() {
						curSportcollPage = 1;
						String url = Utils.mallListUrl + curSportcollPage + "&rg=" + curSportcollRg + "&sr=" + curSportcollSr;
						sportcollList.setUrl(url);
					}

					@Override
					public void loaded(Object obj) {
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							sportcollJson = result;
							getMallDataFromJson();
							sportcollAdapter.notifyDataSetChanged();
							sportcollList.setLoading(false);
							
						}else{
							showSomeThing(result);
						}
						sportcollList.removeFootView();
					}
				});

				initMallList();
				String url = Utils.mallListUrl + curSportcollPage + "&rg=" + curSportcollRg + "&sr=" + curSportcollSr;
				new Thread(new HttpUtil(url,mallHandler, KEY_SPORT_JSON)).start();
				
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allSportcollListIsLoad = true;
		}
	}
	
	private void initMallList() {
		/**/
		getMallDataFromJson();
		try {
			sportcollList.setAdapter(null);
			/**/
			if(sports.size() < 5){
				sportcollList.removeHeaderView();
				sportcollList.removeFootView();
			}else{
				sportcollList.addHeaderView();
				sportcollList.addFootView();
			}
			sportcollAdapter = new SportCollectAdepter(this, sports);
			sportcollAdapter.setOnClickJoinListener(new SportCollectAdepter.OnClickJoinListener() {
				
				@Override
				public void onClickJoinListener(int aid) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyShouCang.this, ChatActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("actionId", aid);
					intent.putExtras(bundle);
					startActivity(intent);
				}

				@Override
				public void onClickCancelListener(int aid) {
					// TODO Auto-generated method stub
					
				}
			});
			sportcollList.setAdapter(sportcollAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFriendView() {
		if (allMallcollListIsLoad == false) {
			try {
				mallcollList = (RefreshListView) findViewById(R.id.messagePriList);
				mallcollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getFriendInfo(view);
							}
						});
				
				mallcollList.setOnRefreshListener(new RefreshListView.RefreshListener() {

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
							mallcollJson = result;
							malls.clear();
							getFriendDataFromJson();
							mallcollList.setLastRow(false);
							mallcollList.setLoading(false);
							mallcollAdepter.notifyDataSetChanged();
						}else{
							showSomeThing(result);
						}
						mallcollList.setSelection(1);
					}

					@Override
					public void more() {
						if(!mallcollList.isLastData()){
							++ curMallcollPage;
							String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curMallcollPage;
							mallcollList.setUrl(url);
						}else{
							showSomeThing("加载到最后一条了");
						}
					}

					@Override
					public void setUrl() {
						curMallcollPage = 1;
						String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curMallcollPage;
						mallcollList.setUrl(url);
					}

					@Override
					public void loaded(Object obj) {
						if (obj == null) {
							return;
						}

						String result = obj.toString();
						if (result.length() > 0 && !result.equals("服务访问失败")) {
							mallcollJson = result;
							getFriendDataFromJson();
							mallcollAdepter.notifyDataSetChanged();
							mallcollList.setLoading(false);
							
						}else{
							showSomeThing(result);
						}
						mallcollList.removeFootView();
					}
				});

				initFriendList();
				String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curMallcollPage;
				new Thread(new HttpUtil(url,friendHandler, KEY_MALL_JSON)).start();
				
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allMallcollListIsLoad = true;
		}
	}

	private void initFriendList() {
		getFriendDataFromJson();
		try {
			mallcollList.setAdapter(null);
			/**/
			if(malls.size() < 5){
				mallcollList.removeHeaderView();
				mallcollList.removeFootView();
			}else{
				mallcollList.addHeaderView();
				mallcollList.addFootView();
			}
			mallcollAdepter = new MallCollectAdepter(this, malls);
			mallcollAdepter.setOnClickJoinListener(new MallCollectAdepter.OnClickJoinListener() {
				
				@Override
				public void onClickJoinListener(int mallId) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyShouCang.this, MallInfo.class);
					Bundle bundle = new Bundle();
					bundle.putInt("mallId", mallId);
					intent.putExtras(bundle);
					startActivity(intent);
				}

				@Override
				public void onClickCancelListener(int mallId) {
					// TODO Auto-generated method stub
					
				}
			});
			mallcollList.setAdapter(mallcollAdepter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
		
	}
	
	
	public void getFriendInfo(View v) {
		try {
			Intent intent = new Intent(MyShouCang.this, FriendInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

	}
	
	public void getMallInfo(View v) { // 
		try {
			TextView idView = (TextView)v.findViewById(R.id.mallId);
			int id = Integer.parseInt(idView.getText().toString());
			Intent intent = new Intent(MyShouCang.this, MallInfo.class);
			Bundle bundle = new Bundle();
			bundle.putInt("mallId", id);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}
	
	private void initView() {
		mallcollText = (TextView) findViewById(R.id.myaction_inText);
		sportcollText = (TextView) findViewById(R.id.myaction_outText);
		sportcoll = (LinearLayout) findViewById(R.id.myaction_in);
		mallcoll = (LinearLayout) findViewById(R.id.myaction_out);
		messagePager = (ViewPager) findViewById(R.id.myactionPager);

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
		Intent intent = new Intent(MyShouCang.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
	
	public void setFontStyle(int index) {
		if (index == 0) {
			sportcollText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_red_20);
			mallcollText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_20);
		} else {
			sportcollText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_20);
			mallcollText.setTextAppearance(getApplicationContext(),
					R.style.fontstyle_red_20);
		}
	}

	/**
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
