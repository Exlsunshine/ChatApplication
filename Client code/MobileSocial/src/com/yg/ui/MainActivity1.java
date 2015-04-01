package com.yg.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ConvertUtil;
import com.yg.message.DatabaseHandler;
import com.yg.message.ImageMessage;
import com.yg.message.Recorder;
import com.yg.user.ClientUser;
import com.yg.user.FriendUser;

public class MainActivity1 extends Activity
{
	private static final String DEBUG_TAG = "______MainActivity";
	TextView tv;
	ImageView iv;
	Button start, stop, play, pause, load;
	MediaPlayer mPlayer;
	
	private int index = 0;
	private int inner = 0;
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		
		//		 user | password
		//user1: 238  | pwd238
		//user2: 89   | 89
		final ClientUser user = new ClientUser(4, "4", null, this);
		final FriendUser friend = new FriendUser(5, null, null, null, null, null, null, null, null);
		
		tv = (TextView)findViewById(R.id.show);
		iv = (ImageView)findViewById(R.id.img);
		start = (Button)findViewById(R.id.btn_start);
		stop = (Button)findViewById(R.id.btn_stop);
		play = (Button)findViewById(R.id.btn_play);
		pause = (Button)findViewById(R.id.btn_pause);
		load = (Button)findViewById(R.id.btn_load);
		tv.setMovementMethod(new ScrollingMovementMethod());

		iv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				iv.setBackground(ConvertUtil.bitmap2Drawable(ConvertUtil.bytes2Bitmap(user.getFriendList().get(index).getPortrait()), MainActivity1.this));
				index = (index + 1) % user.getFriendList().size();
			}
		});
		start.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run() 
					{
						
						Intent it = new Intent(MainActivity1.this, MainActivity.class);
						startActivity(it);
						
						/*Intent it = new Intent(MainActivity1.this, Login.class);
						startActivity(it);*/
						//user.signin();
						//Recorder.getInstance().startRecordAndFile();
					}
				});
				td.start();
			}
		});
		stop.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				//getFriendList unit test
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						user.getFriendList();
					}
				});
				td.start();
				//Recorder.getInstance().stopRecordAndFile();
			}
		});
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				/*
				//send TextMessage unit test
				TextMessage txtMsg = new TextMessage(user.getID(), friend.getID(), ConvertUtil.string2Bytes("alohaµÄÊ¢´ó¸»ÎÌ!"), CommonUtil.now(), false);
				user.sendMsgTo(friend, txtMsg);
				*/
				
				
				//send ImageMessage unit test
				Bitmap bmp = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/test.jpg");
				ImageMessage imgMsg = new ImageMessage(user.getID(), friend.getID(), bmp, CommonUtil.now(), false);
				user.sendMsgTo(friend, imgMsg);
				
				
				
				/*//send AudioMessage unit test
				try {
					AudioMessage audioMsg = new AudioMessage(user.getID(), friend.getID(), ConvertUtil.amr2Bytes("/mnt/sdcard/TestMobileDatabase/voice/FinalAudio.amr"), CommonUtil.now(), false);
					user.sendMsgTo(friend, audioMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				
				
				/*mPlayer = new MediaPlayer();  
				try
				{  
					mPlayer.setDataSource(Recorder.getInstance().getAMRFilePath());  
					mPlayer.prepare();  
					mPlayer.start();  
				} catch (IOException e) {  
					Log.e("___________", "prepare() failed");  
				}*/
			}
		});
		
		
		pause.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent it = new Intent(MainActivity1.this, ClientUserUnitTest.class);
				startActivity(it);
				/*
				//receive ImageMessage unit test
				String str = "";
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				Log.i(DEBUG_TAG, String.valueOf(inner) + "/" + String.valueOf(list.size()));
				if (list.get(inner).getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE)
				{
					ImageMessage imgMsg = (ImageMessage)(list.get(inner));
					str = String.valueOf(imgMsg.getFromUserID()) + "->" + String.valueOf(imgMsg.getToUserID()) 
							+ " at " + imgMsg.getDate() + "\n";
					iv.setBackground(ConvertUtil.bitmap2Drawable(imgMsg.getImage(), MainActivity.this));
					Log.i(DEBUG_TAG, str);
				}
				inner = (inner + 1) % list.size();
				*/
				
				/*
				//receive TextMessage unit test
				String str = "";
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				for (int i = 0; i < list.size(); i++)
				{
					if (list.get(i).getMessageType() != ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT)
						continue;
					
					TextMessage textMsg = (TextMessage)(list.get(i));
					str += String.valueOf(textMsg.getFromUserID()) + "->" + String.valueOf(textMsg.getToUserID()) 
							+ " : " + textMsg.getText() + " at " + textMsg.getDate() + "\n";
				}
				tv.setText(str);
				*/
				
				/* mPlayer.release();  
				 mPlayer = null;  */
			}
		});
		load.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						dialog = user.makeDialogWith(friend);
					}
				});
				td.start();
				/*generateData();
				getData(getDialog(2, 3));*/
			}
		});
		
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	private ArrayList<AbstractMessage> getDialog(int userID1, int userID2)
	{
		Dialog dialog = new Dialog(userID1, userID2, this);
		return dialog.getDialogHistory();
	}
	
	private void getData(ArrayList<AbstractMessage> list)
	{
		for (int i = 0; i <list.size(); i++)
		{
			Log.i(DEBUG_TAG, "Message id:\r\t" + String.valueOf(list.get(i).getID()));
			Log.i(DEBUG_TAG, "From:\r\t" + String.valueOf(list.get(i).getFromUserID()));
			Log.i(DEBUG_TAG, "To:\r\t" + String.valueOf(list.get(i).getToUserID()));
			Log.i(DEBUG_TAG, "Date:\r\t" + String.valueOf(list.get(i).getDate()));
		}
	}
	
	private void generateData()
	{
		DatabaseHandler db = new DatabaseHandler(this);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
		
		try {
			AudioMessage tm = new AudioMessage(2, 3, ConvertUtil.amr2Bytes(Recorder.getInstance().getAMRFilePath()), dateFormat.format(new Date()), false);
			db.insertMessage(tm);
			Log.i(DEBUG_TAG, "After insert id is: " + "\t" + String.valueOf(tm.getID()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*private void playFromFile(File audioFile)
	{
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(audioFile.getPath());
			mPlayer.prepare();
	        mPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playMp3(byte[] mp3SoundByteArray) 
	{
		System.out.println();
	    try {
	        // create temp file that will hold byte array
	        File tempMp3 = File.createTempFile("audio", "amr", getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(mp3SoundByteArray);
	        fos.close();

	        // Tried reusing instance of media player
	        // but that resulted in system crashes...  
	        mPlayer = new MediaPlayer();

	        // Tried passing path directly, but kept getting 
	        // "Prepare failed.: status=0x1"
	        // so using file descriptor instead
	        FileInputStream fis = new FileInputStream(tempMp3);
	        mPlayer.setDataSource(fis.getFD());

	        mPlayer.prepare();
	        mPlayer.start();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
	MediaPlayer mPlayer;
	private void startPlaying() 
	 {
		 
		 mPlayer = new MediaPlayer();  
	        try {  
	            mPlayer.setDataSource(getAMRFilePath());  
	            mPlayer.prepare();  
	            mPlayer.start();  
	        } catch (IOException e) {  
	            Log.e("___________", "prepare() failed");  
	        }  
	 } 
	
	private void stopPlaying() {  
	        mPlayer.release();  
	        mPlayer = null;  
	    } 
	
	private String getAMRFilePath()
    {
		String fileBasePath ="/mnt/sdcard/TestMobileDatabase/voice/";
		File forlder = new File(fileBasePath);
		if (!forlder.exists())
		{
			boolean res = forlder.mkdir();
			System.out.println(String.valueOf(res) + "\t" + "mkdir");
		}
    		System.out.println("OK");
    		fileBasePath = fileBasePath + "FinalAudio.amr";

    		Log.w("_______________", fileBasePath);
        return fileBasePath;
    }
	*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}