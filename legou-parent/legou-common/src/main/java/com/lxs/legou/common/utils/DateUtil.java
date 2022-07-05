package com.lxs.legou.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    //时间格式
    public static final String PATTERN_YYYYMMDDHH = "yyyyMMddHH";
    public static final String PATTERN_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";

    /**
     * 从yyyy-MM-dd HH:mm格式转成yyyyMMddHH格式
     *
     * @param dateStr yyyy-MM-dd HH:mm
     * @return yyyyMMddHH
     */
    public static String formatStr(String dateStr, String opattern, String npattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(opattern);
        try {
            Date date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat(npattern);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期的凌晨
     *
     * @return Date
     */
    public static Date toDayStartHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * 时间增加N分钟
     *
     * @param date    Date
     * @param minutes int
     * @return Date
     */
    public static Date addDateMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);// 24小时制
        date = calendar.getTime();
        return date;
    }

    /**
     * 时间递增N小时
     *
     * @param hour int
     * @return Date
     */
    public static Date addDateHour(Date date, int hour) {//Jota-time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);// 24小时制
        date = calendar.getTime();
        return date;
    }

    /**
     * 以当前时间区域为起始点,计算接下来的5个(包含起始点)秒杀活动时间节点,秒杀活动每隔2小时开始一次，注意该方法没有做凌晨时间的处理
     *
     * @return List<Date>
     */
    public static List<Date> getDateMenus() {
        //定义一个List<Date>集合，存储所有时间段
        List<Date> dates = getDates(12);
        //判断当前时间属于哪个时间范围
        Date now = new Date();
        for (Date cdate : dates) {
            //开始时间 <= 当前时间 < 开始时间+2小时
            if (cdate.getTime() <= now.getTime() && now.getTime() < addDateHour(cdate, 2).getTime()) {
                now = cdate;
                break;
            }
        }

        //当前需要显示的时间菜单
        List<Date> dateMenus = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dateMenus.add(addDateHour(now, i * 2));
        }
        return dateMenus;
    }

    /**
     * 指定时间往后N个时间间隔
     *
     * @param hours int
     * @return List<Date>
     */
    public static List<Date> getDates(int hours) {
        List<Date> dates = new ArrayList<>();
        //循环12次
        Date date = toDayStartHour(new Date()); //凌晨
        for (int i = 0; i < hours; i++) {
            //每次递增2小时,将每次递增的时间存入到List<Date>集合中
            dates.add(addDateHour(date, i * 2));
        }
        return dates;
    }

    /**
     * Date类对象标准化为yyyyMMddHH的格式
     *
     * @param date    Date
     * @param pattern String
     * @return String
     */
    public static String data2str(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    /**
     * 测试getDateMenus方法
     *
     * @param args 无参数
     */
    public static void main(String[] args) {
        getDateMenus().forEach(System.out::println);
    }

}