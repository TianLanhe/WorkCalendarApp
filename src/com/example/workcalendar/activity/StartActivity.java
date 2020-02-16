package com.example.workcalendar.activity;

import com.example.workcalendar.R;
import android.app.Activity;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消屏幕顶标题栏
		setContentView(R.layout.activity_start);

		String tip = getTip();
		if ("".equals(tip)) {
			turnToMainActivity();
		} else {
			TextView tv = (TextView) findViewById(R.id.tv_tip);
			tv.setText(tip);
			
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					turnToMainActivity();
				}
			}, 600L);
		}
	}
	
	private void turnToMainActivity(){
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(intent);
		finish();
	}

	private String getTip() {
		SharedPreferences pref = getSharedPreferences("tip", MODE_PRIVATE);
		String ret = pref.getString("jianglijie", "");
		return ret;
	}

}
