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
 * 自动生成管理器，spring boot的实现
 * @author liujian
 *
 * @see com.jian.auto.Config
 * @see com.jian.auto.db.TableManager
 */
public class AutoCreateSpringBoot extends AbstractAutoCreate implements AutoCreate {
	
	private Config config =  null;
	private TableManager manager = null;
	private ConfigDB dbConfig = null;
	private String dbPath = ""; //数据库配置
	private String dbPathSecond = ""; //数据库从库配置
	
	
	public AutoCreateSpringBoot(Config config, ConfigDB dbConfig){
		//配置
		this.config = config;
		this.dbConfig = dbConfig;
		//数据库配置
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
		String packName = config.getEntityPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "Base";
			fileName = "Base";
		}else{
			tempName = "Temp"; 
			fileName = table.getEntityName();
		}
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
		return;
	}
	
	@Override
	public void createDB(Table table){
		return;
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
		return;
	}

	@Override
	public void createController(Table table){
		String packName = config.getControllerPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = ""; //模版名
		String fileName = ""; //文件名
		if(table == null){
			tempName = "BaseController";
			fileName = "BaseController";
		}else{
			tempName = "TempController";
			fileName = table.getEntityName() + "Controller";
		}
		doCreateController(packName, tempName, fileName, chartset, table);
	}
	
	@Override
	public void createConfig(){
		String packName = config.getConfigPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "LoginConfig"; //模版名
		String fileName = "LoginConfig"; //文件名
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
				if(line.indexOf("{Table_Name}") != -1 && table != null){
					line = line.replace("{Table_Name}", table.getTableName());
				}
				//Temp
				if(line.indexOf("Temp") != -1 && table != null){
					line = line.replace("Temp", table.getEntityName());
				}
				content.add(line);
			}
			//遍历字段
			if(table != null){
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
						}
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
		System.out.println(DateTools.formatDate()+":	end create entity file... " +packName+" "+fileName);
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
				String dn = fileName.replace("Impl", "").replace("Service", "Dao"); //dao name
				String sn = fileName.replace("Impl", ""); //service name
				String en = fileName.replace("Impl", "").replace("Service", ""); //entity name
				if(line.indexOf("import Dao;") != -1){
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".dao."+ dn + ";";  //import xxxx.dao.xxxDao
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("impl", sn) + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import T;") != -1){
					isBean = true;
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".entity."+en + ";";  //import xxxx.entity.xxx
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
				}else if(line.indexOf("import Config;") != -1){
					line = "import " + packName.replace("controller", "config.Config") + ";";//import xxxx.config.Config
				}else if(line.indexOf("import LoginConfig;") != -1){
					line = "import " + packName.replace("controller", "config.LoginConfig") + ";";//import xxxx.config.LoginConfig
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
					line = "import " + packName.replace("config", "util.Utils") + ";";//import xxxx.utils.Utils
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
		System.out.println(DateTools.formatDate()+":	start create config file... " +" App");
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + "App.txt"); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... "  +"App.txt");
			return;
		}
		String outPath = Tools.getBaseSrcPath() + config.getBasePackge().replace(".", File.separator) + File.separator  + "App.java"; //输出路径
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
					line = "package " + config.getBasePackge() + ";";
				}
				if(line.indexOf("{basePackage}") != -1){
					line = line.replace("{basePackage}", config.getBasePackge());
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
		System.out.println(DateTools.formatDate()+":	end create config file... " +" App");
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
		AutoCreateSpringBoot test =  new AutoCreateSpringBoot(new Config("com.testAuto"), new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator));
		test.start("s_app");
	}
	
}