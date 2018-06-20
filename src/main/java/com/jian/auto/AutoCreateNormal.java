package com.jian.auto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.jian.auto.db.Structure;
import com.jian.auto.db.Table;
import com.jian.auto.db.TableManager;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.Tools;

/**
 * 自动生成管理器，自己的实现
 * @author liujian
 *
 * @see com.jian.auto.Config
 * @see com.jian.auto.db.TableManager
 */
public class AutoCreateNormal extends AbstractAutoCreate implements AutoCreate {
	
	private Config config =  null;
	private TableManager manager = null;
	private ConfigDB dbConfig = null;
	private String dbPath = ""; //数据库配置
	private String dbPathSecond = ""; //数据库从库配置
	
	
	public AutoCreateNormal(Config config, ConfigDB dbConfig){
		//配置
		this.config = config;
		this.dbConfig = dbConfig;
		//数据库配置文件
		String realPath = dbConfig.getDBPath();
		String realPath2 = dbConfig.getDBPathSecond();
		this.dbPath = realPath.substring(realPath.lastIndexOf("/")+1);
		this.dbPathSecond = realPath2.substring(realPath2.lastIndexOf("/")+1);
		//绝对地址，不能用相对地址。因为创建的文件还没有刷新到项目里
		manager = new TableManager(realPath);
	}
	
	
	@Override
	public void initConfigDB() {
		super.config = this.dbConfig;
		
	}

	@Override
	public void initTableManager() {
		super.manager = this.manager;
		
	}

	
	@Override
	public void createEntity(Table table) {
		if(table == null){ //没有基础entity
			return;
		}
		String packName = config.getEntityPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "Temp"; //模版名
		String fileName = table.getEntityName(); //文件名
		doCreateEntity(packName, tempName, fileName, chartset, table);
	}

	@Override
	public void createDao(Table table){
		String packName = config.getDaoPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "BaseDao";
			fileName = "BaseDao";
		}else{
			tempName = "TempDao";
			fileName = table.getEntityName() + "Dao";
		}
		doCreateDao(packName, tempName, fileName, chartset);
	}
	
	@Override
	public void createDaoImpl(Table table){
		String packName = config.getDaoImplPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "BaseDaoImpl";
			fileName = "BaseDaoImpl";
		}else{
			tempName = "TempDaoImpl";
			fileName = table.getEntityName() + "DaoImpl";
		}
		doCreateDao(packName, tempName, fileName, chartset);
	}

	@Override
	public void createDaoUtil(Table table){
		String packName = config.getDaoUtilPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "JdbcOperateManager"; //模版名
		String fileName = "JdbcOperateManager"; //文件名
		doCreateDao(packName, tempName, fileName, chartset);
	}
	
	@Override
	public void createDB(Table table){
		if(table == null){
			return;
		}
		String packName = config.getDaoUtilPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "DB"; //模版名
		String fileName = "DB"; //文件名
		String entityName = table.getEntityName(); //文件名
		doCreateDB(packName, tempName, fileName, chartset, entityName);
	}

	@Override
	public void createService(Table table){
		String packName = config.getServicePath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "BaseService";
			fileName = "BaseService";
		}else{
			tempName = "TempService";
			fileName = table.getEntityName() + "Service";
		}
		doCreateService(packName, tempName, fileName, chartset);
	}
	
	@Override
	public void createServiceImpl(Table table){
		String packName = config.getServiceImplPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "BaseServiceImpl";
			fileName = "BaseServiceImpl";
		}else{
			tempName = "TempServiceImpl";
			fileName = table.getEntityName() + "ServiceImpl";
		}
		doCreateService(packName, tempName, fileName, chartset);
	}

	@Override
	public void createServlet(Table table){
		if(table == null){
			return;
		}
		String packName = config.getServletPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "Servlet"; //模版名
		String fileName = table.getEntityName() + "Servlet"; //文件名
		doCreateServlet(packName, tempName, fileName, chartset);
	}

	@Override
	public void createController(Table table){
		if(table == null){
			return;
		}
		String packName = config.getControllerPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "TempController"; //模版名
		String fileName = table.getEntityName() + "Controller"; //文件名
		doCreateController(packName, tempName, fileName, chartset, table);
	}
	
	@Override
	public void createConfig(){
		String packName = config.getConfigPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "CtrlConfig"; //模版名
		String fileName = "CtrlConfig"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
		tempName = "VerifyConfig"; //模版名
		fileName = "VerifyConfig"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
	}

	@Override
	public void createUtils(){
		String packName = config.getUtilPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "Utils"; //模版名
		String fileName = "Utils"; //文件名
		doCreateUtils(packName, tempName, fileName, chartset);
	}

	@Override
	public void createResources(){
		return;
	}
	
	@Override
	public void createStart(){
		doCreateStart();
	}
	
	@Override
	public void createApi(){
		String packName = config.getControllerPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "APIController"; //模版名
		String fileName = "APIController"; //文件名
		doCreateApi(packName, tempName, fileName, chartset);
	}
	
	
	private void doCreateEntity(String packName, String tempName, String fileName, String chartset, Table table){
		
		System.out.println(DateTools.formatDate()+":	start create entity file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			List<String> content = new ArrayList<String>();
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//table
				if(line.indexOf("{Table_Name}") != -1){
					line = line.replace("{Table_Name}", table.getTableName());
				}
				//Temp
				if(line.indexOf("Temp") != -1){
					line = line.replace("Temp", table.getEntityName());
				}
				content.add(line);
			}
			//遍历字段
			boolean priFlag = true; //判断PrimaryKey是否已添加
			boolean priTypeFlag = true; //判断PrimaryKeyType是否已添加
			List<Structure> sList = table.getTableInfo();
			for (int m = sList.size() - 1; m >= 0; m--) {
				Structure structure = sList.get(m);
				//@PrimaryKey
				if("PRI".equals(structure.getKey())){
					if("auto_increment".equals(structure.getExtra())){
						for (int i = 0; i < content.size(); i++) {
							if("//import".equals(content.get(i).trim())){
								if(priFlag){
									content.add(i+1, "import com.jian.annotation.PrimaryKey;");
									priFlag = false;
								}
								if(priTypeFlag){
									content.add(i+2, "import com.jian.annotation.PrimaryKeyType;");
									priTypeFlag = false;
								}
							}else if("//field".equals(content.get(i).trim())){
								content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.AUTO_INCREMENT)");
								content.add(i+2, "	@Excel(name=\""+structure.getComment().replace("\"", "")+"\", sort="+m+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"") +" )");
								content.add(i+3, "	private "+structure.getType()+" "+structure.getField()+";");
							}
						}
					}else{
						for (int i = 0; i < content.size(); i++) {
							if("//import".equals(content.get(i).trim())){
								if(priFlag){
									content.add(i+1, "import com.jian.annotation.PrimaryKey;");
									priFlag = false;
								}
								if(priTypeFlag){
									content.add(i+2, "import com.jian.annotation.PrimaryKeyType;");
									priTypeFlag = false;
								}
							}else if("//field".equals(content.get(i).trim())){
								if("String".equals(structure.getType())){
									content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.UUID)");
								}else{
									content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.NORMAL)");
								}
								content.add(i+2, "	@Excel(name=\""+structure.getComment().replace("\"", "")+"\", sort="+m+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"") +" )");
								content.add(i+3, "	private "+structure.getType()+" "+structure.getField()+";");
							}
						}
					}
				}else{
					for (int i = 0; i < content.size(); i++) {
						if("//field".equals(content.get(i).trim())){
							content.add(i+1, "	@Excel(name=\""+structure.getComment().replace("\"", "")+"\", sort="+m+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"") +" )");
							content.add(i+2, "	private "+structure.getType()+" "+structure.getField()+";");
						}
					}
				}
				//public
				for (int i = 0; i < content.size(); i++) {
					if("//get set".equals(content.get(i).trim())){
						content.add(i+1, "	public "+structure.getType()+" get"+(structure.getField().substring(0,1).toUpperCase() + structure.getField().substring(1))+"() {");
						content.add(i+2, "		return "+structure.getField()+";");
						content.add(i+3, "	}");
						
						content.add(i+4, "	public void set"+(structure.getField().substring(0,1).toUpperCase() + structure.getField().substring(1))+"("+structure.getType()+" "+structure.getField()+") {");
						content.add(i+5, "		this."+structure.getField()+" = "+structure.getField()+";");
						content.add(i+6, "	}");
					}else if("//serialize".equals(content.get(i).trim())){
						content.add(i+1, "		json.put(\""+structure.getField()+"\", "+structure.getField()+");");
					}else if("//unserialize".equals(content.get(i).trim())){
						content.add(i+1, "			"+structure.getField()+" = json.get"+("String".equals(structure.getType()) ? "String" : structure.getType().substring(0,1).toUpperCase() + structure.getType().substring(1)+"Value" )+"(\""+structure.getField()+"\");");
					}
				}
			}
			
			for (String str : content) {
				bw.write(str);
				bw.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create entity file... " +packName+" "+table.getEntityName());
	}
	
	
	private void doCreateDao(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create dao file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			boolean isBean = false;
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//import
				String dn = fileName.replace("Impl", ""); //dao name
				String en = fileName.replace("Impl", "").replace("Dao", ""); //entity name
				if(line.indexOf("import JdbcOperateManager;") != -1){
					line = "import " + packName.replace("impl", "util.JdbcOperateManager") + ";"; //import xxxx.dao.util.JdbcOperateManager
				}else if(line.indexOf("import Dao;") != -1){
					line = "import " + packName.replace("impl", dn) + ";"; //import xxxx.dao.xxxDao
				}else if(line.indexOf("import T;") != -1){
					isBean = true;
					line = "import " + packName.replace(".dao", "").replace(".impl", "")+".entity."+en + ";"; //import xxxx.entity.xxx
				}else{
					if(isBean){
						line = line.replace("<T>", "<"+en+">");
						line = line.replace("Temp", en);
					}
				}
				//db file
				if(line.indexOf("{dbPath}") != -1){
					line = line.replace("{dbPath}", dbPath);
				}
				if(line.indexOf("{dbPathSecond}") != -1){
					line = line.replace("{dbPathSecond}", dbPathSecond);
				}
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create dao file... " +packName+" "+fileName);
	}
	
	private void doCreateDB(String packName, String tempName, String fileName, String chartset, String entityName){
		System.out.println(DateTools.formatDate()+":	start add db file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName+".txt"); //模版
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName +".java"; //输出路径
		File outFile = new File(outPath);
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		try {
			boolean isExist = outFile.exists();
			RandomAccessFile rafWrite = new RandomAccessFile(outFile, "rw"); 
			List<String> content = new ArrayList<String>();
			String line;
			//如果文件不存在，先创建模版。
			if(!isExist || outFile.length() == 0){
				//以只读方式打开并读取一行数据
				BufferedReader br = new BufferedReader(new InputStreamReader(in, chartset)); 
				File pfile = outFile.getParentFile();
				if(!pfile.exists()){
					pfile.mkdirs();
				}
				while ((line = br.readLine()) != null) {
					if(line.indexOf("package PK;") != -1){
						line = "package " + packName + ";";
					}
					content.add(line);
				}
				br.close();
			}else{
				while ((line = rafWrite.readLine()) != null) {
					content.add(new String(line.getBytes("ISO-8859-1"), chartset));
				}
				rafWrite.seek(0);
			}
			//判断dao层是否已注册
			boolean flag = true;
			for (String str : content) {
				if(str.matches(".*\\s+"+entityName.toUpperCase()+"_DAO\\s+.*")){
					flag = false;
					System.out.println(DateTools.formatDate()+":	db已注册... "+ entityName);
				}
			}
			if(flag){
				for (int i = 0; i < content.size(); i++) {
					if("//import".equals(content.get(i).trim())){
						content.add(i+1, "import "+ packName.replace("util", "") + entityName+"Dao;");
						content.add(i+1, "import "+ packName.replace("util", "impl.") + entityName+"DaoImpl;");
					}else if("//dao".equals(content.get(i).trim())){
						content.add(i+1, "	public static final "+entityName+"Dao "+entityName.toUpperCase()+"_DAO = new "+entityName+"DaoImpl();");
					}
				}
				for (String str : content) {
					rafWrite.write(str.getBytes());
					rafWrite.write("\n".getBytes());
				}
			}
			rafWrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(DateTools.formatDate()+":	end add dao file... " +packName+" "+entityName);
	}
	
	private void doCreateService(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create service file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			boolean isBean = false;
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//import
				String sn = fileName.replace("Impl", ""); //service name
				String en = fileName.replace("Impl", "").replace("Service", ""); //entity name
				if(line.indexOf("import DB;") != -1){
					line = "import " + packName.replace("service.impl", "dao.util.DB") + ";"; //import xxxx.dao.util.DB
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("impl", sn) + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import T;") != -1){
					isBean = true;
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".entity."+en + ";";  //import xxxx.entity.xxx
				}else if(line.indexOf("DB.TEMP_DAO") != -1){
					line = line.replace("DB.TEMP_DAO", "DB."+en.toUpperCase()+"_DAO");
				}else{
					if(isBean){
						line = line.replace("<T>", "<"+en+">");
						line = line.replace("Temp", en);
					}
				}
				
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create service file... " +packName+" "+fileName);
	}
	
	
	
	private void doCreateServlet(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create servlet file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			boolean isBean = false;
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//import
				String dn = fileName.replace("Impl", ""); //dao name
				String en = fileName.replace("Impl", "").replace("Dao", ""); //entity name
				if(line.indexOf("import JdbcOperateManager;") != -1){
					line = "import " + packName.replace("impl", "util.JdbcOperateManager") + ";"; //import xxxx.dao.util.JdbcOperateManager
				}else if(line.indexOf("import Dao;") != -1){
					line = "import " + packName.replace("impl", dn) + ";"; //import xxxx.dao.xxxDao
				}else if(line.indexOf("import T;") != -1){
					isBean = true;
					line = "import " + packName.replace("dao.impl", "entity."+en) + ";"; //import xxxx.entity.xxx
				}else{
					if(isBean){
						line = line.replace("<T>", "<"+en+">");
						line = line.replace("Temp", en);
					}
				}
				
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create servlet file... " +packName+" "+fileName);
	}
	
	private void doCreateController(String packName, String tempName, String fileName, String chartset, Table table){
		System.out.println(DateTools.formatDate()+":	start create controller file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + Config.getTempPath() + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//import
				String en = fileName.replace("Controller", ""); //entity name
				if(line.indexOf("import T;") != -1){
					line = "import " + packName.replace("controller", "entity."+en) + ";"; //import xxxx.entity.xxx
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("controller", "service."+en+"Service") + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import ServiceImpl;") != -1){
					line = "import " + packName.replace("controller", "service.impl."+en+"ServiceImpl") + ";"; //import xxxx.service.impl.xxxServiceImpl
				}else if(line.indexOf("import CtrlConfig;") != -1){
					line = "import " + packName.replace("controller", "config.CtrlConfig") + ";";//import xxxx.config.CtrlConfig
				}else if(line.indexOf("import VerifyConfig;") != -1){
					line = "import " + packName.replace("controller", "config.VerifyConfig") + ";";//import xxxx.config.VerifyConfig
				}else if(line.indexOf("import Utils;") != -1){
					line = "import " + packName.replace("controller", "util.Utils") + ";";//import xxxx.util.Utils
				}else{
					line = line.replace("Temp", en);
				}
				//mapping
				if(line.indexOf("{path}") != -1){
					line = line.replace("{path}", en.toLowerCase());
				}
				if(line.indexOf("{reqPrefix}") != -1){
					line = line.replace("{reqPrefix}", config.getReqPrefix());
				}
				if(line.indexOf("{comment}") != -1){
					line = line.replace("{comment}", table.getComment());
				}
				
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create controller file... " +packName+" "+fileName);
	}
	
	
	private void doCreateConfig(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create config file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}else if(line.indexOf("import Utils;") != -1){
					line = "import " + packName.replace("config", "util.Utils") + ";";//import xxxx.util.Utils
				}
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create config file... " +packName+" "+fileName);
	}
	
	private void doCreateUtils(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create config file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), chartset)); 
			br = new BufferedReader(new InputStreamReader(in, chartset)); 
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create config file... " +packName+" "+fileName);
	}
	
	private void doCreateStart(){
		System.out.println(DateTools.formatDate()+":	start create config file... " +" javax.servlet.ServletContainerInitializer");
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + "javax.servlet.ServletContainerInitializer"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... "  +" javax.servlet.ServletContainerInitializer");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + "/META-INF/services/javax.servlet.ServletContainerInitializer"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile))); 
			br = new BufferedReader(new InputStreamReader(in)); 
			String line; 
			while((line = br.readLine()) != null){ 
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create config file... " +" javax.servlet.ServletContainerInitializer");
	}
	
	
	private void doCreateApi(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create controller file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName + ".txt"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + Config.getTempPath() + tempName + ".txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println(DateTools.formatDate()+":	output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile))); 
			br = new BufferedReader(new InputStreamReader(in)); 
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//mapping
				if(line.indexOf("{reqPrefix}") != -1){
					line = line.replace("{reqPrefix}", config.getReqPrefix());
				}
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println(DateTools.formatDate()+":	end create config file... " +packName+" "+fileName);
	}
	
	
	public static void main(String[] args) {
		
//		System.out.println(System.getProperty("user.dir"));
//        File directory = new File("");//设定为当前文件夹  
//        try{  
//           System.out.println(directory.getCanonicalPath());//获取标准的路径  
//           System.out.println(directory.getAbsolutePath());//获取绝对路径  
//       }catch(Exception e)  
//        {  
//            e.printStackTrace();  
//        }

		
//		AutoCreateManager test =  new AutoCreateManager("com.testAuto", "db.properties", "s_", "_");
//		test.start();
		
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/auth?characterEncoding=utf8";
		String user = "root";
		String password = "123456";
		String driverClass = "com.mysql.jdbc.Driver";
		String prefix = "s_";
		String separator = "_";
		AutoCreateNormal test =  new AutoCreateNormal(new Config("com.testAuto"), new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator));
		test.start("s_app");
	}
	
}
