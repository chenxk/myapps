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
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.bean.CommentBean;

/**
 * @author Monkey
 *
 */
public class CommentAdepter extends BaseAdapter {

	private List<CommentBean> coll;

    private Context ctx;
    
    private LayoutInflater mInflater;

    
    public CommentAdepter(Context context, List<CommentBean> coll) {
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
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null)
        {    
            convertView = mInflater.inflate(R.layout.comment_info, null);
        }
        try {
        	//得到条目中的子组件
            
            TextView textview1 = (TextView) convertView.findViewById(R.id.commentUser);
            TextView textview2 = (TextView) convertView.findViewById(R.id.comment);
            TextView textview3 = (TextView) convertView.findViewById(R.id.commentTime);
            
            //从list对象中为子组件赋值
            CommentBean bean = coll.get(position);
            textview1.setText(bean.getuName()+":");
    		textview2.setText(bean.getComment());
    		textview3.setText(bean.getPostTime());
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		}
        
        
        return convertView;
		
	}

}
