package com.lj.userbasicsetting;


import com.yg.commons.ConstantValues;
import com.yg.user.ClientUser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class phoneListener implements OnClickListener
{

	private ClientUser user;
	private ActivityUserBasicSetting context;
	private EditText phone;
	 
	private DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
		

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			final String phonenum = phone.getText().toString();
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_SET_PHONE;
			msg.obj = phonenum;
			context.handle.sendMessage(msg);
			new Thread()
			{
				public void run() 
				{
					user.setPhoneNumber(phonenum);
				};
			}.start();
			
		}
	};
	
	public phoneListener(ClientUser user, ActivityUserBasicSetting context) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.context = context;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Builder b = new AlertDialog.Builder(context).setTitle("请输入手机号").setIcon(android.R.drawable.ic_dialog_info)  
				.setPositiveButton("确定", confirmListener)  
				.setNegativeButton("取消", null);
			phone = new EditText(b.getContext());
			b.setView(phone).show();
	}

}
