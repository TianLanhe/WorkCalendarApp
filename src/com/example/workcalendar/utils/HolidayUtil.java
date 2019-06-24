package com.example.workcalendar.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by necer on 2018/11/16.
 */
public class HolidayUtil {


    /**
     * ���ݹ�����ȡ����
     *
     * @return
     */
    public static String getSolarHoliday(int solarYear, int solarMonth, int solarDay) {
        String message = "";
        if (solarMonth == 1 && solarDay == 1) {
            message = "Ԫ��";
        } else if (solarMonth == 2 && solarDay == 14) {
            message = "���˽�";
        } else if (solarMonth == 3 && solarDay == 8) {
            message = "��Ů��";
        } else if (solarMonth == 3 && solarDay == 12) {
            message = "ֲ����";
        } else if (solarMonth == 3 && solarDay == 15) {
            message = "������";
        } else if (solarMonth == 4) {
            if (solarDay == 1) {
                message = "���˽�";
            } else if (solarDay >= 4 && solarDay <= 6) {
                if (solarYear <= 1999) {
                    int compare = (int) (((solarYear - 1900) * 0.2422 + 5.59) - ((solarYear - 1900) / 4));
                    if (compare == solarDay) {
                        message = "������";
                    }
                } else {
                    int compare = (int) (((solarYear - 2000) * 0.2422 + 4.81) - ((solarYear - 2000) / 4));
                    if (compare == solarDay) {
                        message = "������";
                    }
                }
            }
        } else if (solarMonth == 5 && solarDay == 1) {
            message = "�Ͷ���";
        } else if (solarMonth == 5 && solarDay == 4) {
            message = "�����";
        } else if (solarMonth == 6 && solarDay == 1) {
            message = "��ͯ��";
        } else if (solarMonth == 7 && solarDay == 1) {
            message = "������";
        } else if (solarMonth == 8 && solarDay == 1) {
            message = "������";
        } else if (solarMonth == 9 && solarDay == 10) {
            message = "��ʦ��";
        } else if (solarMonth == 10 && solarDay == 1) {
            message = "�����";
        }
        return message;
    }


    /**
     * ���ڻ�ȡ�й��Ĵ�ͳ����
     *
     * @param lunarMonth ũ������
     * @param lunarDay   ũ����
     * @return �й���ͳ����
     */
    public static String getLunarHoliday(int lunarYear, int lunarMonth, int lunarDay) {
        String message = "";
        if (lunarMonth == 1 && lunarDay == 1) {
            message = "����";
        } else if (lunarMonth == 1 && lunarDay == 15) {
            message = "Ԫ����";
        } else if (lunarMonth == 2 && lunarDay == 2) {
            message = "��̧ͷ";
        } else if (lunarMonth == 5 && lunarDay == 5) {
            message = "�����";
        } else if (lunarMonth == 7 && lunarDay == 7) {
            message = "��Ϧ";
        } else if (lunarMonth == 7 && lunarDay == 15) {
            message = "��Ԫ��";
        } else if (lunarMonth == 8 && lunarDay == 15) {
            message = "�����";
        } else if (lunarMonth == 9 && lunarDay == 9) {
            message = "������";
        } else if (lunarMonth == 12 && lunarDay == 8) {
            message = "���˽�";
        } else if (lunarMonth == 12 && lunarDay == 23) {
            message = "С��";
        } else {
            if (lunarMonth == 12) {
                if ((((LunarUtil.daysInLunarMonth(lunarYear, lunarMonth) == 29) && lunarDay == 29))
                        || ((((LunarUtil.daysInLunarMonth(lunarYear, lunarMonth) == 30) && lunarDay == 30)))) {
                    message = "��Ϧ";
                }
            }
        }
        return message;
    }

    //�����ڼ��� ��Ϣ������
    public static List<String> holidayList = Arrays.asList("2017-12-30", "2017-12-31", "2018-01-01", "2018-02-15", "2018-02-16", "2018-02-17", "2018-02-18",
            "2018-02-19", "2018-02-20", "2018-02-21", "2018-04-05", "2018-04-06", "2018-04-07", "2018-04-29", "2018-04-30", "2018-05-01", "2018-06-16", "2018-06-17",
            "2018-06-18", "2018-09-22", "2018-09-23", "2018-09-24", "2018-10-01", "2018-10-02", "2018-10-03", "2018-10-04", "2018-10-05", "2018-10-06", "2018-10-07",
            "2018-12-30", "2018-12-31", "2019-01-01", "2019-02-04", "2019-02-05", "2019-02-06", "2019-02-07", "2019-02-08", "2019-02-09", "2019-02-10", "2019-04-05", "2019-04-06",
            "2019-04-07", "2019-05-01", "2019-05-02", "2019-05-03", "2019-05-04", "2019-06-07", "2019-06-08", "2019-06-09", "2019-09-13", "2019-09-14", "2019-09-15", "2019-10-01", "2019-10-02",
            "2019-10-03", "2019-10-04", "2019-10-05", "2019-10-06", "2019-10-07");

    //���������
    public static List<String> workdayList = Arrays.asList("2018-02-11", "2018-02-24", "2018-04-08", "2018-04-28", "2018-09-29", "2018-04-30", "2018-12-29",
            "2019-02-02", "2019-02-03", "2019-04-28", "2019-05-05", "2019-09-29", "2019-10-12");


}
