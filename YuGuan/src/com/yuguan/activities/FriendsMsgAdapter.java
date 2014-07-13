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

import com.yuguan.bean.FriendMsgBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.RoundImageView;
import com.yuguan.util.Utils;

public class FriendsMsgAdapter extends BaseAdapter {

	private List<FriendMsgBean> list;

	private Context ctx;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private LinearLayout huifu;
	private RoundImageView userImage;
	private TextView friendmsgname;
	private TextView friendmsg;
	private TextView msgSendTime;

	public FriendsMsgAdapter() {
	}

	public FriendsMsgAdapter(List<FriendMsgBean> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		mImageLoader = new ImageLoader(ctx);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return list == null ? 0 : list.get(position).getUid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.friendmsg, null);
		}
		try {
			// 得到条目中的子组件
			huifu = (LinearLayout) convertView.findViewById(R.id.huifu);

			huifu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});

			TextView actionId = (TextView) convertView
					.findViewById(R.id.friendmsgId);
			friendmsgname = (TextView) convertView
					.findViewById(R.id.friendmsgname);
			friendmsg = (TextView) convertView.findViewById(R.id.friendContent);
			msgSendTime = (TextView) convertView.findViewById(R.id.msgSendTime);

			// 从list对象中为子组件赋值
			FriendMsgBean bean = list.get(position);
			String url = Utils.activityImg + bean.getPic();
			// imag.setTag(url);
			mImageLoader.loadImage(url, this, userImage);

			actionId.setText(bean.getUid() + "");
			friendmsgname.setText(bean.getName());
			friendmsg.setText(bean.getMsg());
			msgSendTime.setText(bean.getTime());

		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return convertView;
	}

	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

}
