package com.lj.songpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ViewTouchRotate extends ImageView
{
	private Bitmap gViewImage;
	private int gAngle = 0;
	private double gStartAngle = 0;
	
	public ViewTouchRotate(Context context) 
	{
		super(context);
	}
	
	public ViewTouchRotate(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	
	
	public ViewTouchRotate(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setGearImage(Bitmap bitmap)
	{
		LayoutParams layout = this.getLayoutParams();
		Log.e("s", bitmap.getWidth() + " " + bitmap.getHeight());
		Log.e("ss", layout.width + " " + layout.height);
		Matrix matrix = new Matrix();
		matrix.postScale((float)layout.width / bitmap.getWidth(), (float)layout.height / bitmap.getHeight()); 
		gViewImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		super.onTouchEvent(event);
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN)
			gStartAngle = getAngle(event.getX(), event.getY());
		else if (action == MotionEvent.ACTION_MOVE)
		{
			double currentAngle = getAngle(event.getX(), event.getY());
			double angle = gStartAngle - currentAngle;
			if (Math.abs(angle) >= 200)
				angle = (angle > 0) ? 360 - angle : 360 + angle;
			changeAngle(angle);
			gStartAngle = currentAngle;
		}
		return true;
	}
	
	public void changeAngle(double angle)
	{
		gAngle += angle;
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		canvas.rotate(gAngle, gViewImage.getWidth()/2, gViewImage.getHeight()/2);
		canvas.drawBitmap(gViewImage, 0, 0,null);
	}
	
	private double getAngle(double xTouch, double yTouch) 
	{
	    double x = xTouch - (gViewImage.getWidth() / 2d);
	    double y = gViewImage.getHeight() - yTouch - (gViewImage.getHeight() / 2d);
	  
	    switch (getQuadrant(x, y)) 
	    {
	        case 1:
	            return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        case 2:
	            return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        case 3:
	            return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	        case 4:
	            return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
	        default:
	            return 0;
	    }
	}
	
	private static int getQuadrant(double x, double y) 
	{
	    if (x >= 0) 
	        return y >= 0 ? 1 : 4;
	    else 
	        return y >= 0 ? 2 : 3;
	}
}
