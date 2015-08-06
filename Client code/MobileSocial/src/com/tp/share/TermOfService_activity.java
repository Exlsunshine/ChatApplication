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
		title.setText("服务条款");
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
		final String warning = "1、服务条款的确认和接纳\n本APP的各项内容和服务的所有权归本公司拥有。用户在接受本服务之前，请务必仔细阅读本条款。用户使用服务，或通过完成注册程序，表示用户接受所有服务条款。\n";
		final String userAccept = "2、用户同意：\n(1) 提供及时、详尽及准确的个人资料。\n(2) 不断更新注册资料、符合及时、详尽、准确的要求。\n(3)本APP将不公开用户的姓名、地址、电子邮箱、帐号和电话号码等信息（请阅隐私保护条款）。\n";
		final String userSecurity = "3、用户的帐号、密码和安全性\n一旦成功注册成为会员，您将有一个密码和用户名。\n用户将对用户名和密码的安全负全部责任。另外，每个用户都要对以其用户名进行的所有活动和事件负全责。您可以随时改变您的密码。\n用户若发现任何非法使用用户帐号或存在安全漏洞的情况，请立即通告本公司。\n";
		final String userInfo = "4、对用户信息的存储和限制\n本网站及APP不对用户发布信息的删除或储存失败负责，本公司有判定用户的行为是否符合服务条款的要求和精神的保留权利。如果用户违背了服务条款的规定，有中断对其提供服务的权利。";
		final String content = warning + userAccept + userSecurity + userInfo;
		termOfServieContent.setText(content);
	}
}
