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

import com.yuguan.bean.FriendBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

/**
 * @author Monkey
 * 
 */
public class UserAdepter extends BaseAdapter {

	private List<FriendBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;

	public UserAdepter(Context context, List<FriendBean> coll) {
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
		return coll == null ? -1 : coll.get(position).getUid();
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.friend_list, null);
			// 得到条目中的子组件
			ImageView imag = (ImageView) convertView
					.findViewById(R.id.friendPic);
			TextView friendId = (TextView) convertView
					.findViewById(R.id.friendId);
			TextView friendTitle = (TextView) convertView
					.findViewById(R.id.friendTitle);
			ImageView friendSex = (ImageView) convertView
					.findViewById(R.id.friendSex);
			TextView friendAddress = (TextView) convertView
					.findViewById(R.id.friendAddress);

			holder.imag = imag;
			holder.friendAddress = friendAddress;
			holder.friendId = friendId;
			holder.friendSex = friendSex;
			holder.friendTitle = friendTitle;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			// 从list对象中为子组件赋值
			FriendBean bean = coll.get(position);
			String url = Utils.userImg + bean.getPic();
			holder.imag.setTag(url);
			mImageLoader.loadImage(url, this, holder.imag);
			holder.friendId.setText(bean.getUid() + "");
			holder.friendTitle.setText(bean.getName());
			holder.friendAddress.setText(bean.getAddr());
			if (bean.getSex() == 0) {
				holder.friendSex.setImageResource(R.drawable.boy_48);
			} else {
				holder.friendSex.setImageResource(R.drawable.girl_48);
			}
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}
		return convertView;

	}

	public final class ViewHolder {
		public ImageView imag;
		public TextView friendId;
		public TextView friendTitle;
		public ImageView friendSex;
		public TextView friendAddress;

	}

}
