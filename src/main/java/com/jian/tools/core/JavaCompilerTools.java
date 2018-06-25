package com.jian.tools.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
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
	public static Map<String, byte[]> classBytes = new HashMap<String, byte[]>();
	
	/**
	 * 
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @return
	 */
	public static Class<?> compiler(String className){
		String source = "";
		String path = Tools.getBaseSrcPath() + className.replace(".", File.separator) + ".java";
		File file = new File(path);
		if(!file.exists()) {
			System.out.println("not find file:	" + path);
			return null;
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
			System.out.println("no source:	" + source);
			return null;
		}
		return compiler(className, source);
	}
	
	/**
	 * 
	 * @param className 类全名。e.g. "com.jian.Test"
	 * @param source java code
	 * @return
	 */
	public static Class<?> compiler(String className, String source){
		byte[] buffer = classBytes.get(className);
		MemoryClassLoader classLoader = new MemoryClassLoader();
		if(buffer != null) {
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
	
}

	class MemoryJavaFileManager  extends ForwardingJavaFileManager<JavaFileManager>{

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
	
	class MemoryInputJavaFileObject extends SimpleJavaFileObject{
		
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
	
	class MemoryOutputJavaFileObject extends SimpleJavaFileObject{

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
					JavaCompilerTools.classBytes.put(className, bos.toByteArray());
				}

			};
		}
		
	}
	
	class MemoryClassLoader extends ClassLoader{


		@Override
		protected Class<?> findClass(String className) throws ClassNotFoundException {
			byte[] buffer = JavaCompilerTools.classBytes.get(className);
			if (buffer == null) {
				return super.findClass(className);
			}
			return defineClass(className, buffer, 0, buffer.length);
		}

		
	}
