package com.ui;

import java.util.Calendar;

import com.commons.CommonUtil;
import com.example.testmobiledatabase.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity
{
	private int userID;
	private String loginAccount;
	private String password;
	private String nickName;
	private String email;
	private String sex;
	private String phoneNumber;
	
	private String birthday;
	private String portrait;
	private String hometownID;
	
	private TextView tv;
    private Spinner cities;
    private Spinner proviences;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> provienceAdapter;
    
    private Hometown hometown;
	

    private void initLayout()
    {
    	tv = (TextView)findViewById(R.id.pwd);
    }
    
    private void initData()
    {
    	hometown = new Hometown();
    	hometown.provinces = new String [] {"Select"};
    	hometown.cities = new String [] {"Select"};
    	hometown.districts = new String [] {"Select"};
    }
    
    private void setupListeners()
    {
    	tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				date();
			}
		});
    }
    
    private void setupAdapter()
    {
    	cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CommonUtil.getProvienceList());
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cities = (Spinner) findViewById(R.id.cities);
        cities.setAdapter(cityAdapter);
        cities.setOnItemSelectedListener(new citySelectedListener());
        cities.setVisibility(View.VISIBLE);
        
        provienceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CommonUtil.getProvienceList());
        provienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proviences = (Spinner) findViewById(R.id.proviences);
		proviences.setAdapter(provienceAdapter);
        proviences.setOnItemSelectedListener(new provienceSelectedListener());
        proviences.setVisibility(View.VISIBLE);
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
	
	private class provienceSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Toast.makeText(Register.this, String.format("Pro: %d-%d", arg2, arg3), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private class citySelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Toast.makeText(Register.this, String.format("City: %d-%d", arg2, arg3), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
    }
	
	private void date()
	{
		OnDateSetListener d = new OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
			{
				Toast.makeText(Register.this, String.format("%d-%d-%d", arg1, arg2, arg3), Toast.LENGTH_SHORT).show();
			}
		};
		
		new DatePickerDialog(Register.this,
                d,
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	private class Hometown
	{
		public String provinces [];
		public String cities [];
		public String districts [];
	}
}