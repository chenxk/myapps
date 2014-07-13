package com.yuguan.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.user.UserActActionBean;
import com.yuguan.bean.user.UserOrgActionBean;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

/**
 * 
 * 我组织的和我参与的
 * 
 * 
 * @author charles.chen
 * 
 */
public class MyActivityAdapter extends BaseAdapter {

	private List<UserActActionBean> list;
	private List<UserOrgActionBean> coll;
	/**
	 * flag = 0 org else act
	 * */
	private int flag = 10;
	private Context ctx;

	private LayoutInflater mInflater;

	private ImageLoader mImageLoader;

	public MyActivityAdapter() {
	}

	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	public MyActivityAdapter(Context ctx, List<UserActActionBean> list) {
		this.list = list;
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		mImageLoader = new ImageLoader(ctx);
	}

	public MyActivityAdapter(Context ctx, List<UserOrgActionBean> list, int a) {
		this.coll = list;
		this.ctx = ctx;
		this.flag = a;
		mInflater = LayoutInflater.from(ctx);
		mImageLoader = new ImageLoader(ctx);
	}

	@Override
	public int getCount() {
		if (flag == 0) {
			return coll == null ? 0 : coll.size();
		} else {
			return list == null ? 0 : list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		long aid = 0;
		String pic = "";
		String aname = "";
		String ftime = "";
		int status = 0;
		int sort = 0;
		int score = 0;
		int reviewed = 0;
		int usernum = 0;

		if (flag == 0) {
			UserOrgActionBean bean = coll.get(position);
			pic = bean.getPic();
			aid = bean.getAid();
			aname = bean.getAname();
			ftime = bean.getFtime();
			status = bean.getStatus();
			score = bean.getScore();
			usernum = bean.getUsernum();
		} else {
			UserActActionBean bean = list.get(position);
			pic = bean.getPic();
			aid = bean.getAid();
			aname = bean.getAname();
			ftime = bean.getFtime();
			status = bean.getStatus();
			score = bean.getScore();
			sort = bean.getSort();
			reviewed = bean.getReviewed();
		}

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.myaction_list, null);
			LinearLayout imag = (LinearLayout) convertView
					.findViewById(R.id.myactionImg);
			RelativeLayout myjoin = (RelativeLayout) convertView
					.findViewById(R.id.myjoinLayout);
			RelativeLayout myorg = (RelativeLayout) convertView
					.findViewById(R.id.myorgLayout);
			TextView actionId = (TextView) convertView
					.findViewById(R.id.myactionId);
			TextView title = (TextView) convertView
					.findViewById(R.id.myactionTitle);
			TextView bTime = (TextView) convertView
					.findViewById(R.id.myactionTime);
			TextView mall = (TextView) convertView
					.findViewById(R.id.myactionMall);
			TextView comeon = (TextView) convertView.findViewById(R.id.comeon);
			TextView xinyuscore = (TextView) convertView
					.findViewById(R.id.xinyuscore);
			TextView usernums = (TextView) convertView
					.findViewById(R.id.usernum);
			TextView orggeifen = (TextView) convertView
					.findViewById(R.id.orggeifen);
			ImageView sortMC = (ImageView) convertView
					.findViewById(R.id.sortMC);
			LinearLayout actionScore = (LinearLayout) convertView
					.findViewById(R.id.actionScore);

			holder.actionId = actionId;
			holder.actionScore = actionScore;
			holder.bTime = bTime;
			holder.comeon = comeon;
			holder.imag = imag;
			holder.mall = mall;
			holder.title = title;
			holder.myjoin = myjoin;
			holder.myorg = myorg;
			holder.orggeifen = orggeifen;
			holder.sortMC = sortMC;
			holder.usernum = usernums;
			holder.xinyuscore = xinyuscore;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {

			holder.actionScore.setVisibility(View.GONE);
			holder.myjoin.setVisibility(View.GONE);
			holder.myorg.setVisibility(View.GONE);
			if (flag == 0) {

				if (status == 1) {
					holder.comeon.setText("报名中");
				}

				if (status == 2) {
					holder.comeon.setText("进行中");
				}

				if (status == 3) {
					holder.comeon.setText("已结束");
					holder.myorg.setVisibility(View.VISIBLE);
					holder.xinyuscore.setText("信誉积分:" + score);
					holder.usernum.setText("(已有" + usernum + "位参与者评分)");
					holder.actionScore.setVisibility(View.VISIBLE);
				}

				if (status == 4) {
					holder.comeon.setText("已结束");
					holder.myorg.setVisibility(View.VISIBLE);
					holder.xinyuscore.setText("信誉积分:" + score);
					holder.usernum.setText("(已有" + usernum + "位参与者评分)");
				}

			} else {

				if (status == 1) {
					holder.comeon.setText("报名中");
				}

				if (status == 2) {
					holder.comeon.setText("已结束");
					if (reviewed > 0) {
						holder.myjoin.setVisibility(View.VISIBLE);
						if (sort == 1) {
							holder.sortMC.setImageResource(R.drawable.pf_mc);
						} else if (sort == 2) {
							holder.sortMC.setImageResource(R.drawable.pf_mc_02);
						} else if (sort == 3) {
							holder.sortMC.setImageResource(R.drawable.pf_mc_03);
						}
						holder.orggeifen.setText(score + "分");
					}
					holder.actionScore.setVisibility(View.VISIBLE);
				}

				if (status == 3) {
					holder.comeon.setText("已结束");
					if (reviewed > 0) {
						holder.myjoin.setVisibility(View.VISIBLE);
						if (sort == 1) {
							holder.sortMC.setImageResource(R.drawable.pf_mc);
						} else if (sort == 2) {
							holder.sortMC.setImageResource(R.drawable.pf_mc_02);
						} else if (sort == 3) {
							holder.sortMC.setImageResource(R.drawable.pf_mc_03);
						}
						holder.orggeifen.setText(score + "分");
					}
				}

			}

			final int index = position;

			holder.actionScore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (flag == 0) {
						Intent intent = new Intent(ctx,
								PingFenResultActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("orgActionBean", coll.get(index));
						intent.putExtras(bundle);
						ctx.startActivity(intent);
					} else {
						Intent intent = new Intent(ctx, PingFenActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("actActionBean", list.get(index));
						intent.putExtras(bundle);
						ctx.startActivity(intent);
					}
				}
			});

			String url = Utils.activityImg + pic;
			holder.imag.setTag(url);
			if (!mBusy) {
				mImageLoader.loadImage(url, this, holder.imag);
			} else {
				Bitmap bitmap = mImageLoader.getBitmapFromCache(url);
				if (bitmap != null) {
					BitmapDrawable bd = new BitmapDrawable(bitmap);
					holder.imag.setBackgroundDrawable(bd);
				} else {
					holder.imag
							.setBackgroundResource(R.drawable.action_list_back);
				}
			}

			holder.actionId.setText(aid + "");
			holder.title.setText(aname);
			holder.bTime.setText(ftime);
			holder.mall.setText("");

		} catch (Exception e) {
			showSomething(e.toString());
		}

		return convertView;
	}

	public void showSomething(String str) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

	public final class ViewHolder {
		public LinearLayout imag;
		public TextView actionId;
		public TextView title;
		public TextView bTime;
		public TextView mall;
		public TextView comeon;
		public TextView orggeifen;
		public TextView xinyuscore;
		public TextView usernum;
		public LinearLayout actionScore;
		public RelativeLayout myjoin;
		public RelativeLayout myorg;
		public ImageView sortMC;
	}

}
