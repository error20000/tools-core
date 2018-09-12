package com.jian.auto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	public void createTable() {
		super.createTable();
		//删除数据库配置
		String path = Tools.getBaseResPath() + "autodb.properties"; //主库
		File file = new File(path);
		System.out.println(path + " delete: "+file.delete());
		path = Tools.getBaseResPath() + "autodb2.properties"; //从库
		file = new File(path);
		System.out.println(path + " delete: "+file.delete());
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
		String tempName = "VerifyConfig"; //模版名
		String fileName = "VerifyConfig"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
		tempName = "Config"; //模版名
		fileName = "Config"; //文件名
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
		String chartset = config.getChartset(); //字符集
		String tempName = "application-dev.properties"; //模版名
		String fileName = "application-dev.properties"; //文件名
		doCreateResources(tempName, fileName, chartset);
		tempName = "application-product.properties";
		fileName = "application-prod.properties";
		doCreateResources(tempName, fileName, chartset);
		tempName = "application-test.properties";
		fileName = "application-test.properties"; 
		doCreateResources(tempName, fileName, chartset);
		tempName = "application.properties";
		fileName = "application.properties"; 
		doCreateResources(tempName, fileName, chartset);
		tempName = "logback.xml"; //模版名
		fileName = "logback.xml"; //文件名
		doCreateResources(tempName, fileName, chartset);
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
				int priCount = 0;
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
									priCount++;
									content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.AUTO_INCREMENT)");
									content.add(i+2, "	@Excel(name=\""+structure.getComment().replace("\"", "")
											+"\", sort="+m
											+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"")
											+ ", length=\""+ structure.getLength()+ "\""
											+ ", isNull="+ ("YES".equalsIgnoreCase(structure.getIsNull()) ? 1 : 0) +" )");
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
									priCount++;
								    //判断主键类型
									if(structure.getComment().toLowerCase().contains("uuid")){
										content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.UUID)");
									}else if(priCount > 1){
										content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.GROUP)");
									}else{
										content.add(i+1, "	@PrimaryKey(type=PrimaryKeyType.NORMAL)");
									}
									content.add(i+2, "	@Excel(name=\""+structure.getComment().replace("\"", "")
											+"\", sort="+m
											+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"")
											+ ", length=\""+ structure.getLength()+ "\""
											+ ", isNull="+ ("YES".equalsIgnoreCase(structure.getIsNull()) ? 1 : 0) +" )");
									content.add(i+3, "	private "+structure.getType()+" "+structure.getField()+";");
								}
							}
						}
					}else{
						for (int i = 0; i < content.size(); i++) {
							if("//field".equals(content.get(i).trim())){
								content.add(i+1, "	@Excel(name=\""+structure.getComment().replace("\"", "")
										+"\", sort="+m
										+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"")
										+ ", length=\""+ structure.getLength()+ "\""
										+ ", isNull="+ ("YES".equalsIgnoreCase(structure.getIsNull()) ? 1 : 0) +" )");
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
				//修正主键类型
				if(priCount > 1){
					for (int i = 0; i < content.size(); i++) {
						String temp = content.get(i).replace("PrimaryKeyType.NORMAL", "PrimaryKeyType.GROUP");
						content.set(i, temp);
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
				String dn = fileName.replaceAll("Impl$", ""); //dao name
				String en = fileName.replaceAll("Impl$", "").replaceAll("Dao$", ""); //entity name
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
				String dn = fileName.replaceAll("Impl$", "").replaceAll("Service$", "Dao"); //dao name
				String sn = fileName.replaceAll("Impl$", ""); //service name
				String en = fileName.replaceAll("Impl$", "").replaceAll("Service$", ""); //entity name
				if(line.indexOf("import Dao;") != -1){
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".dao."+ dn + ";";  //import xxxx.dao.xxxDao
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("impl", sn) + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import Config;") != -1){
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".config.Config" + ";"; //import xxxx.config.Config
				}else if(line.indexOf("import Utils;") != -1){
					line = "import " + packName.replace(".service", "").replace(".impl", "")+".util.Utils" + ";"; //import xxxx.util.Utils
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
			List<String> content = new ArrayList<String>();
			String line; 
			while((line = br.readLine()) != null){ 
				//packge
				if(line.indexOf("package PK;") != -1){
					line = "package " + packName + ";";
				}
				//import
				String en = fileName.replaceAll("Controller$", ""); //entity name
				if(line.indexOf("import T;") != -1){
					line = "import " + packName.replace("controller", "entity."+en) + ";"; //import xxxx.entity.xxx
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("controller", "service."+en+"Service") + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import ServiceImpl;") != -1){
					line = "import " + packName.replace("controller", "service.impl."+en+"ServiceImpl") + ";"; //import xxxx.service.impl.xxxServiceImpl
				}else if(line.indexOf("import Config;") != -1){
					line = "import " + packName.replace("controller", "config.Config") + ";";//import xxxx.config.Config
				}else if(line.indexOf("import VerifyConfig;") != -1){
					line = "import " + packName.replace("controller", "config.VerifyConfig") + ";";//import xxxx.config.VerifyConfig
				}else if(line.indexOf("import Utils;") != -1){
					line = "import " + packName.replace("controller", "util.Utils") + ";";//import xxxx.util.Utils
				}else{
					line = line.replace("Temp", en);
				}
				//mapping
				if(line.indexOf("{path}") != -1){
					line = line.replace("{path}", en.substring(0, 1).toLowerCase()+en.substring(1));
				}
				if(line.indexOf("{reqPrefix}") != -1){
					line = line.replace("{reqPrefix}", config.getReqPrefix());
				}
				if(line.indexOf("{comment}") != -1){
					line = line.replace("{comment}", table.getComment());
				}
				content.add(line);
			}
			//api
			String autoFillPrimaryKey = config.getAutoFillPrimaryKey();
			String autoFillDateForAdd = config.getAutoFillDateForAdd();
			String autoFillDateForModify = config.getAutoFillDateForModify();
			/*Class<?> clzz = JavaCompilerTools.compiler(packName.replace("controller", "config.Config"));
			Field[] f =  Tools.getFields(clzz);
		     for (int i = 0; i < f.length; i++)  {
		    	 	if(f[i].getName().equals("autoFillPrimaryKey")){
		    	 		autoFillPrimaryKey = (String) f[i].get(null);
		    	  }else if(f[i].getName().equals("autoFillDateForAdd")){
		    		  autoFillDateForAdd = (String) f[i].get(null);
		    	  }else if(f[i].getName().equals("autoFillDateForModify")){
		    		  autoFillDateForModify = (String) f[i].get(null);
		    	  }
		     }*/
		   //request
			for (int i = 0; i < content.size(); i++) {
				if("//add request".equals(content.get(i).trim())){
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						String str = structure.getComment().replace("：", " ").replace(":", " ").replace("\t", " ").replace("\n", " ").split(" ")[0];
						if(autoFillPrimaryKey.toLowerCase().contains(str.toLowerCase())){
							continue;
						}else if(autoFillDateForAdd.toLowerCase().contains(str.toLowerCase())){
							continue;
						}else if(autoFillDateForModify.toLowerCase().contains(str.toLowerCase())){
							continue;
						}
						//@PrimaryKey
						if("PRI".equals(structure.getKey())){
							if("auto_increment".equals(structure.getExtra())){
								continue;
							}else{
								count++;
								content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=1,  info=\""+structure.getComment().replace("\"", "")+"\"),");
							}
						}else{
							count++;
							content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull="+("YES".equalsIgnoreCase(structure.getIsNull()) ? 0 : 1)+",  info=\""+structure.getComment().replace("\"", "")+"\"),");
						}
					}
					
				}else if("//modify request".equals(content.get(i).trim())){
					//主键必填，其他选填
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"修改条件：\"),");
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						String str = structure.getComment().replace("：", " ").replace(":", " ").replace("\t", " ").replace("\n", " ").split(" ")[0];
						if(autoFillDateForAdd.toLowerCase().contains(str.toLowerCase())){
							continue;
						}else if(autoFillDateForModify.toLowerCase().contains(str.toLowerCase())){
							continue;
						}
						//@PrimaryKey
						if("PRI".equals(structure.getKey())){
							count++;
							content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=1,  info=\""+structure.getComment().replace("\"", "")+"\"),");
						}
					}
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"可修改字段：\"),");
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						String str = structure.getComment().replace("：", " ").replace(":", " ").replace("\t", " ").replace("\n", " ").split(" ")[0];
						if(autoFillDateForAdd.toLowerCase().contains(str.toLowerCase())){
							continue;
						}else if(autoFillDateForModify.toLowerCase().contains(str.toLowerCase())){
							continue;
						}
						//@PrimaryKey
						if("PRI".equals(structure.getKey())){
							//do nothing
						}else{
							count++;
							content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=0,  info=\""+structure.getComment().replace("\"", "")+"\"),");
						}
					}
				}else if("//delete request".equals(content.get(i).trim())){
					//主键必填
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						//@PrimaryKey
						if("PRI".equals(structure.getKey())){
							count++;
							content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=1,  info=\""+structure.getComment().replace("\"", "")+"\"),");
						}
					}
				}else if("//deleteBy request".equals(content.get(i).trim())){
					//主键必填
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						count++;
						content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=0,  info=\""+structure.getComment().replace("\"", "")+"\"),");
					}
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"注意：以上条件不可同时为空。\"),");
				}else if("//findPage request".equals(content.get(i).trim()) ){
					//主键必填
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"可选条件：\"),");
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						count++;
						content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=0,  info=\""+structure.getComment().replace("\"", "")+"\"),");
					}
				}else if("//findOne request".equals(content.get(i).trim()) || "//findList request".equals(content.get(i).trim()) ){
					//主键必填
					List<Structure> sList = table.getTableInfo();
					int count = 0;
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"可选条件：\"),");
					for (int m = 0; m < sList.size(); m++) {
						Structure structure = sList.get(m);
						count++;
						content.add(i+count, "				@ParamsInfo(name=\""+structure.getField()+"\", type=\""+structure.getType()+"\", isNull=0,  info=\""+structure.getComment().replace("\"", "")+"\"),");
					}
					count++;
					content.add(i+count, "				@ParamsInfo(info=\"注意：以上条件不可同时为空。\"),");
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
	
	private void doCreateResources(String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create config file... " +fileName);
		InputStream in = getClass().getResourceAsStream(Config.getTempPath() + tempName); //模版路径
		if(in == null){
			System.out.println(DateTools.formatDate()+":	not find template... " + tempName);
			return;
		}
		String outPath = Tools.getBaseResPath() + fileName; //输出路径
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
				//JDBC
				if(line.indexOf("{jdbcUrl}") != -1){
					line = line.replace("{jdbcUrl}", dbConfig.getJdbcUrl());
				}
				if(line.indexOf("{user}") != -1){
					line = line.replace("{user}", dbConfig.getUser());
				}
				if(line.indexOf("{password}") != -1){
					line = line.replace("{password}", dbConfig.getPassword());
				}
				if(line.indexOf("{driverClass}") != -1){
					line = line.replace("{driverClass}", dbConfig.getDriverClass());
				}
				//自主填充
				if(line.indexOf("{auto_fill_primary_key}") != -1){
					line = line.replace("{auto_fill_primary_key}", Tools.string2Unicode(config.getAutoFillPrimaryKey()));
				}
				if(line.indexOf("{auto_fill_date_for_add}") != -1){
					line = line.replace("{auto_fill_date_for_add}", Tools.string2Unicode(config.getAutoFillDateForAdd()));
				}
				if(line.indexOf("{auto_fill_date_for_modify}") != -1){
					line = line.replace("{auto_fill_date_for_modify}", Tools.string2Unicode(config.getAutoFillDateForModify()));
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
		System.out.println(DateTools.formatDate()+":	end create config file... " +fileName);
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
				}else if(line.indexOf("import App;") != -1){
					line = "import " + packName.replace("controller", "App") + ";"; //xxxx.App
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
		AutoCreateSpringBoot test =  new AutoCreateSpringBoot(new Config("com.testAuto"), new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator));
		test.start("s_app");
	}
	
}
