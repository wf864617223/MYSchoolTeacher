package com.rimi.schoolteacher.bean;

/**
 * Created by Android on 2016/7/25.
 */
public class InsideTeacherList {
    private int staffId;

    private String staffIsinpost;

    private String staffName;

    private String staffSex;

    private String staffTelphone;

    private Object curriculum;

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffIsinpost() {
        return staffIsinpost;
    }

    public void setStaffIsinpost(String staffIsinpost) {
        this.staffIsinpost = staffIsinpost;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffSex() {
        return staffSex;
    }

    public void setStaffSex(String staffSex) {
        this.staffSex = staffSex;
    }

    public String getStaffTelphone() {
        return staffTelphone;
    }

    public void setStaffTelphone(String staffTelphone) {
        this.staffTelphone = staffTelphone;
    }

    public Object getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Object curriculum) {
        this.curriculum = curriculum;
    }
}
