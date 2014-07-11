package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.SysNoticeBean;

public class SysNoticeAdepter extends BaseAdapter {

	private List<SysNoticeBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;

	public SysNoticeAdepter(Context context, List<SysNoticeBean> coll) {
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
			convertView = mInflater.inflate(R.layout.sysmsginfo, null);
			TextView sysmsg = (TextView) convertView.findViewById(R.id.sysmsg);
			TextView sysSendTime = (TextView) convertView
					.findViewById(R.id.sysSendTime);
			viewHolder.sysmsg = sysmsg;
			viewHolder.sysSendTime = sysSendTime;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			SysNoticeBean bean = coll.get(position);
			viewHolder.sysmsg.setText(bean.getMsg());
			viewHolder.sysSendTime.setText(bean.getFtime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView sysmsg;
		public TextView sysSendTime;
	}

}
