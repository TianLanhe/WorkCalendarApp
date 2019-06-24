package com.example.workcalendar.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

import org.joda.time.LocalDate;

import com.example.workcalendar.adapter.BaseCalendarAdapter;
import com.example.workcalendar.listener.OnClickDisableDateListener;
import com.example.workcalendar.listener.OnYearMonthChangedListener;
import com.example.workcalendar.model.NDate;
import com.example.workcalendar.painter.CalendarPainter;
import com.example.workcalendar.painter.InnerPainter;
import com.example.workcalendar.utils.Attrs;
import com.example.workcalendar.utils.AttrsUtil;
import com.example.workcalendar.utils.Util;

public abstract class BaseCalendar extends ViewPager {

    private Context mContext;

    private Attrs attrs;
    protected BaseCalendarView mCurrView;//��ǰ��ʾ��ҳ��
    protected BaseCalendarView mLastView;//��ǰ��ʾ��ҳ�����һ��ҳ��
    protected BaseCalendarView mNextView;//��ǰ��ʾ��ҳ�����һ��ҳ��

    protected LocalDate mSelectDate;//����������ѡ�е�����,�������ѡ�кͷ�ҳѡ��
    protected LocalDate mOnClickDate;//���ѡ�е�����


    protected OnYearMonthChangedListener onYearMonthChangedListener;
    protected OnClickDisableDateListener onClickDisableDateListener;

    protected LocalDate startDate, endDate, initializeDate, callBackDate;

    protected CalendarPainter mCalendarPainter;


    public BaseCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.attrs = AttrsUtil.getAttrs(context, attributeSet);
        this.mCalendarPainter = new InnerPainter(attrs);
        init(context);
    }


    public BaseCalendar(Context context, Attrs attrs,CalendarPainter calendarPainter) {
        super(context);
        this.attrs = attrs;
        this.mCalendarPainter = calendarPainter;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        drawView(position);
                    }
                });
            }
        });

        initializeDate = mSelectDate = new LocalDate();
        initDate(initializeDate);
    }


    private void initDate(LocalDate initializeDate) {

        String startDateString = attrs.startDateString;
        String endDateString = attrs.endDateString;
        try {
            startDate = new LocalDate(startDateString);
            endDate = new LocalDate(endDateString);
        } catch (Exception e) {
            throw new RuntimeException("startDate��endDate��Ҫ yyyy-MM-dd ��ʽ������");
        }


        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("startDate������endDate֮ǰ");
        }

        if (startDate.isBefore(new LocalDate("1901-01-01"))) {
            throw new RuntimeException("startDate������1901-01-01֮��");
        }

        if (endDate.isAfter(new LocalDate("2099-12-31"))) {
            throw new RuntimeException("endDate������2099-12-31֮ǰ");
        }


      /*  if (startDate.isAfter(initializeDate) || endDate.isBefore(initializeDate)) {
            throw new RuntimeException("����������Ҫ��������");
        }*/

        BaseCalendarAdapter calendarAdapter = getCalendarAdapter(mContext, attrs, initializeDate);
        int currItem = calendarAdapter.getCurrItem();
        setAdapter(calendarAdapter);

        //��ǰitemΪ0ʱ��OnPageChangeListener���ص�
        if (currItem == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    drawView(getCurrentItem());
                }
            });
        }

        setCurrentItem(currItem);

    }

    public void setDateInterval(String startFormatDate, String endFormatDate) {
        attrs.startDateString = startFormatDate;
        attrs.endDateString = endFormatDate;
        initDate(initializeDate);
    }

    public void setInitializeDate(String formatInitializeDate) {
        try {
            initializeDate = mSelectDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate�Ĳ�����Ҫ yyyy-MM-dd ��ʽ������");
        }
        initDate(initializeDate);
    }

    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {
        attrs.startDateString = startFormatDate;
        attrs.endDateString = endFormatDate;
        try {
            initializeDate = mSelectDate = new LocalDate(formatInitializeDate);
        } catch (Exception e) {
            throw new RuntimeException("setInitializeDate�Ĳ�����Ҫ yyyy-MM-dd ��ʽ������");
        }
        initDate(initializeDate);
    }

    private void drawView(int position) {

        this.mCurrView = (BaseCalendarView) findViewWithTag(position);
        this.mLastView = (BaseCalendarView) findViewWithTag(position - 1);
        this.mNextView = (BaseCalendarView) findViewWithTag(position + 1);


        if (mCurrView == null) {
            return;
        }
        LocalDate initialDate = mCurrView.getInitialDate();
        //��ǰҳ��ĳ�ʼֵ���ϸ�ҳ��ѡ�е����ڣ����»��ܣ������ϸ�ҳ��ѡ�е����ڵó���ǰҳ��ѡ�е�����
        int currNum = getTwoDateCount(mSelectDate, initialDate, attrs.firstDayOfWeek);//�ó�����ҳ������
        if (currNum != 0) {
            mSelectDate = getDate(mSelectDate, currNum);
        }
        mSelectDate = getSelectDate(mSelectDate);

        //���ƵĹ���1��Ĭ��ѡ�У�ÿ��ҳ�涼����ѡ�С�1��Ĭ�ϲ�ѡ�У����ǵ���˵�ǰҳ���ĳ������
        //��ǰҳ��Ļص�������
        boolean isCurrViewDraw = attrs.isDefaultSelect || (mCurrView.contains(mOnClickDate));

        //��ҳ�ص��������ظ�������ͨ��callBackDate�ɱ����ظ��ص�
        callBack(isCurrViewDraw, false);
        notifyView();
    }

    //ˢ��ҳ��
    protected void notifyView() {

        if (mCurrView == null) {
            mCurrView = (BaseCalendarView) findViewWithTag(getCurrentItem());
        }
        if (mCurrView != null) {
            boolean isDraw = attrs.isDefaultSelect || (mCurrView.contains(mOnClickDate));
            mCurrView.setSelectDate((mCurrView.contains(mOnClickDate) ? mOnClickDate : mSelectDate), isDraw);
        }
        if (mLastView == null) {
            mLastView = (BaseCalendarView) findViewWithTag(getCurrentItem() - 1);
        }
        if (mLastView != null) {
            boolean isDraw = attrs.isDefaultSelect || (mLastView.contains(mOnClickDate));
            mLastView.setSelectDate(getSelectDate((mLastView.contains(mOnClickDate) ? mOnClickDate : getLastSelectDate(mSelectDate))), isDraw);
        }
        if (mNextView == null) {
            mNextView = (BaseCalendarView) findViewWithTag(getCurrentItem() + 1);
        }
        if (mNextView != null) {
            boolean isDraw = attrs.isDefaultSelect || (mNextView.contains(mOnClickDate));
            mNextView.setSelectDate(getSelectDate((mNextView.contains(mOnClickDate) ? mOnClickDate : getNextSelectDate(mSelectDate))), isDraw);
        }
    }

    //ˢ������ҳ��
    public void notifyAllView() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseCalendarView calendarView = (BaseCalendarView) getChildAt(i);
            if (calendarView != null) {
                calendarView.invalidate();
            }
        }
    }

    //��ת
    protected void jumpDate(LocalDate localDate, boolean isDraw) {
        localDate = getSelectDate(localDate);
        int num = getTwoDateCount(mSelectDate, localDate, attrs.firstDayOfWeek);
        onClickDate(localDate, num);
    }

    //�������
    protected void onClickDate(LocalDate localDate, int indexOffset) {
        mOnClickDate = localDate;
        mSelectDate = localDate;
        //����ص� �Ȼص�������ת֮��callBackDateֵ�Ѿ��仯�����������»ص�
        callBack(true, true);
        if (indexOffset != 0) {
            setCurrentItem(getCurrentItem() + indexOffset, Math.abs(indexOffset) == 1);
        } else {
            notifyView();
        }
    }


    //�ص�
    private void callBack(boolean isDraw, boolean isClick) {
        if (!mSelectDate.equals(callBackDate)) {
            //ѡ�лص� ,�����˲Ż�ص�
            if (isDraw) {
                onSelcetDate(Util.getNDate(mSelectDate), isClick);
                callBackDate = mSelectDate;
            }
            //���»ص�
            onYearMonthChanged(mSelectDate, isClick);
        }
    }

    //���ڱ߽紦��
    private LocalDate getSelectDate(LocalDate localDate) {
        if (localDate.isBefore(startDate)) {
            return startDate;
        } else if (localDate.isAfter(endDate)) {
            return endDate;
        } else {
            return localDate;
        }
    }


    //��ȥviewpager��adapter
    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context, Attrs attrs, LocalDate initializeDate);

    //�������ڵ��������
    protected abstract int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type);

    //���count֮��ĵ�����
    protected abstract LocalDate getDate(LocalDate localDate, int count);

    //�ػ浱ǰҳ��ʱ����ȡ�ϸ���ѡ�е�����
    protected abstract LocalDate getLastSelectDate(LocalDate currectSelectDate);

    //�ػ浱ǰҳ��ʱ����ȡ�¸���ѡ�е�����
    protected abstract LocalDate getNextSelectDate(LocalDate currectSelectDate);


    //��������ѡ�е����ڣ���ѡ��Ȧ�ĲŻ�ص�
    protected abstract void onSelcetDate(NDate nDate, boolean isClick);

    //��ݺ��·ݱ仯�ص�,����ͷ�ҳ����ص���������û������ѡ��
    public void onYearMonthChanged(LocalDate localDate, boolean isClick) {
        if (onYearMonthChangedListener != null) {
            onYearMonthChangedListener.onYearMonthChanged(this, localDate.getYear(), localDate.getMonthOfYear(), isClick);
        }
    }

    //����������Ƿ����
    protected boolean isClickDateEnable(LocalDate localDate) {
        return !(localDate.isBefore(startDate) || localDate.isAfter(endDate));
    }

    //��������õ����ڴ���
    protected void onClickDisableDate(LocalDate localDate) {
        if (onClickDisableDateListener != null) {
            onClickDisableDateListener.onClickDisableDate(Util.getNDate(localDate));
        } else {
            Toast.makeText(getContext(), TextUtils.isEmpty(attrs.disabledString) ? "������" : attrs.disabledString, Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnYearMonthChangeListener(OnYearMonthChangedListener onYearMonthChangedListener) {
        this.onYearMonthChangedListener = onYearMonthChangedListener;
    }

    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {
        this.onClickDisableDateListener = onClickDisableDateListener;
    }

    //��һҳ��������������һ�£�������������һ��
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    //��һҳ
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }


    //��ת����
    public void jumpDate(String formatDate) {
        LocalDate jumpDate;
        try {
            jumpDate = new LocalDate(formatDate);
        } catch (Exception e) {
            throw new RuntimeException("jumpDate�Ĳ�����Ҫ yyyy-MM-dd ��ʽ������");
        }

        jumpDate(jumpDate, true);
    }

    //�ص�����
    public void toToday() {
        jumpDate(new LocalDate(), true);
    }

    //��ȡ���俪ʼ����
    public LocalDate getStartDate() {
        return startDate;
    }

    //��ȡ�����������
    public LocalDate getEndDate() {
        return endDate;
    }

    //���û�����
    public void setCalendarPainter(CalendarPainter calendarPainter) {
        this.mCalendarPainter = calendarPainter;
       // notifyAllView();
    }

    //BaseCalendarView���ƻ�ȡmCalendarPainter
    public CalendarPainter getCalendarPainter() {
        return mCalendarPainter;
    }


}
