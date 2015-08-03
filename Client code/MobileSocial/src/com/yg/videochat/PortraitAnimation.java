package com.yg.videochat;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;

public class PortraitAnimation
{
	private static PortraitAnimation portraitAnimation;
	private int currentSelection;
	private ArrayList<Integer> availableSelections;
	
	public static PortraitAnimation getInstance()
	{
		if (portraitAnimation == null)
			portraitAnimation = new PortraitAnimation();
		
		return portraitAnimation;
	}
	
	private PortraitAnimation()
	{
		currentSelection = 0;
		availableSelections = new ArrayList<Integer>();
		availableSelections.add(R.drawable.yg_video_chat_random_portrait1);
		availableSelections.add(R.drawable.yg_video_chat_random_portrait2);
		availableSelections.add(R.drawable.yg_video_chat_random_portrait3);
		availableSelections.add(R.drawable.yg_video_chat_random_portrait4);
		availableSelections.add(R.drawable.yg_video_chat_random_portrait5);
		availableSelections.add(R.drawable.yg_video_chat_random_portrait6);
	}
	
	public int getCurrentSelection()
	{
		return availableSelections.get(currentSelection);
	}
	
	public int getNextSelection()
	{
		currentSelection += 1;
		currentSelection %= 6;
		
		return availableSelections.get(currentSelection);
	}
}
