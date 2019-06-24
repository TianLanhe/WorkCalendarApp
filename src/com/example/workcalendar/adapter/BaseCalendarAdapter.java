package com.example.workcalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import com.example.workcalendar.utils.Attrs;
import com.example.workcalendar.view.BaseCalendarView;

public abstract class BaseCalendarAdapter extends PagerAdapter {


    protected Context mContext;
    protected int mCount;//��ҳ��
    protected int mCurr;//��ǰλ��
    protected Attrs mAttrs;//���Բ���
    protected LocalDate mInitializeDate;//���ڳ�ʼ����Ĭ���ǵ���


    public BaseCalendarAdapter(Context context, Attrs attrs,LocalDate initializeDate) {
        this.mContext = context;
        this.mAttrs = attrs;
        this.mInitializeDate = initializeDate;
        LocalDate startDate = new LocalDate(attrs.startDateString);
        this.mCount = getIntervalCount(startDate, new LocalDate(attrs.endDateString), attrs.firstDayOfWeek) + 1;
        this.mCurr = getIntervalCount(startDate, mInitializeDate, attrs.firstDayOfWeek);
    }

    @Override
    public int getCount() {
        return mCount;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseCalendarView view = getView(mContext, mAttrs.firstDayOfWeek, mInitializeDate, mCurr, position);
        view.setTag(position);
        container.addView(view);
        return view;
    }

    protected abstract BaseCalendarView getView(Context context,int weekFirstDayType,LocalDate initializeDate,int curr,int position);

    protected abstract int getIntervalCount(LocalDate startDate, LocalDate endDate, int weekFirstDayType);

    //��ǰҳ��λ��
    public int getCurrItem() {
        return mCurr;
    }

}

