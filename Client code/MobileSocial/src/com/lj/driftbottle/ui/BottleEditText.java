package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.FirstBottle;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottleEditText extends RelativeLayout
{
	private final int MIN_LENGTH = 5;
	private final int MAX_LENGTH = 150;
	private EditText editText = null;
	private Button btnSend = null;
	private TextView textView = null;
	private Context context;
	
	public BottleEditText(Context context) 
	{
		super(context);
		init(context);
	}
	
	public BottleEditText(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}

	public Button getSendBtn()
	{
		return btnSend;
	}
	
	public String getText()
	{
		return editText.getText().toString();
	}
	
	public void shake()
	{
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
		textView.startAnimation(shake);
	}
	
	private void init(Context context)
	{
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.lj_driftbottle_edittext, this, true);
		editText = (EditText)findViewById(R.id.lj_driftbottle_edittext_edit);
		textView = (TextView)findViewById(R.id.lj_driftbottle_edittext_text);
		textView.setText(MIN_LENGTH + "-" + MAX_LENGTH + "×Ö");
		textView.setTextColor(Color.RED);
		editText.addTextChangedListener(new EditWatcher());
		btnSend = (Button)findViewById(R.id.lj_driftbottle_edittext_send);
	}
	
	public boolean isTextLengthValid(int length)
	{
		return length >= MIN_LENGTH && length <= MAX_LENGTH;
	}

	private class EditWatcher implements TextWatcher
	{
		@Override
		public void afterTextChanged(Editable arg0)
		{
			int textLength = editText.getText().toString().length();
			if (textLength >= MIN_LENGTH && textLength <= MAX_LENGTH)
				textView.setTextColor(Color.GREEN);
		/*	else if (textLength == 0)
				textView.setTextColor(Color.GRAY);*/
			else
				textView.setTextColor(Color.RED);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) 
		{
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}
		
	}
}
