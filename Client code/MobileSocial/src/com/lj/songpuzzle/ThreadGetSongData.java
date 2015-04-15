package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import com.yg.commons.ConstantValues;


import android.os.Handler;
import android.os.Message;

public class ThreadGetSongData extends Thread
{
	private Handler gHandler;
	
	public ThreadGetSongData(Handler handler) 
	{
		gHandler = handler;
	}
	
	@Override
	public void run() 
	{
		super.run();
		SongPuzzleVideoTransfer songPuzzleData = new SongPuzzleVideoTransfer();
		ArrayList<HashMap<String, Object>> songData = songPuzzleData.getSongData();
		if (songData == null)
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.ERROR_NETWORK;
			gHandler.sendMessage(msg);
		}
		else
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SONGPUZZLE_HANDLER_GET_SONGDATA;
			msg.obj = songData;
			gHandler.sendMessage(msg);
		}
		
	}
}
