package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.SysNoticeBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.Utils;

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
			LinearLayout deleteSysMsg = (LinearLayout) convertView.findViewById(R.id.deleteSysMsg);
			viewHolder.sysmsg = sysmsg;
			viewHolder.sysSendTime = sysSendTime;
			viewHolder.deleteSysMsg = deleteSysMsg;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			final SysNoticeBean bean = coll.get(position);
			viewHolder.sysmsg.setText(bean.getMsg());
			viewHolder.sysSendTime.setText(bean.getFtime());
			viewHolder.deleteSysMsg.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deleteMsg(bean.getId());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	
	public void setconvertViewGone(long id){
		SysNoticeBean temp = null;
		for(SysNoticeBean bean : coll){
			if(bean.getId() == id){
				temp = bean;
				break;
			}
		}
		if(temp != null){
			coll.remove(temp);
			this.notifyDataSetChanged();
		}
	}
	
	
	public void deleteMsg(final long id) {
		String url = Utils.delSysMsgUrl +  "&msgid=" + id;
		new Thread(new HttpUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("DELETE_PRIMSG");
				if (result.length() > 0 && !result.equals("服务访问失败")) {
					showSomeThing("删除成功!");
					setconvertViewGone(id);
				}
			}
		}, "DELETE_PRIMSG")).start();
	}
	
	public void showSomeThing(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView sysmsg;
		public TextView sysSendTime;
		public LinearLayout deleteSysMsg;
	}

}
