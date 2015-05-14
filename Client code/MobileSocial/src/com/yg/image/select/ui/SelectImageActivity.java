package com.yg.image.select.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import com.example.testmobiledatabase.R;
import com.yg.image.filter.ui.FilterActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SelectImageActivity extends Activity
{
	private static final String DEBUG_TAG = "SelectImageActivity______";
	public static final String RESULT_IMAGE_PATH = "IMAGE_PATH";
	public static final String FILTER_ENABLE = "FILTER_ENABLE";
	
	
	private static final int REQUEST_CAMERA = 0;
	private static final int REQUEST_GALLERY = 1;
	private static final int REQUEST_FILTER = 12;
	
	private Animation animation;
	private RelativeLayout topHolder;
	private RelativeLayout bottomHolder;
	private RelativeLayout middleCircle;
	private Uri imageUri;
	private boolean clickStatus = true;
	private boolean filterEnabled = false;
	private String preferredSelectionType = null;
	
	public static Bitmap bitmap = null;
	public static final String FROM_GALLERY = "from_gallery";
	public static final String FROM_CAMERA = "from_camera";
	public static final String SELECTION_TYPE = "selection_type";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_select_image_activity);
		
		//check if filter feature is needed or not
		filterEnabled = getIntent().getBooleanExtra(FILTER_ENABLE, false);
		//check if already set a selection type
		preferredSelectionType = getIntent().getStringExtra(SELECTION_TYPE);
		
		topHolder = (RelativeLayout) findViewById(R.id.yg_select_picture_top_holder);
		bottomHolder = (RelativeLayout) findViewById(R.id.yg_select_picture_bottom_holder);
		middleCircle = (RelativeLayout) findViewById(R.id.yg_select_picture_step_number);
		setupDialogActionBar();
		
		if (preferredSelectionType != null)
		{
			Log.i(DEBUG_TAG, preferredSelectionType);
			if (preferredSelectionType.equals(FROM_GALLERY))
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run() 
					{
						flyOut("displayGallery");						
					}
				}, 800);
			}
			else if (preferredSelectionType.equals(FROM_CAMERA))
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run() 
					{
						flyOut("displayCamera");
					}
				}, 800);
			}
		}
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_select_image_actionbar);
		
	
		LinearLayout back = (LinearLayout)findViewById(R.id.yg_select_image_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SelectImageActivity.this.finish();
			}
		});
	}

	@Override
	protected void onStart() 
	{
		overridePendingTransition(0, 0);
		flyIn();
		super.onStart();
	}
	
	private void flyIn()
	{
		Log.i(DEBUG_TAG, "flyIn");
		clickStatus = true;
		
		animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_holder_top);
		topHolder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_holder_bottom);
		bottomHolder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_middle_circle);
		middleCircle.startAnimation(animation);
	}

	@Override
	protected void onStop() 
	{
		overridePendingTransition(0, 0);
		super.onStop();
	}

	public void startGallery(View view)
	{
		flyOut("displayGallery");
	}

	public void startCamera(View view) 
	{
		flyOut("displayCamera");
	}
	
	private void flyOut(final String methodName)
	{
		Log.i(DEBUG_TAG, "flyOut outter");
		if (clickStatus)
		{

			Log.i(DEBUG_TAG, "flyOut inner");
			clickStatus = false;
			
			animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_middle_circle_back);
			middleCircle.startAnimation(animation);
	
			animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_holder_top_back);
			topHolder.startAnimation(animation);
	
			animation = AnimationUtils.loadAnimation(this, R.anim.yg_select_image_holder_bottom_back);
			bottomHolder.startAnimation(animation);
	
			animation.setAnimationListener(new AnimationListener() 
			{
				@Override
				public void onAnimationStart(Animation arg0) {
				}
	
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
	
				@Override
				public void onAnimationEnd(Animation arg0) 
				{
					callMethod(methodName);
				}
			});
		}
	}
	
	@SuppressWarnings("unused")
	private void displayGallery()
	{
		/*Intent intent = new Intent(SelectImageActivity.this, FilterActivity.class);
		startActivity(intent);*/
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) 
		{
			Intent intent = new Intent();
			intent.setType("image/jpeg");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUEST_GALLERY);
		} 
		else 
			Toast.makeText(getApplicationContext(), "Error: your SD-Card is not available.", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressWarnings("unused")
	private void displayCamera() 
	{
		imageUri = getOutputMediaFile();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}
	
	private Uri getOutputMediaFile()
	{
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "title");
		values.put(MediaStore.Images.Media.DESCRIPTION, "description");
		return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == REQUEST_CAMERA)
		{
			try
			{
				if (resultCode == RESULT_OK) 
					displayPhotoActivity();
				else 
					getApplicationContext().getContentResolver().delete(imageUri, null, null);
			} 
			catch (Exception e)
			{
				Toast.makeText(getApplicationContext(), "Image not found.(Camera)", Toast.LENGTH_SHORT).show();
			}
		}
		else if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY)
		{
			try 
			{
				imageUri = data.getData();
				displayPhotoActivity();
			} 
			catch (Exception e) 
			{
				Toast.makeText(getApplicationContext(), "Image not found.(Gallery)", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == REQUESTCODE_CROP && resultCode == RESULT_OK)
		{
			try
			{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);

				if (filterEnabled)
				{
					Intent it = new Intent(SelectImageActivity.this, FilterActivity.class);
					startActivityForResult(it, REQUEST_FILTER);
				}
				else
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
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					returnResult(imagePath);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else if (requestCode == REQUEST_FILTER && resultCode == RESULT_OK)
			returnResult(data.getStringExtra(FilterActivity.RESULT_IMAGE_PATH));
	}
	
	private void returnResult(String result)
	{
		Intent intent = SelectImageActivity.this.getIntent();
		intent.putExtra(RESULT_IMAGE_PATH, result);
		SelectImageActivity.this.setResult(RESULT_OK, intent);
		SelectImageActivity.this.finish();
	}
	
	private void displayPhotoActivity() 
	{
		startPhotoZoom(imageUri);
	}
	
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IMAGE_URI"));
	private static final int REQUESTCODE_CROP = 2;
	
	private void startPhotoZoom(Uri uri) 
	{  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 600); 
        intent.putExtra("outputY", 600);  
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUESTCODE_CROP);  
    }

	private void callMethod(String methodName)
	{
		if (methodName.equals("finish")) 
		{
			overridePendingTransition(0, 0);
			finish();
		}
		else 
		{
			try 
			{
				Method method = getClass().getDeclaredMethod(methodName);
				method.invoke(this, new Object[] {});
			}
			catch (Exception e) {}
		}
	}

	@Override
	public void onBackPressed() 
	{
		flyOut("finish");
		super.onBackPressed();
	}
}
