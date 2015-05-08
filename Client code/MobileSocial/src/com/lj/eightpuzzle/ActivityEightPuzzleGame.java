package com.lj.eightpuzzle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.example.testmobiledatabase.R;
import com.lj.satellitemenu.SatelliteMenu;
import com.lj.satellitemenu.SatelliteMenuItem;
import com.lj.satellitemenu.SatelliteMenu.SateliteClickedListener;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityEightPuzzleGame extends Activity
{
	private int userID; 
	private final int[] EIGHTPUZZLE_IMAGEVIEW_ID = {R.id.lj_img_00, R.id.lj_img_01, R.id.lj_img_02,
													R.id.lj_img_10, R.id.lj_img_11, R.id.lj_img_12,
													R.id.lj_img_20, R.id.lj_img_21, R.id.lj_img_22};
	private final int SPEED_MIN = 3000;
	private final int SPEED_MAX = 4500;
	private final int SPEED_THREOLD = 1000;
	private final int ANIMATION_DURATION = 300;
	
	private final int MENU_ITEM_RESTART = 1;
	private final int MENU_ITEM_HINT = 2;
	
	private float moveLength ;
	private EightPuzzleImageViewBoard gImageViewBoard;
	private Bitmap eightPuzzleAnswerImage;
	private TextView gStepTextView;
	private int gDirection;
	private int gStep = 0;
	
	private GestureDetector gestureDetector;
	private TranslateAnimation moveRight;
	private TranslateAnimation moveLeft;
	private TranslateAnimation moveUp;
	private TranslateAnimation moveDown;
	
	private TranslateAnimation updateStep;
	
	private LinkedList<Integer> moveStepList;
	
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
			case ConstantValues.InstructionCode.ERROR_NETWORK:
				break;
			case ConstantValues.InstructionCode.GAMESET_HANDLER_DOWNLOAD_IMAGE:
				eightPuzzleAnswerImage = (Bitmap) msg.obj;
		//		eightPuzzleAnswerImage = BitmapFactory.decodeResource(getResources(),R.drawable.a); 
				gImageViewBoard = new EightPuzzleImageViewBoard(createImageVIewBoard(), eightPuzzleAnswerImage, getApplicationContext());
				moveLength = gImageViewBoard.getImageViewSize();
				moveRight = new TranslateAnimation(0, moveLength, 0, 0);
		        moveRight.setDuration(ANIMATION_DURATION);
		        moveRight.setAnimationListener(moveAnimationListener);
		        
		        
		        moveLeft = new TranslateAnimation(0, -moveLength, 0, 0);
		        moveLeft.setDuration(ANIMATION_DURATION);
		        moveLeft.setAnimationListener(moveAnimationListener);
		        
		        moveUp = new TranslateAnimation(0, 0, 0, -moveLength);
		        moveUp.setDuration(ANIMATION_DURATION);
		        moveUp.setAnimationListener(moveAnimationListener);
		        
		        moveDown = new TranslateAnimation(0, 0, 0, moveLength);
		        moveDown.setDuration(ANIMATION_DURATION);
		        moveDown.setAnimationListener(moveAnimationListener);
		        
		        updateStep = new TranslateAnimation(0, 0, 0, -100);
		        updateStep.setDuration(ANIMATION_DURATION);
		        updateStep.setAnimationListener(updateStepAnimationListener);
				break;
			}
		};
	};
	
	private AnimationListener updateStepAnimationListener = new AnimationListener() 
	{
		
		@Override
		public void onAnimationStart(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationEnd(Animation animation) 
		{
			gStepTextView.setText(gStep + "");
		}
	};
	
	private AnimationListener moveAnimationListener = new AnimationListener() 
	{
		@Override
		public void onAnimationStart(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationEnd(Animation animation) 
		{
			gImageViewBoard.getCurrentMoveImageView(gDirection).clearAnimation();
			gImageViewBoard.excMove(gDirection);
			moveStepList.removeFirst();
			gStep++;
			gStepTextView.startAnimation(updateStep);
			if (gImageViewBoard.isWin())
			{
				Toast.makeText(ActivityEightPuzzleGame.this, "Wwwwwwwwwwwwwwwwwwwwwwwin", Toast.LENGTH_LONG).show();
				moveStepList.clear();
			}
			if (!moveStepList.isEmpty())
			{
				gDirection = moveStepList.getFirst();
				if (gImageViewBoard.getMoveIndex(gDirection) == -1)
				{
					moveStepList.clear();
					return;
				}
				switch (gDirection)
				{
				case ConstantValues.InstructionCode.DIRECTION_RIGHT:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveRight);
					break;
				case ConstantValues.InstructionCode.DIRECTION_LEFT:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveLeft);
					break;
				case ConstantValues.InstructionCode.DIRECTION_DOWN:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveDown);
					break;
				case ConstantValues.InstructionCode.DIRECTION_UP:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveUp);
					break;
				}
			}
		}
	};
	
	GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
	{
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
		{
		
			if (Math.abs(velocityX) - Math.abs(velocityY) >= SPEED_THREOLD)
			{
				if (velocityX > 0)
					moveStepList.addLast(ConstantValues.InstructionCode.DIRECTION_RIGHT);
				else
					moveStepList.addLast(ConstantValues.InstructionCode.DIRECTION_LEFT);
			}
			else if (Math.abs(velocityY) - Math.abs(velocityX) >= SPEED_THREOLD)
			{
				if (velocityY > 0)
					moveStepList.addLast(ConstantValues.InstructionCode.DIRECTION_DOWN);
				else
					moveStepList.addLast(ConstantValues.InstructionCode.DIRECTION_UP);
			}
			if (moveStepList.size() == 1)
			{
				gDirection = moveStepList.getFirst();
				if (gImageViewBoard.getMoveIndex(gDirection) == -1)
				{
					moveStepList.clear();
					return false;
				}
				switch (gDirection)
				{
				case ConstantValues.InstructionCode.DIRECTION_RIGHT:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveRight);
					break;
				case ConstantValues.InstructionCode.DIRECTION_LEFT:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveLeft);
					break;
				case ConstantValues.InstructionCode.DIRECTION_DOWN:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveDown);
					break;
				case ConstantValues.InstructionCode.DIRECTION_UP:
					gImageViewBoard.getCurrentMoveImageView(gDirection).startAnimation(moveUp);
					break;
				}
			}
			return true;
		};
	};
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return gestureDetector.onTouchEvent(event);
	}
	

	
	private ImageView[] createImageVIewBoard()
	{
		ImageView[] board = new ImageView[EIGHTPUZZLE_IMAGEVIEW_ID.length];
		for (int i = 0; i < EIGHTPUZZLE_IMAGEVIEW_ID.length; i++)
			board[i] = (ImageView) findViewById(EIGHTPUZZLE_IMAGEVIEW_ID[i]);
		return board;
	}
	
	private void restart()
	{
		gImageViewBoard.restart();
		gStep = 0;
		gStepTextView.setText("0");
	}
	

	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lj_activity_eightpuzzle);
        
        
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);
        
        gestureDetector = new GestureDetector(this,onGestureListener);
        
        moveStepList = new LinkedList<Integer>();
        
        gStepTextView = (TextView)findViewById(R.id.lj_step);
        
        
        
        Button restartBtn = (Button) findViewById(R.id.lj_eightpuzzle_restart_button);
        restartBtn.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				new AlertDialog.Builder(ActivityEightPuzzleGame.this)   
				.setTitle("确认")  
				.setMessage("确定重新开始？")  
				.setPositiveButton("是", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						restart();
					}
				})  
				.setNegativeButton("否", null)  
				.show();  
			}
		});
        new ThreadDownloadGameImage(userID, gHandler).start();
        
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
		titleTextView.setText("图片迷城");
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
    public void onWindowFocusChanged(boolean hasFocus) {
    	// TODO Auto-generated method stub
    	super.onWindowFocusChanged(hasFocus);
    	
    }
}
