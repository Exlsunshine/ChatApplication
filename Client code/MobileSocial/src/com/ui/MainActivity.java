package com.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.testmobiledatabase.R;
import com.message.AbstractMessage;
import com.message.ConvertUtil;
import com.message.DatabaseHandler;
import com.message.TextMessage;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private static final String DEBUG_TAG = "______MainActivity";
	TextView tv;
	ImageView iv;
	Button start, stop, play, pause;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		tv = (TextView)findViewById(R.id.show);
		iv = (ImageView)findViewById(R.id.img);
		start = (Button)findViewById(R.id.btn_start);
		stop = (Button)findViewById(R.id.btn_stop);
		play = (Button)findViewById(R.id.btn_play);
		pause = (Button)findViewById(R.id.btn_pause);
		
		generateData();
		getData();
	}
	
	
	private void getData()
	{
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<AbstractMessage> list = db.getAllMsg(1, 2);
		
		for (int i = 0; i <list.size(); i++)
		{
			Log.i(DEBUG_TAG, "Message id: " + String.valueOf(list.get(i).getID()));
			Log.i(DEBUG_TAG, "From: " + String.valueOf(list.get(i).getFromUserID()));
			Log.i(DEBUG_TAG, "To: " + String.valueOf(list.get(i).getToUserID()));
			Log.i(DEBUG_TAG, "Content: " + String.valueOf(ConvertUtil.bytes2String(list.get(i).getContent())));
		}
	}
	
	private void generateData()
	{
		DatabaseHandler db = new DatabaseHandler(this);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		TextMessage tm = new TextMessage(1, 2, ConvertUtil.string2Bytes("1->2This is another test.这是一个测试。"), dateFormat.format(date), false);
		db.insertMessage(tm);
		Log.i(DEBUG_TAG, "After insert id is: " + "\t" + String.valueOf(tm.getID()));
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