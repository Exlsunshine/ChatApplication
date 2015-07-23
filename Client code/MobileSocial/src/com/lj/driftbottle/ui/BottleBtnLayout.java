package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottleBtnLayout extends LinearLayout
{
	public static final int LAYOUT_STYLE_THROW = 1;
	public static final int LAYOUT_STYLE_REPLY = 2;
	private final int MAX_TEXT_LENGTH = 150;
	private final int MIN_TEXT_LENGTH = 5;
	
	private TextView textView = null;
	private EditText editText = null;
	private Context context;
	
	public BottleBtnLayout(Context context) 
	{
		super(context);
		init(context);
	}
	
	public BottleBtnLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}
	
	public Button getBtn()
	{
		return (Button) findViewById(R.id.lj_bottle_btn_send);
	}

	private void init(Context context)
	{
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.lj_bottle_btn, this, true);
		textView = ((TextView)findViewById(R.id.lj_bottle_btn_text));
		textView.setText("还差" + MIN_TEXT_LENGTH + "个字");
	}
	
	public boolean isTextLengthValid(int length)
	{
		return length >= MIN_TEXT_LENGTH && length <= MAX_TEXT_LENGTH;
	}
	
	public void shake()
	{
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
		textView.startAnimation(shake);
	}
	
	public void setStyle(int style, EditText editText)
	{
		this.editText = editText;
		this.editText.addTextChangedListener(new EditWatcher());
		if (style == LAYOUT_STYLE_THROW)
		{
			((Button)findViewById(R.id.lj_bottle_btn_send)).setText("扔出去");
			findViewById(R.id.lj_bottle_btn_throwback).setVisibility(View.INVISIBLE);
		}
		else
		{
			((Button)findViewById(R.id.lj_bottle_btn_send)).setText("发送");
			findViewById(R.id.lj_bottle_btn_throwback).setVisibility(View.VISIBLE);
		}
	}
	
	private class EditWatcher implements TextWatcher
	{
		@Override
		public void afterTextChanged(Editable arg0)
		{
			int textLength = editText.getText().toString().length();
			if (textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH)
			{
				textView.setTextColor(Color.GREEN);
				textView.setText("还可以输入" + (MAX_TEXT_LENGTH - textLength) + "个字");
			}
			else if (textLength < MIN_TEXT_LENGTH)
			{
				textView.setTextColor(Color.RED);
				textView.setText("还差" + (MIN_TEXT_LENGTH - textLength) + "个字");
			}
			else if (textLength > MAX_TEXT_LENGTH)
			{
				textView.setTextColor(Color.RED);
				textView.setText("已超出" + (textLength - MAX_TEXT_LENGTH) + "个字");
			}
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
