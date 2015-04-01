package com.lj.customview;


import com.example.testmobiledatabase.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class UserDataWindowView extends View{

	public static final int H = 500;
	private Bitmap background;
	private float position_X;
	private float position_Y;
	private int backgroundWitdh;
	private int backgroundHeight;
	private int hu = 70;
	private int hd = 80;
	
	private int gameType;
	private String nickName;
	
	private boolean flag = false;
	
	
	private Typeface typeFace;
	public UserDataWindowView(Context context, float x, float y, String nickname, Typeface tf, int gametype) {
		super(context);
		// TODO Auto-generated constructor stub
		position_X = x;
		position_Y = y;
		nickName = nickname;
		background = BitmapFactory.decodeResource(getResources(), R.drawable.user_window);
		
		backgroundWitdh = background.getWidth();
		backgroundHeight = background.getHeight();
		typeFace = tf;
		gameType = gametype;
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
	public boolean getFlag() {
		return flag;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.translate(position_X, position_Y);
		if (hd + hu < backgroundHeight)
		{
			Bitmap a = Bitmap.createBitmap(background, 0, 0, backgroundWitdh,  hu);
			Bitmap b = Bitmap.createBitmap(background, 0, 368, backgroundWitdh,  hd);
			canvas.drawBitmap(a, 0, 0, null);
			canvas.drawBitmap(b, 0, hu, null);
			hu += 20;
		}
		else
		{
			canvas.drawBitmap(background, 0, 0, null);
			
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
		//	paint.setTypeface(typeFace);
			paint.setTextSize(75);
			canvas.drawText(nickName, 10, 150, paint);
			canvas.drawText("GameType : " + gameType, 10, 150 + 75, paint);
			flag = true;
		}
		postInvalidateDelayed(5);
	}
	
	
}
