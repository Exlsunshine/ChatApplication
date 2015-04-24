package com.tp.ui;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.example.testmobiledatabase.R;
import com.tp.messege.AbstractPost;
import com.tp.messege.PostManager;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu)
	{  
        getMenuInflater().inflate(R.menu.tp_sendpost_menu, menu);  
        return true;  
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();
	    switch (id) 
	    {
	        case R.id.action_send:
	        	sendPost();
	            break;
	        default:
	            break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void sendPost()
	{
		final String posttext;
		final String sex = "female";
		final String location = "北京";
		if (commentET.getText().toString().trim().equals("") && isSendPhoto == false)
		{
			Toast.makeText(getApplicationContext(), "无法发送",Toast.LENGTH_SHORT).show();
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
							StaticUser.pm.addNewImagePost(CommonUtil.now(), postphoto, location, sex);
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
							int postUserID = 4;
							StaticUser.pm.addNewImagePost(CommonUtil.now(), postphoto, location, sex);
							AbstractPost tmp = StaticUser.pm.getfriendpost().get(0);
							if (tmp != null)
							{
								StaticUser.pm.addNewComment(-1, tmp.getPostID(), postUserID, -1, posttext, CommonUtil.now(), sex);
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
						StaticUser.pm.addNewTextPost(CommonUtil.now(), posttext, location, sex);
						Message message = Message.obtain();
						message.what = intent;
						handler.sendMessage(message);
					}
				});
				td.start();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tp_sendpostactivity);
		initview();
		StaticUser.pm = new PostManager(4);
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
						FileInputStream fis;
						try 
						{
							fis = new FileInputStream(picturePath);
							postphoto = BitmapFactory.decodeStream(fis);
						} 
						catch (FileNotFoundException e) 
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
}
