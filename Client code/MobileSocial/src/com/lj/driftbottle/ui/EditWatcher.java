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
	private static final int MAX_TEXT_LENGTH = 50;
	private EditText editText;
	private TextView textView;
	
	public static boolean isTextLengthValid(String text)
	{
		int length = text.replace("\n", "").length();
		return length >= MIN_TEXT_LENGTH && length <= MAX_TEXT_LENGTH;
	}
	
	public EditWatcher(EditText editText, TextView textView) 
	{
		this.editText = editText;
		this.textView = textView;
		String text = "至少 " + MIN_TEXT_LENGTH + " 个字";
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
	
	private int length = -1;
	@Override
	public void afterTextChanged(Editable arg0) 
	{
		
		int textLength = editText.getText().toString().replace("\n", "").length();
		if (length == textLength)
			return;
		length = textLength;
		if (textLength >= MIN_TEXT_LENGTH && textLength <= MAX_TEXT_LENGTH)
		{
			String text = "还可以输入 " + (MAX_TEXT_LENGTH - textLength) + " 个字";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.GREEN);
			textView.setText(style);
		}
		else if (textLength < MIN_TEXT_LENGTH)
		{
			String text = "还差 " + (MIN_TEXT_LENGTH - textLength) + " 个字";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.RED);
			textView.setText(style);
		}
		else if (textLength > MAX_TEXT_LENGTH)
		{
			String text = "已超出 " + (textLength - MAX_TEXT_LENGTH) + " 个字";
			SpannableStringBuilder style = getDigitColorBuilder(text, Color.RED);
			textView.setText(style);
			
			SpannableStringBuilder editStyle = new SpannableStringBuilder(editText.getText().toString());
			editStyle.setSpan(new ForegroundColorSpan(Color.GRAY),MAX_TEXT_LENGTH,textLength,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			editText.setText(editStyle);
			editText.setSelection(textLength);
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
