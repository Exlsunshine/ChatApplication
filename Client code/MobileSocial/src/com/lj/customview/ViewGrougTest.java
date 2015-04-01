package com.lj.customview;


import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ViewGrougTest extends ViewGroup{

	UserDataWindowView userDataWindowView;
	
	private Button btn;
	int position_X;
	int position_Y;
	public ViewGrougTest(Context context, int x, int y, String nickname, Typeface tf, int gametype) {
		super(context);
		// TODO Auto-generated constructor stub
		position_X = x;
		position_Y = y;
		userDataWindowView = new UserDataWindowView(context, x, y, nickname, tf, gametype);
		addView(userDataWindowView);
		btn = new Button(context);
		addView(btn);
		btn.setText("Game");
	}
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		View v = getChildAt(0);
        v.layout(0, 0, 3000, 3000);
        if (userDataWindowView.getFlag())
        {
        	v = getChildAt(1);
        	v.layout(position_X + 220, position_Y + 250, position_X + 220 + 250, position_Y + 250 + 100);
        	btn.setText("Game");
        }
	}
	public void setPosition(float x, float y)
	{
		position_X = (int)x;
		position_Y = (int)y;
		userDataWindowView.setPosition(x, y);
		View v = getChildAt(1);
    	v.layout(position_X + 220, position_Y + 250, position_X + 220 + 250, position_Y + 250 + 100);
	//	v.layout(position_X, position_Y + 250, position_X+ 250, position_Y + 250 + 100);
	}
	public float getPos_X()
	{
		return position_X;
	}
	public float getPos_Y()
	{
		return position_Y;
	}

}
