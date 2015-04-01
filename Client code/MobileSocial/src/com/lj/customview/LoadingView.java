package com.lj.customview;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

public class LoadingView extends View
{
	Bitmap bitmap;
	int angle = 0;
	int time = 0;//90
	float ANGLE_PERSTEP = 5;
	float ang = ANGLE_PERSTEP / 2;
	float position_X;
	float position_Y;
	int picture = R.drawable.red;
	//350  
	//offset 30,40,50,60,70
	//0.3f 0.25f 0.2f 0.15f 0.1f

	float[] angles = {220,150,90, 40, 0};
//	float[] scales = {0.2f, 0.18f, 0.16f, 0.14f, 0.12f};
//	float[] offsets = {30,40,50,60,70};
//	float[] scales = {0.3f, 0.25f, 0.2f, 0.15f, 0.1f};
	float[] scales = {0.08f, 0.06f, 0.04f, 0.035f, 0.03f};
	float[] offsets = {10,20,25,30,35};
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	//float[] angle_prestep ={3.6f, 3.6f ,3.6}
	public LoadingView(Context context, float x, float y) {
		super(context);
		// TODO Auto-generated constructor stub
		position_X = x;
		position_Y = y;
		bitmap = BitmapFactory.decodeResource(getResources(), picture);
		
		for (int i = 0; i < scales.length; i++)
		{
			Matrix matrix = new Matrix();
			matrix.postScale(scales[i], scales[i]);
			Bitmap s = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), picture),0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
			bitmaps.add(s);
		}
	}
	
	
	
	public LoadingView(Context context, float zoomScale)
	{
		super(context);
		Matrix matrix = new Matrix();
		matrix.postScale(zoomScale, zoomScale);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red);
		bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	}
	public void setPosition(float x, float y)
	{
		position_X = x;
		position_Y = y;
	}
	public float getPos_X()
	{
		return position_X;
	}
	public float getPos_Y()
	{
		return position_Y;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);	
		
		canvas.translate(position_X, position_Y);
		for (int i = 0; i < scales.length; i++)
		{
			if (angles[i] <= 270)
				angles[i] += ANGLE_PERSTEP;
			else
				angles[i] += ang;
			canvas.save();
			canvas.rotate(angles[i],0,0);
			canvas.drawBitmap(bitmaps.get(i), offsets[i], offsets[i], null);
			canvas.restore();
			angles[i] = angles[i] % 360;
		}
		postInvalidateDelayed(10);
	}
}
