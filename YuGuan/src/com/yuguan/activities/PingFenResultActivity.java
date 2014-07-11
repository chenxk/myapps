package com.yuguan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class PingFenResultActivity extends Activity {

	
	private TextView pingfenName;
	private LinearLayout submitScore;
	private LinearLayout cancleScore;
	private RefreshListView allFrdScoreList;
	private TextView guanjun;
	private TextView yajun;
	private TextView jijun;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingfenres);
		
		initView();
	}
	
	
	public void initView(){
		pingfenName = (TextView) findViewById(R.id.pingfenresName);
		allFrdScoreList = (RefreshListView) findViewById(R.id.allFrdScoreList);
		guanjun = (TextView) findViewById(R.id.guanjun);
		yajun = (TextView) findViewById(R.id.yajun);
		jijun = (TextView) findViewById(R.id.jijun);
		submitScore = (LinearLayout) findViewById(R.id.submitResScore);
		cancleScore = (LinearLayout) findViewById(R.id.cancleResScore);
	}
	
	
	
	
	public void doPingfenResBack(View v){
		this.finish();
	}
	
	
	public void doPingfenResLogin(View v){
	}
}
