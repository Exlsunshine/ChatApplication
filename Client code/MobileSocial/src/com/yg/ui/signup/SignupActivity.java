package com.yg.ui.signup;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.ui.login.LoginGuideActivity;
import com.yg.ui.signup.implementation.SignupImplementation;

public class SignupActivity extends Activity 
{
	private Button ok;
	private AlertDialog firstDialog, secondeDialog;
	
	private SignupImplementation loginObj;
	private String email, nickname, password;
	private String portraitPath, phoneNumber, sex;
	private String birthday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_signup_activity);
		
		Bundle bundle = this.getIntent().getExtras();
		email = bundle.getString("email");
		nickname = bundle.getString("nickname");
		password = bundle.getString("password");
		loginObj = new SignupImplementation(email, nickname, password);
		
		setupLayout();
		setupListeners();
	}
	
	private void setupLayout()
	{
		ok = (Button)findViewById(R.id.yg_signup_activity_ok);
	}
	
	private void setupListeners()
	{
		ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				showFirstDialog();
			}
		});
	}
	
	private void showFirstDialog()
	{
		firstDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		firstDialog.show();
		firstDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		firstDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = firstDialog.getWindow();
		window.setContentView(R.layout.yg_signup_first_dialog);
		
		Button next = (Button) window.findViewById(R.id.yg_signup_first_dialog_activity_next);
		next.setOnClickListener(new onNextBtnClickListener());
		
		ImageView portrait = (ImageView) window.findViewById(R.id.yg_signup_first_dialog_portrait);
		portrait.setOnClickListener(new onPortraitImgClickListener());
	}
	
	
	/**********************										***********************/
	/**********************			以下是选图相关函数				***********************/
	/**********************										***********************/
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));
	
	private void startPhotoZoom(Uri uri) 
	{  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CROP);  
    }  
	
	private class onPortraitImgClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			new AlertDialog.Builder(SignupActivity.this).setTitle("请选择")
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
			}).setNegativeButton("取消", null).show(); 
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_GALLERY && resultCode == RESULT_OK)
		{
			Uri selectedImage =  data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			File temp = new File(picturePath);
			startPhotoZoom(Uri.fromFile(temp)); 
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CAMERA && resultCode == RESULT_OK)
		{
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.USERSET_PORTRAIT);  
            startPhotoZoom(Uri.fromFile(temp));  
			
		}
		else if (requestCode == ConstantValues.InstructionCode.REQUESTCODE_CROP && resultCode == RESULT_OK)
		{
			Bitmap bitmap = null;
			try 
			{
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			
			portraitPath = Environment.getExternalStorageDirectory() + "/Portrait.jpg";
			Window window = firstDialog.getWindow();
			ImageView portraitIv = (ImageView)window.findViewById(R.id.yg_signup_first_dialog_portrait);
			portraitIv.setImageBitmap(bitmap);
			
			
			bitmap = null;
		}
	}

	/**********************										***********************/
	/**********************			以上是选图相关函数				***********************/
	/**********************										***********************/
	
	
	private class onNextBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			boolean validatePass = true;
			Window window = firstDialog.getWindow();
			EditText phoneNumberEt = (EditText)window.findViewById(R.id.yg_signup_first_dialog_phone_number);
			ImageView portraitIv = (ImageView)window.findViewById(R.id.yg_signup_first_dialog_portrait);
			RadioGroup sexGroup = (RadioGroup)window.findViewById(R.id.yg_signup_first_dialog_sexgroup);
			RadioButton sexRb = (RadioButton)window.findViewById(sexGroup.getCheckedRadioButtonId());
			
			if (phoneNumberEt.getText().toString().length() == 0 || phoneNumberEt.getText().toString() == null)
			{
				Animation shake = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
				phoneNumberEt.startAnimation(shake);
				validatePass = false;
			}
			if (portraitPath == null)
			{
				Animation shake = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.yg_loginguide_page3_login_dialog_anim_shake);
				portraitIv.startAnimation(shake);
				validatePass = false;
			}

			if(validatePass)
			{
				sex = sexRb.getText().toString();
				phoneNumber = phoneNumberEt.getText().toString();
				loginObj.setPortraitPath(portraitPath);
				loginObj.setPhoneNumber(phoneNumber);
				loginObj.setSex(sex);
				firstDialog.cancel();
				
				
				
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run() 
					{
						loginObj.initHometownData();
					}
				});
				td.start();
				try {
					td.join();
					showSecondeDialog();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void showSecondeDialog()
	{
		secondeDialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		
		secondeDialog.show();
		secondeDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		secondeDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = secondeDialog.getWindow();
		window.setContentView(R.layout.yg_signup_second_dialog);
		
		Button done = (Button) window.findViewById(R.id.yg_signup_second_dialog_done);
		done.setOnClickListener(new onDoneBtnClickListener());
		
		TextView birthday = (TextView) window.findViewById(R.id.yg_signup_second_dialog_birthday);
		birthday.setOnClickListener(new onBirthdayClickListener());
	
		Spinner proviences = (Spinner) window.findViewById(R.id.yg_signup_second_dialog_proviences);
	    Spinner cities = (Spinner) window.findViewById(R.id.yg_signup_second_dialog_cities);
	    Spinner districts = (Spinner) window.findViewById(R.id.yg_signup_second_dialog_districts);
	    ArrayAdapter<String> provienceAdapter;
	    ArrayAdapter<String> cityAdapter;
	    ArrayAdapter<String> districtAdapter;
	
	    cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, loginObj.getCities());
	    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    cities.setAdapter(cityAdapter);
	    cities.setOnItemSelectedListener(new citySelectedListener());
	    cities.setVisibility(View.VISIBLE);
	        
	    provienceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, loginObj.getProvinces());
	    provienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    proviences.setAdapter(provienceAdapter);
	    proviences.setOnItemSelectedListener(new provienceSelectedListener());
	    proviences.setVisibility(View.VISIBLE);
	        
	    districtAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, loginObj.getDistricts());
	    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    districts.setAdapter(provienceAdapter);
	    districts.setOnItemSelectedListener(new districtSelectedListener());
	    districts.setVisibility(View.VISIBLE);
	}
	
	private class districtSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			loginObj.setCurrentDistrict(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private class provienceSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, final int arg2, long arg3)
		{
			
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					loginObj.setCurrentProvince(arg2);
				}
			});
			td.start();
			try {
				td.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			Window window = secondeDialog.getWindow();
			Spinner cities = (Spinner) window.findViewById(R.id.yg_signup_second_dialog_cities);
			cities.setAdapter(new ArrayAdapter<String>(SignupActivity.this,android.R.layout.simple_spinner_item, loginObj.getCities()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private class citySelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, final int arg2, long arg3)
		{
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					loginObj.setCurrentCity(arg2);
				}
			});
			td.start();
			try {
				td.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			
			Window window = secondeDialog.getWindow();
			Spinner districts = (Spinner) window.findViewById(R.id.yg_signup_second_dialog_districts);
			districts.setAdapter(new ArrayAdapter<String>(SignupActivity.this,android.R.layout.simple_spinner_item, loginObj.getDistricts()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }

	public class onDoneBtnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			int result = loginObj.register();
			if (result > 0 && result != 65535)
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run() 
					{
						Intent it = new Intent(SignupActivity.this, LoginGuideActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("index", 3);
						it.putExtras(bundle);
						startActivity(it);
						SignupActivity.this.finish();
					}
				}, 2000);
			}
		}
	}
	
	public class onBirthdayClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			pickDate();
		}
	}
	
	private void pickDate()
	{
		OnDateSetListener pickListener = new OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
			{
				birthday = String.format("%d-%d-%d", arg1, arg2 + 1, arg3);
				loginObj.setBirthday(birthday);
				
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						Window window = secondeDialog.getWindow();
						TextView birthday = (TextView) window.findViewById(R.id.yg_signup_second_dialog_birthday);
						birthday.setText(SignupActivity.this.birthday);
					}
				});
			}
		};
		
		new DatePickerDialog(SignupActivity.this, pickListener, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).show();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}
}