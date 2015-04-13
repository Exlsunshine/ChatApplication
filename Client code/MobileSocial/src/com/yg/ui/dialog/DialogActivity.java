package com.yg.ui.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ConvertUtil;
import com.yg.message.ImageMessage;
import com.yg.message.Recorder;
import com.yg.message.TextMessage;
import com.yg.ui.dialog.implementation.DialogAdapter;
import com.yg.user.FriendUser;

public class DialogActivity extends Activity
{
	private static final String DEBUG_TAG = "DialogActivity______";
	private ArrayList<AbstractMessage> messages;
	private Bitmap selectedImg = null;
	
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

	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_dialog_actionbar);
		
		TextView title = (TextView)findViewById(R.id.yg_dialog_actionbar_title);
		title.setText(getFriendByID(friendID).getAlias() == null ? getFriendByID(friendID).getNickName() : getFriendByID(friendID).getAlias());
	
		LinearLayout back = (LinearLayout)findViewById(R.id.yg_dialog_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DialogActivity.this.finish();
			}
		});
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yg_dialog_activity);
		Intent intent = getIntent();
		friendID = intent.getIntExtra("reveiewer", -1);

		setupDialogActionBar();
		
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

		messages = new ArrayList<AbstractMessage>();
		messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
				
		msgAdapter = new DialogAdapter(this, messages);
		listView.setAdapter(msgAdapter);
		listView.setSelection(msgAdapter.getCount() - 1);

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
				TextMessage txtMsg = new TextMessage(ConstantValues.user.getID(), friendID, content, CommonUtil.now(), true);
				ConstantValues.user.sendMsgTo(getFriendByID(friendID), txtMsg);
				editText.setText("");
				
				messages.clear();
				messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
				msgAdapter.notifyDataSetChanged();
				listView.setSelection(listView.getCount() - 1);

				Intent intent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
				sendBroadcast(intent);
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
					
					ImageButton imgPicker = (ImageButton)findViewById(R.id.yg_dialog_activity_appkefu_plus_pick_picture_btn);
					imgPicker.setOnClickListener(new onImageSelectClickListener());
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
					
					Recorder.getInstance().startRecordAndFile();
					Log.i(DEBUG_TAG, "Start record");
				}
				else if (event.getAction() == MotionEvent.ACTION_MOVE) 
				{
					//int x = (int) event.getX();
					int y = (int) event.getY();
					String a = "" + y;
					Log.i(DEBUG_TAG,"MotionEvent.ACTION_MOVE y position is " + a);
					if (y < 0)
					{
						record_hint_layout.setVisibility(View.GONE);
						record_hint_cancel_layout.setVisibility(View.VISIBLE);
						record_hint_text_record_layout.setVisibility(View.GONE);
						record_hint_text_cancel_layout.setVisibility(View.VISIBLE);
						isRecord = false;
					} 
					else
					{
						record_hint_layout.setVisibility(View.VISIBLE);
						record_hint_cancel_layout.setVisibility(View.GONE);
						record_hint_text_record_layout.setVisibility(View.VISIBLE);
						record_hint_text_cancel_layout.setVisibility(View.GONE);
						isRecord = true;
					}
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) 
				{
					Recorder.getInstance().stopRecordAndFile();
					
					record_hintview.setVisibility(View.GONE);
					if (isRecord) 
					{
						Log.i(DEBUG_TAG, "Send record");
						
						Thread td = new Thread(new Runnable()
						{
							@Override
							public void run() 
							{
								AudioMessage audioMsg = null;
								try 
								{
									audioMsg = new AudioMessage(ConstantValues.user.getID(),
											friendID, ConvertUtil.amr2Bytes(Recorder.getInstance().getAMRFilePath()), CommonUtil.now(), true);
								} catch (IOException e) {
									e.printStackTrace();
								}
								ConstantValues.user.sendMsgTo(getFriendByID(friendID), audioMsg);
							}
						});
						td.start();
						try {
							td.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						messages.clear();
						messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
						msgAdapter.notifyDataSetChanged();
						listView.setSelection(listView.getCount() - 1);
						Intent intent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
						sendBroadcast(intent);
					} 
					else 
					{
						Toast.makeText(DialogActivity.this, "Abort", Toast.LENGTH_SHORT).show();
					}
				}

				return false;
			}
		});
		
		//registerReceiver(broadcastReceiver, intentFilter());
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

	private IntentFilter intentFilter()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_COMPLETED);
		
		return intentFilter;
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if ((intent.getAction().equals(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_COMPLETED)))
			{
				int fromUserID = intent.getIntExtra("fromUserID", -1);
				
				if (fromUserID == DialogActivity.this.friendID)
				{
					messages.clear();
					messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
					msgAdapter.notifyDataSetChanged();
					listView.setSelection(listView.getCount() - 1);
				}
			}
		}
	};
	
	protected void onResume()
	{
		super.onResume();
		registerReceiver(broadcastReceiver, intentFilter());
		
		messages.clear();
		messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
		msgAdapter.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	};
	
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	};
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	/**********************										***********************/
	/**********************			以下是选图相关函数				***********************/
	/**********************										***********************/
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));
	
	private void startPhotoZoom(Uri uri) 
	{  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CROP);  
    }  
	
	private class onImageSelectClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			new AlertDialog.Builder(DialogActivity.this).setTitle("请选择")
			.setIcon(R.drawable.ic_launcher)
			.setItems(new String[] {"本地图库", "照相机"}, new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					if (which == 0)
					{
						Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, ConstantValues.InstructionCode.REQUESTCODE_GALLERY);
					}							
					else if (which == 1)
					{
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT)));  
						startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CAMERA);  
					}
					dialog.dismiss();  
				}
			}).setNegativeButton("取消", null).show(); 
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY && resultCode == RESULT_OK)
		{
			Uri selectedImage =  data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			File temp = new File(picturePath);
			startPhotoZoom(Uri.fromFile(temp)); 
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CAMERA && resultCode == RESULT_OK)
		{
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.USERSET_PORTRAIT);  
            startPhotoZoom(Uri.fromFile(temp));  
			
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CROP && resultCode == RESULT_OK)
		{
			try 
			{
				selectedImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					ImageMessage imgMsg = new ImageMessage(ConstantValues.user.getID(), friendID, selectedImg, CommonUtil.now(), true);
					ConstantValues.user.sendMsgTo(getFriendByID(friendID), imgMsg);
				}
			});
			td.start();
			try {
				td.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			messages.clear();
			messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
			msgAdapter.notifyDataSetChanged();
			listView.setSelection(listView.getCount() - 1);

			Intent intent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
			sendBroadcast(intent);
		}
	}
	/**********************										***********************/
	/**********************			以上是选图相关函数				***********************/
	/**********************										***********************/
}