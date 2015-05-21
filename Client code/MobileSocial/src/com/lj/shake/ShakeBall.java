package com.lj.shake;


import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ShakeBall extends ImageView
{
	private Context gContext = null;
	private final int WALL_NONE = 0;
	private final int WALL_TOP = 1;
	private final int WALL_BOTTOM = 2;
	private final int WALL_LEFT = 3;
	private final int WALL_RIGHT = 4;
	
	private Handler gHandler = null;
	
	private int gMoveWidth = 0;
	private int gMoveHeight = 0;
	
	private boolean gStart = false;
	
	private int gVelocityX = 0;
	private int gVelocityY = 0;
	
	private Animation gRotateAnimation = null;
	private Animation gZoominAnimation = null;//= AnimationUtils.loadAnimation(gContext, R.anim.lj_shakeball_zoomin);
	
	public void startZoominAnimation()
	{
		gZoominAnimation.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation arg0) 
			{
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) 
			{
			}
			
			@Override
			public void onAnimationEnd(Animation arg0)
			{
				gHandler.sendEmptyMessage(ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_SHOW);
			}
		});
		this.startAnimation(gZoominAnimation);
	}
	
	public ShakeBall(Context context) 
	{
		super(context);
		gZoominAnimation = AnimationUtils.loadAnimation(context, R.anim.lj_shakeball_zoomin);
//		gContext = context;
	}
	
	public ShakeBall(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		gZoominAnimation = AnimationUtils.loadAnimation(context, R.anim.lj_shakeball_zoomin);
	//	gContext = context;
	}
	
	private void update()
	{
		int positionX = (int) (this.getX() + gVelocityX);
		int positionY = (int) (this.getY() + gVelocityY);
		int size = this.getWidth();
		
		int wall = getCollisionWall(positionX, positionY, size);
		setSpeed(wall);
		
		if (wall != WALL_NONE)
			gHandler.sendEmptyMessage(ConstantValues.InstructionCode.SHAKE_HANDLER_COLLISION);
		if (wall == WALL_LEFT)
			positionX = 0;
		else if(wall == WALL_RIGHT)
			positionX = gMoveWidth - size;
		else if (wall == WALL_TOP)
			positionY = 0;
		else if (wall == WALL_BOTTOM)
			positionY = gMoveHeight - size;
		
		this.layout(positionX, positionY, positionX + size, positionY + size);
	}
	
	private void setSpeed(int wall)
	{
		if (wall == WALL_NONE)
			return;
		else if (wall == WALL_LEFT || wall == WALL_RIGHT)
			gVelocityX  = -gVelocityX;
		else if (wall == WALL_TOP || wall == WALL_BOTTOM)
			gVelocityY = -gVelocityY;
	}
	
	private int getCollisionWall(float x, float y, int size)
	{
		if (x <= 0)
			return WALL_LEFT;
		else if (x >= gMoveWidth - size)
			return WALL_RIGHT;
		else if (y <= 0)
			return WALL_TOP;
		else if (y >= gMoveHeight - size)
			return WALL_BOTTOM;
		else
			return WALL_NONE;
	}
	
	public void setStartRotate(boolean flag)
	{
		gStart = flag;
		this.startAnimation(gRotateAnimation);
	}
	
	public void setRotateAnimation(int v)
	{
		int pivotType = Animation.RELATIVE_TO_SELF;
		gRotateAnimation = new RotateAnimation(0f, 360f, pivotType, 0.5f, pivotType, 0.5f);
		gRotateAnimation.setDuration(v);
		gRotateAnimation.setInterpolator(new LinearInterpolator());
		gRotateAnimation.setRepeatCount(-1);
	}
	
	public void setVelocity(int x, int y)
	{
		gVelocityX = x;
		gVelocityY = y;
	}
	
	public void setMoveRange(int width, int height)
	{
		gMoveWidth = width;
		gMoveHeight = height;
	}

	public void setHandler(Handler handler)
	{
		gHandler = handler;
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		if (gStart)
		{
			update();
			postInvalidateDelayed(5);
		}
	}
}
