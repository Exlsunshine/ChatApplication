package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.lj.bazingaball.ActivityBazingaBall;
import com.lj.setting.achievement.ThreadGameChallengFail;
import com.lj.shake.ActivityShake;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySongPuzzle extends Activity {

	private int userID;
	private final float RECORDER_SCALE = 0.8f;
	private boolean flag = true;
	private int gPhoneWidth;
	private int gPhoneHeight;
	private TextView[] gBlankView;
	private TextView[] charView;
	private SongPuzzleGame gSongPuzzleGame;
	private int[] gBlankSet;
	private Animation gCharZoomin;
	private Animation gCharZoomout;
	
	private AlertDialog gRightDialog = null;
	private AlertDialog gGameEndDialog = null;
	
	private int gRightNum = 0;
	
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
			case ConstantValues.InstructionCode.ERROR_NETWORK:
				break;
			case ConstantValues.InstructionCode.SONGPUZZLE_HANDLER_GET_SONGDATA:
				ArrayList<HashMap<String, Object>> songData = (ArrayList<HashMap<String, Object>>) msg.obj;
				gSongPuzzleGame = new SongPuzzleGame(songData);
				initData();
				break;
			}
		};
	};

	OnTouchListener videoControlListener = new OnTouchListener() 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			int id = v.getId();
			int action = event.getAction();
			ImageView imageView = (ImageView)v;
			if (id == FrameLayoutRecorder.VIDEO_CONTROL_START_ID)
			{
				if (action == MotionEvent.ACTION_DOWN)
					imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_start_down));
				else if (action == MotionEvent.ACTION_UP)
				{
					imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_start));
					gSongPuzzleGame.play(ActivitySongPuzzle.this);
				}
			}
			else if (id == FrameLayoutRecorder.VIDEO_CONTROL_NEXT_ID) 
			{
				if (action == MotionEvent.ACTION_DOWN)
					imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_next_down));
				else if (action == MotionEvent.ACTION_UP)
				{
					imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_next));
					if (gSongPuzzleGame.isFinish())
					{
						Toast.makeText(ActivitySongPuzzle.this, "Already last.", Toast.LENGTH_LONG).show();
						return true;
					}
					gSongPuzzleGame.stop();
					if (gSongPuzzleGame.isFinish())
					{
						if (gRightNum >= 1)
							Toast.makeText(ActivitySongPuzzle.this, "Win", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(ActivitySongPuzzle.this, "Lose", Toast.LENGTH_LONG).show();
						return true;
					}
					String answer = "";
					for (int i = 0; i < gBlankSet.length; i++)
					{
						if (gBlankSet[i] == -1)
							break;
						else
							answer += gBlankView[i].getText().toString();
					}
					if (answer.length() < gSongPuzzleGame.getCurrentAnswer().length())
					{
						showAnswerFullDialog();
						return true;
					}
					if (gSongPuzzleGame.isRight(answer))
					{ 
						showAnswerDialog(gSongPuzzleGame.getCurrentAnswer(), answer, true);
						gRightNum++;
					}
					else
						showAnswerDialog(gSongPuzzleGame.getCurrentAnswer(), answer, false);
					gSongPuzzleGame.next();
					if (!gSongPuzzleGame.isFinish())
						initData();
					else
					{
						gRightDialog.dismiss();
						showGameEndDialog(gRightNum);
						gRightDialog.show();
					}
				}
			}
			return true;
		}
	};
	
	private void showAnswerFullDialog()
	{
		final AlertDialog dialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = dialog.getWindow();
		window.setContentView(R.layout.lj_songpuzzle_remindnotfull_dialog);
		Button btn = (Button) window.findViewById(R.id.lj_songpuzzle_remind_dialog_btn);
		btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				dialog.dismiss();
			}
		});
	}
	
	private void showGameEndDialog(int num)
	{
		gGameEndDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		gGameEndDialog.setCanceledOnTouchOutside(true);
		gGameEndDialog.show();
		gGameEndDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		gGameEndDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		gGameEndDialog.setCancelable(false);
		
		Window window = gGameEndDialog.getWindow();
		window.setContentView(R.layout.lj_songpuzzle_gameend_dialog);
		
		TextView correctText = (TextView) window.findViewById(R.id.lj_songpuzzle_dialog_correctnum_text);
		String str = "您回答正确数量：" + num;
		correctText.setText(str);
		
		TextView status = (TextView) window.findViewById(R.id.lj_songpuzzle_dialog_end_status_test);
		Button btn = (Button) window.findViewById(R.id.lj_songpuzzle_dialog_op);
		if (num >= 1)
		{
			status.setText("恭喜您挑战成功");
			btn.setText("查看信息");
			btn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					Toast.makeText(ActivitySongPuzzle.this, "挑战成功", Toast.LENGTH_SHORT).show();
					Thread td = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							ConstantValues.user.makeFriendWith(userID, ActivitySongPuzzle.this);
						}
					});
					td.start();
					setResult(ActivityShake.RESULT_CODE_FRIENDADD);
					finish();
				}
			});
		}
		else
		{
			status.setText("很遗憾您挑战失败");
			btn.setText("退出");
			btn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					new ThreadGameChallengFail(ConstantValues.user.getID(), userID).start();
					finish();
				}
			});
		}
		
	}
	
	private void showAnswerDialog(String rightAnswer, String userAnswer, boolean flag)
	{
		gRightDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		gRightDialog.setCanceledOnTouchOutside(true);
		gRightDialog.show();
		gRightDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		gRightDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = gRightDialog.getWindow();
		window.setContentView(R.layout.lj_songpuzzle_right_dialog);
		
		TextView flagText = (TextView) window.findViewById(R.id.lj_songpuzzle_dialog_status);
		if (flag)
			flagText.setText("恭喜您回答正确");
		else
			flagText.setText("很遗憾您回答错误");
		TextView rightAnswerText = (TextView) window.findViewById(R.id.lj_songpuzzle_dialog_rightanswer);
		rightAnswerText.setText("正确答案："  + rightAnswer);
		
		TextView userAnswerText = (TextView) window.findViewById(R.id.lj_songpuzzle_dialog_useranswer);
		userAnswerText.setText("您的答案：" + userAnswer);
		
		Button next = (Button) window.findViewById(R.id.lj_songpuzzle_dialog_next);
		next.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				gRightDialog.dismiss();
			}
		});
	}
	
	OnClickListener charClickListener = new OnClickListener() 
	{
		private int getBlankIndex()
		{
			for (int i = 0; i < gBlankSet.length; i++)
				if (gBlankSet[i] == -1)
					return i;
			return -1;
		}
		
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			String str = ((TextView)v).getText().toString();
			int index = getBlankIndex();
			if (index == -1)
				return;
			gBlankSet[index] = id;
			gBlankView[index].setText(str);
			v.setEnabled(false);
			v.setVisibility(View.INVISIBLE);
			v.startAnimation(gCharZoomin);
			gBlankView[index].startAnimation(gCharZoomout);
		}
	};
	
	OnClickListener blankClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			if (gBlankSet[id] == -1)
				return;
			else
			{
				int index = gBlankSet[id];
				gBlankView[id].startAnimation(gCharZoomin);
				gBlankView[id].setText("");
				charView[index].startAnimation(gCharZoomout);
				charView[index].setVisibility(View.VISIBLE);
				charView[index].setEnabled(true);
				gBlankSet[id] = -1;
			}
		}
	};
	
	private void initData()
	{
		String answer = gSongPuzzleGame.getCurrentAnswer();
		if (answer== null)
			return;
		String choiceChar = gSongPuzzleGame.getCurrentChoiceChar();
		int length = answer.length();
		gBlankSet = new int[length];
		for (int i = 0; i < gBlankView.length; i++)
		{
			gBlankView[i].setText("");
			if (i < length)
			{
				gBlankView[i].setTextColor(Color.BLACK);
				gBlankView[i].setVisibility(View.VISIBLE);
				gBlankView[i].startAnimation(gCharZoomout);
				gBlankSet[i] = -1;
			}
			else
				gBlankView[i].setVisibility(View.GONE);
		}
		for (int i = 0; i < charView.length; i++)
		{
			charView[i].setTextColor(Color.BLACK);
			charView[i].setText(String.valueOf(choiceChar.charAt(i)));
			charView[i].setEnabled(true);
			charView[i].setVisibility(View.VISIBLE);
			charView[i].startAnimation(gCharZoomout);
		}
	}
	
	private void initUI()
	{
		LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.lj_songpuzzle_mainlayout);
		
		TextView infoBoard = new TextView(getApplicationContext());
		LinearLayout.LayoutParams infoBoardLayout = new LayoutParams(gPhoneWidth, gPhoneHeight / 15);
		infoBoardLayout.topMargin = gPhoneHeight / 30;
		infoBoard.setLayoutParams(infoBoardLayout);
		infoBoard.setTextSize(gPhoneHeight / 50);
		infoBoard.setText("歌曲名");
		infoBoard.setGravity(Gravity.CENTER);
		infoBoard.setBackgroundResource(R.drawable.lj_songpuzzle_answertypeboard);
		mainLinearLayout.addView(infoBoard);
		
		LinearLayout answerBoardLinearLayout = new LinearLayout(getApplicationContext());
		LinearLayout.LayoutParams answerBoardLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		answerBoardLinearLayout.setLayoutParams(answerBoardLayout);
		answerBoardLinearLayout.setBackgroundResource(R.drawable.lj_songpuzzle_answerboard);
		answerBoardLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mainLinearLayout.addView(answerBoardLinearLayout);
		
		LinearLayout blankLinearLayout = new LinearLayout(getApplicationContext());
		LinearLayout.LayoutParams blankLayout = new LayoutParams(gPhoneWidth, gPhoneHeight / 15);
		blankLayout.topMargin = gPhoneHeight / 30;
		blankLinearLayout.setLayoutParams(blankLayout);
		blankLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		blankLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		answerBoardLinearLayout.addView(blankLinearLayout);
		
		gBlankView = new TextView[6];
		for (int i = 0; i < gBlankView.length; i++)
		{
			gBlankView[i] = new TextView(getApplicationContext());
			LinearLayout.LayoutParams blankViewLayout = new LayoutParams(gPhoneHeight / 15, gPhoneHeight / 15);
			gBlankView[i].setLayoutParams(blankViewLayout);
			gBlankView[i].setGravity(Gravity.CENTER);
			gBlankView[i].setBackgroundResource(R.drawable.lj_songpuzzle_blankboard);
			gBlankView[i].setTextSize(gPhoneHeight / 50);
			gBlankView[i].setId(i);
			gBlankView[i].setOnClickListener(blankClickListener);
			blankLinearLayout.addView(gBlankView[i]);
		}
		
		charView = new TextView[18];
		for (int i = 0; i < 3; i++)
		{
			LinearLayout charLinearLayout = new LinearLayout(getApplicationContext());
			LinearLayout.LayoutParams charLayout = new LayoutParams(gPhoneWidth, gPhoneHeight / 15);
			charLayout.topMargin = gPhoneHeight / 40;
			charLinearLayout.setLayoutParams(charLayout);
			charLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			charLinearLayout.setGravity(Gravity.CENTER);
			answerBoardLinearLayout.addView(charLinearLayout);
			for (int j = 0; j < 6; j++)
			{
				int index = i * 6 + j;
				charView[index] = new TextView(getApplicationContext());
				LinearLayout.LayoutParams charViewLayout = new LayoutParams(gPhoneHeight / 13, gPhoneHeight / 13);
				charView[index].setLayoutParams(charViewLayout);
				charView[index].setGravity(Gravity.CENTER);
				charView[index].setBackgroundResource(R.drawable.lj_songpuzzle_charboard);
				charView[index].setTextSize(gPhoneHeight / 50);
				charView[index].setId(index);
				charView[index].setOnClickListener(charClickListener);
				charLinearLayout.addView(charView[index]);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_activity_songpuzzle); 
		gCharZoomin = AnimationUtils.loadAnimation(ActivitySongPuzzle.this, R.anim.lj_songpuzzle_char_zoomin);
		gCharZoomout = AnimationUtils.loadAnimation(ActivitySongPuzzle.this, R.anim.lj_songpuzzle_char_zoomout);
		Intent intent = getIntent();
	    userID = intent.getIntExtra("userID", 0);
	    setupDialogActionBar();
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleTextView = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleTextView.setText("音乐闯关");
		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new ThreadGameChallengFail(ConstantValues.user.getID(), userID).start();
				gSongPuzzleGame.stop();
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			new ThreadGameChallengFail(ConstantValues.user.getID(), userID).start();
			gSongPuzzleGame.stop();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && flag)
		{
			FrameLayoutRecorder layout = (FrameLayoutRecorder)findViewById(R.id.layout);
			
			DisplayMetrics metrics=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			gPhoneWidth = metrics.widthPixels;
			gPhoneHeight = metrics.heightPixels;
			int widthPixels=(int) (metrics.widthPixels * RECORDER_SCALE);
			
			layout.initLayout(widthPixels);
			layout.setVideoControlListener(videoControlListener);
			
			initUI();
			flag = false;
			new ThreadGetSongData(gHandler).start();
		}
	}
}
