package com.lj.shake;


import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FrameLayoutUserInfo extends FrameLayout
{
	private String gNickName = null;
	private int gGameType = 0;
	private int gUserID = 0;
	
	private final String EIGHTPUZZLE_STRING = "Í¼Æ¬ÃÔ³Ç";
	private final String SONGPUZZLE_STRING = "¸èÇú´³¹Ø";
	private final String BAZINGABALL_STRING = "´ÁÆøÅÝ";
	
	private TextView gNickNameTextView = null;
	private TextView gGameTypeTextView = null;
	private TextView gBeginGameTextView = null;
	
	public FrameLayoutUserInfo(Context context) 
	{
		super(context); 
		init(context);
	} 

	public FrameLayoutUserInfo(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.lj_map_userinfo, this, true);
		gNickNameTextView = (TextView)findViewById(R.id.lj_map_nickname); 
		gGameTypeTextView = (TextView)findViewById(R.id.lj_map_gametype);
		gBeginGameTextView = (TextView)findViewById(R.id.lj_map_begingame);
	}
	
	public void initUserData(int userID, String nickname, int gametype)
	{
		gUserID = userID;
		gNickName = nickname;
		gNickNameTextView.setText(gNickName);
		gGameType = gametype;
		String str = null;
		switch (gGameType)
		{
		case ConstantValues.InstructionCode.GAME_TYPE_EIGHTPUZZLE:
			str = EIGHTPUZZLE_STRING;
			break;
		case ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE:
			str = SONGPUZZLE_STRING;
			break;
		case ConstantValues.InstructionCode.GAME_TYPE_BAZINGABALL:
			str = BAZINGABALL_STRING;
			break;
		}
		gGameTypeTextView.setText(str);
	}

	public void setBeginGameTouchListener(OnTouchListener touchlistener)
	{
		gBeginGameTextView.setOnTouchListener(touchlistener);
	}
}
