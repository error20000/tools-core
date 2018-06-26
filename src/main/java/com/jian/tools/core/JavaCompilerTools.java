package com.jian.tools.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class JavaCompilerTools {

	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	private static StandardJavaFileManager stdManager;
	private static Map<String, byte[]> cache = new HashMap<String, byte[]>();
	private static boolean clearCache = false;
	
	
	
	/**
	 * 标准类编译。会生成class文件
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @return
	 */
	public static Class<?> compilerStandard(String className) {
		String path = Tools.getBaseSrcPath() + className.replace(".", File.separator) + ".java";
		int res = compiler.run(null, null, null, path);
		if(res != 0){
			throw new IllegalArgumentException("compiler" + className + " failed");
		}
		String inPath = Tools.getBaseSrcPath() + className.replace(".", File.separator) + ".class";
		String outPath = Tools.getBaseClsPath() + className.replace(".", File.separator) + ".class";
		File inFile = new File(inPath);
		File outFile = new File(outPath);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(inFile);
			out = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int index = 0;
			while ((index = in.read(buf)) != -1) {
				out.write(buf, 0, index);
			}
			in.close();
			out.close();
			inFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 内存类编译
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @return
	 */
	public static Class<?> compiler(String className){
		String source = "";
		String path = Tools.getBaseSrcPath() + className.replace(".", File.separator) + ".java";
		File file = new File(path);
		if(!file.exists()) {
			throw new IllegalArgumentException("not find file:	" + path);
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				source += line;
				source += "\n";
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Tools.isNullOrEmpty(source)) {
			throw new IllegalArgumentException("no source:	" + source);
		}
		return compiler(className, source);
	}
	
	/**
	 * 内存类编译
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @param source java code
	 * @return
	 */
	public static Class<?> compiler(String className, String source){
		return compiler(className, source, false);
	}
	
	/**
	 * 内存类编译
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @param source java code
	 * @param reload 是否每次编译，默认：false
	 * @return
	 */
	public static Class<?> compiler(String className, String source, boolean reload){
		clearCache = reload;
		//取缓存
		byte[] buffer = cache.get(className);
		MemoryClassLoader classLoader = new MemoryClassLoader();
		if(buffer != null && !reload) {
			try {
				return classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
		//创建class
		if(compiler == null) {
			throw new IllegalArgumentException("not find tools.jar");
		}
		stdManager = compiler.getStandardFileManager(null, null, null);
		
		MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager);
		String simpleName = className.substring(className.lastIndexOf(".") + 1);
		JavaFileObject javaFileObject = manager.makeClassSource(simpleName, source);
		CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
		Boolean result = task.call();
		if (!result) {
			throw new IllegalArgumentException("compiler " + className+ " failed");
		}
		
		try {
			return classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static class MemoryJavaFileManager  extends ForwardingJavaFileManager<JavaFileManager>{
		
		protected MemoryJavaFileManager(JavaFileManager manager) {
			super(manager);
		}
		
		
		@Override
		//className 类全名 e.g. "com.jian.Test"
		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
				throws IOException {
			if (kind == Kind.CLASS) {
				return new MemoryOutputJavaFileObject(className);
			} else {
				return super.getJavaFileForOutput(location, className, kind, sibling);
			}
		}
		
		//className 类名 e.g. "Test"
		JavaFileObject makeClassSource(String className, String source) {
			return new MemoryInputJavaFileObject(className, source);
		}
	}
	
	static class MemoryInputJavaFileObject extends SimpleJavaFileObject{
		
		private String source;
		
		protected MemoryInputJavaFileObject(String className, String source) {
			super(URI.create("string:///" + className + Kind.SOURCE.extension), Kind.SOURCE);
			this.source = source;
		}
		
		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			if(source == null){
				throw new IllegalArgumentException("source == null");
			}
			return source;
		}
		
	}
	
	static class MemoryOutputJavaFileObject extends SimpleJavaFileObject{
		
		private String className;
		
		protected MemoryOutputJavaFileObject(String className) {
			super(URI.create("string:///" + className + Kind.CLASS.extension), Kind.CLASS);
			this.className = className;
		}
		
		@Override
		public OutputStream openOutputStream() throws IOException {
			return new FilterOutputStream(new ByteArrayOutputStream()) {
				
				@Override
				public void close() throws IOException {
					out.close();
					ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
					cache.put(className, bos.toByteArray());
				}
				
			};
		}
		
	}
	
	static class MemoryClassLoader extends ClassLoader{
		
		
		@Override
		protected Class<?> findClass(String className) throws ClassNotFoundException {
			byte[] buffer = cache.get(className);
			if (buffer == null) {
				return super.findClass(className);
			}
			if(clearCache){
				cache.remove(className);
			}
			return defineClass(className, buffer, 0, buffer.length);
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(System.getProperty("sun.boot.class.path"));
		System.out.println("--------------------------------------------------------");
		System.out.println(System.getProperty("java.ext.dirs"));
		System.out.println("--------------------------------------------------------");
		System.out.println(System.getProperty("java.class.path"));
		System.out.println("--------------------------------------------------------");


		System.out.println("============================================================================");
		System.out.println(JavaCompilerTools.class.getClassLoader().getClass().getName());
		System.out.println(JavaCompilerTools.class.getClassLoader().getParent().getClass().getName());
		System.out.println(JavaCompilerTools.class.getClassLoader().getSystemClassLoader().getClass().getName());
		System.out.println("============================================================================");
		
//		Class<?> clss = JavaCompilerTools.compilerStandard("com.jian.tools.core.DebugTools");
//		System.out.println(clss.getName());
	}
}

