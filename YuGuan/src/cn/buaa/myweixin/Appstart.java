package cn.buaa.myweixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.yuguan.util.AsyncImageLoader;

public class Appstart extends Activity {

	private AsyncImageLoader asyncImageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ȫ����ʾ
		// Toast.makeText(getApplicationContext(), "���ӣ��úñ��У�",
		// Toast.LENGTH_LONG).show();
		// overridePendingTransition(R.anim.hyperspace_in,
		// R.anim.hyperspace_out);
		asyncImageLoader = new AsyncImageLoader();
		setContentView(R.layout.appstart);
		/*
		final ImageView imag = (ImageView) findViewById(R.id.welcomeImg);
		
		String url = "http://192.168.10.106:8080/yuqiu/images/action/m_1399812448626.JPG";
		
		Drawable cachedImage = asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                if (imag != null) {
                	imag.setImageDrawable(imageDrawable);
                }
            }
        });
		if (cachedImage == null) {
			imag.setImageResource(R.drawable.welcome);
		}else{
			imag.setImageDrawable(cachedImage);
		}*/
		//new DownloadTask(this,imag, R.drawable.welcome).execute("http://192.168.10.106:8080/yuqiu/images/action/m_1399812448626.JPG");
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// WhatsnewDoor
				Intent intent = new Intent(Appstart.this, MainWeixin.class);
				startActivity(intent);
				Appstart.this.finish();
			}
		}, 1000);
	}
}