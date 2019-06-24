package com.example.workcalendar.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class DataUtil {

	private static final String HOLIDAY_FILE = "_holiday.dat";
	private static final String REPLACE_STR_FILE = "_replace_string.dat";
	private static final String REPLACE_COLOR_FILE = "_replace_color.dat";

	private static final String DOMAIN_NAME = "http://47.107.96.73:16688";

	private static boolean SaveDataToFile(Context context, String key,
			String filename, Object object) {
		try {
			FileOutputStream out = context.openFileOutput(key + filename,
					Context.MODE_PRIVATE);
			ObjectOutputStream objectout = new ObjectOutputStream(out);

			objectout.writeObject(object);

			objectout.close();
			out.close();
		} catch (Exception exp) {
			return false;
		}
		return true;
	}

	private static Object GetDataFromFile(Context context, String key,
			String filename) {
		try {
			FileInputStream in = context.openFileInput(key + filename);
			ObjectInputStream objectin = new ObjectInputStream(in);

			return objectin.readObject();
		} catch (Exception exp) {
			return null;
		}
	}

	public static boolean SaveHolidayListToFile(Context context, String key,
			List<String> holidayList) {
		return SaveDataToFile(context, key, HOLIDAY_FILE, holidayList);
	}

	@SuppressWarnings("unchecked")
	public static List<String> GetHolidayListFromFile(Context context,
			String key) {
		List<String> holidayList = new ArrayList<String>();
		Object obj = GetDataFromFile(context, key, HOLIDAY_FILE);
		if (obj != null)
			holidayList = (List<String>) obj;
		return holidayList;
	}

	public static boolean SaveReplaceLunarStrMapToFile(Context context,
			String key, Map<String, String> replaceStrMap) {
		return SaveDataToFile(context, key, REPLACE_STR_FILE, replaceStrMap);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> GetReplaceLunarStrMapFromFile(
			Context context, String key) {
		Map<String, String> replaceStrMap = new HashMap<String, String>();
		Object obj = GetDataFromFile(context, key, REPLACE_STR_FILE);
		if (obj != null)
			replaceStrMap = (Map<String, String>) obj;
		return replaceStrMap;
	}

	public static boolean SaveReplaceLunarColorMapToFile(Context context,
			String key, Map<String, Integer> replaceColorMap) {
		return SaveDataToFile(context, key, REPLACE_COLOR_FILE, replaceColorMap);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Integer> GetReplaceLunarColorMapFromFile(
			Context context, String key) {
		Map<String, Integer> replaceColorMap = new HashMap<String, Integer>();
		Object obj = GetDataFromFile(context, key, REPLACE_COLOR_FILE);
		if (obj != null)
			replaceColorMap = (Map<String, Integer>) obj;
		return replaceColorMap;
	}

	public static List<String> GetHolidayListFromNet(String key) {
		List<String> holidayList = new ArrayList<String>();

		try {
			HttpURLConnection connect = connectURL("/workcalendar/getholidaylist/"
					+ key);
			if (connect != null) {
				String resp = getJSONString(connect);
				connect.disconnect();
				JSONArray jsonarray = new JSONArray(resp);
				for (int i = 0; i < jsonarray.length(); ++i)
					holidayList.add(jsonarray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return holidayList;
	}

	public static Map<String, String> GetReplaceLunarStrMapFromNet(String key) {
		Map<String, String> replaceStrMap = new HashMap<String, String>();

		try {
			HttpURLConnection connect = connectURL("/workcalendar/getreplacestringmap/"
					+ key);
			if (connect != null) {
				String resp = getJSONString(connect);
				connect.disconnect();
				JSONObject jsonobject = new JSONObject(resp);
				Iterator<String> keyIter = jsonobject.keys();
				String k;
				while (keyIter.hasNext()) {
					k = (String) keyIter.next();
					replaceStrMap.put(k, jsonobject.getString(k));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return replaceStrMap;
	}

	public static Map<String, Integer> GetReplaceLunarColorMapFromNet(String key) {
		Map<String, Integer> replaceColorMap = new HashMap<String, Integer>();

		try {
			HttpURLConnection connect = connectURL("/workcalendar/getreplacecolormap/"
					+ key);
			if (connect != null) {
				String resp = getJSONString(connect);
				connect.disconnect();
				JSONObject jsonobject = new JSONObject(resp);
				Iterator<String> keyIter = jsonobject.keys();
				String k;
				while (keyIter.hasNext()) {
					k = (String) keyIter.next();
					replaceColorMap.put(k, jsonobject.getInt(k));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return replaceColorMap;
	}

	// 传入路径名(不包含协议头和域名)，打开并设置HTTP连接
	private static HttpURLConnection connectURL(String str_url) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(DOMAIN_NAME + str_url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setConnectTimeout(3000);
			connection.connect();
		} catch (IOException e) {
			return null;
		}
		return connection;
	}

	// 获取返回的 JSON 数据
	private static String getJSONString(HttpURLConnection connection) {
		String content = "";
		try {
			if (connection.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(
						connection.getInputStream());
				BufferedReader reader = new BufferedReader(in);

				String line;
				while ((line = reader.readLine()) != null)
					content += line;
			}
		} catch (IOException e) {
			content = "";
		}

		return content;
	}
}
