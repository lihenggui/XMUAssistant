/*
package com.merxury.xmuassistant;

*/
/**
 * Created by dhd on 2016/7/13.
 *//*


import org.jsoup.Jsoup;


        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class CourseService {
    private static volatile CourseService courseService;
    private CourseService(){}
    public static CourseService getCourseService() {
        if(courseService==null){
            synchronized (CourseService.class) {
                if(courseService==null)
                    courseService=new CourseService();
            }
        }
        return courseService;
    }

    public boolean save(Course course) {
        return course.save();
    }


    public List<Course> findAll() {
        return DataSupport.findAll(Course.class);
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



    */
/**
     * 根据传进来的课程格式转换为对应的实体类并保存
       * @param sub
        * @param startYear
        * @param endYear
         * @param semester
          * @param week
         * @param startSection
          * @param endSection
        * @return
        *//*


    private Course storeCourseByResult(String sub,int startYear,int endYear,int semester, int week,
                                       int startSection, int endSection) {
        // 周二第1,2节{第4-16周}      二,1,2,4,16,null
              // {第2-10周|3节/周}            null,null,null,2,10,3节/周
              // 周二第1,2节{第4-16周|双周}   二,1,2,4,16,双周
              // 周二第1节{第4-16周}            二,1,null,4,16,null
             // 周二第1节{第4-16周|双周}         二,1,null,4,16,双周
               // str格式如上，这里只是简单考虑每个课都只有两节课，实际上有三节和四节，模式就要改动，其他匹配模式请自行修改

                String reg = "周?(.)?第?(\\d{1,2})?,?(\\d{1,2})?节?\\{第(\\d{1,2})-(\\d{1,2})周\\|?((.*周))?\\}";

              String splitPattern = "<br />";

        String[] temp = sub.split(splitPattern);
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(temp[1]);
        matcher.matches();
        Course course = new Course();
              //课程开始学年
              course.setStartYear(startYear);
              //课程结束学年
                course.setEndYear(endYear);
               //课程学期
               course.setSemester(semester);

             //课程名
             course.setCourseName(temp[0]);
            //课程时间，冗余字段
               course.setCourseTime(temp[1]);
              //教师
             course.setTeacher(temp[2]);

              try {
                       // 数组可能越界，即没有教室
                       course.setClasssroom(temp[3]);
                 } catch (ArrayIndexOutOfBoundsException e) {
                      course.setClasssroom("无教室");
                   }
                //周几，可能为空，此时使用传进来的值
              if (null != matcher.group(1)){
                     course.setDayOfWeek(getDayOfWeek(matcher.group(1)));
                 }else{
                    course.setDayOfWeek(getDayOfWeek(week+""));
                 }
              //课程开始节数，可能为空，此时使用传进来的值
              if (null != matcher.group(2)){
                    course.setStartSection(Integer.parseInt(matcher.group(2)));
                  }else{
                     course.setStartSection(startSection);
                  }

             //课程结束时的节数，可能为空，此时使用传进来的值
               if (null != matcher.group(3)){
                       course.setEndSection(Integer.parseInt(matcher.group(3)));
                  }else{
                       course.setEndSection(endSection);
                    }

              //起始周
               course.setStartWeek(Integer.parseInt(matcher.group(4)));
               //结束周
              course.setEndWeek(Integer.parseInt(matcher.group(5)));
             //单双周
             String t = matcher.group(6);
               setEveryWeekByChinese(t, course);
                save(course);
             return course;
            }





    */
/**
      * 提取课程格式，可能包含多节课
         * @param str
         * @param startYear
         * @param endYear
     * @param semester
      * @param week
         * @param startSection
         * @param endSection
        * @return
        *//*


    private int splitCourse(String str, int startYear,int endYear,int semester,int week, int startSection,
                            int endSection) {
        String pattern = "<br /><br />";
        String[] split = str.split(pattern);
        if (split.length > 1) {// 如果大于一节课
            for (int i = 0; i < split.length; i++) {
                if (!(split[i].startsWith("<br />") && split[i].endsWith("<br />"))) {
                    storeCourseByResult(split[i], startYear,endYear,semester,week, startSection,
                            endSection);// ���浥�ڿ�
                } else {
                    // <br />文化地理（网络课程）<br />周日第10节{第17-17周}<br />李宏伟<br />
                                     // 以上格式的特殊处理，此种格式在没有教师的情况下产生，即教室留空后<br />依旧存在

                    int brLength = "<br />".length();
                    String substring = split[i].substring(brLength,
                            split[i].length() - brLength);
                    storeCourseByResult(substring, startYear,endYear,semester,week, startSection,
                            endSection);//  保存单节课
                }
            }
            return split.length;
        } else {
            storeCourseByResult(str, startYear,endYear,semester,week, startSection, endSection);//保存
            return 1;
        }
    }

    */
/**
         * 填充map，获取周几，第几节课至第几节课
        * @param childColumn
          * @param map
       * @param i
         * @return 周几
        *//*


    public static int fillMap(Element childColumn, int map[][], int i) {
        //这个函数的作用自行领悟，总之就是返回周几，也是无意中发现的，于是就这样获取了，作用是双重保障，因为有些课事无法根据正则匹配出周几第几节到第几节
        boolean hasAttr = childColumn.hasAttr("rowspan");
        int week = 0;
        if (hasAttr) {
            for (int t = 0; t < map[0].length; t++) {
                if (map[i][t] == 0) {
                    int r = Integer.parseInt(childColumn.attr("rowspan"));
                    for (int l = 0; l < r; l++) {
                        map[i + l][t] = 1;
                    }
                    week = t + 1;
                    break;
                }
            }

        } else {
            if (childColumn.childNodes().size() > 1) {
                childColumn.attr("rowspan", "1");
            }
            for (int t = 0; t < map[0].length; t++) {
                if (map[i][t] == 0) {
                    map[i][t] = 1;
                    week = t + 1;
                    break;
                }
            }
        }
        return week;
    }
    */
/**
     * 设置单双周
   * @param week
      * @param course
       *//*


    public void setEveryWeekByChinese(String week, Course course) {
        // 1代表单周，2代表双周
        if (week != null) {
            if (week.equals("单周"))
                course.setEveryWeek(1);
            else if (week.equals("双周"))
                course.setEveryWeek(2);
        }
        // 默认值为0，代表每周
    }

    */
/**根据中文数字一，二，三，四，五，六，日，转换为对应的阿拉伯数字
      * @param day
       * @return int
      *//*


    public int getDayOfWeek(String day) {
        if (day.equals("一"))
            return 1;
        else if (day.equals("二"))
            return 2;
        else if (day.equals("三"))
            return 3;
        else if (day.equals("四"))
            return 4;
        else if (day.equals("五"))
            return 5;
        else if (day.equals("六"))
            return 6;
        else if (day.equals("日"))
            return 7;
        else
            return 0;
    }
}
*/
