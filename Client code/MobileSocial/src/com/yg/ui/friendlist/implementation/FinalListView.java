package com.yg.ui.friendlist.implementation;

import com.example.testmobiledatabase.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class FinalListView extends ListView implements OnScrollListener {
	/**
	 * ����scroller
	 */
	Scroller scroller = null;
	/**
	 * ��ָ����ʱXY������
	 */
	private int downX;
	private int downY;
	/**
	 * ��ָ�������ʱX Y������
	 */
	private int cX;
	private int cY;
	/**
	 * ��ǰListView��position (dispatchTouchEvent�е���)
	 */
	private int listPosition = 0;
	/**
	 * ��ǰListView��ѡ�е�item
	 */
	private View itemView;
	/**
	 * ��һ��ѡ��ListView��position (dispatchTouchEvent�е���)
	 */
	private int listPosition1 = 0;
	/**
	 * ��ǰListView��position (onTouchEvent�е���)
	 */
	private int listPosition2 = 0;
	/**
	 * ����ǰListView��position (onTouchEvent�е���)
	 */
	//private int listPosition3 = 0;
	/**
	 * ���listview������
	 */
	private int count = 0;
	/**
	 * �ж��Ƿ���븸���onTouchEvent,false����
	 */
	private boolean dd = true;
	/**
	 * �ж��Ƿ���븸���onTouchEvent,false����
	 */
	private boolean dc = false;
	/**
	 * �жϵ�ǰitem�Ƿ��ǻ���״̬
	 */
	private boolean tc = false;
	/**
	 * ����RemoveListener�ӿ�
	 */
	private RemoveListener mRemoveListener;

	private boolean isSrollh = true;

	private boolean isSrollv = true;

	private LayoutInflater inflater;

	private LinearLayout headerView;
	private TextView headerTipsTv;
	private ImageView arrowup;
	private ImageView progressBar;

	private int headerContentHeight;
	private OnRefreshListener refreshListener;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int startY;
	private int tempY;
	private int tempY1;
	private boolean dd3 = false;
	private boolean dc3 = false;
	//private boolean dc4 = false;
	private boolean dc5 = false;
	//private int x = 100;

	private int firstVisibleItem1;

	//private View itemView1;

	private Context context = null;

	private Handler handler = null;
	//private Handler handler1 = null;

	private int width = 0;

	private int viewWidth;

	private AnimationDrawable animationDrawable = null;

	public FinalListView(Context context) {
		super(context);
		scroller = new Scroller(context);
		init(context);
		this.context = context;
	}

	public FinalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
		this.context = context;
	}

	public FinalListView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		init(context);
		this.context = context;

	}

	@SuppressWarnings("deprecation")
	private void init(Context context) {

		inflater = LayoutInflater.from(context);
		this.headerView = (LinearLayout) inflater.inflate(R.layout.yg_lv_header,
				null);
		this.headerTipsTv = (TextView) headerView
				.findViewById(R.id.headerTipsTv);
		this.arrowup = (ImageView) headerView.findViewById(R.id.arrowup);
		this.progressBar = (ImageView) headerView
				.findViewById(R.id.progressBar);

		progressBar.setBackgroundResource(R.drawable.yg_appkefu_loading);
		measureView(headerView);
		headerContentHeight = headerView.getMeasuredHeight();

		headerView.setPadding(0, -1 * headerContentHeight, 0, 0);

		headerView.invalidate();

		addHeaderView(headerView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(300);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(300);
		reverseAnimation.setFillAfter(true);
		handler = new Handler();
		//handler1 = new Handler();

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		viewWidth = Dp2Px(context, 90);

	}

	/**
	 * �ַ��¼�,��ʼ������
	 * 
	 * @return super.dispatchTouchEvent(event) ���ø����dispatchTouchEvent����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: {

			if (!scroller.isFinished()) {
				return super.dispatchTouchEvent(event);
			}
			downX = (int) event.getX();
			downY = (int) event.getY();
			cX = downX;
			cY = downY;
			listPosition = pointToPosition(downX, downY);

			if (count != 0) {
				if (listPosition1 != listPosition) {
					// while(itemView.getScrollX() != 0)
					// {
					// String a = "" + itemView.getScrollX() ;
					// Log.i("sdfs", a);
					// scroller.startScroll(itemView.getScrollX(), 0,
					// -itemView.getScrollX(), 0,300);
					// postInvalidate();
					// }
					itemView.scrollTo(0, 0);
					isSrollv = true;
					dc = false;
					tc = false;
				}
			}

			listPosition1 = listPosition;

			if (listPosition == AdapterView.INVALID_POSITION) {
				return super.dispatchTouchEvent(event);
			}

			itemView = getChildAt(listPosition - getFirstVisiblePosition());

			count++;
			break;

		}
		case MotionEvent.ACTION_MOVE:

			break;

		case MotionEvent.ACTION_UP:

			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * ʵ��ListView item�������߼�
	 * 
	 * @return super.onTouchEvent ���ø����onTouchEvent����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (listPosition != AdapterView.INVALID_POSITION)
		{
			final int action = ev.getAction();
			int x = (int) ev.getX();
			int y = (int) ev.getY();

			listPosition2 = pointToPosition(x, y);
			//listPosition3 = pointToPosition(downX, downY);
			switch (action)
			{
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getY();
				tempY1 = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (isSrollh)
				{
					int deltaX = downX - x;
					int deltaY = downY - y;

					if (Math.abs(deltaY) < 70 || dc)
					{
						if (Math.abs(deltaX) > 50 || itemView.getScrollX() > 0)
						{
							if ((downX - x + itemView.getScrollX()) >= 0
									&& (downX - x + itemView.getScrollX()) <= 2 * viewWidth) 
							{
								dc = true;
								isSrollv = false;
								itemView.scrollTo(deltaX + itemView.getScrollX(), 0);
								downX = x;
							}
							downX = x;
						}
						return true;
					}
				}
				if (isSrollv)
				{
					tempY = (int) ev.getY();

					if ((tempY - startY) > 0)
						dd3 = true;

					if ((tempY - startY) / 3 < headerContentHeight - 6) 
					{
						progressBar.setVisibility(View.GONE);
						arrowup.setVisibility(View.VISIBLE);
						this.arrowup
								.setImageResource(R.drawable.yg_appkefu_pulltorefresh_arrow);
						headerTipsTv.setText("����ˢ��");
						//dc4 = true;
						dc3 = false;
					} 
					else if ((tempY - startY) / 3 > headerContentHeight + 6) 
					{
						headerTipsTv.setText("�ͷ�ˢ��");
						dc3 = true;
					} 
					else 
					{
						if (tempY1 <= tempY) 
							arrowup.startAnimation(animation);
						else 
							arrowup.startAnimation(reverseAnimation);
					}

					if (tempY > startY && firstVisibleItem1 == 0)
					{
						headerView.setPadding(0, (tempY - startY) / 3 - headerContentHeight, 0, 0);
						isSrollh = false;
						dc5 = true;
					} 
					else 
					{
						dc5 = false;
						startY = tempY;
						isSrollh = true;
					}

					tempY1 = tempY;

					if (dc5) 
						return true;
					else
						break;
				}
			case MotionEvent.ACTION_UP:
				if (isSrollh) 
				{
					x = (int) ev.getX();
					y = (int) ev.getY();

					if (Math.abs(cY - y) > 70 && !tc && !dc)
						return super.onTouchEvent(ev);
					else
					{
						if (cX - x > 30)
						{
							if (itemView.getScrollX() > 0 && itemView.getScrollX() < 2 * viewWidth) 
							{
								dd = true;
								scroller.startScroll(itemView.getScrollX(), 0,
										2 * viewWidth - itemView.getScrollX(), 0, 300);
								postInvalidate();
								tc = true;
							}
						}

						if (cX - x < -30)
						{
							if (itemView.getScrollX() > 0 && itemView.getScrollX() < 2 * viewWidth) 
							{
								dd = true;
								dc = false;
								scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(), 0, 300);
								postInvalidate();
								tc = false;
								isSrollv = true;
							}
						}
						if (Math.abs(cX - x) <= 30) 
						{
							if (tc && x > width - (2 * viewWidth) && x < width - viewWidth) 
							{
								mRemoveListener.removeItem(listPosition2, 0);
								dd = true;
							}
							else if (tc && x < width && x > width - viewWidth)
							{
								mRemoveListener.removeItem(listPosition2, 1);
								scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(), 0, 300);
								isSrollv = true;
								postInvalidate();
								dd = true;
							}
							else if (Math.abs(cY - y) < 5)
							{
								dd = false;
								isSrollv = true;
							}
						}

						cX = 0;

						if (dd)
							return true;
						else 
						{
							dd = true;
							break;
						}
					}
				}

				if (isSrollv)
				{
					if (dd3)
					{
						x = 100;
						dd3 = false;
						arrowup.clearAnimation();
						arrowup.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);
						animationDrawable = (AnimationDrawable) progressBar.getBackground();
						animationDrawable.start();
						isSrollh = true;
						if (dc3) 
						{
							headerTipsTv.setText("����ˢ��");

							new Thread()
							{
								public void run()
								{

									while (headerView.getPaddingTop() > 1) 
									{
										handler.post(runnableUi);
										try
										{
											sleep(20);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									dc3 = false;
									onLvRefresh();

								};
							}.start();

						} 
						else
						{
							headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
							setSelection(0);
						}
					}
					return true;
				}
			}
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ʵ��item�����ı�Ҫ����,�ڵ���postInvalidate()���Զ�����
	 * 
	 * @return null
	 */
	public void computeScroll()
	{

		if (scroller.computeScrollOffset())
		{
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * ����ɾ���ļ�����
	 * 
	 * @param removeListener
	 *            �̳�RemoveListener�ӿڵĶ���
	 * @return null
	 */
	public void setRemoveListener(RemoveListener removeListener) 
	{
		this.mRemoveListener = removeListener;
	}

	/**
	 * ����ɾ���ӿ�,�ڻص�����removeItem��ɾ����position��item
	 * 
	 * @return null
	 */
	public interface RemoveListener
	{
		public void removeItem(int position, int id);
	}

	@SuppressWarnings("deprecation")
	private void measureView(View child) 
	{
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null)
			params = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0) 
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		else 
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) 
	{
		firstVisibleItem1 = firstVisibleItem;
	}

	public void setonRefreshListener(OnRefreshListener refreshListener)
	{
		this.refreshListener = refreshListener;
	}

	public interface OnRefreshListener 
	{
		public void onRefresh();
	}

	public void onRefreshComplete() 
	{
		new Thread()
		{
			public void run()
			{
				while (headerView.getPaddingTop() > -1 * headerContentHeight)
				{
					handler.post(runnableUi1);
					try 
					{
						sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void onLvRefresh() 
	{
		if (refreshListener != null) 
			refreshListener.onRefresh();
	}

	public void Refresh()
	{
		Toast.makeText(context, "��ˢ��", Toast.LENGTH_SHORT).show();
		headerTipsTv.setText("ˢ�³ɹ�");
		animationDrawable.stop();
		progressBar.setVisibility(View.GONE);
		arrowup.setVisibility(View.VISIBLE);
		this.arrowup.setImageResource(R.drawable.yg_appkefu_pulltorefresh_success);
	}

	Runnable runnableUi = new Runnable()
	{
		@Override
		public void run() 
		{
			headerView.setPadding(0, (int) (headerView.getPaddingTop() * 0.75f), 0, 0);
		}
	};

	Runnable runnableUi1 = new Runnable()
	{
		@Override
		public void run()
		{
			headerView.setPadding(0, (int) ((headerView.getPaddingTop() - 10) * 1.25f), 0, 0);
		}
	};

	public int Dp2Px(Context context, float dp)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}