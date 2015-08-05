package com.tp.share;

import com.example.testmobiledatabase.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class About_activity extends Activity 
{
	private ImageView weiBoBtn, weChatBtn, qqBtn, copyBoardBtn, eMailBtn, friendCircleBtn, renrenBtn;
	private Button updateBtn, recommandBtn, policyBtn;
	private ImageView mask, blurImageView;
	private RelativeLayout aboutLayout;
	private LinearLayout buttonLayout;
	private Animation animationTopToMid, animationMidToBottom;
	private boolean isRecommandClicked = false;
	private final int blurlayout = 0, showblurlayout = 1;
	private Bitmap bm;
	boolean isBlur = false;
	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private final String title = "分享";
	final String content = "好东西，要分享！向大家推荐#Mystery#陌生交友应用!";
	private final String TAG_ = "About_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_about_activity);
        setupDialogActionBar();
        initSocialMedia();
        initview();
    }
    public class onAnimationListener implements AnimationListener
    {
		@Override
		public void onAnimationEnd(Animation arg0) 
		{
			if (arg0 == animationTopToMid)
			{
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = buttonLayout.getWidth();
				int height = buttonLayout.getHeight();
				int left = (int) buttonLayout.getX();
				int top = (int) ((aboutLayout.getHeight() - height) / 2);
				buttonLayout.layout(left, top, left + width, top + height);
				Log.e(TAG_, "layout animationTopToMid:" + left +" " + top + " " + width + " " + height + dm.heightPixels);
				buttonLayout.clearAnimation();
				recommandBtn.setEnabled(false);
				updateBtn.setEnabled(false);
				policyBtn.setEnabled(false);
				
			}
			else if (arg0 == animationMidToBottom)
			{
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = buttonLayout.getWidth();
				int height = buttonLayout.getHeight();
				int left = (int) buttonLayout.getX();
				int top = 0;
				buttonLayout.layout(left, top, left + width, top + height);
				Log.e(TAG_, "layout animationMidToBottom:" + left + " " + top + " " + width + " " + height);
				buttonLayout.clearAnimation();
				buttonLayout.setVisibility(View.INVISIBLE);
				recommandBtn.setEnabled(true);
				updateBtn.setEnabled(true);
				policyBtn.setEnabled(true);
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) 
		{
		}

		@Override
		public void onAnimationStart(Animation arg0) 
		{
			recommandBtn.setEnabled(false);
			updateBtn.setEnabled(false);
			policyBtn.setEnabled(false);
			if (arg0 == animationTopToMid)
			{
				buttonLayout.setVisibility(View.VISIBLE);
				mask.setVisibility(View.VISIBLE);
			}
			else if (arg0 == animationMidToBottom)
			{
				mask.setVisibility(View.INVISIBLE);
			}
		}
    }
    
    private void initSocialMedia()
    {
		com.umeng.socialize.utils.Log.LOG = true;
		String appIDWEIXIN = "wx3139a15c7d365561";
		String appSecretIDWEIXIN = "d3892f80142ec4fe80fcd637a7f99cbb";
		String appIDRenRen = "480957";
		String apiKeyRenRen = "a4b4262af9894920ae188bb5707fb2a5";
		String appSecretIDRenRen = "a9c7e88a28204078887f87fa980e8801";
		
		UMWXHandler wxHandler = new UMWXHandler(this, appIDWEIXIN, appSecretIDWEIXIN);
		wxHandler.addToSocialSDK();
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(title);
		weixinContent.setTitle(content);
		mController.setShareMedia(weixinContent);
		
		UMWXHandler wxCircleHandler = new UMWXHandler(this,appIDWEIXIN,appSecretIDWEIXIN);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle(title);
		mController.setShareMedia(circleMedia);
		
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(this, "201874", "28401c0964f04a72a14c812d6132fcef", "3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);
    }
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.tp_about_actionbar);
		
		LinearLayout back = (LinearLayout)findViewById(R.id.tp_app_about_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
    private void initview()
    {
    	buttonLayout = (LinearLayout) findViewById(R.id.tp_about_buttonlayout);
    	mask = (ImageView) findViewById(R.id.tp_about_activity_mask);
    	blurImageView = (ImageView) findViewById(R.id.tp_about_activity_blur);
    	buttonLayout.bringToFront();
    	aboutLayout = (RelativeLayout) findViewById(R.id.tp_about_layout);
    	recommandBtn = (Button) findViewById(R.id.tp_about_recommand_btn);
    	updateBtn = (Button) findViewById(R.id.tp_about_update_btn);
    	policyBtn = (Button) findViewById(R.id.tp_about_policy_btn);
    	weiBoBtn = (ImageView) findViewById(R.id.tp_about_weibo_btn);
    	weChatBtn = (ImageView) findViewById(R.id.tp_about_wechat_btn);
    	qqBtn = (ImageView) findViewById(R.id.tp_about_qq_btn);
    	copyBoardBtn = (ImageView) findViewById(R.id.tp_about_copyBoard_btn);
    	eMailBtn = (ImageView) findViewById(R.id.tp_about_email_btn);
    	friendCircleBtn = (ImageView) findViewById(R.id.tp_about_friendcircle_btn);
    	renrenBtn = (ImageView) findViewById(R.id.tp_about_renren_btn);
    	animationTopToMid = AnimationUtils.loadAnimation(About_activity.this, R.anim.tp_top_to_mid);
    	animationMidToBottom = AnimationUtils.loadAnimation(About_activity.this, R.anim.tp_mid_to_bottom);
    	animationMidToBottom.setAnimationListener(new onAnimationListener());
    	animationTopToMid.setAnimationListener(new onAnimationListener());
    	
    	policyBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(About_activity.this, TermOfService_activity.class);
				startActivity(intent);
			}
		});
    	updateBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(About_activity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
			}
		});
    	qqBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				Log.e(TAG_, "weiBoBtn click");
				Intent intent = new Intent("android.intent.action.SEND");
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, content);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setType("text/plain");
				intent.setComponent(new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity"));
				About_activity.this.startActivity(intent);
			}
		});
    	weiBoBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(About_activity.this, "此功能正在开发中，敬请期待!", Toast.LENGTH_SHORT).show();
			}
		});
    	renrenBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(About_activity.this, "此功能正在开发中，敬请期待!", Toast.LENGTH_SHORT).show();
			}
		});
    	weChatBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				SnsPostListener mSnsPostListener = new SnsPostListener() 
				{
					@Override
					public void onStart() 
					{
					}
					@Override
					public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
						if (eCode == 200) 
						{
	                         //Toast.makeText(About_activity.this, "分享成功!", Toast.LENGTH_SHORT).show();
	                     } 
						else 
						{
	                          String eMsg = "";
	                          if (eCode == -101)
	                          {
	                              eMsg = "没有授权";
	                          }
	                         // Toast.makeText(About_activity.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
	                     }
					}
				};
				mController.postShare(About_activity.this,SHARE_MEDIA.WEIXIN, mSnsPostListener);
				mController.registerListener(mSnsPostListener);
			}
		});
    	eMailBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				 Intent email =  new  Intent(android.content.Intent.ACTION_SEND);  
		         email.setType( "plain/text" );  
		         String emailSubject =  title;
		         String emailBody = content;

		         email.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		         email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);  
		         email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);  
		         startActivityForResult(Intent.createChooser(email,  "请选择邮箱" ),1001 );  
			}
		});
    	copyBoardBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				ClipboardManager cmb = (ClipboardManager)About_activity.this.getSystemService(About_activity.this.CLIPBOARD_SERVICE); 
				if (cmb.getText().equals(content))
				{
					Toast.makeText(About_activity.this, "内容已经存在，快告诉小伙伴们吧!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(About_activity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
					cmb.setText(content.trim());
				}
			}
		});
    	friendCircleBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				SnsPostListener mSnsPostListener = new SnsPostListener() 
				{
					@Override
					public void onStart() 
					{
					}
					@Override
					public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
						if (eCode == 200) 
						{
	                         //Toast.makeText(About_activity.this, "分享成功.", Toast.LENGTH_SHORT).show();
	                     } 
						else 
						{
	                          String eMsg = "";
	                          if (eCode == -101)
	                          {
	                              eMsg = "没有授权";
	                          }
	                      //Toast.makeText(About_activity.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
	                     }
					}
				};
				mController.postShare(About_activity.this,SHARE_MEDIA.WEIXIN_CIRCLE, mSnsPostListener);
				mController.registerListener(mSnsPostListener);
			}
		});
    	aboutLayout.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				if (arg0 != buttonLayout)
				{
					if (isRecommandClicked == true)
					{
						Log.e(TAG_, "aboutLayout click");
						isRecommandClicked = false;
						buttonLayout.startAnimation(animationMidToBottom);
						blurImageView.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
    	recommandBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				isRecommandClicked = true;
				Message message = Message.obtain();
				message.what = showblurlayout;
				handler.sendMessage(message);
				buttonLayout.startAnimation(animationTopToMid);
			}
		});
    	aboutLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() 
            {
            	if (isBlur == false)
            	{
            		Log.e(TAG_, "aboutLayout.getViewTreeObserver()");
            		aboutLayout.setDrawingCacheEnabled(true);
            		aboutLayout.buildDrawingCache();
            		bm = aboutLayout.getDrawingCache();
            		bm = Blur.fastblur(bm, 25);
            		isBlur = true;
            	}
            }
        });
    }
    
    private Handler handler = new Handler()
    {
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case showblurlayout:
				blurImageView.setImageBitmap(bm);
				blurImageView.setVisibility(View.VISIBLE);
				blurImageView.bringToFront();
				buttonLayout.bringToFront();
				break;
			}
		}
	};
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if(ssoHandler != null)
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        switch (requestCode) 
        {  
        case 1001:
        	buttonLayout.startAnimation(animationMidToBottom);
        	blurImageView.setVisibility(View.INVISIBLE);
            break;  
        case 1002:  
        	buttonLayout.startAnimation(animationMidToBottom);
        	blurImageView.setVisibility(View.INVISIBLE);
            break;  
        default:  
            break;  
        }  
    }
}
