package cn.buaa.myweixin;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.yuguan.bean.AccountInfo;
import com.yuguan.bean.FriendBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.InitValue;
import com.yuguan.util.Utils;

public class Login extends Activity {
	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��

	private CheckBox rememberPwd;
	private CheckBox autoLogin;
	private SharedPreferences sp;
	private Login instance;
	private ImageLoader mImageLoader;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mImageLoader = new ImageLoader(getApplicationContext());
        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);
        rememberPwd = (CheckBox) findViewById(R.id.cb_mima);
        autoLogin = (CheckBox) findViewById(R.id.cb_auto);
        instance = this;
        sp = InitValue.preferences;
        
        if(sp != null){
        	boolean remPwd = sp.getBoolean("rememberPwd", true);
        	String name = sp.getString("userName", "");
        	String pwd = sp.getString("userPwd", "");
        	rememberPwd.setChecked(remPwd);
        	if(remPwd){
        		mUser.setText(name);
            	mPassword.setText(pwd);
        	}
        	boolean autoLog = (sp.getBoolean("autoLogin", true));
        	autoLogin.setChecked(autoLog);
        	if(autoLog && !"".equals(name) && !"".equals(pwd)){
        		login(name, pwd);
        	}
        }
        
        
        rememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sp.edit().putBoolean("rememberPwd", isChecked).commit();
			}
		});
        
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sp.edit().putBoolean("autoLogin", isChecked).commit();
				if(isChecked){
					rememberPwd.setChecked(isChecked);
					sp.edit().putBoolean("rememberPwd", isChecked).commit();
				}
			}
		});
    }
    
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }

    public void login_mainweixin(View v) {
    	if(!"".equals(mUser.getText().toString()) && !"".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
        {
    		 //String url = Utils.loginUrl + mUser.getText().toString() + "&password=" + mPassword.getText().toString();
    		 login(mUser.getText().toString(), mPassword.getText().toString());
         }
        else if("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
        {
        	new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("��¼����")
			.setMessage("�ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
			.create().show();
         }
        else{
        	new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("��¼ʧ��")
			.setMessage("�ʺŻ������벻��ȷ��\n������������룡")
			.create().show();
        }
    	
    	//��¼��ť
    	/*
      	Intent intent = new Intent();
		intent.setClass(Login.this,Whatsnew.class);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT).show();
		this.finish();*/
      }  
    public void login_back(View v) {     //������ ���ذ�ť
      	this.finish();
      }  
    public void login_pw(View v) {     //�������밴ť
    	Uri uri = Uri.parse("http://www.baidu.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
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
							// unsuccess
							if(message == null || message.equals("null")){
								Utils.loginInfo = AccountInfo.getAccountInfo(json);
								sp.edit().putString("userName", json.getString("username")).commit();
								sp.edit().putString("userPwd", json.getString("password")).commit();
								//Utils.getAccountInfo(Utils.loginInfo.getId());
								getAccountInfo(Utils.loginInfo.getId());
								showSomeThing("��½�ɹ�,��ӭ "+ json.getString("username"));
								instance.finish();
							}else{
								new AlertDialog.Builder(Login.this)
								.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
								.setTitle("��¼����")
								.setMessage(message)
								.create().show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						new AlertDialog.Builder(Login.this)
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
								final String url = Utils.userImg + Utils.self.getPic();
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Utils.selfPic = mImageLoader.getImageFromUrl(url);
									}
								}).start();
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
			}
		 }, "GETACCOUNTINFO")).start();
    }
    
    public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

}
