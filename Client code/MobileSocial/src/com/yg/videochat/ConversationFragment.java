package com.yg.videochat;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationFragment extends Fragment
{
	private static final String TAG = "ConversationFragment______";
	private View view;
	private ImageView hangup;
	private ImageView mic;
	private ImageView speaker;
	private ImageView camera;
	private ImageView cameraSwitch;
	private boolean isMicEnable = true;
	private boolean isCameraEnable = true;
	private boolean isSpeakerEnable = true;
    private MediaPlayer ringtone;
	private ImageView imgMyCameraOff;
	private View localVideoView;
	private View remoteVideoView;
	private IntentFilter intentFilter;
	private boolean isMessageProcessed;
	private int startReason;
	private TextView userName;
	private ArrayList<Integer> opponents;
	private String sessionID;
	private String callerName;
	private CameraState cameraState = CameraState.NONE;
	private AudioStreamReceiver audioStreamReceiver;
	private int qbConferenceType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.yg_video_chat_conversation_fragment, container, false);
		
		
		Log.d(TAG, "Fragment. Thread id: " + Thread.currentThread().getId());
		
		if (getArguments() != null)
		{
			opponents = getArguments().getIntegerArrayList("opponents");
			qbConferenceType = getArguments().getInt("conference_type");
			startReason = getArguments().getInt(VideoChatActivity.START_CONVERSATION_REASON);
			sessionID = getArguments().getString(VideoChatActivity.SESSION_ID);
			callerName = getArguments().getString(VideoChatActivity.CALLER_NAME);
			Log.d(TAG, "CALLER_NAME: " + callerName);
		}
		
		setupLayout();
		setupListeners();
		setUpUiByCallType(qbConferenceType);
		
		return view;
	}
	
	private void setUpUiByCallType(int qbConferenceType) 
	{
		if (qbConferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO.getValue())
		{
			camera.setVisibility(View.GONE);
			cameraSwitch.setVisibility(View.INVISIBLE);

			localVideoView.setVisibility(View.INVISIBLE);
			remoteVideoView.setVisibility(View.INVISIBLE);

			imgMyCameraOff.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setupLayout()
	{
		localVideoView = view.findViewById(R.id.yg_activity_video_chat_conversation_local_video);
		remoteVideoView = view.findViewById(R.id.yg_activity_video_chat_conversation_remote_video);
	
		camera = (ImageView) view.findViewById(R.id.yg_video_chat_conversation_camera);
		cameraSwitch = (ImageView) view.findViewById(R.id.yg_activity_video_chat_conversation_camera_switch);
		speaker = (ImageView) view.findViewById(R.id.yg_video_chat_conversation_speaker);
		mic = (ImageView) view.findViewById(R.id.yg_video_chat_conversation_mic);
		
		hangup = (ImageView) view.findViewById(R.id.yg_video_chat_conversation_hangup);
		userName = (TextView) view.findViewById(R.id.yg_activity_video_chat_conversation_name);
		imgMyCameraOff = (ImageView) view.findViewById(R.id.yg_activity_video_chat_conversation_camera_off);
		
		actionButtonsEnabled(false);
	}
	
	private void setupListeners()
	{
		cameraSwitch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (((VideoChatActivity) getActivity()).getCurrentSession() != null) 
				{
					((VideoChatActivity) getActivity()).getCurrentSession().switchCapturePosition(new Runnable() 
					{
						@Override
						public void run() 
						{
							Log.i(TAG, "Camera was switched.");
						}
					});
					Log.d(TAG, "Camera was switched!");
				}
			}
		});

		camera.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if (cameraState != CameraState.DISABLED_FROM_USER)
				{
					isCameraEnable = false;
					toggleCamera(false);
					cameraState = CameraState.DISABLED_FROM_USER;
				} 
				else
				{
					isCameraEnable = true;
					toggleCamera(true);
					cameraState = CameraState.ENABLED_FROM_USER;
				}
			}
		});

		speaker.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				isSpeakerEnable = !isSpeakerEnable;
				if (((VideoChatActivity) getActivity()).getCurrentSession() != null)
				{
					Log.d(TAG, "Dynamic switched!");
					((VideoChatActivity) getActivity()).getCurrentSession().switchAudioOutput();
				}
			}
		});

		mic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if (((VideoChatActivity) getActivity()).getCurrentSession() != null)
				{
					if (isMicEnable) 
					{
						Log.d(TAG, "Mic is off!");
						((VideoChatActivity) getActivity()).getCurrentSession().setAudioEnabled(false);
						isMicEnable = false;
					} 
					else
					{
						Log.d(TAG, "Mic is on!");
						((VideoChatActivity) getActivity()).getCurrentSession().setAudioEnabled(true);
						isMicEnable = true;
					}
				}
			}
		});

		hangup.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				stopOutBeep();
				actionButtonsEnabled(false);
				hangup.setEnabled(false);
				Log.d(TAG, "Call is stopped");

				((VideoChatActivity) getActivity()).hangUpCurrentSession();
				hangup.setEnabled(false);
				hangup.setActivated(false);
			}
		});
	}
	
	public void actionButtonsEnabled(boolean enability) 
	{
		camera.setEnabled(enability);
		cameraSwitch.setEnabled(enability);
		mic.setEnabled(enability);
		hangup.setEnabled(enability);
		speaker.setEnabled(enability);
        
		camera.setActivated(enability);
		cameraSwitch.setActivated(enability);
		mic.setActivated(enability);
		hangup.setActivated(enability);
		speaker.setActivated(enability);
    }
	
	public void stopOutBeep()
	{
		if (ringtone != null) 
		{
			try 
			{
                ringtone.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
			ringtone.release();
            ringtone = null;
        }
    }

	private void startOutBeep()
	{
		ringtone = MediaPlayer.create(getActivity(), R.raw.beep);
		ringtone.setLooping(true);
		ringtone.start();
	}
	
	private class AudioStreamReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			/*
			 * if (intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG))
			 * { Log.d(TAG, "ACTION_HEADSET_PLUG " + intent.getIntExtra("state",
			 * -1)); } else
			 */if (intent.getAction().equals(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED))
			 {
				 Log.d(TAG, "ACTION_SCO_AUDIO_STATE_UPDATED "
						 + intent.getIntExtra("EXTRA_SCO_AUDIO_STATE", -2));
			 }

			 if (intent.getIntExtra("state", -1) == 0 /*
													 * || intent.getIntExtra(
													 * "EXTRA_SCO_AUDIO_STATE",
													 * -1) == 0
													 */)
			 {
				 speaker.setEnabled(false);
			 }
			 else if (intent.getIntExtra("state", -1) == 1) 
			 {
				 speaker.setEnabled(true);
			 } 
			 else
			 {
				// Toast.makeText(context, "Output audio stream is incorrect",
				// Toast.LENGTH_LONG).show();
			 }
			 speaker.invalidate();
		}
	}
	
	private enum CameraState 
	{
		NONE, DISABLED_FROM_USER, ENABLED_FROM_USER
	}
	
	private void toggleCamera(boolean isNeedEnableCam)
	{
		// temporary insertion will be removed when GLVideoView will be fixed
		DisplayMetrics displaymetrics = new DisplayMetrics();
		displaymetrics.setToDefaults();

		ViewGroup.LayoutParams layoutParams = imgMyCameraOff.getLayoutParams();

		layoutParams.height = localVideoView.getHeight();
		layoutParams.width = localVideoView.getWidth();

		imgMyCameraOff.setLayoutParams(layoutParams);

		Log.d(TAG, "Width is: " + imgMyCameraOff.getLayoutParams().width
				+ " height is:" + imgMyCameraOff.getLayoutParams().height);

		if (((VideoChatActivity) getActivity()).getCurrentSession() != null)
		{
			((VideoChatActivity) getActivity()).getCurrentSession().setVideoEnabled(isNeedEnableCam);
			isCameraEnable = isNeedEnableCam;

			if (isNeedEnableCam) 
			{
				Log.d(TAG, "Camera is on!");
				cameraSwitch.setVisibility(View.VISIBLE);
				imgMyCameraOff.setVisibility(View.INVISIBLE);
			} 
			else 
			{
				Log.d(TAG, "Camera is off!");
				cameraSwitch.setVisibility(View.INVISIBLE);
				imgMyCameraOff.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public static enum StartConversetionReason {
		INCOME_CALL_FOR_ACCEPTION, OUTCOME_CALL_MADE;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.d(TAG, "onCreate() from " + TAG);
		super.onCreate(savedInstanceState);

		intentFilter = new IntentFilter();
		intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);

		audioStreamReceiver = new AudioStreamReceiver();
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
		
		getActivity().registerReceiver(audioStreamReceiver, intentFilter);

		super.onStart();
		QBRTCSession session = ((VideoChatActivity) getActivity()).getCurrentSession();
		if (!isMessageProcessed) 
		{
			if (startReason == StartConversetionReason.INCOME_CALL_FOR_ACCEPTION.ordinal()) 
				session.acceptCall(session.getUserInfo());
			else
			{
				session.startCall(session.getUserInfo());
				startOutBeep();
			}
			isMessageProcessed = true;
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		// If user changed camera state few times and last state was
		// CameraState.ENABLED_FROM_USER 
		// than we turn on camera, else we nothing change
		if (cameraState != CameraState.DISABLED_FROM_USER && qbConferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.getValue())
			toggleCamera(true);
	}
	
	@Override
	public void onPause()
	{
		// If camera state is CameraState.ENABLED_FROM_USER or CameraState.NONE
		// than we turn off camera
		if (cameraState != CameraState.DISABLED_FROM_USER) 
			toggleCamera(false);
				
		super.onPause();
	}

	@Override
	public void onStop() 
	{
		super.onStop();
		
		stopOutBeep();
		getActivity().unregisterReceiver(audioStreamReceiver);
	}
}
