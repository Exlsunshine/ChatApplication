package com.lj.setting.game;


import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.lj.bazingaball.ActivityBazingaBall;
import com.lj.eightpuzzle.ThreadDownloadGameImage;
import com.lj.ui.switchbutton.SwitchButton;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.image.select.ui.SelectImageActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentGameSetting extends Fragment
{
//	private static final int ACTIVITY_RESULT_CODE_EIGHTPUZZLE = 1;
	private static final int ACTIVITY_REQUEST_CODE_EIGHTPUZZLE = 1;
	
	private boolean hasEightpuzzle = true;
	private boolean hasBazingball = false;
	
	private Context gContext;
	private View gView;
	private SwitchButton gEightPuzzleSwitch = null;
	private SwitchButton gSongSwitch = null;
	private SwitchButton gBazingaSwitch = null;
	private ImageView gEightPuzzleImage = null;
	private TextView gBazingaScoreText = null;
	private TextView gBazingaBeginText = null;
	
	private HashMap<String, String> gChangeMap = null;
	
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_GAMETYPE)
			{
				if (msg.arg1 == ConstantValues.InstructionCode.GAME_NOT_SET)
				{
				}
				else if (msg.arg1 == ConstantValues.InstructionCode.GAME_TYPE_EIGHTPUZZLE)
				{
					gEightPuzzleSwitch.setChecked(true);
					gSongSwitch.setChecked(false); 
					gBazingaSwitch.setChecked(false);
				}
				else if (msg.arg1 == ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE)
				{
					gEightPuzzleSwitch.setChecked(false);
					gSongSwitch.setChecked(true);
					gBazingaSwitch.setChecked(false);
				}
				else if (msg.arg1 == ConstantValues.InstructionCode.GAME_TYPE_BAZINGABALL)
				{
					gEightPuzzleSwitch.setChecked(false);
					gSongSwitch.setChecked(false);
					gBazingaSwitch.setChecked(true);
				}
				gChangeMap.remove("game");
			}
			else if (msg.what == ConstantValues.InstructionCode.GAMESET_HANDLER_DOWNLOAD_IMAGE)
			{
				hasEightpuzzle = true;
				Bitmap bitmap = (Bitmap) msg.obj;
				gEightPuzzleImage.setImageBitmap(bitmap);
			}
			else if (msg.what == ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_MOLE)
			{
				String score = msg.obj.toString();
				if (!score.equals("0"))
				{
					gBazingaScoreText.setText(score);
					hasBazingball = true;
				}
			}
		};
	};
	
	public FragmentGameSetting()
	{
		super();
	}
	
	private void initGameData()
	{
		new ThreadGetGameType(ConstantValues.user.getID(), gHandler).start();
		new ThreadDownloadGameImage(ConstantValues.user.getID(), gHandler).start();
		new ThreadGetBazingaScore(ConstantValues.user.getID(), gHandler).start();
	}
	
	public FragmentGameSetting(Context context, HashMap<String, String> map) 
	{
		gContext = context;
		gChangeMap = map;
	}
	
	private OnClickListener gViewOnClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			if (id == R.id.lj_game_setting_eightpuzzle_image || id == R.id.lj_game_setting_eightpuzzle_image_text)
			{
				Intent intent = new Intent(gContext, SelectImageActivity.class);
				intent.putExtra(SelectImageActivity.FILTER_ENABLE, false);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_EIGHTPUZZLE);
			}
			else if (id == R.id.lj_game_setting_bazinga_begin)
			{
				if (!CommonUtil.isSdkVersionValid())
				{
					Toast.makeText(gContext, "此游戏类型暂时仅对Android 4.4及以上用户开放", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityBazingaBall.class);
				intent.putExtra("userID", ConstantValues.user.getID());  
				intent.putExtra("requestCode", ActivityBazingaBall.BAZINGABALL_REQUEST_CODE);  
				startActivityForResult(intent, ActivityBazingaBall.BAZINGABALL_REQUEST_CODE);
			}
		}
	};
	
	private OnCheckedChangeListener gSwitchOnCheckedChangeListener = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) 
		{
			int id = v.getId();
			if(isChecked)
			{
				if (id == R.id.lj_game_setting_eightpuzzle_switch)
				{
					if (hasEightpuzzle)
					{
						gSongSwitch.setChecked(false);
						gBazingaSwitch.setChecked(false);
						gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_EIGHTPUZZLE));
					}
					else
					{
						Toast.makeText(gContext, "您尚未选择图片", Toast.LENGTH_SHORT).show();
						v.setChecked(false);
					}
						
				}
				else if (id == R.id.lj_game_setting_song_switch)
				{
					gEightPuzzleSwitch.setChecked(false);
					gBazingaSwitch.setChecked(false);
					gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE));
				}
				else if (id == R.id.lj_game_setting_bazinga_switch)
				{
					if (hasBazingball)
					{
						gEightPuzzleSwitch.setChecked(false);
						gSongSwitch.setChecked(false);
						gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_BAZINGABALL));
					}
					else
					{
						Toast.makeText(gContext, "您需要完成一次游戏，初始化分数。", Toast.LENGTH_SHORT).show();
						v.setChecked(false);
					}
				}
			}
			else
			{
				if (gEightPuzzleSwitch.isChecked() || gBazingaSwitch.isChecked() || gSongSwitch.isChecked())
					return;
				v.setChecked(true);
				Toast.makeText(gContext, "您必须设定一个游戏", Toast.LENGTH_SHORT).show();
			//	gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE));
			}
		}
	};
	
	private OnClickListener gSwitchClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			
			if(((SwitchButton)v).isChecked())
			{
				if (id == R.id.lj_game_setting_eightpuzzle_switch)
				{
					if (hasEightpuzzle)
					{
						gSongSwitch.setChecked(false);
						gBazingaSwitch.setChecked(false);
						gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_EIGHTPUZZLE));
					}
					else
					{
						Toast.makeText(gContext, "您尚未选择图片", Toast.LENGTH_SHORT).show();
						((Switch)v).setChecked(false);
					}
						
				}
				else if (id == R.id.lj_game_setting_song_switch)
				{
					gEightPuzzleSwitch.setChecked(false);
					gBazingaSwitch.setChecked(false);
					gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE));
				}
				else if (id == R.id.lj_game_setting_bazinga_switch)
				{
					if (hasBazingball)
					{
						gEightPuzzleSwitch.setChecked(false);
						gSongSwitch.setChecked(false);
						gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_BAZINGABALL));
					}
					else
					{
						Toast.makeText(gContext, "您需要完成一次游戏，初始化分数。", Toast.LENGTH_SHORT).show();
						((Switch)v).setChecked(false);
					}
				}
			}
			else
			{
				gSongSwitch.setChecked(true);
				Toast.makeText(gContext, "已为您选择默认音乐游戏", Toast.LENGTH_LONG).show();
				gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE));
			}
		}
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		gView = inflater.inflate(R.layout.lj_fragment_game_setting, container, false);
        return gView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		gEightPuzzleSwitch = (SwitchButton)gView.findViewById(R.id.lj_game_setting_eightpuzzle_switch);
		gSongSwitch = (SwitchButton)gView.findViewById(R.id.lj_game_setting_song_switch);
		gBazingaSwitch = (SwitchButton)gView.findViewById(R.id.lj_game_setting_bazinga_switch);
		
		gEightPuzzleSwitch.setOnCheckedChangeListener(gSwitchOnCheckedChangeListener);
		gSongSwitch.setOnCheckedChangeListener(gSwitchOnCheckedChangeListener);
		gBazingaSwitch.setOnCheckedChangeListener(gSwitchOnCheckedChangeListener);
		
		gEightPuzzleImage = (ImageView)gView.findViewById(R.id.lj_game_setting_eightpuzzle_image);
		gEightPuzzleImage.setOnClickListener(gViewOnClickListener);
		
		TextView eightpuzzleText = (TextView)gView.findViewById(R.id.lj_game_setting_eightpuzzle_image_text);
		eightpuzzleText.setOnClickListener(gViewOnClickListener);
		
		gBazingaScoreText = (TextView)gView.findViewById(R.id.lj_game_setting_bazinga_score);
		
		gBazingaBeginText = (TextView)gView.findViewById(R.id.lj_game_setting_bazinga_begin);
		gBazingaBeginText.setOnClickListener(gViewOnClickListener);
		
		initGameData();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ActivityBazingaBall.BAZINGABALL_REQUEST_CODE && resultCode == ActivityBazingaBall.BAZINGABALL_RESULT_CODE)
		{
			int score = data.getIntExtra("score", 0);
			gBazingaScoreText.setText(String.valueOf(score));
			gChangeMap.put("bazinga", String.valueOf(score));
			hasBazingball = true;
		}
		else if (requestCode == ACTIVITY_REQUEST_CODE_EIGHTPUZZLE && resultCode == Activity.RESULT_OK)
		{
			String filePath = data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH);
			hasEightpuzzle = true;
			Bitmap bitmap = null;
			try 
			{
				bitmap = BitmapFactory.decodeFile(filePath);
				gEightPuzzleImage.setImageBitmap(bitmap);
				bitmap = null;
				gChangeMap.put("eightpuzzle", filePath);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
