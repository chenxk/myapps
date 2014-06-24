package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.ActionBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

public class ActivityAdapter extends BaseAdapter {

	private List<ActionBean> list;
	

	private Context ctx;

	private LayoutInflater mInflater;

	private LinearLayout imag;
	
	private ImageLoader mImageLoader;

	public ActivityAdapter() {
	}
	
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	public ActivityAdapter(List<ActionBean> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		mImageLoader = new ImageLoader(ctx);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return list == null ? 0 : list.get(position).getId();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.sport_list, null);
		}
		try {
			// 得到条目中的子组件
			imag = (LinearLayout) convertView.findViewById(R.id.actionImg);
			TextView actionId = (TextView) convertView
					.findViewById(R.id.actionId);
			TextView title = (TextView) convertView
					.findViewById(R.id.actionTitle);
			TextView bTime = (TextView) convertView
					.findViewById(R.id.actionTime);
			TextView mall = (TextView) convertView
					.findViewById(R.id.actionMall);

			// 从list对象中为子组件赋值
			ActionBean bean = list.get(position);
			String url = Utils.activityImg + bean.getPic();
			imag.setTag(url);
			if (!mBusy) {
				mImageLoader.loadImage(url, this, imag);
			} else {
				Bitmap bitmap = mImageLoader.getBitmapFromCache(url);
				if (bitmap != null) {
					BitmapDrawable bd=new BitmapDrawable(bitmap);
					imag.setBackgroundDrawable(bd);
				} else {
					imag.setBackgroundResource(R.drawable.action_list_back);
				}
			}

			actionId.setText(bean.getId() + "");
			title.setText(bean.getTitle());
			bTime.setText(bean.getbTime());
			mall.setText(bean.getMall());

		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return convertView;
	}

	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

}
