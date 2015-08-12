package com.example.timet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "tt.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_MG= "mg_table";
	public static final String TABLE_EM = "em_table";
	public static final String TABLE_EM_DATA = "em_data_table";
	public static final String ID = "id";
	public static final String MG = "mg";
	public static final String ID_MG = "idmg";
	public static final String ID_EM = "idem";
	public static final String NAME_SURNAME = "ns";
	public static final String POCITON = "poc";
	public static final String DATE = "dateem";
	public static final String TIME_ARR = "arr";
	public static final String TIME_DEP = "dep";

	private static final String SQL_CREATE_MG = "CREATE TABLE "
			+ TABLE_MG + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ MG + " VARCHAR(255));";
	private static final String SQL_CREATE_EM = "CREATE TABLE "
			+ TABLE_EM + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ NAME_SURNAME + " VARCHAR(255),"+ ID_MG + " VARCHAR(255),"
			+ POCITON + " VARCHAR(255));";
	private static final String SQL_CREATE_EM_DATA = "CREATE TABLE "
			+ TABLE_EM_DATA + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ID_EM + " VARCHAR(255)," 
			+ DATE + " VARCHAR(255)," 
			+ TIME_ARR + " VARCHAR(255)," 
			+ TIME_DEP + " VARCHAR(255));";

	private static final String SQL_DELETE_MG = "DROP TABLE IF EXISTS "
			+ TABLE_MG;
	private static final String SQL_DELETE_EM = "DROP TABLE IF EXISTS "
			+ TABLE_EM;
	private static final String SQL_DELETE_EM_DATA = "DROP TABLE IF EXISTS "
			+ TABLE_EM_DATA;

	public DB(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_MG);
		db.execSQL(SQL_CREATE_EM);
		db.execSQL(SQL_CREATE_EM_DATA);
	}
	
	public void onDelete(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_MG);
		db.execSQL(SQL_DELETE_EM);
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion
				+ " до версии " + newVersion + ", которое удалит все старые данные");
		//onCreate(db);
	}


}
