package com.lj.songpuzzle;


import java.io.File;
import java.io.IOException;

import com.yg.message.ConvertUtil;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * AMR�ļ����Ź�����
 * @author EXLsunshine
 */
public class AMRPlayer
{
	private static final String DEBUG_TAG = "AMRPlayer______";
	private Context context;
	private byte [] bytes;
	private File audioFile;
	private MediaPlayer mediaPlayer;
	private long audioLength = -1;
	
	public AMRPlayer(byte [] bytes)
	{
		this.bytes = bytes;
	}
	
	/**
	 * ��ʼ����
	 * @param context ����Activity.this
	 */
	public void play(Context context)
	{
		this.context = context;
		try 
		{
			playFromBytes(this.bytes, this.context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void playFromBytes(byte [] bytes, Context context) throws IOException
	{
		if (audioFile == null || mediaPlayer == null)
			loadAudioData(bytes, context);
		
		mediaPlayer.start();
	}
	
	private void loadAudioData(byte [] bytes, Context context) throws IOException
	{
		audioFile = ConvertUtil.bytes2AmrFile(bytes, context);
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(audioFile.getPath());
		mediaPlayer.prepare();
		
		Log.i(DEBUG_TAG, "Audio length is " + String.valueOf(mediaPlayer.getDuration()));
		audioLength = mediaPlayer.getDuration() / 1000;
	}
	
	/**
	 * ֹͣ����
	 */
	public void stop()
	{
		if (mediaPlayer == null)
			return;
		mediaPlayer.release();  
		mediaPlayer = null;
	}
	
	/**
	 * ������Ƶ��ʱ�䳤��
	 * @param context ����Activity.this
	 * @return ��Ƶ��ʱ�䳤��
	 */
	public long getDuration(Context context)
	{
		this.context = context;
		if (audioLength == -1)
		{
			try {
				loadAudioData(this.bytes, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return audioLength;
	}
}