package com.yuguan.mycomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class SysmsgInfo extends LinearLayout {

	private TextView sysmsg;
	private TextView sysSendTime;

	public SysmsgInfo(Context context) {
		super(context);
		initView(context);
	}

	public SysmsgInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.sysmsginfo, this, true);
		sysmsg = (TextView) findViewById(R.id.sysmsg);
		sysSendTime = (TextView) findViewById(R.id.sysSendTime);
	}

	public void initValue(String msg, String time) {
		sysmsg.setText(msg);
		sysSendTime.setText(time);
	}

}
