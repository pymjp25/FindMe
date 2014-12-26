package com.example.findme.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	
	private int id;

	private String phoneNumber;
	
	private String email;
	
	private String password;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeInt(id);
		out.writeString(phoneNumber);
		out.writeString(email);
		out.writeString(password);
	}
	
	public User(){
		
	}
	
	private User(Parcel in){
		id=in.readInt();
		phoneNumber=in.readString();
		email=in.readString();
		password=in.readString();
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in){
			return new User(in);
		}
		
		public User[] newArray(int size){
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
