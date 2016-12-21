package com.nitkkr.gawds.tech16.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nitkkr.gawds.tech16.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Home Laptop on 16-Dec-16.
 */

public class AvatarAdapter extends BaseAdapter
{
	private TypedArray array;
	private Context context;

	public AvatarAdapter(Context context)
	{
		array=context.getResources().obtainTypedArray(R.array.Avatar);
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return array.length();
	}

	@Override
	public Object getItem(int i)
	{
		return null;
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		if(view==null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.layout_avatar, null);
		}
		CircleImageView CircleImageView=( CircleImageView)view.findViewById(R.id.avatar_image);
		CircleImageView.setImageResource(array.getResourceId(i,0));

		return view;
	}

	@Override
	protected void finalize() throws Throwable
	{
		array.recycle();
		super.finalize();
	}
}
