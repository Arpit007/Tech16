package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.Activity.Event;
import com.nitkkr.gawds.tech16.Adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class RegisteredEventList extends Fragment
{
	private ListView listView;
	private ArrayList<String> listDataChild;
	public RegisteredEventList()
	{

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		final View view= inflater.inflate(R.layout.fragment_registered_event_list, container, false);

		listView = (ListView) view.findViewById(R.id.registered_event_list);

		prepareListData();

		final EventListAdapter listAdapter = new EventListAdapter(view.getContext(), listDataChild);

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getCount()==0)
				{
					view.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else view.findViewById(R.id.None).setVisibility(View.INVISIBLE);
				super.onChanged();
			}
		});

		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				String Data=(String)(listView.getAdapter()).getItem(i);
				//TODO:Implemet================================

				view.getContext().startActivity(new Intent(view.getContext(), Event.class));
			}
		});

		return view;
	}

	void prepareListData()
	{
		listDataChild=new ArrayList<>();
		listDataChild.add("Hello");
		listDataChild.add("World");
	}
}
