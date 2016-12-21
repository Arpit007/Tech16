package com.nitkkr.gawds.tech16.helper1;

import android.content.Context;

import com.nitkkr.gawds.tech16.model1.MessageModel;
import com.nitkkr.gawds.tech16.model1.MessageType;
import com.nitkkr.gawds.tech16.src1.MessageEvent;
import com.nitkkr.gawds.tech16.src1.MessageInvite;
import com.nitkkr.gawds.tech16.src1.MessageResult;
import com.nitkkr.gawds.tech16.src1.MessageSimple;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageHanlder
{
	public void DispatchMessage(String message, Context context)
	{
		MessageModel formattedMessage=getFormattedMessage(message);
		//TODO:Set Model Fields here=======================

		switch(formattedMessage.getType())
		{
			case EVENT:
				new MessageEvent().performAction(formattedMessage, context);
				break;
			case TEAM_INVITE:
				new MessageInvite().performAction(formattedMessage, context);
				break;
			case EVENT_RESULT:
				new MessageResult().performAction(formattedMessage, context);
				break;
			case SIMPLE_MESSAGE:
				new MessageSimple().performAction(formattedMessage, context);
				break;
		}
	}

	public MessageType getMessageType(String message)
	{
		//TODO: Implement
		return MessageType.EVENT;
	}

	public MessageModel getFormattedMessage(String message)
	{
		switch(getMessageType(message))
		{
			case EVENT:
				return new MessageEvent().formatMessage(message);
			case TEAM_INVITE:
				return new MessageInvite().formatMessage(message);
			case EVENT_RESULT:
				return new MessageResult().formatMessage(message);
			case SIMPLE_MESSAGE:
				return new MessageSimple().formatMessage(message);
		}
		return null;
	}
}