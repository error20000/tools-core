package com.jian.tools.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Tools {
	
	
	//反射
	private static Map<String, Field> fields = new HashMap<String ,Field>();
	private static Map<String, Method> methods = new HashMap<String ,Method>();
	private static Map<String, Field[]> fieldArrays = new HashMap<String ,Field[]>();
	private static Map<String, Method[]> methodArrays = new HashMap<String ,Method[]>();
	private static Map<String, List<Class<?>>> classLists = new HashMap<String , List<Class<?>>>();

	//写文件线程池
	private static ExecutorService service = Executors.newFixedThreadPool(10);
	private static Lock lock = new ReentrantLock();
	
	//可配置信息
	private static String initDateFormatStr = "yyyy-MM-dd HH:mm:ss";	//格式化时间的格式，默认值："yyyy-MM-dd HH:mm:ss"
	private static String initCharsetName = "utf-8";	//统一字符编码，默认值："utf-8"
	private static String initAttackLogPath = "attacks/request/";	//记录SQL注入日志文件路径，默认值："attacks/request/"
	
	

	//TODO 基本的
	/**
	 * 判断是否为空串
	 * @param str
	 * @return true表示为null或""
	 */
	public static boolean isNullOrEmpty(Object str){
		if(str == null || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	public static int parseInt(Object str){
		if(isNullOrEmpty(str)){
			return 0;
		}else if(str instanceof String){
			return parseNumber((String) str).intValue();
		}else if(str instanceof Integer){
			return ((Integer) str).intValue();
		}else if(str instanceof Double){
			return ((Double) str).intValue();
		}else if(str instanceof Float){
			return ((Float) str).intValue();
		}else if(str instanceof Long){
			return ((Long) str).intValue();
		}else if(str instanceof Byte){
			return ((Byte) str).intValue();
		}else if(str instanceof Short){
			return ((Short) str).intValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).intValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).intValue();
		}else if(str instanceof Character){
			return Character.isDigit((Character) str) ? Character.getNumericValue((Character) str) : (int) (char) str;  
		}else{
			return 0;
		}
	}
	
	public static long parseLong(Object str){
		if(isNullOrEmpty(str)){
			return 0L;
		}else if(str instanceof String){
			return parseNumber((String) str).longValue();
		}else if(str instanceof Integer){
			return ((Integer) str).longValue();
		}else if(str instanceof Double){
			return ((Double) str).longValue();
		}else if(str instanceof Float){
			return ((Float) str).longValue();
		}else if(str instanceof Long){
			return ((Long) str).longValue();
		}else if(str instanceof Byte){
			return ((Byte) str).longValue();
		}else if(str instanceof Short){
			return ((Short) str).longValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).longValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).longValue();
		}else{
			return 0L;
		}
	}
	
	public static float parseFloat(Object str){
		if(isNullOrEmpty(str)){
			return 0F;
		}else if(str instanceof String){
			return parseNumber((String) str).floatValue();
		}else if(str instanceof Integer){
			return ((Integer) str).floatValue();
		}else if(str instanceof Double){
			return ((Double) str).floatValue();
		}else if(str instanceof Float){
			return ((Float) str).floatValue();
		}else if(str instanceof Long){
			return ((Long) str).floatValue();
		}else if(str instanceof Byte){
			return ((Byte) str).floatValue();
		}else if(str instanceof Short){
			return ((Short) str).floatValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).floatValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).floatValue();
		}else{
			return 0F;
		}
	}
	
	public static double parseDouble(Object str){
		if(isNullOrEmpty(str)){
			return 0D;
		}else if(str instanceof String){
			return parseNumber((String) str).doubleValue();
		}else if(str instanceof Integer){
			return ((Integer) str).doubleValue();
		}else if(str instanceof Double){
			return ((Double) str).doubleValue();
		}else if(str instanceof Float){
			return ((Float) str).doubleValue();
		}else if(str instanceof Long){
			return ((Long) str).doubleValue();
		}else if(str instanceof Byte){
			return ((Byte) str).doubleValue();
		}else if(str instanceof Short){
			return ((Short) str).doubleValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).doubleValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).doubleValue();
		}else{
			return 0D;
		}
	}

	public static short parseShort(Object str){
		if(isNullOrEmpty(str)){
			return 0;
		}else if(str instanceof String){
			return parseNumber((String) str).shortValue();
		}else if(str instanceof Integer){
			return ((Integer) str).shortValue();
		}else if(str instanceof Double){
			return ((Double) str).shortValue();
		}else if(str instanceof Float){
			return ((Float) str).shortValue();
		}else if(str instanceof Long){
			return ((Long) str).shortValue();
		}else if(str instanceof Byte){
			return ((Byte) str).shortValue();
		}else if(str instanceof Short){
			return ((Short) str).shortValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).shortValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).shortValue();
		}else{
			return 0;
		}
	}

	public static byte parseByte(Object str){
		if(isNullOrEmpty(str)){
			return 0;
		}else if(str instanceof String){
			return parseNumber((String) str).byteValue();
		}else if(str instanceof Integer){
			return ((Integer) str).byteValue();
		}else if(str instanceof Double){
			return ((Double) str).byteValue();
		}else if(str instanceof Float){
			return ((Float) str).byteValue();
		}else if(str instanceof Long){
			return ((Long) str).byteValue();
		}else if(str instanceof Byte){
			return ((Byte) str).byteValue();
		}else if(str instanceof Short){
			return ((Short) str).byteValue();
		}else if(str instanceof BigInteger){
			return ((BigInteger) str).byteValue();
		}else if(str instanceof BigDecimal){
			return ((BigDecimal) str).byteValue();
		}else{
			return 0;
		}
	}

	public static BigInteger parseBigInteger(Object str){
		if(isNullOrEmpty(str)){
			return new BigInteger("0");
		}else if(str instanceof String){
			return new BigInteger((String) str);
		}else if(str instanceof Integer){
			return new BigInteger(((Integer) str).toString());
		}else if(str instanceof Double){
			return new BigInteger(parseString(parseInt(str)));
		}else if(str instanceof Float){
			return new BigInteger(parseString(parseInt(str)));
		}else if(str instanceof Long){
			return BigInteger.valueOf((Long) str);
		}else if(str instanceof Byte){
			return new BigInteger(parseString(parseInt(str)));
		}else if(str instanceof Short){
			return new BigInteger(parseString(parseInt(str)));
		}else{
			return new BigInteger("0");
		}
	}
	
	public static boolean parseBoolean(Object str){
		if(isNullOrEmpty(str)){
			return false;
		}else if(str instanceof String){
			return Boolean.parseBoolean((String) str);
		}else if(str instanceof Integer){
			return ((Integer) str) == 0 ? false : true;
		}else if(str instanceof Double){
			return ((Double) str) == 0 ? false : true;
		}else if(str instanceof Float){
			return ((Float) str) == 0 ? false : true;
		}else if(str instanceof Long){
			return ((Long) str) == 0 ? false : true;
		}else if(str instanceof Byte){
			return ((Byte) str) == 0 ? false : true;
		}else if(str instanceof Short){
			return ((Short) str) == 0 ? false : true;
		}else{
			return false;
		}
	}
	
	public static Number parseNumber(String str){
		Number number = 0;
		try {
			number = NumberFormat.getInstance().parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return number;
	}

	public static String parseString(Object str){
		return String.valueOf(str);
	}
	
	/**
	 * 格式化字符串
	 * @param str  null转""
	 * @return
	 */
	public static String formatString(String str){
		if(isNullOrEmpty(str)){
			return "";
		}else{
			return str;
		}
	}
	
	/**
	 * 提取字符串中的数字
	 * @param str  null转""
	 * @return
	 */
	public static String takenNumber(String str){
		if(isNullOrEmpty(str)){
			return "";
		}else{
			str = str.replaceAll("[^0-9]", "");
			return str;
		}
	}
	
	/**
	 * 格式化日期
	 * @return 返回当前日期，格式：initDateFormatStr
     * @deprecated 请使用{@code DateTools}。 参见{@link DebugTools#formatDate()}
	 */
	@Deprecated
	public static String formatDate(){
		return formatDate(initDateFormatStr);
	}
	
	/**
	 * 格式化日期
	 * @param str 日期格式
	 * @return 返回当前日期.
     * @deprecated 请使用{@code DateTools}。 参见{@link DebugTools#formatDate(String)}
	 */
	@Deprecated
	public static String formatDate(String str){
		return formatDate(null, str);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @return 返回传入日期，格式：initDateFormatStr
     * @deprecated 请使用{@code DateTools}。 参见{@link DebugTools#formatDate(Date)}
	 */
	@Deprecated
	public static String formatDate(Date date){
		return formatDate(date, initDateFormatStr);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @param str 返回日期格式，默认：initDateFormatStr
	 * @return 返回传入日期，传入格式。
     * @deprecated 请使用{@code DateTools}。 参见{@link DebugTools#formatDate(Date, String)}
	 */
	@Deprecated
	public static String formatDate(Date date, String str){
		if(date == null){
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
//			date = new Date();
		}
		str = isNullOrEmpty(str) ? initDateFormatStr : str;
		return new SimpleDateFormat(str).format(date);
	}
	
	
	/**
	 * 生成随机数
	 * @param length 随机数长度
	 * @return 返回字符串类型
	 */
	public static String createVCodeNumber(int length) {
		return createVCode(length, "num", false, "");
	}
	
	/**
	 * 生成随机字符串
	 * @param length 随机字符串长度
	 * @param capType 字母大小写。"A"大写，"a"小写，其他随机大小写
	 * @return 返回字符串类型
	 */
	public static String createVCodeString(int length, String capType) {
		return createVCode(length, "char", false, capType);
	}
	
	/**
	 * 生成随机字符串
	 * @param length 随机字符串长度
	 * @param codeType 字符类型。"num"数字，"char"字母，其他随机数字、字母
	 * @param ram {@code codeType} 为其他时，ram才有效。true表示数字字母间隔出现，false随机出现
	 * @param capType 字母大小写。"A"大写，"a"小写，其他随机大小写
	 * @return 返回字符串类型
	 */
	public static String createVCode(int length, String codeType, boolean ram, String capType ) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = i % 2 == 0 ? "num" : "char"; //数字 字母间隔
			switch (codeType) {
			case "num":
				charOrNum =  "num" ; //数字
				break;
			case "char":
				charOrNum =  "char" ; // 字母
				break;
			default:
				if(ram){  //随机 数字 字母
					if(random.nextInt(2) % 2 == 0){
						charOrNum =  "num" ; //数字
					}else{
						charOrNum =  "char" ; // 字母
					}
				}
				break;
			}
			
			if ("char".equalsIgnoreCase(charOrNum)){ 
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;  
				switch (capType) {
				case "A":
					choice = 97; //大写
					break;
				case "a":
					choice = 65; // 小写
					break;
				default:
					break;
				}
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)){ 
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
	
	/**
	 * 转换成十六进制的字符串形式
	 * @param bytes 字节数组
	 * @return String 返回十六进制的字符串
	 */
	public static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuffer buf = new StringBuffer();
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) { 			
			String shaHex = Integer.toHexString(bytes[j] & 0xFF);
			if (shaHex.length() < 2) {
				buf.append(0);
			}
			buf.append(shaHex);

		}
		return buf.toString();
	}
	
	/**
	 * MD5，默认字符编码 “utf-8”
	 * @param str 待加密字符串
	 * @return String 返回md5字符串
	 */
	public static String md5(String str) {
		return md5(str, initCharsetName);
	}
	
	/**
	 * MD5
	 * @param str  待加密字符串
	 * @param charsetName  编码，默认 “utf-8”
	 * @return String 返回md5字符串
	 */
	public static String md5(String str, String charsetName) {
		try {
			charsetName = isNullOrEmpty(charsetName) ? initCharsetName : charsetName;
			return md5(str.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * MD5
	 * @param bytes  待加密字节数组
	 * @return String 返回md5字符串
	 */
	public static String md5(byte[] bytes) {
		String res = "";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(bytes);
			res = getFormattedText(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 获取jdbc 的 driverClass
	 * @param jdbc	jdbc地址
	 * @return driverClass
	 */
	public static String getDriverClass(String jdbc){
		String clss = "";
		if(jdbc.startsWith("jdbc:odps")){
			clss = "com.aliyun.odps.jdbc.OdpsDriver";
			
		}else if(jdbc.startsWith("jdbc:derby")){
			clss = "org.apache.derby.jdbc.EmbeddedDriver";
			
		}else if(jdbc.startsWith("jdbc:mysql")){
			clss = "com.mysql.jdbc.Driver ";
			
		}else if(jdbc.startsWith("jdbc:oracle")){
			clss = "oracle.jdbc.driver.OracleDriver ";
			
		}else if(jdbc.startsWith("jdbc:microsoft")){
			clss = "com.microsoft.jdbc.sqlserver.SQLServerDriver ";
			
		}else if(jdbc.startsWith("jdbc:sybase:Tds")){
			clss = "com.sybase.jdbc2.jdbc.SybDriver ";
			
		}else if(jdbc.startsWith("jdbc:jtds")){
			clss = "net.sourceforge.jtds.jdbc.Driver";
			
		}else if(jdbc.startsWith("jdbc:postgresql")){
			clss = "org.postgresql.Driver";
			
		}else if(jdbc.startsWith("jdbc:fake")){
			clss = "com.alibaba.druid.mock.MockDriver";
			
		}else if(jdbc.startsWith("jdbc:mock")){
			clss = "com.alibaba.druid.mock.MockDriver";
			
		}else if(jdbc.startsWith("jdbc:hsqldb")){
			clss = "org.hsqldb.jdbcDriver";
			
		}else if(jdbc.startsWith("jdbc:db2")){
			clss = "COM.ibm.db2.jdbc.app.DB2Driver";
			//DB2的JDBC Driver十分混乱，这个匹配不一定对
			
		}else if(jdbc.startsWith("jdbc:sqlite")){
			clss = "org.sqlite.JDBC";
			
		}else if(jdbc.startsWith("jdbc:ingres")){
			clss = "com.ingres.jdbc.IngresDriver";
			
		}else if(jdbc.startsWith("jdbc:h2")){
			clss = "org.h2.Driver";
			
		}else if(jdbc.startsWith("jdbc:mckoi")){
			clss = "com.mckoi.JDBCDriver";
			
		}else if(jdbc.startsWith("jdbc:cloudscape")){
			clss = "COM.cloudscape.core.JDBCDriver";
			
		}else if(jdbc.startsWith("jdbc:informix-sqli")){
			clss = "com.informix.jdbc.IfxDriver";
			
		}else if(jdbc.startsWith("jdbc:timesten")){
			clss = "com.timesten.jdbc.TimesTenDriver";
			
		}else if(jdbc.startsWith("jdbc:as400")){
			clss = "com.ibm.as400.access.AS400JDBCDriver";
			
		}else if(jdbc.startsWith("jdbc:sapdb")){
			clss = "com.sap.dbtech.jdbc.DriverSapDB";
			
		}else if(jdbc.startsWith("jdbc:JSQLConnect")){
			clss = "com.jnetdirect.jsql.JSQLDriver";
			
		}else if(jdbc.startsWith("jdbc:JTurbo")){
			clss = "com.newatlanta.jturbo.driver.Driver";
			
		}else if(jdbc.startsWith("jdbc:firebirdsql")){
			clss = "org.firebirdsql.jdbc.FBDriver";
			
		}else if(jdbc.startsWith("jdbc:interbase")){
			clss = "interbase.interclient.Driver";
			
		}else if(jdbc.startsWith("jdbc:pointbase")){
			clss = "com.pointbase.jdbc.jdbcUniversalDriver";
			
		}else if(jdbc.startsWith("jdbc:edbc")){
			clss = "ca.edbc.jdbc.EdbcDriver";
			
		}else if(jdbc.startsWith("jdbc:mimer:multi1")){
			clss = "com.mimer.jdbc.Driver";
			
		}
		return clss;
	}
	
	/**
	 * 获取配置文件
	 * @param filename
	 * @return
	 */
	public static Properties getProperties(String filename){
		return getProperties(Tools.class.getResourceAsStream(filename.startsWith("/") ? filename : "/"+filename));
	}
	
	/**
	 * 获取配置文件
	 * @param file
	 * @return
	 */
	public static Properties getProperties(File file){
		try {
			return getProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取配置文件
	 * @param in
	 * @return
	 */
	public static Properties getProperties(InputStream in){
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					in = null;
				}
			}
		}
		return properties;
	}
	

	
	//TODO request相关
	/**
	 * 获取请求参数
	 * @param req	request请求
	 * @param key	参数名
	 * @return String	返回参数值，没找到返回null
	 */
	public static String getReqParam(HttpServletRequest req, String key){
		String value = req.getParameter(key);
		if (value == null) { //修改：不使用isNullOrEmpty()，是应为有时需要字段置空
			Object tmp = req.getAttribute(key);
			if(tmp != null){
				value = String.valueOf(tmp);
			}
		}
		return value == null ? null : value.trim();
	}
	
	/**
	 * 获取请求参数，参数值防注入
	 * @param req	request请求
	 * @param key	参数名
	 * @return String	返回参数值，没找到或被注入返回null
	 */
	public static String getReqParamSafe(HttpServletRequest req, String key){
		String value = req.getParameter(key);
		if (value == null) { //修改：不使用isNullOrEmpty(value)，是应为有时需要字段置空
			Object tmp = req.getAttribute(key);
			if(tmp != null){
				value = String.valueOf(tmp);
			}
		}
		if(value != null && isAttack(value)){
			value = null;
		}
		return value == null ? null : value.trim();
	}
	
	
	/**
	 * 检测是否SQL注入
	 * @param value
	 * @return boolean
	 */
	public static boolean isAttack(String value){
		boolean flag = false;
		if(isNullOrEmpty(value)){
			flag = false;
		}else if(value.toLowerCase().matches(Attacks.sqlComment)){
			flag = true;
		}else if(value.toLowerCase().matches(Attacks.sqlBatch)){
			flag = true;
		}else if(value.toLowerCase().matches(Attacks.sqlAnd)){
			flag = true;
		}else if(value.toLowerCase().matches(Attacks.sqlOr)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 检测SQL注入类型
	 * @param value
	 * @return String
	 */
	public static String attackType(String value){
		String str = "";
		if(isNullOrEmpty(value)){
			str = "";
		}else if(value.toLowerCase().matches(Attacks.sqlComment)){
			str = "SQL Comment Injection";
		}else if(value.toLowerCase().matches(Attacks.sqlBatch)){
			str = "SQL Batch Injection";
		}else if(value.toLowerCase().matches(Attacks.sqlAnd)){
			str = "SQL And Injection";
		}else if(value.toLowerCase().matches(Attacks.sqlOr)){
			str = "SQL Or Injection";
		}
		return str;
	}
	
	/**
	 * 记录SQL注入
	 * @param path	文件路径，默认：initAttackLogPath
	 * @param str	内容
	 */
	public static void attackRecord (String path, String str){
		File file = new File(path);
		if(file.exists()){
			path = path + File.separator + DateTools.formatDate(null, "yyyyMMdd") + ".txt";
		}else{
			path = Tools.getBasePath() + (isNullOrEmpty(path) ? initAttackLogPath : path) + DateTools.formatDate(null, "yyyyMMdd") + ".txt";
		}
		Tools.fileWrite(path, str);
	}
	
	/**
	 * 记录SQL注入
	 * @param path	文件路径，默认：initAttackLogPath
	 * @param str	内容
	 */
	public static void attackRecord (String path, HttpServletRequest req, String name, String value){
		//记录日志
		String str = "参数值: {"+name+" : "+value+"}|"+attackType(value)+"|" + DateTools.formatDate(null, "yyyy-MM-dd HH:mm:ss S") + "|"+getIp(req)+"|"+req.getRequestURI()+(req.getQueryString() != null ? "?"+req.getQueryString() : "");
		attackRecord("", str);
	}

	/**
	 * 获取请求参数，防注入
	 * @param req	request请求
	 * @return	map
	 */
	public static Map<String, Object> getReqParamsToMap(HttpServletRequest req){
		Map<String, Object> obj = new HashMap<String, Object>();
		Enumeration<String> enums = req.getParameterNames();
		if(enums == null || !enums.hasMoreElements()){
			enums = req.getAttributeNames();
		}
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			//参数名attack，需过滤
			if(isAttack(name)){
				//记录日志
				String str = "参数名: "+attackType(name)+"|" + DateTools.formatDate(null, "yyyy-MM-dd HH:mm:ss S") + "|"+getIp(req)+"|"+req.getRequestURI()+(req.getQueryString() != null ? "?"+req.getQueryString() : "");
				attackRecord("", str);
				continue;
			}
			//参数值attack，可不过滤，使用的SQL预编译
			String value = getReqParam(req, name);
			if(isAttack(value)){
				//记录日志
				attackRecord("", req, name, value);
			}
			obj.put(name, value);
		}
		return obj;
	}
	
	/**
	 * 获取请求参数，防注入
	 * @param req	request请求
	 * @param params	过滤条件。只获取与配置名相同的参数，多个逗号“,”隔开
	 * @return	map
	 */
	public static Map<String, Object> getReqParamsToMap(HttpServletRequest req, String params) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> enums = req.getParameterNames();
		if(enums == null || !enums.hasMoreElements()){
			enums = req.getAttributeNames();
		}
		while(enums.hasMoreElements()){
			String name = enums.nextElement();
			String[] tmp = params.replace("，", ",").split(",");
			boolean flag = false;
			for (String str : tmp) {
				if(str.equals(name)){
					flag = true;
					break;
				}
			}
			if(flag){
				//参数值attack，可不过滤，使用的SQL预编译
				String value = getReqParam(req, name);
				if(isAttack(value)){
					//记录日志
					attackRecord("", req, name, value);
				}
				map.put(name, value);
			}
		}
		return map;
	}
	
	/**
	 * 获取请求参数，防注入
	 * @param req	request请求
	 * @param clss	过滤条件。只获取与类属性名相同的参数
	 * @return	map
	 */
	public static Map<String, Object> getReqParamsToMap(HttpServletRequest req, Class<?> clss){
		Map<String, Object> obj = new HashMap<String, Object>();
		Enumeration<String> enums = req.getParameterNames();
		if(enums == null || !enums.hasMoreElements()){
			enums = req.getAttributeNames();
		}
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			Field[] fields = getFields(clss); //clss.getDeclaredFields();
			for (Field f : fields) {
				if(name.equals(f.getName())){
					Object value = null;
					String tmpValue = getReqParam(req, name);
					//参数值attack，可不过滤，使用的SQL预编译
					if(isAttack(tmpValue)){
						//记录日志
						attackRecord("", req, name, tmpValue);
					}
					//格式化
					switch (f.getType().getSimpleName()) {
					case "int":
						value = Tools.parseInt(tmpValue);
						break;
					case "long":
						value = Tools.parseLong(tmpValue);
						break;
					case "float":
						value = Tools.parseFloat(tmpValue);
						break;
					case "double":
						value = Tools.parseDouble(tmpValue);
						break;
					case "boolean":
						value = Tools.parseBoolean(tmpValue);
						break;
					default:
						value = tmpValue;
						break;
					}
					obj.put(name, value);
				}
			}
		}
		return obj;
	}
	
	/**
	 * 获取请求参数，防注入
	 * @param req	request请求
	 * @param obj	转换对象
	 * @return	T
	 */
	public static <T> T getReqParamsToObject(HttpServletRequest req, T obj){
		Class<?> clss = obj.getClass();
		Field[] fields = getFields(clss); //clss.getDeclaredFields();
		Method[] methods = getMethods(clss); //clss.getDeclaredMethods();
		for (Field f : fields) {
			Object value = null;
			Enumeration<String> enums = req.getParameterNames();
			if(enums == null || !enums.hasMoreElements()){
				enums = req.getAttributeNames();
			}
			while (enums.hasMoreElements()) {
				String name = (String) enums.nextElement();
				String tmpValue = getReqParam(req, name);
				if(name.equals(f.getName())){
					//参数值attack，可不过滤，使用的sql预编译
					if(isAttack(tmpValue)){
						//记录日志
						attackRecord("", req, name, tmpValue);
					}
					//格式化
					switch (f.getType().getSimpleName()) {
					case "int":
						value = Tools.parseInt(tmpValue);
						break;
					case "long":
						value = Tools.parseLong(tmpValue);
						break;
					case "float":
						value = Tools.parseFloat(tmpValue);
						break;
					case "double":
						value = Tools.parseDouble(tmpValue);
						break;
					case "boolean":
						value = Tools.parseBoolean(tmpValue);
						break;
					default:
						value = tmpValue;
						break;
					}
					for (Method m : methods) {
						if(m.getName().startsWith("set") && m.getName().substring(3).equalsIgnoreCase(name)){
							try {
								m.invoke(obj, value);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return obj;
	}
	
	
	
	/**
	 * 对象转map
	 * @param obj	对象
	 * @return	Map
	 */
	public static Map<String, Object> parseObjectToMap(Object object){
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = getFields(object.getClass()); //object.getClass().getDeclaredFields();
		Method[] methods = getMethods(object.getClass()); //object.getClass().getDeclaredMethods();  
		for (Field f : fields) {
			String key = f.getName();
			Object value = null;
			for (Method method : methods) {
				if(method.getName().replace("get", "").toLowerCase().equals(key.toLowerCase())){
					try {
						value = method.invoke(object);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			//转String类型，空值为空字符串。
			if(value == null && "String".equals(f.getType().getSimpleName())){
				value = "";
			}
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * 数组转map
	 * @param objs	数组
	 * @return	Map，key为数组序号
	 */
	public static Map<String, Object> parseArrayToMap(Object... objs){
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < objs.length; i++) {
			map.put(String.valueOf(i + 1), objs[i]);
		}
		return map;
	}
	
	
	/**
	 * response输出
	 * @param resp	response
	 * @param result	输出字符串
	 */
	public static void output(HttpServletResponse resp, String result) {
		output(null, resp, result, false);
	}
	
	/**
	 * response输出
	 * @param resp	response
	 * @param result	输出字符串
	 * @param cos	是否跨域
	 */
	public static void output(HttpServletRequest req, HttpServletResponse resp, String result) {
		output(req, resp, result, false);
	}
	
	/**
	 * response输出
	 * @param req	request
	 * @param resp	response
	 * @param result	输出字符串
	 * @param cos	是否跨域
	 */
	public static void output(HttpServletRequest req, HttpServletResponse resp, String result, boolean cos) {
		try {
			if(isNullOrEmpty(resp.getContentType())){
				resp.setContentType("text/html;charset=utf-8");
			}
			if(cos){
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");//返回所有支持的方法，避免多次"预检"请求
				resp.setHeader("Access-Control-Max-Age", "36000");//本次预检请求的有效期，单位为秒
				resp.setHeader("Access-Control-Allow-Credentials", "true"); //请求允许cookie，需配合Origin使用（Origin不能配置为*），并且ajax也要withCredentials为true
				
				String header = req.getHeader("Access-Control-Request-Headers");
				if(isNullOrEmpty(header)){
					resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
				}else{
					resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,"+header);
				}
			}
			PrintWriter writer = resp.getWriter();
			writer.print(result);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * sign验证，value不为空才参数验证。
	 * <p>格式：md5(key+value+key+value+。。。+secretKey)
	 * @param data	参数
	 * @param secretKey	密钥
	 * @return	md5字符串
	 */
	public static String getSign(Map<String, Object> data, String secretKey){
		List<String> keys = new ArrayList<String>(data.keySet());
		Collections.sort(keys);
		String str = "";
		for (String key : keys) {
			Object temp = data.get(key);
			if (!isNullOrEmpty(temp)){
				String value = String.valueOf(temp);
				str += key+value;
			}
		}
		return md5(str + secretKey);
	}
	
	/**
	 * sign验证，value不为空才参数验证。
	 * <p>格式：md5(key+connector+value+separator+key+connector+value+separator+。。。+secretKey)
	 * @param data	参数
	 * @param connector	链接符
	 * @param separator	分隔符
	 * @param secretKey	密钥
	 * @return	md5字符串
	 */
	public static String getSign(Map<String, Object> data, String connector, String separator, String secretKey){
		List<String> keys = new ArrayList<String>(data.keySet());
		Collections.sort(keys);
		String str = "";
		for (String key : keys) {
			Object temp = data.get(key);
			if (!isNullOrEmpty(temp)){
				String value = String.valueOf(temp);
				str += separator + key + connector + value;
			}
		}
		str = Tools.isNullOrEmpty(str) ? "" : str.substring(separator.length());
		return md5(str + secretKey);
	}
	
	/**
	 * 获取ip地址
	 * @param req request
	 * @return
	 */
	public static String getIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if(!isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = req.getHeader("X-Real-IP");
        if(!isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return req.getRemoteAddr();
    }

	/**
	 * 获取根url地址
	 * @param request
	 * @return tomcat http://127.0.0.1:8080/sitesnqx/   nginx http://127.0.0.1/
	 */
	public static String getBaseUrl(HttpServletRequest request) {
        String base = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        if(base.split(":").length >= 3){ 
        	base += request.getContextPath();
        }
        return base+"/";
    }
	
	/**
	 * 获取项目地址
	 */
	public static String getBasePath() {
//		System.getProperty("user.dir"); // 在eclipse中调试，会是eclipse的地址。如：C:\Program Files\eclipse\eclipse
		String base = Tools.class.getResource("/").getPath();
		if(base.indexOf("/target/") != -1){ //maven
			base = base.split("/target/")[0];
		}else if(base.indexOf("/build/") != -1){ //eclipse
			base = base.split("/build/")[0];
		}else{ 
			base = base.replace("/WEB-INF/classes/", "");
		}
		return base + "/";
    }
	
	/**
	 * 获取项目src地址
	 */
	public static String getBaseSrcPath() {
		String base = Tools.class.getResource("/").getPath();
		if(base.indexOf("/target/") != -1){ //maven
			base = base.split("/target/")[0] + "/src/main/java/";
		}else if(base.indexOf("/build/") != -1){ //eclipse
			base = base.split("/build/")[0] + "/src/";
		}else{ 
			base = base.replace("/WEB-INF/classes/", "") + "/src/";
		}
		return base;
    }
	
	/**
	 * 获取项目res地址
	 */
	public static String getBaseResPath() {
		String base = Tools.class.getResource("/").getPath();
		if(base.indexOf("/target/") != -1){ //maven
			base = base.split("/target/")[0] + "/src/main/resources/";
		}else if(base.indexOf("/build/") != -1){ //eclipse
			base = base.split("/build/")[0] + "/src/";
		}else{ 
			base = base.replace("/WEB-INF/classes/", "") + "/src/";
		}
		return base;
    }
	
	/**
	 * 获取项目class地址
	 */
	public static String getBaseClsPath() {
		String base = Tools.class.getResource("/").getPath();
		return base;
    }
	
	//TODO 文件
	/**
	 * 写文件。在原文件基础上新增内容。
	 * @param file	待写入文件
	 * @param content 待写入内容
	 */
	public static void fileWrite(File file, String content) {
		List<String> list = new ArrayList<String>();
		list.add(content);
		fileWrite(file, list, true);
	}
	
	/**
	 * 写文件。在原文件基础上新增内容。
	 * @param path	文件路径
	 * @param content 待写入内容
	 */
	public static void fileWrite(String path, String content) {
		List<String> list = new ArrayList<String>();
		list.add(content);
		fileWrite(new File(path), list, true);
	}
	
	/**
	 * 写文件
	 * @param file	待写入文件
	 * @param content 待写入内容
	 * @param append  if true, then bytes will be written to the end of the file rather than the beginning
	 */
	public static void fileWrite(File file, List<String> content, boolean append) {
		service.submit(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				String charset = initCharsetName;
				OutputStream out = null;
				try {
					//file.getParentFile().mkdirs();
					File pfile = file.getParentFile();
					if(!pfile.exists()){
						pfile.mkdirs();
					}
					out = new FileOutputStream(file, append);
					for (int i = 0; i < content.size(); i++) {
						out.write(content.get(i).getBytes(charset));
						out.write("\n".getBytes());
					}
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
					if(out != null){
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}finally{
							out = null;
						}
					}
				}
			}
		});
	}
	
	/**
	 * 读文件
	 * @param file	待读入文件
	 * @return List<String>
	 */
	public static List<String> fileReader(File file) {
		List<String> content = new ArrayList<String>();
		if(file.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), initCharsetName));
				String line;
				while ((line = reader.readLine()) != null) {
					content.add(line);
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						reader = null;
					}
				}
			}
		}
		return content;
	}
	
	/**
	 * 检测文件变化
	 * @param file	被检测文件
	 * @param refrushTime	检测频率（s）
	 * @param callback	回调函数，参数：{@code WatchEvent<?>}
	 */
	public static void fileWatch(File file, int refrushTime, CallBack callback){
		if(!file.exists()){
			System.out.println("文件不存在！！！");
			return;
		}
		if(refrushTime < 0){
			System.out.println("检测频率为正数，单位秒！！！");
			return;
		}
		//监控文件变化
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = file.getParentFile().toPath();
			while (true) {
				WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
				List<WatchEvent<?>> watchEvents = key.pollEvents();  
				for(WatchEvent<?> event : watchEvents){ 
					//根据事件类型采取不同的操作。。。。。。。  
					callback.execute(event);
				}  
				boolean valid = key.reset();
				if(!valid){
					break;
				}
				//检测频率
				Thread.sleep(refrushTime * 1000);
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 线程关闭  service
	 */
	public static void shutdown() {
		service.shutdownNow();
	}
	
	/**
	 * 将base64编码字符串转换为图片
	 * @Author: 
	 * @CreateTime: 
	 * @param imgStr base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	*/
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null)
			return false;
		try {
			// 解密
			byte[] b = Base64.getDecoder().decode(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 根据图片地址转换为base64编码字符串
	 * @Author: 
	 * @CreateTime: 
	 * @param imgFile 图片路径-具体到文件
	 * @return
	 */
	public static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);
	        data = new byte[inputStream.available()];
//	        inputStream.read(data);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return Base64.getEncoder().encodeToString(data);
	}
	
	/**
	 * 中文转unicode。小于255原样输出
	 * @param string
	 * @return
	 */
	public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            if(c > 255){
            	unicode.append("\\u" + Integer.toHexString(c));
            }else{
            	unicode.append(c);
            }
        }
        return unicode.toString();
    }
	
	/**
	 * unicode转中文。4位unicode
	 * @param unicode
	 * @return
	 */
	public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 0; i < hex.length; i++) {
            // 转换出每一个代码点
        	if(hex[i].length() < 4){
        		string.append(hex[i]);
        	}else{
        		int data = Integer.parseInt(hex[i].substring(0, 4), 16);
        		// 追加成string
        		string.append((char) data);
        		string.append(hex[i].substring(4));
        	}
        }
        return string.toString();
    }
	
	/**
	 * 获取正则匹配值
	 * @param str	待匹配字符串
	 * @param regEx	正则表达式
	 * @return list 返回匹配的结果集
	 */
	public static List<String> parseRegEx(String str, String regEx) {
		List<String> result = new ArrayList<String>();
		Pattern pat = Pattern.compile(regEx); 
		Matcher mat = pat.matcher(str); 
		while (mat.find()) {
			String r = mat.group();
			result.add(r);
		}
		return result;
	}
	
	/**
	 * 判读是否是基本类型(null, boolean, byte, char, double, float, int, long, short, string)
	 * @param clazz Class 对象
	 * @return true: 是基本类型, false:非基本类型
	 */
	public static boolean isBasicType(Class<?> clazz){
		if(clazz == null || clazz.isPrimitive() || clazz.getName().startsWith("java.lang")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判读是否是 JDK 中定义的类(java包下的所有类)
	 * @param clazz Class 对象
	 * @return true: 是基本类型, false:非基本类型
	 */
	public static boolean isSystemType(Class<?> clazz){
		if( clazz.isPrimitive() || clazz.getName().contains("java.")){
			return true;
		}else{
			return false;
		}
	}
	
	//TODO 反射工具
	/**
	 * 获得类所有的Field
	 * 
	 * @param clazz 类对象
	 * @return Field数组
	 */
	public static Field[] getFields(Class<?> clazz) {
		String mark = clazz.getCanonicalName();
		Field[] fields = null;

		if(fieldArrays.containsKey(mark)){
			fields = fieldArrays.get(mark);
		}else {
			ArrayList<Field> fieldArray = new ArrayList<Field>();
			for (; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] tmpFields = clazz.getDeclaredFields();
				fieldArray.addAll(Arrays.asList(tmpFields));
			}
			fields = fieldArray.toArray(new Field[]{});
			fieldArrays.put(mark, fields);
			fieldArray.clear();
		}

		return fields;
	}

	/**
	 * 查找类特定的Field
	 * 
	 * @param clazz   类对象
	 * @param fieldName field 名称
	 * @return field 对象
	 * @throws NoSuchFieldException 无 Field 异常
	 * @throws SecurityException 安全性异常
	 */
	public static Field findField(Class<?> clazz, String fieldName)
			throws ReflectiveOperationException {

		String mark = clazz.getCanonicalName()+"#"+fieldName;

        if(fields.containsKey(mark)){
            return fields.get(mark);
        }else {
            Field field = null;

            for (; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                }catch(ReflectiveOperationException e){
                    field = null;
                }
            }

            fields.put(mark, field);

            return field;
        }
	}

	/**
	 * 查找类特定的Field
	 * 			不区分大小写,并且替换掉特殊字符
	 * @param clazz   类对象
	 * @param fieldName Field 名称
	 * @return Field 对象
	 * @throws ReflectiveOperationException 反射异常
     */
	public static Field findFieldIgnoreCase(Class<?> clazz, String fieldName)
			throws ReflectiveOperationException{

		String mark = clazz.getCanonicalName()+"#"+fieldName;

		if(fields.containsKey(mark)){
			return fields.get(mark);
		}else {
			for (Field field : getFields(clazz)) {
				if (field.getName().equalsIgnoreCase(fieldName)) {
					fields.put(mark, field);
					return field;
				}
			}
		}
		return null;
	}
	
	/**
     * 获取类的方法集合
     * @param clazz		类对象
     * @return Method 对象数组
     */
	public static Method[] getMethods(Class<?> clazz) {

		Method[] methods = null;

		String mark = clazz.getCanonicalName();

		if(methodArrays.containsKey(mark)){
			return methodArrays.get(mark);
		} else {
			List<Method> methodList = new ArrayList<Method>();
			for (; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
				Method[] tmpMethods = clazz.getDeclaredMethods();
				methodList.addAll(Arrays.asList(tmpMethods));
			}
			methods = methodList.toArray(new Method[]{});
			methodArrays.put(mark,methods);
			methodList.clear();
		}
		return methods;
	}
	
	/**
	 * 获取类的特定方法的集合
	 * 		类中可能存在同名方法
	 * @param clazz		类对象
	 * @param name		方法名	
	 * @return Method 对象数组
	 */
	public static Method[] getMethods(Class<?> clazz, String name) {

		Method[] methods = null;

		String mark = clazz.getCanonicalName()+"#"+name;

		if(methodArrays.containsKey(mark)){
			return methodArrays.get(mark);
		} else {
			ArrayList<Method> methodList = new ArrayList<Method>();
			Method[] allMethods = getMethods(clazz);
			for (Method method : allMethods) {
				if (method.getName().equals(name))
					methodList.add(method);
			}
			methods = methodList.toArray(new Method[0]);
			methodArrays.put(mark, methods);
			methodList.clear();
		}
		return methods;
	}
	
	/**
	 * 查找类中的方法
	 * @param clazz        类对象
	 * @param name		   方法名	
	 * @param paramTypes   参数类型
	 * @return			   方法对象
	 * @throws ReflectiveOperationException 反射异常
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) 
			throws ReflectiveOperationException {
		String mark = clazz.getCanonicalName()+"#"+name;
		for(Class<?> paramType : paramTypes){
			mark = mark + "$" + paramType.getCanonicalName();
		}

		if(methods.containsKey(mark)){
			return methods.get(mark);
		}else {
			Method method = null;

			for (; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
				try {
					method = clazz.getDeclaredMethod(name, paramTypes);
					break;
				}catch(ReflectiveOperationException e){
					method = null;
				}
			}

			methods.put(mark, method);
			return method;
		}
	}

	/**
	 * 查找类中的方法(使用参数数量)
	 * @param clazz        类对象
	 * @param name		   方法名
	 * @param paramCount   参数数量
	 * @return			   方法对象
	 * @throws ReflectiveOperationException 反射异常
	 */
	public static Method[] findMethod(Class<?> clazz, String name, int paramCount) 
			throws ReflectiveOperationException {
		Method[] methods = null;

		String mark = clazz.getCanonicalName()+"#"+name+"@"+paramCount;

		if(methodArrays.containsKey(mark)){
			return methodArrays.get(mark);
		} else {
			ArrayList<Method> methodList = new ArrayList<Method>();
			Method[] allMethods = getMethods(clazz, name);
			for (Method method : allMethods) {
				if (method.getParameterTypes().length == paramCount) {
					methodList.add(method);
				}
			}
			methods = methodList.toArray(new Method[]{});
			methodArrays.put(mark,methods);
			methodList.clear();
		}

		return methods;
	}

	
	/**
	 * 获取范型类型
	 * @param type 类型对象
	 * @return Class 对象
	 * @throws ClassNotFoundException 类找不到异常
	 */
	public static Class<?>[] getGenericClass(ParameterizedType type) throws ClassNotFoundException{
		Class<?>[] result = null;
		Type[] actualType = type.getActualTypeArguments();
		result = new Class[actualType.length];

		for(int i=0;i<actualType.length;i++){
			if(actualType[i] instanceof Class){
				result[i] = (Class<?>)actualType[i];
			}else if(actualType[i] instanceof Type){
				String classStr = actualType[i].toString();
				classStr = classStr.replaceAll("<.*>", "");
				result[i] = Class.forName(classStr);
			}
		}
		return result;
	}


	/**
	 * 获取 Field 的范型类型
	 * @param field  field 对象
	 * @return 返回范型类型数组
	 * @throws ClassNotFoundException 类找不到异常
	 */
	public static Class<?>[] getGenericType(Field field) throws ClassNotFoundException {
		Type type = field.getGenericType();
		if(type instanceof ParameterizedType){
			return getGenericClass((ParameterizedType)type);
		}
		return null;
	}
	
	/**
	 * 获取 Class 的范型类型
	 * @param clss  Class 对象
	 * @return 返回范型类型数组
	 * @throws ClassNotFoundException 类找不到异常
	 */
	public static Class<?>[] getGenericType(Class<?> clss) throws ClassNotFoundException {
		Type type = clss.getGenericSuperclass();
		if(type instanceof ParameterizedType){
			return getGenericClass((ParameterizedType)type);
		}
		return null;
	}
	
	/**
	 * 获取 Field 的范型类型
	 * @param field  field 对象
	 * @param index  序号
	 * @return 返回Class对象
	 * @throws ClassNotFoundException 类找不到异常
	 */
	public static Class<?> getGenericType(Field field, int index) throws ClassNotFoundException {
		Class<?>[] clsses = getGenericType(field);
		if(clsses != null){
			return clsses[index];
		}
		return null;
	}
	
	/**
	 * 获取 Class 的范型类型
	 * @param clss  Class 对象
	 * @param index  序号
	 * @return 返回Class对象
	 * @throws ClassNotFoundException 类找不到异常
	 */
	public static Class<?> getGenericType(Class<?> clss, int index) throws ClassNotFoundException {
		Class<?>[] clsses = getGenericType(clss);
		if(clsses != null){
			return clsses[index];
		}
		return null;
	}
	
	/**
	 * 获取指定包下面的所有 Class文件
	 * @param packageName  包名。“com.jian.test”
	 * @return 返回Class对象
	 * @throws ClassNotFoundException 类找不到异常
	 * @throws IOException io异常
	 */
	public static List<Class<?>> findClass(String packageName) throws IOException, ClassNotFoundException {

		List<Class<?>> classes = new ArrayList<Class<?>>();
		String mark = "packageName#" + packageName;
		//查询包下的所有class
		if(classLists.containsKey(mark)){
			classes = classLists.get(mark);
		}else{
			ClassLoader classLoader = Tools.class.getClassLoader();
			String path = packageName.replace(".", "/");
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			for (File directory : dirs) {
				classes.addAll(findClass(directory, packageName));
			}
			classLists.put(mark, classes);
		}
		return classes;
	}
	
	private static List<Class<?>> findClass(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		if(!isNullOrEmpty(packageName)){
			packageName = packageName + ".";
		}

		File[] files = directory.listFiles();

		for (File file : files) {
			if("META-INF".equals(file.getName()) || "BOOT-INF".equals(file.getName())) {
				continue; //排除不相关的文件夹
			}
			if (file.isDirectory()) {
				assert !file.getName().contains(".") : " Error: " +file.getName() +" -- "+ file.getPath();
				classes.addAll(findClass(file, packageName + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				String name = packageName + file.getName().substring(0, file.getName().length() - 6);
				Class<?> temp = Tools.class.getClassLoader().loadClass(name);
				classes.add(temp);
			}
		}
		return classes;
	}

	public static void main(String[] args) {
//		String imgstr = getImageStr("C:\\Users\\Administrator\\Desktop\\65.png");
//		System.out.println(imgstr);
//		generateImage(imgstr, "C:\\Users\\Administrator\\Desktop\\63.png");

		String str = Base64.getEncoder().encodeToString("严".getBytes());
		System.out.println(str);
		byte[] b = "严".getBytes();
		for (byte c : b) {
			System.out.print(Integer.toHexString(c & 0xff));
			System.out.print(" ");
		}
		System.out.println();
		
		final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
        };
		int[] fromBase64 = new int[256];
     
        Arrays.fill(fromBase64, -1);
        for (int i = 0; i < toBase64.length; i++) {
        	System.out.println((int)toBase64[i]);
        	fromBase64[toBase64[i]] = i;
        }
        fromBase64['='] = -2;
        
        for (int i : fromBase64) {

			System.out.print(i);
			System.out.print(" ");
		}
        
	}
	
}
