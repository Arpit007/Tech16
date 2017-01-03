package com.nitkkr.gawds.tech17.model;

/**
 * Created by Home Laptop on 16-Nov-16.
 */

public enum MessageType
{
	SIMPLE_MESSAGE("Text"),
	EVENT("Event"),
	TEAM_INVITE("Invite");

	private String value;

	MessageType(String t)
	{
		value = t;
	}

	String getValue()
	{
		return value;
	}
}
