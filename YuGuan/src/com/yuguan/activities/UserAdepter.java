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

import com.yuguan.bean.UserBean;
import com.yuguan.util.DownloadTask;
import com.yuguan.util.Utils;

/**
 * @author Monkey
 *
 */
public class UserAdepter extends BaseAdapter {

	private List<UserBean> coll;

    private Context ctx;
    
    private LayoutInflater mInflater;

    
    public UserAdepter(Context context, List<UserBean> coll) {
        this.ctx = context;
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
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
		return coll == null ? 0 : coll.get(position).getId();
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
            UserBean bean = coll.get(position);
            //new DownloadTask(imag,R.drawable.head1_128).execute(Utils.userImg + bean.getPic());
            friendTitle.setText(bean.getName());
            friendAddress.setText(bean.getAddress());
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
