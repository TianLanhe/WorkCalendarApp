package com.example.workcalendar.view;

import android.content.Context;

import org.joda.time.LocalDate;

import com.example.workcalendar.listener.OnClickMonthViewListener;
import com.example.workcalendar.model.NDate;
import com.example.workcalendar.utils.Util;

import java.util.List;

public class MonthView extends BaseCalendarView {

    private OnClickMonthViewListener mOnClickMonthViewListener;


    public MonthView(Context context, LocalDate localDate, int weekFirstDayType,OnClickMonthViewListener onClickMonthViewListener) {
        super(context, localDate, weekFirstDayType);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }


    @Override
    protected List<NDate> getNCalendar(LocalDate localDate, int type) {
        return Util.getMonthCalendar(localDate,type);
    }

    @Override
    protected void onClick(NDate nDate, LocalDate initialDate) {
        if (Util.isLastMonth(nDate.localDate, initialDate)) {
            mOnClickMonthViewListener.onClickLastMonth(nDate.localDate);
        } else if (Util.isNextMonth(nDate.localDate, initialDate)) {
            mOnClickMonthViewListener.onClickNextMonth(nDate.localDate);
        } else {
            mOnClickMonthViewListener.onClickCurrentMonth(nDate.localDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return Util.isEqualsMonth(date, initialDate);
    }
}
