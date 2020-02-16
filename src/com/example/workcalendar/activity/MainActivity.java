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
	
	private final String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日",};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消屏幕顶标题栏
		setContentView(R.layout.activity_main);
		
		calendar = (MonthCalendar) findViewById(R.id.miui10Calendar);
		
		tv_month = (TextView) findViewById(R.id.tv_month);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_year = (TextView) findViewById(R.id.tv_year);
        
        // 点击月份回到今日
        tv_month.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				calendar.toToday();
			}
        });
        
        // 点击日历调整年月日文字的显示
        calendar.setOnMonthSelectListener(new OnMonthSelectListener(){
			@Override
			public void onMonthSelect(NDate date, boolean isClick) {
				LocalDate localDate = date.localDate;
				tv_year.setText(localDate.getYear()+"");
				tv_month.setText(localDate.getMonthOfYear()+"");
				tv_week.setText(weeks[localDate.getDayOfWeek() - 1]);
			}
        });

        
        // 联网获取启动页提示文字
        new Thread(new Runnable(){
        	@Override
			public void run() {
        		String key = "jianglijie";
        		
        		SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("tip", MODE_PRIVATE).edit();
				
				// 从网络读取数据，如果有则将数据保存到本地，否则删除本地原有数据
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
        
        // 联网或从本地获取日历
        painter = (InnerPainter)calendar.getCalendarPainter();
        new Thread(new Runnable(){
			@Override
			public void run() {
				String key = "jianglijie";
				
				// 清空原有的节假日和补班日
				painter.setHolidayAndWorkdayList(new ArrayList<String>(), new ArrayList<String>());
				
				// 先从本地文件读取数据，如果有则应用数据
				List<String> holidayList = DataUtil.GetHolidayListFromFile(MainActivity.this, key);
				if(holidayList.size() != 0)
					painter.setHolidayAndWorkdayList(holidayList, null);
				
				Map<String, String> replaceLunarStrMap = DataUtil.GetReplaceLunarStrMapFromFile(MainActivity.this, key);
				if(replaceLunarStrMap.size() != 0)
					painter.setReplaceLunarStrMap(replaceLunarStrMap);

				Map<String, Integer> replaceLunarColorMap= DataUtil.GetReplaceLunarColorMapFromFile(MainActivity.this, key);
				if(replaceLunarColorMap.size() != 0)
					painter.setReplaceLunarColorMap(replaceLunarColorMap);
				
				// 从网络读取数据，如果有则应用数据并将数据保存到本地
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
