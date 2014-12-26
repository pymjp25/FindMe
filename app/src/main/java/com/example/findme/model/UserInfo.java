package com.example.findme.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable{

	private int id;
	
	private int userId;
	
	private String name;
	
	private long birthday;
	
	private String sex;
	
	private String phoneNumber;
	
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeInt(id);
		out.writeInt(userId);
		out.writeString(email);
		out.writeString(name);
		out.writeString(phoneNumber);
		out.writeString(sex);
		out.writeLong(birthday);
	}
	
	public UserInfo(){
		
	}
	
	public UserInfo(Parcel in){
		this.id=in.readInt();
		this.userId=in.readInt();
		this.email=in.readString();
		this.name=in.readString();
		this.phoneNumber=in.readString();
		this.sex=in.readString();
		this.birthday=in.readLong();
	}
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
		
		@Override
		public UserInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserInfo[size];
		}
		
		@Override
		public UserInfo createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new UserInfo(in);
		}
	};
	
}
