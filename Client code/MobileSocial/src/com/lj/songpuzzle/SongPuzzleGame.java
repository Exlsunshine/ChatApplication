package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;


import com.yg.user.DownloadManager;

import android.content.Context;
import android.util.Log;

public class SongPuzzleGame 
{
	private String[] gAnswer;
	private String[] gChoiceChar;
	private int gCurrentIndex;
	private AMRPlayer[] gAMRPlayer;
	public SongPuzzleGame(ArrayList<HashMap<String, Object>> songData) 
	{
		gCurrentIndex = 0;
		gAnswer = new String[songData.size()];
		gChoiceChar = new String[songData.size()];
		gAMRPlayer = new AMRPlayer[songData.size()];
		for (int i = 0; i < songData.size(); i++)
		{
			HashMap<String, Object> map = songData.get(i);
			gAnswer[i] = map.get("song").toString();
			gChoiceChar[i] = map.get("char").toString();
			String url = map.get("url").toString();
			DownloadManager manager = new DownloadManager(url);
			gAMRPlayer[i] = new AMRPlayer(manager.getAudioFile());
		}
	}
	
	public void next()
	{
		if (isFinish())
			return;
		gCurrentIndex++;
	}
	
	public boolean isFinish()
	{
		if (gCurrentIndex == gAnswer.length)
			return true;
		else
			return false;
	}
	
	public void stop()
	{
		if (gCurrentIndex == gAnswer.length)
			return;
		gAMRPlayer[gCurrentIndex].stop();
	}
	
	public void play(Context context)
	{
		if (gCurrentIndex == gAnswer.length)
			return;
		gAMRPlayer[gCurrentIndex].play(context);
	}
	
	public String getCurrentAnswer()
	{
		if (isFinish())
			return null;
		else
			return gAnswer[gCurrentIndex];
	}
	
	public String getCurrentChoiceChar()
	{
		if (isFinish())
			return null;
		else 
			return gChoiceChar[gCurrentIndex];
	}	
	
	public boolean isRight(String answer)
	{
		if (gAnswer[gCurrentIndex].equals(answer))
			return true;
		else
			return false;
	}
}
