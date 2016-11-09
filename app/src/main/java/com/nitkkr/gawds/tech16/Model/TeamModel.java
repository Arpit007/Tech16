package com.nitkkr.gawds.tech16.Model;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class TeamModel implements iUserModel
{
	private String TeamName;
	private ArrayList<UserModel> Members;

	public String getTeamName(){return TeamName;}
	public ArrayList<UserModel> getMembers(){return Members;}

	public void setTeamName(String teamName){TeamName=teamName;}
	public void setMembers(ArrayList<UserModel> members){Members=members;}
}