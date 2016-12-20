package com.nitkkr.gawds.tech16.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventKey implements Serializable
{
	private String Name;
	private int EventID;
	private boolean Notify;
	private int Society=0;

	//Change ID from int to String

	public int getSocietyId(){return Society;}
	public int getEventID()
	{
		return EventID;
	}
	public String getEventName()
	{
		return Name;
	}

	public void setSocietyId(int societyId){this.Society = societyId;}
	public void setEventID(int ID)
	{
		this.EventID = ID;
	}
	public void setEventName(String name)
	{
		Name = name;
	}
	public void setNotify(boolean notify)
	{
		Notify = notify;
	}

	public boolean isNotify()
	{
		return Notify;
	}

	public EventKey(){}
	public EventKey(@NonNull  String name, int id, boolean notify)
	{
		Name = name;
		EventID=id;
		Notify=notify;
	}
}
