package com.example.workcalendar.listener;

import com.example.workcalendar.view.BaseCalendar;

public interface OnYearMonthChangedListener {
    void onYearMonthChanged(BaseCalendar baseCalendar, int year, int month,boolean isClick);
}
