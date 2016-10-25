package com.rimi.schoolteacher.https;

/**
 * Created by Android on 2016/7/21.
 */
public class HttpUrls {
//    private static final String TEST_URL = "http://192.168.10.3/api/";
    private static final String TEST_URL = "http://do.rimiedu.com/wxjy/api/";
//private static final String TEST_URL = "http://192.168.6.65:8080/api/";
    //登录
    public static final String LOGIN = TEST_URL + "staff/login.html";
    //签到相关
    public static final String SIGN_ABOUT = TEST_URL + "staff/saveRollcallStaff.html";
    //创建班级
    public static final String CREATE_CLASS = TEST_URL + "staff/saveClass.html";
    //班主任获取学生的请假信息列表
    public static final String GET_STAFF_LIST = TEST_URL + "staff/getStaffList.html";
    //班主任处理学生请假信息
    public static final String DEAL_ASK_LEAVE = TEST_URL + "staff/dealAskLeave.html";
    //get班级列表
    public static final String GET_CLASS_LIST = TEST_URL + "staff/getClassList.html";
    //保存教师签到信息
    public static final String SAVE_TEACHER_SIGN_DATA = TEST_URL + "staff/saveRollcallTeacher.html";
    //get学生列表
    public static final String GET_STUDENT_LIST = TEST_URL + "staff/getStudentList.html";
    //教师获取课程列表信息
    public static final String TEACHER_GET_CLASS_LIST = TEST_URL + "staff/getCurriculumList.html";
    //教师获取学生考勤结果列表信息
    public static final String GET_KAOQIN_LIST = TEST_URL + "staff/getStuRollcallList.html";
    //删除班级信息
    public static final String DELETE_CLASS = TEST_URL + "staff/deleteClass.html";
    //删除学生信息
    public static final String DELETE_STUDENT_DATA = TEST_URL + "staff/deleteStudent.html";
    //删除老师信息
    public static final String DELETE_TEACHER_DATA = TEST_URL + "staff/deleteStaff.html";
    //获取通知信息
    public static final String GET_NOTIFY_DATA = TEST_URL + "notice/getNoticeList.html";
    //判断当前是否可以签到
    public static final String JUDGE_IS_SIGN = TEST_URL + "staff/ifSignIn.html";
    //判断当前是否可以签退
    public static final String JUDGE_IS_SIGNOUT = TEST_URL + "staff/ifSignOff.html";
    //上传头像
    public static final String UPLOAD_HEAD = TEST_URL + "staff/setStaffPicture.html";
    //获取办公考勤地点
    public static final String GET_KAOQIN_DATA = TEST_URL + "staff/attendance.html";
    //获取外出办公考勤地点
    public static final String GET_OUTSIDE_KAOQIN_DATA = TEST_URL + "staff/outAttendance.html";
    //修改密码
    public static final String CHANGE_PASSWORD = TEST_URL + "staff/changePassword.html";
    //搜索学生
    public static final String SEARCH_STUDENT = TEST_URL + "staff/getStudentByName.html";
    //about US
    public static final String ABOUT_US = TEST_URL + "aboutus/getAboutUs.html";
    //同步推送TOKEN
    public static final String SYNCH_PUSH_TOKEN = TEST_URL + "staff/token.html";
    //获取考勤误差信息
    public static final String GET_ERROR_DISTANCE = TEST_URL + "distance/getDistance.html";
}
