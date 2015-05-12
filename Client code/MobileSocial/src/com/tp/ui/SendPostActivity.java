package com.tp.ui;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.testmobiledatabase.R;
import com.tp.messege.AbstractPost;
import com.tp.messege.PostManager;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SendPostActivity extends Activity
{
	private ImageView selectphotoIV;
	private ImageView postphotoIV;
	private EditText commentET;
	private Bitmap postphoto;
	private final int setpostphoto = 1;
	private final int intent = 2;
	private boolean isSendPhoto = false;
	
	
	private void sendPost()
	{
		final String posttext;
		final String sex = ConstantValues.user.getSex();
		final String location = "北京";
		if (commentET.getText().toString().trim().equals("") && isSendPhoto == false)
		{
			Toast.makeText(getApplicationContext(), "不能发送空的消息",Toast.LENGTH_SHORT).show();
		}
		else
		{
			posttext = commentET.getText().toString().trim();
			if (isSendPhoto == true)
			{
				isSendPhoto = false;
				if (commentET.getText().toString().trim().equals(""))   //发图片，没文字
				{
					Toast.makeText(getApplicationContext(), "发送图片",Toast.LENGTH_SHORT).show();
					Thread td = new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							ConstantValues.user.pm.addNewImagePost(now(), postphoto, location, sex);
							Message message = Message.obtain();
							message.what = intent;
							handler.sendMessage(message);
						}
					});
					td.start();
				}
				else                  //发图片，有文字
				{
					Thread td = new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							int postUserID = ConstantValues.user.getID();
							ConstantValues.user.pm.addNewImagePost(now(), postphoto, location, sex);
							AbstractPost tmp = ConstantValues.user.pm.getfriendpost().get(0);
							if (tmp != null)
							{
								ConstantValues.user.pm.addNewComment(-1, tmp.getPostID(), postUserID, -1, posttext, now(), sex);
								Message message = Message.obtain();
								message.what = intent;
								handler.sendMessage(message);
							}
							else
							{
								Toast.makeText(getApplicationContext(), "null",Toast.LENGTH_SHORT).show();
							}
						}
					});
					td.start();
				}
			}
			else                     //发文字
			{
				Thread td = new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						ConstantValues.user.pm.addNewTextPost(now(), posttext, location, sex);
						Message message = Message.obtain();
						message.what = intent;
						handler.sendMessage(message);
					}
				});
				td.start();
			}
		}
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.tp_sendpostactivity_actionbar);
		
		TextView title = (TextView)findViewById(R.id.tp_sendpostactivity_actionbar_title);
		title.setText("分享");
		LinearLayout back = (LinearLayout)findViewById(R.id.tp_sendpostactivity_actionbar_back);
		LinearLayout send = (LinearLayout)findViewById(R.id.tp_sendpostactivity_actionbar_send);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		send.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				sendPost();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tp_sendpostactivity);
		setupDialogActionBar();
		initview();
	}
	
	private void initview()
	{
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		selectphotoIV = (ImageView) findViewById(R.id.sendpostactivity_selectPhoto);
		postphotoIV = (ImageView) findViewById(R.id.sendpostactivity_postphoto);
		commentET = (EditText) findViewById(R.id.sendpostactivity_photoPostComment);
		selectphotoIV.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, ConstantValues.InstructionCode.REQUESTCODE_GALLERY);
			}
		});
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what) 
			{
			case setpostphoto:
				Drawable photo = new BitmapDrawable(postphoto);
				selectphotoIV.setVisibility(View.INVISIBLE);
				if (postphotoIV == null)
				{
					Log.d("postphotoIV________", "postphotoIV null");
					return ;
				}
				
				postphotoIV.setBackground(photo);
				postphotoIV.setVisibility(View.VISIBLE);
				break;
			case intent:
				isSendPhoto = false;
				Intent intent = new Intent(SendPostActivity.this, PublicActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) 
	{
		if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY)
		{
			if (resultCode == Activity.RESULT_OK) 
			{
				new Thread()
				{
					public void run() 
					{
						isSendPhoto = true;
						Uri selectedImage =  data.getData();
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						String picturePath = cursor.getString(columnIndex);
						cursor.close();
						FileInputStream fis = null;
						ExifInterface exif = null; 
						int degree=0;
						try 
						{
							fis = new FileInputStream(picturePath);
							
							/*BitmapFactory.Options options = new BitmapFactory.Options();
							options.inPurgeable = true;
							options.inInputShareable = true;
							options.inSampleSize = 2;
							postphoto = BitmapFactory.decodeStream(fis, null, options);*/
							//postphoto = BitmapFactory.decodeStream(fis);
							postphoto = decodeSampledBitmapFromPath(picturePath, dip2px(SendPostActivity.this,300), dip2px(SendPostActivity.this,300));
							Log.e("SPA____", dip2px(SendPostActivity.this,300) + " dp");
						} 
						catch (FileNotFoundException e1) 
						{
							e1.printStackTrace();
						}
						try 
						{
							exif = new ExifInterface(picturePath);
							if (exif != null) 
							{  
								// 读取图片中相机方向信息  
								int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);  
								// 计算旋转角度  
								switch (ori) 
								{  
								case ExifInterface.ORIENTATION_ROTATE_90:  
									degree = 90;  
									break;  
								case ExifInterface.ORIENTATION_ROTATE_180:  
									degree = 180;  
									break;  
								case ExifInterface.ORIENTATION_ROTATE_270:  
									degree = 270;  
									break;  
								default:  
									degree = 0;  
									break;  
								}
							}
							if (degree != 0) 
							{  
						         // 旋转图片  
						         Matrix m = new Matrix();  
						         m.postRotate(degree);  
						         postphoto = Bitmap.createBitmap(postphoto, 0, 0, postphoto.getWidth(), postphoto.getHeight(), m, true);  
						     }							
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						Message message = Message.obtain();
						message.what = setpostphoto;
						handler.sendMessage(message);
					}
				}.start();
			 }
			else
			{
				isSendPhoto = false;
				Log.d("onActivityResult","photo null");
			}
		}
	}
	
	private  String now()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	    return sdf.format(cal.getTime());
	}
	
	public Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) 
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
		        reqHeight);
		Log.e("SPA____", options.inSampleSize + " inSampleSize");
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		return bmp;
	}
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
		    if (width > height) {
		        inSampleSize = Math.round((float) height / (float) reqHeight);
		    } else {
		        inSampleSize = Math.round((float) width / (float) reqWidth);
		     }
		 }
		 return inSampleSize;
	}
	public int dip2px(Context context, float dpValue) 
	{  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 
}
