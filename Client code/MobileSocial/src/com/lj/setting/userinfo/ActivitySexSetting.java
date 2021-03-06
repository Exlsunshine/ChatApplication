package com.lj.setting.userinfo;


import com.example.testmobiledatabase.R;
import com.lj.datapicker.ArrayWheelAdapter;
import com.lj.datapicker.OnWheelChangedListener;
import com.lj.datapicker.WheelView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitySexSetting extends Activity
{
	private WheelView gPickerSex = null;
	private TextView gSexText = null;
	
	private final String MALE = "��";
	private final String FEMALE = "Ů";
	private final String[] SEX_OPTION = {MALE, FEMALE};
	
	private OnWheelChangedListener gSexChangeListener = new OnWheelChangedListener() 
	{
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) 
		{
			int index = wheel.getCurrentItem();
			gSexText.setText(SEX_OPTION[index]);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_setting_sex_layout);
		gPickerSex = (WheelView) findViewById(R.id.lj_picker_sex);
		gSexText = (TextView) findViewById(R.id.lj_sex_setting_sex);
		
		Intent intent = getIntent();
		String sex = intent.getStringExtra("sex");
		
		if (sex == null)
			sex = MALE;
		int selectIndex = 0;
		if (sex.equals(MALE))
			selectIndex = 0;
		else
			selectIndex = 1;
		gPickerSex.setAdapter(new ArrayWheelAdapter<String>(SEX_OPTION));
		gPickerSex.setCurrentItem(selectIndex);
		gSexText.setText(SEX_OPTION[selectIndex]);
		gPickerSex.addChangingListener(gSexChangeListener);
		setupDialogActionBar();
	}

	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleText = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleText.setText("�Ա�");
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();  
				intent.putExtra("sex", SEX_OPTION[gPickerSex.getCurrentItem()]);
				setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_SEX, intent);  
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();  
			intent.putExtra("sex", SEX_OPTION[gPickerSex.getCurrentItem()]);
			setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_SEX, intent);  
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
