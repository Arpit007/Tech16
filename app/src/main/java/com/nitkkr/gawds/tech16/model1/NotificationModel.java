package com.nitkkr.gawds.tech16.model1;

import java.util.Date;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class NotificationModel extends EventKey
{
	private boolean generated=false;
	private long date;

	public void setGenerated(boolean generated){this.generated = generated;}
	public void setDate(long date){this.date = date;}

	public boolean isGenerated(){return generated;}
	public long getDate(){return date;}
	public Date getDateObject()
	{
		return new Date(date);
	}
}