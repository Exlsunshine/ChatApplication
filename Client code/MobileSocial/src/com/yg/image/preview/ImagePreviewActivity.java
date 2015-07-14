package com.yg.image.preview;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.testmobiledatabase.R;

public class ImagePreviewActivity extends Activity 
{
	public static final String IMG_PATH_TAG = "ImageDetailsActivity.ImagePathTag";

	private static final String DEBUG_TAG = "______ImageDetailsActivity";
	private Bitmap bmp;
	private ImageView img;
	private String imgPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.yg_image_preview_activity);
		
		imgPath = getIntent().getStringExtra(IMG_PATH_TAG);
		img = (ImageView)findViewById(R.id.img_preview_activity_img);
		
		File imgFile = new  File(imgPath);
		if(imgFile.exists())
		{
			bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    img.setImageBitmap(bmp);
		}
		else
			Log.e(DEBUG_TAG, "Error in loading image from the given path: " + imgPath);
	}
	
	/**
	 * Click anywhere to exit this current full screen view.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:
			if (bmp != null)
				bmp.recycle();
			System.gc();
			ImagePreviewActivity.this.finish();
			break;
		default:
			break;
		}
		
		return true;
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}*/
}