package com.example.findme.model;

public class CaseInfo {
	
	/**
     "id integer primary key autoincrement, " +
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
	
	private int id;
	
	private int userId;
	
	private int caseType;

    private int areaType;
	
	private String name;
	
	private String contact;
	
	private long publishTime;

    private int status;// 设置caseInfo状态，1-新发布，2-已完成，3-已放弃
	
	private int placeType;
	
	private String placeInfo;
	
	private String remark;
	
	private String bonus;

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

	public int getCaseType() {
		return caseType;
	}

	public void setCaseType(int caseType) {
		this.caseType = caseType;
	}

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	public int getPlaceType() {
		return placeType;
	}

	public void setPlaceType(int placeType) {
		this.placeType = placeType;
	}

	public String getPlaceInfo() {
		return placeInfo;
	}

	public void setPlaceInfo(String placeInfo) {
		this.placeInfo = placeInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
