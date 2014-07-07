package com.yuguan.mycomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class SportApplyInfo extends LinearLayout {

	private TextView friendfrom;
	private TextView sportname;
	private TextView sportSendTime;
	private LinearLayout ok;
	private LinearLayout no;

	public SportApplyInfo(Context context) {
		super(context);
		initView(context);
	}

	public SportApplyInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.sportapplyinfo, this,
				true);
		friendfrom = (TextView) findViewById(R.id.friendfrom);
		sportname = (TextView) findViewById(R.id.sportname);
		sportSendTime = (TextView) findViewById(R.id.sportSendTime);
		ok = (LinearLayout) findViewById(R.id.but_ok);

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		no = (LinearLayout) findViewById(R.id.but_no);

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initValue(String name,String sport, String time) {
		friendfrom.setText(name);
		sportname.setText(sport);
		sportSendTime.setText(time);
	}

}
