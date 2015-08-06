
package com.tp.ui;

import java.util.ArrayList;

import org.json.JSONException;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import com.example.testmobiledatabase.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.tp.adapter.PublicActivityAdapter;
import com.tp.adapter.PullToRefreshBase.OnRefreshListener;
import com.tp.adapter.PullToRefreshBase.OnShouldPullDwonListener;
import com.tp.adapter.PullToRefreshListView;
import com.tp.adapter.PullToRefreshListView.OnPositionChangedListener;
import com.tp.messege.AbstractPost;
import com.tp.messege.EmptyPost;
import com.tp.views.MenuRightAnimations;
import com.yg.commons.ConstantValues;
import com.yg.guide.manager.GuideComplete;
import com.yg.guide.manager.UserGuide;
import com.yg.guide.tourguide.Overlay;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class PublicActivity extends Activity implements OnTouchListener, OnPositionChangedListener 
{
	/**
	 * Below codes belongs to YG
	 */
	private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
	private static final int DO_NOT_CHANGE_REFRESH = 0x01;
	private void initDropdownAnim() 
	{
		mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
		mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() 
		{
			@Override
			public void onRefresh()
			{
				new Task().execute();
			}
		});
		mWaveSwipeRefreshLayout.setCustomEnable(false);
	}

	private class Task extends AsyncTask<Void, Void, String[]> 
	{
		private ArrayList<AbstractPost> posts;
		
		@Override
		protected String[] doInBackground(Void... params) 
		{
			long beginTime = 0;
			long endTime = 0;
			try 
			{
				beginTime = System.currentTimeMillis();
				posts = ConstantValues.user.pm.getLatestPosts();
				endTime = System.currentTimeMillis();
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
			if ((endTime - beginTime) < 3000)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) 
		{
			if (posts == null)
			{
				Log.d("Pull down to refresh", "getLatestPosts size 0");
				Message message = Message.obtain();
				message.what = pulluprefreshempty;
				handler.sendMessage(message);
			} 
			else 
			{
				Log.d("Pull down to refresh",  "getLatestPosts size " + posts.size());
				ap.clear();
				ap.add(empty);
				ap.addAll(posts);
				Message message = Message.obtain();
				message.what = setAdpter;
				handler.sendMessage(message);
			}
				
			mWaveSwipeRefreshLayout.setRefreshing(false);
			mWaveSwipeRefreshLayout.setCustomEnable(false);
			mPullRefreshListView.setPullDownActionEnabled(true);
			super.onPostExecute(result);
		}
	}
	/**
	 * Above codes belongs to YG
	 */

		
    // clock
    private FrameLayout clockLayout;
    private LinearLayout send, back;
    private Button fakeButton;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private EmptyPost empty = new EmptyPost();
    private PublicActivityAdapter chatHistoryAdapter;
    private static ArrayList<AbstractPost> ap = new ArrayList<AbstractPost>();
    private final int setAdpter = 1;
    private final int pulluprefresh = 2;
    private final int pulluprefreshempty = 3;
    private final int refresh = 4;
    private final int hideclocklayout = 5;
    private final int showclocklayout = 6;
    private int position1 = 0;
    private boolean isOncreate = false;
    private int index = 0, top = 0;
    private UserGuide userGuide;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.tp_publicactivity_actionbar);
		
		TextView title = (TextView)findViewById(R.id.tp_publicactivity_actionbar_title);
		title.setText("朋友圈");
		back = (LinearLayout)findViewById(R.id.tp_publicactivity_actionbar_back);
		send = (LinearLayout)findViewById(R.id.tp_publicactivity_actionbar_send);
	}
    private void setupUserGuide()
	{
    	fakeButton.setVisibility(View.VISIBLE);
    	fakeButton.bringToFront();
		userGuide = new UserGuide(this, "匿 名", "发送新鲜事", Gravity.BOTTOM | Gravity.LEFT, Overlay.Style.Circle, "#FF9900");
		userGuide.addAnotherGuideArea(send, fakeButton, true, "我 的 分 享", "点击查看自己的新鲜事", Gravity.BOTTOM, Gravity.CENTER, "#1E90FF");
		userGuide.beginWith(send, false, new GuideComplete()
		{
			@Override
			public void onUserGuideCompleted()
			{
				fakeButton.setVisibility(View.GONE);
				setClickListener();
				UserGuide.disableUserGuide(UserGuide.FRIENDCIRCLE_ACTIVITY);
			}
		});
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_feed_activity2);
        
        /**
    	 * Below codes belongs to YG
    	 */
		System.gc();
    	/**
    	 * Above codes belongs to YG
    	 */
		
		
        MenuRightAnimations.initOffset(PublicActivity.this);
        setupDialogActionBar();
        new Thread()
        {
        	public void run() 
        	{
        		try
        		{
        			if (ap.size() == 0)
        			{
            			ArrayList<AbstractPost> posts = ConstantValues.user.pm.get10Posts();
            			if (posts != null)
            			{
            				ap.add(empty);
            				ap.addAll(posts);
            			}
            			else
            				ap.add(empty);
        			}
        			else
        			{
        				ArrayList<AbstractPost> posts = ConstantValues.user.pm.getLatestPosts();
            			if (posts != null)
            			{
            				ap.clear();
            				ap.add(empty);
            				ap.addAll(posts);
            			}
        			}
        			
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
        fakeButton = (Button) findViewById(R.id.tp_fake_button);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.publicactivity_pulltorefreshlist_view);
        mListView = mPullRefreshListView.getRefreshableView();
       
        mPullRefreshListView.setOnPositionChangedListener(this);
        clockLayout = (FrameLayout)findViewById(R.id.publicactivity_clock);
        mPullRefreshListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true)); 
        mPullRefreshListView.setOnScrollListener(new OnScrollListener() 
        {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
                {
					index = mListView.getFirstVisiblePosition();  
					View v = mListView.getChildAt(0);  
					top = (v == null) ? 0 : v.getTop();  
					Thread td = new Thread(new Runnable() 
			        {
						@Override
						public void run() 
						{
							try 
							{
								Thread.sleep(2000);
								Message message = Message.obtain();
			    				message.what = hideclocklayout;
			    				handler.sendMessage(message);
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
					});
			        td.start();
                }
				else
				{
					Message message = Message.obtain();
    				message.what = showclocklayout;
    				handler.sendMessage(message);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
			{
			}
		});
        if (UserGuide.isNeedUserGuide(PublicActivity.this, UserGuide.FRIENDCIRCLE_ACTIVITY))
        	setupUserGuide();
        else
        	setClickListener();
        isOncreate = true;
        
        /**
    	 * Below codes belongs to YG
    	 */
        initDropdownAnim();
    	/**
    	 * Above codes belongs to YG
    	 */
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
				mListView.setSelectionFromTop(index, top);
				Log.e("PA_____", "setSelection position1 = " + position1);
				/*mListView.setOnItemClickListener(new OnItemClickListenerImpl());*/
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
			case hideclocklayout:
				clockLayout.setVisibility(View.INVISIBLE);
				break;
			case showclocklayout:
				clockLayout.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	 @Override
	    public boolean onTouch(View v, MotionEvent event) 
	    {
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
			position1 = position;
			Log.e("PA_____", "position1 = " + position1);
			if (position == 1)
			{
				Intent intent = new Intent(PublicActivity.this,MyselfPostActivity.class);
        		startActivity(intent);
			}
			if (position > 1)
			{
				int postId = (int) chatHistoryAdapter.getItemId(position - 1);
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
    }
    
    @Override
    public void onPositionChanged(PullToRefreshListView listView, int firstVisiblePosition, View scrollBarPanel) 
    {
    	TextView clocktext = (TextView) findViewById(R.id.clock_digital_time);
    	ImageView minView = (ImageView) findViewById(R.id.clock_face_minute);
    	ImageView hourView = (ImageView) findViewById(R.id.clock_face_hour);
        hourView.setImageResource(R.drawable.tp_clock_hour_rotatable);
        TextView datestr = ((TextView) findViewById(R.id.clock_digital_date));
        if (firstVisiblePosition == 1)
        {
        	clockLayout.setVisibility(View.INVISIBLE);
        	return;
        }
        else
        	clockLayout.setVisibility(View.VISIBLE);
    	if (firstVisiblePosition > ap.size() || firstVisiblePosition == ap.size())
    	{
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
    	position1 = mListView.getFirstVisiblePosition();
        MarginLayoutParams layoutParams = (MarginLayoutParams) clockLayout.getLayoutParams();
        layoutParams.setMargins(0, top, 0, 0);
        clockLayout.setLayoutParams(layoutParams);
        //clockLayout.setVisibility(View.VISIBLE);
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
		private ArrayList<AbstractPost> tmp;
		int pullState;
		public GetDataTask(int pullType) 
		{
			this.pullState = pullType;
		}
		
		@Override
		protected String[] doInBackground(Void... params) 
		{
			if(pullState == 1) 
			{
			}
			if(pullState == 2) 
			{
				//上拉
				Log.d("onPostExecute", "上拉");
				tmp = ConstantValues.user.pm.getHistoryPosts();
			}			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if(pullState == 1) 
			{
			}
			if(pullState == 2) 
			{
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
		}
	}
	
	private void setClickListener() 
	{
		/**
    	 * Below codes belongs to YG
    	 */
		mPullRefreshListView.setOnShouldPullDwonListener(new OnShouldPullDwonListener()
		{
			@Override
			public void onShouldPullDown() 
			{
				mWaveSwipeRefreshLayout.setCustomEnable(true);
			}
		});
    	/**
    	 * Above codes belongs to YG
    	 */
        
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				/**
		    	 * Below codes belongs to YG
		    	 */
				System.gc();
				/**
		    	 * Above codes belongs to YG
		    	 */
				finish();
			}
		});
		
		send.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(PublicActivity.this, SendPostActivity.class);
				startActivity(intent);
			}
		});
		/*fakeButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Log.e("fakeButton.setOnClickListener", "onclick");
			}
		});*/
		mListView.setOnItemClickListener(new OnItemClickListenerImpl());
	}
	
	@Override
	public void onResume()
	{
		if (isOncreate == false)
		{
			mListView.post(new Runnable() 
			{
				@Override
				public void run() 
				{
					Message message = Message.obtain();
        			ap.clear();
        			ap.add(empty);
        			ap.addAll(ConstantValues.user.pm.getfriendpost());
        			message.what = setAdpter;
        			handler.sendMessage(message);
				}
			});
			super.onResume();
		}
		else
		{
			isOncreate = false;
			super.onResume();
		}
	}
}
