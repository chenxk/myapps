package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.ActionCollectBean;

public class SportCollectAdepter extends BaseAdapter {

	private List<ActionCollectBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;
	private OnClickJoinListener onClickJoinListener;

	public SportCollectAdepter(Context context, List<ActionCollectBean> coll) {
		this.ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}
	
	

	public OnClickJoinListener getOnClickJoinListener() {
		return onClickJoinListener;
	}



	public void setOnClickJoinListener(OnClickJoinListener onClickJoinListener) {
		this.onClickJoinListener = onClickJoinListener;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.shoucang_action_item, null);
			ImageView shoucangActionCancel = (ImageView) convertView.findViewById(R.id.shoucangActionCancel);
			TextView shoucangActionName = (TextView) convertView.findViewById(R.id.shoucangActionName);
			TextView shoucangActionAddr = (TextView) convertView.findViewById(R.id.shoucangActionAddr);
			TextView shoucangActionTime = (TextView) convertView.findViewById(R.id.shoucangActionTime);
			TextView shoucangActionUserNum = (TextView) convertView.findViewById(R.id.shoucangActionUserNum);
			LinearLayout doJoin = (LinearLayout) convertView.findViewById(R.id.doJoin);
			
			viewHolder.shoucangActionName = shoucangActionName;
			viewHolder.shoucangActionAddr = shoucangActionAddr;
			viewHolder.shoucangActionTime = shoucangActionTime;
			viewHolder.shoucangActionUserNum = shoucangActionUserNum;
			viewHolder.shoucangActionCancel = shoucangActionCancel;
			viewHolder.doJoin = doJoin;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			final ActionCollectBean bean = coll.get(position);
			viewHolder.shoucangActionName.setText(bean.getAname());
			viewHolder.shoucangActionAddr.setText(bean.getMname());
			viewHolder.shoucangActionTime.setText(bean.getFtime());
			viewHolder.shoucangActionUserNum.setText("已经有" + bean.getUnum() + "位球友报名");
			viewHolder.doJoin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onClickJoinListener != null){
						onClickJoinListener.onClickJoinListener(bean.getId());
					}
				}
			});
			viewHolder.shoucangActionCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onClickJoinListener != null){
						onClickJoinListener.onClickCancelListener(bean.getId());
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView shoucangActionName;
		public TextView shoucangActionAddr;
		public TextView shoucangActionTime;
		public TextView shoucangActionUserNum;
		public ImageView shoucangActionCancel;
		public LinearLayout doJoin;
	}
	
	public interface OnClickJoinListener{
		public void onClickJoinListener(int aid);
		public void onClickCancelListener(int aid);
	}

}
