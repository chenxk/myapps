package cn.buaa.myweixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yuguan.activities.ActivityAdapter;
import com.yuguan.activities.FriendInfo;
import com.yuguan.activities.MallAdepter;
import com.yuguan.activities.MallInfo;
import com.yuguan.activities.RefreshListView;
import com.yuguan.activities.UserAdepter;
import com.yuguan.bean.AccountInfo;
import com.yuguan.bean.ActionBean;
import com.yuguan.bean.CountyBean;
import com.yuguan.bean.FriendBean;
import com.yuguan.bean.MallBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.InitValue;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.Utils;

public class MainWeixin extends Activity {

	public static MainWeixin instance = null;

	private ViewPager mTabPager;
	// private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1, mTab2, mTab3;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;// ����ˮƽ����λ��
	private int two;
	private int three;
	// private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private View layout;
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	// private Button mRightBtn;
	private RoundImageView userImg;
	private LinearLayout mainTitleBar;

	/** ʡ�����б� */
	// private ArrayList<String> provinces = new ArrayList<String>();
	private int cityId = 173;

	/** ��Ļ��� */
	private int mScreenWidth = 0;
	/** Item��� */
	// private int mItemWidth = 0;
	private HorizontalScrollView mColumnHorizontalScrollView;

	/** ���������б� */
	private ArrayList<CountyBean> counties = new ArrayList<CountyBean>();
	// private boolean isCountyLoading = false;
	//public static ImageCacheManager imageCacheManager;

	/** ���л */
	private RefreshListView allActivitiesList;
	private ActivityAdapter activityAdapter;
	private List<ActionBean> actions = new ArrayList<ActionBean>();
	private boolean allActivitiesListIsLoad = false;
	private LinearLayout activityRadioGroup_content;
	private int activityColumnSelectIndex = 0;
	private int curActivityPage = 1;
	// ����ʽ
	private int curActivitySr = 0;
	// ����ID
	private int curActivityRg = 0;

	/** ���г��� */
	private RefreshListView allMallsList;
	private MallAdepter mallAdapter;
	private List<MallBean> malls = new ArrayList<MallBean>();
	private boolean allMallsListIsLoad = false;
	private LinearLayout mall_mRadioGroup_content;
	private int mallColumnSelectIndex = 0;
	private int curMallPage = 1;
	// ����ʽ
	private int curMallSr = 0;
	// ����ID
	private int curMallRg = 0;
	
	/** ���к��� */
	private RefreshListView allFriendsList;
	private UserAdepter friendAdepter;
	private List<FriendBean> friends = new ArrayList<FriendBean>();
	private boolean allFriendsListIsLoad = false;
	private LinearLayout friend_mRadioGroup_content;
	private int friendColumnSelectIndex = 0;
	private int curFriendPage = 1;
	// ����ʽ
	private int curFriendSr = 0;
	// ����ID
	private int curFriendRg = 0;
	
	private final String KEY_COUNTY_JSON = "KEY_COUNTY_JSON";
	private final String KEY_ACTIVITY_JSON = "KEY_ACTIVITY_JSON";
	private final String KEY_MALL_JSON = "KEY_MALL_JSON";
	private final String KEY_FRIEND_JSON = "KEY_FRIEND_JSON";
	private String countyJson = InitValue.countyJson;
	private String activityJson = InitValue.activityJson;
	private String mallJson = InitValue.mallJson;
	private String friendJson = InitValue.friendJson;

	@SuppressLint("HandlerLeak")
	private Handler countyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_COUNTY_JSON);
			if (result != null && !"�������ʧ��".equals(result)) {
				countyJson = result;
				initTabColumn(currIndex);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler activityHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_ACTIVITY_JSON);
			if (result.length() > 0 && !result.equals("�������ʧ��")) {
				activityJson = result;
				actions.clear();
				getActionDataFromJson();
				activityAdapter.notifyDataSetChanged();
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler mallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_MALL_JSON);
			if (result.length() > 0 && !result.equals("�������ʧ��")) {
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
			if (result.length() > 0 && !result.equals("�������ʧ��")) {
				friendJson = result;
				friends.clear();
				getFriendDataFromJson();
				friendAdepter.notifyDataSetChanged();
			}
		}
	};

	public static boolean netIsGood = true;
	// ����ֻ�����״̬
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				netIsGood = false;
				showSomeThing("�޷���������!");
			} else {
				netIsGood = true;
			}
		}
	};
	

	private ImageLoader mImageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* �̶��洢�ռ��С
		imageCacheManager = ImageCacheManager.getImageCacheService(this,
				ImageCacheManager.MODE_FIXED_MEMORY_USED, "_yuguan");
		imageCacheManager.setMax_Memory(1024 * 1024 * 50);
		 */
		mImageLoader = new ImageLoader(getApplicationContext());
		
		InitValue.preferences = this.getSharedPreferences("USERINFO", Context.MODE_WORLD_READABLE);
		
		if(InitValue.preferences != null){
        	String name = InitValue.preferences.getString("userName", "");
        	String pwd = InitValue.preferences.getString("userPwd", "");
        	boolean autoLog = (InitValue.preferences.getBoolean("autoLogin", true));
        	if(autoLog && !"".equals(name) && !"".equals(pwd)){
        		login(name, pwd);
        	}
        }
		//��ӡ�����Ľ���ֱ��ǣ�
		//data·�� /datasd��·�� /mnt/sdcard��·�� /systemandroid����sp�ļ���·����android��SharedPreferences��·��/data/data/package_name/shared_prefsò��ͨ��Դ�룬����ķ��������޸���ЩĬ��·��ͨ��context.openFileInput����fileName������content.openFileOutput(��fileName��,Activity.MODE_PRIVATE)Ĭ�ϵ��ļ�·��Ϊ/data/data/��.��/files
		try {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			this.registerReceiver(connectionReceiver, intentFilter);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
		setContentView(R.layout.main_weixin);
		// ����activityʱ���Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;

		userImg = (RoundImageView) findViewById(R.id.right_btn);
		mainTitleBar = (LinearLayout) findViewById(R.id.main_title_bar);
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mTab1 = (ImageView) findViewById(R.id.img_weixin);
		mTab2 = (ImageView) findViewById(R.id.img_address);
		mTab3 = (ImageView) findViewById(R.id.img_friends);
		// mTab4 = (ImageView) findViewById(R.id.img_settings);
		// mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		// mTab4.setOnClickListener(new MyOnClickListener(3));
		Display currDisplay = getWindowManager().getDefaultDisplay();// ��ȡ��Ļ��ǰ�ֱ���
		int displayWidth = currDisplay.getWidth();
		// int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // ����ˮƽ����ƽ�ƴ�С
		two = one * 2;
		three = one * 3;
		// Log.i("info", "��ȡ����Ļ�ֱ���Ϊ" + one + two + three + "X" + displayHeight);

		mScreenWidth = displayWidth;

		// InitImageView();//ʹ�ö���
		// ��Ҫ��ҳ��ʾ��Viewװ��������
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.main_tab_weixin, null);
		View view2 = mLi.inflate(R.layout.main_tab_address, null);
		View view3 = mLi.inflate(R.layout.main_tab_friends, null);
		// View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		// ÿ��ҳ���view����
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		// views.add(view4);
		// ���ViewPager������������
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
						initActionView();
					}
				} catch (Exception e) {
					showSomeThing(e.toString() + " WWWW");
				}
				

				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);

	}

	public void login(String name,String pwd){
    	String url = Utils.loginUrl + name + "&password=" + pwd;
		 new Thread(new HttpUtil(url,
					new Handler(){
			 @Override
			public void handleMessage(Message msg) {
				 super.handleMessage(msg);
					Bundle data = msg.getData();
					String result = data.getString("LOGINAJAX");
					if (result != null && !"�������ʧ��".equals(result)) {
						try {
							JSONObject json = new JSONObject(result);
							String message = null;
							try {
								message = json.getString("message");
							} catch (Exception e) {
							}
							// {"message":null,"password":"Lehmann",
							//"user":{"email":"yongzhong15@126.com","id":28,"name":"Johnney",
							//"password":"ABEFCB49241976E2F7A4A88030CD3255","status":2,"vip":0},"username":"Johnney"}
							if(message == null || message.equals("null")){
								Utils.loginInfo = AccountInfo.getAccountInfo(json);
								//Utils.getAccountInfo(Utils.loginInfo.getId());
								getAccountInfo(Utils.loginInfo.getId());
								showSomeThing("��½�ɹ�,��ӭ "+ json.getString("username"));
							}else{
								new AlertDialog.Builder(MainWeixin.this)
								.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
								.setTitle("��¼����")
								.setMessage(message)
								.create().show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						new AlertDialog.Builder(MainWeixin.this)
						.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
						.setTitle("��¼ʧ��")
						.setMessage("�������ʧ�ܣ�")
						.create().show();
					}
			}
		 }, "LOGINAJAX")).start();
    }
	
	public void getAccountInfo(int id){
    	String url = Utils.getAccountInfoUrl + id;
    	new Thread(new HttpUtil(url, new Handler(){
			 @Override
			public void handleMessage(Message msg) {
				 super.handleMessage(msg);
					Bundle data = msg.getData();
					String result = data.getString("GETACCOUNTINFO");
					if (result != null && !"�������ʧ��".equals(result)) {
						try {
							JSONObject json = new JSONObject(result);
							JSONObject message  = json.getJSONObject("user");
							if(message != null){
								Utils.self = FriendBean.getBeanFromJson(message);
								String url = Utils.userImg + Utils.self.getPic();
								userImg.setTag(url);
								mImageLoader.loadImage(url, null, userImg);
								Utils.selfPic = mImageLoader.getImageFromUrl(url);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
			}
		 }, "GETACCOUNTINFO")).start();
    }
    
	
	private void initActionView() {
		if (allActivitiesListIsLoad == false) {
			activityRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
			initTabColumn(0);
			new Thread(new HttpUtil(Utils.countyUrl + cityId,countyHandler, KEY_COUNTY_JSON)).start();
			
			try {
				allActivitiesList = (RefreshListView) findViewById(R.id.allActivitiesLists);
				allActivitiesList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								
								getActionInfo(view);
							}

						});

				allActivitiesList
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
								if (result.length() > 0 && !result.equals("�������ʧ��")) {
									activityJson = result;
									actions.clear();
									getActionDataFromJson();
									allActivitiesList.setLastRow(false);
									allActivitiesList.setLoading(false);
									activityAdapter.notifyDataSetChanged();
								}else{
									showSomeThing(result);
								}
								allActivitiesList.setSelection(1);
							}

							@Override
							public void more() {
								if(!allActivitiesList.isLastData()){
									++ curActivityPage;
									String url = Utils.activityUrl + curActivityPage + "&rg=" + curActivityRg + "&sr=" + curActivitySr;
									allActivitiesList.setUrl(url);
								}else{
									showSomeThing("���ص����һ����");
								}
							}

							@Override
							public void setUrl() {
								curActivityPage = 1;
								String url = Utils.activityUrl + curActivityPage + "&rg=" + curActivityRg + "&sr=" + curActivitySr;
								allActivitiesList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0 && !result.equals("�������ʧ��")) {
									activityJson = result;
									getActionDataFromJson();
									activityAdapter.notifyDataSetChanged();
									allActivitiesList.setLoading(false);
								}else{
									showSomeThing(result);
								}
								allActivitiesList.removeFootView();
							}
						});

				initActivitiesList();
				
				new Thread(new HttpUtil(Utils.activityUrl + curActivityPage + "&rg=" + curActivityRg + "&sr=" + curActivitySr,
						activityHandler, KEY_ACTIVITY_JSON)).start();

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allActivitiesListIsLoad = true;
		}
	}

	private void initMallView() {
		if (allMallsListIsLoad == false) {
			mall_mRadioGroup_content = (LinearLayout) findViewById(R.id.mall_mRadioGroup_content);
			initTabColumn(1);
			new Thread(new HttpUtil(Utils.countyUrl + cityId,countyHandler, KEY_COUNTY_JSON)).start();
			try {
				allMallsList = (RefreshListView) findViewById(R.id.allMallsLists);
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
						if (result.length() > 0 && !result.equals("�������ʧ��")) {
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
							showSomeThing("���ص����һ����");
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
						if (result.length() > 0 && !result.equals("�������ʧ��")) {
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

	private void initFriendView() {
		if (allFriendsListIsLoad == false) {
			//friend_mRadioGroup_content = (LinearLayout) findViewById(R.id.friend_mRadioGroup_content);
			//initTabColumn(2);
			//new Thread(new HttpUtil(Utils.countyUrl + cityId,countyHandler, KEY_COUNTY_JSON)).start();
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.allfriendsLists);
				allFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getFriendInfo(view,position);
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
						if (result.length() > 0 && !result.equals("�������ʧ��")) {
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
							showSomeThing("���ص����һ����");
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
						if (result.length() > 0 && !result.equals("�������ʧ��")) {
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

	public void getCityFromJson() {

		counties.clear();
		counties.add(new CountyBean(0, null, "ȫ��", 0));
		try {
			JSONObject jsonObject = new JSONObject(countyJson);
			Utils.cid = jsonObject.getInt("cid");
			JSONArray jsonArray = jsonObject.getJSONArray("counties");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				CountyBean bean = new CountyBean();
				bean.setCity(json.getString("city"));
				bean.setFlag(json.getString("flag"));
				bean.setId(json.getInt("id"));
				try {
					bean.setPid(json.getInt("pid"));
				} catch (Exception e) {
					bean.setPid(0);
				}
				counties.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getActionDataFromJson() {
		try {
			JSONObject jsonObject = new JSONObject(activityJson);
			JSONArray jsonArray = jsonObject.getJSONArray("actions");
			if (jsonArray.length() == 0) {
				showSomeThing("û�и�������");
				allActivitiesList.setLastData(true);
				return;
			} else {
				allActivitiesList.setLastData(false);
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				ActionBean action = ActionBean.getActionBean(json);
				if(action != null)
				actions.add(action);
			}
		} catch (JSONException e1) {
			showSomeThing(e1.toString());
		}

	}

	public void getMallDataFromJson() {
		// malls.clear();
		try {
			JSONObject jsonObject = new JSONObject(mallJson);
			JSONArray jsonArray = jsonObject.getJSONArray("malls");
			if (jsonArray.length() == 0) {
				showSomeThing("û�и�������");
				allMallsList.setLastData(true);
				return;
			} else {
				allMallsList.setLastData(false);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				MallBean bean = MallBean.getMallBean(json);
				if(bean != null)
				malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getFriendDataFromJson() {
		// �û��б�
		// friends.clear();
		try {
			JSONObject jsonObject = new JSONObject(friendJson);
			JSONArray jsonArray = jsonObject.getJSONArray("frds");
			if (jsonArray.length() == 0) {
				showSomeThing("û�и�������");
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

	public void initActivitiesList() {
		getActionDataFromJson();
		try {
			allActivitiesList.setAdapter(null);
			if(actions.size() < 5){
				allActivitiesList.removeHeaderView();
				allActivitiesList.removeFootView();
			}else{
				allActivitiesList.addHeaderView();
				allActivitiesList.addFootView();
			}
			activityAdapter = new ActivityAdapter(actions, this);
			allActivitiesList.setAdapter(activityAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
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
			mallAdapter = new MallAdepter(this, malls);
			allMallsList.setAdapter(mallAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
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

	public List<HashMap<String, String>> getSimpleAdapterData() {
		getActionDataFromJson();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Iterator<ActionBean> iterator = actions.iterator();
		while (iterator.hasNext()) {
			ActionBean action = iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("bTime", action.getbTime());
			map.put("mall", action.getMall());
			map.put("actionId", action.getId() + "");
			map.put("pic", action.getPic());
			map.put("score", action.getScore());
			map.put("title", action.getTitle());
			list.add(map);
		}
		return list;
	}

	public List<HashMap<String, Object>> getSimpleAdapterMallData() {
		getMallDataFromJson();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Iterator<MallBean> iterator = malls.iterator();
		while (iterator.hasNext()) {
			MallBean action = iterator.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pic", action.getPic());
			map.put("score", action.getScore());
			map.put("address", action.getAddress());
			map.put("title", action.getTitle());
			list.add(map);
		}
		return list;
	}

	public List<HashMap<String, Object>> getSimpleAdapterFriendData() {
		getFriendDataFromJson();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Iterator<FriendBean> iterator = friends.iterator();
		while (iterator.hasNext()) {
			FriendBean action = iterator.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pic", action.getPic());
			if (action.getSex() == 0) {
				map.put("sex", R.drawable.girl_48);
			} else {
				map.put("sex", R.drawable.boy_48);
			}

			map.put("address", action.getAddr());
			map.put("name", action.getName());
			list.add(map);
		}
		return list;
	}

	/**
	 * ��ʼ��Column��Ŀ��
	 * */
	private void initTabColumn(final int index) {
		//if (counties.isEmpty())
		getCityFromJson();

		if (index == 0) {
			if (activityRadioGroup_content != null) {
				activityRadioGroup_content.removeAllViews();
			} else {
				showSomeThing("activityRadioGroup_content is null");
				return;
			}
		}

		if (index == 1) {
			if (mall_mRadioGroup_content != null) {
				mall_mRadioGroup_content.removeAllViews();
			} else {
				showSomeThing("mall_mRadioGroup_content is null");
				return;
			}
		}

		if (index == 2) {
			if (friend_mRadioGroup_content != null) {
				friend_mRadioGroup_content.removeAllViews();
			} else {
				showSomeThing("friend_mRadioGroup_content is null");
				return;
			}
		}

		int count = counties.size();
		// showSomeThing(count+"");
		/**/
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < count; i++) {
			String city = counties.get(i).getCity();
			// int length = city.length();
			// mItemWidth = (40) * length;
			params.leftMargin = 5;
			params.rightMargin = 5;
			// params.width = mItemWidth;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(counties.get(i).getId());
			columnTextView.setText(city);
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
			if (index == 0) {
				if (activityColumnSelectIndex == i) {
					columnTextView.setSelected(true);
				}
			}
			if (index == 1) {
				if (mallColumnSelectIndex == i) {
					columnTextView.setSelected(true);
				}
			}

			if (index == 2) {
				if (friendColumnSelectIndex == i) {
					columnTextView.setSelected(true);
				}
			}

			columnTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = 0;
					int count = 0;
					if (index == 0) {
						count = activityRadioGroup_content.getChildCount();
					}

					if (index == 1) {
						count = mall_mRadioGroup_content.getChildCount();
					}

					if (index == 2) {
						count = friend_mRadioGroup_content.getChildCount();
					}

					for (int i = 0; i < count; i++) {
						View localView = null;
						if (index == 0) {
							localView = activityRadioGroup_content
									.getChildAt(i);
						}

						if (index == 1) {
							localView = mall_mRadioGroup_content.getChildAt(i);
						}

						if (index == 2) {
							localView = friend_mRadioGroup_content
									.getChildAt(i);
						}

						if (localView != v)
							localView.setSelected(false);
						else {
							id = localView.getId();
							localView.setSelected(true);
						}
					}
					try {
						if(index == 0){
							curActivityRg = id;
							curActivityPage = 1;
							new Thread(new HttpUtil(Utils.activityUrl + curActivityPage + "&rg=" + curActivityRg + "&sr=" + curActivitySr,
									new Handler() {

								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									Bundle data = msg.getData();
									String result = data.getString("CHANGEQU");
									if (result.length() > 0 && !result.equals("�������ʧ��")) {
										activityJson = result;
										actions.clear();
										initActivitiesList();
									}
								}
							}, "CHANGEQU")).start();
						}
						if(index == 1){
							curMallRg = id;
							curMallPage = 1;
							new Thread(new HttpUtil(Utils.mallListUrl + curMallPage + "&rg=" + curMallRg + "&sr=" + curMallSr,
									new Handler() {

								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									Bundle data = msg.getData();
									String result = data.getString("CHANGEQU2");
									if (result.length() > 0 && !result.equals("�������ʧ��")) {
										mallJson = result;
										malls.clear();
										initMallList();
									}
								}
							}, "CHANGEQU2")).start();
						}
						if(index == 2){
							
							if(true){
								return;
							}
							
							curFriendRg = id;
							curFriendPage = 1;
							new Thread(new HttpUtil(Utils.activityUrl + curActivityPage + "&rg=" + curActivityRg + "&sr=" + curActivitySr,
									new Handler() {

								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									Bundle data = msg.getData();
									String result = data.getString("CHANGEQU3");
									if (result.length() > 0 && !result.equals("�������ʧ��")) {
										activityJson = result;
										actions.clear();
										initActivitiesList();
									}
								}
							}, "CHANGEQU3")).start();
						}
						showSomeThing(id + "");
					} catch (Exception e) {
						showSomeThing(e.toString());
					}

				}
			});
			try {
				if (index == 0) {
					activityRadioGroup_content.addView(columnTextView, i,
							params);
				}

				if (index == 1) {
					mall_mRadioGroup_content.addView(columnTextView, i, params);
				}

				if (index == 2) {
					friend_mRadioGroup_content.addView(columnTextView, i,
							params);
				}

			} catch (Exception e) {
				showSomeThing(e.toString());
			}

		}
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		deleteMenuWindow();
		return super.onTouchEvent(event);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	/**
	 * ѡ���Column�����Tab
	 * */
	public void selectTab(int tab_postion) {
		activityColumnSelectIndex = tab_postion;
		for (int i = 0; i < activityRadioGroup_content.getChildCount(); i++) {
			View checkView = activityRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// �ж��Ƿ�ѡ��
		for (int j = 0; j < activityRadioGroup_content.getChildCount(); j++) {
			View checkView = activityRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	public void clickMallLocationBtn(View v) {
		showSomeThing("��λ����");
	}

	public void clickActionLocationBtn(View v) {

		showSomeThing("��λ�");
	}

	/**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	/*
	 * ҳ���л�����(ԭ����:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {

			if (position == 0) {
				mainTitleBar.setVisibility(View.VISIBLE);
				initActionView();
			} else {
				mainTitleBar.setVisibility(View.GONE);
			}
			if (position == 1) {
				initMallView();
			}

			if (position == 2) {
				initFriendView();
			}

			Animation animation = null;
			switch (position) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_find_frd_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					// mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_find_frd_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					// mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					// mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 3:
				// mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = position;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			// mTabImg.startAnimation(animation);
			deleteMenuWindow();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			deleteMenuWindow();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			deleteMenuWindow();
		}

	}

	public void deleteMenuWindow() {

		if (menuWindow != null && menuWindow.isShowing()) {
			menuWindow.dismiss();
		}
	}

	private long clickKeyCodeBackTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // ��ȡ
																				// back��

			if (menu_display) { // ��� Menu�Ѿ��� ���ȹر�Menu
				menuWindow.dismiss();
				menu_display = false;
			} else {
				long nowTime = System.currentTimeMillis();
				if (nowTime - clickKeyCodeBackTime > 2000) {
					showSomeThing("�ٴε���˳�����!");
					clickKeyCodeBackTime = nowTime;
				} else {
					if (connectionReceiver != null) {
						unregisterReceiver(connectionReceiver);
					}
					this.finish();
				}
				// Intent intent = new Intent();
				// intent.setClass(MainWeixin.this,Exit.class);
				// startActivity(intent);
			}
		}

		else if (keyCode == KeyEvent.KEYCODE_MENU) { // ��ȡ Menu��
			if (!menu_display) {
				// ��ȡLayoutInflaterʵ��
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// �����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				// �÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.main_menu, null);

				// ��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
				menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT); // ������������width��height
				// menuWindow.showAsDropDown(layout); //���õ���Ч��
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
				// ��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
				// mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout) layout
						.findViewById(R.id.menu_close_btn);

				// �����ÿһ��Layout���е����¼���ע��ɡ�����
				// ���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
				// ����׼����һЩ����ͼƬ������ɫ
				mCloseBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(Main.this, "�˳�",
						// Toast.LENGTH_LONG).show();
						// Intent intent = new Intent();
						// intent.setClass(MainWeixin.this,Exit.class);
						// startActivity(intent);
						menuWindow.dismiss(); // ��Ӧ����¼�֮��ر�Menu
						instance.finish();
					}
				});
				menu_display = true;
			} else {
				// �����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
			}

			return false;
		}
		return false;
	}

	// ���ñ������Ҳఴť������
	public void createSport(View v) {
		Toast.makeText(getApplicationContext(), "����˴����", Toast.LENGTH_LONG)
				.show();
	}

	// ���ñ������Ҳఴť������
	public void btnmainright(View v) {
		try {
			Intent intent = null;//new Intent(MainWeixin.this, Login.class);
			
			if(Utils.loginInfo != null){
				intent = new Intent(MainWeixin.this, com.yuguan.activities.AccountInfo.class);
			}else{
				intent = new Intent(MainWeixin.this, Login.class);
			}
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
		
		//Intent intent = new Intent(MainWeixin.this, Login.class);
		//startActivity(intent);
		// Toast.makeText(getApplicationContext(), "����˹��ܰ�ť",
		// Toast.LENGTH_LONG).show();
	}

	public void getActionInfo(View v) { // С�� �Ի�����
		if(!netIsGood){
			showSomeThing("");
		}
		TextView idView = (TextView)v.findViewById(R.id.actionId);
		int id = Integer.parseInt(idView.getText().toString());
		Intent intent = new Intent(MainWeixin.this, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("actionId", id);
		intent.putExtras(bundle);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "��¼�ɹ�",
		// Toast.LENGTH_LONG).show();
	}

	public void getMallInfo(View v) { // С�� �Ի�����
		try {
			TextView idView = (TextView)v.findViewById(R.id.mallId);
			int id = Integer.parseInt(idView.getText().toString());
			Intent intent = new Intent(MainWeixin.this, MallInfo.class);
			Bundle bundle = new Bundle();
			bundle.putInt("mallId", id);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "��¼�ɹ�",
		// Toast.LENGTH_LONG).show();
	}

	public void getFriendInfo(View v,int position) {
		try {
			FriendBean bean = friends.get(position);
			Intent intent = new Intent(MainWeixin.this, FriendInfo.class);
			TextView idView = (TextView)v.findViewById(R.id.friendId);
			int id = Integer.parseInt(idView.getText().toString());
			Bundle bundle = new Bundle();
			bundle.putInt("friendId", id);
			bundle.putSerializable("friendBean", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "��¼�ɹ�",
		// Toast.LENGTH_LONG).show();
	}

	public void exit_settings(View v) { // �˳� α���Ի��򡱣���ʵ��һ��activity
		Intent intent = new Intent(MainWeixin.this, ExitFromSettings.class);
		startActivity(intent);
	}

	public void btn_shake(View v) { // �ֻ�ҡһҡ
		Intent intent = new Intent(MainWeixin.this, ShakeActivity.class);
		startActivity(intent);
	}
	
	public void clickfriendLocationBtn(View v){
		curFriendPage = 1;
		String url = Utils.friendsUrl + "&cid=" + Utils.cid + "&pn=" + curFriendPage;
		new Thread(new HttpUtil(url,friendHandler, KEY_FRIEND_JSON)).start();
	}
}
