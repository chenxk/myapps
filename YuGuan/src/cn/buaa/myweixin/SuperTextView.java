package cn.buaa.myweixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuperTextView extends LinearLayout {

	private String text_name;
	private int text_color = 0x000;
	private boolean singleLine;
	private float text_size = 15;

	private String text_name2;
	private int text_color2 = 0x000;
	private boolean singleLine2;
	private float text_size2 = 15;

	private TextView textview1;
	private TextView textview2;

	private View view;

	public SuperTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedarray = context.obtainStyledAttributes(attrs,
				R.styleable.MyTextView);

		text_color = typedarray.getResourceId(R.styleable.MyTextView_textcolor,
				0x00);
		text_name = typedarray.getString(R.styleable.MyTextView_textname);
		singleLine = typedarray.getBoolean(R.styleable.MyTextView_singleline,
				true);
		text_size = typedarray.getFloat(R.styleable.MyTextView_textsize, 15);

		text_color2 = typedarray.getResourceId(
				R.styleable.MyTextView_textcolor2, 0x00);
		text_name2 = typedarray.getString(R.styleable.MyTextView_textname2);
		singleLine2 = typedarray.getBoolean(R.styleable.MyTextView_singleline2,
				true);
		text_size2 = typedarray.getFloat(R.styleable.MyTextView_textsize2, 15);
		
		typedarray.recycle();

		view = LayoutInflater.from(context).inflate(
				R.layout.mycomponent_layout, this, true);

		textview1 = (TextView) view.findViewById(R.id.attributeName);
		textview2 = (TextView) view.findViewById(R.id.attributeValue);

		textview1.setText(text_name); // 设置第一行文字
		textview1.setTextColor(text_color); // 设置背景色
		textview1.setSingleLine(singleLine);
		textview1.setTextSize(TypedValue.COMPLEX_UNIT_SP, text_size);

		textview2.setText(text_name2); // 设置第二行文字
		textview2.setTextColor(text_color2); // 设置背景色
		textview2.setSingleLine(singleLine2);
		textview2.setTextSize(TypedValue.COMPLEX_UNIT_SP, text_size2);
	}

}
