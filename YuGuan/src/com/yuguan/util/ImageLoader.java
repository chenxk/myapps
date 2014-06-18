package com.yuguan.util;


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
import cn.buaa.myweixin.R;

public class ImageLoader {
	private static final String TAG = "ImageLoader";
	private static final int MAX_CAPACITY = 10;// һ����������ռ�
	private static final long DELAY_BEFORE_PURGE = 1000 * 1000;// ��ʱ������

	// 0.75�Ǽ�������Ϊ����ֵ��true���ʾ��������������ĸߵ�����false���ʾ���ղ���˳������
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// ������һ��������ֵ��ʱ�򣬽��ϵ�ֵ��һ������ᵽ��������
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};
	// �������棬���õ�����Ӧ�ã�ֻ�����ڴ�Խ���ʱ����Ӧ�òŻᱻ���գ���Ч�ı�����oom
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);
	
	
	private ImageFileCache imageFileCache;
	
	private Context context;

	public ImageLoader(Context context) {
		this.context = context;
		imageFileCache = new ImageFileCache();
	}

	// ��ʱ������
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	// ���û��������timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * ������
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	/**
	 * ���ػ��棬���û���򷵻�null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// ��һ����������
		if (bitmap != null) {
			return bitmap;
		}
		
		bitmap = getFromSecondLevelCache(url);// �Ӷ�����������
		if(bitmap != null)
			return bitmap;
		
		bitmap = imageFileCache.getImage(url);// ���ļ�����
		if(bitmap != null)
			addImage2Cache(url, bitmap);
		return bitmap;
	}
	
	/**
	 * �����ļ�������
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
	 * �Ӷ�����������
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// �����ڴ�Խ����������Ѿ���gc������
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * ��һ����������
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// ��������ʵ�Ԫ�طŵ�����ͷ���������һ�η��ʸ�Ԫ�صļ����ٶȣ�LRU�㷨��
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}

	/**
	 * ����ͼƬ������������о�ֱ�Ӵӻ������ã�������û�о�����
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, ImageView imageView) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// �ӻ����ж�ȡ
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.action_list_back);//����û����ΪĬ��ͼƬ
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, imageView,null);
		} else {
			imageView.setImageBitmap(bitmap);//��Ϊ����ͼƬ
		}
	}
	
	/**
	 * ����ͼƬ������������о�ֱ�Ӵӻ������ã�������û�о�����
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, LinearLayout layout) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// �ӻ����ж�ȡ
		if (bitmap == null) {
			layout.setBackgroundResource(R.drawable.action_list_back);//����û����ΪĬ��ͼƬ
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter,null,layout);
		} else {
			BitmapDrawable bd=new BitmapDrawable(bitmap);
			layout.setBackgroundDrawable(bd);//��Ϊ����ͼƬ
		}
	}

	/**
	 * ���뻺��
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
		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			if(params[1] != null)
				adapter = (BaseAdapter) params[1];
			if(params[2] != null)
				imageView = (ImageView) params[2];
			if(params[3] != null)
				layout = (LinearLayout) params[3];/**/
			Bitmap drawable = loadImageFromInternet(url);// ��ȡ����ͼƬ
			return drawable;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return;
			}
			addImage2Cache(url, result);// ���뻺��
			if(!savePicAsFile(result, url)){
				//showSomething("���汾��ʧ��");
			}
			if(imageView != null){
				imageView.setImageBitmap(result);
			}
			if(layout != null){
				BitmapDrawable bd = new BitmapDrawable(result);
				layout.setBackgroundDrawable(bd);
			}/**/
			//showSomething(url);
			if(adapter != null){
				adapter.notifyDataSetChanged();// ����getView����ִ�У����ʱ��getViewʵ���ϻ��õ��ոջ���õ�ͼƬ
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
