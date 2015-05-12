package com.lj.setting.game;


import java.io.File;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.lj.bazingaball.ActivityBazingaBall;
import com.lj.eightpuzzle.ThreadDownloadGameImage;
import com.yg.commons.ConstantValues;
import com.yg.image.select.ui.SelectImageActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentGameSetting extends Fragment
{
	private static final int ACTIVITY_RESULT_CODE_EIGHTPUZZLE = 1;
	private static final int ACTIVITY_REQUEST_CODE_EIGHTPUZZLE = 1;
	private Context gContext;
	private View gView;
	private Switch gEightPuzzleSwitch = null;
	private Switch gSongSwitch = null;
	private Switch gBazingaSwitch = null;
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
			}
			else if (msg.what == ConstantValues.InstructionCode.GAMESET_HANDLER_DOWNLOAD_IMAGE)
			{
				Bitmap bitmap = (Bitmap) msg.obj;
				gEightPuzzleImage.setImageBitmap(bitmap);
			}
			else if (msg.what == ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_MOLE)
			{
				String score = msg.obj.toString();
				gBazingaScoreText.setText(score);
			}
		};
	};
	
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
	
	private OnTouchListener gViewOnTouchListener = new OnTouchListener() 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			int id = v.getId();
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				if (id == R.id.lj_game_setting_eightpuzzle_image)
				{
					Intent intent = new Intent(gContext, SelectImageActivity.class);
					intent.putExtra(SelectImageActivity.FILTER_ENABLE, false);
					startActivityForResult(intent, ACTIVITY_REQUEST_CODE_EIGHTPUZZLE);
				}
				else if (id == R.id.lj_game_setting_bazinga_begin)
				{
					Intent intent = new Intent();
					intent.setClass(getActivity(), ActivityBazingaBall.class);
					intent.putExtra("userID", ConstantValues.user.getID());  
					intent.putExtra("requestCode", ActivityBazingaBall.BAZINGABALL_REQUEST_CODE);  
					startActivityForResult(intent, ActivityBazingaBall.BAZINGABALL_REQUEST_CODE);
				}
			}
			return true;
		}
	};
	
	private OnClickListener gSwitchClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			
			if(((Switch)v).isChecked())
			{
				if (id == R.id.lj_game_setting_eightpuzzle_switch)
				{
					gSongSwitch.setChecked(false);
					gBazingaSwitch.setChecked(false);
					gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_EIGHTPUZZLE));
				}
				else if (id == R.id.lj_game_setting_song_switch)
				{
					gEightPuzzleSwitch.setChecked(false);
					gBazingaSwitch.setChecked(false);
					gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_SONGPUZZLE));
				}
				else if (id == R.id.lj_game_setting_bazinga_switch)
				{
					gEightPuzzleSwitch.setChecked(false);
					gSongSwitch.setChecked(false);
					gChangeMap.put("game", String.valueOf(ConstantValues.InstructionCode.GAME_TYPE_BAZINGABALL));
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
		
		gEightPuzzleSwitch = (Switch)gView.findViewById(R.id.lj_game_setting_eightpuzzle_switch);
		gSongSwitch = (Switch)gView.findViewById(R.id.lj_game_setting_song_switch);
		gBazingaSwitch = (Switch)gView.findViewById(R.id.lj_game_setting_bazinga_switch);
		
		gEightPuzzleSwitch.setOnClickListener(gSwitchClickListener);
		gSongSwitch.setOnClickListener(gSwitchClickListener);
		gBazingaSwitch.setOnClickListener(gSwitchClickListener);
		
		gEightPuzzleImage = (ImageView)gView.findViewById(R.id.lj_game_setting_eightpuzzle_image);
		gEightPuzzleImage.setOnTouchListener(gViewOnTouchListener);
		
		gBazingaScoreText = (TextView)gView.findViewById(R.id.lj_game_setting_bazinga_score);
		
		gBazingaBeginText = (TextView)gView.findViewById(R.id.lj_game_setting_bazinga_begin);
		gBazingaBeginText.setOnTouchListener(gViewOnTouchListener);
		
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
		}
		else if (requestCode == ACTIVITY_REQUEST_CODE_EIGHTPUZZLE && resultCode == Activity.RESULT_OK)
		{
			String filePath = data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH);
			
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
