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
import com.jian.tools.core.Tools;

/**
 * 自动生成管理器。需要配置的参数：
 * <p>{@code packge} 包路径
 * <p>{@code dbPath} 数据库配置文件（必填）
 * <p>{@code dbPathSecond} 数据库从库配置文件
 * <p>{@code prefix} 表前缀
 * <p>{@code separator} 表分隔符
 * <p>{@code chartset} 生成文件字符集，默认“utf-8”
 * @author liujian
 *
 * @see com.tools.auto.Config
 * @see com.tools.auto.db.TableManager
 */
public class AutoCreateNormal2 {
	
	private Config config =  null;
	private TableManager manager = null;
	private String dbPath = ""; //数据库配置
	private String dbPathSecond = ""; //数据库从库配置
	private String prefix = ""; //数据表前缀
	private String separator = ""; //数据表分隔符
	
	public AutoCreateNormal2(){
		config = new Config();
	}
	
	public AutoCreateNormal2(Config config, ConfigDB dbConfig){
		//配置
		this.config = config;
		//数据库配置
		String realPath = dbConfig.getDBPath();
		String realPath2 = dbConfig.getDBPathSecond();
		this.dbPath = realPath.substring(realPath.lastIndexOf("/")+1);
		this.dbPathSecond = realPath2.substring(realPath2.lastIndexOf("/")+1);
		this.prefix = dbConfig.getPrefix();
		this.separator = dbConfig.getSeparator();
		//绝对地址，不能用相对地址。因为创建的文件还没有刷新到项目里
		manager = new TableManager(realPath);
	}
	
	public AutoCreateNormal2(String packge, String dbPath){
		if(!Tools.isNullOrEmpty(packge)){
			config = new Config(packge);
		}
		if(!Tools.isNullOrEmpty(dbPath)){
			this.dbPath = dbPath;
			manager = new TableManager(dbPath);
		}
	}
	
	public AutoCreateNormal2(String packge, String dbPath, String prefix, String separator){
		if(!Tools.isNullOrEmpty(packge)){
			config = new Config(packge);
		}
		if(!Tools.isNullOrEmpty(dbPath)){
			this.dbPath = dbPath;
			manager = new TableManager(dbPath);
		}
		this.prefix = prefix;
		this.separator = separator;
	}
	
	public void setPackge(String packge){
		config.setBasePackge(packge);
	} 
	
	public void setDbPath(String dbPath){
		if(!Tools.isNullOrEmpty(dbPath)){
			this.dbPath = dbPath;
			manager = new TableManager(dbPath);
		}
	}
	
	public void setDbPathSecond(String dbPathSecond){
		this.dbPathSecond = dbPathSecond;
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	} 
	
	public void setSeparator(String separator){
		this.separator = separator;
	} 
	
	public void setChartset(String chartset){
		config.setChartset(chartset);
	} 
	
	public void setOverWrite(boolean overWrite){
		config.setOverWrite(overWrite);
		
	} 
	
	public void setReqPrefix(String reqPrefix){
		config.setReqPrefix(reqPrefix);
	}
	
	/**
	 * 整库创建
	 */
	public void start(){
		if(manager == null){
			System.out.println("没有配置数据库地址（dbPath）。。。。。。");
			return;
		}
		createBase();
		createTable();
	} 
	
	/**
	 * 整表创建
	 * @param tableName	表名
	 */
	public void start(String tableName){
		if(manager == null){
			System.out.println("没有配置数据库地址（dbPath）。。。。。。");
			return;
		}
		if(Tools.isNullOrEmpty(tableName)){
			System.out.println("表名不能空。。。。。。");
			return;
		}
		createTable(tableName);
	}

	public void createTable(){
		if(manager == null){
			return;
		}
		//解析数据库
		System.out.println("解析数据库...");
		List<Table> list = manager.getDbInfo();
		for (Table table : list) {
			String ename = table.getTableName().replaceFirst(prefix, "");
			ename = ename.substring(0, 1).toUpperCase() + ename.substring(1);
			if(!Tools.isNullOrEmpty(separator)){
				int index = 0;
				while ((index = ename.indexOf(separator)) != -1) {
					ename = ename.substring(0, index)+ename.substring(index+1, index+2).toUpperCase()+ename.substring(index+2);
				}
			}
			table.setEntityName(ename);
			createEntity(table);
		}
		System.out.println("处理完毕...");
	}
	
	public void createTable(String tableName){
		if(manager == null){
			return;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return;
		}
		//解析数据表
		System.out.println("解析数据表...");
		Table table = manager.getDbTable(tableName);
		String ename = table.getTableName().replaceFirst(prefix, "");
		ename = ename.substring(0, 1).toUpperCase() + ename.substring(1);
		if(!Tools.isNullOrEmpty(separator)){
			int index = 0;
			while ((index = ename.indexOf(separator)) != -1) {
				ename = ename.substring(0, index)+ename.substring(index+1, index+2).toUpperCase()+ename.substring(index+2);
			}
		}
		table.setEntityName(ename);
		createEntity(table);
		System.out.println("处理完毕...");
	}
	
	/**
	 * 
	 * @param table 表信息
	 */
	public void createEntity(Table table){
		System.out.println("start create ..." + table.getEntityName());
		String packName = config.getEntityPath(); //包路径
		String eName = table.getEntityName();
		//创建entity
		createEntityFile(packName, table, config.getChartset());
		
		//创建dao
		createDao("TempDao", eName + "Dao");
		createDaoImpl("TempDaoImpl", eName + "DaoImpl");
		createDB(eName);
		//创建service
		createService("TempService", eName + "Service");
		createServiceImpl("TempServiceImpl", eName + "ServiceImpl");
		//创建controller
		createController("TempController", eName + "Controller", table);
	}
	
	/**
	 * 创建dao层接口
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createDao(String tempName, String fileName){
		String packName = config.getDaoPath(); //包路径
		createDaoFile(packName, tempName, fileName, config.getChartset());
	}
	
	/**
	 * 创建dao层实现
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createDaoImpl(String tempName, String fileName){
		String packName = config.getDaoImplPath(); //包路径
		createDaoFile(packName, tempName, fileName, config.getChartset());
	}

	/**
	 * 创建dao层工具
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createDaoUtil(String tempName, String fileName){
		String packName = config.getUtilPath(); //包路径
		createDaoFile(packName, tempName, fileName, config.getChartset());
	}
	
	/**
	 * 注册dao层到DB。 例如：public static final UserDao USER_DAO = new UserDaoImpl();
	 * @param fileName 生成文件名
	 */
	public void createDB(String fileName){
		String packName = config.getUtilPath(); //包路径
		createDBFile(packName, fileName, config.getChartset());
	}

	/**
	 * 创建service层接口
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createService(String tempName, String fileName){
		String packName = config.getServicePath(); //包路径
		createServiceFile(packName, tempName, fileName, config.getChartset());
	}
	
	/**
	 * 创建service层实现
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createServiceImpl(String tempName, String fileName){
		String packName = config.getServiceImplPath(); //包路径
		createServiceFile(packName, tempName, fileName, config.getChartset());
	}

	/**
	 * 创建控制层，以Servlet模式
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createServlet(String tempName, String fileName){
		String packName = config.getServletPath(); //包路径
		createServletFile(packName, tempName, fileName, config.getChartset());
	}

	/**
	 * 创建控制层，以Controller模式
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createController(String tempName, String fileName, Table table){
		String packName = config.getControllerPath(); //包路径
		createControllerFile(packName, tempName, fileName, config.getChartset(), table);
	}
	
	/**
	 * 创建配置文件
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createConfig(String tempName, String fileName){
		String packName = config.getConfigPath(); //包路径
		createConfigFile(packName, tempName, fileName, config.getChartset());
	}
	
	/**
	 * 创建基本信息。包括：BaseDao、BaseDaoImpl、JdbcOperateManager、BaseService、BaseServiceImpl
	 * @param tempName 模版名
	 * @param fileName 生成文件名
	 */
	public void createBase(){
		System.out.println("start create base file...");
		createDao("BaseDao", "BaseDao");
		createDaoImpl("BaseDaoImpl", "BaseDaoImpl");
		createDaoUtil("JdbcOperateManager", "JdbcOperateManager");
		createService("BaseService", "BaseService");
		createServiceImpl("BaseServiceImpl", "BaseServiceImpl");
		createConfig("CtrlConfig","CtrlConfig");
		createConfig("VerifyConfig","VerifyConfig");
		System.out.println("end create base file.");
	}
	
	
	private void createEntityFile(String packName, Table table, String chartset){
		System.out.println("start create entity file... " +packName+" "+table.getEntityName());
		InputStream in = getClass().getResourceAsStream("Temp.txt"); //模版
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + table.getEntityName() + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
									content.add(i+1, "import com.tools.jdbc.PrimaryKey;");
									priFlag = false;
								}
								if(priTypeFlag){
									content.add(i+2, "import com.tools.jdbc.PrimaryKeyType;");
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
									content.add(i+1, "import com.tools.jdbc.PrimaryKey;");
									priFlag = false;
								}
							}else if("//field".equals(content.get(i).trim())){
								content.add(i+1, "	@PrimaryKey");
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
		System.out.println("end create entity file... " +packName+" "+table.getEntityName());
	}
	
	
	private void createDaoFile(String packName, String tempName, String fileName, String chartset){
		System.out.println("start create dao file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(tempName + ".txt"); //模版
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
		System.out.println("end create dao file... " +packName+" "+fileName);
	}
	
	private void createServiceFile(String packName, String tempName, String fileName, String chartset){
		System.out.println("start create service file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(tempName + ".txt"); //模版
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
		System.out.println("end create service file... " +packName+" "+fileName);
	}
	
	private void createDBFile(String packName, String fileName, String chartset){
		System.out.println("start add db file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream("DB.txt"); //模版
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + "DB.java"; //输出路径
		File outFile = new File(outPath);
		System.out.println("output file... " +outPath);
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
				if(str.matches(".*\\s+"+fileName.toUpperCase()+"_DAO\\s+.*")){
					flag = false;
					System.out.println("db已注册... "+ fileName);
				}
			}
			if(flag){
				for (int i = 0; i < content.size(); i++) {
					if("//import".equals(content.get(i).trim())){
						content.add(i+1, "import "+ packName.replace("util", "") + fileName+"Dao;");
						content.add(i+1, "import "+ packName.replace("util", "impl.") + fileName+"DaoImpl;");
					}else if("//dao".equals(content.get(i).trim())){
						content.add(i+1, "	public static final "+fileName+"Dao "+fileName.toUpperCase()+"_DAO = new "+fileName+"DaoImpl();");
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
		System.out.println("end add dao file... " +packName+" "+fileName);
	}
	
	private void createServletFile(String packName, String tempName, String fileName, String chartset){
		System.out.println("start create servlet file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(tempName + ".txt"); //模版
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
		System.out.println("end create servlet file... " +packName+" "+fileName);
	}
	
	private void createControllerFile(String packName, String tempName, String fileName, String chartset, Table table){
		System.out.println("start create controller file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(tempName + ".txt"); //模版路径
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
		System.out.println("end create controller file... " +packName+" "+fileName);
	}
	
	
	private void createConfigFile(String packName, String tempName, String fileName, String chartset){
		System.out.println("start create config file... " +packName+" "+fileName);
		InputStream in = getClass().getResourceAsStream(tempName + ".txt"); //模版路径
		String outPath = Tools.getBaseSrcPath() + packName.replace(".", File.separator) + File.separator + fileName + ".java"; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0 && !config.isOverWrite()){
			return;
		}
		System.out.println("output file... " +outPath);
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
		System.out.println("end create config file... " +packName+" "+fileName);
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
		AutoCreateNormal2 test =  new AutoCreateNormal2(new Config("com.testAuto"), new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator));
		test.start("s_app");
	}
	
}
