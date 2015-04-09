package com.yg.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;
import com.yg.ui.dialog.implementation.CircleBitmap;
import com.yg.ui.dialog.implementation.DialogAdapter;
import com.yg.ui.dialog.implementation.msgtext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class DialogActivity extends Activity
{
	private String name = null;
	private List<msgtext> list = new ArrayList<msgtext>();
	private DialogAdapter msgAdapter = null;
	private ListView listView = null;
	private Bitmap bmp;
	private Bitmap bmp1;
	private msgtext a;
	private EditText editText = null;
	private Button send = null;
	private Button addButton = null;
	private Button recordvoicebtn = null;
	private Button voiceButton = null;
	private LinearLayout record_hintview = null;
	private LinearLayout record_hint_layout = null;
	private LinearLayout record_hint_cancel_layout = null;
	private LinearLayout record_hint_text_record_layout = null;
	private LinearLayout record_hint_text_cancel_layout = null;
	private RelativeLayout plusRelativelayout = null;
	//private int[] location = new int[2];
	//private int recordvoicebtn_Y = 0;
	//private int recordvoicebtn_X = 0;
	private boolean isRecord = true;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yg_dialog_activity);
		Intent intent = getIntent();
		name = intent.getStringExtra("reveiewer");

		listView = (ListView) super.findViewById(R.id.yg_dialog_activity_dialog_listview);
		editText = (EditText) super.findViewById(R.id.yg_dialog_activity_inputbox);
		send = (Button) super.findViewById(R.id.yg_dialog_activity_send);
		addButton = (Button) super.findViewById(R.id.yg_dialog_activity_add_utton);
		recordvoicebtn = (Button) super.findViewById(R.id.yg_dialog_activity_record_btn);
		voiceButton = (Button) super.findViewById(R.id.yg_dialog_activity_voice_button);
		record_hintview = (LinearLayout) super.findViewById(R.id.yg_dialog_activity_record_hintview);
		record_hint_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_record_image_layout);
		record_hint_cancel_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_cancel_image_layout);
		record_hint_text_record_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_record_text_layout);
		record_hint_text_cancel_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_cancel_text_layout);
		plusRelativelayout = (RelativeLayout) super.findViewById(R.id.yg_dialog_activity_plus_rl);
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		Resources res = getResources();
		bmp = BitmapFactory.decodeResource(res, R.drawable.yg_portrait_sample_x);
		bmp = CircleBitmap.circleBitmap(bmp);
		bmp1 = BitmapFactory.decodeResource(res, R.drawable.yg_portrait_sample_x);
		bmp1 = CircleBitmap.circleBitmap(bmp1);

		a = new msgtext(this, "斜阳晚钟", "文艺青年", "晚上好", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "好啊", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "吃饭了吗", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "没有,正要去呢", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "快去", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "好", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "嘛呢", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "路上呢", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "回家?", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "对", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "哪你先回吧", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "好的", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "到了吗", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "嗯嗯", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "太累了", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "~~", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "斜阳晚钟", "文艺青年", "!!", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);
		a = new msgtext(this, "文艺青年", "斜阳晚钟", "= =", "2014-03-27 14:32", bmp, bmp1);
		list.add(a);

		msgAdapter = new DialogAdapter(this, list, name);
		listView.setAdapter(msgAdapter);
		listView.setSelection(msgAdapter.getCount());

		editText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				if (!editText.getText().toString().equals(""))
				{
					send.setVisibility(View.VISIBLE);
					addButton.setVisibility(View.INVISIBLE);
				} 
				else 
				{
					addButton.setVisibility(View.VISIBLE);
					send.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
			}

		});

		send.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				String content = editText.getText().toString();
				msgtext aa = new msgtext(DialogActivity.this, "斜阳晚钟", name, content, "2015-04-09-13:42", bmp, bmp1);
				msgAdapter.add(aa);
				listView.setSelection(listView.getCount() - 1);
				editText.setText("");
			}
		});

		addButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if (plusRelativelayout.getVisibility() == View.GONE) 
				{
					plusRelativelayout.setVisibility(View.VISIBLE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				} 
				else 
				{
					plusRelativelayout.setVisibility(View.GONE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				}
			}
		});

		recordvoicebtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				recordvoicebtn.setVisibility(View.GONE);
			}
		});

		voiceButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if (recordvoicebtn.getVisibility() == View.GONE)
				{
					recordvoicebtn.setVisibility(View.VISIBLE);
					listView.setSelection(listView.getCount() - 1);
				} 
				else
					recordvoicebtn.setVisibility(View.GONE);
			}
		});

		recordvoicebtn.setOnTouchListener(new OnTouchListener() 
		{
			public boolean onTouch(View v, MotionEvent event) 
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{
					record_hintview.setVisibility(View.VISIBLE);
					StartRecord();
				}
				else if (event.getAction() == MotionEvent.ACTION_MOVE) 
				{
					//int x = (int) event.getX();
					int y = (int) event.getY();
					String a = "" + y;
					Log.i("dfdf", a);
					if (y < 0)
					{
						record_hint_layout.setVisibility(View.GONE);
						record_hint_cancel_layout.setVisibility(View.VISIBLE);
						record_hint_text_record_layout.setVisibility(View.GONE);
						record_hint_text_cancel_layout.setVisibility(View.VISIBLE);
						isRecord = true;
					} 
					else
					{
						record_hint_layout.setVisibility(View.VISIBLE);
						record_hint_cancel_layout.setVisibility(View.GONE);
						record_hint_text_record_layout.setVisibility(View.VISIBLE);
						record_hint_text_cancel_layout.setVisibility(View.GONE);
						isRecord = false;
					}
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) 
				{
					record_hintview.setVisibility(View.GONE);
					if (isRecord) 
					{
					} 
					else 
					{
					}
				}

				return false;
			}
		});
	}

	public void StartRecord()
	{
	}
}