package com.yg.videochat;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IncomingCallFragment extends Fragment implements Serializable
{
	private static final String TAG = "IncomeCallFragment______";

	private ArrayList<Integer> opponents;
    private QBRTCSessionDescription sessionDescription;
    private int qbConferenceType;
    private QBRTCTypes.QBConferenceType conferenceType;
    private View view;
    private ImageView rejectBtn;
    private ImageView takeBtn;
    private TextView callerName;
    private MediaPlayer ringtone;
    private Vibrator vibrator;
    
    private LinearLayout back;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		if (getArguments() != null) 
        {
            opponents = getArguments().getIntegerArrayList("opponents");
            sessionDescription = (QBRTCSessionDescription) getArguments().getSerializable("sessionDescription");
            qbConferenceType = getArguments().getInt("conference_type");

            conferenceType = qbConferenceType == 1 ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
            Log.d(TAG, conferenceType.toString() + "From onCreateView()");
        }
		
		if (savedInstanceState == null) 
        {
            view = inflater.inflate(R.layout.yg_video_chat_incoming_fragment, container, false);

            initUI(view);
            initButtonsListener();
        }
		
		return view;
    }

	private void initUI(View view) 
	{
        callerName = (TextView) view.findViewById(R.id.yg_video_chat_incoming_name);

        rejectBtn = (ImageView) view.findViewById(R.id.yg_video_chat_incoming_deny);
        takeBtn = (ImageView) view.findViewById(R.id.yg_video_chat_incoming_accept);
        back = (LinearLayout) view.findViewById(R.id.yg_video_chat_incoming_back);
    }
	
	private void initButtonsListener()
	{
		rejectBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				reject();
			}
		});

		takeBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				takeBtn.setClickable(false);
				Log.d(TAG, "Call is taken.");
				stopCallNotification();
				 
				((VideoChatActivity) getActivity()).addConversationFragmentReceiveCall();
				 
				Log.d(TAG, "Call is started");
			}
		});
		 
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				reject();
			}
		});
	}

	private void reject()
	{
		rejectBtn.setClickable(false);
		Log.d(TAG, "Call is rejected");
		stopCallNotification();
		 
		((VideoChatActivity) getActivity()).rejectCurrentSession();
		((VideoChatActivity) getActivity()).removeIncomeCallFragment();
		((VideoChatActivity) getActivity()).addOpponentsFragment();
	}
	
	public void startCallNotification()
	{
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		ringtone = MediaPlayer.create(getActivity(), notification);
		ringtone.start();

		vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		long[] vibrationCycle = {0, 1000, 1000};
		if (vibrator.hasVibrator()) 
			vibrator.vibrate(vibrationCycle, 1);
	}
	
	private void stopCallNotification() 
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

		if (vibrator != null) 
			vibrator.cancel();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        setRetainInstance(true);
        Log.d(TAG, "onCreate() from IncomeCallFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() 
    {
        super.onStart();
        startCallNotification();
    }

    @Override
    public void onStop() 
    {
        super.onDestroy();
        stopCallNotification();
        Log.d(TAG, "onDestroy() from IncomeCallFragment");
    }
}