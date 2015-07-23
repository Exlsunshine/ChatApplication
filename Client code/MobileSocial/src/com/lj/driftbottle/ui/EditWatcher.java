package com.lj.driftbottle.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
		String text = "���� " + MIN_TEXT_LENGTH + " ����";
		SpannableStringBuilder style = getDigitColorBuilder(text, Color.RED);
		textView.setText(style);
	}
	
	private SpannableStringBuilder getDigitColorBuilder(String text, int color)
	{
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		String reg = "\\d+";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		int start = text.indexOf(matcher.group());
		int end = start + matcher.group().length();
		style.setSpan(new ForegroundColorSpan(color),start,end,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		style.setSpan(new AbsoluteSizeSpan(58), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}
	
	@Override
	public void afterTextChanged(Editable arg0) 
	{
		int textLength = editText.getText().toString().length();
		
		if (textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH)
		{
			String text = "���������� " + (MAX_TEXT_LENGTH - textLength) + " ����";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.GREEN);
			textView.setText(style);
		}
		else if (textLength < MIN_TEXT_LENGTH)
		{
			String text = "���� " + (MIN_TEXT_LENGTH - textLength) + " ����";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.RED);
			textView.setText(style);
		}
		else if (textLength > MAX_TEXT_LENGTH)
		{
			String text = "�ѳ��� " + (textLength - MAX_TEXT_LENGTH) + " ����";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.RED);
			textView.setText(style);
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
