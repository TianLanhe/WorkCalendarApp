package com.example.workcalendar.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import com.example.workcalendar.model.Lunar;
import com.example.workcalendar.model.NDate;

import java.util.ArrayList;
import java.util.List;

public class Util {


    /**
     * ��Ļ���
     * @param context
     * @return
     */
    public static int getScreenWith(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }



    /**
     *
     * @param color ԭ������ɫ
     * @param alpha ͸���ȣ�С��
     * @return
     */
/*
    public static int getAlphaColor(int color, double alpha) {
        int a = (int) Math.round(alpha * 255);
        String hex = Integer.toHexString(a).toUpperCase();
        if (hex.length() == 1) hex = "0" + hex;
        String hexCode = "#" + hex + String.format("%06X", Integer.valueOf(16777215 & color));
        int newColor;
        try {
            newColor = Color.parseColor(hexCode);
        } catch (Throwable throwable) {
            newColor = color;
        }
        return newColor;
    }
*/


    /**
     * dpתpx
     *
     * @param context
     * @param
     * @return
     */
    public static float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * spתpx
     *
     * @param context
     * @param spVal
     * @return
     */
    public static float sp2px(Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    //�Ƿ�ͬ��
    public static boolean isEqualsMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear();
    }


    /**
     * ��һ���ǲ��ǵڶ�������һ����,ֻ�ڴ˴���Ч
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isLastMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(-1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * ��һ���ǲ��ǵڶ�������һ���£�ֻ�ڴ˴���Ч
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNextMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * ����������ھ��뼸����
     *
     * @return
     */
    public static int getIntervalMonths(LocalDate date1, LocalDate date2) {
        date1 = date1.withDayOfMonth(1);
        date2 = date2.withDayOfMonth(1);

        return Months.monthsBetween(date1, date2).getMonths();
    }

    /**
     * ����������ھ��뼸��
     *
     * @param date1
     * @param date2
     * @param type  һ��
     * @return
     */
    public static int getIntervalWeek(LocalDate date1, LocalDate date2, int type) {

        if (type == Attrs.MONDAY) {
            date1 = getMonFirstDayOfWeek(date1);
            date2 = getMonFirstDayOfWeek(date2);
        } else {
            date1 = getSunFirstDayOfWeek(date1);
            date2 = getSunFirstDayOfWeek(date2);
        }

        return Weeks.weeksBetween(date1, date2).getWeeks();

    }


    /**
     * �Ƿ��ǽ���
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return new LocalDate().equals(date);

    }

    /**
     * @param localDate ����
     * @param type      300�����գ�301��һ
     * @return
     */
    public static List<NDate> getMonthCalendar(LocalDate localDate, int type) {

        LocalDate lastMonthDate = localDate.plusMonths(-1);//�ϸ���
        LocalDate nextMonthDate = localDate.plusMonths(1);//�¸���

        int days = localDate.dayOfMonth().getMaximumValue();//��������
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//�ϸ��µ�����
        int firstDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1).getDayOfWeek();//���µ�һ���ܼ�
        int endDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), days).getDayOfWeek();//�������һ���ܼ�

        List<NDate> dateList = new ArrayList<>();


        //��һ��ʼ��
        if (type == Attrs.MONDAY) {

            //��һ��ʼ��
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2));
                dateList.add(getNDate(date));
            }
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }

        } else {
            //�ϸ���
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1));
                    dateList.add(getNDate(date));
                }
            }
            //����
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
            //�¸���
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
        }

        //ĳЩ���2�·�28�죬����������ֻռ4��
        if (dateList.size() == 28) {
            for (int i = 0; i < 7; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(getNDate(date));
            }
        }
        return dateList;

    }


    /**
     * ����ͼ������
     *
     * @param localDate
     * @return
     */
    public static List<NDate> getWeekCalendar(LocalDate localDate, int type) {
        List<NDate> dateList = new ArrayList<>();

        if (type == Attrs.MONDAY) {
            localDate = getMonFirstDayOfWeek(localDate);
        } else {
            localDate = getSunFirstDayOfWeek(localDate);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate date = localDate.plusDays(i);
            dateList.add(getNDate(date));
        }
        return dateList;
    }


    public static List<String> getHolidayList() {
        return HolidayUtil.holidayList;
    }

    public static List<String> getWorkdayList() {
        return HolidayUtil.workdayList;
    }


    //ת��һ�ܴ����տ�ʼ
    public static LocalDate getSunFirstDayOfWeek(LocalDate date) {
        if (date.dayOfWeek().get() == 7) {
            return date;
        } else {
            return date.minusWeeks(1).withDayOfWeek(7);
        }
    }

    //ת��һ�ܴ���һ��ʼ
    public static LocalDate getMonFirstDayOfWeek(LocalDate date) {
        return date.dayOfWeek().withMinimumValue();
    }


    public static NDate getNDate(LocalDate localDate) {
        NDate nDate = new NDate();

        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();

        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);

        if (solarYear != 1900) {
            nDate.lunar = lunar;
            nDate.localDate = localDate;
            nDate.solarTerm = SolarTermUtil.getSolatName(solarYear, solarMonth < 10 ? ("0" + solarMonth) : (solarMonth + "") + solarDay);
            nDate.solarHoliday = HolidayUtil.getSolarHoliday(solarYear, solarMonth, solarDay);
            nDate.lunarHoliday = HolidayUtil.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay);

        }

        return nDate;
    }
}
