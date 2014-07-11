package com.yuguan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.ScoreInfo;

public class PingFenActivity extends Activity {

	private TextView pingfenName;
	private ScoreInfo fullscore;
	private EditText remark;
	private LinearLayout submitScore;
	private LinearLayout cancleScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingfen);

		initView();
	}

	public void initView() {
		pingfenName = (TextView) findViewById(R.id.pingfenActionName);
		fullscore = (ScoreInfo) findViewById(R.id.fullscore);
		remark = (EditText) findViewById(R.id.remark);
		submitScore = (LinearLayout) findViewById(R.id.submitScore);
		cancleScore = (LinearLayout) findViewById(R.id.cancleScore);
	}

	public void doPingfenBack(View v) {
		this.finish();
	}

	public void doPingfenLogin(View v) {
	}
}
