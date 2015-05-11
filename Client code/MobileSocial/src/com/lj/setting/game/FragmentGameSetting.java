package com.lj.setting.game;


import java.io.File;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.lj.bazingaball.ActivityBazingaBall;
import com.lj.eightpuzzle.ThreadDownloadGameImage;
import com.yg.commons.ConstantValues;

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
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.GAMESET_IMAGE_NAME));
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
					new AlertDialog.Builder(gContext).setTitle("请选择")
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
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"EightPuzzleGame.jpg")));  
								startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CAMERA);  
							}
							dialog.dismiss();  
						}
					}).setNegativeButton("取消", null).show();
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
    }  
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY && resultCode == Activity.RESULT_OK)
		{
			Uri selectedImage =  data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = gContext.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			File temp = new File(picturePath);
			startPhotoZoom(Uri.fromFile(temp)); 
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK)
		{
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.GAMESET_IMAGE_NAME);  
            startPhotoZoom(Uri.fromFile(temp));  
			
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CROP && resultCode == Activity.RESULT_OK)
		{
			Bitmap bitmap = null;
			try 
			{
				bitmap = MediaStore.Images.Media.getBitmap(gContext.getContentResolver(), IMAGE_URI);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			gEightPuzzleImage.setImageBitmap(bitmap);
			bitmap = null;
			gChangeMap.put("eightpuzzle", Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.GAMESET_IMAGE_NAME);
		}
		else if (requestCode == ActivityBazingaBall.BAZINGABALL_REQUEST_CODE && resultCode == ActivityBazingaBall.BAZINGABALL_RESULT_CODE)
		{
			int score = data.getIntExtra("score", 0);
			gBazingaScoreText.setText(String.valueOf(score));
			gChangeMap.put("bazinga", String.valueOf(score));
		}
	}
}
