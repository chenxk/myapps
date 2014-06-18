package com.yuguan.imagecache;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DBClient extends SQLiteOpenHelper {
	private String modeName;
	private Context context;

	public DBClient(Context context, String modeName, String tag) {
		super(context, context.getPackageName() + ".db", null, 1);
		this.modeName = (modeName + tag);
		this.context = context;
	}

	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("create table "
					+ this.modeName
					+ "  (_id integer primary key autoincrement,cache_url varchar(50), create_time integer, usetimes integer,cache_filename varchar(50),cache_size integer)");
			//Toast.makeText(context, "´´½¨±í", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
		
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	synchronized boolean insert(CacheInfo cacheInfo, SQLiteDatabase db) {
		try {
			db.execSQL(
					"insert into "
							+ this.modeName
							+ "(cache_url,create_time,usetimes,cache_filename,cache_size) values (?,?,?,?,?)",
					new Object[] { cacheInfo.getUrl().toString(),
							Long.valueOf(cacheInfo.getCreatAt()),
							Integer.valueOf(cacheInfo.getUsetimes()),
							cacheInfo.getFileName(),
							Long.valueOf(cacheInfo.getFileSize()) });
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	synchronized boolean update(int usetimes, String url, SQLiteDatabase db) {
		try {
			db.execSQL("update " + this.modeName
					+ " set usetimes=? where cache_url='" + url + "'",
					new Object[] { Integer.valueOf(usetimes) });
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	synchronized boolean update(long createTime, String url, SQLiteDatabase db) {
		try {
			db.execSQL("update " + this.modeName
					+ " set create_time=? where cache_url='" + url + "'",
					new Object[] { Long.valueOf(createTime) });
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	synchronized boolean updateOther(int usetimes, String url, SQLiteDatabase db) {
		try {
			db.execSQL("update " + this.modeName
					+ " set usetimes=? where cache_url not in('" + url + "')",
					new Object[] { Integer.valueOf(usetimes) });
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	synchronized CacheInfo select(String url, SQLiteDatabase db) {
		String sql = "select cache_url,create_time,usetimes,cache_filename,cache_size from "
				+ this.modeName + " where cache_url='" + url + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if ((cursor != null) && (cursor.getCount() > 0)) {
			cursor.moveToFirst();
			CacheInfo cacheInfo = new CacheInfo();
			try {
				cacheInfo.setUrl(new URL(cursor.getString(0)));
			} catch (MalformedURLException e) {
				return null;
			}
			cacheInfo.setCreatAt(cursor.getLong(1));
			cacheInfo.setUsetimes(cursor.getInt(2));
			cacheInfo.setFileName(cursor.getString(3));
			cacheInfo.setFileSize(cursor.getLong(4));
			cursor.close();
			return cacheInfo;
		}
		return null;
	}

	synchronized boolean delete(String url, SQLiteDatabase db) {
		try {
			db.execSQL("delete from " + this.modeName + " where cache_url='"
					+ url + "'");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	synchronized List<CacheInfo> selectAll(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select cache_url,create_time,usetimes,cache_filename,cache_size from "
						+ this.modeName, null);
		if ((cursor != null) && (cursor.getCount() > 0)) {
			List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				CacheInfo cacheInfo = new CacheInfo();
				try {
					cacheInfo.setUrl(new URL(cursor.getString(0)));
				} catch (MalformedURLException e) {
					return null;
				}
				cacheInfo.setCreatAt(cursor.getLong(1));
				cacheInfo.setUsetimes(cursor.getInt(2));
				cacheInfo.setFileName(cursor.getString(3));
				cacheInfo.setFileSize(cursor.getLong(4));
				cacheInfos.add(cacheInfo);
			}
			cursor.close();
			return cacheInfos;
		}
		return null;
	}

	SQLiteDatabase getSQLiteDatabase() {
		return getWritableDatabase();
	}
}