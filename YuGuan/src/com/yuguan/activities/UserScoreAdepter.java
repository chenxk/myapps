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

import com.yuguan.bean.UserBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

/**
 * @author Monkey
 * 
 */
public class UserScoreAdepter extends BaseAdapter {

	private List<UserBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private SelectUser selectUser;

	public UserScoreAdepter(Context context, List<UserBean> coll) {
		this.ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(ctx);
	}
	
	

	public SelectUser getSelectUser() {
		return selectUser;
	}



	public void setSelectUser(SelectUser selectUser) {
		this.selectUser = selectUser;
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
		return coll == null ? 0 : coll.get(position).getUid();
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
			convertView = mInflater.inflate(R.layout.userscore_list, null);
			// 得到条目中的子组件
			ImageView imag = (ImageView) convertView
					.findViewById(R.id.friendScorePic);
			TextView friendTitle = (TextView) convertView
					.findViewById(R.id.friendScoreTitle);
			ImageView friendSex = (ImageView) convertView
					.findViewById(R.id.friendScoreSex);
			ScoreInfo userscore = (ScoreInfo) convertView
					.findViewById(R.id.userscore);
			ImageView imggj = (ImageView) convertView.findViewById(R.id.imggj);
			ImageView imgyj = (ImageView) convertView.findViewById(R.id.imgyj);
			ImageView imgjj = (ImageView) convertView.findViewById(R.id.imgjj);

			

			holder.friendSex = friendSex;
			holder.friendTitle = friendTitle;
			holder.imag = imag;
			holder.imggj = imggj;
			holder.imgjj = imgjj;
			holder.imgyj = imgyj;
			holder.userscore = userscore;

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			// 从list对象中为子组件赋值
			final UserBean bean = coll.get(position);
			
			holder.imggj.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					selectUser.select(bean, 1,0);
				}
			});

			holder.imgyj.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectUser.select(bean, 2,0);
				}
			});

			holder.imgjj.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectUser.select(bean, 3,0);
				}
			});
			
			String url = Utils.userImg + bean.getPic();
			holder.imag.setTag(url);
			mImageLoader.loadImage(url, this, holder.imag);
			holder.friendTitle.setText(bean.getName());
			if (bean.getSex() == 0) {
				holder.friendSex.setImageResource(R.drawable.boy_48);
			} else {
				holder.friendSex.setImageResource(R.drawable.girl_48);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return convertView;

	}

	public final class ViewHolder {
		public ImageView imag;
		public TextView friendTitle;
		public ImageView friendSex;
		public ImageView imggj;
		public ImageView imgyj;
		public ImageView imgjj;
		public ScoreInfo userscore;
	}
	
	public interface SelectUser{
		public void select(UserBean bean,int jb,int score);
	}

}
