package com.yg.image.filter.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.yg.image.filter.filters.BlockPrintFilter;
import com.yg.image.filter.filters.ColorQuantizeFilter;
import com.yg.image.filter.filters.ComicFilter;
import com.yg.image.filter.filters.EdgeFilter;
import com.yg.image.filter.filters.IImageFilter;
import com.yg.image.filter.filters.MistFilter;
import com.yg.image.filter.filters.OldPhotoFilter;
import com.yg.image.filter.filters.RaiseFrameFilter;
import com.yg.image.filter.filters.SaturationModifyFilter;
import com.yg.image.filter.filters.SepiaFilter;
import com.yg.image.filter.filters.SoftGlowFilter;
import com.yg.image.filter.filters.VintageFilter;
import com.yg.image.filter.filters.ZoomBlurFilter;

public class FilterPreviewCache
{
	private static final String DEBUG_TAG = "FilterPreviewCache______";
	/**
	 * key   represents the filter name<br>
	 * value represents the path of the image which has been applied with the filter.
	 */
	private HashMap<String, FilterCacheItem> filterCache;
	private List<IImageFilter> avaliableFilters;
	private Bitmap target;
	private ImageView imageview;
	private Context context;
	
	/**
	 * Filter preview class.
	 * @param target the bitmap which you want to apply filter to.
	 */
	public FilterPreviewCache(Bitmap target, ImageView imageview, Context context)
	{
		this.imageview = imageview;
		this.target = target;
		this.context = context;
		this.imageview.setImageBitmap(target);
		
		avaliableFilters = new ArrayList<IImageFilter>();
		filterCache = new HashMap<String, FilterCacheItem>();
		loadFilters();
		saveTragetBmp(target);
		//preProcess();
	}
	
	private void saveTragetBmp(Bitmap bmp)
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
			}
		}
		
		filterCache.put("0", new FilterCacheItem(imagePath, FilterCacheItem.STATUS_DONE));
	}
	
	/*private void preProcess()
	{
		for (int i = 2; i <= 12; i++)
			processImage(String.valueOf(i), true);
	}*/
	
	/**
	 * Apply specific filter, and show the filter result on imageview.
	 * @param tag the filter's tag.
	 * @param imageview the imageview which will show the filter result.
	 */
	synchronized public void applyFilterByTag(String tag)
	{
		//Zero tag means apply no filters. Just show the original bitmap.
		if (tag.equals("0"))
		{
			imageview.setImageBitmap(target);
			return;
		}
		
		if (filterCache.containsKey(tag))
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			
			imageview.setImageBitmap(BitmapFactory.decodeFile(filterCache.get(tag).getFilterPath(), options));
		}
		else
		{
			Toast.makeText(context, "´¦ÀíÖÐ...", Toast.LENGTH_LONG).show();
			processImage(tag, false);
		}
	}
	
	private void processImage(String tag, boolean isPreProcess)
	{
		IImageFilter filter = avaliableFilters.get(Integer.parseInt(tag) - 1);
		
		if (isPreProcess)
			new ProcessImageTask(filter, target, tag, filterCache, null).execute();
		else
			new ProcessImageTask(filter, target, tag, filterCache, imageview).execute();
	}
	
	/**
	 * Get the path of the bitmap file which has been applied by the specific filter.
	 * @param tag filter's tag
	 * @return path of the bitmap file<br>
	 * null indicates we can not find the file with the given tag.
	 */
	public String getFilterBmpPath(String tag)
	{
		return filterCache.get(tag).getFilterPath();
	}
	
	
	public void clearCacheExcept(final String tag)
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				if (!filterCache.containsKey(tag))
				{
					Log.e(DEBUG_TAG, "Filter cache does not containts tag: " + tag);
					return;
				}
				
				for (String key : filterCache.keySet())
				{
					if (!key.equals(tag))
					{
						File file = new File(filterCache.get(key).getFilterPath());
						boolean deleted = file.delete();
						Log.i(DEBUG_TAG, "Delete filter cache status:" + deleted + ". (" + key + ")");
					}
				}
			}
		});
		td.start();
	}
	
	/**
	 * Check if the given tag filter finishes processing.
	 * @param tag filter's tag
	 * @return true if has finished<br>
	 * false otherwise
	 */
	public boolean hasFinished(String tag)
	{
		boolean result = false;
		
		if (filterCache.containsKey(tag))
		{
			if (filterCache.get(tag).getStatus() == FilterCacheItem.STATUS_DONE)
				result = true;
		}
		
		return result;
	}
	
	private void loadFilters() 
	{
		avaliableFilters.add(new ZoomBlurFilter(30));
		avaliableFilters.add(new SoftGlowFilter(10, 0.1f, 0.1f));
		avaliableFilters.add(new RaiseFrameFilter(20));
		avaliableFilters.add(new ComicFilter());
		avaliableFilters.add(new EdgeFilter());
		avaliableFilters.add(new BlockPrintFilter());
		avaliableFilters.add(new MistFilter());
		avaliableFilters.add(new SaturationModifyFilter());
		avaliableFilters.add(new ColorQuantizeFilter());
		avaliableFilters.add(new VintageFilter());
		avaliableFilters.add(new OldPhotoFilter());
		avaliableFilters.add(new SepiaFilter());
	}
}
