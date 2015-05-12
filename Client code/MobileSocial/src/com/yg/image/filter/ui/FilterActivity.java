package com.yg.image.filter.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.yg.image.select.ui.SelectImageActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends Activity
{
	//private static final String DEBUG_TAG = "FilterActivity______";
	public static final String RESULT_IMAGE_PATH = "IMAGE_PATH";
	/**
	 * Preview image.
	 */
	private ImageView imageView;
	/**
	 * Available filters.
	 */
	private FilterPreviewCache filterCache;
	/**
	 * Filters' sample preview image.
	 */
	private ArrayList<ImageView> samples;
	/**
	 * Previous selected filter's index.<br>
	 * The index belongs to {@link #samples}
	 * @see #samples
	 */
	private int previousSelection = 0;
	private int currentSelection = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_filter_activity);
		
		setupLayouts();
		setupDialogActionBar();
		
		/*BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = 1;
		filterCache = new FilterPreviewCache(BitmapFactory.decodeResource(getResources(), R.drawable.portrait, opt), imageView);
		*/
		filterCache = new FilterPreviewCache(SelectImageActivity.bitmap, imageView, FilterActivity.this);
	}
	
	private void setupLayouts()
	{
		imageView = (ImageView)findViewById(R.id.yg_filter_img);
		
		samples = new ArrayList<ImageView>();
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample0));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample1));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample2));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample3));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample4));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample5));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample6));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample7));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample8));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample9));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample10));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample11));
		samples.add((ImageView) findViewById(R.id.yg_holder_filter_sample12));
	}

	private void showCurrentSelection(int currentSelection)
	{
		samples.get(currentSelection).setScaleX(0.8f);
		samples.get(currentSelection).setScaleY(0.8f);
		
		/**
		 * If previous selected filter is not current selected filter(the initial condition),
		 * then scale the previous filter to original size.
		 */
		if (previousSelection != currentSelection)
		{
			samples.get(previousSelection).setScaleX(1.0f);
			samples.get(previousSelection).setScaleY(1.0f);
			previousSelection = currentSelection;
		}
	}
	
	public void onClickEffectButton(View view)
	{
		filterCache.applyFilterByTag(view.getTag().toString());
		currentSelection = Integer.parseInt(view.getTag().toString());
		
		showCurrentSelection(currentSelection);
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x00, 0x00, 0x00)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_filter_actionbar);
		
		LinearLayout back = (LinearLayout)findViewById(R.id.yg_filter_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FilterActivity.this.finish();
			}
		});
		
		TextView next = (TextView)findViewById(R.id.yg_filter_actionbar_next);
		next.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (filterCache.hasFinished(String.valueOf(currentSelection)))
				{
					filterCache.clearCacheExcept(String.valueOf(currentSelection));
					
					Intent intent = FilterActivity.this.getIntent();
					intent.putExtra(RESULT_IMAGE_PATH, filterCache.getFilterBmpPath(String.valueOf(currentSelection)));
					FilterActivity.this.setResult(RESULT_OK, intent);
					FilterActivity.this.finish();
				}
				else
					Toast.makeText(FilterActivity.this, "还在处理中...稍安勿躁:)", Toast.LENGTH_SHORT).show();
			}
		});
	}
}