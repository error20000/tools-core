package com.jian.tools.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTools {
	
	private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	
	private final static int TIME_MILLIS = 1;
	private final static int TIME_SECONDS = 2;
	private final static int TIME_MINUTES = 3;
	private final static int TIME_HOURS = 4;
	private final static int TIME_DAYS = 5;
	
	
	/**
	 * 格式化日期
	 * @return 返回当前日期，格式：{@link DateTools#DATE_FORMAT_STR}
	 */
	public static String formatDate(){
		return formatDate(DATE_FORMAT_STR);
	}
	
	/**
	 * 格式化日期
	 * @param str 日期格式
	 * @return 返回当前日期.
	 */
	public static String formatDate(String str){
		return formatDate(null, str);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @return 返回传入日期，格式：{@link DateTools#DATE_FORMAT_STR}
	 */
	public static String formatDate(Date date){
		return formatDate(date, DATE_FORMAT_STR);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @param str 返回日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回传入日期，传入格式。
	 */
	public static String formatDate(Date date, String str){
		if(date == null){
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
		}
		str = Tools.isNullOrEmpty(str) ? DATE_FORMAT_STR : str;
		return new SimpleDateFormat(str).format(date);
	}
	
	
	/**
	 * 格式化为日期
	 * @param time 时间，类型：毫秒
	 * @return 返回日期。
	 */
	public static Date formatDate(long time){
		return formatDate(time, TIME_MILLIS);
	}
	
	/**
	 * 格式化为日期
	 * @param time 时间
	 * @param timeType 时间类型
	 * @return 返回日期。
	 */
	public static Date formatDate(long time, int timeType){
		Calendar calendar = Calendar.getInstance();
		switch (timeType) {
		case TIME_MILLIS:
			calendar.setTimeInMillis(time);
			break;
		case TIME_SECONDS:
			calendar.setTimeInMillis(time * 1000);
			break;
		case TIME_MINUTES:
			calendar.setTimeInMillis(time * 60 * 1000);
			break;
		case TIME_HOURS:
			calendar.setTimeInMillis(time * 60 * 60 * 1000);
			break;
		case TIME_DAYS:
			calendar.setTimeInMillis(time * 24 * 60 * 60 * 1000);
			break;

		default:
			System.out.println("格式化为日期有误，不支持该时间类型！！！");
			break;
		}
		return calendar.getTime();
	}
	
	/**
	 * 格式化为日期
	 * @param date 日期 ，默认格式：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回日期。
	 */
	public static Date formatDateStr(String date){
		return formatDateStr(date, DATE_FORMAT_STR);
	}

	/**
	 * 格式化为日期
	 * @param date 日期 
	 * @param str 日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回日期。
	 */
	public static Date formatDateStr(String date, String str){
		if(Tools.isNullOrEmpty(date)){
			System.out.println("日期不能为空！！！");
			return null;
		}
		str = Tools.isNullOrEmpty(str) ? DATE_FORMAT_STR : str;
		if(!date.matches(str
				.replace("y", "[0-9]")
				.replace("M", "[0-9]")
				.replace("d", "[0-9]")
				.replace("H", "[0-9]")
				.replace("m", "[0-9]")
				.replace("s", "[0-9]")
				.replace("S", "[0-9]"))){
			System.out.println("日期与日期格式不匹配，无法格式化！！！");
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		
		int year = subRegEx(date, str, "y+");
		if(year != -1 )
			calendar.set(Calendar.YEAR, year);
		
		int month = subRegEx(date, str, "M+");
		month = month == -1 ? 1 : month;
		calendar.set(Calendar.MONTH, month - 1);
		
		int day = subRegEx(date, str, "d+");
		day = day == -1 ? 1 : day;
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		int hour = subRegEx(date, str, "H+");
		hour = hour == -1 ? 0 : hour;
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		
		int minute = subRegEx(date, str, "m+");
		minute = minute == -1 ? 0 : minute;
		calendar.set(Calendar.MINUTE, minute);
		
		int second = subRegEx(date, str, "s+");
		second = second == -1 ? 0 : second;
		calendar.set(Calendar.SECOND, second);
		
		int millis = subRegEx(date, str, "S+");
		millis = millis == -1 ? 0 : millis;
		calendar.set(Calendar.MILLISECOND, millis);
		
		return calendar.getTime();
	}

	/**
	 * 日期相减 	date1 - date2
	 * @param date1 日期 格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @param date2 日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回日期。
	 */
	public static long reduceDateStr(String date1, String date2){
		return formatDateStr(date1, DATE_FORMAT_STR).getTime() - formatDateStr(date2, DATE_FORMAT_STR).getTime();
	}


	/**
	 * 日期相减 	date1 - date2
	 * @param date1 日期
	 * @param date2 日期
	 * @param str 日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回日期。
	 */
	public static long reduceDateStr(String date1, String date2, String str){
		return formatDateStr(date1, str).getTime() - formatDateStr(date2, str).getTime();
	}


	/**
	 * 日期相减 	date1 - date2
	 * @param date1 日期
	 * @param str1 日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @param date2 日期
	 * @param str2 日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回日期。
	 */
	public static long reduceDateStr(String date1, String str1, String date2, String str2){
		return formatDateStr(date1, str1).getTime() - formatDateStr(date2, str2).getTime();
	}
	
	
	private static int subRegEx(String date, String str, String regEx){
		int r = -1;
		Pattern pat = Pattern.compile(regEx); 
		Matcher mat = pat.matcher(str); 
		while (mat.find()) {
			r = Tools.parseInt(date.substring(mat.start(), mat.end()));
			break;
		}
		return r;
	}
	
	
}
