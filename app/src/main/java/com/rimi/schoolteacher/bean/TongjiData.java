package com.rimi.schoolteacher.bean;

/**
 * Created by Android on 2016/8/2.
 */
public class TongjiData {
    private String curriculumEndTime;

    private String curriculumId;

    private String curriculumName;

    private String curriculumStartTime;

    private String teacherName;

    private int sum;

    private int rollcall;

    private int late;

    private int ask;

    private int leaveEarly;

    private int absent;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getRollcall() {
        return rollcall;
    }

    public void setRollcall(int rollcall) {
        this.rollcall = rollcall;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public int getAsk() {
        return ask;
    }

    public void setAsk(int ask) {
        this.ask = ask;
    }

    public int getLeaveEarly() {
        return leaveEarly;
    }

    public void setLeaveEarly(int leaveEarly) {
        this.leaveEarly = leaveEarly;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public String getCurriculumEndTime() {
        return curriculumEndTime;
    }

    public void setCurriculumEndTime(String curriculumEndTime) {
        this.curriculumEndTime = curriculumEndTime;
    }

    public String getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getCurriculumStartTime() {
        return curriculumStartTime;
    }

    public void setCurriculumStartTime(String curriculumStartTime) {
        this.curriculumStartTime = curriculumStartTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
