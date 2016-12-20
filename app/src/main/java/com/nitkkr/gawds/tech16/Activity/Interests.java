package com.nitkkr.gawds.tech16.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Adapter.InterestAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.ResponseStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interests extends AppCompatActivity
{
	private AppUserModel appUserModel=(AppUserModel)AppUserModel.MAIN_USER.clone();
	private InterestAdapter adapter;
	private ProgressDialog mProgressDialog;
	String token,interests_post_data;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interests);

		ListView listView = (ListView) findViewById(R.id.interest_list);
		adapter = new InterestAdapter(getBaseContext());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				adapter.onItemClick(view, i);
			}
		});

		listView.setAdapter(adapter);

		ActionBarDoneButton barDone = new ActionBarDoneButton(this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (adapter.isDone())
				{
					appUserModel.setInterests(adapter.getFinalList());

					//Used for Edit User
					if(getIntent().getBooleanExtra("Return_Interest",false))
					{
						Intent data=new Intent();
						Bundle bundle=new Bundle();
						bundle.putSerializable("Interests",adapter.getFinalList());
						data.putExtras(bundle);
						setResult(RESULT_OK,data);
						finish();
						return;
					}

					interests_post_data="[";
					//TODO: Send Info
					ArrayList<String> s=appUserModel.getSelectedInterests();
					for(int i=0;i<s.size()-1;i++)
					{
						interests_post_data+='"'+s.get(i)+'"'+",";
					}
					interests_post_data+='"'+s.get(s.size()-1)+'"'+"]";
					sendInterests();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Select minimum 1 Interest", Toast.LENGTH_LONG).show();
				}
			}
		});

		barDone.setLabel("Interests");
		token=AppUserModel.MAIN_USER.getToken();
	}

	public void sendInterests()
	{
		showProgressDialog("Uploading Information, Please Wait...");
		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.get_user_interests_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						try
						{
							response = new JSONObject(res);

							if(response.getJSONObject("status").getInt("code")==200)
							{
								serverResponse(ResponseStatus.SUCCESS);
							}
							else
							{
								serverResponse(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							Toast.makeText(Interests.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

						Toast.makeText(Interests.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(Interests.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",token);
				params.put("interests",interests_post_data);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);

	}

	public void serverResponse(ResponseStatus status){
		switch (status)
		{
			case SUCCESS:
				appUserModel.saveAppUser(Interests.this);
				AppUserModel.MAIN_USER=appUserModel;

				SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
				editor.putBoolean("Skip",false);
				editor.apply();

				if(!ActivityHelper.isDebugMode(getApplicationContext()))
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
				}
				if(getIntent().getExtras().getBoolean("Start_Home",true))
					startActivity(new Intent(Interests.this, Home.class));
				else
				{
					Intent intent=new Intent();
					intent.putExtra("Logged_In",true);
					setResult(RESULT_OK,intent);
				}
				finish();
				break;
			case FAILED:
				Toast.makeText(Interests.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
				break;
			case OTHER:
				Toast.makeText(Interests.this,"Network error",Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
	}

	private void showProgressDialog(String msg) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(msg);
			mProgressDialog.setIndeterminate(true);
		}

		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
}
