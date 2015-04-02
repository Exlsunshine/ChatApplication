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

public class birthdayListener implements OnClickListener
{
	private ClientUser user;
	private ActivityUserBasicSetting context;
	private EditText year;
	private EditText month;
	private EditText day; 
	
	public birthdayListener(ClientUser user, ActivityUserBasicSetting context) 
	{
		this.user = user;
		this.context = context;
	}
	
	private DialogInterface.OnClickListener dayListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			final String birthday = year.getText().toString() + "-" + month.getText().toString() + "-" + day.getText().toString();
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_SET_BIRTHDAY;
			msg.obj = birthday;
			context.handle.sendMessage(msg);
			new Thread()
			{
				public void run() 
				{
					user.setBirthday(birthday);
				};
			}.start();
		}
	};
	
	private DialogInterface.OnClickListener monthListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			Builder b = new AlertDialog.Builder(context).setTitle("����������")  .setIcon(android.R.drawable.ic_dialog_info)  
					.setPositiveButton("ȷ��", dayListener)  
					.setNegativeButton("ȡ��", null);
			day = new EditText(b.getContext());
			b.setView(day).show();
		}
	};
	
	private DialogInterface.OnClickListener yearListener = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			Builder b = new AlertDialog.Builder(context).setTitle("�����·�")  .setIcon(android.R.drawable.ic_dialog_info)  
					.setPositiveButton("ȷ��", monthListener)  
					.setNegativeButton("ȡ��", null);
			month = new EditText(b.getContext());
			b.setView(month).show();
		}
	};
	
	@Override
	public void onClick(View arg0) 
	{
		Builder b = new AlertDialog.Builder(context).setTitle("�������")  .setIcon(android.R.drawable.ic_dialog_info)  
				.setPositiveButton("ȷ��", yearListener)  
				.setNegativeButton("ȡ��", null);
		year = new EditText(b.getContext());
		b.setView(year).show();
	}
}
