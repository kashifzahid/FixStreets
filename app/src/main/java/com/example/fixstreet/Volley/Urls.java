package com.example.fixstreet.Volley;

public class Urls {
    public static final String url="http://ec2-18-223-107-103.us-east-2.compute.amazonaws.com:8080/api/";
//public static final String url="http://192.168.10.23:8080/api/";
    public static final String login=url+"Login";
    public static final String Teacher=url+"Teacher/";
    public static final String TeacherProfile=Teacher+"Profile";
    public static final String TeacherDashboard=Teacher+"Dashboard";
    public static final String TeacherDashboardDetails=Teacher+"DashboardDetails";

    public static final String TeacherStudents=Teacher+"Students";
    public static final String Academics=url+"Academics/";
    public static final String Attendance=url+"Attendance/";
    public static final String GetSurah=Academics+"GetSurah";
    public static final String PostDiary=Academics+"PostDiary";
    public static final String GetDiary=Academics+"GetDiaryByTeacher";
    public static final String GetDiaryParent=Academics+"GetDiary";

    public static final String PostAttendance=Attendance+"Post";
    public static final String PostNewSabq=Academics+"PostNewSabq";
    public static final String GetSabqStatus=Academics+"GetSabqStatus";
}
