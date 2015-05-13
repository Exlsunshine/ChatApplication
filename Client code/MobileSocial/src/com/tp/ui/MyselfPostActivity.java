
package com.tp.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.tp.adapter.MyselfPostAdpter;
import com.tp.messege.AbstractPost;
import com.tp.messege.EmptyPost;
import com.tp.views.ExtendedListView;
import com.tp.views.ExtendedListView.OnPositionChangedListener;
import com.tp.views.MenuRightAnimations;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MyselfPostActivity extends Activity implements OnTouchListener, OnPositionChangedListener 
{
    private ExtendedListView dataListView;

    // clock
    private FrameLayout clockLayout;
    private EmptyPost empty = new EmptyPost();
    private ArrayList<AbstractPost> ap = new ArrayList<AbstractPost>();
    private final int setAdpter = 1;
    private MyselfPostAdpter chatHistoryAdapter;
    private boolean isOncreate = false;
    
    
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.tp_myselfpostactivity_actionbar);
		
		TextView title = (TextView)findViewById(R.id.tp_myselfpostactivity_actionbar_title);
		title.setText("我的分享");
		LinearLayout back = (LinearLayout)findViewById(R.id.tp_myselftpostactivity_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_myselfpostactivity);
        MenuRightAnimations.initOffset(MyselfPostActivity.this);
        setupDialogActionBar();
        
        new Thread()
        {
        	public void run() 
        	{
        		try
        		{
        			ap.add(empty);
        			ArrayList<AbstractPost> posts = ConstantValues.user.pm.getMyselfPosts();
        			if (posts != null)
        				ap.addAll(posts);
          			Message message = Message.obtain();
    				message.what = setAdpter;
    				handler.sendMessage(message);
        		}
        		catch (Exception e)
                {
        			e.printStackTrace();
                }
        	};
        }.start();
        
        dataListView = (ExtendedListView) findViewById(R.id.myselfpostactivity_list_view);
        dataListView.setCacheColorHint(Color.TRANSPARENT);
        dataListView.setOnPositionChangedListener(this);
        clockLayout = (FrameLayout)findViewById(R.id.myselfpostactivity_clock);
        dataListView.setOnItemClickListener(new OnItemClickListenerImpl());
        isOncreate = true;
    }

    
    
    private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case setAdpter:
				chatHistoryAdapter = new MyselfPostAdpter(MyselfPostActivity.this, ap);
			    dataListView.setAdapter(chatHistoryAdapter);
			    dataListView.setOnItemClickListener(new OnItemClickListenerImpl());
				break;
			}
		}
	};

    @Override
    public boolean onTouch(View v, MotionEvent event) 
    {
        System.out.println("ontouch...................");
        return false;
    }

    private float[] computMinAndHour(int currentMinute, int currentHour) {
        float minuteRadian = 6f * currentMinute;
        float hourRadian = 360f / 12f * currentHour;
        float[] rtn = new float[2];
        rtn[0] = minuteRadian;
        rtn[1] = hourRadian;
        return rtn;
    }

    private float[] lastTime = {0f, 0f};

    private RotateAnimation[] computeAni(int min, int hour) 
    {
        RotateAnimation[] rtnAni = new RotateAnimation[2];
        float[] timef = computMinAndHour(min, hour);
        System.out.println("min===" + timef[0] + " hour===" + timef[1]);
        RotateAnimation ra = new RotateAnimation(lastTime[0], timef[0], Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true); 
        ra.setFillBefore(true);
        ra.setDuration(800);
        rtnAni[0] = ra;
        lastTime[0] = timef[0];
        RotateAnimation ra2 = new RotateAnimation(lastTime[1], timef[1], Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra2.setFillAfter(true);
        ra2.setFillBefore(true);
        ra2.setDuration(800);
        rtnAni[1] = ra2;
        lastTime[1] = timef[1];
        return rtnAni;
    }

    @Override
    public void onPositionChanged(ExtendedListView listView, int firstVisiblePosition,View scrollBarPanel) 
    {
    	TextView clocktext = (TextView) findViewById(R.id.clock_digital_time);
    	ImageView minView = (ImageView) findViewById(R.id.clock_face_minute);
    	ImageView hourView = (ImageView) findViewById(R.id.clock_face_hour);
        hourView.setImageResource(R.drawable.tp_clock_hour_rotatable);
        TextView datestr = ((TextView) findViewById(R.id.clock_digital_date));
    	if (firstVisiblePosition > ap.size() || firstVisiblePosition == ap.size() || firstVisiblePosition == 0)
    	{
    		datestr.setText("上午");
    		clocktext.setText("00:00");
    	    RotateAnimation[] tmp = computeAni(0,0);
    	    minView.startAnimation(tmp[0]);
    	    hourView.startAnimation(tmp[1]);
    		return;
    	}
        datestr.setText("上午");
        AbstractPost abpost = ap.get(firstVisiblePosition);
        String []date = abpost.getPostDate().split(" ");
        String []daytime = date[1].split(":");
        int hour = Integer.parseInt(daytime[0]);
        int min = Integer.parseInt(daytime[1]);
        Log.e("clock______", firstVisiblePosition + " " + hour + ":" + min);
        String tmpstr = "";
        if (hour > 12) 
        {
            hour = hour - 12;
            datestr.setText("下午");
            tmpstr += "";
        } 
        if (0 < hour && hour < 10) 
            tmpstr += "0";
        tmpstr += hour + ":";
        if (min < 10)
        	tmpstr += "0" + min;
        else
        	tmpstr += min;
        clocktext.setText(tmpstr);
        RotateAnimation[] tmp = computeAni(min,hour);
        minView.startAnimation(tmp[0]);
        hourView.startAnimation(tmp[1]);
    }

    @Override
    public void onScollPositionChanged(View scrollBarPanel,int top) 
    {
        System.out.println("onScollPositionChanged======================");
        MarginLayoutParams layoutParams = (MarginLayoutParams) clockLayout.getLayoutParams();
        System.out.println("left=="+layoutParams.leftMargin+" top=="+layoutParams.topMargin+" bottom=="+layoutParams.bottomMargin+" right=="+layoutParams.rightMargin);
        layoutParams.setMargins(0, top, 0, 0);
        clockLayout.setLayoutParams(layoutParams);
    }
    public class OnItemClickListenerImpl implements OnItemClickListener
   	{
   		@Override
   		public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
   		{
   			Log.d("OnItemClickListenerImpl", " 111111111");
   			if (position >= 1)
   			{
   				int postId = (int) chatHistoryAdapter.getItemId(position);
   				Log.d("OnItemClickListenerImpl", postId + " ");
   				Intent intent = new Intent(MyselfPostActivity.this, TextPostCommentListActivity.class);
   				Bundle bundle = new Bundle();
   				bundle.putInt("postid", postId);
   				intent.putExtras(bundle); 
   				startActivity(intent);
   			}
   		}
   	}
    
    @Override
	public void onResume()
	{
		if (isOncreate == false)
		{
			Thread td = new Thread(new Runnable()
	        {
				@Override
	        	public void run() 
	        	{
	        		try
	        		{
	        			Thread.sleep(50);
	        			Message message = Message.obtain();
	        			ap.clear();
	        			ap.add(empty);
	        			ap.addAll(ConstantValues.user.pm.getmypost());
	        			message.what = setAdpter;
	        			handler.sendMessage(message);
	        		}
	        		catch (Exception e)
	                {
	        			e.printStackTrace();
	                }
	        	}
	        });
			td.start();
			super.onResume();
		}
		else
		{
			isOncreate = false;
			super.onResume();
		}
	}
}
