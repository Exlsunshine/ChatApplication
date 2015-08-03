package com.yg.guide.manager;

import java.util.ArrayList;

import com.yg.guide.tourguide.Overlay;
import com.yg.guide.tourguide.Overlay.Style;
import com.yg.guide.tourguide.Pointer;
import com.yg.guide.tourguide.ToolTip;
import com.yg.guide.tourguide.TourGuide;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class UserGuide
{
	public static final String RECENT_DIALOG_ACTIVITY = "RECENT_DIALOG_ACTIVITY";
	public static final String FRIENDLIST_ACTIVITY = "FRIENDLIST_ACTIVITY";
	public static final String DIALOG_ACTIVITY = "DIALOG_ACTIVITY";
	public static final String FRIENDCIRCLE_ACTIVITY = "FRIENDCIRCLE_ACTIVITY";
	public static final String SHAKE_ACTIVITY = "SHAKE_ACTIVITY";
	public static final String MAP_ACTIVITY = "MAP_ACTIVITY";
	public static final String GAME_MUSIC_ACTIVITY = "GAME_MUSIC_ACTIVITY";
	public static final String GAME_PICTURE_PUZZLE_ACTIVITY = "GAME_PICTURE_PUZZLE_ACTIVITY";
	public static final String GAME_BUBBLE_ACTIVITY = "GAME_BUBBLE_ACTIVITY";
	
	private static final String DEBUG_TAG = "UserGuide______";
	private TourGuide tutorialHandler;
	private Activity activity;
	private GuideComplete callback;
	private ArrayList<View> views;
	private Context context;
	private static SharedPreferences preference = null;

	private static final String SHARED_PREFERENCE_NAME = "jmmsr_user_guide_information";
	private static final boolean ENABLE = true;
	private static final boolean DISABLE = false;
	
	
	public UserGuide(Activity activity, String title, String description, int gravity, Style style, String toolTipColor)
	{
		this.activity = activity;
		this.views = new ArrayList<View>();
		
		initTourGuide(title, description, gravity, style, toolTipColor);
	}

	public static boolean isNeedUserGuide(Context context, String activityTag)
	{
		if (preference == null)
			preference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, PreferenceActivity.MODE_PRIVATE);
		
		return preference.getBoolean(activityTag, ENABLE);
	}
	
	public static void disableUserGuide(String activityTag)
	{
		if (preference == null)
		{
			Log.e(DEBUG_TAG, "Context or preference is null.");
			return;
		}
		
		preference.edit().putBoolean(activityTag, DISABLE).commit();
	}
	
	public static void enableAllUserGuides(boolean enable)
	{
		if (preference == null)
		{
			Log.e(DEBUG_TAG, "Context or preference is null.");
			return;
		}
		
		preference.edit().putBoolean(RECENT_DIALOG_ACTIVITY, enable).commit();
		preference.edit().putBoolean(FRIENDLIST_ACTIVITY, enable).commit();
		preference.edit().putBoolean(DIALOG_ACTIVITY, enable).commit();
		preference.edit().putBoolean(FRIENDCIRCLE_ACTIVITY, enable).commit();
		preference.edit().putBoolean(SHAKE_ACTIVITY, enable).commit();
		preference.edit().putBoolean(MAP_ACTIVITY, enable).commit();
		preference.edit().putBoolean(GAME_MUSIC_ACTIVITY, enable).commit();
		preference.edit().putBoolean(GAME_PICTURE_PUZZLE_ACTIVITY, enable).commit();
		preference.edit().putBoolean(GAME_BUBBLE_ACTIVITY, enable).commit();
	}
	
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