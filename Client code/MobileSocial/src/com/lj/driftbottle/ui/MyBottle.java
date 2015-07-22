package com.lj.driftbottle.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.CommBottle;
import com.lj.setting.achievement.ThreadGameChallengFail;
import com.yg.commons.ConstantValues;
import com.yg.user.DownloadManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class MyBottle extends Activity {
	
	private Button back;
	private ListView listView;
	private int[] bottleIDArray;
	private final int GET_MY_BOTTLE_HANDLER = 0x01;
	private final int REMOVE_BOTTLE_HANDLER = 0x02;
	private final int LAST_TEXT_LENGTH = 10;
	public static HashMap<String, Bitmap> portraitTable = new HashMap<String, Bitmap>();
	private ArrayList<CommBottle> myBottleList = null;
	private List<Map<String, Object>> adapterData = null;
	private SimpleAdapter adapter = null;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if (msg.what == GET_MY_BOTTLE_HANDLER)
			{
				String[] from = {"img", "text", "bottle_status"};
				int[] to = {R.id.lj_bottle_list_img, R.id.lj_bottle_list_text, R.id.lj_bottle_list_status};
				adapterData = (List<Map<String, Object>>) msg.obj;
				adapter = new SimpleAdapter(getApplicationContext(), adapterData, R.layout.lj_my_bottles_list_item
						, from, to);
				adapter.setViewBinder(new MyViewBinder());
				listView.setAdapter(adapter);
			}
			else if (msg.what == REMOVE_BOTTLE_HANDLER)
			{
				adapterData.remove(msg.arg1);
				adapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "删除漂流瓶", Toast.LENGTH_SHORT).show();
			}
		};
	};

	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleTextView = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleTextView.setText("我的漂流瓶");
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_bottle);
		
	//	back=(Button) findViewById(R.id.my_bottle_back);
	//	back.setOnClickListener(listener);
		
		listView = (ListView)findViewById(R.id.my_bottle_list);
		listView.setOnItemLongClickListener(new ListViewItemLongClick());
		listView.setOnItemClickListener(new ListViewItemClick());
		new Thread(new GetDataRun()).start();
		setupDialogActionBar();
	}
	
	
	
	private class GetDataRun implements Runnable
	{
		private List<Map<String, Object>> getData(ArrayList<CommBottle> bottleList)
		{
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			bottleIDArray = new int[bottleList.size()];
			for (int i = 0; i < bottleList.size(); i++)
			{
				Map<String, Object> map = new Hashtable<String, Object>();
				CommBottle bottle = bottleList.get(i);
				bottleIDArray[i] = bottle.getBottleID();
				
				String text = bottle.getLastText();
				if (text.length() >= LAST_TEXT_LENGTH)
					text = text.substring(0, LAST_TEXT_LENGTH) + "......";
				map.put("text", bottle.getBottleID() + " " + bottle.getNickname() + " " + bottle.getSex() + " " + bottle.getHometown());
				
				String url = bottle.getPortraitURL();
				Bitmap bitmap = portraitTable.get(url);
				if (bitmap == null)
				{
					bitmap = new DownloadManager(url).getBmpFile();
					portraitTable.put(url, bitmap);
				}
				map.put("img", bitmap);
				
				int bottleStatus = bottle.getBottleStatus();
				int bottleRelationStatus = bottle.getBottleRelationStatus();
				if (bottleStatus == CommBottle.BOTTLE_STATUS_PICKED && bottleRelationStatus == CommBottle.BOTTLE_RELATION_STATUS_NORMAL)
					map.put("bottle_status", "瓶子\n已被\n捡到");
				else if (bottleStatus == CommBottle.BOTTLE_STATUS_PICKED && bottleRelationStatus == CommBottle.BOTTLE_RELATION_STATUS_DELETE)
					map.put("bottle_status", "瓶子\n已被\n对方\n删除");
				else if (bottleStatus == CommBottle.BOTTLE_STATUS_UNPICKED && bottleRelationStatus == CommBottle.BOTTLE_RELATION_STATUS_NORMAL)
					map.put("bottle_status", "瓶子\n正在\n漂流");
				
				list.add(map);
			}
			return list;
		}
		
		@Override
		public void run() 
		{
			myBottleList = DriftBottleActivity.bottleManager.myBottles();
			List<Map<String, Object>> result = getData(myBottleList);
			Message msg = new Message();
			msg.what = GET_MY_BOTTLE_HANDLER;
			msg.obj = result;
			handler.sendMessage(msg);
		}
		
	}
	
	private class MyViewBinder implements ViewBinder
	{
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation)
		{
			if (data instanceof Bitmap && view instanceof ImageView)
			{
				ImageView imageView = (ImageView) view;  
                Bitmap bmp = (Bitmap) data;  
                imageView.setImageBitmap(bmp); 
                return true;
			}
			return false;
		}
		
	}
	
	private class ListViewItemLongClick implements OnItemLongClickListener
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position, long id) 
		{
			final int bottleID = bottleIDArray[position];
			new AlertDialog.Builder(MyBottle.this) 
              .setTitle("删除漂流瓶") 
              .setPositiveButton("确定", new DialogInterface.OnClickListener() 
              {
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							DriftBottleActivity.bottleManager.removeBottle(bottleID);
							Message msg = new Message();
							msg.what = REMOVE_BOTTLE_HANDLER;
							msg.arg1 = position;
							handler.sendMessage(msg);
						}
					}).start();
				}
			}).show();
			return true;
		}
		
	}
	
	private class ListViewItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) 
		{
			int bottleID = bottleIDArray[position];
			Intent intent = new Intent();
			intent.putExtra("bottleID", bottleID);
			intent.setClass(getApplicationContext(), BottleInfoActivity.class);
			startActivity(intent);
		}
	}

}
