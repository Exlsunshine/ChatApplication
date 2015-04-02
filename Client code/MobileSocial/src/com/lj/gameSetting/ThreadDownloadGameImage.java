package com.lj.gameSetting;

import com.yg.commons.ConstantValues;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class ThreadDownloadGameImage extends Thread
{
	private int userID;
	private Handler handle;

	public ThreadDownloadGameImage(int userid, Handler h) 
	{
		userID = userid;
		handle = h;
	}
	
	@Override
	public void run() 
	{
		super.run();
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.HANDLER_WAIT_FOR_DATA;
		handle.sendMessage(msg);
		
		WebEightPuzzleImageTransfer webEight = new WebEightPuzzleImageTransfer();
		Bitmap bitmap = webEight.downloadImage(userID);
		if (bitmap == null)
			return;
		Message msgs = new Message();
		msgs.what = ConstantValues.InstructionCode.GAMESET_HANDLER_DOWNLOAD_IMAGE;
		msgs.obj = bitmap;
		handle.sendMessage(msgs);
	}
}
