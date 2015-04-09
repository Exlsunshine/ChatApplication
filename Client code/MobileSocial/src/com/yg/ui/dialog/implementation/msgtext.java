package com.yg.ui.dialog.implementation;

import android.content.Context;
import android.graphics.Bitmap;

public class msgtext {

	private String sender = null;
	private String receiver = null;
	private Bitmap senderView = null;
	private Bitmap receiverView = null;
	private String msg = null;
	private Context mContext;
	private String time = null;

	public msgtext(Context context, String sender, String receiver, String msg,String time ,
			Bitmap senderView, Bitmap receiverView) {
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
		this.mContext = context;
		this.senderView = senderView;
		this.receiverView = receiverView;
		this.time = time;

	}

	public String getsender() {
		return this.sender;
	}

	public String getreceiver() {
		return this.receiver;
	}

	public String getmsg() {
		return this.msg;
	}
	
	public String gettime() {
		return this.time;
	}
	
	public Bitmap getsenderView() {
		return this.senderView;
	}
	
	public Bitmap getreceiverView() {
		return this.receiverView;
	}

}
