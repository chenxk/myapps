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

import com.yuguan.bean.FriendBean;
import com.yuguan.bean.UserBean;
import com.yuguan.util.DownloadTask;
import com.yuguan.util.ImageLoader;
import com.yuguan.util.Utils;

/**
 * @author Monkey
 *
 */
public class UserAdepter extends BaseAdapter {

	private List<FriendBean> coll;

    private Context ctx;
    
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    
    public UserAdepter(Context context, List<FriendBean> coll) {
        this.ctx = context;
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(ctx);
    }
    
    
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll == null ? 0:coll.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return coll == null ? 0 : coll.get(position).getUid();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null)
        {    
            convertView = mInflater.inflate(R.layout.friend_list, null);
        }
        try {
        	//得到条目中的子组件
    		ImageView imag = (ImageView)convertView.findViewById(R.id.friendPic);
            TextView friendTitle = (TextView)convertView.findViewById(R.id.friendTitle);
            ImageView friendSex = (ImageView)convertView.findViewById(R.id.friendSex);
            TextView friendAddress = (TextView)convertView.findViewById(R.id.friendAddress);
            
            //从list对象中为子组件赋值
            FriendBean bean = coll.get(position);
            String url = Utils.userImg + bean.getPic();
			imag.setTag(url);
			mImageLoader.loadImage(url, this, imag);
            friendTitle.setText(bean.getName());
            friendAddress.setText(bean.getAddr());
            if(bean.getSex() == 0){
            	friendSex.setImageResource(R.drawable.boy_48);
            }else{
            	friendSex.setImageResource(R.drawable.girl_48);
            }
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}
        
        
        return convertView;
		
	}

}
