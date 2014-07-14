package com.yuguan.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.FriendBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.UploadUtil;
import com.yuguan.util.Utils;

public class AccountInfo extends Activity {

	private TextView accountName;
	private TextView accountAddress;
	private RoundImageView accountImg;
	private ImageView accountSex;
	private ImageView msgNew;
	private LinearLayout msgcenter;
	private LinearLayout mysport;
	private TextView mysportText;
	private LinearLayout myfriends;
	private TextView myfriendsText;
	private LinearLayout myshoucang;
	private TextView myshoucangText;
	private ImageLoader mImageLoader;
	// 剪切后保存的图片
	private String uploadFile = "";
	// 保存拍照留下的临时照片
	private String newCapturePhotoPath = "";
	private String[] items = new String[] { "选择图片", "拍照" };
	/* 头像名称 */
	private static String IMAGE_FILE_NAME = ""
			+ (int) (System.currentTimeMillis() / 1000) + ".jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private int uid = 0;
	private boolean isAccount = false;

	@SuppressLint("HandlerLeak")
	private Handler countHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("countHandler");
			if (result != null && !"服务访问失败".equals(result)) {
				try {
					Utils.msgCount = new JSONObject(result);
					Utils.actinvite = Utils.msgCount.getInt("actinvite");
					Utils.addfrd = Utils.msgCount.getInt("addfrd");
					Utils.notice = Utils.msgCount.getInt("notice");
					Utils.prvmsg = Utils.msgCount.getInt("prvmsg");

					if (Utils.notice + Utils.addfrd > 0) {
						msgNew.setVisibility(View.VISIBLE);
					}
					// showSomeThing(result);
				} catch (JSONException e) {
					showSomeThing(e.toString());
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	public void init(){
		try {
			setContentView(R.layout.accountinfo);
			uid = getIntent().getExtras().getInt("uid");
			isAccount = getIntent().getExtras().getBoolean("isAccount");
			initView();
			new Thread(new HttpUtil(Utils.getMessageCountUrl + "&uid="
					+ Utils.loginInfo.getId(), countHandler, "countHandler"))
					.start();
			mImageLoader = new ImageLoader(getApplicationContext());
			getAccountInfo(uid);
		} catch (Exception e) {
			showSomeThing(e.toString());
		}
	}
	
	//Activity从后台重新回到前台时被调用  
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        Log.i("onRestart", "onRestart called.");  
       // init();
    }  
      
    //Activity创建或者从被覆盖、后台重新回到前台时被调用  
    @Override  
    protected void onResume() {  
        super.onResume();  
        Log.i("onResume", "onResume called.");  
    }  
    
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
    /** 
     * Activity被系统杀死时被调用. 
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死. 
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态. 
     * 在onPause之前被调用. 
     */  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
    }  
      
    /** 
     * Activity被系统杀死后再重建时被调用. 
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity. 
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后. 
     */  
    @Override  
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);  
    }  

	public void getAccountInfo(int id) {
		String url = Utils.getAccountInfoUrl + id;
		new Thread(new HttpUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("GETACCOUNTINFO");
				if (result != null && !"服务访问失败".equals(result)) {
					try {
						JSONObject json = new JSONObject(result);
						JSONObject message = json.getJSONObject("user");
						if (message != null) {
							FriendBean bean = FriendBean
									.getBeanFromJson(message);
							final String url = Utils.userImg + bean.getPic();
							accountImg.setTag(url);
							mImageLoader.loadImage(url, null, accountImg);
							if (isAccount) {
								Utils.self = bean;
								accountImg.setDrawingCacheEnabled(true);
								Utils.selfPic = accountImg.getDrawingCache();
								/*new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Utils.selfPic = mImageLoader
												.getImageFromUrl(url);
									}
								}).start();*/
							}
							initValue(bean);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}, "GETACCOUNTINFO")).start();
	}

	private void initView() {
		accountName = (TextView) findViewById(R.id.accountName);
		accountImg = (RoundImageView) findViewById(R.id.accountImg);
		accountSex = (ImageView) findViewById(R.id.accountSex);
		msgNew = (ImageView) findViewById(R.id.msgNew);
		accountAddress = (TextView) findViewById(R.id.accountAddress);
		msgcenter = (LinearLayout) findViewById(R.id.msgcenter);
		mysport = (LinearLayout) findViewById(R.id.mysport);
		mysportText = (TextView) findViewById(R.id.mysportText);
		myfriends = (LinearLayout) findViewById(R.id.myfriends);
		myfriendsText = (TextView) findViewById(R.id.myfriendsText);
		myshoucang = (LinearLayout) findViewById(R.id.myshoucang);
		myshoucangText = (TextView) findViewById(R.id.myshoucangText);

		if (isAccount) {
			msgcenter.setVisibility(View.VISIBLE);
			mysportText.setText("我的活动");
			myfriendsText.setText("我的好友");
			myshoucangText.setText("我的收藏");

		} else {
			msgcenter.setVisibility(View.GONE);
			mysportText.setText("他的活动");
			myfriendsText.setText("他的好友");
			myshoucangText.setText("他的收藏");
		}

		accountImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAccount)
					showDialog();
			}
		});

	}

	private void initValue(FriendBean bean) {
		if (bean != null) {
			accountName.setText(bean.getName());
			if (isAccount && Utils.selfPic != null) {
				accountImg.setImageBitmap(Utils.selfPic);
			}
			if (bean.getSex() == 0) {
				accountSex.setImageResource(R.drawable.boy_48);
			} else {
				accountSex.setImageResource(R.drawable.girl_48);
			}
			accountAddress.setText(bean.getAddr());
		}

	}

	public void doMsgcenter(View v) {
		Intent intent = new Intent(AccountInfo.this, MessageCenter.class);
		Bundle bundle = new Bundle();
		bundle.putInt("uid", uid);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void doMysports(View v) {
		Intent intent = new Intent(AccountInfo.this, MyActions.class);
		Bundle bundle = new Bundle();
		bundle.putInt("uid", uid);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void doMyfriends(View v) {
	}

	public void doMyshoucang(View v) {
	}

	public void doaccountBack(View v) {
		this.finish();
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		IMAGE_FILE_NAME = "pic" + (int) (System.currentTimeMillis() / 1000)
				+ ".jpg";
		tempUri = Uri.parse("file:///sdcard/" + IMAGE_FILE_NAME);

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {
								// 拍完照片之后保存的路径（文件名）
								newCapturePhotoPath = Environment
										.getExternalStorageDirectory()
										.toString()
										+ "/temp.jpg";
								// 加上这个后，图片就不会被压缩变小了
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT, Uri
												.fromFile(new File(
														newCapturePhotoPath)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {

			// 结果码不等于取消时候
			if (resultCode != RESULT_CANCELED) {

				switch (requestCode) {
				case IMAGE_REQUEST_CODE:
					startPhotoZoom(data.getData());
					break;
				case CAMERA_REQUEST_CODE:
					if (hasSdcard()) {
						File file = new File(newCapturePhotoPath);
						startPhotoZoom(Uri.fromFile(file));
					} else {
						showSomeThing("未找到存储卡，无法存储照片！");
					}

					break;
				case RESULT_REQUEST_CODE:
					if (data != null) {
					}
					// 照片剪切好后上传服务器
					uploadFile = "/mnt/sdcard/" + IMAGE_FILE_NAME;
					upload();
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			showSomeThing(e.toString());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Uri tempUri;

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 480);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private File getFileByUri(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		actualimagecursor.moveToFirst();

		String img_path = actualimagecursor
				.getString(actual_image_column_index);

		File file = new File(img_path);

		return file;
	}

	private String getFilePathByUri(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		if (actualimagecursor == null) {
			showSomeThing("actualimagecursor is null");
			return null;
		}
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		actualimagecursor.moveToFirst();

		String img_path = actualimagecursor
				.getString(actual_image_column_index);

		return img_path;
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		// Bundle extras = data.getExtras();
		// Bitmap photo = extras.getParcelable("data");
		try {
			Bitmap photo = decodeUriAsBitmap(tempUri);
			Utils.selfPic = photo;
			accountImg.setImageBitmap(photo);
			String url = Utils.userImg + IMAGE_FILE_NAME;
			mImageLoader.saveImageToFile(url, photo);
			
			Intent mIntent = new Intent(Utils.USERCHANGEIMAGE); 
            //mIntent.putExtra(Utils.NEWUSERIMAGE, photo); 
            //发送广播 
            sendBroadcast(mIntent); 
		} catch (Exception e) {
			showSomeThing("shi " + e.toString());
		}
	}

	private Handler resHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			int responseCode = data.getInt("responseCode");
			String responseMessage = data.getString("responseMessage");
			// 图片上传成功
			if (responseCode == UploadUtil.UPLOAD_SUCCESS_CODE) {
				showSomeThing("上传成功");
				// 设置新头像
				getImageToView(null);
				// 删除旧照片
				File old = new File(mImageLoader.imageFileCache.getDirectory()
						+ File.separator + Utils.self.getPic());
				old.delete();
				// 删除剪切留下的照片
				if (uploadFile.length() > 0) {
					File file = new File(uploadFile);
					if(file.exists())
						file.delete();
				}
				// 删除拍照留下的照片
				if (newCapturePhotoPath.length() > 0) {
					File file = new File(newCapturePhotoPath);
					if(file.exists())
						file.delete();
				}
				Utils.self.setPic(IMAGE_FILE_NAME);
				
			} else {
				showSomeThing(responseMessage);
			}

		}
	};

	public void upload() {
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setHandler(resHandler);
		uploadUtil
				.setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {

					@Override
					public void onUploadProcess(int uploadSize) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onUploadDone(int responseCode, String message) {
						// TODO Auto-generated method stub
						showSomeThing(message);
					}

					@Override
					public void initUpload(int fileSize) {
						// TODO Auto-generated method stub

					}
				});

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", Utils.loginInfo.getId() + "");
		uploadUtil.uploadFile(uploadFile, "img", Utils.uploadFileUrl, params);

	}

	/* 上传文件至Server的方法 */
	private void uploadFileToServer() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			String actionUrl = Utils.uploadFileUrl;
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(10 * 1000);// 设置超时的时间
			con.setReadTimeout(10 * 1000);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + IMAGE_FILE_NAME
					+ "\";uid=\"" + Utils.loginInfo.getId() + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			Object obj = con.getResponseMessage();
			if (obj != null) {
				showSomeThing(obj.toString());
			}
			int res = con.getResponseCode();

			if (res == 200) {
				InputStream is = con.getInputStream();

				if (is != null) {
					int ch;
					StringBuffer b = new StringBuffer();
					while ((ch = is.read()) != -1) {
						b.append((char) ch);
					}
					showSomeThing("上传成功" + b.toString());
				}
			} else {
				showSomeThing(res + " code");
			}

			ds.close();
			con.disconnect();
		} catch (Exception e) {
			showSomeThing("上传失败" + e.toString());
		}
	}

}
