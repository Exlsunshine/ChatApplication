package com.yg.ui.dialog;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.message.TextMessage;
import com.yg.ui.dialog.implementation.DialogAdapter;
import com.yg.user.FriendUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	private int friendID = -1;
	private DialogAdapter msgAdapter = null;
	private ListView listView = null;
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
	private boolean isRecord = true;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yg_dialog_activity);
		Intent intent = getIntent();
		friendID = intent.getIntExtra("reveiewer", -1);

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

		
		msgAdapter = new DialogAdapter(this, 
				ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
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
				TextMessage txtMsg = new TextMessage(ConstantValues.user.getID(),
						friendID, content, CommonUtil.now(), true);
				ConstantValues.user.sendMsgTo(getFriendByID(friendID), txtMsg);
				msgAdapter.notifyDataSetChanged();
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
	
	private FriendUser getFriendByID(int id)
	{
		for (int i = 0; i < ConstantValues.user.getFriendList().size(); i++)
		{
			if (ConstantValues.user.getFriendList().get(i).getID() == id)
				return ConstantValues.user.getFriendList().get(i);
		}
		return null;
	}

	public void StartRecord()
	{
	}
}