package com.yg.ui.signup;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import com.yg.image.select.ui.SelectImageActivity;
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
	
	private static final int SELECT_PORTRAIT_REQUEST = 34;
	
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
	private class onPortraitImgClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			Intent intent = new Intent(SignupActivity.this, SelectImageActivity.class);
			intent.putExtra(SelectImageActivity.FILTER_ENABLE, true);
			startActivityForResult(intent, SELECT_PORTRAIT_REQUEST);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == SELECT_PORTRAIT_REQUEST && resultCode == RESULT_OK)
		{
			String filePath = data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH);
			
			Bitmap bitmap = null;
			try 
			{
				bitmap = BitmapFactory.decodeFile(filePath);
				portraitPath = filePath;
				Window window = firstDialog.getWindow();
				ImageView portraitIv = (ImageView)window.findViewById(R.id.yg_signup_first_dialog_portrait);
				portraitIv.setImageBitmap(bitmap);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
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