package com.example.workcalendar.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.example.workcalendar.R;
import com.example.workcalendar.listener.OnMonthSelectListener;
import com.example.workcalendar.model.NDate;
import com.example.workcalendar.painter.InnerPainter;
import com.example.workcalendar.utils.DataUtil;
import com.example.workcalendar.view.MonthCalendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private MonthCalendar calendar;
	private InnerPainter painter;
	
	private TextView tv_month;
	private TextView tv_week;
	private TextView tv_year;
	
	private final String[] weeks = {"��һ", "�ܶ�", "����", "����", "����", "����", "����",};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȡ����Ļ��������
		setContentView(R.layout.activity_main);
		
		calendar = (MonthCalendar) findViewById(R.id.miui10Calendar);
		
		tv_month = (TextView) findViewById(R.id.tv_month);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_year = (TextView) findViewById(R.id.tv_year);
        
        // ����·ݻص�����
        tv_month.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				calendar.toToday();
			}
        });
        
        // ��������������������ֵ���ʾ
        calendar.setOnMonthSelectListener(new OnMonthSelectListener(){
			@Override
			public void onMonthSelect(NDate date, boolean isClick) {
				LocalDate localDate = date.localDate;
				tv_year.setText(localDate.getYear()+"");
				tv_month.setText(localDate.getMonthOfYear()+"");
				tv_week.setText(weeks[localDate.getDayOfWeek() - 1]);
			}
        });

        
        // ������ȡ����ҳ��ʾ����
        new Thread(new Runnable(){
        	@Override
			public void run() {
        		String key = "jianglijie";
        		
        		SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("tip", MODE_PRIVATE).edit();
				
				// �������ȡ���ݣ�����������ݱ��浽���أ�����ɾ������ԭ������
				try {
					String tip;
					tip = DataUtil.GetTipFromNet(key);
					if(tip.equals("")){
						editor.remove(key);
					}else{
						editor.putString(key, tip);
					}
					editor.commit();
				} catch (IOException e) {
				}
        	}
        }).start();
        
        // ������ӱ��ػ�ȡ����
        painter = (InnerPainter)calendar.getCalendarPainter();
        new Thread(new Runnable(){
			@Override
			public void run() {
				String key = "jianglijie";
				
				// ���ԭ�еĽڼ��պͲ�����
				painter.setHolidayAndWorkdayList(new ArrayList<String>(), new ArrayList<String>());
				
				// �ȴӱ����ļ���ȡ���ݣ��������Ӧ������
				List<String> holidayList = DataUtil.GetHolidayListFromFile(MainActivity.this, key);
				if(holidayList.size() != 0)
					painter.setHolidayAndWorkdayList(holidayList, null);
				
				Map<String, String> replaceLunarStrMap = DataUtil.GetReplaceLunarStrMapFromFile(MainActivity.this, key);
				if(replaceLunarStrMap.size() != 0)
					painter.setReplaceLunarStrMap(replaceLunarStrMap);

				Map<String, Integer> replaceLunarColorMap= DataUtil.GetReplaceLunarColorMapFromFile(MainActivity.this, key);
				if(replaceLunarColorMap.size() != 0)
					painter.setReplaceLunarColorMap(replaceLunarColorMap);
				
				// �������ȡ���ݣ��������Ӧ�����ݲ������ݱ��浽����
				holidayList = DataUtil.GetHolidayListFromNet(key);
				if(holidayList.size() != 0){
					painter.setHolidayAndWorkdayList(holidayList, null);
					DataUtil.SaveHolidayListToFile(MainActivity.this,key , holidayList);
				}
				
				replaceLunarStrMap = DataUtil.GetReplaceLunarStrMapFromNet(key);
				if(replaceLunarStrMap.size() != 0){
					painter.setReplaceLunarStrMap(replaceLunarStrMap);
					DataUtil.SaveReplaceLunarStrMapToFile(MainActivity.this, key, replaceLunarStrMap);
				}

				replaceLunarColorMap= DataUtil.GetReplaceLunarColorMapFromNet(key);
				if(replaceLunarColorMap.size() != 0){
					painter.setReplaceLunarColorMap(replaceLunarColorMap);
					DataUtil.SaveReplaceLunarColorMapToFile(MainActivity.this, key, replaceLunarColorMap);
				}
				
				calendar.invalidate();
			}
        }).start();
	}
}
