package com.lj.driftbottle.ui;


import com.example.testmobiledatabase.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class chuck_animation extends Activity {
	
	private  AnimationDrawable ad;
	
	private  ImageView chuck_empty2,chuck_empty1,chuck_spray;
	private  RelativeLayout chuck_bottle_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chuck_pop1);
		
		chuck_empty2 = (ImageView)findViewById(R.id.lj_chuck_empty2);
		chuck_empty1=(ImageView)findViewById(R.id.lj_chuck_empty1);
		chuck_spray=(ImageView) findViewById(R.id.lj_chuck_spray);
		chuck_bottle_layout=(RelativeLayout) findViewById(R.id.lj_chuck_bottle_layout);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;//宽度height = dm.heightPixels ;//高度
		int height = dm.heightPixels;
		Animation animationR=AnimationUtils.loadAnimation(this,R.anim.anim_set);
		Animation animationS=AnimationUtils.loadAnimation(this,R.anim.anim_set);
	//	Animation animationT=AnimationUtils.loadAnimation(this,R.anim.chuck_bottle_translate);
		Animation animationT = new TranslateAnimation(0, (float) (-width * 0.39574719432959243945658594211459), 0, (float) (-height * 0.25014174344436569808646350106308));
		animationT.setDuration(2000);
		AnimationSet set=new AnimationSet(false);
		set.addAnimation(animationR);
		set.addAnimation(animationS);
		set.addAnimation(animationT);
		
		doStartAnimation(set);
		//doStartAnimation(R.anim.chuck_bottle_translate);

		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				chuck_spray.setVisibility(View.VISIBLE);
				ad.setOneShot(true);
				ad.start();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						chuck_spray.setVisibility(View.GONE);
					}
				}, 1000);
				chuck_empty1.setVisibility(View.GONE);
				chuck_empty2.setVisibility(View.GONE);
			}
		}, 2000);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		//		Intent intent=new Intent(chuck_animation.this,DriftBottleActivity.class);
		//		startActivity(intent);
				finish();
			}
		}, 4000);
		
		
	}
	
	
	private void doStartAnimation(AnimationSet set){
//    	chuck_empty1.startAnimation(set);
//    	chuck_empty2.startAnimation(set);
    	chuck_bottle_layout.startAnimation(set);
    }
	
	//播放语音动画
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
			// TODO Auto-generated method stub
			super.onWindowFocusChanged(hasFocus);
			ad=(AnimationDrawable) getResources().getDrawable(R.anim.bottle_spray);
			if(chuck_spray!=null){
				chuck_spray.setBackground(ad);
			}
			
		}
	

}
