package com.yuguan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yuguan.util.ImageLoader.ImageLoadTask;

/**
 * 异步图片控件 
 * 使用：new AsyncImageView().asyncLoadBitmapFromUrl("http://xxxx","缓存路径"){
 * 
 * @author gaoomei@gmail.com
 * @site http://obatu.sinaapp.com
 * @version 1.0
 * @2011-12-3
 */
public class AsyncImageView extends ImageView {

	/**
	 * 异步task加载器
	 */
	private ImageLoadTask mAsyncLoad;

	/**
	 * 下载回来的图片缓存存活时间，单位：秒(s),默认30分钟
	 */
	private long mCacheLiveTime = 1800;
	private Context context;

	public AsyncImageView(Context context) {
		super(context);
		this.context = context;
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
			mAsyncLoad = null;
		}
		super.setImageDrawable(drawable);
	}

	/**
	 * 重写下面几个设置图片资源的方法，目地是取消网络加载
	 */
	@Override
	public void setImageResource(int resId) {
		cancelLoad();
		super.setImageResource(resId);
	}

	@Override
	public void setImageURI(Uri uri) {
		cancelLoad();
		super.setImageURI(uri);
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		cancelLoad();
		super.setImageBitmap(bitmap);
	}

	/**
	 * 取消正在进行的异步task
	 */
	public void cancelLoad() {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
			mAsyncLoad = null;
		}
	}

	/**
	 * 设置图片存活时间
	 * 
	 * @param second
	 *            存活时间，单位【秒】，如果等于0或null，则不缓存
	 */
	public void setCacheLiveTime(long second) {
		if (second == 0) {
			this.mCacheLiveTime = 0;
		} else if (second >= 0) {
			this.mCacheLiveTime = second * 1000;
		}
	}

	/**
	 * 从网络异步加载
	 * 
	 * @param url
	 * @param saveFileName
	 */
	public void asyncLoadBitmapFromUrl(String url) {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
		}
		// AsyncTask不可重用，所以每次重新实例
		mAsyncLoad = new ImageLoader(context).new ImageLoadTask();
		mAsyncLoad.execute(url,null,this,null);
	}
	
}

