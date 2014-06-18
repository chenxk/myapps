package com.yuguan.util;

import java.net.URL;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.buaa.myweixin.MainWeixin;

import com.yuguan.imagecache.CacheInfo;

public class ImageDownload implements Runnable{

	private String url;
	private Handler handler;
	private Context ctx;
	
	public ImageDownload(String url, Handler handler, Context ctx) {
		super();
		this.url = url;
		this.handler = handler;
		this.ctx = ctx;
	}

	@Override
	public void run() {
		try {
			CacheInfo cacheInfo = MainWeixin.imageCacheManager
					.downlaodImageReInfo(new URL(url));
			if (cacheInfo != null) {
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("FILENAME",
						cacheInfo.getFileName());
				message.setData(bundle);
				handler.sendMessage(message);
			}	
		} catch (Exception e) {
			showSomething("URL  " + e.toString());
		}
			
	}
	
	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

}
