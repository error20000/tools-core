package com.jian.tools.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

public class LogsTools {

	private static ExecutorService service = Executors.newFixedThreadPool(20);
	private static Object lock = new Object();
	private static boolean close = false;
	
	/**
	 * 写日志开关
	 * @param close true 关闭日志记录，false 打开日志记录。默认：false.
	 */
	public static void close(boolean close) {
		LogsTools.close = close;
	}
	
	/**
	 * 写日志，保存为文件
	 * @param context
	 * @param path
	 * @param log
	 */
	public static void logSet(HttpServletRequest req, String path, String log) {
		String base = req.getServletContext().getRealPath("/");
		File file = new File(base+File.separator+"logs"+ File.separator+path+File.separator+Tools.formatDate(null, "yyyyMMdd")+".txt");
		logSet(file, log);
	}
	
	/**
	 * 写文件日志
	 * @param path
	 * @param log
	 * @text 
	 * Utils.class.getClassLoader().getResource("/").toString()
	 * windows file:/E:/Workspaces/MyEclipse%20Professional%202014/.metadata/.me_tcat7/webapps/sitesnqx/WEB-INF/classes/
	 * linux file:/home/admin/website-tomcat-7.0.42/webapps/sitesnqx/WEB-INF/classes/
	 * 
	 * Utils.class.getClassLoader().getResource("/").getPath()
	 * windows E:/Workspaces/MyEclipse%20Professional%202014/.metadata/.me_tcat7/webapps/sitesnqx/WEB-INF/classes/
	 * linux /home/admin/website-tomcat-7.0.42/webapps/sitesnqx/WEB-INF/classes/
	 * 
	 * /build/classes  eclipse
	 * /WEB-INF/classes  myeclipse
	 * 
	 */
	public static void logSet(String path, String log) {
		String base = Tools.class.getResource("/").getPath().replace("/build/classes/", "").replace("/WEB-INF/classes/", "");
		//toString()
//		base = base.split(":").length < 2 ? "/"+base : base; //linux
		String rpath = base + File.separator + "logs"+ File.separator + path + File.separator + Tools.formatDate(null, "yyyyMMdd") + ".txt";
		File file = new File(rpath);
		logSet(file, log);
	}
	
	/**
	 * 写文件日志
	 * @param file
	 * @param log
	 */
	public static void logSet(File file, String log) {
		if(close) return;
		service.submit(new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					OutputStream out;
					try {
						//file.getParentFile().mkdirs();
						File pfile = file.getParentFile();
						if(!pfile.exists()){
							pfile.mkdirs();
						}
						String str = Tools.formatDate(null, "yyyy-MM-dd hh:mm:ss") + " -- " + log;
						out = new FileOutputStream(file, true);
						byte[] b = new String(str + "\r\n").getBytes("utf-8");
						for (int i = 0; i < b.length; i++) {
							out.write(b[i]);
						}
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * 线程关闭  service
	 */
	public static void shutdown() {
		service.shutdownNow();
	}
	
}
