package com.yg.ui.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.emoji.EmojiParser;
import com.yg.emoji.ParseEmojiMsgUtil;
import com.yg.emoji.SelectFaceHelper;
import com.yg.emoji.SelectFaceHelper.OnFaceOprateListener;
import com.yg.image.select.ui.SelectImageActivity;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ConvertUtil;
import com.yg.message.ImageMessage;
import com.yg.message.Recorder;
import com.yg.message.TextMessage;
import com.yg.ui.dialog.implementation.DialogAdapter;
import com.yg.user.FriendUser;
import com.yg.user.WebServiceAPI;

public class DialogActivity extends Activity
{
	private static final String DEBUG_TAG = "DialogActivity______";
	private static final int LOCATION_TAG = 0x01;
	private static final int RANDOM_CHATTING_THEME_TAG = 0x02;
	
	private ArrayList<AbstractMessage> messages;
	private Bitmap selectedImg = null;
	private TimerTask voiceIconUpdateTask;
	private Timer voiceIconUpdateTimer;
	private static final int voiceInconUpdateFreq = 100;
	private Handler uiHandler;
	private static final int UPDATE_AMP = 0x03;
	
	private int friendID = -1;
	private DialogAdapter msgAdapter = null;
	private ListView listView = null;
	private EditText editText = null;
	private Button send = null;
	private Button addButton = null;
	private Button recordvoicebtn = null;
	private Button voiceButton = null;
	private Button emoji = null;
	private LinearLayout record_hintview = null;
	private LinearLayout record_hint_layout = null;
	private LinearLayout record_hint_cancel_layout = null;
	private LinearLayout record_hint_text_record_layout = null;
	private LinearLayout record_hint_text_cancel_layout = null;
	private RelativeLayout plusRelativelayout = null;
	private boolean isRecord = true;
	private SelectFaceHelper faceHelper;
	private View addFaceToolView;
	private ImageView ampHint;
	
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
	
	private void notifyChattingWithSomebody()
	{
		Intent currentChatIntent = new Intent(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION);
		currentChatIntent.putExtra("fromUserID", friendID);
		sendBroadcast(currentChatIntent );
	}
	
	private void notifyClearRedDots()
	{
		Intent currentChatIntent = new Intent(ConstantValues.InstructionCode.CLEAR_MESSAGE_RED_DOT);
		currentChatIntent.putExtra("friendUserID", friendID);
		sendBroadcast(currentChatIntent );
	}
	
	private void initEmoji()
	{
		if (null == faceHelper) 
		{
			faceHelper = new SelectFaceHelper(this, addFaceToolView);
			faceHelper.setFaceOpreateListener(mOnFaceOprateListener);
		}
		
		hideInputManager(this);
	}
	
	public void hideInputManager(Context ct)
	{
		try 
		{
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
					.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			Log.i(DEBUG_TAG, "hideInputManager Catch error,skip it!", e);
		}
	}
	
	OnFaceOprateListener mOnFaceOprateListener = new OnFaceOprateListener() 
	{
		@Override
		public void onFaceSelected(SpannableString spanEmojiStr) 
		{
			if (null != spanEmojiStr)
			{
				editText.append(spanEmojiStr);
			}
		}

		@Override
		public void onFaceDeleted()
		{
			int selection = editText.getSelectionStart();
			String text = editText.getText().toString();
			if (selection > 0) 
			{
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) 
				{
					int start = text.lastIndexOf("[");
					int end = selection;
					editText.getText().delete(start, end);
					return;
				}
				editText.getText().delete(selection - 1, selection);
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_dialog_activity);
		
		Intent intent = getIntent();
		friendID = intent.getIntExtra("reveiewer", -1);

		notifyChattingWithSomebody();
		notifyClearRedDots();
		
		setupDialogActionBar();
		
		listView = (ListView) super.findViewById(R.id.yg_dialog_activity_dialog_listview);
		editText = (EditText) super.findViewById(R.id.yg_dialog_activity_inputbox);
		send = (Button) super.findViewById(R.id.yg_dialog_activity_send);
		addButton = (Button) super.findViewById(R.id.yg_dialog_activity_add_utton);
		recordvoicebtn = (Button) super.findViewById(R.id.yg_dialog_activity_record_btn);
		voiceButton = (Button) super.findViewById(R.id.yg_dialog_activity_voice_button);
		emoji = (Button) super.findViewById(R.id.yg_dialog_activity_emotion);
		addFaceToolView = (View) findViewById(R.id.yg_emoji_add_tool);
		ampHint = (ImageView) findViewById(R.id.yg_dialog_appkefu_voice_rcd_hint_amp);
		record_hintview = (LinearLayout) super.findViewById(R.id.yg_dialog_activity_record_hintview);
		record_hint_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_record_image_layout);
		record_hint_cancel_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_cancel_image_layout);
		record_hint_text_record_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_record_text_layout);
		record_hint_text_cancel_layout = (LinearLayout) super.findViewById(R.id.yg_dialog_record_hint_cancel_text_layout);
		plusRelativelayout = (RelativeLayout) super.findViewById(R.id.yg_dialog_activity_plus_rl);
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		messages = new ArrayList<AbstractMessage>();
		Log.i(DEBUG_TAG, "Dialog contains history messages:" + ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory().size());
		messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
		msgAdapter = new DialogAdapter(this, messages);
		listView.setAdapter(msgAdapter);
		listView.setSelection(msgAdapter.getCount() - 1);


		initEmoji();
		
		editText.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					//set emoji invisible
					View view = (View) findViewById(R.id.yg_dialog_activity_emoji_layout);
					view.setVisibility(View.GONE);

					//set plus panel invisible
					plusRelativelayout.setVisibility(View.GONE);
				}
			}
		});
		
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
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						String content = EmojiParser.getInstance(DialogActivity.this).parseEmoji(ParseEmojiMsgUtil.convertToMsg(editText.getText(), DialogActivity.this));
						TextMessage txtMsg = new TextMessage(ConstantValues.user.getID(), friendID, content, CommonUtil.now(), true);
						ConstantValues.user.sendMsgTo(getFriendByID(friendID), txtMsg);
						
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								editText.setText("");
								messages.clear();
								messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
								msgAdapter.notifyDataSetChanged();
								listView.setSelection(listView.getCount() - 1);
							}
						});

						Intent intent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
						sendBroadcast(intent);
					}
				});
				td.start();
				/*String content = editText.getText().toString();
				TextMessage txtMsg = new TextMessage(ConstantValues.user.getID(), friendID, content, CommonUtil.now(), true);
				ConstantValues.user.sendMsgTo(getFriendByID(friendID), txtMsg);
				editText.setText("");
				
				messages.clear();
				messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
				msgAdapter.notifyDataSetChanged();
				listView.setSelection(listView.getCount() - 1);

				Intent intent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
				sendBroadcast(intent);*/
			}
		});

		addButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if (plusRelativelayout.getVisibility() == View.GONE) 
				{
					//disable edittext and IM
					editText.clearFocus();
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				
					//disable emoji
					View view = (View) findViewById(R.id.yg_dialog_activity_emoji_layout);
					view.setVisibility(View.GONE);
					
					//show plus panel
					plusRelativelayout.setVisibility(View.VISIBLE);
					ImageButton imgPicker = (ImageButton)findViewById(R.id.yg_dialog_activity_appkefu_plus_pick_picture_btn);
					imgPicker.setOnClickListener(new onImageSelectClickListener());
					
					ImageButton imgTaker = (ImageButton)findViewById(R.id.yg_dialog_activity_appkefu_plus_take_picture_btn);
					imgTaker.setOnClickListener(new onImageTakeClickListener());
				
					ImageButton imgLocation = (ImageButton)findViewById(R.id.yg_dialog_activity_appkefu_plus_location_btn);
					imgLocation.setOnClickListener(new onLocationClickListener());
				
					ImageButton chatTheme = (ImageButton)findViewById(R.id.yg_dialog_activity_appkefu_plus_theme);
					chatTheme.setOnClickListener(new onThemeClickListener());
				} 
				else 
				{
					//disable plus panel
					plusRelativelayout.setVisibility(View.GONE);
					
					//enable edittext
					editText.requestFocus();
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
		
		emoji.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				View view = (View) findViewById(R.id.yg_dialog_activity_emoji_layout);
				
				if (view.getVisibility() == View.GONE)
				{
					//disable edittext and IM
					hideInputManager(DialogActivity.this);
					editText.clearFocus();
					
					//disable plus panel
					plusRelativelayout.setVisibility(View.GONE);
					
					//show emoji
					view.setVisibility(View.VISIBLE);
					listView.setSelection(listView.getCount() - 1);
				}
				else
				{
					//disable emoji
					view.setVisibility(View.GONE);
					
					//enable edittext
					editText.requestFocus();
				}
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
					startUpdateVoiceIcon();
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
						Toast.makeText(DialogActivity.this, "已取消", Toast.LENGTH_SHORT).show();
					}
					stopUpdateVoiceIcon();
				}

				return false;
			}
		});
		
		//registerReceiver(broadcastReceiver, intentFilter());
		
		uiHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case UPDATE_AMP:
					int level = Recorder.getInstance().getVolumLevel(7);
					Log.i(DEBUG_TAG, String.valueOf(level));
					int resId = DialogActivity.this.getResources().getIdentifier("yg_dialog_appkefu_voice_rcd_hint_amp" + level, "drawable", 
							DialogActivity.this.getPackageName());
					ampHint.setImageResource(resId);
					break;
				case LOCATION_TAG:
					String location = (String) msg.obj;
					setInputEnable(true, "我在[" + location + "]");
					break;
				case RANDOM_CHATTING_THEME_TAG:
					String theme = (String) msg.obj;
					setInputEnable(true, "聊聊[" + theme + "]怎么样?");
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	
	private void setInputEnable(boolean enable, String hint)
	{
		editText.setText(hint);
		editText.setClickable(enable);
		editText.setEnabled(enable);
		send.setClickable(enable);
		send.setEnabled(enable);
		emoji.setClickable(enable);
		emoji.setEnabled(enable);
		voiceButton.setClickable(enable);
		voiceButton.setEnabled(enable);
	}
	
	private void startUpdateVoiceIcon()
	{
		voiceIconUpdateTask = new TimerTask() 
		{
			@Override
			public void run()
			{
				uiHandler.sendEmptyMessage(UPDATE_AMP);
			}
		};
		voiceIconUpdateTimer = new Timer();
		voiceIconUpdateTimer.schedule(voiceIconUpdateTask, 0, voiceInconUpdateFreq);
	}
	
	private void stopUpdateVoiceIcon()
	{
		voiceIconUpdateTimer.cancel();
		voiceIconUpdateTask.cancel();
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
				else
				{
					//Nofity from status bar.
					Toast.makeText(DialogActivity.this, String.valueOf(fromUserID), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	protected void onResume()
	{
		super.onResume();
		registerReceiver(broadcastReceiver, intentFilter());
		
		Intent currentChatIntent = new Intent(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION);
		currentChatIntent.putExtra("fromUserID", friendID);
		sendBroadcast(currentChatIntent );
		
		messages.clear();
		messages.addAll(ConstantValues.user.makeDialogWith(getFriendByID(friendID)).getDialogHistory());
		msgAdapter.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	};
	
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		
		Intent currentChatIntent = new Intent(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION);
		currentChatIntent.putExtra("fromUserID", -1);
		sendBroadcast(currentChatIntent );
	};
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		Intent currentChatIntent = new Intent(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION);
		currentChatIntent.putExtra("fromUserID", -1);
		sendBroadcast(currentChatIntent );
	}
	
	private class onThemeClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			setInputEnable(false, "正在生成随机话题...");
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
//					WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
//					Object ret = wsAPI.callFuntion("requestTheme");
//					String theme = ret.toString();
					
					try
					{
						Thread.sleep(3000);
						Message msg = new Message();
						msg.what = RANDOM_CHATTING_THEME_TAG;
						msg.obj = "你喜欢看什么类型的电影";
						uiHandler.sendMessage(msg);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			});
			td.start();
		}
	}
	
	private class onLocationClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			setInputEnable(false, "正在读取您的位置...");
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					//String location = getMyLocation();
					try
					{
						Thread.sleep(3000);
						Message msg = new Message();
						msg.what = LOCATION_TAG;
						msg.obj = "北京工业大学平乐园100号";
						uiHandler.sendMessage(msg);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			});
			td.start();
		}
	}
	
	/**********************										***********************/
	/**********************			以下是选图相关函数				***********************/
	/**********************										***********************/
	/*	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));
	
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
    }  */
	
	private class onImageTakeClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			Intent intent = new Intent(DialogActivity.this, SelectImageActivity.class);
			intent.putExtra(SelectImageActivity.SELECTION_TYPE, SelectImageActivity.FROM_CAMERA);
			startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_GALLERY);
		}
	}
	
	private class onImageSelectClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			Intent intent = new Intent(DialogActivity.this, SelectImageActivity.class);
			intent.putExtra(SelectImageActivity.SELECTION_TYPE, SelectImageActivity.FROM_GALLERY);
			startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_GALLERY);
	
			/*new AlertDialog.Builder(DialogActivity.this).setTitle("请选择")
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
			}).setNegativeButton("取消", null).show(); */
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(	(requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY && resultCode == RESULT_OK)
		||  (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CAMERA && resultCode == RESULT_OK))
		{
			selectedImg = BitmapFactory.decodeFile(data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH));
		
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
		
		}/*
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
		}*/
	}
	/**********************										***********************/
	/**********************			以上是选图相关函数				***********************/
	/**********************										***********************/
}