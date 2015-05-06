package com.lj.gameSetting;


import com.lj.eightpuzzle.WebEightPuzzleImageTransfer;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class ThreadUploadGameImage extends Thread
{
	private int userID;
	private Bitmap myBitmap;
	private Handler handle;
	
	public ThreadUploadGameImage(int userid, Bitmap bm, Handler h) 
	{
		userID = userid;
		myBitmap = bm;
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
		try 
		{
			boolean f = webEight.uploadImage(userID, myBitmap);
			if (!f)
				CommonUtil.sendNetWorkErrorMessage(handle);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			msg.what = ConstantValues.InstructionCode.ERROR_NETWORK;
			handle.sendMessage(msg);
			return;
		}
		msg.what = ConstantValues.InstructionCode.SUCCESS;
		handle.sendMessage(msg);
	}
}
