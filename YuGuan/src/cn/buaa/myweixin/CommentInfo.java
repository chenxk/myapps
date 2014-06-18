package cn.buaa.myweixin;

import com.yuguan.bean.CommentBean;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentInfo extends LinearLayout {

	private String commentUser;
	private String comment;
	private String commentTime;

	
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;

	private View view;
	
	
	public CommentInfo(Context context){
		super(context);
		initView(context);
	}
	
	
	public CommentInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		TypedArray typedarray = context.obtainStyledAttributes(attrs,
				R.styleable.CommentInfo);

		commentUser = typedarray.getString(R.styleable.CommentInfo_commentuser);
		comment = typedarray.getString(R.styleable.CommentInfo_comment);
		commentTime = typedarray.getString(R.styleable.CommentInfo_commenttime);
		
		typedarray.recycle();

		initView(context);

		textview1.setText(commentUser); // 设置第一行文字
		textview2.setText(comment); // 设置第二行文字
		textview3.setText(commentTime); // 设置第二行文字
		
	}
	
	
	public void initView(Context context){
		view = LayoutInflater.from(context).inflate(R.layout.comment_info, this, true);
		textview1 = (TextView) view.findViewById(R.id.commentUser);
		textview2 = (TextView) view.findViewById(R.id.comment);
		textview3 = (TextView) view.findViewById(R.id.commentTime);
	}
	
	public void initData(CommentBean bean){
		textview1.setText(bean.getuName()+":");
		textview2.setText(bean.getComment());
		textview3.setText(bean.getPostTime());
	}

}
