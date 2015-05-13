package com.lj.eightpuzzle;

import java.util.LinkedList;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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
	private final int SPEED_THREOLD = 1000;
	private final int ANIMATION_DURATION = 300;
	
	
	private final int GAME_ING = 0;
	private final int GAME_END = 1;
	private int gGameStatus = GAME_ING;
	
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
	
	private AlertDialog gRestartDialog = null;
	private AlertDialog gSuccessDialog = null;
	
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
			gStepTextView.setText(String.valueOf(gStep));
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
				showSuccessDialog();
				gGameStatus = GAME_END;
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
		if (gGameStatus == GAME_ING)
			return gestureDetector.onTouchEvent(event);
		else
			return false;
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
        String nikcname = intent.getStringExtra("nickname");
        gestureDetector = new GestureDetector(this,onGestureListener);
        
        moveStepList = new LinkedList<Integer>();
        
        gStepTextView = (TextView)findViewById(R.id.lj_step);
        
        TextView nicknameText = (TextView)findViewById(R.id.lj_eightpuzzle_nickname);
        nicknameText.setText(nikcname);
        
        new ThreadDownloadGameImage(userID, gHandler).start();
        
        setupDialogActionBar();
        gGameStatus = GAME_ING;
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
		
		TextView restartTextView = (TextView)findViewById(R.id.lj_common_actionbar_confirm_text);
		restartTextView.setText("重来");
		restartTextView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				showRestartDialog();
			}
		});
		
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
    public void onWindowFocusChanged(boolean hasFocus) 
    {
    	super.onWindowFocusChanged(hasFocus);
    	
    }

    private void showRestartDialog()
	{
		gRestartDialog = new android.app.AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		gRestartDialog.setCanceledOnTouchOutside(true);
		gRestartDialog.show();
		gRestartDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		gRestartDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = gRestartDialog.getWindow();
		window.setContentView(R.layout.lj_eightpuzzle_restart_dialog);
		
		
		Button cancel = (Button) window.findViewById(R.id.lj_eightpuzzle_restart_dialog_button_cancel);
		Button confirm = (Button) window.findViewById(R.id.lj_eightpuzzle_restart_dialog_button_confirm);
		cancel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				gRestartDialog.dismiss();
			}
		});
		confirm.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				restart();
				gRestartDialog.dismiss();
			}
		});
	}
    
    private void showSuccessDialog()
    {
    	gSuccessDialog = new android.app.AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
    	gSuccessDialog.setCanceledOnTouchOutside(true);
    	gSuccessDialog.show();
    	gSuccessDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    	gSuccessDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = gSuccessDialog.getWindow();
		window.setContentView(R.layout.lj_eightpuzzle_success_dialog);
		
		
		gSuccessDialog.setCancelable(false);
		Button look = (Button) window.findViewById(R.id.lj_eightpuzzle_dialog_op);
		look.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(ActivityEightPuzzleGame.this, "挑战成功", Toast.LENGTH_SHORT).show();
				
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						ConstantValues.user.makeFriendWith(userID, ActivityEightPuzzleGame.this);
					}
				});
				td.start();
				
				gSuccessDialog.dismiss();
			}
		});
    }
}
