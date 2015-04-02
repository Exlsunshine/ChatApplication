package com.lj.gameSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivitySongSetting extends Activity
{
	public static final String SET_STEP = "SetStep";
	public static final int SET_SINGER = 1;
	public static final int SET_SONG = 2;
	public static final int SET_LYRIC = 3;
	private ListView listview = null;
	private String[] adapterList;
	
	private Handler handle = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivitySongSetting.this, android.R.layout.simple_list_item_1, adapterList);
			listview.setAdapter(adapter);
		};
	};
	
	OnItemClickListener itemClickListener = new OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
		{
			String select = adapterList[arg2];
			Intent intent = new Intent();
			intent.putExtra("result", select);
			ActivitySongSetting.this.setResult(RESULT_OK, intent);
			ActivitySongSetting.this.finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		listview = new ListView(this);
		listview.setOnItemClickListener(itemClickListener);
		setContentView(listview);
		
		initListView();
	}
	
	private void initListView()
	{
		Bundle bundle = getIntent().getExtras();
		int currentStep = bundle.getInt(SET_STEP);
		switch (currentStep) 
		{
		case SET_SINGER:	
			new Thread()
			{
				public void run() 
				{
					adapterList = SongPuzzleSetting.getSingerList();
					handle.sendMessage(new Message());
				};
			}.start();
			break;
		case SET_SONG:
			final String singer = bundle.getString("singer");
			new Thread()
			{
				public void run() 
				{
					adapterList = SongPuzzleSetting.getSongList(singer);
					handle.sendMessage(new Message());
				};
			}.start();
			break;
	case SET_LYRIC:
			final String singer2 = bundle.getString("singer");
			final String song =  bundle.getString("song");
			new Thread()
			{
				public void run() 
				{
					adapterList = SongPuzzleSetting.getLyricList(singer2, song);
					handle.sendMessage(new Message());
				};
			}.start();
			break;
		}
	}
}
