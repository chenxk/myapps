/**
 * 
 */
package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.ScoreInfo;

import com.yuguan.bean.MallBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

/**
 * @author Monkey
 * 
 */
public class MallAdepter extends BaseAdapter {

	private List<MallBean> coll;

	private Context ctx;

	private LayoutInflater mInflater;
	private boolean mBusy = false;
	private ImageLoader mImageLoader;

	public MallAdepter(Context context, List<MallBean> coll) {
		this.ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(ctx);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll == null ? 0 : coll.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.mall_list, null);
		}
		try {
			// 得到条目中的子组件
			ImageView imag = (ImageView) convertView.findViewById(R.id.mallPic);
			TextView id = (TextView) convertView.findViewById(R.id.mallId);
			TextView tv1 = (TextView) convertView.findViewById(R.id.mallTitle);
			TextView pb = (TextView) convertView.findViewById(R.id.mallAddress);
			ScoreInfo tv2 = (ScoreInfo) convertView.findViewById(R.id.mallScore);

			// 从list对象中为子组件赋值
			MallBean bean = coll.get(position);
			String url = Utils.mallPicUrl + bean.getPic();
			imag.setTag(url);
			mImageLoader.loadImage(url, this, imag);
			id.setText(bean.getId() + "");
			tv1.setText(bean.getTitle());
			pb.setText(bean.getAddress());
			tv2.setScore(bean.getScore());
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return convertView;

	}

}
