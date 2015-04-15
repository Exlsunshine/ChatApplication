package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import org.kobjects.base64.Base64;

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
			gAMRPlayer[i] = new AMRPlayer(Base64.decode(map.get("record").toString()));
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
		gAMRPlayer[gCurrentIndex].stop();
	}
	
	public void play(Context context)
	{
		gAMRPlayer[gCurrentIndex].play(context);
	}
	
	public String getCurrentAnswer()
	{
		Log.e("sss", gCurrentIndex + " " + gAnswer.length);
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
