package com.example.workcalendar.painter;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.workcalendar.model.NDate;

public interface CalendarPainter {


    /**
     * ���ƽ��������
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect �����Ƿ�ѡ��
     */
    void onDrawToday(Canvas canvas, Rect rect, NDate nDate, boolean isSelect);


    /**
     * ���Ƶ�ǰ�»��ܵ�����
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect �Ƿ�ѡ��
     */
    void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, NDate nDate, boolean isSelect);

    /**
     * ������һ�£���һ�µ����ڣ�����������ʵ��
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    void onDrawNotCurrentMonth(Canvas canvas, Rect rect, NDate nDate);


    /**
     * ���Ʋ����õ����ڣ��ͷ���setDateInterval(startFormatDate, endFormatDate)��Ӧ
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    void onDrawDisableDate(Canvas canvas, Rect rect, NDate nDate);



}
