package com.jian.tools.core;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class DateTools {
	
	private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	
	private final static int TIME_MILLIS = 1;
	private final static int TIME_SECONDS = 2;
	private final static int TIME_MINUTES = 3;
	private final static int TIME_HOURS = 4;
	private final static int TIME_DAYS = 5;
	
	public static Logger logger = LoggerFactory.getLogger(DateTools.class);
	
	
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

	/**
	 * 解析出字符串中的日期。支持格式：yyyy.M+.d+、yy.M+.d+、yyyy.M+d?、yyyyM+d?
	 * @param str
	 * @return
	 */
	public static DateObject subDateStr(String str){
		int[] maxDay = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		DateObject dateObj = null;
		String r = null;
		//标准：yyyy、yy年M+月d+日，有分隔符。如：2022.4.13，22.04.13
		String regEx = "[0-9]+[.][0-9]+[.][0-9]+";
		Pattern pat = Pattern.compile(regEx); 
		Matcher mat = pat.matcher(str); 
		while (mat.find()) {
			r = mat.group();
			logger.debug(r);
			String[] t = r.split("[.]");
			if(!(t[0].length() == 2 || t[0].length() == 4) || t[1].length() > 2 || t[2].length() > 2) {
				logger.debug(String.format("%s 不是日期格式。", r));
				continue;
			}
			int y = t[0].length() == 2 ? Integer.parseInt((LocalDateTime.now().getYear()+"").substring(0, 2) + t[0]) : Integer.parseInt(t[0]);
			int m = Integer.parseInt(t[1]);
			int d = Integer.parseInt(t[2]);
			dateObj = tryCreateDateObject(r, maxDay, y, m, d);
			break;
		}
		if(r != null) {
			return dateObj;
		}
		//非标准：yyyy年M+月d?日，有分隔符。如：2022.413，2022.0413，2022.113，2022.11
		r = null;
		regEx = "[0-9]+[.][0-9]+";
		pat = Pattern.compile(regEx); 
		mat = pat.matcher(str); 
		while (mat.find()) {
			r = mat.group();
			logger.debug(r);
			String[] t = r.split("[.]");
			if(!(t[0].length() == 4) || t[1].length() > 4) {
				logger.debug(String.format("%s 不是日期格式。", r));
				continue;
			}
			int y = Integer.parseInt(t[0]);
			//第一次尝试，月份两位数
			int m = t[1].length() <= 2 ? Integer.parseInt(t[1]) : Integer.parseInt(t[1].substring(0, 2));
			int d = t[1].length() <= 2 ? 0 : Integer.parseInt(t[1].substring(2));
			dateObj = tryCreateDateObject("第一次尝试 " + r, maxDay, y, m, d);
			if(dateObj ==  null) {
				//第二次尝试，月份一位数
				m = t[1].length() < 2 ? Integer.parseInt(t[1]) : Integer.parseInt(t[1].substring(0, 1));
				d = t[1].length() < 2 ? 0 : Integer.parseInt(t[1].substring(1));
				dateObj = tryCreateDateObject("第二次尝试 " + r, maxDay, y, m, d);
			}
			break;
		}
		if(r != null) {
			return dateObj;
		}
		//标准：yyyy年M+月d?日，无分隔符。如：20220413，2022413，202241，20224，202211，202219
		r = null;
		regEx = "[0-9]{5,}";
		pat = Pattern.compile(regEx); 
		mat = pat.matcher(str); 
		while (mat.find()) {
			r = mat.group();
			logger.debug(r);
			int y = Integer.parseInt(r.substring(0, 4));
			//第一次尝试，月份两位数
			int m = r.length() <= 6 ? Integer.parseInt(r.substring(4)) : Integer.parseInt(r.substring(4, 6));
			int d = r.length() <= 6 ? 0 : Integer.parseInt(r.substring(6));
			dateObj = tryCreateDateObject("第一次尝试 " + r, maxDay, y, m, d);
			if(dateObj ==  null) {
				//第二次尝试，月份一位数
				m = r.length() < 6 ? Integer.parseInt(r.substring(4)) : Integer.parseInt(r.substring(4, 5));
				d = r.length() < 6 ? 0 : Integer.parseInt(r.substring(5));
				dateObj = tryCreateDateObject("第二次尝试 " + r, maxDay, y, m, d);
			}
			break;
		}
		if(r != null) {
			return dateObj;
		}
		//标准：M+月d+日，有分隔符。如：04.13，4.13，4.1
		regEx = "[0-9]+[.]+[0-9]+";
		//标准：M+月d+日，无分隔符。如：0413，413，41
		regEx = "[0-9]{2,}";
		return dateObj;
	}
	
	private static DateObject tryCreateDateObject(String r, int[] maxDay, int y, int m, int d) {
		if(m > 12) {
			logger.debug(String.format("%s 不是日期格式。其中月份 %s 大于12。", r, m));
			return null;
		}
		if( y%4 == 0 && m == 2 && d > 29 || y%4 != 0 && d > maxDay[m]) {
			logger.debug(String.format("%s 不是日期格式。其中 %s 月份的最大天数超过 %s 天。为 %s 天", r, m, maxDay[m], d));
			return null;
		}
		logger.debug(String.format("%s 解析成功。 年：%s，月：%s，日：%s", r, y, m, d));
		return new DateObject(y, m, d);
	}
	
	public static void main(String[] args) {
		
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
		loggerList.forEach(logger -> {
		    logger.setLevel(Level.DEBUG);
		});

		subDateStr("D10_49_WT_2022.3.29");
		subDateStr("D10_44_wt_22.3.16test");
		subDateStr("D10_49_WT_2022.32.29");
		subDateStr("D10_49_WT_2023.2.29");
		subDateStr("D10_49_WT_20222.3.29");
		System.out.println("---------------标准：yyyy、yy年M+月d+日，有分隔符。如：2022.4.13，22.04.13------------------");
		
		subDateStr("D10_36_2022.0209");
		subDateStr("D10_36_2022.029");
		subDateStr("D10_36_2022.209");
		subDateStr("D10_36_2022.29");
		subDateStr("D10_36_2022.11");
		subDateStr("D10_36_2022.259");
		subDateStr("D10_36_20221.259");
		subDateStr("D10_36_22.259");
		System.out.println("---------------非标准：yyyy年M+月d?日，有分隔符。如：2022.413，2022.0413，2022.113，2022.11------------------");
		
		subDateStr("D10_43_WT_20220304");
		subDateStr("D10_43_WT_2022304");
		subDateStr("D10_43_WT_202234");
		subDateStr("D10_43_WT_20223");
		subDateStr("D10_43_WT_202211");
		subDateStr("D10_43_WT_202219");
		subDateStr("D10_43_WT_201114");
		subDateStr("D10_43_WT_201191");
		System.out.println("---------------标准：年月日无分隔符。如：20220413，2022413，202241，20224，202211，202219------------------");
		
		subDateStr("");
		System.out.println(subDateStr("D10_43_WT_20220304").getDateStr());
		System.out.println(subDateStr("D10_43_WT_20220304").getDateStr("yyyy/MM/dd"));
		System.out.println(subDateStr("D10_43_WT_20220304").getDateStr("yyyy-MM-dd"));
		System.out.println(subDateStr("D10_43_WT_20220304").getDateStr("yyyy.MM.dd"));
		System.out.println(subDateStr("UDavinciRecord/WTRecorder_lfs/D10_52_wt_2022.4.1/").getDateStr("yyyyMMdd"));
		System.out.println(subDateStr("D10_43_WT_202203").getDateStr("yyyyMM"));
		System.out.println( formatDate(subDateStr("D10_43_WT_2022032").getDate()) );
		System.out.println( formatDate(subDateStr("D10_43_WT_202203").getDate()) );
		System.out.println("---------------getDateStr、getDate------------------");
		
//		subDateStr("D10_27_WT_99.1");
//		subDateStr("D10_32_WT_12.21");
//		subDateStr("D10_30_10.28wt-nanzhuB");
//		subDateStr("D10_34_wt_0105");
	}
	
	public static class DateObject {
		
		private int y;
		private int m;
		private int d;
		
		private final String DATE_FORMAT_STR = "yyyy-MM-dd";

		public DateObject(int y, int m, int d) {
			super();
			this.y = y;
			this.m = m;
			this.d = d;
		}

		public Date getDate() {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, y);
			calendar.set(Calendar.MONTH, m == 0 ? 0 : m - 1);
			calendar.set(Calendar.DAY_OF_MONTH, d == 0 ? 1 : d);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		
		public String getDateStr() {
			return getDateStr(DATE_FORMAT_STR);
		}
		
		public String getDateStr(String formatStr) {
			formatStr = formatStr.replace("yyyy", y + "");
			formatStr = formatStr.replace("MM", m < 10 ? "0" + m : m + "");
			formatStr = formatStr.replace("dd", d < 10 ? "0" + d : d + "");
			return formatStr;
		}

		public int getY() {
			return y;
		}

		public DateObject setY(int y) {
			this.y = y;
			return this;
		}

		public int getM() {
			return m;
		}

		public DateObject setM(int m) {
			this.m = m;
			return this;
		}

		public int getD() {
			return d;
		}

		public DateObject setD(int d) {
			this.d = d;
			return this;
		}
		
	}
	
}
