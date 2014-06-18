package com.yuguan.mycomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

public class MyTextView extends LinearLayout {

	private String text_name;
	private String text_value;
	private int text_color = 0xFFFF;
	private boolean singleLine;

	private TextView textview1;
	private TextView textview2;

	private View view;

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedarray = context.obtainStyledAttributes(attrs,
				R.styleable.MyTextView);

		text_color = typedarray.getResourceId(R.styleable.MyTextView_textcolor,
				0xfff);
		text_name = typedarray.getString(R.styleable.MyTextView_textname);
		// text_value = typedarray2.getString(R.styleable.MyTextView_textstr);
		singleLine = typedarray.getBoolean(R.styleable.MyTextView_singleline,
				true);
		typedarray.recycle();

		view = LayoutInflater.from(context).inflate(
				R.layout.mycomponent_layout, this, true);

		textview1 = (TextView) view.findViewById(R.id.attributeName);

		textview2 = (TextView) view.findViewById(R.id.attributeValue);

		textview1.setText(text_name); // 设置第一行文字
		textview2.setText(text_value); // 设置第二行文字
		textview2.setTextColor(text_color); // 设置背景色
		textview2.setSingleLine(singleLine);

	}

	public String getText_name() {
		return text_name;
	}

	public void setText_name(String text_name) {
		this.text_name = text_name;
		textview1.setText(text_name); // 设置第一行文字
	}

	public String getText_value() {
		return text_value;
	}

	public void setText_value(String text_value) {
		this.text_value = text_value;
		textview2.setText(text_value);
	}

	public int getText_color() {
		return text_color;
	}

	public void setText_color(int text_color) {
		this.text_color = text_color;
		textview2.setTextColor(text_color); // 设置背景色
		textview1.setTextColor(text_color); // 设置背景色
	}

	public boolean isSingleLine() {
		return singleLine;
	}

	public void setSingleLine(boolean singleLine) {
		this.singleLine = singleLine;
		textview2.setSingleLine(singleLine);
	}

}
