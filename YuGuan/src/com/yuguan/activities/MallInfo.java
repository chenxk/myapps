package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.DoubleTextView;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.ScoreInfo;

import com.yuguan.bean.CommentBean;
import com.yuguan.util.HttpPostUtil;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class MallInfo extends Activity{

	private ScrollView screen;
	private TextView title;
	private ImageView bigImage;
	private LinearLayout horizontalScrollView;
	private ScoreInfo scoreInfo;
	private ImageView loveImg;
	private TextView desc;
	private DoubleTextView address;
	private TextView mallMap;
	private DoubleTextView phone;
	private ImageView fuwuxinxiImg;
	private LinearLayout fuwuxinxiInfo;
	private boolean fuwuxinxiExp = false;
	private ImageView jiaotongImg;
	private LinearLayout jiaotongInfo;
	private boolean jiaotongExp = false;
	private ImageView pinglunImg;
	private LinearLayout pinglunInfo;
	private boolean pinglunExp = false;
	private EditText mEditTextContent;
	
	
	private TextView fuwuOpen;
	private TextView fuwuPeo;
	private TextView fuwuCaizhi;
	private TextView fuwuDili;
	private TextView fuwuNum;
	private TextView fuwuHigh;
	private TextView fuwuNet;
	private TextView fuwuLingyu;
	private TextView fuwuShop;
	private TextView bus;
	private TextView subway;
	private TextView parking;
	
	private List<String> imags = new ArrayList<String>();

	private int mallId;
	private int commentId = 0;
	private ImageLoader mImageLoader;
	private String mallInfoJson = InitValue.mallsInfo;
	private String mallPicsJson = InitValue.picsInfoJson;
	private String mallEnvJson = InitValue.envInfoJson;
	private RefreshListView commentList;
	private CommentAdepter commentAdepter;
	
	private List<CommentBean> comments = new ArrayList<CommentBean>();
	

	private final String KEY_MALL_JSON = "KEY_MALL_JSON";
	@SuppressLint("HandlerLeak")
	private Handler mallHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_MALL_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				mallInfoJson = result;
			}
			getMallInfoFromJson();
		}
	};

	private final String KEY_PICS_JSON = "KEY_PICS_JSON";
	@SuppressLint("HandlerLeak")
	private Handler picsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_PICS_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				mallPicsJson = result;
			}
			getPicsFromJSON();
		}

	};
	
	private String commentJson = InitValue.commentJson;
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
	
	private final String KEY_ENV_JSON = "KEY_ENV_JSON";
	@SuppressLint("HandlerLeak")
	private Handler envHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString(KEY_ENV_JSON);
			if (result != null && !"服务访问失败".equals(result)) {
				mallEnvJson = result;
			}
			getEnvInfoFromJSON();
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
	
	public void init(){
		setContentView(R.layout.mallinfo);

		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		mallId = getIntent().getIntExtra("mallId", 0);
		//showSomeThing(mallId + "");
		mImageLoader = new ImageLoader(getApplicationContext());

		initView();

		new Thread(new HttpUtil(Utils.mallInfoUrl + mallId, mallHandler,
				KEY_MALL_JSON)).start();
		new Thread(new HttpUtil(Utils.mallPicsUrl + mallId,
				picsHandler, KEY_PICS_JSON)).start();
		new Thread(new HttpUtil(Utils.mallCommentUrl + mallId +"&commentid=" + commentId,
				commentHandler, KEY_COMMENT_JSON)).start();
		new Thread(new HttpUtil(Utils.mallEnvInfoUrl + mallId,
				envHandler, KEY_ENV_JSON)).start();
	}

	public void initView() {
		mEditTextContent = (EditText) findViewById(R.id.mallComment);
		screen = (ScrollView) findViewById(R.id.scroll);
		screen.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		title = (TextView) findViewById(R.id.mallBt);
		horizontalScrollView = (LinearLayout) findViewById(R.id.horizontalImages);
		bigImage = (ImageView) findViewById(R.id.bigImage);
		scoreInfo = (ScoreInfo) findViewById(R.id.mall_score);
		scoreInfo.setClickAble(true);
		loveImg = (ImageView) findViewById(R.id.shoucang_mall);
		desc = (TextView) findViewById(R.id.desc);
		address = (DoubleTextView) findViewById(R.id.mallAddress);
		mallMap = (TextView) findViewById(R.id.mallMap);
		phone = (DoubleTextView) findViewById(R.id.mallPhone);
		//expandableListView = (ExpandableListView) findViewById(R.id.expandListView);
		fuwuxinxiImg = (ImageView) findViewById(R.id.fuwuxinxi_img);
		fuwuxinxiInfo = (LinearLayout) findViewById(R.id.fuwuxinxi_info);
		jiaotongImg = (ImageView) findViewById(R.id.jiaotong_img);
		jiaotongInfo = (LinearLayout) findViewById(R.id.jiaotong_info);
		pinglunImg = (ImageView) findViewById(R.id.pinglun_img);
		pinglunInfo = (LinearLayout) findViewById(R.id.pinglun_info);
		
		fuwuOpen = (TextView) findViewById(R.id.fuwuOpen);
		fuwuPeo = (TextView) findViewById(R.id.fuwuPeo);
		fuwuCaizhi = (TextView) findViewById(R.id.fuwuCaizhi);
		fuwuDili = (TextView) findViewById(R.id.fuwuDili);
		fuwuNum = (TextView) findViewById(R.id.fuwuNum);
		fuwuHigh = (TextView) findViewById(R.id.fuwuHigh);
		fuwuNet = (TextView) findViewById(R.id.fuwuNet);
		fuwuLingyu = (TextView) findViewById(R.id.fuwuLingyu);
		fuwuShop = (TextView) findViewById(R.id.fuwuShop);
		bus = (TextView) findViewById(R.id.bus);
		subway = (TextView) findViewById(R.id.subway);
		parking = (TextView) findViewById(R.id.parking);
		
		commentList = (RefreshListView) findViewById(R.id.mallCommentList);
		commentList.setInterup(true);
		commentList.setParent(screen);
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
					String url = Utils.mallCommentUrl + mallId +"&commentid=" + commentId;
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
						String url = Utils.mallCommentUrl + mallId +"&commentid=" + commentId;
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
	
	
	private void getEnvInfoFromJSON() {
		try {
			JSONObject mallJson = new JSONObject(mallEnvJson);
			if (mallJson != null) {
				JSONObject env = mallJson.getJSONObject("envInfo");
				fuwuOpen.setText(env.getString("optime"));
				fuwuPeo.setText(env.getString("openobj"));
				fuwuCaizhi.setText(env.getString("floor"));
				fuwuDili.setText(env.getString("position"));
				fuwuNum.setText(env.getInt("num")+"片");
				fuwuHigh.setText(env.getInt("height")+"米");
				fuwuNet.setText(env.getString("wifi"));
				fuwuLingyu.setText(env.getString("wash"));
				fuwuShop.setText(env.getString("sale"));
				bus.setText(env.getString("bus"));
				subway.setText(env.getString("subway"));
				parking.setText(env.getString("parking"));
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

	private void getMallInfoFromJson() {
		try {
			JSONObject json = new JSONObject(mallInfoJson);
			if (json != null) {
				JSONObject actionInfo = json.getJSONObject("mallInfo");
				if (actionInfo == null)
					return;

				title.setText(actionInfo.getString("name"));
				Double score = actionInfo.getDouble("score");
				scoreInfo.setScore(score.intValue());
				desc.setText(actionInfo.getString("desc"));
				address.setValueTwo(actionInfo.getString("address"));
				phone.setValueTwo(actionInfo.getString("phone"));
				
			}
		} catch (JSONException e) {
			showSomeThing(e.toString());
		}

	}

	public void getPicsFromJSON() {

		try {

			JSONObject json = new JSONObject(mallPicsJson);
			JSONArray images = json.getJSONArray("imgs");

			horizontalScrollView.removeAllViews();
			imags.clear();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					150 * 2, 100 * 2);
			if (images != null) {
				for (int i = 0; i < images.length(); i++) {
					String imgName = images.get(i).toString();
					String url = Utils.mallPicUrl + imgName;
					imags.add(url);
					ImageView img = new ImageView(this);
					img.setPadding(10, 10, 10, 10);
					img.setScaleType(ScaleType.FIT_XY);
					img.setBackgroundResource(R.drawable.radio_buttong_bg);
					img.setId(i);
					img.setTag(url);
					mImageLoader.loadImage(url, null, img);
					if (i == 0) {
						img.setSelected(true);
						bigImage.setTag(url);
						mImageLoader.loadImage(url, null, bigImage);
					}
					img.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int count = horizontalScrollView.getChildCount();

							for (int i = 0; i < count; i++) {
								View localView = horizontalScrollView
										.getChildAt(i);

								if (localView != v)
									localView.setSelected(false);
								else {
									localView.setSelected(true);
									Bitmap map = mImageLoader
											.getBitmapFromCache(imags.get(i));
									if (map != null) {
										bigImage.setImageBitmap(map);
									}
								}
							}
						}
					});

					horizontalScrollView.addView(img, i, params);
				}
			}
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	public void back(View v) {
		this.finish();
	}
	
	public void doShoucang(View v) {
		// itemId="+mid+"&uid="+uid+"&
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		String url = Utils.mallShouCangUrl + mallId + "&uid" + Utils.loginInfo.getId();
		new Thread(new HttpUtil(url, new Handler(){
			public void handleMessage(Message msg) {
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
							loveImg.setImageResource(R.drawable.love2_32);
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

	public void dologin(View v) {
		Intent intent = new Intent(MallInfo.this, Login.class);
		startActivity(intent);
	}
	
	public void doExpandFuwuxinxi(View v){
		if(fuwuxinxiExp){
			fuwuxinxiExp = false;
			fuwuxinxiImg.setImageResource(R.drawable.expand);
			fuwuxinxiInfo.setVisibility(View.GONE);
		}else{
			fuwuxinxiExp = true;
			fuwuxinxiImg.setImageResource(R.drawable.expanded);
			fuwuxinxiInfo.setVisibility(View.VISIBLE);
		}
	}
	
	public void doExpandJiaotong(View v){
		if(jiaotongExp){
			jiaotongExp = false;
			jiaotongImg.setImageResource(R.drawable.expand);
			jiaotongInfo.setVisibility(View.GONE);
		}else{
			jiaotongExp = true;
			jiaotongImg.setImageResource(R.drawable.expanded);
			jiaotongInfo.setVisibility(View.VISIBLE);
		}
	}
	
	public void doExpandPinglun(View v){
		if(pinglunExp){
			pinglunExp = false;
			pinglunImg.setImageResource(R.drawable.expand);
			pinglunInfo.setVisibility(View.GONE);
		}else{
			pinglunExp = true;
			pinglunImg.setImageResource(R.drawable.expanded);
			pinglunInfo.setVisibility(View.VISIBLE);
		}
	}
	
	public void sendComment(View v) {
		String contString = mEditTextContent.getText().toString();
		if(Utils.loginInfo == null){
			showSomeThing("请先登陆...");
			return;
		}
		if (contString.length() > 0) {
			
			String url = Utils.putMallCommentUrl + "?mid=" + mallId + "&uid=" + Utils.loginInfo.getId() + "&score=" + scoreInfo.getScore();
			
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
										new Thread(new HttpUtil(Utils.mallCommentUrl + mallId +"&commentid=" + commentId,
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

}
