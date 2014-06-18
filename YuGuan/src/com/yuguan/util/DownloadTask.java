package com.yuguan.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.buaa.myweixin.MainWeixin;

import com.yuguan.imagecache.CacheInfo;

public class DownloadTask extends AsyncTask<String, Void, CacheInfo> {

	private ImageView mImageView;
	private LinearLayout layout;
	private int defultResId;
	private Context ctx;

	public DownloadTask(Context ctx,ImageView mImageView, int defultResId) {
		this.mImageView = mImageView;
		this.defultResId = defultResId;
		this.ctx = ctx;
	}

	public DownloadTask(Context ctx,LinearLayout layout, int defultResId) {
		this.ctx = ctx;
		this.layout = layout;
		this.defultResId = defultResId;
	}

	@Override
	protected CacheInfo doInBackground(String... params) {
		try {
			return MainWeixin.imageCacheManager.downlaodImageReInfo(new URL(
					params[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onPostExecute(CacheInfo result){
		super.onPostExecute(result);
		if (result != null) {
			if (mImageView != null) {
				mImageView.setImageBitmap(result.getValue());
			}
			
			if (layout != null) {
				FileInputStream is = null;
				try {
					is = ctx.openFileInput(result.getFileName());
					Drawable drawable = Drawable.createFromStream(is, null);
					layout.setBackgroundDrawable(drawable);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			showSomething("Download没有获得图片信息");
		}

	}

	@Override
	protected void onPreExecute() {
		if (mImageView != null) {
			mImageView.setImageResource(defultResId);
		}
		if (layout != null) {
			layout.setBackgroundResource(defultResId);
		}
		super.onPreExecute();
	}

}
