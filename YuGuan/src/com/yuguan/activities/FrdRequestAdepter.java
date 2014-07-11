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

import com.yuguan.bean.user.FrdRequestMsgBean;

/**
 * @author Monkey
 * 
 */
public class FrdRequestAdepter extends BaseAdapter {

	private List<FrdRequestMsgBean> coll;
	private Context ctx;
	private LayoutInflater mInflater;

	public FrdRequestAdepter(Context context, List<FrdRequestMsgBean> coll) {
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
			convertView = mInflater.inflate(R.layout.friendapplyinfo, null);
			TextView reqId = (TextView) convertView.findViewById(R.id.reqId);
			TextView reqFrdId = (TextView) convertView
					.findViewById(R.id.reqFrdId);
			TextView friendmsg = (TextView) convertView
					.findViewById(R.id.friendmsg);
			TextView applySendTime = (TextView) convertView
					.findViewById(R.id.applySendTime);
			LinearLayout btn_ok = (LinearLayout) convertView
					.findViewById(R.id.btn_ok);
			LinearLayout btn_no = (LinearLayout) convertView
					.findViewById(R.id.btn_no);
			holder.applySendTime = applySendTime;
			holder.btn_no = btn_no;
			holder.btn_ok = btn_ok;
			holder.friendmsg = friendmsg;
			holder.reqFrdId = reqFrdId;
			holder.reqId = reqId;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			FrdRequestMsgBean bean = coll.get(position);
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
		public TextView applySendTime;
		public LinearLayout btn_ok;
		public LinearLayout btn_no;
	}

}
