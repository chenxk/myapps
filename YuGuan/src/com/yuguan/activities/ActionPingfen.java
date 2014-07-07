package com.yuguan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

import com.yuguan.util.RoundImageView;

public class ActionPingfen extends Activity implements OnClickListener {

	private TextView accountName;
	private TextView accountAddress;
	private RoundImageView accountImg;
	private ImageView accountSex;
	private LinearLayout msgcenter;
	private LinearLayout mysport;
	private LinearLayout myfriends;
	private LinearLayout myshoucang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pingfen);

		initView();

	}

	private void initView() {
		accountName = (TextView) findViewById(R.id.accountName);
		accountImg = (RoundImageView) findViewById(R.id.accountImg);
		accountSex = (RoundImageView) findViewById(R.id.accountSex);
		accountAddress = (TextView) findViewById(R.id.accountAddress);
		msgcenter = (LinearLayout) findViewById(R.id.msgcenter);
		mysport = (LinearLayout) findViewById(R.id.mysport);
		myfriends = (LinearLayout) findViewById(R.id.myfriends);
		myshoucang = (LinearLayout) findViewById(R.id.myshoucang);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msgcenter:
			doMsgcenter(v);
			break;
		case R.id.mysport:
			doMysports(v);
			break;
		case R.id.myfriends:
			doMyfriends(v);
			break;
		case R.id.myshoucang:
			doMyshoucang(v);
			break;
		case R.id.account_back:
			doBack(v);
			break;

		default:
			break;
		}
	}

	public void doMsgcenter(View v) {
		Intent intent = new Intent(ActionPingfen.this, MessageCenter.class);
		startActivity(intent);
	}
	
	public void doMysports(View v) {
	}
	
	public void doMyfriends(View v) {
	}
	
	public void doMyshoucang(View v) {
	}


	public void doBack(View v) {
		this.finish();
	}

	public void doLogin(View v) {
		Intent intent = new Intent(ActionPingfen.this, Login.class);
		startActivity(intent);
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
}
