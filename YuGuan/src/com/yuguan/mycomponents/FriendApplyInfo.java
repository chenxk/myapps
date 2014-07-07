package com.yuguan.mycomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class FriendApplyInfo extends LinearLayout {

	private TextView friendmsg;
	private TextView applySendTime;
	private LinearLayout ok;
	private LinearLayout no;

	public FriendApplyInfo(Context context) {
		super(context);
		initView(context);
	}

	public FriendApplyInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.friendapplyinfo, this,
				true);
		friendmsg = (TextView) findViewById(R.id.friendmsg);
		applySendTime = (TextView) findViewById(R.id.applySendTime);
		ok = (LinearLayout) findViewById(R.id.btn_ok);

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		no = (LinearLayout) findViewById(R.id.btn_no);

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initValue(String msg, String time) {
		friendmsg.setText(msg);
		applySendTime.setText(time);
	}

}
