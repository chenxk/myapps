package com.yuguan.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.buaa.myweixin.Login;
import cn.buaa.myweixin.R;

public class MallInfo extends Activity implements OnClickListener {

	private LinearLayout horizontalScrollView;
	private ImageView bigImage;
	private List<Integer> inds = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mallinfo);
		try {
			horizontalScrollView = (LinearLayout) findViewById(R.id.horizontalImages);
			bigImage = (ImageView) findViewById(R.id.bigImage);
			initImgs();
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}
		
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void initImgs() {
		horizontalScrollView.removeAllViews();
		inds.clear();
		inds.add(R.drawable.action_list_back);
		inds.add(R.drawable.playyumao);
		inds.add(R.drawable.action_list_back);
		inds.add(R.drawable.playyumao);
		inds.add(R.drawable.action_list_back);
		inds.add(R.drawable.playyumao);

		int count = inds.size();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150*2,
				100*2);
		for (int i = 0; i < count; i++) {
			ImageView img = new ImageView(this);
			img.setPadding(10, 10, 10, 10);
			img.setScaleType(ScaleType.FIT_XY);
			img.setBackgroundResource(R.drawable.radio_buttong_bg);
			img.setId(i);
			img.setImageResource(inds.get(i));
			if(i == 0){
				img.setSelected(true);
			}
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int count = horizontalScrollView.getChildCount();

					for (int i = 0; i < count; i++) {
						View localView = horizontalScrollView.getChildAt(i);

						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							bigImage.setImageResource(inds.get(i));
						}
					}

				}
			});
			horizontalScrollView.addView(img, i, params);
		}
	}

	public void showSomeThing(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {

	}

	public void back(View v) {
		this.finish();
	}

	public void dologin(View v) {
		Intent intent = new Intent(MallInfo.this, Login.class);
		startActivity(intent);
	}

}
