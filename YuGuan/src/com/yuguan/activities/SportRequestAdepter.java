/**
 * 
 */
package com.yuguan.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.yuguan.bean.user.FrdRequestMsgBean;
import com.yuguan.bean.user.HuodongInviteMsgBean;
import com.yuguan.util.HttpUtil;
import com.yuguan.util.Utils;

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
			final HuodongInviteMsgBean bean = coll.get(position);
			holder.applySendTime.setText(bean.getFtime());
			holder.btn_no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					acceptSportReq(bean.getId(),0);
				}
			});
			holder.btn_ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					baoming(bean.getAid(),bean.getId());
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
	
	public void setconvertViewGone(long id) {
		HuodongInviteMsgBean temp = null;
		for (HuodongInviteMsgBean bean : coll) {
			if (bean.getId() == id) {
				temp = bean;
				break;
			}
		}
		if (temp != null) {
			coll.remove(temp);
			this.notifyDataSetChanged();
		}
	}

	public void acceptSportReq(final long msgid,final int type) {
		
		String url = Utils.acceptSportReqUrl + "&msgid=" + msgid;
		new Thread(new HttpUtil(url, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("DELETE_PRIMSG");
				if (result.length() > 0 && !result.equals("服务访问失败")) {
					if(type == 0){
						showSomeThing("已忽略此邀请");
					}
					setconvertViewGone(msgid);
				}else{
					showSomeThing("服务访问失败!");
				}
			}
		}, "DELETE_PRIMSG")).start();
	}

	public void showSomeThing(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}
	
	
	public void baoming(int actionId,final long id){
		String url = Utils.baomingtUrl + Utils.loginInfo.getId() + "&aid" + actionId;
		new Thread(new HttpUtil(url, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String result = data.getString("BAOMING");
				if (result != null && !"服务访问失败".equals(result)) {
					try {
						JSONObject json = new JSONObject(result);
						if(json.getString("status").equals("suc")){
							showSomeThing("活动报名成功!");
							acceptSportReq(id,1);
						}else{
							showSomeThing("活动报名失败!");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					showSomeThing("服务访问失败!");
				}
			}
		}, "BAOMING")).start();
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
