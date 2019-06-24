package com.example.workcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.joda.time.LocalDate;

import com.example.workcalendar.model.NDate;
import com.example.workcalendar.painter.CalendarPainter;
import com.example.workcalendar.utils.Util;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCalendarView extends View {

    private int mLineNum;//����
    protected LocalDate mInitialDate;//��mInitialDate���ܿ�ʼ�ĵ�һ�� �����ǰҳ�������
    protected List<Rect> mRectList;//����õľ��μ���
    protected List<NDate> mDateList;//ҳ������ݼ���
    private LocalDate mSelectDate;//���ѡ�е�����
    private boolean isDraw;//�Ƿ�������ѡ�е�����

    public BaseCalendarView(Context context, LocalDate localDate, int weekFirstDayType) {
        super(context);
        this.mInitialDate = this.mSelectDate = localDate;
        mDateList = getNCalendar(localDate, weekFirstDayType);
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//����/7
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //����ʱ��ȡ���俪ʼ�������ںͻ�����Painter
        BaseCalendar calendar = (BaseCalendar) getParent();
        LocalDate startDate = calendar.getStartDate();
        LocalDate endDate = calendar.getEndDate();
        CalendarPainter painter = calendar.getCalendarPainter();

        mRectList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                NDate nDate = mDateList.get(i * 7 + j);
                LocalDate date = nDate.localDate;

                //�ڿ��������ڵ��������ƣ�
                if (!(date.isBefore(startDate) || date.isAfter(endDate))) {
                    if (isEqualsMonthOrWeek(date, mInitialDate)) {  //���º������µ���ɫ��ͬ
                        if (Util.isToday(date)) {  //������ѡ�еĵ���
                            painter.onDrawToday(canvas, rect, nDate, isDraw && date.equals(mSelectDate));
                        } else { //���Ĭ��ѡ�񣬾ͻ��ƣ����Ĭ�ϲ�ѡ���Ҳ��ǵ�����Ͳ�����
                            painter.onDrawCurrentMonthOrWeek(canvas, rect, nDate, isDraw && date.equals(mSelectDate));
                        }
                    } else {  //���ǵ��µ�����
                        painter.onDrawNotCurrentMonth(canvas, rect, nDate);
                    }
                } else { //��������֮�������
                    painter.onDrawDisableDate(canvas, rect, nDate);
                }
            }
        }
    }

    //��ȡÿ��Ԫ�ؾ���
    private Rect getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect;
        //5�е��·ݣ�5�о���ƽ��view�ĸ߶�  mLineNum==1���ܵ����
        if (mLineNum == 5 || mLineNum == 1) {
            int rectHeight = height / mLineNum;
            rect = new Rect(j * width / 7, i * rectHeight, j * width / 7 + width / 7, i * rectHeight + rectHeight);
        } else {
            //6�е��·ݣ�Ҫ��һ�к����һ�о��ε����ķֱ�ͺ�5���·ݵ�һ�к����һ�о��ε����Ķ���
            //5��һ�����θ߶� mHeight/5, ��ͼ��֪,4��5�о��εĸ߶ȵ���5��6�о��εĸ߶�  �ʣ�6�е�ÿһ�����θ߶���  (mHeight/5)*4/5
            int rectHeight5 = height / 5;
            int rectHeight6 = (height / 5) * 4 / 5;
            rect = new Rect(j * width / 7, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, j * width / 7 + width / 7, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
        }
        return rect;
    }


    /**
     * �õ���ǰҳ������ݣ��ܺ���
     *
     * @param initialDate ��ʼ����ǰҳ�����ݵ�date
     * @param type        һ�ܿ�ʼ�����ջ�����һ
     * @return
     */
    protected abstract List<NDate> getNCalendar(LocalDate initialDate, int type);


    /**
     * ����¼�
     *
     * @param clickNData  �����date
     * @param initialDate ��ʼ����ǰҳ���date
     */
    protected abstract void onClick(NDate clickNData, LocalDate initialDate);


    //��ʼ�������ںͻ��Ƶ������Ƿ���ͬ�£��ܶ���ͬ
    public abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


    //��ȡ��ǰҳ��ĳ�ʼ����
    public LocalDate getInitialDate() {
        return mInitialDate;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    NDate clickDate = mDateList.get(i);
                    onClick(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });


    //����ѡ�е����� ������
    public void setSelectDate(LocalDate localDate, boolean isDraw) {
        //Ĭ��ѡ�к�isDraw������һ�ͻ���
        this.isDraw = isDraw;
        this.mSelectDate = localDate;
        invalidate();
    }

    //ѡ�е����ڵ������ľ���
    public int getMonthCalendarOffset() {
        int monthCalendarOffset;
        //ѡ�е��ǵڼ���
        int selectIndex = mDateList.indexOf(new NDate(mSelectDate)) / 7;
        if (mLineNum == 5) {
            //5�е��·�
            monthCalendarOffset = getMeasuredHeight() / 5 * selectIndex;
        } else {
            // int rectHeight5 = getMeasuredHeight() / 5;
            int rectHeight6 = (getMeasuredHeight() / 5) * 4 / 5;
            monthCalendarOffset = rectHeight6 * selectIndex;
        }
        return monthCalendarOffset;
    }


    //�Ƿ��ǵ�������
    public boolean contains(LocalDate localDate) {
        if (localDate == null) {
            return false;
        } else if (isEqualsMonthOrWeek(localDate, mInitialDate)) {
            return true;
        } else {
            return false;
        }
    }
}
