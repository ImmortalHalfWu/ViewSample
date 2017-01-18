package com.viewsample.viewsample.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.viewsample.viewsample.R;
import com.viewsample.viewsample.R.id;
import com.viewsample.viewsample.R.layout;
import com.viewsample.viewsample.views.SideSlipViewGroup.SideSlipViewGroupBuilder;

/**   
 * @TODO 侧滑
 * @author WuImmortalHalf
 * @date 创建时间：2016年8月8日 上午11:00:34 * 
 * @version   3.0
 */     
@SuppressLint("ViewHolder")
public class ActivitySideSlip extends Activity {

	private HorizontalScrollView mScrollview;
	private RelativeLayout mRelative;
	int maxHeight,maxWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sideslip);
		initValue();
//		initView();
//		initView3();
		initView4();
		
	}

	private void initView4() {
		// TODO Auto-generated method stub
		ListView mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					LayoutInflater mInflater = LayoutInflater.from(ActivitySideSlip.this);
					View leftView= mInflater.inflate(R.layout.item_sideslip2, null),rightView= mInflater.inflate(R.layout.item_sideslip2, null),midView= mInflater.inflate(R.layout.item_sideslip, null);
					OnClickListener mClickListener = new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(ActivitySideSlip.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
						}
					};
					leftView.setTag("left");
					rightView.setTag("right");
					midView .setTag("mid");
					leftView.setOnClickListener(mClickListener);
					rightView.setOnClickListener(mClickListener);
					midView.setOnClickListener(mClickListener);
					convertView = new SideSlipViewGroupBuilder(
							leftView, 
							midView, 
							rightView).builder(ActivitySideSlip.this);
					
				}else {
				}
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return "";
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 10;
			}
		});
	}

	private void initView3() {
		// TODO Auto-generated method stub
		SideSlipViewGroupBuilder mBuilder =
				new SideSlipViewGroupBuilder(
						LayoutInflater.from(this).inflate(R.layout.item_sideslip2, null),
						LayoutInflater.from(this).inflate(R.layout.item_sideslip, null),
						LayoutInflater.from(this).inflate(R.layout.item_sideslip2, null));
		setContentView(mBuilder.builder(this));
	}

	private void initValue() {
		// TODO Auto-generated method stub
		
		maxHeight =  ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		maxWidth = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
	}
	
	
	RelativeLayout mLayout;
	private void initView2() {
		// TODO Auto-generated method stub
		
//		mScrollview = (HorizontalScrollView) findViewById(R.id.scrollView);
		mLayout = (RelativeLayout) LayoutInflater.from(ActivitySideSlip.this).inflate(R.layout.activity_sideslip,null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mLayout.setLayoutParams(layoutParams);
		
		RelativeLayout mRelativeLayout = new RelativeLayout(this);
		LayoutParams relativeParams = new LayoutParams(layoutParams);
		mRelativeLayout.setLayoutParams(relativeParams);
		
		ViewGroup leftView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_sideslip, null);
		LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftView.setLayoutParams(leftParams);
		leftView.setId(R.layout.item_sideslip);
		mRelativeLayout.addView(leftView);
		
		ViewGroup rightView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_sideslip2, null);
		LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		rightParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		rightParams.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
		rightView.setLayoutParams(rightParams);
		
		mRelativeLayout.addView(rightView);

		
        RelativeLayout rl=new RelativeLayout(this);
        rl.setBackgroundColor(Color.GRAY);
        Button btn1=new Button(this);
        btn1.setText("btn1"); 
        System.out.println(btn1.getId());
        btn1.setId(123); 
        Button btn2=new Button(this);
        btn2.setText("btn2"); 
        rl.addView(btn1); 
        rl.addView(btn2); 
        
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        btn1.setLayoutParams(lp);
        lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.LEFT_OF, btn1.getId());
        btn2.setLayoutParams(lp); 
		
//		mLayout.addView(mRelativeLayout);
//		setContentView(mLayout);
        mLayout.addView(mRelativeLayout);
		setContentView(mLayout);
	}
}
