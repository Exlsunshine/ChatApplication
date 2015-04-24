package com.tp.adapter;


import com.example.testmobiledatabase.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;


public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> implements OnScrollListener{

	private LoadingLayout headerLoadingView;
	private LoadingLayout footerLoadingView;
	
	private OnPositionChangedListener mPositionChangedListener;
	
	private int mLastPosition = -1;
	
	private OnScrollListener mOnScrollListener = null;

    private View mScrollBarPanel = null;

    private int mScrollBarPanelPosition = 0;
    
    private Animation mInAnimation = null;

    private Animation mOutAnimation = null;
    
    private int mWidthMeasureSpec;

    private int mHeightMeasureSpec;
    private int extent, offset, range = 0;
    private final Handler mHandler = new Handler();

    private final Runnable mScrollBarPanelFadeRunnable = new Runnable() {

        @Override
        public void run() {
            if (mOutAnimation != null) {
                // mScrollBarPanel.startAnimation(mOutAnimation);
            }
        }
    };
	
	public static interface OnPositionChangedListener {
        public void onPositionChanged(PullToRefreshListView listView, int position, View scrollBarPanel);
        
        public void onScollPositionChanged(View scrollBarPanel,int top);

		void onScrollStateChanged(AbsListView view, int scrollState);
    }
	
	class InternalListView extends ListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
		
		@Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        System.out.println("onMeasure.......................");
	        if (mScrollBarPanel != null && getAdapter() != null) {
	            mWidthMeasureSpec = widthMeasureSpec;
	            mHeightMeasureSpec = heightMeasureSpec;

	            measureChild(mScrollBarPanel, widthMeasureSpec, heightMeasureSpec);
	        }
	    }
		@Override
		protected int computeVerticalScrollExtent() {
			extent = super.computeVerticalScrollExtent();
			Log.d("computeVerticalScrollExtent", extent + ""); 
			return extent;
		}
		
		
		@Override
		protected int computeVerticalScrollOffset() {
			int sRange = super.computeVerticalScrollRange();
			int sExtent = super.computeVerticalScrollExtent();
			int range = sRange - sExtent;
			if(range == 0){
				return 0;
			}
			offset =  (int) (sRange * super.computeVerticalScrollOffset() * 1.0f / range);
			Log.d("computeVerticalScrolloffset", offset + ""); 
			return offset;
		}
		
		@Override
		protected int computeVerticalScrollRange() {
			range = super.computeVerticalScrollRange();
			return range;
		}
	}
	
	public int getRefreshType() {
			return getCurrentMode();
	}
	
	public PullToRefreshListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(this);
		this.setDisableScrollingWhileRefreshing(false);
		
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedListView);
        final int scrollBarPanelLayoutId = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanel, -1);
        final int scrollBarPanelInAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelInAnimation, R.anim.tp_in_animation);
        final int scrollBarPanelOutAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelOutAnimation, R.anim.tp_out_animation);
        a.recycle();

        if (scrollBarPanelLayoutId != -1) {
            setScrollBarPanel(scrollBarPanelLayoutId);
        }

        final int scrollBarPanelFadeDuration = ViewConfiguration.getScrollBarFadeDuration();

        if (scrollBarPanelInAnimation > 0) {
            mInAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelInAnimation);
        }

        if (scrollBarPanelOutAnimation > 0) {
            mOutAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelOutAnimation);
            mOutAnimation.setDuration(scrollBarPanelFadeDuration);

            mOutAnimation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mScrollBarPanel != null) {
                        mScrollBarPanel.setVisibility(View.GONE);
                    }
                }
            });
        }
		
		
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	public void setReleaseLabel(String releaseLabel) {
		super.setReleaseLabel(releaseLabel);

		if (null != headerLoadingView) {
			headerLoadingView.setReleaseLabel(releaseLabel);
		}
		if (null != footerLoadingView) {
			footerLoadingView.setReleaseLabel(releaseLabel);
		}
	}

	public void setPullLabel(String pullLabel) {
		super.setPullLabel(pullLabel);

		if (null != headerLoadingView) {
			headerLoadingView.setPullLabel(pullLabel);
		}
		if (null != footerLoadingView) {
			footerLoadingView.setPullLabel(pullLabel);
		}
	}

	public void setRefreshingLabel(String refreshingLabel) {
		super.setRefreshingLabel(refreshingLabel);

		if (null != headerLoadingView) {
			headerLoadingView.setRefreshingLabel(refreshingLabel);
		}
		if (null != footerLoadingView) {
			footerLoadingView.setRefreshingLabel(refreshingLabel);
		}
	}

	@Override
	protected final ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);

		final int mode = this.getMode();

		// Loading View Strings
		String pullLabel = context.getString(R.string.pull_to_refresh_pull_down_label);
		String refreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
		String releaseLabel = context.getString(R.string.pull_to_refresh_release_label);
		
		// Add Loading Views
		if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
			FrameLayout frame = new FrameLayout(context);
			headerLoadingView = new LoadingLayout(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel);
			frame.addView(headerLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			headerLoadingView.setVisibility(View.GONE);
			lv.addHeaderView(frame);
		}
		if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
			FrameLayout frame = new FrameLayout(context);
			footerLoadingView = new LoadingLayout(context, MODE_PULL_UP_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel);
			frame.addView(footerLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			footerLoadingView.setVisibility(View.GONE);
			lv.addFooterView(frame);
		}

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}
	
	public void setScrollBarPanel(View scrollBarPanel) {
	        mScrollBarPanel = scrollBarPanel;
	        mScrollBarPanel.setVisibility(View.GONE);
	        requestLayout();
	}

	public void setScrollBarPanel(int resId) {
	        setScrollBarPanel(LayoutInflater.from(getContext()).inflate(resId, this, false));
	}

	public View getScrollBarPanel() {
	        return mScrollBarPanel;
	}
	
	public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
	        mPositionChangedListener = onPositionChangedListener;
	}
	@Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	 {
		 if (null != mPositionChangedListener && null != mScrollBarPanel) {

	            // Don't do anything if there is no itemviews
	            if (totalItemCount > 0) {
	                /*
	                 * from android source code (ScrollBarDrawable.java)
	                 */
	            	Log.d("onScrollmScrollBarPanelPosition", mScrollBarPanelPosition + "");
	                final int thickness = getVerticalScrollbarWidth();
	                int height = Math.round((float) getMeasuredHeight() * extent  / range);
	                int h = getMeasuredHeight();
	                int thumbOffset = Math.round((float) (getMeasuredHeight() - height) * offset / (range - extent));
	                Log.d("onScroll1", thumbOffset + " ");
	                final int minLength = thickness * 2;
	                if (height < minLength) 
	                {
	                    height = minLength;
	                }
	                thumbOffset += height / 2;
	                Log.d("onScroll2", thumbOffset + " ");
	                /*
	                 * find out which itemviews the center of thumb is on
	                 */
	                final int count = getChildCount();
	                for (int i = 0; i < count; ++i) {
	                    final View childView = getChildAt(i);
	                    if (childView != null) {
	                        if (thumbOffset > childView.getTop() && thumbOffset < childView.getBottom()) {
	                            /*
	                             * we have our candidate
	                             */
	                            if (mLastPosition != firstVisibleItem + i) {
	                                mLastPosition = firstVisibleItem + i;

	                                /*
	                                 * inform the position of the panel has changed
	                                 */
	                                mPositionChangedListener.onPositionChanged(this, mLastPosition,mScrollBarPanel);
	                                /*
	                                 * measure panel right now since it has just
	                                 * changed INFO: quick hack to handle TextView
	                                 * has ScrollBarPanel (to wrap text in case
	                                 * TextView's content has changed)
	                                 */
	                                measureChild(mScrollBarPanel, mWidthMeasureSpec, mHeightMeasureSpec);
	                            }
	                            break;
	                        }
	                    }
	                }

	                /*
	                 * update panel position
	                 */
	                mScrollBarPanelPosition = thumbOffset - mScrollBarPanel.getMeasuredHeight() / 2;
	                Log.d("onScrollmScrollBarPanelPosition___________", thumbOffset + " " + mScrollBarPanel.getMeasuredHeight());
	                final int x = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth()
	                        - getVerticalScrollbarWidth();
	                System.out.println("left==" + x + " top==" + mScrollBarPanelPosition + " bottom=="
	                        + (x + mScrollBarPanel.getMeasuredWidth()) + " right=="
	                        + (mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight()));
	                mScrollBarPanel.layout(x, mScrollBarPanelPosition,
	                        x + mScrollBarPanel.getMeasuredWidth(), mScrollBarPanelPosition
	                                + mScrollBarPanel.getMeasuredHeight());
	                Log.d("onScrollmScrollBarPanelPosition", mScrollBarPanelPosition + "");
	                mPositionChangedListener.onScollPositionChanged(this, mScrollBarPanelPosition);
	            }
	        }

	        if (mOnScrollListener != null) {
	            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	        }
	 }
	 
	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
	        if (mOnScrollListener != null) {
	            mOnScrollListener.onScrollStateChanged(view, scrollState);
	        }
	    }
	 
	@Override
	protected void setRefreshingInternal(boolean doScroll) {
		super.setRefreshingInternal(false);

		final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
		final int selection, scrollToY;

		switch (getCurrentMode()) {
			case MODE_PULL_UP_TO_REFRESH:
				originalLoadingLayout = this.getFooterLayout();
				listViewLoadingLayout = this.footerLoadingView;
				selection = refreshableView.getCount() - 1;
				scrollToY = getScrollY() - getHeaderHeight();
				break;
			case MODE_PULL_DOWN_TO_REFRESH:
			default:
				originalLoadingLayout = this.getHeaderLayout();
				listViewLoadingLayout = this.headerLoadingView;
				selection = 0;
				scrollToY = getScrollY() + getHeaderHeight();
				break;
		}

		if (doScroll) {
			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			this.setHeaderScroll(scrollToY);
		}

		// Hide our original Loading View
		originalLoadingLayout.setVisibility(View.INVISIBLE);

		// Show the ListView Loading View and set it to refresh
		listViewLoadingLayout.setVisibility(View.VISIBLE);
		listViewLoadingLayout.refreshing();

		if (doScroll) {
			// Make sure the ListView is scrolled to show the loading
			// header/footer
			refreshableView.setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	@Override
	protected void resetHeader() {

		LoadingLayout originalLoadingLayout;
		LoadingLayout listViewLoadingLayout;

		int scrollToHeight = getHeaderHeight();
		final boolean doScroll;

		switch (getCurrentMode()) {
			case MODE_PULL_UP_TO_REFRESH:
				originalLoadingLayout = this.getFooterLayout();
				listViewLoadingLayout = footerLoadingView;
				doScroll = this.isReadyForPullUp();
				break;
			case MODE_PULL_DOWN_TO_REFRESH:
			default:
				originalLoadingLayout = this.getHeaderLayout();
				listViewLoadingLayout = headerLoadingView;
				scrollToHeight *= -1;
				doScroll = this.isReadyForPullDown();
				break;
		}

		// Set our Original View to Visible
		originalLoadingLayout.setVisibility(View.VISIBLE);

		// Scroll so our View is at the same Y as the ListView header/footer,
		// but only scroll if the ListView is at the top/bottom
		if (doScroll) {
			this.setHeaderScroll(scrollToHeight);
		}

		// Hide the ListView Header/Footer
		listViewLoadingLayout.setVisibility(View.GONE);

		super.resetHeader();
	}
	
	@Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        final boolean isAnimationPlayed = super.awakenScrollBars(startDelay, invalidate);

        if (isAnimationPlayed == true && mScrollBarPanel != null) {
            if (mScrollBarPanel.getVisibility() == View.GONE) {
               // mScrollBarPanel.setVisibility(View.VISIBLE);
                if (mInAnimation != null) {
                    // mScrollBarPanel.startAnimation(mInAnimation);

                }
            }

            mHandler.removeCallbacks(mScrollBarPanelFadeRunnable);
            mHandler.postAtTime(mScrollBarPanelFadeRunnable,
                    AnimationUtils.currentAnimationTimeMillis() + startDelay);
        }

        return isAnimationPlayed;
    }

    

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.out.println("onLayout.......................");
        if (mScrollBarPanel != null) {
            final int x = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth()
                    - getVerticalScrollbarWidth();
            mScrollBarPanel.layout(x, mScrollBarPanelPosition,
                    x + mScrollBarPanel.getMeasuredWidth(), mScrollBarPanelPosition
                            + mScrollBarPanel.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        System.out.println("dispatchDraw.......................");
        if (mScrollBarPanel != null && mScrollBarPanel.getVisibility() == View.VISIBLE) {

            drawChild(canvas, mScrollBarPanel, getDrawingTime());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        System.out.println("onDetachedFromWindow.......................");
        mHandler.removeCallbacks(mScrollBarPanelFadeRunnable);
    }

}
