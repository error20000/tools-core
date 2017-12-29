package com.jian.tools.core;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class FileTools {

	//写文件线程池
	private static ExecutorService service = Executors.newFixedThreadPool(20);
	private static Lock lock = new ReentrantLock();
	public static String initCharsetName = "utf-8";	//统一字符编码，默认值："utf-8"
	
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
				OutputStream out;
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
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), initCharsetName));
				String line;
				while ((line = reader.readLine()) != null) {
					content.add(line);
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	

	/**
	 * 读文件
	 * @param file	待读入文件
	 * @return String
	 */
	public static String fileReaderAll(File file) {
		String content = "";
		if(file.exists()){
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), initCharsetName));
				List<String> list = reader.lines().collect(Collectors.toList());
				for (String str : list) {
		    		content += str + "\n";
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
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
	
	
}

