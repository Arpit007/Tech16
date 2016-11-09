package com.nitkkr.gawds.tech16.Model;

import java.util.Date;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class BaseEventModel
{
	private Date EventDate;
	private String Venue;
	private String Description;
	private String EventName;
	private String Image_URL;
	private boolean Notify;

	public Date getEventDate(){return EventDate;}
	public String getVenue(){return Venue;}
	public String getDescription(){return Description;}
	public String getEventName(){return EventName;}
	public String getImage_URL(){return Image_URL;}

	public boolean isNotify(){return Notify;}

	public void setEventName(String name){EventName=name;}
	public void setEventDate(Date date){EventDate=date;}
	public void setVenue(String venue){Venue=venue;}
	public void setDescription(String description){Description=description;}
	public void setImage_URL(String image_URL){Image_URL=image_URL;}
	public void setNotify(boolean notify){Notify=notify;}
}