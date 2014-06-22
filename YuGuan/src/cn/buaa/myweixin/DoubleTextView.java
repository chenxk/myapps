package cn.buaa.myweixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoubleTextView extends LinearLayout {

	
	private String valueOne;
	private String valueTwo;
	
	private TextView view1;
	private TextView view2;
	
	private View view;
	
	public DoubleTextView(Context context) {
		super(context);
	}

	public DoubleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray typedarray = context.obtainStyledAttributes(attrs,R.styleable.DoubleTextView);
		
		valueOne = typedarray.getString(R.styleable.DoubleTextView_valueone);
		valueTwo = typedarray.getString(R.styleable.DoubleTextView_valuetwo);
		typedarray.recycle();
		
		
		view = LayoutInflater.from(context).inflate(R.layout.double_textview,this,true);
		
		view1 = (TextView) view.findViewById(R.id.valueone);
		view2 = (TextView) view.findViewById(R.id.valuetwo);
		
		
		view1.setText(valueOne);
		view2.setText(valueTwo);
	}

	public String getValueTwo() {
		return valueTwo;
	}

	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
		view2.setText(valueTwo);
	}
	


}
