package com.example.findme.db;

import com.example.findme.model.Area;
import com.example.findme.model.CaseInfo;
import com.example.findme.model.User;
import com.example.findme.model.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FindMeDB {

	// 数据库名
	public static final String DB_NAME = "find_me";

	// 数据库版本
	public static final int VERSION = 1;

	private static FindMeDB findMeDB;

	private SQLiteDatabase db;

	private User user;

	// 获取数据库对象
	private FindMeDB(Context context) {
		FindMeOpenHelper dbHelper = new FindMeOpenHelper(context, DB_NAME,
				null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 初始化数据库
	 */
	public synchronized static FindMeDB getInstance(Context context) {
		if (findMeDB == null) {
			findMeDB = new FindMeDB(context);
		}
		return findMeDB;
	}

	/**
	 * 根据传入账号读取数据库中User信息
	 */
	public User loadUser(String account) {
		user = null;
		Cursor cursor = db.query("User", null, "phone_number=?",
				new String[] { account }, null, null, null);
		if (cursor.getCount() == 0) {
			cursor = db.query("User", null, "email=?",
					new String[] { account }, null, null, null);
			if (cursor.getCount() == 0) {
				return user;
			} else {
				user = new User();
				if (cursor.moveToFirst()) {
					user.setId(cursor.getInt(cursor.getColumnIndex("id")));
					user.setPhoneNumber(cursor.getString(cursor
							.getColumnIndex("phone_number")));
					user.setEmail(cursor.getString(cursor
							.getColumnIndex("email")));
					user.setPassword(cursor.getString(cursor
							.getColumnIndex("password")));
				}
			}
		} else {
			user = new User();
			if (cursor.moveToFirst()) {
				user.setId(cursor.getInt(cursor.getColumnIndex("id")));
				user.setPhoneNumber(cursor.getString(cursor
						.getColumnIndex("phone_number")));
				user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
				user.setPassword(cursor.getString(cursor
						.getColumnIndex("password")));
			}
		}
		return user;
	}

	/**
	 * 根据传入userId读取数据库中User信息
	 *
	 */
	public User loadUser(int userId) {
		user = null;
		Cursor cursor = db.query("User", null, "id=?",
				new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor.moveToFirst()) {
			user = new User();
			user.setId(userId);
			user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			user.setPhoneNumber(cursor.getString(cursor
					.getColumnIndex("phone_number")));
			return user;//如果有就返回存在的user
		} else {
			return user;// 没有返回null
		}
	}

	/**
	 * 新建User
	 */
	public boolean createUser(User user) {
		if (user != null) {
			ContentValues values = new ContentValues();
			values.put("phone_number", user.getPhoneNumber());
			values.put("email", user.getEmail());
			values.put("password", user.getPassword());
			db.insert("User", null, values);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新User
	 */
	public boolean updateUser(User user) {
		if (user == null) {
			return false;
		} else {
			ContentValues values = new ContentValues();
			values.put("phone_number", user.getPhoneNumber());
			values.put("password", user.getPassword());
			values.put("email", user.getEmail());
			db.update("User", values, "id=?", new String[] { String.valueOf(user.getId()) });
			return true;
		}
	}

	/**
	 * 新建UserInfo
	 */
	public boolean createUserInfo(UserInfo userInfo) {
		ContentValues values = new ContentValues();
		values.put("user_id", userInfo.getUserId());
		values.put("name", userInfo.getName());
		values.put("birthday", String.valueOf(userInfo.getBirthday()));
		values.put("sex", userInfo.getSex());
		values.put("phone_number", userInfo.getPhoneNumber());
		values.put("email", userInfo.getEmail());
		db.insert("UserInfo", null, values);
		return true;
	}

	/**
	 * 根据传入的userId读取UserInfo信息
	 * 
	 */
	public UserInfo loadUserInfo(int userId) {
		UserInfo userInfo = null;
		Cursor cursor = db.query("UserInfo", null, "user_id=?",
				new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor.moveToFirst()) {
			userInfo = new UserInfo();
			userInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			userInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
			userInfo.setBirthday(cursor.getLong(cursor
					.getColumnIndex("birthday")));
			userInfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			userInfo.setPhoneNumber(cursor.getString(cursor
					.getColumnIndex("phone_number")));
			userInfo.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			userInfo.setSex(cursor.getString(cursor.getColumnIndex("sex")));
		}
		return userInfo;
	}

	/**
	 * 更新UserInfo
	 */
	public boolean updateUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return false;
		} else {
			ContentValues values = new ContentValues();
			values.put("user_id", userInfo.getUserId());
			values.put("name", userInfo.getName());
			values.put("birthday", userInfo.getBirthday());
			values.put("sex", userInfo.getSex());
			values.put("phone_number", userInfo.getPhoneNumber());
			values.put("email", userInfo.getEmail());
			db.update("UserInfo", values, "user_id=?", new String[]{String.valueOf(userInfo.getUserId())});
			return true;
		}
	}

    /**
     * 保存全国各级省市区数据到数据库
     */
    public boolean saveArea(Area area) {
        if( area != null ){
            ContentValues values = new ContentValues();
            values.put("area_father_code", area.getAreaFatherCode());
            values.put("area_name", area.getAreaName());
            values.put("area_self_code", area.getAreaSelfCode());
            db.insert("Area", null, values);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 从数据库中读取全国各级省市区列表数据
     */
    public List<Area> loadAreaList(int areaCode){
        List<Area> list = new ArrayList<Area>();
        Cursor cursor = db.query("Area", null, "area_father_code=?",
                new String[]{String.valueOf(areaCode)}, null, null, null);
        if( cursor.moveToFirst() ){
            do{
                Area area = new Area();
                area.setId(cursor.getInt(cursor.getColumnIndex("id")));
                area.setAreaFatherCode(cursor.getInt(cursor.getColumnIndex("area_father_code")));
                area.setAreaName(cursor.getString(cursor.getColumnIndex("area_name")));
                area.setAreaSelfCode(cursor.getInt(cursor.getColumnIndex("area_self_code")));
                list.add(area);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库中读取地区数据
     */
    public Area loadArea(int areaCode){
        Area area = new Area();
        Cursor cursor = db.query("Area", null, "area_self_code=?",
                new String[]{String.valueOf(areaCode)}, null, null, null);
        if( cursor.moveToFirst() ){
                area.setId(cursor.getInt(cursor.getColumnIndex("id")));
                area.setAreaFatherCode(cursor.getInt(cursor.getColumnIndex("area_father_code")));
                area.setAreaName(cursor.getString(cursor.getColumnIndex("area_name")));
                area.setAreaSelfCode(cursor.getInt(cursor.getColumnIndex("area_self_code")));
        }
        return area;
    }

    /**
     * 创建caseInfo
     * "id integer primary key autoincrement, " +
     "user_id integer," +
     "case_type integer, " +
     "area_type integer, " +
     "name text, " +
     "contact text, " +
     "publish_time integer, " +
     "place_type integer, " +
     "place_info text, " +
     "remark text, " +
     "bonus text)"
     */
    public boolean createCaseInfo(CaseInfo caseInfo){
        if( caseInfo != null ){
            ContentValues values = new ContentValues();
            values.put("user_id", caseInfo.getUserId());
            values.put("case_type", caseInfo.getCaseType());
            values.put("area_type", caseInfo.getAreaType());
            values.put("name", caseInfo.getName());
            values.put("contact", caseInfo.getContact());
            values.put("publish_time", caseInfo.getPublishTime());
            values.put("status", caseInfo.getStatus());
            values.put("place_type", caseInfo.getPlaceType());
            values.put("place_info", caseInfo.getPlaceInfo());
            values.put("remark", caseInfo.getRemark());
            values.put("bonus", caseInfo.getBonus());
            db.insert("CaseInfo", null, values);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据传入的user_id返回该用户关注的caseInfo
     */
    public List<CaseInfo> loadCaseInfo(int userId){
        List<CaseInfo> list = new ArrayList<CaseInfo>();
        Cursor cursor = db.query("CaseInfo", null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null, null);
        if( cursor.moveToFirst() ){
            do{
                CaseInfo caseInfo = new CaseInfo();
                caseInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                caseInfo.setPlaceInfo(cursor.getString(cursor.getColumnIndex("place_info")));
                caseInfo.setAreaType(cursor.getInt(cursor.getColumnIndex("area_type")));
                caseInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                caseInfo.setBonus(cursor.getString(cursor.getColumnIndex("bonus")));
                caseInfo.setPlaceType(cursor.getInt(cursor.getColumnIndex("place_type")));
                caseInfo.setCaseType(cursor.getInt(cursor.getColumnIndex("case_type")));
                caseInfo.setContact(cursor.getString(cursor.getColumnIndex("contact")));
                caseInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                caseInfo.setPublishTime(cursor.getLong(cursor.getColumnIndex("publish_time")));
                caseInfo.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                caseInfo.setUserId(userId);
                list.add(caseInfo);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
