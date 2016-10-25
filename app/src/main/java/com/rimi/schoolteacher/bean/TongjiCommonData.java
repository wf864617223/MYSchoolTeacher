package com.rimi.schoolteacher.bean;

/**
 * Created by Android on 2016/8/2.
 */
public class TongjiCommonData {
    private String rollcallStudentTime;

    private String rollcallStudentType;

    private Student student;

    public String getRollcallStudentTime() {
        return rollcallStudentTime;
    }

    public void setRollcallStudentTime(String rollcallStudentTime) {
        this.rollcallStudentTime = rollcallStudentTime;
    }

    public String getRollcallStudentType() {
        return rollcallStudentType;
    }

    public void setRollcallStudentType(String rollcallStudentType) {
        this.rollcallStudentType = rollcallStudentType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
