package com.yuguan.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class ImageGetFromHttp {
	private static final String LOG_TAG = "ImageGetFromHttp";
	public static Context ctx;
	public static  Bitmap downloadBitmap(String url) {
		int TIME_OUT_DELAY = 10000;
		final HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				TIME_OUT_DELAY); // 超时设置
		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// 连接超时
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(LOG_TAG, "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				showSomething("Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					if(inputStream != null){
						FilterInputStream fit = new FlushedInputStream(inputStream);
						return BitmapFactory.decodeStream(fit);
					}else{
						showSomething("inputStream is null");
					}
				} finally {
					if (inputStream != null) {
						inputStream.close();
						inputStream = null;
					}
					entity.consumeContent();
				}
			}else{
				showSomething("entity is null");
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
			showSomething("I/O error while retrieving bitmap from " + url);
		} catch (IllegalStateException e) {
			getRequest.abort();
			showSomething("Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			showSomething("Error while retrieving bitmap from " + e.toString());
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}
	
	public static void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	public static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}
