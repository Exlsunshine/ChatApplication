package com.lj.setting.userinfo;

import java.io.File;
import java.util.HashMap;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.image.select.ui.SelectImageActivity;
import com.yg.ui.signup.SignupActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentUserInfoSetting extends Fragment
{
	
	private Context gContext;
	private View gView;
	public static final int ACTIVITY_RESULT_CODE_SEX = 1;
	public static final int ACTIVITY_REQUEST_CODE_SEX = 1;
	
	public static final int ACTIVITY_RESULT_CODE_AGE = 2;
	public static final int ACTIVITY_REQUEST_CODE_AGE = 2;
	
	public static final int ACTIVITY_RESULT_CODE_HOMETOWN = 3;
	public static final int ACTIVITY_REQUEST_CODE_HOMETOWN = 3;
	
	private static final int ACTIVITY_RESULT_CODE_PORTRAIT = 4;
	private static final int ACTIVITY_REQUEST_CODE_PORTRAIT = 4;
	
	private ImageView gPortraitImage = null;
	private EditText gNicknameEditText = null;
	private TextView gSexTextView = null;
	private TextView gAgeTextView = null;
	private TextView gConstellationTextView = null;
	private TextView gHometownTextView = null;
	private EditText gPhoneEditText = null;
	
	
	private HashMap<String, String> gChangeMap = null;  
	
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));
	
	private final int[] VIEW_ID = {R.id.lj_userinfo_setting_sex_layout, R.id.lj_userinfo_setting_sex_text, R.id.lj_userinfo_setting_sex_image,
								   R.id.lj_userinfo_setting_age_layout, R.id.lj_userinfo_setting_age_text, R.id.lj_userinfo_setting_age_image,
								   R.id.lj_userinfo_setting_constellation_layout, R.id.lj_userinfo_setting_constellation_text, R.id.lj_userinfo_setting_constellation_image,
								   R.id.lj_userinfo_setting_hometown_layout, R.id.lj_userinfo_setting_hometown_text, R.id.lj_userinfo_setting_hometown_image,
								   R.id.lj_userinfo_setting_password_layout, R.id.lj_userinfo_setting_password_text, R.id.lj_userinfo_setting_password_image,
								   R.id.lj_userinfo_setting_portrait};
	
	public FragmentUserInfoSetting(Context context, HashMap<String, String> map) 
	{
		gChangeMap = map;
		gContext = context;
	}
	
	private OnClickListener gSettingOnClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			if (id == R.id.lj_userinfo_setting_sex_content_text || id == R.id.lj_userinfo_setting_sex_layout || id == R.id.lj_userinfo_setting_sex_text || id == R.id.lj_userinfo_setting_sex_image)
			{
				String sex = gSexTextView.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("sex", sex);
				intent.setClass(getActivity(), ActivitySexSetting.class);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_SEX);
			}
			else if (id == R.id.lj_userinfo_setting_age_layout || id == R.id.lj_userinfo_setting_age_text || id == R.id.lj_userinfo_setting_age_image || id == R.id.lj_userinfo_setting_age_content_text ||
					 id == R.id.lj_userinfo_setting_constellation_layout || id == R.id.lj_userinfo_setting_constellation_text || id == R.id.lj_userinfo_setting_constellation_image || id == R.id.lj_userinfo_setting_constellation_content_text)
			{
				String date = gAgeTextView.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("date", date);
				intent.setClass(getActivity(), ActivityAgeSetting.class);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_AGE);
			}
			else if (id == R.id.lj_userinfo_setting_hometown_layout || id == R.id.lj_userinfo_setting_hometown_text || id == R.id.lj_userinfo_setting_hometown_image || id == R.id.lj_userinfo_setting_hometown_content_text)
			{
				String hometown = gHometownTextView.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("hometown", hometown);
				intent.setClass(getActivity(), ActivityHometownSetting.class);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_HOMETOWN);
			}
			else if (id == R.id.lj_userinfo_setting_password_layout || id == R.id.lj_userinfo_setting_password_text || id == R.id.lj_userinfo_setting_password_image)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityPasswordSetting.class);
				startActivity(intent);
			}
			else if (id == R.id.lj_userinfo_setting_portrait)
			{
				Intent intent = new Intent(gContext, SelectImageActivity.class);
				intent.putExtra(SelectImageActivity.FILTER_ENABLE, true);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_PORTRAIT);
				/*new AlertDialog.Builder(gContext).setTitle("请选择")
					.setIcon(R.drawable.ic_launcher)
					.setItems(new String[] {"本地图库", "照相机"}, new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							if (which == 0)
							{
								Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, ConstantValues.InstructionCode.REQUESTCODE_GALLERY);
							}							
							else if (which == 1)
							{
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT)));  
								startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CAMERA);  
							}
							dialog.dismiss();  
						}
					}).setNegativeButton("取消", null).show(); */
			}
		}
	};
	
	private OnFocusChangeListener gEditTextFocusChangeListener = new OnFocusChangeListener() 
	{
		String oldString = null;
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			String str = ((EditText)v).getText().toString();
			if (hasFocus)
				oldString = str;
			else
			{
				if (!oldString.equals(str))
				{
					if (v.getId() == R.id.lj_userinfo_setting_nickname)
						gChangeMap.put("nickname", str);
					else if (v.getId() == R.id.lj_userinfo_setting_phone)
						gChangeMap.put("phone", str);
				}
			}
		}
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		gView = inflater.inflate(R.layout.lj_fragment_userinfo_setting, container, false);
        return gView;
    }
	
	private void initData()
	{
		//头像
		gPortraitImage.setImageBitmap(ConstantValues.user.getPortraitBmp());
		//昵称
		gNicknameEditText.setText(ConstantValues.user.getNickName());
		//性别
		if (ConstantValues.user.getSex().equals("male"))
			gSexTextView.setText("男");
		else
			gSexTextView.setText("女");
		//生日
		gAgeTextView.setText(ConstantValues.user.getBirthday());
		//星座
		gConstellationTextView.setText(CommonUtil.getConstellation(ConstantValues.user.getBirthday()));
		//故乡
		gHometownTextView.setText(ConstantValues.user.getHometown());
		//手机号
		gPhoneEditText.setText(ConstantValues.user.getPhoneNumber());
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		gNicknameEditText = (EditText) gView.findViewById(R.id.lj_userinfo_setting_nickname);
        gNicknameEditText.clearFocus();
        gNicknameEditText.setOnFocusChangeListener(gEditTextFocusChangeListener);
        
        gSexTextView = (TextView) gView.findViewById(R.id.lj_userinfo_setting_sex_content_text);
        gSexTextView.setOnClickListener(gSettingOnClick);
        
        
        gAgeTextView = (TextView) gView.findViewById(R.id.lj_userinfo_setting_age_content_text);
        gAgeTextView.setOnClickListener(gSettingOnClick);
        
        for (int i = 0; i < VIEW_ID.length; i++)
        	gView.findViewById(VIEW_ID[i]).setOnClickListener(gSettingOnClick);
        
        gConstellationTextView = (TextView)gView.findViewById(R.id.lj_userinfo_setting_constellation_content_text);
        
        gHometownTextView = (TextView)gView.findViewById(R.id.lj_userinfo_setting_hometown_content_text);
        
        gPhoneEditText = (EditText)gView.findViewById(R.id.lj_userinfo_setting_phone);
        gPhoneEditText.setOnFocusChangeListener(gEditTextFocusChangeListener);
        
        gPortraitImage =(ImageView)gView.findViewById(R.id.lj_userinfo_setting_portrait);
        initData();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTIVITY_REQUEST_CODE_SEX && resultCode == ACTIVITY_RESULT_CODE_SEX)
		{
			String sex = data.getStringExtra("sex");
			gSexTextView.setText(sex);
			if (sex.equals("男"))
				gChangeMap.put("sex", "male");
			else
				gChangeMap.put("sex", "female");
		}
		else if (requestCode == ACTIVITY_REQUEST_CODE_AGE && resultCode == ACTIVITY_RESULT_CODE_AGE)
		{
			String date = data.getStringExtra("date");
			gAgeTextView.setText(date);
			gConstellationTextView.setText(CommonUtil.getConstellation(date));
			gChangeMap.put("date", date);
		}
		else if (requestCode == ACTIVITY_REQUEST_CODE_HOMETOWN && resultCode == ACTIVITY_RESULT_CODE_HOMETOWN)
		{
			String hometown = data.getStringExtra("hometown");
			gHometownTextView.setText(hometown);
			gChangeMap.put("hometown", hometown);
		}
		else if (requestCode == ACTIVITY_REQUEST_CODE_PORTRAIT && resultCode == Activity.RESULT_OK)
		{
			String filePath = data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH);
			
			Bitmap bitmap = null;
			try 
			{
				bitmap = BitmapFactory.decodeFile(filePath);
				gPortraitImage.setImageBitmap(bitmap);
				gChangeMap.put("portrait", filePath);
				bitmap = null;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void clearTextFocus()
	{
		gNicknameEditText.clearFocus();
		gPhoneEditText.clearFocus();
	}
}
