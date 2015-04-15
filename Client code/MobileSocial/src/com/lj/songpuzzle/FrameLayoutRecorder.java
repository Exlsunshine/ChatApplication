package com.lj.songpuzzle;


import com.example.testmobiledatabase.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class FrameLayoutRecorder extends RelativeLayout
{
	public static int VIDEO_CONTROL_START_ID = 1;
	public static int VIDEO_CONTROL_NEXT_ID = 2;
	
	private float gResizeScale;
	private int gPhoneWidth;
	private int gPhoneHeight;
	
	private ImageView gLeftBox;
	private ImageView gRightBox;
	private ImageView gLeftTapeCenter;
	private ImageView gRightTapeCenter;
	private ImageView gTape;
	private ImageView gLeftStripe;
	private ImageView gRightStripe;
	private ViewTouchRotate gVoiceRotary;
	
	public FrameLayoutRecorder(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	
	private Bitmap resizeBitmap(Bitmap bitmap)
	{
		Matrix matrix = new Matrix(); 
		matrix.postScale(gResizeScale, gResizeScale);
		
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	private void resizeLayoutSize(int width, int height)
	{
		gPhoneWidth = width;
		gPhoneHeight = height;
		ViewGroup.LayoutParams layoutparams = this.getLayoutParams();
		layoutparams.width = gPhoneWidth;
		layoutparams.height = gPhoneHeight;
		this.setLayoutParams(layoutparams);
	}
	
	private ImageView setView(int width, int height, double left, double top, Animation animation, Bitmap bitmap)
	{
		ImageView view = new ImageView(getContext());
		RelativeLayout.LayoutParams leftBoxLayoutParams = new LayoutParams(width, height);
		leftBoxLayoutParams.leftMargin = (int) (left * gPhoneWidth);
		leftBoxLayoutParams.topMargin = (int) (top * gPhoneHeight);
		view.setLayoutParams(leftBoxLayoutParams);
		view.setImageBitmap(bitmap);
		view.setScaleType(ScaleType.CENTER_INSIDE);
		if (animation != null)
			view.startAnimation(animation);
		this.addView(view);
		return view;
	}
	
	public void setVideoControlListener(OnTouchListener listener)
	{
		gLeftBox.setOnTouchListener(listener);
		gRightBox.setOnTouchListener(listener);
	}
	 
	public void initLayout(int width)
	{
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_recorder);
		gResizeScale = (float)width / background.getWidth();
		background = resizeBitmap(background); 
		resizeLayoutSize(background.getWidth(), background.getHeight());
		this.setBackgroundDrawable(new BitmapDrawable(background));
		 
		int boxSize = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_start).getWidth() * gResizeScale);
		gLeftBox = setView(boxSize, boxSize, 0.02508960573476702508960573476703, 0.49358974358974358974358974358974, AnimationUtils.loadAnimation(getContext(), R.anim.lj_box_zoom), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_start));
		gLeftBox.setId(VIDEO_CONTROL_START_ID);
		gRightBox = setView(boxSize, boxSize, 0.65681003584229390681003584229391, 0.49358974358974358974358974358974, AnimationUtils.loadAnimation(getContext(), R.anim.lj_box_zoom), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicebox_next));
		gRightBox.setId(VIDEO_CONTROL_NEXT_ID);
		
		int stripeSize = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_stripe).getWidth() * gResizeScale);
		gLeftStripe = setView(stripeSize, stripeSize, 0.41387455197132616487455197132616, 0.52976923076923076923076923076923, AnimationUtils.loadAnimation(getContext(), R.anim.lj_strip_zoomin), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_stripe));
		gRightStripe = setView(stripeSize, stripeSize, 0.50158817204301075268817204301075, 0.52976923076923076923076923076923, AnimationUtils.loadAnimation(getContext(), R.anim.lj_strip_zoomout), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_stripe));
		
		int tapeCenterSize = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tapecenter).getWidth() * gResizeScale);
		gLeftTapeCenter = setView(tapeCenterSize, tapeCenterSize, 0.43996415770609318996415770609319, 0.56666666666666666666666666666667, AnimationUtils.loadAnimation(getContext(), R.anim.lj_tapecenter_rotate), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tapecenter));
		gRightTapeCenter = setView(tapeCenterSize, tapeCenterSize, 0.52777777777777777777777777777778, 0.56666666666666666666666666666667, AnimationUtils.loadAnimation(getContext(), R.anim.lj_tapecenter_rotate), BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tapecenter));
	
		int tapeWidth = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tape).getWidth() * gResizeScale);
		int tapeHeight = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tape).getHeight() * gResizeScale);
		gTape = setView(tapeWidth, tapeHeight, 0.37813620071684587813620071684588, 0.50484615384615384615384615384615, null, BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_tape));
	
		int rotarySize = (int) (BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicerotary).getWidth() * gResizeScale);
		gVoiceRotary = new ViewTouchRotate(getContext());
		RelativeLayout.LayoutParams leftBoxLayoutParams = new LayoutParams(rotarySize, rotarySize);
		leftBoxLayoutParams.leftMargin = (int) (0.87544802867383512544802867383513 * gPhoneWidth);
		leftBoxLayoutParams.topMargin = (int) (0.17820512820512820512820512820513 * gPhoneHeight);
		gVoiceRotary.setLayoutParams(leftBoxLayoutParams);
		gVoiceRotary.setScaleType(ScaleType.CENTER_INSIDE);
		gVoiceRotary.setGearImage(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_voicerotary));
		this.addView(gVoiceRotary);
		
	/*	int videoControlSize = (int)(BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_start).getWidth() * gResizeScale);
		gStartVideo = setView(videoControlSize, videoControlSize, 0.14516129032258064516129032258065, 0.66410256410256410256410256410256, null, BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_start));
		gStartVideo.setId(VIDEO_CONTROL_START_ID);
		gNextVideo = setView(videoControlSize, videoControlSize, 0.77688172043010752688172043010753, 0.66538461538461538461538461538462, null, BitmapFactory.decodeResource(getResources(), R.drawable.lj_songpuzzle_next));
		gNextVideo.setId(VIDEO_CONTROL_NEXT_ID);*/
	}

}
