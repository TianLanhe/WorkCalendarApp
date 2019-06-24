package com.example.workcalendar.adapter;

import android.content.Context;

import org.joda.time.LocalDate;

import com.example.workcalendar.listener.OnClickMonthViewListener;
import com.example.workcalendar.utils.Attrs;
import com.example.workcalendar.utils.Util;
import com.example.workcalendar.view.BaseCalendarView;
import com.example.workcalendar.view.MonthView;

public class MonthCalendarAdapter extends BaseCalendarAdapter {


    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate, OnClickMonthViewListener onClickMonthViewListener) {
        super(context, attrs,initializeDate);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    protected BaseCalendarView getView(Context context,int weekFirstDayType,LocalDate initializeDate,int curr,int position) {
        LocalDate date = initializeDate.plusMonths(position - curr);
        MonthView monthView = new MonthView(context, date, weekFirstDayType, mOnClickMonthViewListener);
        return monthView;
    }

    @Override
    protected int getIntervalCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

}
