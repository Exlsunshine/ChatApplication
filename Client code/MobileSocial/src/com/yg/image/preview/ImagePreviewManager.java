package com.yg.image.preview;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ImagePreviewManager
{
	private static final String DEBUG_TAG = "______ImageDetailCacheManager";
	private static final int IMAGE_SAVING_COMPLETED = 0x01;
	private static final String IMAGE_CACHE_DIR = Environment.getExternalStorageDirectory() + File.separator + "MobileSocial/ImageCache";
	private HashMap<Integer, String> map;
	private Context context;
	private Handler handler;
	
	@SuppressLint("UseSparseArrays")
	public ImagePreviewManager(Context context)
	{
		this.context = context;
		this.map = new HashMap<Integer, String>();
		this.handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				switch (msg.what)
				{
				case IMAGE_SAVING_COMPLETED:
					int key = msg.arg1;
					String path = (String) msg.obj;
					map.put(key, path);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	
	public boolean contains(Integer key)
	{
		return map.containsKey(key);
	}
	
	public void push(Integer key, String value)
	{
		if (contains(key))
			return;
			
		map.put(key, value);
	}
	
	public void push(final Integer key, final ImageView img)
	{
		if (contains(key))
			return;
		
		img.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(context, ImagePreviewActivity.class);
				intent.putExtra(ImagePreviewActivity.IMG_PATH_TAG, get(key));
				context.startActivity(intent);
			}
		});
		
		saveImageFromImageView(key, img);
	}

	public String remove(Integer key)
	{
		return map.remove(key);
	}
	
	public String get(Integer key)
	{
		return map.get(key);
	}
	
	private void saveImageFromImageView(final Integer key, final ImageView img)
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				BitmapDrawable btmpDr = (BitmapDrawable) img.getDrawable();
				Bitmap bmp = btmpDr.getBitmap();

				/*File sdCardDirectory = Environment.getExternalStorageDirectory();*/
				try
				{
				    File sdCardDirectory = new File(IMAGE_CACHE_DIR);
				    sdCardDirectory.mkdirs();

				    String imageNameForSDCard = "cached_image_" + System.currentTimeMillis() + ".jpg";

				    File image = new File(sdCardDirectory, imageNameForSDCard);
				    FileOutputStream outStream;

				    outStream = new FileOutputStream(image);
				    bmp.compress(Bitmap.CompressFormat.JPEG, 80, outStream); 
				    /* 100 to keep full quality of the image */
				    outStream.flush();
				    outStream.close();

				    //Refreshing SD card
				    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
				    
				    Log.d(DEBUG_TAG, "Successfully saved image to " + image.getAbsolutePath());
				    Message msg = new Message();
				    msg.what = IMAGE_SAVING_COMPLETED;
				    msg.obj = image.getAbsolutePath();
				    msg.arg1 = key;
				    handler.sendMessage(msg);
				}
				catch (Exception e) 
				{
				    e.printStackTrace();
				    Toast.makeText(context, "Image could not be saved : Please ensure you have SD card installed " + "properly", Toast.LENGTH_LONG).show();
				    Log.e(DEBUG_TAG, "Failed to save image.");
				}
			}
		});
		td.start();
	}
}