package com.lj.userbasicsetting;

import java.io.File;
import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.message.ConvertUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityUserBasicSetting extends Activity
{
	private final Uri IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),ConstantValues.InstructionCode.USERSET_PORTRAIT));

	private static final String DEBUG_TAG = "ActivityUserBasicSetting_____________________";
	private TextView nicknameText;
	private TextView passwordText;
	private TextView birthdayText;
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private Spinner districtSpinner;
	private TextView phoneText;
	private TextView sexText;
	private ImageView portraitImageView;
	
	private String[] hometown;
	
	private String[] provinceArray;
	private String[] cityArray;
	private String[] districtArray;
	private String provinceSelect;
	private String citySelect;
	private String districtSelect;
	
	public Handler handle = new Handler()
	{
		private int getSelectItemPosition(String str, String[] array)
		{
			for (int i = 0; i < array.length; i++)
			{
				if (str.equals(array[i]))
					return i;
			}
			return 0;
		}
		
		private void initSpinner(String[] array, Spinner spinner, String select, int index)
		{
			spinner.setAdapter(new ArrayAdapter<String>(ActivityUserBasicSetting.this,android.R.layout.simple_spinner_item, array));
			select = hometown[index];
			int position = getSelectItemPosition(select, array);
			spinner.setSelection(position);
			Log.e(DEBUG_TAG, select);
		}
		
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
			case ConstantValues.InstructionCode.ERROR_NETWORK:
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_PROVINCE:
				provinceArray = (String[]) msg.obj;
				initSpinner(provinceArray, provinceSpinner, provinceSelect, 0);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_CITY:
				cityArray = (String[]) msg.obj;
				initSpinner(cityArray, citySpinner, citySelect, 1);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_DISTRICT:
				districtArray = (String[]) msg.obj;
				initSpinner(districtArray, districtSpinner, districtSelect, 2);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_SET_NICKNAME:
				nicknameText.setText((String)msg.obj);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_SET_BIRTHDAY:
				birthdayText.setText((String)msg.obj);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_SET_PHONE:
				phoneText.setText((String)msg.obj);
				break;
			case ConstantValues.InstructionCode.USERSET_HANDLER_SET_SEX:
				sexText.setText((String)msg.obj);
				break;
			}
		};
	};
	
	OnClickListener imageSelectListener = new OnClickListener() 
	{
		@Override
		public void onClick(View arg0) 
		{
			new AlertDialog.Builder(ActivityUserBasicSetting.this).setTitle("请选择")
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
	};

	
	private void initTextView()
	{
		nicknameText = (TextView)findViewById(R.id.lj_nickname);
		nicknameText.setOnClickListener(new nicknameListener(ConstantValues.user, this));
		
		passwordText = (TextView)findViewById(R.id.lj_password);
		passwordText.setOnClickListener(new passwordListener(ConstantValues.user, this));
		
		birthdayText = (TextView)findViewById(R.id.lj_birthday);
		birthdayText.setOnClickListener(new birthdayListener(ConstantValues.user, this));
		
		provinceSpinner = (Spinner)findViewById(R.id.lj_province);
		citySpinner = (Spinner)findViewById(R.id.lj_city);
		districtSpinner = (Spinner)findViewById(R.id.lj_district);
		new ThreadGetProvinceArray(handle).start();
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) 
			{
				provinceSelect = provinceArray[index];
				new ThreadGetCityArray(handle, provinceSelect).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
		
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) 
			{
				citySelect = cityArray[index];
				new ThreadGetDistrictArray(handle, provinceSelect, citySelect).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
		
		districtSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3)
			{
				districtSelect = districtArray[index];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
			}
		});
		
		phoneText = (TextView)findViewById(R.id.lj_phone);
		phoneText.setOnClickListener(new phoneListener(ConstantValues.user, this));
		
		sexText = (TextView)findViewById(R.id.lj_sex);
		sexText.setOnClickListener(new sexListener(ConstantValues.user, this));
		
		portraitImageView = (ImageView)findViewById(R.id.lj_portraitImageView);
		portraitImageView.setOnClickListener(imageSelectListener);
	}
	

	private void initUserBasicSetting()
	{
		nicknameText.setText(ConstantValues.user.getNickName());
		passwordText.setText("password password");
		birthdayText.setText(ConstantValues.user.getBirthday());
		
		String home = ConstantValues.user.getHometown();
		hometown = home.split(" ");
		
		phoneText.setText(ConstantValues.user.getPhoneNumber());
		sexText.setText(ConstantValues.user.getSex());
		Bitmap bitmap = ConvertUtil.bytes2Bitmap(ConstantValues.user.getPortrait());
		portraitImageView.setAdjustViewBounds(true); 
		portraitImageView.setImageBitmap(bitmap);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_layout_userbasic_set);
		initTextView();
		initUserBasicSetting();
	}
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			portraitImageView.setImageBitmap(bitmap);
			new Thread()
			{
				public void run() 
				{
					ConstantValues.user.setPortrait(Environment.getExternalStorageDirectory() + "/Portrait.jpg");
				};
			}.start();
			bitmap = null;
		}
	}
}
