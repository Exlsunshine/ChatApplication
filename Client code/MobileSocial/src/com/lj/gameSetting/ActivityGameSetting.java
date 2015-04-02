package com.lj.gameSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.user.WebServiceAPI;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGameSetting extends Activity
{
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.GAMESET_IMAGE_NAME));
	
	private final int[] SONG_PUZZLE_BTNS_ID_1 = {R.id.lj_singerBtn_1, R.id.lj_songBtn_1, R.id.lj_lyricBtn_1};
	private final int[] SONG_PUZZLE_BTNS_ID_2 = {R.id.lj_singerBtn_2, R.id.lj_songBtn_2, R.id.lj_lyricBtn_2};
	
	private Button[] songBtn1;
	private Button[] songBtn2;
	private Switch switch1;
	private Switch switch2;
	private TextView scoreText;
	private Button currentBtn;
	private Button lockBtn;
	
	private Handler handle = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
			case ConstantValues.InstructionCode.ERROR_NETWORK:
				Toast.makeText(ActivityGameSetting.this, "net work error", Toast.LENGTH_LONG).show();
				break;
			case ConstantValues.InstructionCode.HANDLER_WAIT_FOR_DATA:
				Toast.makeText(ActivityGameSetting.this, "wait", Toast.LENGTH_LONG).show();
				break;
			case ConstantValues.InstructionCode.SUCCESS:
				Toast.makeText(ActivityGameSetting.this, "success", Toast.LENGTH_LONG).show();
				break;
			case ConstantValues.InstructionCode.GAMESET_HANDLER_DOWNLOAD_IMAGE:
				Bitmap selectBitmap = (Bitmap) msg.obj;
				((ImageView)findViewById(R.id.lj_imageView)).setImageBitmap(selectBitmap);
				selectBitmap = null;
				break;
			case ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_SONG:
				ArrayList<HashMap<String, Object>> songSetting = (ArrayList<HashMap<String, Object>>) msg.obj;
				HashMap<String, Object> set1 = songSetting.get(0);
				songBtn1[0].setText(set1.get(ConstantValues.InstructionCode.GAMESET_KEY_SINGER).toString());
				songBtn1[1].setText(set1.get(ConstantValues.InstructionCode.GAMESET_KEY_SONG).toString());
				songBtn1[2].setText(set1.get(ConstantValues.InstructionCode.GAMESET_KEY_LYRIC).toString());
				if (set1.get(ConstantValues.InstructionCode.GAMESET_KEY_ANSWERTYPE).toString().equals(ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SONG))
					switch1.setChecked(true);
				else
					switch1.setChecked(false);
				
				HashMap<String, Object> set2 = songSetting.get(1);
				songBtn2[0].setText(set2.get(ConstantValues.InstructionCode.GAMESET_KEY_SINGER).toString());
				songBtn2[1].setText(set2.get(ConstantValues.InstructionCode.GAMESET_KEY_SONG).toString());
				songBtn2[2].setText(set2.get(ConstantValues.InstructionCode.GAMESET_KEY_LYRIC).toString());
				if (set2.get(ConstantValues.InstructionCode.GAMESET_KEY_ANSWERTYPE).toString().equals(ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SONG))
					switch2.setChecked(true);
				else
					switch2.setChecked(false);
				if (songBtn1[0].getText().toString() == ConstantValues.InstructionCode.GAMESET_DEFAULT_SINGER)
				{
					songBtn1[1].setEnabled(false);
					songBtn1[2].setEnabled(false);
					songBtn2[1].setEnabled(false);
					songBtn2[2].setEnabled(false);
				}
				break;
			case ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_MOLE:
				String result = msg.obj.toString();
				scoreText.setText(result.toString());
				break;
			}
		};
	};
	
	OnClickListener imageSelectListener = new OnClickListener() 
	{
		@Override
		public void onClick(View arg0) 
		{
			new AlertDialog.Builder(ActivityGameSetting.this).setTitle("请选择")
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
	};
	
	OnClickListener songPuzzleBtnListener = new OnClickListener() 
	{
		private void startSongSelectActivity(View view, Bundle bundle)
		{
			currentBtn = (Button)view;
			int id = view.getId();
			for (int i = 0; i < SONG_PUZZLE_BTNS_ID_1.length; i++)
			{
				if (id == SONG_PUZZLE_BTNS_ID_1[i])
				{
					lockBtn = songBtn1[2];
					break;
				}
				else if (id == SONG_PUZZLE_BTNS_ID_2[i])
				{
					lockBtn = songBtn2[2];
					break;
				}
			}
			Intent intent = new Intent();
			intent.setClass(ActivityGameSetting.this, ActivitySongSetting.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, ConstantValues.InstructionCode.GAMESET_REQUESTCODE_SONG);
		}
		
		private Bundle initBundleWithViewId(int viewId)
		{
			Bundle bundle = new Bundle();
			switch (viewId)
			{
			case R.id.lj_singerBtn_1:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_SINGER);
				break;
			case R.id.lj_singerBtn_2:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_SINGER);
				break;
			case R.id.lj_songBtn_1:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_SONG);
				bundle.putString("singer", songBtn1[0].getText().toString());
				break;
			case R.id.lj_songBtn_2:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_SONG);
				bundle.putString("singer", songBtn2[0].getText().toString());
				break;
			case R.id.lj_lyricBtn_1:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_LYRIC);
				bundle.putString("singer", songBtn1[0].getText().toString());
				bundle.putString("song", songBtn1[1].getText().toString());
				break;
			case R.id.lj_lyricBtn_2:
				bundle.putInt(ActivitySongSetting.SET_STEP, ActivitySongSetting.SET_LYRIC);
				bundle.putString("singer", songBtn2[0].getText().toString());
				bundle.putString("song", songBtn2[1].getText().toString());
				break;
			}
			return bundle;
		}
		
		@Override
		public void onClick(View view) 
		{
			int viewId = view.getId();
			Bundle bundle = initBundleWithViewId(viewId);
			startSongSelectActivity(view, bundle);	
		}
	};
	
	private void initEightPuzzleSetting()
	{
		ThreadDownloadGameImage thread = new ThreadDownloadGameImage(ConstantValues.user.getID(), handle);
		thread.start();
	}
	
	private void initSongGameSetting()
	{
		ThreadInitSongSetting thread = new ThreadInitSongSetting(ConstantValues.user.getID(), handle);
		thread.start();
	}
	
	private void initMoleScore()
	{
		final WebServiceAPI webserviceMole = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME_SETTING, "GameMole");
		new Thread()
		{
			@Override
			public void run() 
			{
				super.run();
				String[] name = {"userID"};
				Object[] values = {ConstantValues.user.getID()};
				Object result = webserviceMole.callFuntion("getMoleScore", name, values);
				Message msg = new Message();
				msg.what = ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_MOLE;
				msg.obj = result;
				handle.sendMessage(msg);
			}
		}.start();
	}
	private void initUserSetting()
	{
		initEightPuzzleSetting();
		initSongGameSetting();
		initMoleScore();
	}
	
	private void initBtn()
	{
		songBtn1 = new Button[SONG_PUZZLE_BTNS_ID_1.length];
		songBtn2 = new Button[SONG_PUZZLE_BTNS_ID_2.length];
		for (int i = 0; i < SONG_PUZZLE_BTNS_ID_1.length; i++)
		{
			songBtn1[i] = (Button)findViewById(SONG_PUZZLE_BTNS_ID_1[i]);
			songBtn1[i].setOnClickListener(songPuzzleBtnListener);
			songBtn2[i] = (Button)findViewById(SONG_PUZZLE_BTNS_ID_2[i]);
			songBtn2[i].setOnClickListener(songPuzzleBtnListener);
		}
		
		Button imageSelectBtn = (Button)findViewById(R.id.lj_selectImageBtn);
		imageSelectBtn.setOnClickListener(imageSelectListener);
		
		switch1 = (Switch)findViewById(R.id.lj_flagswitch_1);
		switch2 = (Switch)findViewById(R.id.lj_flagswitch_2);
		
		scoreText = (TextView)findViewById(R.id.lj_score);
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
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_layout_game_set);
		initBtn();
		initUserSetting();
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == ConstantValues.InstructionCode.GAMESET_REQUESTCODE_SONG && resultCode == RESULT_OK)
		{
			String result = data.getStringExtra("result");
			currentBtn.setText(result);
			if (songBtn1[0].getText().toString() != ConstantValues.InstructionCode.GAMESET_DEFAULT_SINGER)
				songBtn1[1].setEnabled(true);
			if (songBtn2[0].getText().toString() != ConstantValues.InstructionCode.GAMESET_DEFAULT_SINGER)
				songBtn2[1].setEnabled(true);
			lockBtn.setEnabled(true);
			if (currentBtn.getId() == R.id.lj_singerBtn_1)
			{
				lockBtn.setEnabled(false);
				songBtn1[1].setText("歌手");
				songBtn1[2].setText("歌词");
			}
			else if (currentBtn.getId() == R.id.lj_singerBtn_2)
			{
				lockBtn.setEnabled(false);
				songBtn2[1].setText("歌手");
				songBtn2[2].setText("歌词");
			}
			else if (currentBtn.getId() == R.id.lj_songBtn_1)
				songBtn1[2].setText("歌词");
			else if (currentBtn.getId() == R.id.lj_songBtn_2)
				songBtn2[2].setText("歌词");
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY && resultCode == RESULT_OK)
		{
			Uri selectedImage =  data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			File temp = new File(picturePath);
			startPhotoZoom(Uri.fromFile(temp)); 
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CAMERA && resultCode == RESULT_OK)
		{
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.GAMESET_IMAGE_NAME);  
            startPhotoZoom(Uri.fromFile(temp));  
			
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CROP && resultCode == RESULT_OK)
		{
			Bitmap bitmap = null;
			try 
			{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			ImageView imageView = (ImageView)findViewById(R.id.lj_imageView);
			imageView.setImageBitmap(bitmap);
			bitmap = null;
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.menu_game_save, menu);
        return true;
    }
	
	private boolean checkSongSetting()
	{
		String[] temp = {"歌手", "歌名", "歌词"};
		for (int i = 0; i < songBtn1.length; i++)
		{
			if (songBtn1[i].getText().toString().equals(temp[i]) || songBtn2[i].getText().toString().equals(temp[i]))
				return false;
		}
		return true;
	}
	
	private ArrayList<HashMap<String, Object>> getSongSettingList()
	{
		ArrayList<HashMap<String, Object>> map = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("singer", songBtn1[0].getText().toString());
		item.put("song", songBtn1[1].getText().toString());
		item.put("lyric", songBtn1[2].getText().toString());
		if (switch1.isChecked())
			item.put("puzzle_answer", ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SONG);
		else
			item.put("puzzle_answer", ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SINGER);
		
		map.add(item);
		item = new HashMap<String, Object>();
		item.put("singer", songBtn2[0].getText().toString());
		item.put("song", songBtn2[1].getText().toString());
		item.put("lyric", songBtn2[2].getText().toString());
		if (switch2.isChecked())
			item.put("puzzle_answer", ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SONG);
		else
			item.put("puzzle_answer", ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SINGER);
		
		map.add(item);
		return map;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        int id = item.getItemId();
        switch (id)
        {
        case R.id.eightpuzzle:
        	Bitmap bitmap = null;
			try 
			{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);
			} catch (Exception e) 
			{
				e.printStackTrace();
				Toast.makeText(this, "没有图片", Toast.LENGTH_LONG).show();
			}
        	ThreadUploadGameImage thread = new ThreadUploadGameImage(ConstantValues.user.getID(), bitmap, handle);
        	thread.start();
        	break;
        case R.id.song:
        	if (!checkSongSetting())
        	{
        		Toast.makeText(this, "请选择", Toast.LENGTH_LONG).show();
        		return false;
        	}
        	ArrayList<HashMap<String, Object>> map = getSongSettingList();
        	ThreadSetSongSetting setSongThread = new ThreadSetSongSetting(ConstantValues.user.getID(), handle, map);
        	setSongThread.start();
        	break;
        case R.id.hit:
        	int score = Integer.valueOf(scoreText.getText().toString());
        	if (score == 0)
        	{
        		Toast.makeText(this, "0分！！！", Toast.LENGTH_LONG).show();
        		return false;
        	}
        	ThreadUploadScore threadScore = new ThreadUploadScore(handle, ConstantValues.user.getID(), score);
        	threadScore.start();
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
}
