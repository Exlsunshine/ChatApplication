package com.ui;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.commons.CommonUtil;
import com.commons.ConstantValues;
import com.example.testmobiledatabase.R;
import com.user.ClientUser;

public class Register extends Activity
{
	private String loginAccount;
	private String password;
	private String nickName;
	private String email;
	private String sex = "male";
	private String phoneNumber;
	private String birthday;
	private String portraitPath;
	private String hometown;
	private HometownDataset hometownDataset;
	
	private EditText etLoginAccount;
	private EditText etPassword;
	private EditText etPasswordConfirm;
	private EditText etNickame;
	private EditText etEmail;
	private RadioGroup rgSex;
	private EditText etPhoneNumber;
	private TextView tvBirthday;
	private ImageView ivPortrait;
	private Button btnSignup;
    private Spinner proviences;
    private Spinner cities;
    private Spinner districts;
    private ArrayAdapter<String> provienceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;
    
    private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));
    
    private void initLayout()
    {
    	etLoginAccount = (EditText)findViewById(R.id.reg_email);
    	etPassword = (EditText)findViewById(R.id.reg_pwd);
    	etPasswordConfirm = (EditText)findViewById(R.id.reg_pwd_confirm);
    	etNickame = (EditText)findViewById(R.id.reg_nick_name);
    	etEmail = (EditText)findViewById(R.id.reg_email);
    	rgSex = (RadioGroup)findViewById(R.id.reg_sex);
    	etPhoneNumber = (EditText)findViewById(R.id.reg_phone_number);
    	tvBirthday = (TextView)findViewById(R.id.reg_birthday);
    	ivPortrait = (ImageView)findViewById(R.id.reg_protrait);
    	btnSignup = (Button)findViewById(R.id.reg_signup);
    	
    	cities = (Spinner) findViewById(R.id.reg_cities);
    	proviences = (Spinner) findViewById(R.id.reg_proviences);
    	districts = (Spinner) findViewById(R.id.reg_districts);
    }
    
    private void setupListeners()
    {
    	tvBirthday.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View arg0)
			{
				pickDate();
			}
		});
    	
    	rgSex.setOnCheckedChangeListener(new OnCheckedChangeListener()
    	{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
				RadioButton rb = (RadioButton)Register.this.findViewById(arg0.getCheckedRadioButtonId());
				sex = rb.getText().toString();
			}
		});
    	
    	btnSignup.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View arg0)
			{
				loginAccount = etLoginAccount.getText().toString();
				password = etPassword.getText().toString();
				nickName = etNickame.getText().toString();
				email = etEmail.getText().toString();
				phoneNumber = etPhoneNumber.getText().toString();
				hometown = hometownDataset.currentProvince + " " + hometownDataset.currentCity + " " + hometownDataset.currentDistrict;
				
				if (0 == ClientUser.createNewUser(loginAccount, password, nickName, email, sex, birthday, portraitPath, hometown, phoneNumber))
				{
					Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
					
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run() 
						{
							Intent it = new Intent(Register.this, Login.class);
							startActivity(it);
							Register.this.finish();
						}
					}, 2000);
				}
				else
				{
					Toast.makeText(Register.this, "Failed try again.", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	
    	ivPortrait.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View arg0) 
			{
				new AlertDialog.Builder(Register.this).setTitle("请选择").setIcon(R.drawable.ic_launcher).setItems(new String[] {"本地图库", "照相机"}, new DialogInterface.OnClickListener()
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
		});
    }
    
    private void startPhotoZoom(Uri uri) 
    {  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, ConstantValues.InstructionCode.REQUESTCODE_CROP);  
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
			try {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), IMAGE_URI);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ivPortrait.setImageBitmap(bitmap);
			portraitPath = Environment.getExternalStorageDirectory() + "/" + ConstantValues.InstructionCode.USERSET_PORTRAIT;
			bitmap = null;
		}
    }
    
    private void initData()
    {
    	hometownDataset = new HometownDataset();
    	hometownDataset.provinces = CommonUtil.getProvienceList();
    	hometownDataset.cities = new String [] {"Select"};
    	hometownDataset.districts = new String [] {"Select"};
    }
    
    private void setupAdapter()
    {
    	cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, hometownDataset.cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(cityAdapter);
        cities.setOnItemSelectedListener(new citySelectedListener());
        cities.setVisibility(View.VISIBLE);
        
        provienceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, hometownDataset.provinces);
        provienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proviences.setAdapter(provienceAdapter);
        proviences.setOnItemSelectedListener(new provienceSelectedListener());
        proviences.setVisibility(View.VISIBLE);
        
        districtAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, hometownDataset.districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districts.setAdapter(provienceAdapter);
        districts.setOnItemSelectedListener(new districtSelectedListener());
        districts.setVisibility(View.VISIBLE);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initLayout();
		setupListeners();
		initData();
		setupAdapter();
	}
	
	private class districtSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			hometownDataset.currentDistrict = hometownDataset.districts[arg2];
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private class provienceSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			hometownDataset.currentProvince = hometownDataset.provinces[arg2];
			hometownDataset.cities = CommonUtil.getCityList(hometownDataset.currentProvince);
			cities.setAdapter(new ArrayAdapter<String>(Register.this,android.R.layout.simple_spinner_item, hometownDataset.cities));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private class citySelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			hometownDataset.currentCity = hometownDataset.cities[arg2];
			hometownDataset.districts = CommonUtil.getDistrictList(hometownDataset.currentProvince, hometownDataset.currentCity);
			districts.setAdapter(new ArrayAdapter<String>(Register.this,android.R.layout.simple_spinner_item, hometownDataset.districts));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private void pickDate()
	{
		OnDateSetListener pickListener = new OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
			{
				birthday = String.format("%d-%d-%d", arg1, arg2 + 1, arg3);
				
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						tvBirthday.setText(birthday);
					}
				});
			}
		};
		
		new DatePickerDialog(Register.this, pickListener, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	private class HometownDataset
	{
		public String provinces [] = null;
		public String cities [] = null;
		public String districts [] = null;
		
		public String currentProvince = null;
		public String currentCity = null;
		public String currentDistrict = null;
	}
}