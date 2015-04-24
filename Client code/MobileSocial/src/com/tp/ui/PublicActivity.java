
package com.tp.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.tp.adapter.PublicActivityAdapter;
import com.tp.adapter.PullToRefreshBase.OnRefreshListener;
import com.tp.adapter.PullToRefreshListView;
import com.tp.adapter.PullToRefreshListView.OnPositionChangedListener;
import com.tp.messege.AbstractPost;
import com.tp.messege.EmptyPost;
import com.tp.messege.PostManager;
import com.tp.views.MenuRightAnimations;
import com.yg.commons.ConstantValues;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class PublicActivity extends Activity implements OnTouchListener, OnPositionChangedListener 
{
    
    // clock
    private FrameLayout clockLayout;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private EmptyPost empty = new EmptyPost();
    private PublicActivityAdapter chatHistoryAdapter;
    private ArrayList<AbstractPost> ap = new ArrayList<AbstractPost>();
    private final int setAdpter = 1;
    private final int pulluprefresh = 2;
    private final int pulluprefreshempty = 3;
    private final int refresh = 4;
    private int position1 = 0;
    private final int textBtn = 2131034131;
    private boolean isOncreate = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_feed_activity2);
        MenuRightAnimations.initOffset(PublicActivity.this);
        new Thread()
        {
        	public void run() 
        	{
        		try
        		{
        			ap.add(empty);
          			ap.addAll(ConstantValues.user.pm.get10Posts());
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
        
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.publicactivity_pulltorefreshlist_view);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(new OnItemClickListenerImpl());
        mPullRefreshListView.setOnPositionChangedListener(this);
        setClickListener();
        clockLayout = (FrameLayout)findViewById(R.id.publicactivity_clock);
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
				chatHistoryAdapter = new PublicActivityAdapter(PublicActivity.this, ap);
				mListView.setAdapter(chatHistoryAdapter);
				mListView.setSelection(position1);
				Log.d("handleMessage", position1 + "");
				mListView.setOnItemClickListener(new OnItemClickListenerImpl());
				mPullRefreshListView.onRefreshComplete();
				break;
			case pulluprefresh:
				chatHistoryAdapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
				break;
			case pulluprefreshempty:
				mPullRefreshListView.onRefreshComplete();
				break;
			case refresh:
				chatHistoryAdapter.notifyDataSetChanged();
				break;
			}
		}
	};

	 @Override
	    public boolean onTouch(View v, MotionEvent event) 
	    {
		 	Log.d("on touch", " 111111111");
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
        RotateAnimation ra = new RotateAnimation(lastTime[0], timef[0], Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
    
    public class OnItemClickListenerImpl implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
		{
			Log.d("OnItemClickListenerImpl", " 111111111");
			if (position == 1)
			{
				Intent intent = new Intent(PublicActivity.this,MyselfPostActivity.class);
        		startActivity(intent);
        		Log.d("userportraitIV", "111111");
			}
			if (position > 1)
			{
				int postId = (int) chatHistoryAdapter.getItemId(position - 1);
				Log.d("OnItemClickListenerImpl", postId + " ");
				Intent intent = new Intent(PublicActivity.this, TextPostCommentListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("postid", postId);
				intent.putExtras(bundle); 
				startActivity(intent);
			}
		}
	}
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) 
    {
    	if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) 
    	{
    		Log.e("onScrollStateChanged", mListView.getFirstVisiblePosition() + "");
    		position1 = mListView.getFirstVisiblePosition();
    	}
    }
    
    @Override
    public void onPositionChanged(PullToRefreshListView listView, int firstVisiblePosition, View scrollBarPanel) 
    {
    	TextView clocktext = (TextView) findViewById(R.id.clock_digital_time);
    	ImageView minView = (ImageView) findViewById(R.id.clock_face_minute);
    	ImageView hourView = (ImageView) findViewById(R.id.clock_face_hour);
        hourView.setImageResource(R.drawable.tp_clock_hour_rotatable);
        TextView datestr = ((TextView) findViewById(R.id.clock_digital_date));
    	if (firstVisiblePosition > ap.size() || firstVisiblePosition == ap.size())
    	{
    		/*datestr.setText("上午");
    		clocktext.setText("00:00");*/
    	    RotateAnimation[] tmp = computeAni(0,0);
    	    minView.startAnimation(tmp[0]);
    	    hourView.startAnimation(tmp[1]);
    		return;
    	}
        System.out.println("layout=======padding top========"+scrollBarPanel.getPaddingTop());
        
        datestr.setText("上午");
        AbstractPost abpost = ap.get(firstVisiblePosition);
        Log.e("clock______", abpost.getPostDate());
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
    
    OnRefreshListener mOnrefreshListener = new OnRefreshListener() 
    {
		public void onRefresh() 
		{
			new GetDataTask(mPullRefreshListView.getRefreshType()).execute();
		}
	};
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> 
	{
		int pullState;
		public GetDataTask(int pullType) 
		{
			this.pullState = pullType;
		}
		
		@Override
		protected String[] doInBackground(Void... params) {
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if(pullState == 1) 
			{
				Log.e("onPostExecute", "下拉");
				Thread td = new Thread(new Runnable()
		        {
					@Override
		        	public void run() 
		        	{
		        		try
		        		{
		        			ArrayList<AbstractPost> tmp = ConstantValues.user.pm.getLatestPosts();
		        			if (tmp == null)
		        			{
		        				Message message = Message.obtain();
			    				message.what = pulluprefreshempty;
			    				handler.sendMessage(message);
		        			}
		        			else
		        			{
		        				ap.clear();
			        			ap.add(empty);
			          			ap.addAll(tmp);
			          			Message message = Message.obtain();
			    				message.what = setAdpter;
			    				handler.sendMessage(message);
		        			}
		        		}
		        		catch (Exception e)
		                {
		        			e.printStackTrace();
		                }
		        	}
		        });
				td.start();
				try 
				{
					td.join();
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			if(pullState == 2) 
			{
				//上拉
				Log.e("onPostExecute", "上拉");
				Thread td = new Thread(new Runnable()
		        {
					@Override
		        	public void run() 
		        	{
		        		try
		        		{
		        			Log.d("getHistoryPostssdebug_______", "______");
		        			ArrayList<AbstractPost> tmp = ConstantValues.user.pm.getHistoryPosts();
		        			if (tmp == null)
		        			{
		        				Message message = Message.obtain();
			    				message.what = pulluprefreshempty;
			    				handler.sendMessage(message);
		        			}
		        			else
		        			{
		        				ap.clear();
			        			ap.add(empty);
		        				ap.addAll(tmp);
			          			Message message = Message.obtain();
			    				message.what = pulluprefresh;
			    				handler.sendMessage(message);
		        			}
		        		}
		        		catch (Exception e)
		                {
		        			e.printStackTrace();
		                }
		        	}
		        });
				td.start();
			}
			super.onPostExecute(result);
		}
		private String[] mStrings = { "Abbaye de Belloc" };
	}
	
	private void setClickListener() 
	{
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
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
	        			ap.addAll(ConstantValues.user.pm.getfriendpost());
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
