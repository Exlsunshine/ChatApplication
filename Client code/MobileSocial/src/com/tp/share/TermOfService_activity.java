package com.tp.share;


import com.example.testmobiledatabase.R;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TermOfService_activity extends Activity 
{
	private TextView termOfServieContent;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tp_about_termofservice_activity);
		termOfServieContent = (TextView) findViewById(R.id.tp_termofservicecontent_et);
		setupDialogActionBar();
		setTermOfService();
	}

	private void setupDialogActionBar() 
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.tp_about_termofservice_actionbar);

		TextView title = (TextView) findViewById(R.id.tp_about_termofservice_actionbar_title);
		title.setText("��������");
		LinearLayout back = (LinearLayout) findViewById(R.id.tp_app_about_actionbar_back);
		back.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}
	private void setTermOfService()
	{
		final String warning = "1�����������ȷ�Ϻͽ���\n��APP�ĸ������ݺͷ��������Ȩ�鱾��˾ӵ�С��û��ڽ��ܱ�����֮ǰ���������ϸ�Ķ�������û�ʹ�÷��񣬻�ͨ�����ע����򣬱�ʾ�û��������з������\n";
		final String userAccept = "2���û�ͬ�⣺\n(1) �ṩ��ʱ���꾡��׼ȷ�ĸ������ϡ�\n(2) ���ϸ���ע�����ϡ����ϼ�ʱ���꾡��׼ȷ��Ҫ��\n(3)��APP���������û�����������ַ���������䡢�ʺź͵绰�������Ϣ��������˽���������\n";
		final String userSecurity = "3���û����ʺš�����Ͱ�ȫ��\nһ���ɹ�ע���Ϊ��Ա��������һ��������û�����\n�û������û���������İ�ȫ��ȫ�����Ρ����⣬ÿ���û���Ҫ�������û������е����л���¼���ȫ����������ʱ�ı��������롣\n�û��������κηǷ�ʹ���û��ʺŻ���ڰ�ȫ©���������������ͨ�汾��˾��\n";
		final String userInfo = "4�����û���Ϣ�Ĵ洢������\n����վ��APP�����û�������Ϣ��ɾ���򴢴�ʧ�ܸ��𣬱���˾���ж��û�����Ϊ�Ƿ���Ϸ��������Ҫ��;���ı���Ȩ��������û�Υ���˷�������Ĺ涨�����ж϶����ṩ�����Ȩ����";
		final String content = warning + userAccept + userSecurity + userInfo;
		termOfServieContent.setText(content);
	}
}
