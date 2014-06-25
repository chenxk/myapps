package com.yuguan.activities;

import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FriendInfo extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friendinfo);
	}
	
	public void doBack(View v) {
		this.finish();
	}
	
	public void doLogin(View v) {
		Intent intent = new Intent(FriendInfo.this, Login.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		
	}
}
