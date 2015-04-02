package com.lj.userbasicsetting;

import com.yg.commons.ConstantValues;
import com.yg.user.ClientUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class sexListener implements OnClickListener
{
	private ClientUser user;
	private ActivityUserBasicSetting context;
	private String sex = "male";
	
	public sexListener(ClientUser user, ActivityUserBasicSetting context) 
	{
		this.user = user;
		this.context = context;
	}
	
	OnMultiChoiceClickListener choiceListener = new OnMultiChoiceClickListener() 
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1, boolean arg2) 
		{
			switch (arg1)
			{
			case 0:
				sex = "male";
				break;
			case 1:
				sex = "female";
				break;
			}
		}
	};
	
	private DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			switch (arg1)
			{
			case 0:
				sex = "male";
				break;
			case 1:
				sex = "female";
			}
			
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_SET_SEX;
			msg.obj = sex;
			context.handle.sendMessage(msg);
			new Thread()
			{
				public void run() 
				{
					user.setSex(sex);
				};
			}.start();
			arg0.dismiss();
		}
	};
	
	@Override
	public void onClick(View arg0) 
	{
		new AlertDialog.Builder(context)  
			.setTitle("选择性别")  
			.setSingleChoiceItems(new String[] {"男","女"}, 0, confirmListener)  
			.setNegativeButton("取消", null)  
			.show();  
	}
}
