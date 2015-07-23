package com.lj.driftbottle.ui;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditWatcher implements TextWatcher
{
	private static final int MIN_TEXT_LENGTH = 5;
	private static final int MAX_TEXT_LENGTH = 150;
	private EditText editText;
	private TextView textView;
	
	public static boolean isTextLengthValid(String text)
	{
		int length = text.length();
		return length >= MIN_TEXT_LENGTH && length <= MAX_TEXT_LENGTH;
	}
	
	public EditWatcher(EditText editText, TextView textView) 
	{
		this.editText = editText;
		this.textView = textView;
		textView.setText("����" + MIN_TEXT_LENGTH + "����");
	}
	@Override
	public void afterTextChanged(Editable arg0) 
	{
		int textLength = editText.getText().toString().length();
		if (textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH)
		{
			textView.setTextColor(Color.GREEN);
			textView.setText("����������" + (MAX_TEXT_LENGTH - textLength) + "����");
		}
		else if (textLength < MIN_TEXT_LENGTH)
		{
			textView.setTextColor(Color.RED);
			textView.setText("����" + (MIN_TEXT_LENGTH - textLength) + "����");
		}
		else if (textLength > MAX_TEXT_LENGTH)
		{
			textView.setTextColor(Color.RED);
			textView.setText("�ѳ���" + (textLength - MAX_TEXT_LENGTH) + "����");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

}
