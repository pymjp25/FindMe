package com.example.findme.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FindMeOpenHelper extends SQLiteOpenHelper {

	// User表
	public static final String CREATE_USER = "create table User(" +
			"id integer primary key autoincrement, " +
			"phone_number text, " +
			"email text, " +
			"password text)";
	
	//UserInfo表
	public static final String CREATE_USERINFO = "create table UserInfo(" +
			"id integer primary key autoincrement, " +
			"user_id integer," +
			"name text, " +
			"birthday integer, " +
			"sex text, " +
			"phone_number text, " +
			"email text)";
	
	//创建Case表
	public static final String CREATE_CASEINFO = "create table CaseInfo(" +
            "id integer primary key autoincrement, " +
            "user_id integer," +
			"case_type integer, " +
            "area_type integer, " +
			"name text, " +
			"contact text, " +
			"publish_time integer, " +
            "status integer, " +
			"place_type integer, " +
			"place_info text, " +
			"remark text, " +
			"bonus text)";

    // 创建省级列表
    public static final String CREATE_AREA = "create table Area("+
            "id integer primary key autoincrement, " +
            "area_father_code integer, " +
            "area_name text, " +
            "area_self_code integer)";

	public FindMeOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER);//创建User表
		db.execSQL(CREATE_USERINFO);
		db.execSQL(CREATE_CASEINFO);
        db.execSQL(CREATE_AREA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch(oldVersion){
		case 1:
        case 2:
		default:
		}
	}

}
