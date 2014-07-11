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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.HuodongInviteMsgBean;

/**
 * @author Monkey
 * 
 */
public class SportRequestAdepter extends BaseAdapter {

	private List<HuodongInviteMsgBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;

	public SportRequestAdepter(Context context, List<HuodongInviteMsgBean> coll) {
		this.ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.sportapplyinfo, null);
			TextView reqId = (TextView) convertView.findViewById(R.id.sportReqId);
			TextView reqFrdId = (TextView) convertView
					.findViewById(R.id.sportReqFrdId);
			TextView friendmsg = (TextView) convertView
					.findViewById(R.id.friendfrom);
			TextView sportName = (TextView) convertView
					.findViewById(R.id.sportname);
			TextView applySendTime = (TextView) convertView
					.findViewById(R.id.sportSendTime);
			LinearLayout btn_ok = (LinearLayout) convertView
					.findViewById(R.id.but_ok);
			LinearLayout btn_no = (LinearLayout) convertView
					.findViewById(R.id.but_no);
			holder.applySendTime = applySendTime;
			holder.btn_no = btn_no;
			holder.btn_ok = btn_ok;
			holder.friendmsg = friendmsg;
			holder.reqFrdId = reqFrdId;
			holder.reqId = reqId;
			holder.sportName = sportName;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			HuodongInviteMsgBean bean = coll.get(position);
			holder.applySendTime.setText(bean.getFtime());
			holder.btn_no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			holder.btn_ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			holder.friendmsg.setText(bean.getUname());
			holder.reqFrdId.setText(bean.getUid() + "");
			holder.reqId.setText(bean.getId() + "");
			holder.sportName.setText(bean.getAname());
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return convertView;

	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView reqId;
		public TextView reqFrdId;
		public TextView friendmsg;
		public TextView sportName;
		public TextView applySendTime;
		public LinearLayout btn_ok;
		public LinearLayout btn_no;
	}

}
