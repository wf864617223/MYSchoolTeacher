package com.rimi.schoolteacher.bean;

import java.io.Serializable;

/**
 * Created by Android on 2016/7/25.
 */
public class ClassData implements Serializable{

    private String classId;

    private String className;

    private String classSum;

    private String professionId;

    private String staffId;

    private Object staffList;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSum() {
        return classSum;
    }

    public void setClassSum(String classSum) {
        this.classSum = classSum;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Object getStaffList() {
        return staffList;
    }

    public void setStaffList(Object staffList) {
        this.staffList = staffList;
    }
}
