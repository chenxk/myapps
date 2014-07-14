package com.yuguan.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.buaa.myweixin.R;

import com.yuguan.util.HttpUtil;

public class RefreshListView extends ListView implements OnScrollListener {

	private float mDownY;
	private float mMoveY;

	private int mHeaderHeight;

	private int mCurrentScrollState;

	private final static int NONE_PULL_REFRESH = 0; // 正常状态
	private final static int ENTER_PULL_REFRESH = 1; // 进入下拉刷新状态
	private final static int OVER_PULL_REFRESH = 2; // 进入松手刷新状态
	private final static int EXIT_PULL_REFRESH = 3; // 松手后反弹和加载状态
	private int mPullRefreshState = 0; // 记录刷新状态

	private final static int REFRESH_BACKING = 0; // 反弹中
	private final static int REFRESH_BACED = 1; // 达到刷新界限，反弹结束后
	private final static int REFRESH_RETURN = 2; // 没有达到刷新界限，返回
	private final static int REFRESH_DONE = 3; // 加载数据结束

	private LinearLayout mHeaderLinearLayout = null;
	private LinearLayout mFooterLinearLayout = null;
	private TextView mHeaderTextView = null;
	private TextView mHeaderUpdateText = null;
	private ImageView mHeaderPullDownImageView = null;
	private ImageView mHeaderReleaseDownImageView = null;
	private ProgressBar mHeaderProgressBar = null;
	private TextView mFooterTextView = null;
	private ProgressBar mFooterProgressBar = null;
	private SimpleDateFormat mSimpleDateFormat;

	private Object mRefreshObject = null;
	private RefreshListener mRefreshListener = null;
	private Context context;

	private String url;
	// 是否是最后一行
	private boolean isLastRow = false;
	// 是否是最后一条数据
	private boolean isLastData = false;
	// 是否正在加载中
	private boolean isLoading = false;

	private boolean isInterup = false;
	// 包裹listview的父控件
	private ViewParent parent;

	public boolean isInterup() {
		return isInterup;
	}

	public void setInterup(boolean isInterup) {
		this.isInterup = isInterup;
	}
	

	public void setParent(ViewParent parent) {
		this.parent = parent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setOnRefreshListener(RefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isInterup) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setParentScrollAble(false);// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
											// 停住不能滚动
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
				break;
			default:
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void setParentScrollAble(boolean b) {
		//ViewParent parent = this.getParent().getParent().getParent();
		parent.requestDisallowInterceptTouchEvent(!b);
		// showSomeThing(b+" "+parent.toString());
	}

	void init(final Context context) {
		mHeaderLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_header, null);
		addHeaderView();
		mHeaderTextView = (TextView) findViewById(R.id.refresh_list_header_text);
		mHeaderUpdateText = (TextView) findViewById(R.id.refresh_list_header_last_update);
		mHeaderPullDownImageView = (ImageView) findViewById(R.id.refresh_list_header_pull_down);
		mHeaderReleaseDownImageView = (ImageView) findViewById(R.id.refresh_list_header_release_up);
		mHeaderProgressBar = (ProgressBar) findViewById(R.id.refresh_list_header_progressbar);

		mFooterLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_footer, null);
		addFootView();
		mFooterProgressBar = (ProgressBar) findViewById(R.id.refresh_list_footer_progressbar);
		mFooterTextView = (TextView) mFooterLinearLayout
				.findViewById(R.id.refresh_list_footer_text);

		setSelection(1);
		setOnScrollListener(this);
		measureView(mHeaderLinearLayout);
		mHeaderHeight = mHeaderLinearLayout.getMeasuredHeight();

		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		mHeaderUpdateText.setText(context.getString(
				R.string.app_list_header_refresh_last_update,
				mSimpleDateFormat.format(new Date())));
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (getHeaderViewsCount() == 0) {
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveY = ev.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) ((mMoveY - mDownY) / 3),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP:
			// when you action up, it will do these:
			// 1. roll back util header topPadding is 0
			// 2. hide the header by setSelection(1)
			if (mPullRefreshState == OVER_PULL_REFRESH
					|| mPullRefreshState == ENTER_PULL_REFRESH) {
				new Thread() {
					public void run() {
						Message msg;
						while (mHeaderLinearLayout.getPaddingTop() > 1) {
							System.out.println(mHeaderLinearLayout.getPaddingTop());
							msg = mHandler.obtainMessage();
							msg.what = REFRESH_BACKING;
							mHandler.sendMessage(msg);
							try {
								sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						msg = mHandler.obtainMessage();
						if (mPullRefreshState == OVER_PULL_REFRESH) {
							msg.what = REFRESH_BACED;
						} else {
							msg.what = REFRESH_RETURN;
						}
						mHandler.sendMessage(msg);
					};
				}.start();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (getHeaderViewsCount() == 0) {
			return;
		}

		if (firstVisibleItem + visibleItemCount == totalItemCount
				&& totalItemCount > 0) {
			isLastRow = true;
		} else {
			isLastRow = false;
		}

		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderLinearLayout.getBottom() >= 0 && mHeaderLinearLayout
						.getBottom() < mHeaderHeight)) {
			// 进入且仅进入下拉刷新状态
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = ENTER_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderLinearLayout.getBottom() >= mHeaderHeight)) {
			// 下拉达到界限，进入松手刷新状态
			if (mPullRefreshState == ENTER_PULL_REFRESH
					|| mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = OVER_PULL_REFRESH;
				mDownY = mMoveY; // 为下拉1/3折扣效果记录开始位置
				mHeaderTextView.setText("松手刷新");// 显示松手刷新
				mHeaderPullDownImageView.setVisibility(View.GONE);// 隐藏"下拉刷新"
				mHeaderReleaseDownImageView.setVisibility(View.VISIBLE);// 显示向上的箭头

			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem != 0) {
			// 不刷新了
			if (mPullRefreshState == ENTER_PULL_REFRESH) {
				mPullRefreshState = NONE_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0) {
			// 飞滑状态，不能显示出header，也不能影响正常的飞滑
			// 只在正常情况下才纠正位置
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				setSelection(1);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
		if (getHeaderViewsCount() == 0) {
			return;
		}
		try {
			if (isLastRow
					&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { // 加载元素
				if (isLoading == false && isLastData == false) {
					isLoading = true;
					addFootView();
					if (mRefreshListener != null) {
						mRefreshListener.more();
					}
					new Thread(new HttpUtil(url, loadHandler, key)).start();
				}
				isLastRow = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			showSomeThing(e.toString());
		}

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(1);
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private String key = "FLUSH_ALL_DATA";

	private Handler loadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			mRefreshObject = data.getString(key);
			mRefreshListener.loaded(mRefreshObject);
			isLoading = false;
		}
	};

	private Handler flushHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			mRefreshObject = data.getString(key);
			msg = mHandler.obtainMessage();
			msg.what = REFRESH_DONE;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (getHeaderViewsCount() == 0) {
				return;
			}

			switch (msg.what) {
			case REFRESH_BACKING:
				if (getHeaderViewsCount() == 1) {
					mHeaderLinearLayout
							.setPadding(
									mHeaderLinearLayout.getPaddingLeft(),
									(int) (mHeaderLinearLayout.getPaddingTop() * 0.75f),
									mHeaderLinearLayout.getPaddingRight(),
									mHeaderLinearLayout.getPaddingBottom());
				}
				break;
			case REFRESH_BACED:
				mHeaderTextView.setText("正在加载...");
				mHeaderProgressBar.setVisibility(View.VISIBLE);
				mHeaderPullDownImageView.setVisibility(View.GONE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mPullRefreshState = EXIT_PULL_REFRESH;
				if (mRefreshListener != null) {
					mRefreshListener.setUrl();
				}
				new Thread(new HttpUtil(url, flushHandler, key)).start();
				break;
			case REFRESH_RETURN:
				mHeaderTextView.setText("下拉刷新");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				break;
			case REFRESH_DONE:
				mHeaderTextView.setText("下拉刷新");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderReleaseDownImageView.setVisibility(View.GONE);
				mHeaderUpdateText.setText(getContext().getString(
						R.string.app_list_header_refresh_last_update,
						mSimpleDateFormat.format(new Date())));
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				if (mRefreshListener != null) {
					mRefreshListener.refreshed(mRefreshObject);
				}
				break;
			default:
				break;
			}
		}
	};

	public interface RefreshListener {
		Object refreshing();

		void setUrl();

		void refreshed(Object obj);

		void more();

		void loaded(Object obj);
	}

	public void finishFootView() {
		if (getFooterViewsCount() == 0) {
			return;
		}
		mFooterProgressBar.setVisibility(View.GONE);
		mFooterTextView.setText(R.string.app_list_footer_more);
	}

	public void addFootView() {
		if (getFooterViewsCount() == 0) {
			addFooterView(mFooterLinearLayout);
		}
	}

	public void addHeaderView() {
		if (getHeaderViewsCount() == 0) {
			addHeaderView(mHeaderLinearLayout);
		}
	}

	public void removeFootView() {
		if (getFooterViewsCount() == 1)
			removeFooterView(mFooterLinearLayout);
	}

	public void removeHeaderView() {
		if (getHeaderViewsCount() == 1)
			removeHeaderView(mHeaderLinearLayout);
	}

	public void showSomeThing(String str) {
		Toast.makeText(this.context, str, Toast.LENGTH_LONG).show();
	}

	public boolean isLastRow() {
		return isLastRow;
	}

	public void setLastRow(boolean isLastRow) {
		this.isLastRow = isLastRow;
	}

	public boolean isLastData() {
		return isLastData;
	}

	public void setLastData(boolean isLastData) {
		this.isLastData = isLastData;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

}
