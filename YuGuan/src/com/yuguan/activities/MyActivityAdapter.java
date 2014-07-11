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

import com.yuguan.bean.user.UserActActionBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;
/**
 * 
 * 我组织的和我参与的
 * 
 * 
 * @author charles.chen
 *
 */
public class MyActivityAdapter extends BaseAdapter {

	private List<UserActActionBean> list;

	private Context ctx;

	private LayoutInflater mInflater;

	private ImageLoader mImageLoader;

	public MyActivityAdapter() {
	}

	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	public MyActivityAdapter(List<UserActActionBean> list, Context ctx) {
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.myaction_list, null);
			LinearLayout imag = (LinearLayout) convertView
					.findViewById(R.id.myactionImg);
			TextView actionId = (TextView) convertView
					.findViewById(R.id.myactionId);
			TextView title = (TextView) convertView
					.findViewById(R.id.myactionTitle);
			TextView bTime = (TextView) convertView
					.findViewById(R.id.myactionTime);
			TextView mall = (TextView) convertView
					.findViewById(R.id.myactionMall);
			TextView comeon = (TextView) convertView.findViewById(R.id.comeon);
			LinearLayout actionScore = (LinearLayout) convertView
					.findViewById(R.id.actionScore);

			holder.actionId = actionId;
			holder.actionScore = actionScore;
			holder.bTime = bTime;
			holder.comeon = comeon;
			holder.imag = imag;
			holder.mall = mall;
			holder.title = title;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {

			holder.actionScore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});
			UserActActionBean bean = list.get(position);
			String url = Utils.activityImg + bean.getPic();
			holder.imag.setTag(url);
			if (!mBusy) {
				mImageLoader.loadImage(url, this, holder.imag);
			} else {
				Bitmap bitmap = mImageLoader.getBitmapFromCache(url);
				if (bitmap != null) {
					BitmapDrawable bd = new BitmapDrawable(bitmap);
					holder.imag.setBackgroundDrawable(bd);
				} else {
					holder.imag.setBackgroundResource(R.drawable.action_list_back);
				}
			}

			holder.actionId.setText(bean.getAid() + "");
			holder.title.setText(bean.getAname());
			holder.bTime.setText(bean.getFtime());
			holder.mall.setText("");

		} catch (Exception e) {
			showSomething(e.toString());
		}

		return convertView;
	}

	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

	public final class ViewHolder {
		public LinearLayout imag;
		public TextView actionId;
		public TextView title;
		public TextView bTime;
		public TextView mall;
		public TextView comeon;
		public LinearLayout actionScore;
	}

}
