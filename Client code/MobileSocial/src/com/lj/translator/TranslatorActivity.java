package com.lj.translator;


import com.example.testmobiledatabase.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class TranslatorActivity extends Activity
{
	public static String TEXT = "text";
	public static String TOLANGUAGE = "toLanguage";
	
	private final int TRANSLATE_SUCCESS = 1;
	private final int TRANSLATE_FAIL = 2;
	
	private static Translator translator = new Translator();
	private TextView textView = null;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == TRANSLATE_SUCCESS)
			{
				String result = msg.obj.toString();
				textView.setText(result);
			}
			else if (msg.what == TRANSLATE_FAIL)
			{
				textView.setText("∑≠“Î ß∞‹£¨«ÎºÏ≤ÈÕ¯¬Á¡¨Ω”°£");
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lj_translator_layout);
        
        View layout = findViewById(R.id.translate_layout);
        layout.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				TranslatorActivity.this.finish();
			}
		});
        textView = (TextView)findViewById(R.id.translateText);
        Intent intent = getIntent();
        final String text = intent.getStringExtra(TEXT);
        final String toLanguage = intent.getStringExtra(TOLANGUAGE);
        new Thread(new Runnable() 
        {
			@Override
			public void run()
			{
				String result = translator.autoTranslateTo(text, toLanguage);
				Message msg = new Message();
				msg.what = result == null ? TRANSLATE_FAIL : TRANSLATE_SUCCESS;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}).start();
	}
}
