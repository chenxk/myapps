package com.yuguan.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ImageLoader {
	private static final String TAG = "ImageLoader";
	private static final int MAX_CAPACITY = 50;// 一级缓存的最大空间
	private static final long DELAY_BEFORE_PURGE = 1000 * 60;// 定时清理缓存

	// 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};
	// 二级缓存，采用的是软应用，只有在内存吃紧的时候软应用才会被回收，有效的避免了oom
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);
	
	
	public ImageFileCache imageFileCache;
	
	private Context context;

	public ImageLoader(Context context) {
		this.context = context;
		imageFileCache = new ImageFileCache();
	}
	
	public ImageLoader(){
		
	}

	// 定时清理缓存
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	// 重置缓存清理的timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * 清理缓存
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	/**
	 * 
	 * 返回缓存，如果没有则返回null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// 从一级缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		
		bitmap = getFromSecondLevelCache(url);// 从二级缓存中拿
		if(bitmap != null)
			return bitmap;
		
		bitmap = imageFileCache.getImage(url);// 从文件加载
		if(bitmap != null)
			addImage2Cache(url, bitmap);
		return bitmap;
	}
	
	/**
	 * 保存文件到本地
	 * @param bt
	 * @param url
	 */
	private boolean savePicAsFile(Bitmap bt,String url){
		Bitmap bitmap = imageFileCache.getImage(url);
		if(bitmap == null && bt != null){
			return imageFileCache.saveBitmap(bt, url);
		}
		return false;
	}

	/**
	 * 从二级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * 从一级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}
	
	public Bitmap getImageFromUrl(String url){
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			bitmap = loadImageFromInternet(url);
			if(bitmap == null){
				addImage2Cache(url, bitmap);// 放入缓存
				savePicAsFile(bitmap, url);
			}
		}
		return (bitmap);//
	}
	
	public boolean saveImageToFile(String url,Bitmap bitmap){
		addImage2Cache(url, bitmap);// 放入缓存
		return savePicAsFile(bitmap, url);
	}
	
	public File getImageFile(String url){
		return imageFileCache.getImageFile(url);
	}
	

	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, ImageView imageView) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, imageView,null);
		} else {
			imageView.setImageBitmap(bitmap);//设为缓存图片
		}
	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, LinearLayout layout) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter,null,layout);
		} else {
			BitmapDrawable bd=new BitmapDrawable(bitmap);
			layout.setBackgroundDrawable(bd);//设为缓存图片
		}
	}

	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(url, value);
		}
	}

	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		String url;
		BaseAdapter adapter;
		ImageView imageView;
		LinearLayout layout;
		
		/**
		 * 是否取消
		 */
		private boolean isCancel = false;
		
		@Override
		protected void onCancelled() {
			System.out.println("async load imgae cancel");
			isCancel = true;
		}
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			if (isCancel) {
				return null;
			}
			url = (String) params[0];
			if(params[1] != null)
				adapter = (BaseAdapter) params[1];
			if(params[2] != null)
				imageView = (ImageView) params[2];
			if(params[3] != null)
				layout = (LinearLayout) params[3];/**/
			Bitmap drawable = loadImageFromInternet(url);// 获取网络图片
			return drawable;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (isCancel) {
				return ;
			}
			if (result == null) {
				return;
			}
			addImage2Cache(url, result);// 放入缓存
			if(!savePicAsFile(result, url)){
				//showSomething("保存本地失败");
			}
			if(imageView != null){
				if(imageView.getTag().equals(url))
					imageView.setImageBitmap(result);
			}
			if(layout != null){
				BitmapDrawable bd = new BitmapDrawable(result);
				layout.setBackgroundDrawable(bd);
			}/**/
			//showSomething(url);
			if(adapter != null){
				adapter.notifyDataSetChanged();// 触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片
			}
		}
	}
	
	public void showSomething(String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public Bitmap loadImageFromInternet(String url) {
		Bitmap bitmap = null;
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				Log.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}

}
