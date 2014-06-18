package com.yuguan.imagecache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.List;
import java.util.Random;

public class ImageCacheManager {
	private DBClient dbClient;
	private static ImageCacheManager instance = null;
	private Context mContext;
	public FilesUtils filesUtils;
	private int mode = 1;

	private int max_Count = 20;

	private long delay_Millisecond = 259200000L;//

	private long max_Memory = 1024*1024*50L;//50M
	
	public static final int MODE_LEAST_RECENTLY_USED = 0;
	public static final int MODE_FIXED_TIMED_USED = 1;
	public static final int MODE_FIXED_MEMORY_USED = 2;
	public static final int MODE_NO_CACHE_USED = 3;

	private ImageCacheManager(Context context, int mode, String tag) {
		this.mode = mode;
		this.mContext = context;
		this.filesUtils = new FilesUtils(context);
		if (mode == 0)
			this.dbClient = new DBClient(this.mContext, "least_recently_used",
					tag);
		else if (mode == 1)
			this.dbClient = new DBClient(this.mContext, "fixed_timed_used", tag);
		else if (mode == 2)
			this.dbClient = new DBClient(this.mContext, "fixed_memory_used",
					tag);
	}

	public static synchronized ImageCacheManager getImageCacheService(
			Context context, int mode, String tag) {
		if (instance == null) {
			instance = new ImageCacheManager(context, mode, tag);
		}
		return instance;
	}
	
	public CacheInfo downlaodImageReInfo(URL url) {
		CacheInfo cacheInfo = null;
		switch (this.mode) {
		case 0:
			cacheInfo = new LRU(url).execute();
			break;
		case 1:
			cacheInfo = new FTU(url).execute();
			break;
		case 2:
			cacheInfo = new FMU(url).execute();
			break;
		case 3:
			cacheInfo = this.filesUtils.downloadImage(url);
			break;
		}
		return cacheInfo;
	}
	
	public void showSomething(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}

	public Bitmap downlaodImage(URL url) {
		Bitmap bitmap = null;
		CacheInfo cacheInfo = null;
		switch (this.mode) {
		case 0:
			cacheInfo = new LRU(url).execute();
			if (cacheInfo == null)
				break;
			bitmap = cacheInfo.getValue();

			break;
		case 1:
			cacheInfo = new FTU(url).execute();
			if (cacheInfo == null)
				break;
			bitmap = cacheInfo.getValue();

			break;
		case 2:
			cacheInfo = new FMU(url).execute();
			if (cacheInfo == null)
				break;
			bitmap = cacheInfo.getValue();

			break;
		case 3:
			cacheInfo = this.filesUtils.downloadImage(url);
			if (cacheInfo == null)
				break;
			bitmap = cacheInfo.getValue();
		}

		return bitmap;
	}

	public void setMax_num(int max_num) {
		this.max_Count = max_num;
	}

	public void setDelay_millisecond(long delay_millisecond) {
		this.delay_Millisecond = delay_millisecond;
	}

	public void setMax_Memory(long max_Memory) {
		this.max_Memory = max_Memory;
	}

	private class FMU implements IDownload {
		URL url = null;

		FMU(URL url) {
			this.url = url;
		}
		CacheInfo cacheInfo = null;
		public CacheInfo execute() {
			final SQLiteDatabase db = ImageCacheManager.this.dbClient.getSQLiteDatabase();
			db.beginTransaction();
			cacheInfo = null;
			try {
				cacheInfo = ImageCacheManager.this.dbClient.select(this.url.toString(), db);
				final List<CacheInfo> cacheInfos = ImageCacheManager.this.dbClient.selectAll(db);
				if (cacheInfo == null) {
					showSomething("数据库中没有记录");
					
					final Handler handler = new Handler() {
			             public void handleMessage(Message message) {
			            	  cacheInfo = (CacheInfo)message.obj;
			            	  if (cacheInfo == null) {
									showSomething("没有从网络下载到");
									return ;
								}
			            	  if (cacheInfo.getFileSize() > ImageCacheManager.this.max_Memory) {
									showSomething("图片过大");
									Log.i(ImageCacheManager.this.mContext.getPackageName(),
											"the image resource"
													+ cacheInfo.getUrl().toString()
													+ " need more  storage than "
													+ ImageCacheManager.this.max_Memory
													+ "B");
								} else {
									if ((cacheInfos != null) && (cacheInfos.size() > 0)) {
										showSomething("cacheInfos");
										long sumSize = 0L;
										while (cacheInfos.size() > 0) {
											int i = 0;
											for (int size = cacheInfos.size(); i < size; i++) {
												CacheInfo tempCache = (CacheInfo) cacheInfos
														.get(i);
												sumSize += tempCache.getFileSize();
											}
											if (sumSize + cacheInfo.getFileSize() <= ImageCacheManager.this.max_Memory) {
												break;
											}
											CacheInfo deleteCacheInfo = maxSize(cacheInfos);
											if (!ImageCacheManager.this.dbClient
													.delete(deleteCacheInfo.getUrl()
															.toString(), db))
												continue;
											ImageCacheManager.this.filesUtils
													.deleteImage(deleteCacheInfo
															.getFileName());
											cacheInfos.remove(deleteCacheInfo);
										}
									}

									if (ImageCacheManager.this.dbClient.insert(cacheInfo,
											db)){
										showSomething("insert");
										if(cacheInfo.getValue() != null){
											ImageCacheManager.this.filesUtils.saveImage(
													cacheInfo.getValue(),
													cacheInfo.getFileName());
										}else{
											showSomething("bitmap is null");
										}
										
									}
										
								}
			             }
			         };
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							cacheInfo = ImageCacheManager.this.filesUtils.downloadImage(url.toString());
							 Message message = handler.obtainMessage(0, cacheInfo);
			                 handler.sendMessage(message);
						}
					}).start();
					
					/*if (cacheInfo == null) {
						showSomething("没有从网络下载到");
						return null;
					}*/
					
				} else {
					Bitmap bitmap = ImageCacheManager.this.filesUtils
							.readImage(cacheInfo.getFileName());
					if (bitmap != null)
						{
						showSomething("bitmap");
						cacheInfo.setValue(bitmap);
						}
				}
			} finally {
				db.endTransaction();
			}
			db.endTransaction();

			return cacheInfo;
		}

		private CacheInfo maxSize(List<CacheInfo> cacheInfos) {
			long max = ((CacheInfo) cacheInfos.get(0)).getFileSize();
			CacheInfo deleteCache = (CacheInfo) cacheInfos.get(0);
			int i = 0;
			for (int size = cacheInfos.size(); i < size; i++) {
				CacheInfo tempCache = (CacheInfo) cacheInfos.get(i);
				if (tempCache.getFileSize() > max) {
					deleteCache = tempCache;
				}
			}
			return deleteCache;
		}
	}

	private class FTU implements IDownload {
		URL url = null;

		FTU(URL url) {
			this.url = url;
		}

		public CacheInfo execute() {
			CacheInfo cacheInfo = null;
			SQLiteDatabase db = ImageCacheManager.this.dbClient
					.getSQLiteDatabase();
			db.beginTransaction();
			try {
				cacheInfo = ImageCacheManager.this.dbClient.select(
						this.url.toString(), db);
				if (cacheInfo == null) {
					cacheInfo = ImageCacheManager.this.filesUtils
							.downloadImage(this.url);
					if (cacheInfo == null)
						return null;
					if (ImageCacheManager.this.dbClient.insert(cacheInfo, db))
						ImageCacheManager.this.filesUtils.saveImage(
								cacheInfo.getValue(), cacheInfo.getFileName());
				} else {
					ImageCacheManager.this.dbClient
							.update(System.currentTimeMillis(),
									this.url.toString(), db);
					Bitmap bitmap = ImageCacheManager.this.filesUtils
							.readImage(cacheInfo.getFileName());
					cacheInfo.setValue(bitmap);
				}
				List<CacheInfo> cacheInfos = ImageCacheManager.this.dbClient.selectAll(db);
				if (cacheInfos != null) {
					int i = 0;
					for (int size = cacheInfos.size(); i < size; i++) {
						CacheInfo tempCache = (CacheInfo) cacheInfos.get(i);

						if ((tempCache.getCreatAt()
								+ ImageCacheManager.this.delay_Millisecond >= System
									.currentTimeMillis())
								|| (!ImageCacheManager.this.dbClient.delete(
										tempCache.getUrl().toString(), db)))
							continue;
						ImageCacheManager.this.filesUtils.deleteImage(tempCache
								.getFileName());
					}
				}

			} finally {
				db.endTransaction();
			}
			db.endTransaction();

			return cacheInfo;
		}
	}

	private class LRU implements IDownload {
		URL url = null;

		LRU(URL url) {
			this.url = url;
		}

		public CacheInfo execute() {
			SQLiteDatabase db = ImageCacheManager.this.dbClient
					.getSQLiteDatabase();
			db.beginTransaction();
			CacheInfo cacheInfo = null;
			try {
				cacheInfo = ImageCacheManager.this.dbClient.select(
						this.url.toString(), db);
				List<CacheInfo> cacheInfos = ImageCacheManager.this.dbClient.selectAll(db);
				if (cacheInfo != null) {
					Bitmap bitmap = ImageCacheManager.this.filesUtils
							.readImage(cacheInfo.getFileName());
					cacheInfo.setValue(bitmap);
					int i = 0;
					for (int size = cacheInfos.size(); i < size; i++) {
						CacheInfo temp = (CacheInfo) cacheInfos.get(i);
						if (this.url.toString()
								.equals(temp.getUrl().toString()))
							ImageCacheManager.this.dbClient.update(0,
									this.url.toString(), db);
						else
							ImageCacheManager.this.dbClient.update(temp
									.getUsetimes() + 1, temp.getUrl()
									.toString(), db);
					}
				} else {
					cacheInfo = ImageCacheManager.this.filesUtils
							.downloadImage(this.url);
					if (cacheInfo == null)
						return null;
					if ((cacheInfos != null)
							&& (cacheInfos.size() >= ImageCacheManager.this.max_Count)) {
						int usetimes = 0;
						CacheInfo deletedCache = (CacheInfo) cacheInfos
								.get(new Random().nextInt(cacheInfos.size()));
						int i = 0;
						for (int size = cacheInfos.size(); i < size; i++) {
							CacheInfo tempCache = (CacheInfo) cacheInfos.get(i);
							if (tempCache.getUsetimes() > usetimes) {
								usetimes = tempCache.getUsetimes();
								deletedCache = tempCache;
							}
						}
						if (ImageCacheManager.this.dbClient.delete(deletedCache
								.getUrl().toString(), db)) {
							ImageCacheManager.this.filesUtils
									.deleteImage(deletedCache.getFileName());
							if (ImageCacheManager.this.dbClient.insert(
									cacheInfo, db)) {
								ImageCacheManager.this.filesUtils.saveImage(
										cacheInfo.getValue(),
										cacheInfo.getFileName());
							}
						}

					} else if (ImageCacheManager.this.dbClient.insert(
							cacheInfo, db)) {
						ImageCacheManager.this.filesUtils.saveImage(
								cacheInfo.getValue(), cacheInfo.getFileName());
					}
				}
			} finally {
				db.endTransaction();
			}
			db.endTransaction();

			return cacheInfo;
		}
	}
}