package com.tp.ui;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.testmobiledatabase.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageZoomInActivity extends Activity
{
	private Bitmap bmp;
	private ImageView img;
	private String imgPath;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tp_image_zoomin_activity);

		imgPath = getIntent().getStringExtra("Path");
		img = (ImageView) findViewById(R.id.img_zoomin_activity_IV);

		File imgFile = new File(imgPath);
		if (imgFile.exists())
		{
			bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ImageZoomInActivity.this).build();
			imageLoader.init(config);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.tp_loading_picture)
					.showImageOnFail(R.drawable.tp_loading_failed)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			imageLoader.displayImage("file://" + imgPath, img, options);
		} 
		else
		{
			bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ImageZoomInActivity.this).build();
			imageLoader.init(config);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.tp_loading_picture)
					.showImageOnFail(R.drawable.tp_loading_failed)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			imageLoader.displayImage(imgPath, img, options);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_UP:
			if (bmp != null)
				bmp.recycle();
			System.gc();
			ImageZoomInActivity.this.finish();
			break;
		default:
			break;
		}
		return true;
	}
}
