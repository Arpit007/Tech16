package com.nitkkr.gawds.tech16.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.R;

public class About extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		ActionBarBack actionBarBack=new ActionBarBack(About.this);
		actionBarBack.setLabel("About");
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(About.this))
			return;
		super.onBackPressed();
	}
}