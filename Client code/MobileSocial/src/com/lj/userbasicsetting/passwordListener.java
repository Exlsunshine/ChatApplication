package com.lj.userbasicsetting;

import com.yg.user.ClientUser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class passwordListener implements OnClickListener
{
	private ClientUser user;
	private ActivityUserBasicSetting context;
	private EditText oldPassword;
	private EditText newPassword;
	private EditText newPasswordAgain;
	
	public passwordListener(ClientUser user, ActivityUserBasicSetting context) 
	{
		this.user = user;
		this.context = context;
	}
	
	private DialogInterface.OnClickListener newpasswordAgainListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			final String password = newPasswordAgain.getText().toString();
			new Thread()
			{
				public void run() 
				{
					user.setPassword(password);
				};
			}.start();
		}
	};
	
	private DialogInterface.OnClickListener newpasswordListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			Builder b = new AlertDialog.Builder(context).setTitle("再次输入新密码")  .setIcon(android.R.drawable.ic_dialog_info)  
					.setPositiveButton("确定", newpasswordAgainListener)  
					.setNegativeButton("取消", null);
			newPasswordAgain = new EditText(b.getContext());
			b.setView(newPasswordAgain).show();
		}
	};
	
	private DialogInterface.OnClickListener oldpasswordListener = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1)
		{
			Builder b = new AlertDialog.Builder(context).setTitle("输入新密码")  .setIcon(android.R.drawable.ic_dialog_info)  
					.setPositiveButton("确定", newpasswordListener)  
					.setNegativeButton("取消", null);
			newPassword = new EditText(b.getContext());
			b.setView(newPassword).show();
		}
	};
	
	@Override
	public void onClick(View arg0)
	{
		Builder b = new AlertDialog.Builder(context).setTitle("输入旧密码")  .setIcon(android.R.drawable.ic_dialog_info)  
		.setPositiveButton("确定", oldpasswordListener)  
		.setNegativeButton("取消", null);
		oldPassword = new EditText(b.getContext());
		b.setView(oldPassword).show();
	}
}
