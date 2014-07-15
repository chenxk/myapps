package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.ScoreInfo;

import com.yuguan.bean.user.UserActActionBean;
import com.yuguan.util.HttpPostUtil;
import com.yuguan.util.Utils;

public class PingFenActivity extends Activity {

	private TextView pingfenName;
	private ScoreInfo actscore;
	private ScoreInfo mallscore;
	private EditText remark;
	private EditText mallremark;
	private LinearLayout submitScore;
	private LinearLayout cancleScore;
	private UserActActionBean bean;
	private PingFenActivity instance;

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

	private void init() {
		setContentView(R.layout.pingfen);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		bean = (UserActActionBean) this.getIntent().getSerializableExtra(
				"actActionBean");
		initView();
	}

	public void initView() {
		pingfenName = (TextView) findViewById(R.id.pingfenActionName);
		actscore = (ScoreInfo) findViewById(R.id.fullscore);
		mallscore = (ScoreInfo) findViewById(R.id.mallscore);
		remark = (EditText) findViewById(R.id.remark);
		mallremark = (EditText) findViewById(R.id.mallremark);
		submitScore = (LinearLayout) findViewById(R.id.submitScore);
		cancleScore = (LinearLayout) findViewById(R.id.cancleScore);

		if (bean != null) {
			pingfenName.setText(bean.getAname());
		}

		submitScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMsg();
			}
		});

		cancleScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				instance.finish();
			}
		});
	}

	public void sendMsg() {
		String msg = remark.getText().toString();
		if (msg.length() == 0) {
			showSomeThing("请输入活动评论");
			return;
		}

		String mall = mallremark.getText().toString();

		if (mall.length() == 0) {
			showSomeThing("请输入场馆评论");
			return;
		}

		int ascore = actscore.getScore();
		if (ascore == 0) {
			showSomeThing("请给活动打分");
			return;
		}

		int mscore = mallscore.getScore();
		if (mscore == 0) {
			showSomeThing("请给球馆打分");
			return;
		}

		// "aid="+aid+"&uid="+uid+"&actscore="+actScore+"&acttext="+actText+"&mallscore="+mallScore+"&malltext="+mallText
		String url = Utils.putactreviewUrl + "&aid=" + bean.getAid() + "&uid="
				+ Utils.loginInfo.getId() + "&actscore=" + ascore
				+ "&mallscore=" + mscore;

		NameValuePair content = new BasicNameValuePair("acttext", msg);
		NameValuePair conten2t = new BasicNameValuePair("malltext", mall);
		HttpPostUtil postUtil = new HttpPostUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("LOGINAJAX");
				if (result != null && !"服务访问失败".equals(result)) {
					instance.finish();
					showSomeThing("评分成功!");
					/*
					 * try { JSONObject json = new JSONObject(result); String
					 * status = json.getString("status"); if
					 * (status.equals("suc")) {
					 * 
					 * } else { showSomeThing("评分失败!"); } } catch (JSONException
					 * e) { e.printStackTrace(); }
					 */
				} else {
					showSomeThing("评分失败!");
				}
			}
		}, "LOGINAJAX");

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(content);
		pairs.add(conten2t);
		postUtil.setPairs(pairs);

		new Thread(postUtil).start();
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	public void doPingfenBack(View v) {
		this.finish();
	}

	public void doPingfenLogin(View v) {

	}
}
