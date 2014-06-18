package cn.buaa.myweixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuguan.bean.UserBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.Utils;

public class UserImage extends LinearLayout {

	private RoundImageView headImg;
	private ImageView sexImg;
	private TextView nameText;

	private int userHead;
	private int userSex;
	private String userName;
	private View view;
	private ImageLoader mImageLoader;

	public UserImage(Context context) {
		super(context);
		initView(context);
	}

	public UserImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedarray = context.obtainStyledAttributes(attrs,
				R.styleable.UserInfo);

		userHead = typedarray.getResourceId(R.styleable.UserInfo_userimage,
				R.drawable.head1_128);
		userSex = typedarray.getResourceId(R.styleable.UserInfo_usersex,
				R.drawable.boy_24);
		userName = typedarray.getString(R.styleable.UserInfo_username);
		typedarray.recycle();

		initView(context);

	}

	public void initView(Context context) {
		mImageLoader = new ImageLoader(context);
		view = LayoutInflater.from(context).inflate(R.layout.usericon, this,
				true);
		headImg = (RoundImageView) view.findViewById(R.id.userImage);
		sexImg = (ImageView) view.findViewById(R.id.userSex);
		nameText = (TextView) view.findViewById(R.id.userName);
	}

	public void initData(UserBean bean) {
		
		String url = Utils.userImg + bean.getPic();
		mImageLoader.loadImage(url, null, headImg);
		if (bean.getSex() == 0) {
			sexImg.setImageResource(R.drawable.girl_32);
		}else{
			sexImg.setImageResource(R.drawable.boy_32);
		}
		nameText.setText(bean.getName());
	}

}
