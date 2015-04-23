package com.lj.bazingaball;



import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.user.WebServiceAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
	public static final int BUTTON_SIZE = 500;  //initial button's size
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
	
	Handler myhandler = new Handler()
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
					RelativeLayout.LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					layout.addRule(RelativeLayout.CENTER_IN_PARENT);
					gameOverText.setLayoutParams(layout);
					int myScore = Integer.valueOf(myScoreTextView.getText().toString());
					String str = "��Ϸ����\r\n" + "����ǰ�÷֣�" + myScore + "\r\n" + "����ս����÷֣�" + goalScore + "\r\n";
					if (myScore >= goalScore)
					{
						str += "��ϲ����ս�ɹ�\r\n" + "�����������������Ϣ";
						isWin = true;
					}
					else
					{
						str += "���ź�����սʧ��\r\n" + "���������˳���Ϸ";
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
				return;
			}
			for (int i = 0; i < buttonset.size(); i++)
			{
				BazingaButton a = buttonset.get(i);
				if (a.width > BUTTON_SIZE / 4)
					return;
			}
			
			for (int i = 0; i < buttonset.size(); i++)
			{
				BazingaButton a = buttonset.get(i);
				a.viewtype = BazingaButton.SWALLOW_VIEW;
			}
			Toast.makeText(ActivityBazingaBall.this, "Game Begin", Toast.LENGTH_LONG).show();
			gamebegin = 1;
			gThreadUpdateScore.start();
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
				new ThreadButtonMove(myhandler).start();
				gThreadUpdateScore = new ThreadUpdateScore(myhandler);
				break;
			}
		}}
	};
	
	OnTouchListener ButtonTouchlistener = new OnTouchListener() 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
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
					if (isWin)
						Toast.makeText(ActivityBazingaBall.this, "Win", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(ActivityBazingaBall.this, "Lose", Toast.LENGTH_LONG).show();
			}
			return false;
		}
	};
	
	private void init()
	{
		buttonset = new LinkedList<BazingaButton>();
	}
	
	private BazingaButton getBazingaButton(BazingaButton arg0, int position, int vx, int vy)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) arg0.getLayoutParams();
		RelativeLayout.LayoutParams newbuttonlp = new RelativeLayout.LayoutParams(lp);
		newbuttonlp.width = arg0.width / 2;
		newbuttonlp.height = arg0.width / 2;
		newbuttonlp.leftMargin = lp.leftMargin + newbuttonlp.width * (position & 0x01);
		newbuttonlp.topMargin = lp.topMargin + newbuttonlp.height * (position >> 4 & 0x01);
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
		btn.setOnTouchListener(ButtonTouchlistener);
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
		init();
		Intent intent = getIntent();
//		goalScore = intent.getIntExtra("goalScore", 50);
		userID = intent.getIntExtra("userID", 0);
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
			initMoleScore();
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//getMenuInflater().inflate(R.menu.main, menu);  
		//return super.onCreateOptionsMenu(menu);
		return false;
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			iniMode();
			reStart();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return super.onTouchEvent(event);
	}
}