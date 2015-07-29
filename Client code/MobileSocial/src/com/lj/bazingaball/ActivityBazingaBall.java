package com.lj.bazingaball;



import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.example.testmobiledatabase.R;
import com.lj.setting.achievement.ThreadGameChallengFail;
import com.lj.shake.ActivityShake;
import com.yg.commons.ConstantValues;
import com.yg.guide.manager.GuideComplete;
import com.yg.guide.manager.UserGuide;
import com.yg.guide.tourguide.Overlay;
import com.yg.user.WebServiceAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityBazingaBall extends Activity
{
	private final int MAX_VELOCITY = 6;   //max velocity of random velocity
	public static int BUTTON_SIZE = 500;  //initial button's size
	public static final int BAZINGABALL_REQUEST_CODE = 0x10;
	public static final int BAZINGABALL_RESULT_CODE = 0x11;
	private final int GAMEEND_TEXT_SIZE = 20;
	private final int UPLEFT = 0x00;
	private final int UPRIGHT = 0x01;
	private final int BOTTOMLEFT = 0x10;
	private final int BOTTOMRIGHT = 0x11;
	private final int []DIVBUTTON = {UPLEFT, UPRIGHT, BOTTOMLEFT, BOTTOMRIGHT}; //mark of button's position 0 : UPLEFT; 1 : UPRIGHT; 2 : BOTTOMLEFT; 3 : BOTTOMRIGHT;
	private final int []PICTURE = {R.drawable.lj_bazingaball_pink, R.drawable.lj_bazingaball_red, R.drawable.lj_bazingaball_violet, R.drawable.lj_bazingaball_yellow};
	
	Random random = new Random();
	private int phoneWidth;    //width of drawable view of facility
	private int phoneHeight;   //height of drawable view of facility
	
	public static int gamebegin = 0;
	
	private int goalScore = 0;
	private boolean flag = true;
	private boolean isWin = false;
	
	RelativeLayout mainlayout;
	LinkedList<BazingaButton> buttonset;
	private TextView myScoreTextView;
	private TextView goalSocreTextView;
	private ThreadUpdateScore gThreadUpdateScore;
	
	private int userID;
	private int gRequestCode = 0;
	private boolean guideFlag = true;
	
	private Handler myhandler = new Handler()
	{
		private void checkGone()
		{
			Iterator<BazingaButton> temp = buttonset.iterator();
			while(temp.hasNext())
			{
				BazingaButton t = temp.next();
				if (t.getVisibility() == View.GONE)
				{
					mainlayout.removeView(t);
					temp.remove();
				}
			}
		}
		
		private void checkGameState()
		{
			if (gamebegin == 1)
			{
				int n = 0;
				for (int i = 0; i < buttonset.size(); i++)
				{
					BazingaButton a = buttonset.get(i);
					if (a.getVisibility() == View.GONE)
						continue;
					if (a.width > BUTTON_SIZE / BazingaButton.GAME_LEVEL)
						n++;
				}
				if (n >= 2)
				{
					gamebegin = 2;
					gThreadUpdateScore.disableUpdateScore();
					TextView gameOverText = new TextView(ActivityBazingaBall.this);
					gameOverText.setTextSize(GAMEEND_TEXT_SIZE);
					RelativeLayout.LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					layout.addRule(RelativeLayout.CENTER_IN_PARENT);
					gameOverText.setLayoutParams(layout);
					int myScore = Integer.valueOf(myScoreTextView.getText().toString());
					if (gRequestCode != BAZINGABALL_REQUEST_CODE)
					{
						String str = "游戏结束\r\n" + "您当前得分：" + myScore + "\r\n" + "您挑战对象得分：" + goalScore + "\r\n";
						if (myScore >= goalScore)
						{
							str += "恭喜您挑战成功\r\n" + "请点击叉子气泡浏览信息";
							isWin = true;
						}
						else
						{
							str += "很遗憾您挑战失败\r\n" + "请点击叉子退出游戏";
							isWin = false;
						}
						gameOverText.setGravity(Gravity.CENTER);
						gameOverText.setText(str);
						gameOverText.setTextColor(Color.WHITE);
						mainlayout.addView(gameOverText);
						
						for (int i = 0; i < buttonset.size(); i++)
						{
							BazingaButton a = buttonset.get(i);
							a.viewtype = BazingaButton.COLLISION_VIEW;
						}
					}
					else
					{
						String str = "游戏结束\r\n" + "您当前得分：" + myScore + "\r\n" + "您最高得分：" + goalScore + "\r\n";
						if (myScore >= goalScore)
						{
							str += "恭喜您挑战成功\r\n" + "请点击叉子气泡返回上级";
							isWin = true;
						}
						else
						{
							str += "很遗憾您挑战失败\r\n" + "请点击叉子返回上级";
							isWin = false;
						}
						gameOverText.setGravity(Gravity.CENTER);
						gameOverText.setText(str);
						gameOverText.setTextColor(Color.WHITE);
						mainlayout.addView(gameOverText);
						
						for (int i = 0; i < buttonset.size(); i++)
						{
							BazingaButton a = buttonset.get(i);
							a.viewtype = BazingaButton.COLLISION_VIEW;
						}
					}
				}
				return;
			}
			for (int i = 0; i < buttonset.size(); i++)
			{
				BazingaButton a = buttonset.get(i);
				if (a.width > BUTTON_SIZE / 4)
					return;
			}
			if (guideFlag)
			{
				if (UserGuide.isNeedUserGuide(ActivityBazingaBall.this, UserGuide.GAME_BUBBLE_ACTIVITY))
					setupGameBeginGuide();
	    		else
	    		{
					Toast.makeText(ActivityBazingaBall.this, "游戏开始", Toast.LENGTH_LONG).show();
					gamebegin = 1;
					gThreadUpdateScore.start();
					for (int i = 0; i < buttonset.size(); i++)
					{
						BazingaButton a = buttonset.get(i);
						a.viewtype = BazingaButton.SWALLOW_VIEW;
					}
	    		}
			}
			guideFlag = false;
		}
		
		private void gameMode()
		{
			for (int i = 0; i < buttonset.size(); i++)
			{
				BazingaButton a = buttonset.get(i);
				if (a.getVisibility() == View.GONE)
					continue;
				if (a.viewtype == BazingaButton.COLLISION_VIEW)
					for (int j = i + 1; j < buttonset.size(); j++)
					{
						BazingaButton b = buttonset.get(j);
						if (b.getVisibility() == View.GONE)
							continue;
						double time = a.isHit(b);
						if (time < 3)
							a.hit(b);
					} 
				else if (a.viewtype == BazingaButton.SWALLOW_VIEW)
				{
					for (int j = 0; j < buttonset.size(); j++)
					{
						BazingaButton b = buttonset.get(j);
						if (a == b || b.getVisibility() == View.GONE)
							continue;
						if (b.viewtype == BazingaButton.COLLISION_VIEW)
						{
							double time = a.isHit(b);
							if (time < 3)
								a.hit(b);
						}
						else if (a.isBiggerThan(b) && a.checkSwallow(b))
							a.swallow(b);
					}
				}
			}
		}
		
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) 
		{
		synchronized (ActivityBazingaBall.class)
		{
			super.handleMessage(msg);
			switch(msg.what)
			{
			case ConstantValues.InstructionCode.BAZINGABALL_HANDLER_MOVE:
				gameMode();
				checkGone();
				checkGameState();
				break;
			case ConstantValues.InstructionCode.BAZINGABALL_HANDLER_UPDATE_SCORE:
				int score = msg.arg1;
				if (score > goalScore)
					myScoreTextView.setTextColor(Color.RED);
				myScoreTextView.setText(String.valueOf(score));
				myScoreTextView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lj_update_score));
				break;
			case ConstantValues.InstructionCode.BAZINGABALL_HANDLER_GET_SCORE:
				String result = msg.obj.toString();
				goalScore = Integer.valueOf(result);
				setStartView();
				reStart();
				iniMode();
				initScoreTextView();
				if (UserGuide.isNeedUserGuide(ActivityBazingaBall.this, UserGuide.GAME_BUBBLE_ACTIVITY))
					setupUserGuide();
				else
					buttonset.get(0).setOnTouchListener(ButtonTouchlistener);
				new ThreadButtonMove(myhandler).start();
				gThreadUpdateScore = new ThreadUpdateScore(myhandler);
				break;
			}
		}}
	};
	
	private void onTouchPerform(View v)
	{
		if (((BazingaButton)v).isAlive())
		{
			mainlayout.removeView(v);
			buttonset.remove((BazingaButton)v);
			v.setVisibility(View.GONE);
			BazingaButton []btnarray = divBazingaButton((BazingaButton)v);
			for (int i = 0; i < 4; i++)
				mainlayout.addView(btnarray[i]);
		}
		else if (!((BazingaButton)v).isAlive() && gamebegin == 2)
		{
			if (gRequestCode != BAZINGABALL_REQUEST_CODE)
			{
				if (isWin)
				{
					Toast.makeText(ActivityBazingaBall.this, "挑战成功", Toast.LENGTH_SHORT).show();
					Thread td = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							ConstantValues.user.makeFriendWith(userID, ActivityBazingaBall.this);
						}
					});
					td.start();
					iniMode();
					reStart();
					setResult(ActivityShake.RESULT_CODE_FRIENDADD);
					finish();
				}
				else
				{
					Toast.makeText(ActivityBazingaBall.this, "解密失败", Toast.LENGTH_LONG).show();
					new ThreadGameChallengFail(ConstantValues.user.getID(), userID).start();
					gameFinish();
				}
			}
			else
			{
				if (isWin)
				{
					int myScore = Integer.valueOf(myScoreTextView.getText().toString());
					Intent intent = new Intent();
					intent.putExtra("score", myScore);
					setResult(BAZINGABALL_RESULT_CODE, intent);
					gameFinish();
				}
				else
				{
					Intent intent = new Intent();
					intent.putExtra("score", goalScore);
					setResult(BAZINGABALL_RESULT_CODE, intent);
					gameFinish();
				}
			}
		}
	}
	
	OnTouchListener ButtonTouchlistener = new OnTouchListener() 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				onTouchPerform(v);
			return true;
		}
	};
	
	private void init()
	{
		buttonset = new LinkedList<BazingaButton>();
	}
	
	private BazingaButton getBazingaButton(BazingaButton arg0, int position, int vx, int vy)
	{
		RelativeLayout.LayoutParams newbuttonlp = new RelativeLayout.LayoutParams(arg0.getLayoutParams());
		newbuttonlp.width = arg0.width / 2;
		newbuttonlp.height = arg0.width / 2;
		newbuttonlp.leftMargin = (int) (arg0.getX() + newbuttonlp.width * (position & 0x01));
		newbuttonlp.topMargin = (int) (arg0.getY() + newbuttonlp.height * (position >> 4 & 0x01));
		int picture = random.nextInt(4);
		BazingaButton btn = new BazingaButton(ActivityBazingaBall.this, newbuttonlp, vx, vy, phoneWidth, phoneHeight, arg0.viewtype, PICTURE[picture]);
		btn.setOnTouchListener(ButtonTouchlistener);
		buttonset.add(btn);
		return btn;
	}
	
	private BazingaButton[] divBazingaButton(BazingaButton arg0)
	{
		BazingaButton []btnarray = new BazingaButton[4];
		for (int i = 0; i < 4; i++)
		{
			int vx = random.nextInt(MAX_VELOCITY) + 1;
			int vy = random.nextInt(MAX_VELOCITY) + 1;
			vx = vx * ((DIVBUTTON[i] & 0x01) * 2 - 1);
			vy = vy * ((DIVBUTTON[i] >> 4 & 0x01) * 2 -1);
			btnarray[i] = getBazingaButton(arg0, DIVBUTTON[i], vx, vy);
		}
		return btnarray;
	}
	
	private void setStartView()
	{
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(BUTTON_SIZE, BUTTON_SIZE);
		lp.leftMargin = (phoneWidth - BUTTON_SIZE) / 2;
		lp.topMargin = (phoneHeight - BUTTON_SIZE) / 2; 
		int picture = random.nextInt(4);
		BazingaButton btn = new BazingaButton(ActivityBazingaBall.this, lp, 0, 0, phoneWidth,phoneHeight, BazingaButton.NONE,PICTURE[picture]);
		mainlayout.addView(btn);
		buttonset.add(btn);
	}
	 
	private void initScoreTextView()
	{
		myScoreTextView = new TextView(this);
		myScoreTextView.setTextSize(phoneHeight / 50);
		myScoreTextView.setGravity(Gravity.CENTER);
		myScoreTextView.setTextColor(Color.WHITE);
		myScoreTextView.setText(String.valueOf(0));
		RelativeLayout.LayoutParams mySocreLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mySocreLayoutParams.leftMargin = phoneWidth / 10;
		mySocreLayoutParams.topMargin = phoneWidth / 10;
		myScoreTextView.setLayoutParams(mySocreLayoutParams);
		mainlayout.addView(myScoreTextView);
		
		goalSocreTextView = new TextView(this);
		goalSocreTextView.setTextSize(phoneHeight / 50);
		goalSocreTextView.setGravity(Gravity.CENTER);
		goalSocreTextView.setTextColor(Color.WHITE);
		goalSocreTextView.setText(String.valueOf(goalScore));
		RelativeLayout.LayoutParams goalSocreLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		goalSocreLayoutParams.leftMargin = phoneWidth - phoneWidth / 10 - 100;
		goalSocreLayoutParams.topMargin = phoneWidth / 10;
		goalSocreTextView.setLayoutParams(goalSocreLayoutParams);
		mainlayout.addView(goalSocreTextView);
	}
	
	private void initMoleScore()
	{
		final WebServiceAPI webserviceMole = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME_SETTING, "GameMole");
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				String[] name = {"userID"};
				Object[] values = {userID};
				Object result = webserviceMole.callFuntion("getMoleScore", name, values);
				Message msg = new Message();
				msg.what = ConstantValues.InstructionCode.BAZINGABALL_HANDLER_GET_SCORE;
				msg.obj = result;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mainlayout = new RelativeLayout(this);
		mainlayout.setBackgroundColor(Color.BLACK);
		setContentView(mainlayout);
	//	setContentView(R.layout.lj_bazingaball_layout);
//		mainlayout = (RelativeLayout) findViewById(R.id.lj_bazingaball_layout);
		init();
		Intent intent = getIntent();
//		goalScore = intent.getIntExtra("goalScore", 50);
		gRequestCode = intent.getIntExtra("requestCode", 0);
		userID = intent.getIntExtra("userID", 0);
	}
	
	private TextView guideView;
	
	private UserGuide userGuide;
	
	private void setupGameBeginGuide()
	{
		guideView.setVisibility(View.VISIBLE);
		userGuide = new UserGuide(this, "游戏开始", "当没有大球存在时游戏开始。小球碰撞可互相融合，超过一定体积时，不再具备融合能力且无法点击分裂。场面上同时存在两个这样的球时，游戏结束。", Gravity.CENTER, Overlay.Style.Circle, "#990066");
		userGuide.addAnotherGuideArea(guideView, myScoreTextView, false, "分数栏", "您的得分", Gravity.RIGHT, Gravity.CENTER, "#990066");
		userGuide.addAnotherGuideArea(myScoreTextView, goalSocreTextView, true, "获胜条件", "您必须超越此分数", Gravity.LEFT, Gravity.CENTER, "#990066");
		userGuide.beginWith(guideView, false, new GuideComplete()
		{
			@Override
			public void onUserGuideCompleted()
			{
				guideView.setVisibility(View.GONE);
				Toast.makeText(ActivityBazingaBall.this, "游戏开始", Toast.LENGTH_LONG).show();
				gamebegin = 1;
				gThreadUpdateScore.start();
				for (int i = 0; i < buttonset.size(); i++)
				{
					BazingaButton a = buttonset.get(i);
					a.viewtype = BazingaButton.SWALLOW_VIEW;
				}
				UserGuide.disableUserGuide(UserGuide.GAME_BUBBLE_ACTIVITY);
			}
		});
	}
	
	private void setupUserGuide()
	{
		guideView = new TextView(getApplicationContext());
		guideView.setText("游戏说明");
		guideView.setTextSize(30);
		guideView.setTextColor(Color.WHITE);
		LayoutParams guideLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		guideLayout.topMargin = 100;
		guideLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);  
		guideLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE); 
		guideView.setLayoutParams(guideLayout);
		mainlayout.addView(guideView);
		guideView.setVisibility(View.GONE);
		userGuide = new UserGuide(this, "分裂小球", "点击屏幕中心小球使之分裂", Gravity.CENTER, Overlay.Style.Circle, "#990066");
//		userGuide.addAnotherGuideArea(recorderLayout.getLeftBox(), charView[0], false, "选项栏", "点击选择歌曲名", Gravity.TOP | Gravity.LEFT, Gravity.CENTER, "#FF9900");
//		userGuide.addAnotherGuideArea(charView[0], gBlankView[0], false, "您的答案", "点击可修改", Gravity.TOP | Gravity.LEFT, Gravity.CENTER, "#FF9900");
//		userGuide.addAnotherGuideArea(gBlankView[0], recorderLayout.getRightBox(), true, "下一首", "点击显示正误并跳至下一题", Gravity.TOP | Gravity.RIGHT, Gravity.CENTER, "#FF9900");
		userGuide.beginWith(buttonset.get(0), true, new GuideComplete()
		{
			@Override
			public void onUserGuideCompleted()
			{
			//	UserGuide.disableUserGuide(UserGuide.GAME_MUSIC_ACTIVITY);
				buttonset.get(0).setOnTouchListener(ButtonTouchlistener);
				onTouchPerform(buttonset.get(0));
				Toast.makeText(getApplicationContext(), "请继续戳破四个大气球", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && flag)
		{
			flag = false;
			phoneWidth = mainlayout.getMeasuredWidth();
			phoneHeight = mainlayout.getMeasuredHeight();
			BUTTON_SIZE = (int) (phoneWidth * 0.463);
			initMoleScore();
		}
	}
	
	
	private void iniMode()
	{
		gamebegin = 0;
		for (int i = 0; i < buttonset.size(); i++)
		{
			BazingaButton a = buttonset.get(i);
			a.viewtype = BazingaButton.COLLISION_VIEW;
		}
	}
	
	private void reStart()
	{
		mainlayout.removeAllViews();
		buttonset.clear();
		setStartView();
	}
	
	private void gameFinish()
	{
		iniMode();
		reStart();
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (gRequestCode == BAZINGABALL_REQUEST_CODE)
				gameFinish();
			else
			{
				if (isWin)
					return super.onKeyDown(keyCode, event);
				else
				{
					new ThreadGameChallengFail(ConstantValues.user.getID(), userID).start();
					gameFinish();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return super.onTouchEvent(event);
	}
}
