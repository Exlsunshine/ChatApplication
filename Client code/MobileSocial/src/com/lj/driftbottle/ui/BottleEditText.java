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
	private EditText editText = null;
	
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

	public String getText()
	{
		return editText.getText().toString();
	}
	
	public EditText getEditText()
	{
		return editText;
	}
	
	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.lj_driftbottle_edittext, this, true);
		editText = (EditText)findViewById(R.id.lj_driftbottle_edittext_edit);
	}
}
