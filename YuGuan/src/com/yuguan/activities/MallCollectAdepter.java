package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.buaa.myweixin.R;
import cn.buaa.myweixin.ScoreInfo;

import com.yuguan.bean.user.MallCollectBean;

public class MallCollectAdepter extends BaseAdapter {

	private List<MallCollectBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;

	public MallCollectAdepter(Context context, List<MallCollectBean> coll) {
		this.ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
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
			convertView = mInflater.inflate(R.layout.shoucang_mall_item, null);
			TextView shoucangMallName = (TextView) convertView.findViewById(R.id.shoucangMallName);
			TextView shoucangMallDesc = (TextView) convertView.findViewById(R.id.shoucangMallDesc);
			TextView shoucangMallAddr = (TextView) convertView.findViewById(R.id.shoucangMallAddr);
			ScoreInfo shoucangMallScore = (ScoreInfo) convertView.findViewById(R.id.shoucangMallScore);
			
			viewHolder.shoucangMallName = shoucangMallName;
			viewHolder.shoucangMallDesc = shoucangMallDesc;
			viewHolder.shoucangMallAddr = shoucangMallAddr;
			viewHolder.shoucangMallScore = shoucangMallScore;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			MallCollectBean bean = coll.get(position);
			viewHolder.shoucangMallName.setText(bean.getName());
			viewHolder.shoucangMallDesc.setText(bean.getDesc());
			viewHolder.shoucangMallAddr.setText(bean.getAddr());
			//viewHolder.shoucangMallScore.setScore(bean.getId());;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView shoucangMallName;
		public TextView shoucangMallDesc;
		public TextView shoucangMallAddr;
		public ScoreInfo shoucangMallScore;
	}

}
