package cn.buaa.myweixin;

import java.util.ArrayList;
import java.util.List;

import cn.buaa.myweixin.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreInfo extends LinearLayout {

	private int score;
	private boolean clickAble;
	private TextView scoreView;

	private List<ImageView> views = new ArrayList<ImageView>();
	private ImageView v1;
	private ImageView v2;
	private ImageView v3;
	private ImageView v4;
	private ImageView v5;

	private View view;

	public boolean isClickAble() {
		return clickAble;
	}

	public void setClickAble(boolean clickable) {
		this.clickAble = clickable;
		for (int i = 0; i < views.size(); i++) {
			views.get(i).setClickable(clickAble);
		}
	}

	public ScoreInfo(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedarray = context.obtainStyledAttributes(attrs,
				R.styleable.ScoreInfo);

		score = typedarray.getInt(R.styleable.ScoreInfo_score, 0);
		clickAble = typedarray.getBoolean(R.styleable.ScoreInfo_clickable,
				false);
		typedarray.recycle();

		view = LayoutInflater.from(context).inflate(R.layout.score_layout,
				this, true);

		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setScore(0);
			}
		});
		v1 = (ImageView) view.findViewById(R.id.star1);
		v2 = (ImageView) view.findViewById(R.id.star2);
		v3 = (ImageView) view.findViewById(R.id.star3);
		v4 = (ImageView) view.findViewById(R.id.star4);
		v5 = (ImageView) view.findViewById(R.id.star5);
		scoreView = (TextView) view.findViewById(R.id.fenshu);

		views.add(v1);
		views.add(v2);
		views.add(v3);
		views.add(v4);
		views.add(v5);

		for (int i = 0; i < views.size(); i++) {
			views.get(i).setTag(i);
			views.get(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String tag = v.getTag().toString();
					int index = Integer.parseInt(tag) + 1;
					setScore(index * 2);
				}
			});
		}

		setClickAble(clickAble);
		setScore(score);
		scoreView.setText(score + "·Ö");
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		int count = score / 2;
		for (int i = 0; i < views.size(); i++) {
			if (i < count) {
				views.get(i).setImageResource(R.drawable.star_yellow);
			} else {
				views.get(i).setImageResource(R.drawable.star_black);
			}
		}
		scoreView.setText(score + "·Ö");
	}

}
