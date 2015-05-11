package com.lj.bazingaball;




import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.view.View;
public class BazingaButton extends View implements Comparable<BazingaButton>{

	//width and height of view
	public int width;    
	private int height;
	
	//velocity of view
	public float vx = 0;         
	public float vy = 0;
	
	//move range of view
	private int movewidth;  
	private int moveheight;
	
	public int id;
	
	//angle of rotate
	private float angle = 0;
	
	//bitmap of picture of view
	private Bitmap myMap;
	
	
	private float zoom = 1.0f;
	
	//self-adaption of canvas size
	private float scaleX;
	private float scaleY;
	
	//type of animation type
	public static final int NONE = 0;    //nothing to do
	public static final int REDUCTION = 1;    //reduce picture 
	public static final int MAGNIFICATION = 2;   //magnify picture
	
	//angle increase per step
	private final int ANGLE_PERSTEP = 5;
	
	//current animation type
	public int animationType = NONE;
	
	public static final int COLLISION_VIEW = 2;
	public static final int SWALLOW_VIEW = 1;
	public int viewtype = 0;
	
	public static float GAME_LEVEL = 2.2f;
	
	private boolean alive = true;
	
	/**
	 * set animation of view
	 * @param type : type of animation
	 * 	NONE : no animation
	 * 	REDUCTION : reduce animation
	 * 	AGNIFICATION : magnify animation
	 */
	public void setAnimation(int type)
	{
		animationType = type;
	}
	
	/**
	 * check size
	 * @param that : another object
	 * @return 
	 * 	true : this is bigger than that
	 * 	false : this is not bigger than that
	 */
	public boolean isBiggerThan(BazingaButton that)
	{
		RelativeLayout.LayoutParams a = (android.widget.RelativeLayout.LayoutParams) this.getLayoutParams();
		RelativeLayout.LayoutParams b = (android.widget.RelativeLayout.LayoutParams) that.getLayoutParams();
		if (a.width >= b.width)
			return true;
		else
			return false;
	}
	
	private int getMax(int []array)
	{
		int t = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (t < array[i])
				t = array[i];
		}
		return t;
	}
	
	private int getMin(int []array)
	{
		int t = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (t > array[i])
				t = array[i];
		}
		return t;
	}
	
	/**
	 * check whether swallow
	 * @param that : another object
	 * @return 
	 * 	true : swallow
	 * 	false : not swallow
	 */
	public boolean checkSwallow(BazingaButton that)
	{
		RelativeLayout.LayoutParams a = (android.widget.RelativeLayout.LayoutParams) this.getLayoutParams();
		RelativeLayout.LayoutParams b = (android.widget.RelativeLayout.LayoutParams) that.getLayoutParams();
		int []w = {a.leftMargin, a.leftMargin + width, b.leftMargin, b.leftMargin + that.width};
		int []h = {a.topMargin, a.topMargin + height, b.topMargin, b.topMargin + that.height};
		
		boolean r = getMax(w) - getMin(w) < width + that.width && getMax(h) - getMin(h) < height + that.height;
		return r;
	}

	/**
	 * calculate time of hit 
	 * @param that : another object
	 * @return time of hit
	 */
	public double isHit(BazingaButton that)
	{
		RelativeLayout.LayoutParams a = (android.widget.RelativeLayout.LayoutParams) that.getLayoutParams();
		RelativeLayout.LayoutParams b = (android.widget.RelativeLayout.LayoutParams) this.getLayoutParams();
		double dx = a.leftMargin + a.width / 2 - b.leftMargin - b.width / 2;
		double dy = a.topMargin + a.height / 2 - b.topMargin - b.height / 2;
		double dvx = that.vx - this.vx;
		double dvy = that.vy - this.vy;
		double dvdr = dx * dvx + dy * dvy;
		if (dvdr  > 0)
			return Double.POSITIVE_INFINITY;
		double dvdv = dvx * dvx + dvy * dvy;
		double drdr = dx *dx + dy * dy;
		double sigma = (double)that.width / 2 + (double)this.width / 2;
		double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
		if (d < 0)
			return Double.POSITIVE_INFINITY;
		
		return -(dvdr + Math.sqrt(d)) / dvdv;
	}
	
	/**
	 * calculate velocity after hit 
	 * @param that : another object
	 */
	public void hit(BazingaButton that)
	{
		RelativeLayout.LayoutParams la = (android.widget.RelativeLayout.LayoutParams) this.getLayoutParams();
		RelativeLayout.LayoutParams lb = (android.widget.RelativeLayout.LayoutParams) that.getLayoutParams();
		double arx = la.leftMargin + la.width / 2;
		double ary = la.topMargin + la.height / 2;
		double brx = lb.leftMargin + lb.width / 2;
		double bry = lb.topMargin + lb.height / 2;
		
		double sx = arx - brx;
		double sy = ary - bry;
		
		double s1x = sx / Math.sqrt(sx * sx + sy * sy);
		double s1y = sy / Math.sqrt(sx * sx + sy * sy);
		
		double tx = -sy;
		double ty = sx;
		
		double t1x = tx / Math.sqrt(tx * tx + ty * ty);
		double t1y = ty / Math.sqrt(tx * tx + ty * ty);
		
		double v1s = this.vx * s1x + this.vy * s1y;
		double v1t = this.vx * t1x + this.vy * t1y;
		
		double v2s = that.vx * s1x + that.vy * s1y;
		double v2t = that.vx * t1x + that.vy * t1y;
		
		double v1sf = v2s;
		double v2sf = v1s;
		
		double nsx = v1sf * s1x;
		double nsy = v1sf * s1y;
		double ntx = v1t * t1x;
		double nty = v1t * t1y;
		this.vx = (float) (nsx + ntx);
		this.vy = (float) (nsy + nty);
		
		nsx = v2sf * s1x;
		nsy = v2sf * s1y;
		ntx = v2t * t1x;
		nty = v2t * t1y;
		
		that.vx = (float) (nsx + ntx);
		that.vy = (float) (nsy + nty);
	}

	/**
	 * calculate swallow
	 * @param that : another object
	 */
	public void swallow(BazingaButton that)
	{
		RelativeLayout.LayoutParams a = (android.widget.RelativeLayout.LayoutParams) this.getLayoutParams();
		double t = Math.sqrt(this.width * this.width + that.width * that.width);
		a.width = new BigDecimal(t).setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
		a.height = a.width;
		height = width = a.width;
		scaleX = (float)a.width / (float)myMap.getWidth();
		scaleY = (float)a.height / (float)myMap.getHeight();
		super.setLayoutParams(a);
		animationType = MAGNIFICATION;
		that.setVisibility(View.GONE);
	}

	
	public BazingaButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public BazingaButton(Context context,AttributeSet attrs)
	{
		super(context, attrs); 
	}
	
	/**
	 * Initializes bazingabutton with context, velocity of x, velocity of y, range of activity of button
	 * @param context
	 * @param vx : velocity of x
	 * @param vy : velocity of y
	 * @param width : width of range
	 * @param height : height of range
	 */
	public BazingaButton(Context context, RelativeLayout.LayoutParams lp, int vx, int vy, int width, int height, int viewtype,int picture) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		myMap = BitmapFactory.decodeResource(getResources(), picture);
		this.setLayoutParams(lp);
		this.vx = vx;
		this.vy = vy;
		this.movewidth = width;
		this.moveheight = height;
		this.height = lp.height;
		this.width = lp.width;
		this.viewtype = viewtype;
		scaleX = (float)lp.width / (float)myMap.getWidth();
		scaleY = (float)lp.height / (float)myMap.getHeight();
		animationType = 0;
	}
	
	
	private void setZoom()
	{
		if (animationType == REDUCTION)
			zoom -= 0.05;
		if (animationType == MAGNIFICATION)
			zoom += 0.05;
		if (zoom == NONE)
			this.setVisibility(View.GONE);
		if (zoom >= 1.25)
			animationType = REDUCTION;
		if (animationType == REDUCTION && zoom <= 1.0)
		{
			zoom = 1.0f;
			animationType = 0;
		}
	}
	
	/**
	 * set velocity of button
	 * @param lp : layoutparams of button
	 */
	private void setVelocity(RelativeLayout.LayoutParams lp)
	{
		lp.leftMargin += vx;
		lp.topMargin += vy;
		if (lp.leftMargin + width > movewidth)
		{
			lp.leftMargin = movewidth - width;
			vx = 0 - vx;;
		}
		else if (lp.leftMargin < 0)
		{
			lp.leftMargin = 0;
			vx = 0 - vx;
		}
		
		if (lp.topMargin + height > moveheight)
		{
			lp.topMargin = moveheight - height;
			vy = 0 - vy;
		}
		else if (lp.topMargin < 0)
		{
			lp.topMargin = 0;
			vy = 0 - vy;
		}
	}
	
	private void drawPicture(Canvas canvas)
	{
		canvas.save();
		canvas.scale(scaleX * zoom, scaleY * zoom);
		canvas.rotate(angle,myMap.getWidth()/2,myMap.getHeight()/2);
		angle += ANGLE_PERSTEP;
		canvas.drawBitmap(myMap, 0, 0,null);
		canvas.restore();
	}
	
	private void drawFork(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(20);
		float start = 0.3f * width;
		canvas.drawLine(start, start, width - start, height - start, paint);
		canvas.drawLine(width - start, start, start, height - start, paint);
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
	synchronized (BazingaButton.class)
	{
	
		// TODO Auto-generated method stub
		setZoom();
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) super.getLayoutParams();
		
		
		setVelocity(lp);
		lp.width = (int) (width * zoom);
		lp.height = (int) (height * zoom);
		super.setLayoutParams(lp);
		
		drawPicture(canvas);
		
		if (ActivityBazingaBall.gamebegin == 1 || ActivityBazingaBall.gamebegin == 2)
		{
			if (width > ActivityBazingaBall.BUTTON_SIZE / GAME_LEVEL)
			{
				alive = false;
				drawFork(canvas);
				viewtype = COLLISION_VIEW;
			}
		}
		postInvalidateDelayed(20);
	}}

	@Override
	public int compareTo(BazingaButton arg0) {
		// TODO Auto-generated method stub
		return this.id - arg0.id;
	}
}
