package com.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commons.CommonUtil;
import com.dialog.Dialog;
import com.example.testmobiledatabase.R;
import com.message.AbstractMessage;
import com.message.AudioMessage;
import com.message.ConvertUtil;
import com.message.DatabaseHandler;
import com.message.Recorder;
import com.message.TextMessage;
import com.user.ClientUser;
import com.user.FriendUser;

public class MainActivity extends Activity
{
	private static final String DEBUG_TAG = "______MainActivity";
	TextView tv;
	ImageView iv;
	Button start, stop, play, pause, load;
	MediaPlayer mPlayer;
	
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//user | password
		//238  | pwd238
		//89   | 89
		final ClientUser user = new ClientUser(89, "89", null, this);
		final FriendUser friend = new FriendUser(238, null, null, null, null, null, null, null, null);
		
		
		tv = (TextView)findViewById(R.id.show);
		iv = (ImageView)findViewById(R.id.img);
		start = (Button)findViewById(R.id.btn_start);
		stop = (Button)findViewById(R.id.btn_stop);
		play = (Button)findViewById(R.id.btn_play);
		pause = (Button)findViewById(R.id.btn_pause);
		load = (Button)findViewById(R.id.btn_load);
		tv.setMovementMethod(new ScrollingMovementMethod());

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
						user.signin();
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
				user.signoff();
				//Recorder.getInstance().stopRecordAndFile();
			}
		});
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				TextMessage txtMsg = new TextMessage(user.getID(), friend.getID(), ConvertUtil.string2Bytes("alohaµÄÊ¢´ó¸»ÎÌ!"), CommonUtil.now(), false);
				user.sendMsgTo(friend, txtMsg);
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
				String str = "";
				
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				for (int i = 0; i < list.size(); i++)
				{
					TextMessage textMsg = (TextMessage)(list.get(i));
					str += String.valueOf(textMsg.getFromUserID()) + "->" + String.valueOf(textMsg.getToUserID()) 
							+ " : " + textMsg.getText() + " at " + textMsg.getDate() + "\n";
				}
				
				tv.setText(str);
				
				/* mPlayer.release();  
				 mPlayer = null;  */
			}
		});
		load.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dialog = user.makeDialogWith(friend);
				/*generateData();
				getData(getDialog(2, 3));*/
			}
		});
		
		
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