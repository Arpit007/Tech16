package com.nitkkr.gawds.tech16.activity.Fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.activity.Event;
import com.nitkkr.gawds.tech16.adapter.AllEventListAdapter;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.database.DbConstants;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.SocietyModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventList extends Fragment
{
	View MyView;
	ExpandableListView expListView;
	HashMap<String, ArrayList<EventKey>> HashData;
	AllEventListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		MyView= inflater.inflate(R.layout.fragment_all_event_list, container, false);

		expListView = (ExpandableListView) MyView.findViewById(R.id.all_event_list);

		prepareListData();

		final ListView listView=(ListView)MyView.findViewById(R.id.search_event_list);

		listAdapter = new AllEventListAdapter(MyView.getContext(), HashData);
		listAdapter.getFilter().setSearchList(listView);

		listView.getAdapter().registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listView.getVisibility()==View.VISIBLE && listView.getAdapter().getCount()==0)
					MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
				else MyView.findViewById(R.id.None).setVisibility(View.GONE);

				super.onChanged();
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Bundle bundle=new Bundle();
				bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
				Intent intent=new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
			}
		});

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getGroupCount()==0)
				{
					MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else MyView.findViewById(R.id.None).setVisibility(View.GONE);
				super.onChanged();
			}
		});

		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
			{
				Bundle bundle=new Bundle();
				bundle.putSerializable("Event",(EventKey)(listAdapter.getChild(i,i1)));

				Intent intent=new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
				return false;
			}
		});

		return MyView;
	}

	private void prepareListData()
	{
		HashData = new HashMap<>();
		ArrayList<SocietyModel> societies= Database.database.getSocietyDB().getAllSocieties();

		for(SocietyModel society: societies)
		{
			HashData.put(society.getName(),Database.database.getEventsDB().getEventKeys(DbConstants.EventNames.Society.Name() + " = " + society.getID()));
		}
	}

	public void SearchQuery(String Query)
	{
		if(Query.equals(""))
		{
			MyView.findViewById(R.id.None).setVisibility(View.GONE);
			MyView.findViewById(R.id.all_event_list).setVisibility(View.VISIBLE);
			MyView.findViewById(R.id.search_event_list).setVisibility(View.GONE);
		}
		else
		{
			MyView.findViewById(R.id.all_event_list).setVisibility(View.GONE);
			MyView.findViewById(R.id.search_event_list).setVisibility(View.VISIBLE);
			listAdapter.getFilter().filter(Query);
		}
	}
}
