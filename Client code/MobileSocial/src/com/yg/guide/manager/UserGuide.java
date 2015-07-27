package com.yg.guide.manager;

import java.util.ArrayList;

import com.yg.guide.tourguide.Overlay;
import com.yg.guide.tourguide.Overlay.Style;
import com.yg.guide.tourguide.Pointer;
import com.yg.guide.tourguide.ToolTip;
import com.yg.guide.tourguide.TourGuide;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class UserGuide
{
	private static final String DEBUG_TAG = "UserGuide______";
	private TourGuide tutorialHandler;
	private Activity activity;
	private GuideComplete callback;
	private ArrayList<View> views;
	
	public UserGuide(Activity activity, String title, String description, int gravity, Style style, String toolTipColor)
	{
		this.activity = activity;
		this.views = new ArrayList<View>();
		
		initTourGuide(title, description, gravity, style, toolTipColor);
	}

	/*private void isNeedUserGuide()
	{
		LoginInfo loginInfo = new LoginInfo(activity);
	}*/
	
	private void initTourGuide(String title, String description, int gravity, Style style, final String toolTipColor)
	{
		/* setup enter and exit animation */
		Animation enterAnimation = new AlphaAnimation(0f, 1f);
		enterAnimation.setDuration(600);
		enterAnimation.setFillAfter(true);

		Animation exitAnimation = new AlphaAnimation(1f, 0f);
		exitAnimation.setDuration(600);
		exitAnimation.setFillAfter(true);
		
		/* initialize TourGuide without playOn() */
		tutorialHandler = TourGuide
				.init(activity)
				.with(TourGuide.Technique.Click)
				.setPointer(new Pointer())
				.setToolTip(
						new ToolTip()
								.setTitle(title)
								.setDescription(description)
								.setGravity(gravity)
								.setBackgroundColor(Color.parseColor(toolTipColor)))
				.setOverlay(
						new Overlay().setEnterAnimation(enterAnimation)
								.setExitAnimation(exitAnimation)
								.disableClick(true)
								.setStyle(style));
	}
	
	/**
	 * Add another user guide view
	 * @param currentView the current view.
	 * @param nextView the next view you want to lead the user to.
	 * @param isLastView true if the nextView is the last view to show
	 * @param title the title of the nextView
	 * @param description the description of the nextView
	 * @param toolTipgravity the gravity of the nextView's tip <b>Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM etc.</b>
	 */
	public void addAnotherGuideArea(View currentView, final View nextView, final boolean isLastView,
				final String title, final String description, final int toolTipgravity, final String toolTipColor)
	{
		addAnotherGuideArea(currentView, nextView, isLastView, title, description, 
				toolTipgravity, Gravity.CENTER, toolTipColor);
	}
	
	public void addAnotherGuideArea(View currentView, final View nextView, final boolean isLastView,
			final String title, final String description,
			final int toolTipgravity, final int pointerGravity, final String toolTipColor)
	{
		this.views.add(nextView);
		Log.i(DEBUG_TAG, "Setting up " + title);
		currentView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Pointer pointer = new Pointer(pointerGravity, Color.parseColor("#FFFFFF"));
				tutorialHandler.cleanUp();
				tutorialHandler.setPointer(pointer)
								.setToolTip(
										new ToolTip().setTitle(title)
										.setDescription(description)
										.setGravity(toolTipgravity)
										.setBackgroundColor(Color.parseColor(toolTipColor)))
								.playOn(nextView);
				
				if (isLastView)
				{
					nextView.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							tutorialHandler.cleanUp();
							removeClickListeners();
							UserGuide.this.callback.onUserGuideCompleted();
						}
					});
				}
			}
		});
	}
	
	/**
	 * Start user guide.
	 * @param view the first view of the user guide.
	 * @param singleGuide true if this beginning view is the only view that involved during the user guide. 
	 */
	public void beginWith(View view, boolean singleGuide, GuideComplete callback)
	{
		this.callback = callback;
		this.views.add(view);
		
		if (singleGuide)
		{
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					tutorialHandler.cleanUp();
					removeClickListeners();
					UserGuide.this.callback.onUserGuideCompleted();
				}
			});
		}
		
		tutorialHandler.playOn(view);
	}
	
	private void removeClickListeners()
	{
		for (View view: views)
			view.setOnClickListener(null);
	}
}