package com.yg.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class LoginGuideActivity extends Activity
{
	private ViewPager viewPager;
	private ImageView dot0;
	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;

	ArrayList<View> views = new ArrayList<View>();
	
	private int currPageIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_loginguide_activity);
		
		setupLayout();
		setupLayoutListeners();
		loadViewpagerData();
	}
	
	private void showExitGameAlert()
	{
		//final AlertDialog dialog = new AlertDialog.Builder(this).create();
		final AlertDialog dialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		//dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		dialog.show();
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.yg_loginguide_page3_login_dialog);
		
		
		// 为确认按钮添加事件,执行退出应用操作
		final Button forgot = (Button) window.findViewById(R.id.yg_loginguide_page3_dialog_forgot);
		forgot.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) {
				Animation shake = AnimationUtils.loadAnimation(LoginGuideActivity.this, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
				forgot.startAnimation(shake);
			}
		});
		// 关闭alert对话框架
		Button login = (Button) window.findViewById(R.id.yg_loginguide_page3_dialog_login);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}
	
	private void setupLayout()
	{
		viewPager = (ViewPager) findViewById(R.id.loginguide_viewpager);
		
		dot0 = (ImageView) findViewById(R.id.loginguide_dot0);
		dot1 = (ImageView) findViewById(R.id.loginguide_dot1);
		dot2 = (ImageView) findViewById(R.id.loginguide_dot2);
		dot3 = (ImageView) findViewById(R.id.loginguide_dot3);
		
		
		
		LayoutInflater inflater = LayoutInflater.from(LoginGuideActivity.this);
		View page0View = inflater.inflate(R.layout.yg_loginguide_page0, null);
		View page1View = inflater.inflate(R.layout.yg_loginguide_page1, null);
		View page2View = inflater.inflate(R.layout.yg_loginguide_page2, null);
		View page3View = inflater.inflate(R.layout.yg_loginguide_page3, null);
		views.add(page0View);
		views.add(page1View);
		views.add(page2View);
		views.add(page3View);
		
	}
	
	private void setupLayoutListeners()
	{
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		Button login = (Button)views.get(3).findViewById(R.id.loginguide_page3_login_btn);
		login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				showExitGameAlert();
			}
		});
	}
	
	private void loadViewpagerData()
	{
		PagerAdapter mPagerAdapter = new PagerAdapter() 
		{
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) 
			{
				return arg0 == arg1;
			}

			@Override
			public int getCount() { return views.size(); }

			@Override
			public void destroyItem(View container, int position, Object object)
			{
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) 
			{
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);
	}
	

	public class MyOnPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageSelected(int arg0)
		{
			switch (arg0)
			{
			case 0:
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 1:
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot0.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 2:
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot1.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			case 3:
				dot3.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page_now));
				dot2.setImageDrawable(getResources().getDrawable(R.drawable.yg_loginguide_page));
				break;
			}
			currPageIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageScrollStateChanged(int arg0) { }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}