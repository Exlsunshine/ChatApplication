package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.BottleManager;
import com.lj.driftbottle.logic.CommBottle;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottleInfoActivity extends Activity
{
	public static final int THROW_BACK = 10;
	private AlertDialog throwBackDialog = null;
	private CommBottle bottle = null;
	private final int REPLY_HANDLER = 0x02;
	private final int THROWBACK_HANDLER = 0x03;
	private EditText editText = null;
	private TextView textView = null;
	private BottleManager bottleManager = null;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == REPLY_HANDLER)
			{
				Intent intent = new Intent(BottleInfoActivity.this, chuck_animation.class);
				startActivity(intent);
				setResult(RESULT_OK);
				finish();
			}
			else if (msg.what == THROWBACK_HANDLER)
			{
				Intent intent = new Intent(BottleInfoActivity.this, chuck_animation.class);
				startActivity(intent);
				
				Intent data = new Intent();
				data.putExtra("bottleID", bottle.getBottleID());
				setResult(THROW_BACK, data);
				finish();
			}
		};
	};
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleTextView = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleTextView.setText("Ư��ƿ");
		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_bottle_info_layout);
		Intent intent = getIntent();
		int bottleID = intent.getIntExtra("bottleID", 0);
		bottleManager = BottleManager.getInstance();
		bottle = bottleManager.getBottleByID(bottleID);
		
		BottleHistoryText bottleHistory = (BottleHistoryText)findViewById(R.id.lj_bottle_info_history);
		bottleHistory.setInfo(bottle);
		
		editText = (EditText) findViewById(R.id.lj_bottle_info_append);
		textView = (TextView) findViewById(R.id.lj_bottle_info_reply_num);
		editText.addTextChangedListener(new EditWatcher(editText, textView));
		findViewById(R.id.lj_bottle_info_reply_btn).setOnClickListener(clickListener);
		findViewById(R.id.lj_bottle_info_throwback_btn).setOnClickListener(clickListener);
		setupDialogActionBar();
		if (bottle.getBottleRelationStatus() == CommBottle.BOTTLE_RELATION_STATUS_DELETE)
		{
			findViewById(R.id.lj_bottle_info_edit_layout).setVisibility(View.GONE);
			findViewById(R.id.lj_bottle_info_remind_delete).setVisibility(View.VISIBLE);
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View arg0)
		{
			int id = arg0.getId();
			if (id == R.id.lj_bottle_info_reply_btn)
			{
				String text = editText.getText().toString();
				if (EditWatcher.isTextLengthValid(text))
				{
					bottle.appentText(text, ConstantValues.user.getID());
					new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							bottleManager.reply(bottle);
							handler.sendEmptyMessage(REPLY_HANDLER);
						}
					}).start();
				}
				else
				{
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.yg_loginguide_page3_login_dialog_anim_shake);
					textView.startAnimation(shake);
				}
			}
			else if (id == R.id.lj_bottle_info_throwback_btn)
			{
				if (bottle.getOwnerID() == ConstantValues.user.getID())
					showThrowBackInvalidDialog();
				else
					showThrowBackDialog();
			}
		}
	};
	
	private void showThrowBackInvalidDialog()
	{
		throwBackDialog = new AlertDialog.Builder(BottleInfoActivity.this, R.style.LoginDialogAnimation).create();
		throwBackDialog.setCanceledOnTouchOutside(true);
		throwBackDialog.show();
		throwBackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		throwBackDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		throwBackDialog.setCancelable(true);
		Window window = throwBackDialog.getWindow();
		window.setContentView(R.layout.lj_throw_back_invalid_dialog);
		Button confirm = (Button) window.findViewById(R.id.lj_throw_back_invalid_dialog_button_confirm);
		confirm.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				throwBackDialog.dismiss();
			}
		});
	}
	
	private void showThrowBackDialog()
	{
		throwBackDialog = new AlertDialog.Builder(BottleInfoActivity.this, R.style.LoginDialogAnimation).create();
		throwBackDialog.setCanceledOnTouchOutside(true);
		throwBackDialog.show();
		throwBackDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		throwBackDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		throwBackDialog.setCancelable(true);
		Window window = throwBackDialog.getWindow();
		window.setContentView(R.layout.lj_throw_back_dialog);
		Button cancel = (Button) window.findViewById(R.id.lj_throw_back_dialog_button_cancel);
		Button confirm = (Button) window.findViewById(R.id.lj_throw_back_dialog_button_confirm);
		cancel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				throwBackDialog.dismiss();
			}
		});
		confirm.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						bottleManager.throwBack(bottle);
						handler.sendEmptyMessage(THROWBACK_HANDLER);
					}
				}).start();
				throwBackDialog.dismiss();
			}
		});
	}
}
