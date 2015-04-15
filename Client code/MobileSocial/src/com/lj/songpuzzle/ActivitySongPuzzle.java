package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
					if (gSongPuzzleGame.isRight(answer))
					{
						gRightNum++;
						Toast.makeText(ActivitySongPuzzle.this, "Right", Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(ActivitySongPuzzle.this, "Wrong", Toast.LENGTH_LONG).show();
					gSongPuzzleGame.next();
					initData();
				}
			}
			return true;
		}
	};
	
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
				gBlankView[i].setVisibility(View.VISIBLE);
				gBlankView[i].startAnimation(gCharZoomout);
				gBlankSet[i] = -1;
			}
			else
				gBlankView[i].setVisibility(View.GONE);
		}
		for (int i = 0; i < charView.length; i++)
		{
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
		infoBoard.setText("¸èÇúÃû");
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
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
			this.finish();
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
