package cn.buaa.myweixin;

import org.json.JSONException;
import org.json.JSONObject;

import com.yuguan.util.HttpUtil;
import com.yuguan.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);
        
    }

    public void login_mainweixin(View v) {
    	
    	
    	if(!"".equals(mUser.getText().toString()) && !"".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
        {
    		 String url = Utils.loginUrl + mUser.getText().toString() + "&password=" + mPassword.getText().toString();
    		 new Thread(new HttpUtil(url,
    					new Handler(){
    			 @Override
    			public void handleMessage(Message msg) {
    				 super.handleMessage(msg);
    					Bundle data = msg.getData();
    					String result = data.getString("LOGINAJAX");
    					if (result != null && !"�������ʧ��".equals(result)) {
    						/* Intent intent = new Intent();
    			             intent.setClass(Login.this,MainWeixin.class);
    			             startActivity(intent);*/
    						try {
								JSONObject json = new JSONObject(result);
								String message = null;
								try {
									message = json.getString("message");
								} catch (Exception e) {
								}
								// success
								if(message == null){
									Utils.loginInfo = json;
									
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
}
