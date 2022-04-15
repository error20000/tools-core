package com.jian.tools.core;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jian.exception.ToolsException;



public class FileTools {

	//写文件线程池
	private static ExecutorService service = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));
	private static Lock lock = new ReentrantLock();
	public static String initCharsetName = "utf-8";	//统一字符编码，默认值："utf-8"
	
	/**
	 * 根据给定的文件名，获取扩展名，包含“.”。
	 * @param filename  文件名
	 * @return 返回扩展名。如：.txt
	 */
	public static String fileSuffix(String filename) {
		if(filename.lastIndexOf( "." ) == -1) {
			return "";
		}
		return filename.substring(filename.lastIndexOf( "." )).toLowerCase();
    }

	/**
	 * 根据给定的文件名，获取扩展名，不包含“.”。
	 * @param filename  文件名
	 * @return 返回扩展名。如：txt
	 */
	public static String fileExtension(String filename) {
		if(filename.lastIndexOf( "." ) == -1) {
			return "";
		}
		return filename.substring(filename.lastIndexOf( "." ) + 1).toLowerCase();
    }
	
	/**
	 * 读取流
	 * @param in	输入流（读）
	 * @return	byte[]
	 * @throws ToolsException
	 */
	public static byte[] read(InputStream in) throws Exception {
		if(in == null) {
			throw new NullPointerException("InputStream is null。");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int count = 0;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);
		}
		in.close();
		return out.toByteArray();
	}
	
	/**
	 * 写入数据
	 * @param out	输出流（写）
	 * @param data	待写入数据
	 * @throws ToolsException
	 */
	public static void write(OutputStream out, byte[] data) throws Exception {
		if(out == null) {
			throw new NullPointerException("OutputStream is null。");
		}
		if(data == null) {
			throw new NullPointerException("data is null。");
		}
		out.write(data);
		out.flush();
		out.close();
	}
	
	/**
	 * 边读边写
	 * @param in	输入流（读）
	 * @param out	输出流（写）
	 * @throws ToolsException
	 */
	public static void readWrite(InputStream in, OutputStream out) throws Exception {
		if(in == null) {
			throw new NullPointerException("InputStream is null。");
		}
		if(out == null) {
			throw new NullPointerException("OutputStream is null。");
		}
		byte[] b = new byte[1024];
		int count = 0;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);
		}
		out.flush();
		in.close();
		out.close();
	}

	/**
	 * 分段输出。（适用于视频在线播放）
	 * @param contentType	数据类型
	 * @param length		数据总大小
	 * @param in			数据流
	 * @param request		请求
	 * @param response		响应
	 * @throws Exception
	 */
	public static void readWrite(String contentType, long length, InputStream in, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(in == null) {
			throw new NullPointerException("InputStream is null。");
		}
		if(contentType == null) {
			throw new NullPointerException("contentType is null。");
		}
//		System.out.println("request Range --->"+request.getHeader("Range"));
		response.setHeader("Accept-Ranges", "bytes");
		response.setContentType(contentType);
		//Range
		long from = 0, to = length - 1; // 定义有效数据流起始, 截止位置
		String range = request.getHeader("Range");
		if (range == null || "".equals(range)) {
	        response.setHeader("Content-Length", Long.toString(length));
	    } else {
	    	String[] ranges = range.replace("bytes=", "").split("-"); // bytes=xx-xx
	    	if (ranges.length > 0) 
	    		from = Long.parseLong(ranges[0]);
	    	if (ranges.length > 1) 
	    		to = Long.parseLong(ranges[1]);
//	    	System.out.println(String.format("request Range [] ---> %s-%s", from, to));
	    	// 设置本批次数据大小
	    	response.setHeader("Content-Length", Long.toString(to - from + 1));
	    	// 设置本批次数据范围及数据总大小
	    	response.setHeader("Content-Range", String.format("bytes %s-%s/%s", from, to, length));
	    	// 设置状态码（206部分完成）
	    	response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	    }
		//数据
		OutputStream out = response.getOutputStream();
		in.skip(from); // 跳过
		byte[] b = new byte[1024*1024];
		int count = 0;
		long limit = to - from + 1; // 数据流大小限制
		while (0 < limit && (count = in.read(b)) != -1) {
			out.write(b, 0, count);
			limit -= count; 
		}
		in.close();
		out.flush();
		out.close();
//		System.out.println(String.format("request Range end......"));
	}
	
	
	
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
	 * 读文件
	 * @param file	待读入文件
	 * @return String 原格式
	 */
	public static String fileReaderAll(File file) {
		String content = "";
		if(file.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), initCharsetName));
				content = reader.lines().collect(Collectors.joining("\n"));
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
					if(callback != null){
						callback.execute(event);
					}
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
	
	
}

