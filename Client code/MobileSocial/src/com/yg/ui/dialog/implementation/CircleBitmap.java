package com.yg.ui.dialog.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class CircleBitmap 
{
	public static Bitmap circleBitmap(Bitmap bitmap)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Paint mPaint = new Paint();
		Canvas canvas = new Canvas(output);
		final int color = 0xff000000;
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		mPaint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		mPaint.setColor(color);
		final int width = bitmap.getWidth();
		canvas.drawCircle(width / 2, width / 2, width / 2, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, mPaint);
		return output;
	}
}