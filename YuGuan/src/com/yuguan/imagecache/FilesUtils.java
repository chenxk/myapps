package com.yuguan.imagecache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.yuguan.util.ImageGetFromHttp.FlushedInputStream;
import com.yuguan.util.Utils;

public class FilesUtils {
	private Context mContext;

	public FilesUtils(Context context) {
		this.mContext = context;
	}
	
	CacheInfo cacheInfo = null;

	CacheInfo downloadImage(final String url) {
		/*new Thread(new Runnable() {
			@Override
			public void run() {}
		}).start();*/
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
			FilterInputStream fit = new FlushedInputStream(i);
			//return BitmapFactory.decodeStream(fit);
					Bitmap mp =  BitmapFactory.decodeStream(fit);//ImageGetFromHttp.downloadBitmap(url);
					int size = 0;
					try {
						cacheInfo = new CacheInfo(new URL(url), size, mp);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			
		return cacheInfo;
	}

	CacheInfo downloadImage(URL url) {
		CacheInfo cacheInfo = null;
		byte[] imageBuffer = (byte[]) null;
		BufferedInputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Utils.TIMEOUT);
			conn.setReadTimeout(Utils.TIMEOUT);
			conn.setInstanceFollowRedirects(true);
			InputStream inputStream = conn.getInputStream();
			if (inputStream == null) {
				showSomething("inputStream is null");
				return null;
			}
			is = new BufferedInputStream(inputStream);
			baos = new ByteArrayOutputStream();
			long size = 0L;
			int b = -1;
			while ((b = is.read()) != -1) {
				showSomething("byte:" + b);
				baos.write(b);
			}
			imageBuffer = baos.toByteArray();
			size = imageBuffer.length;
			Bitmap bm = BitmapFactory.decodeByteArray(imageBuffer, 0,
					imageBuffer.length);

			cacheInfo = new CacheInfo(url, size, bm);
		} catch (IOException e) {
			showSomething("下载问题:" + e.toString());
			Log.i(this.mContext.getPackageName(), url.toString()
					+ " is unavailable");
			return null;
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (Exception e) {
				showSomething("baos问题:" + e.toString());
				return null;
			}
		}
		return cacheInfo;
	}

	public void showSomething(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}
	
	public boolean saveImageToFile(Bitmap bitmap, String fileName) throws Exception{
		FileOutputStream outputStream = mContext.openFileOutput(fileName,  
                0);  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		String houzui = "";
		int index = fileName.lastIndexOf(".");
		houzui = fileName.substring(index+1).toLowerCase();
		if(houzui.equals(".jpg")){
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		if(houzui.equals(".png")){
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		}
        outputStream.write(baos.toByteArray());  
        outputStream.flush();  
        outputStream.close();  
        
		return false;
	}

	public synchronized boolean saveImage(Bitmap bitmap, String fileName) {
		boolean bool = false;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			bos = new BufferedOutputStream(this.mContext.openFileOutput(
					fileName, 0));
			baos = new ByteArrayOutputStream();
			/**/String houzui = "";
			int index = fileName.lastIndexOf(".");
			houzui = fileName.substring(index+1).toLowerCase();
			if(houzui.equals(".jpg")){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
			if(houzui.equals(".png")){
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			}
			
			bis = new BufferedInputStream(new ByteArrayInputStream(
					baos.toByteArray()));
			int b = -1;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
			bool = true;
		} catch (Exception e) {
			bool = false;
			Log.i(this.mContext.getPackageName(),
					"the local storage is not available");
			try {
				bos.close();
				bis.close();
			} catch (IOException e2) {
				bool = false;
				Log.i(this.mContext.getPackageName(),
						"the local storage is not available");
			}
		} finally {
			try {
				bos.close();
				bis.close();
			} catch (IOException e) {
				bool = false;
				Log.i(this.mContext.getPackageName(),
						"the local storage is not available");
			}
		}
		return bool;
	}

	public Bitmap readImage(String fileName) {
		Bitmap bm = null;
		InputStream is = null;
		try {
			FileInputStream inputStream = this.mContext.openFileInput(fileName);
			if(inputStream == null){
				showSomething("inputStream is null");
			}else{
				is = new BufferedInputStream(inputStream);
				bm = BitmapFactory.decodeStream(is);
				if(bm == null){
					showSomething("bm  is null");
				}
			}
		} catch (Exception e) {
			showSomething("read file " + e.toString());
		}
		return bm;
	}

	boolean deleteImage(String fileName) {
		return this.mContext.deleteFile(fileName);
	}
}