package com.nitkkr.gawds.tech17.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.model.SocietyModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Home Laptop on 19-Dec-16.
 */

public class SocietyDB extends SQLiteOpenHelper implements iBaseDB
{
	private iDbRequest dbRequest;
	private Context context;

	public SocietyDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.context = context;
		this.dbRequest = dbRequest;

		if (DbConstants.Constants == null)
		{
			DbConstants.Constants = new DbConstants(context);
		}

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.Query_Create_SocietyTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getSocietyTableName());
	}

	public String getSocietyName(SocietyModel model)
	{
		return getSocietyName(model.getID());
	}

	public String getSocietyName(int ID)
	{
		String Name = "";

		String Query = "SELECT " + DbConstants.SocietyNames.SocietyName.Name() + " FROM " + DbConstants.Constants.getSocietyTableName() + " WHERE " +
				DbConstants.SocietyNames.Id.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.SocietyNames.SocietyName.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				Name = cursor.getString(ColumnIndex[0]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return Name;
	}

	public ArrayList<SocietyModel> getSocieties(String Clause)
	{
		ArrayList<SocietyModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getSocietyTableName();
		if (Clause.equals(""))
		{
			Query += ";";
		}
		else
		{
			Query += " WHERE " + Clause + ";";
		}
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.SocietyNames.Id.Name()),
							Columns.indexOf(DbConstants.SocietyNames.SocietyName.Name()),
							Columns.indexOf(DbConstants.SocietyNames.Description.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					SocietyModel society = new SocietyModel();
					society.setID(cursor.getInt(ColumnIndex[0]));
					society.setName(cursor.getString(ColumnIndex[1]));
					society.setDescription(cursor.getString(ColumnIndex[2]));
					keys.add(society);
				}
				while (cursor.moveToNext());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return keys;
	}

	public ArrayList<SocietyModel> getAllSocieties()
	{
		return getSocieties("");
	}

	public HashMap<Integer, String> getAllSocietiesHash(String Clause)
	{
		return getSocietiesHash("");
	}

	public HashMap<Integer, String> getSocietiesHash(String Clause)
	{
		HashMap<Integer, String> keys = new HashMap<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getSocietyTableName();
		if (Clause.equals(""))
		{
			Query += ";";
		}
		else
		{
			Query += " WHERE " + Clause + ";";
		}
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.SocietyNames.Id.Name()),
							Columns.indexOf(DbConstants.SocietyNames.SocietyName.Name()),
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					keys.put(cursor.getInt(ColumnIndex[0]), cursor.getString(ColumnIndex[1]));
				}
				while (cursor.moveToNext());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return keys;
	}

	public void deleteSociety(SocietyModel society)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getSocietyTableName() + " WHERE " + DbConstants.SocietyNames.Id.Name() + " = " + society.getID() + ";";
		Log.d("Query:\t", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public String getTableName()
	{
		return DbConstants.Constants.getSocietyTableName();
	}

	public void addOrUpdateSociety(SocietyModel society)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();
		values.put(DbConstants.SocietyNames.Id.Name(), society.getID());
		values.put(DbConstants.SocietyNames.SocietyName.Name(), society.getName());
		values.put(DbConstants.SocietyNames.Description.Name(), society.getDescription());

		if (database.update(DbConstants.Constants.getSocietyTableName(), values, DbConstants.SocietyNames.Id.Name() + " = " + society.getID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getSocietyTableName(), null, values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateSocities(ArrayList<SocietyModel> societies)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getSocietyTableName();
		String Society_Id = DbConstants.SocietyNames.Id.Name();
		String Society_Name = DbConstants.SocietyNames.SocietyName.Name();
		String Society_Desc = DbConstants.SocietyNames.Description.Name();

		for (SocietyModel society : societies)
		{
			ContentValues values = new ContentValues();
			values.put(Society_Id, society.getID());
			values.put(Society_Name, society.getName());
			values.put(Society_Desc, society.getDescription());

			if (database.update(TABLENAME, values, Society_Id + " = " + society.getID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	@Override
	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getSocietyTableName());
	}

	public HashMap<Integer, String> societyArrayToHash(ArrayList<SocietyModel> models)
	{
		HashMap<Integer, String> map = new HashMap<>();
		for (SocietyModel model : models)
		{
			map.put(model.getID(), model.getName());
		}
		return map;
	}

	@Override
	public void resetTable()
	{
	}

	@Override
	public void deleteTable()
	{
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getSocietyTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);
	}
}