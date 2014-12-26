package com.example.findme.model;

/**
 * Created by Mark on 2014/12/21.
 */
public class Area {

    private int id;

    private int areaFatherCode;

    private String areaName;

    private int areaSelfCode;

    public int getId() {
        return id;
    }

    public int getAreaFatherCode() {
        return areaFatherCode;
    }

    public int getAreaSelfCode() {
        return areaSelfCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaFatherCode(int areaFatherCode) {
        this.areaFatherCode = areaFatherCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setAreaSelfCode(int areaSelfCode) {
        this.areaSelfCode = areaSelfCode;
    }

    public void setId(int id) {
        this.id = id;
    }
}
