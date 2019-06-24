package com.example.workcalendar.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.joda.time.LocalDate;

import com.example.workcalendar.adapter.BaseCalendarAdapter;
import com.example.workcalendar.adapter.MonthCalendarAdapter;
import com.example.workcalendar.listener.OnClickMonthViewListener;
import com.example.workcalendar.listener.OnMonthAnimatorListener;
import com.example.workcalendar.listener.OnMonthSelectListener;
import com.example.workcalendar.model.NDate;
import com.example.workcalendar.painter.CalendarPainter;
import com.example.workcalendar.utils.Attrs;
import com.example.workcalendar.utils.Util;

public class MonthCalendar extends BaseCalendar implements OnClickMonthViewListener, ValueAnimator.AnimatorUpdateListener {


    protected ValueAnimator monthValueAnimator;//����������
    private OnMonthSelectListener onMonthSelectListener;
    private OnMonthAnimatorListener onMonthAnimatorListener;


    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate) {
        return new MonthCalendarAdapter(context, attrs, initializeDate,this);
    }

    public MonthCalendar(Context context, Attrs attrs, CalendarPainter calendarPainter, int duration, OnMonthAnimatorListener onMonthAnimatorListener) {
        super(context, attrs,calendarPainter);
        this.onMonthAnimatorListener = onMonthAnimatorListener;
        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.addUpdateListener(this);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return Util.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(1);
    }

    @Override
    protected void onSelcetDate(NDate nDate,boolean isClick) {
        if (onMonthSelectListener != null) {
            onMonthSelectListener.onMonthSelect(nDate,isClick);
        }
    }


    @Override
    public void onClickCurrentMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,0);
        } else {
            onClickDisableDate(localDate);
        }

    }

    @Override
    public void onClickLastMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,-1);
        } else {
            onClickDisableDate(localDate);
        }
    }

    @Override
    public void onClickNextMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,1);
        } else {
            onClickDisableDate(localDate);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
    }


    public int getMonthCalendarOffset() {
        if (mCurrView != null) {
            return mCurrView.getMonthCalendarOffset();
        }
        return 0;
    }

    public void autoToMonth() {
        float top = getY();//��ʼλ��
        int end = 0;
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }


    public void autoToMIUIWeek() {
        float top = getY();//��ʼλ��
        int end = -getMonthCalendarOffset(); //����λ��
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }

    public void autoToEMUIWeek() {
        float top = getY();//��ʼλ��
        int end = -getHeight() * 4 / 5; //����λ��
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }


    public boolean isMonthState() {
        return getY() >= 0;
    }

    public boolean isWeekState() {
        return getY() <= -getMonthCalendarOffset();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        float top = getY();
        float i = animatedValue - top;
        float y = getY();
        setY(i + y);

        if (onMonthAnimatorListener != null) {
            //�ص���ѭ>0���ϣ�<0����
            onMonthAnimatorListener.onMonthAnimatorChanged((int) -i);
        }
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (onMonthAnimatorListener != null) {
            onMonthAnimatorListener.onMonthAnimatorChanged(0);
        }
    }
}
