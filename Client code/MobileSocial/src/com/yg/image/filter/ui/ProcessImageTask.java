package com.yg.image.filter.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.yg.image.filter.filters.IImageFilter;
import com.yg.image.filter.filters.Image;

public class ProcessImageTask extends AsyncTask<Void, Void, Bitmap>
{
	private IImageFilter filter;
    private Bitmap target = null;
    private String tag;
    private HashMap<String, String> filterCache;
    private ImageView imageview;
    
    private Image img = null;
    
    public ProcessImageTask(IImageFilter imageFilter, Bitmap target, 
    		String tag, HashMap<String, String> filterCache, ImageView imageview) 
	{
		this.filter = imageFilter;
		this.target = target;
		this.tag = tag;
		this.filterCache = filterCache;
		this.imageview = imageview;
	}

	@Override
	protected void onPreExecute()
	{
		if (img != null)
		{
			img.destImage.recycle();
			img.destImage = null;
		}
		super.onPreExecute();
	}

	public Bitmap doInBackground(Void... params)
	{
		try
    	{
			img = new Image(target);
			if (filter != null)
			{
				img = filter.process(img);
				img.copyPixelsFromBuffer();
			}
			
			return img.getImage();
    	}
		catch(Exception e)
		{
			if (img != null && img.destImage != null && img.destImage.isRecycled()) 
			{
				img.destImage.recycle();
				img.destImage = null;
			}
		}
		finally
		{
			if (img != null && img.image!= null && img.image.isRecycled()) 
			{
				img.image.recycle();
				img.image = null;
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result)
	{
		if(result != null)
		{
			super.onPostExecute(result);
			filterCache.put(tag, retriveSaveBmpPath(result));
			
			if (imageview != null)
				imageview.setImageBitmap(result);
		}
	}
	
	private String retriveSaveBmpPath(Bitmap bmp)
	{
		String imagePath = Environment.getExternalStorageDirectory() + "/JMMSR/filters/" +
				System.currentTimeMillis() + ".JPG";
		
		File imgFile = new File(imagePath);
		imgFile.getParentFile().mkdirs();
		
		if (!imgFile.exists())
		{
			try
			{
				imgFile.createNewFile();
				FileOutputStream out = new FileOutputStream(imagePath);
				bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return imagePath;
	}
}