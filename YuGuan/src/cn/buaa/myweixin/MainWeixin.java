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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yuguan.activities.ActivityAdapter;
import com.yuguan.activities.MallAdepter;
import com.yuguan.activities.MallInfo;
import com.yuguan.activities.RefreshListView;
import com.yuguan.activities.UserAdepter;
import com.yuguan.bean.ActionBean;
import com.yuguan.bean.CountyBean;
import com.yuguan.bean.MallBean;
import com.yuguan.bean.UserBean;
import com.yuguan.imagecache.ImageCacheManager;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MainWeixin extends Activity {

	public static MainWeixin instance = null;

	private ViewPager mTabPager;
	// private ImageView mTabImg;// 动画图片
	private ImageView mTab1, mTab2, mTab3;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	// private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private View layout;
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	// private Button mRightBtn;

	private LinearLayout mainTitleBar;

	/** 省分类列表 */
	// private ArrayList<String> provinces = new ArrayList<String>();
	private int cityId = 173;

	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	// private int mItemWidth = 0;
	private HorizontalScrollView mColumnHorizontalScrollView;

	/** 市区分类列表 */
	private ArrayList<CountyBean> counties = new ArrayList<CountyBean>();
	// private boolean isCountyLoading = false;
	public static ImageCacheManager imageCacheManager;

	/** 所有活动 */
	private RefreshListView allActivitiesList;
	private ActivityAdapter activityAdapter;
	private List<ActionBean> actions = new ArrayList<ActionBean>();
	private boolean allActivitiesListIsLoad = false;
	private LinearLayout activityRadioGroup_content;
	private int activityColumnSelectIndex = 0;
	private int curActivityPage = 1;

	/** 所有场馆 */
	private RefreshListView allMallsList;
	private MallAdepter mallAdapter;
	private List<MallBean> malls = new ArrayList<MallBean>();
	private boolean allMallsListIsLoad = false;
	private LinearLayout mall_mRadioGroup_content;
	private int mallColumnSelectIndex = 0;

	/** 所有好友 */
	private RefreshListView allFriendsList;
	private UserAdepter userAdepter;
	private List<UserBean> friends = new ArrayList<UserBean>();
	private boolean allFriendsListIsLoad = false;
	private LinearLayout friend_mRadioGroup_content;
	private int friendColumnSelectIndex = 0;

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
			if (result != null && !"服务访问失败".equals(result)) {
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
			if (result.length() > 0 && !result.equals("服务访问失败")) {
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
			mallJson = data.getString(KEY_MALL_JSON);
			if (mallJson.length() > 0 && !mallJson.equals("服务访问失败")) {
				initMallList();
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler friendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			friendJson = data.getString(KEY_FRIEND_JSON);
			if (friendJson.length() > 0 && !friendJson.equals("服务访问失败")) {
				initFriendList();
			}
		}
	};

	public static boolean netIsGood = true;
	// 检测手机网络状态
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
				showSomeThing("无法连接网络!");
			} else {
				netIsGood = true;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* 固定存储空间大小 */
		imageCacheManager = ImageCacheManager.getImageCacheService(this,
				ImageCacheManager.MODE_FIXED_MEMORY_USED, "_yuguan");
		imageCacheManager.setMax_Memory(1024 * 1024 * 50);

		
		//打印出来的结果分别是：
		//data路径 /datasd卡路径 /mnt/sdcard根路径 /systemandroid保存sp文件的路径：android存SharedPreferences的路径/data/data/package_name/shared_prefs貌似通过源码，反射的方法可以修改这些默认路径通过context.openFileInput（“fileName”）；content.openFileOutput(“fileName”,Activity.MODE_PRIVATE)默认的文件路径为/data/data/包.名/files
		try {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			this.registerReceiver(connectionReceiver, intentFilter);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
		setContentView(R.layout.main_weixin);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;

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
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		// int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;
		// Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);

		mScreenWidth = displayWidth;

		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.main_tab_weixin, null);
		View view2 = mLi.inflate(R.layout.main_tab_address, null);
		View view3 = mLi.inflate(R.layout.main_tab_friends, null);
		// View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
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

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));

				if (position == 0) {
					initActionView();
				}

				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);

	}

	public void loadImage(ImageView view) {
		if (view != null) {
			Animation operatingAnim = AnimationUtils.loadAnimation(this,
					R.anim.loop);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			view.startAnimation(operatingAnim);
		}
	}

	public void destoryImage(ImageView view) {
		if (view != null) {
			view.clearAnimation();
		}
	}

	private void initActionView() {
		if (allActivitiesListIsLoad == false) {
			activityRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
			// loadImage = (ImageView) findViewById(R.id.activity_loaddata);
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
								if (result.length() > 0 && !result.equals("服务访问失败")) {
									activityJson = result;
									actions.clear();
									getActionDataFromJson();
									allActivitiesList.setLastData(false);
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
									String url = Utils.activityUrl + curActivityPage;
									allActivitiesList.setUrl(url);
								}else{
									showSomeThing("加载到最后一条了");
								}
							}

							@Override
							public void setUrl() {
								curActivityPage = 1;
								String url = Utils.activityUrl + curActivityPage;
								allActivitiesList.setUrl(url);
							}

							@Override
							public void loaded(Object obj) {
								if (obj == null) {
									return;
								}

								String result = obj.toString();
								if (result.length() > 0 && !result.equals("服务访问失败")) {
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
				
				new Thread(new HttpUtil(Utils.activityUrl + curActivityPage,
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
			try {
				allMallsList = (RefreshListView) findViewById(R.id.allMallsLists);
				allMallsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getMallInfo(view);
							}
						});
				initMallList();
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allMallsListIsLoad = true;
		}
	}

	private void initFriendView() {
		if (allFriendsListIsLoad == false) {
			friend_mRadioGroup_content = (LinearLayout) findViewById(R.id.friend_mRadioGroup_content);
			initTabColumn(2);
			try {
				allFriendsList = (RefreshListView) findViewById(R.id.allfriendsLists);
				allFriendsList
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								getFriendInfo(view);
							}
						});
				initFriendList();
			} catch (Exception e) {
				showSomeThing(e.toString());
			}

			allFriendsListIsLoad = true;
		}
	}

	public void getCityFromJson() {

		counties.clear();
		counties.add(new CountyBean(0, null, "全部", 0));
		try {
			JSONObject jsonObject = new JSONObject(countyJson);
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
				showSomeThing("加载到最后一条了");
				allActivitiesList.setLastData(true);
				return;
			} else {
				allActivitiesList.setLastData(false);
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				ActionBean action = new ActionBean();
				action.setbTime(json.getString("bTime"));
				action.setId(json.getInt("id"));
				action.setMall(json.getString("mall"));
				action.setPic(json.getString("pic"));
				action.setScore(json.getString("score"));
				action.setTitle(json.getString("title"));
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
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				MallBean bean = new MallBean();
				bean.setAddress(json.getString("address"));
				bean.setComments(json.getInt("comments"));
				bean.setFavorites(json.getInt("favorites"));
				bean.setMdesc(json.getString("mdesc"));
				bean.setPhone(json.getString("phone"));
				bean.setPic(json.getString("pic"));
				Double score = json.getDouble("score");
				bean.setScore(score.intValue());
				bean.setTitle(json.getString("title"));
				bean.setId(json.getInt("id"));
				malls.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getFriendDataFromJson() {
		// 用户列表：
		// friends.clear();
		try {
			JSONObject jsonObject = new JSONObject(friendJson);
			JSONArray jsonArray = jsonObject.getJSONArray("users");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				UserBean bean = UserBean.getBeanFromJson(json);
				/*UserBean action = new UserBean();
				action.setId(json.getInt("id"));
				action.setAddress(json.getString("address"));
				action.setPic(json.getString("pic"));
				action.setSex(json.getInt("sex"));
				action.setName(json.getString("name"));*/
				if(bean != null)
					friends.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void initActivitiesList() {
		getActionDataFromJson();
		activityAdapter = new ActivityAdapter(actions, this,allActivitiesList);
		allActivitiesList.setAdapter(activityAdapter);
	}

	private void initMallList() {
		/**/
		getMallDataFromJson();
		mallAdapter = new MallAdepter(this, malls);
		try {
			allMallsList.setAdapter(mallAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
	}

	private void initFriendList() {
		getFriendDataFromJson();
		userAdepter = new UserAdepter(this, friends);
		allFriendsList.setAdapter(userAdepter);
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
		Iterator<UserBean> iterator = friends.iterator();
		while (iterator.hasNext()) {
			UserBean action = iterator.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pic", action.getPic());
			if (action.getSex() == 0) {
				map.put("sex", R.drawable.girl_48);
			} else {
				map.put("sex", R.drawable.boy_48);
			}

			map.put("address", action.getAddress());
			map.put("name", action.getName());
			list.add(map);
		}
		return list;
	}

	/**
	 * 初始化Column栏目项
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
	 * 选择的Column里面的Tab
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
		// 判断是否选中
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
		showSomeThing("定位场馆");
	}

	public void clickActionLocationBtn(View v) {

		showSomeThing("定位活动");
	}

	/**
	 * 头标点击监听
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
	 * 页卡切换监听(原作者:D.Winter)
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
			animation.setFillAfter(true);// True:图片停在动画结束位置
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
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
																				// back键

			if (menu_display) { // 如果 Menu已经打开 ，先关闭Menu
				menuWindow.dismiss();
				menu_display = false;
			} else {
				long nowTime = System.currentTimeMillis();
				if (nowTime - clickKeyCodeBackTime > 2000) {
					showSomeThing("再次点击退出程序!");
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

		else if (keyCode == KeyEvent.KEYCODE_MENU) { // 获取 Menu键
			if (!menu_display) {
				// 获取LayoutInflater实例
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);

				// 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				// menuWindow.showAsDropDown(layout); //设置弹出效果
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				// 如何获取我们main中的控件呢？也很简单
				// mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout) layout
						.findViewById(R.id.menu_close_btn);

				// 下面对每一个Layout进行单击事件的注册吧。。。
				// 比如单击某个MenuItem的时候，他的背景色改变
				// 事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(Main.this, "退出",
						// Toast.LENGTH_LONG).show();
						// Intent intent = new Intent();
						// intent.setClass(MainWeixin.this,Exit.class);
						// startActivity(intent);
						menuWindow.dismiss(); // 响应点击事件之后关闭Menu
						instance.finish();
					}
				});
				menu_display = true;
			} else {
				// 如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
			}

			return false;
		}
		return false;
	}

	// 设置标题栏右侧按钮的作用
	public void createSport(View v) {
		Toast.makeText(getApplicationContext(), "点击了创建活动", Toast.LENGTH_LONG)
				.show();
	}

	// 设置标题栏右侧按钮的作用
	public void btnmainright(View v) {
		Intent intent = new Intent(MainWeixin.this, Login.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "点击了功能按钮",
		// Toast.LENGTH_LONG).show();
	}

	public void getActionInfo(View v) { // 小黑 对话界面
		TextView idView = (TextView)v.findViewById(R.id.actionId);
		int id = Integer.parseInt(idView.getText().toString());
		Intent intent = new Intent(MainWeixin.this, ChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("actionId", id);
		intent.putExtras(bundle);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	public void getMallInfo(View v) { // 小黑 对话界面
		try {
			Intent intent = new Intent(MainWeixin.this, MallInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	public void getFriendInfo(View v) {
		try {
			Intent intent = new Intent(MainWeixin.this, MallInfo.class);
			startActivity(intent);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}

		// Toast.makeText(getApplicationContext(), "登录成功",
		// Toast.LENGTH_LONG).show();
	}

	public void exit_settings(View v) { // 退出 伪“对话框”，其实是一个activity
		Intent intent = new Intent(MainWeixin.this, ExitFromSettings.class);
		startActivity(intent);
	}

	public void btn_shake(View v) { // 手机摇一摇
		Intent intent = new Intent(MainWeixin.this, ShakeActivity.class);
		startActivity(intent);
	}
}
