package com.lj.driftbottle.ui;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.BottleManager;
import com.lj.driftbottle.logic.CommBottle;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBottle extends Activity 
{
	private ListView listView;
	private final int GET_MY_BOTTLE_HANDLER = 0x01;
	private final int REMOVE_BOTTLE_HANDLER = 0x02;
	public static HashMap<String, Bitmap> portraitTable = new HashMap<String, Bitmap>();
	private ArrayList<CommBottle> myBottleList = null;
	private MyBottlesListAdapter adapter = null;
	private BottleManager bottleManager = null;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if (msg.what == GET_MY_BOTTLE_HANDLER)
			{
				if (myBottleList.size() == 0)
				{
					View view = findViewById(R.id.lj_my_bottle_remind);
					view.setVisibility(View.VISIBLE);
					view.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View arg0) 
						{
							setResult(RESULT_OK);
							finish();
						}
					});
				}
				else
				{
					adapter = new MyBottlesListAdapter(MyBottle.this, myBottleList);
					listView.setAdapter(adapter);
				}
			}
			else if (msg.what == REMOVE_BOTTLE_HANDLER)
			{
				myBottleList.remove(msg.arg1);
				if (myBottleList.size() == 0)
					findViewById(R.id.lj_my_bottle_remind).setVisibility(View.VISIBLE);
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
		setContentView(R.layout.my_bottle);
		findViewById(R.id.lj_my_bottle_remind).setVisibility(View.GONE);
		bottleManager = BottleManager.getInstance();
		listView = (ListView)findViewById(R.id.my_bottle_list);
		listView.setOnItemLongClickListener(new ListViewItemLongClick());
		listView.setOnItemClickListener(new ListViewItemClick());
		new Thread(new GetDataRun()).start();
		setupDialogActionBar();
	}
	
	private class GetDataRun implements Runnable
	{
		@Override
		public void run() 
		{
			myBottleList = bottleManager.myBottles();
			Message msg = new Message();
			msg.what = GET_MY_BOTTLE_HANDLER;
			msg.obj = myBottleList;
			handler.sendMessage(msg);
		}
		
	}
	
	private class ListViewItemLongClick implements OnItemLongClickListener
	{
		private AlertDialog delteDialog = null;
		
		private void showDeleteBottleDialog(final int bottleID, final int position)
		{
			delteDialog = new AlertDialog.Builder(MyBottle.this, R.style.LoginDialogAnimation).create();
			delteDialog.setCanceledOnTouchOutside(true);
			delteDialog.show();
			delteDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			delteDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			delteDialog.setCancelable(true);
			Window window = delteDialog.getWindow();
			window.setContentView(R.layout.lj_mybottle_delete_dialog);
			Button cancel = (Button) window.findViewById(R.id.lj_my_bottle_delete_dialog_button_cancel);
			Button confirm = (Button) window.findViewById(R.id.lj_my_bottle_delete__dialog_button_confirm);
			cancel.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					delteDialog.dismiss();
				}
			});
			confirm.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							bottleManager.removeBottle(bottleID);
							Message msg = new Message();
							msg.what = REMOVE_BOTTLE_HANDLER;
							msg.arg1 = position;
							handler.sendMessage(msg);
						}
					}).start();
					delteDialog.dismiss();
				}
			});
		}
		
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position, long id) 
		{
			final int bottleID = myBottleList.get(position).getBottleID();
			showDeleteBottleDialog(bottleID, position);
			return true;
		}
		
	}
	
	private class ListViewItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) 
		{
			int bottleID =myBottleList.get(position).getBottleID();
			Intent intent = new Intent();
			intent.putExtra("bottleID", bottleID);
			intent.setClass(getApplicationContext(), BottleInfoActivity.class);
			startActivityForResult(intent, 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0 && resultCode == RESULT_OK)
			adapter.notifyDataSetChanged();
	}

}
