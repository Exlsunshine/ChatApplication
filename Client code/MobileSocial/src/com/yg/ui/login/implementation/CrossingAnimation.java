package com.yg.ui.login.implementation;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class CrossingAnimation
{
	private final static int SHIFT_DISTANCE = 200;
	private final static int ANIM_DURATION = 900;
	private final static int SHIFT_LEFT = -1;
	private final static int SHIFT_RIGHT = 1;
	
	private ImageView leftImg;
	private ImageView rightImg;
	
	private RandomeItem ri = new RandomeItem();
	private class RandomeItem
	{
		private ArrayList<Integer> list;
		private int index;
		
		private void initData()
		{
			list.add(R.drawable.yg_loading_src1);
			list.add(R.drawable.yg_loading_src2);
			list.add(R.drawable.yg_loading_src3);
			list.add(R.drawable.yg_loading_src4);
		}
		
		public RandomeItem()
		{
			list = new ArrayList<Integer>();
			index = 0;
			initData();
		}
		
		public int getResourceID()
		{
			int resID = list.get(index);
			index = (index + 1) % list.size();
			return resID;
		}
	}
	
	public CrossingAnimation(ImageView leftImg, ImageView rightImg)
	{
		this.leftImg = leftImg;
		this.rightImg = rightImg;
	}
	
	public void startAnimation()
	{
		shiftAnim(leftImg, SHIFT_RIGHT, 0);
		shiftAnim(rightImg, SHIFT_LEFT, 0);
	}
	
	private void shiftAnim(final ImageView img, final int orientation, int offset)
	{
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation shiftRight = new TranslateAnimation(0 + offset, SHIFT_DISTANCE * orientation, 0, 0);
		shiftRight.setDuration(offset != 0 ? 0 : ANIM_DURATION);
		//shiftRight.setFillAfter(true);
		set.addAnimation(shiftRight);
		shiftRight.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				AnimationSet set = new AnimationSet(true);
				TranslateAnimation shiftLeft;
				if (orientation == SHIFT_LEFT)
				{
					img.bringToFront();
					shiftLeft = new TranslateAnimation(SHIFT_DISTANCE * orientation, -1 * SHIFT_DISTANCE * orientation, 0, 0);
					shiftLeft.setDuration(ANIM_DURATION * 2);
				}
				else
				{
					shiftLeft = new TranslateAnimation(SHIFT_DISTANCE * orientation, 0, 0, 0);
					shiftLeft.setDuration(ANIM_DURATION);
					ScaleAnimation scale = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					scale.setDuration(ANIM_DURATION);
					set.addAnimation(scale);
				}
				set.addAnimation(shiftLeft);
				
				shiftLeft.setAnimationListener(new AnimationListener()
				{
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) 
					{
						int offset = 0;
						if (orientation == SHIFT_RIGHT)
							img.setImageResource(ri.getResourceID());
						else
							offset = SHIFT_DISTANCE;
						
						shiftAnim(img, -1 * orientation, offset);
					}
				});
				
				img.startAnimation(set);
			}
		});
		
		if (orientation == SHIFT_LEFT)
		{
			ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			scale.setDuration(ANIM_DURATION);
			set.addAnimation(scale);
		}
		
		img.startAnimation(set);
	}
}
