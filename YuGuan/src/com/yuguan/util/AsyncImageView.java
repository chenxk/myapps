package com.yuguan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yuguan.util.ImageLoader.ImageLoadTask;

/**
 * �첽ͼƬ�ؼ� 
 * ʹ�ã�new AsyncImageView().asyncLoadBitmapFromUrl("http://xxxx","����·��"){
 * 
 * @author gaoomei@gmail.com
 * @site http://obatu.sinaapp.com
 * @version 1.0
 * @2011-12-3
 */
public class AsyncImageView extends ImageView {

	/**
	 * �첽task������
	 */
	private ImageLoadTask mAsyncLoad;

	/**
	 * ���ػ�����ͼƬ������ʱ�䣬��λ����(s),Ĭ��30����
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
	 * ��д���漸������ͼƬ��Դ�ķ�����Ŀ����ȡ���������
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
	 * ȡ�����ڽ��е��첽task
	 */
	public void cancelLoad() {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
			mAsyncLoad = null;
		}
	}

	/**
	 * ����ͼƬ���ʱ��
	 * 
	 * @param second
	 *            ���ʱ�䣬��λ���롿���������0��null���򲻻���
	 */
	public void setCacheLiveTime(long second) {
		if (second == 0) {
			this.mCacheLiveTime = 0;
		} else if (second >= 0) {
			this.mCacheLiveTime = second * 1000;
		}
	}

	/**
	 * �������첽����
	 * 
	 * @param url
	 * @param saveFileName
	 */
	public void asyncLoadBitmapFromUrl(String url) {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
		}
		// AsyncTask�������ã�����ÿ������ʵ��
		mAsyncLoad = new ImageLoader(context).new ImageLoadTask();
		mAsyncLoad.execute(url,null,this,null);
	}
	
}

