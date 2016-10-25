package com.rimi.schoolteacher.bean;

/**
 * Created by Android on 2016/7/25.
 */
public class StudentListData {
    private String classId;

    private InsideStudentsList scoll;

    private String studentId;

    private String studentName;

    private String studentSex;

    private String studentTelphone;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public InsideStudentsList getScoll() {
        return scoll;
    }

    public void setScoll(InsideStudentsList scoll) {
        this.scoll = scoll;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public String getStudentTelphone() {
        return studentTelphone;
    }

    public void setStudentTelphone(String studentTelphone) {
        this.studentTelphone = studentTelphone;
    }
}
