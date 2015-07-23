package com.lj.driftbottle.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class LineTextView extends TextView
{
	private Paint paint;
	public LineTextView(Context context) 
	{
		super(context);
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
	}
	
	public LineTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int lineCount = getLineCount();
		int lineHeight = getLineHeight() + (int)getLineSpacingExtra();
		for (int i = 0; i < lineCount; i++) 
		{
			int lineY = (i + 1) * lineHeight;
			canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
		}
	}

}
