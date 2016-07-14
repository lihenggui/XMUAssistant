/*
package com.merxury.xmuassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

*/
/**
 * 课程实体类
  * @author lizhangqu
  * @date 2015-2-1
 *//*


public class Course extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "create table course(id varchar(100) primary key not null,StartYear int," +
            "EndYear int,Semester int,CourseName varchar(100),CourseTime varchar(100),Classroom varchar(100)," +
            "Teacher varchar(100),DayOfWeek int,StartSection int,EndSection int,StartWeek int,endWeek int,SingleOrDoule int);";

    private SQLiteDatabase db;
    Course(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }
    //打开课程表查询网站
   // public static String url = ""

    private int id;
    private int startYear;//学年开始年
    private int endYear;//学年结束年
    private int semester;//学期
    private String courseName;//课程名
   // private String courseTime;//课程时间，冗余字段
    private String classsroom;//教室，可能为空
    private String teacher;//老师
    private int dayOfWeek;//星期几
    private int startSection;//第几节课开始
    private int endSection;//第几节课结束
    private int startWeek;//开始周
    private int endWeek;//结束周
    private int everyWeek;//标记是否是单双周，0为每周,1单周，2双周
    public int getStartYear() {
        return startYear;
    }
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }
    public int getEndYear() {
        return endYear;
    }
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
    public int getSemester() {
        return semester;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEveryWeek() {
        return everyWeek;
    }
    public void setEveryWeek(int everyWeek) {
        this.everyWeek = everyWeek;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public int getStartSection() {
        return startSection;
    }
    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }
    public int getEndSection() {
        return endSection;
    }
    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }
    public int getStartWeek() {
        return startWeek;
    }
    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }
    public int getEndWeek() {
        return endWeek;
    }
    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
   */
/**
    public String getCourseTime() {
        return courseTime;
    }
    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }
    *//*

    public String getClasssroom() {
        return classsroom;
    }
    public void setClasssroom(String classsroom) {
        this.classsroom = classsroom;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

*/
/**
    public String toString() {
        return "Course [id=" + id + ", startYear=" + startYear + ", endYear="
                + endYear + ", semester=" + semester + ", courseName="
                + courseName + ", courseTime=" + courseTime + ", classsroom="
                + classsroom + ", teacher=" + teacher + ", dayOfWeek="
                + dayOfWeek + ", startSection=" + startSection
                + ", endSection=" + endSection + ", startWeek=" + startWeek
                + ", endWeek=" + endWeek + ", everyWeek=" + everyWeek + "]";
    }
*//*


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
    }

    */
/**
     * 根据网页返回结果解析课程并保存
     *
     * @param content
     * @return
     *//*


    public String parseCourse(String content) {
        StringBuilder result = new StringBuilder();
        Document doc = Jsoup.parse(content);

        Elements semesters = doc.select("option[selected=selected]");
        String[] years=semesters.get(0).text().split("-");
        int startYear=Integer.parseInt(years[0]);
        int endYear=Integer.parseInt(years[1]);
        int semester=Integer.parseInt(semesters.get(1).text());



        Elements elements = doc.select("table#Table1");
        Element element = elements.get(0).child(0);
        //�Ƴ�һЩ��������

        element.child(0).remove();
        element.child(0).remove();
        element.child(0).child(0).remove();
        element.child(4).child(0).remove();
        element.child(8).child(0).remove();
        int rowNum = element.childNodeSize();
        int[][] map = new int[11][7];
        for (int i = 0; i < rowNum - 1; i++) {
            Element row = element.child(i);
            int columnNum = row.childNodeSize() - 2;
            for (int j = 1; j < columnNum; j++) {
                Element column = row.child(j);
                int week = fillMap(column, map, i);

                if (column.hasAttr("rowspan")) {
                    try {
                        System.out.println("周"+ week+ " 第"+ (i + 1)+ "节-第"+ (i + Integer.parseInt(column.attr("rowspan"))) + "节");
                        splitCourse(column.html(), startYear,endYear,semester,week, i + 1,i + Integer.parseInt(column.attr("rowspan")));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return result.toString();
    }
}
*/
