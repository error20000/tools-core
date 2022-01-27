package com.jian.auto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jian.auto.db.Structure;
import com.jian.auto.db.Table;
import com.jian.auto.db.TableManager;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.ResultKey;
import com.jian.tools.core.Tools;

/**
 * 	自动生成管理器，spring boot的实现
 * @author liujian
 *
 * @see com.jian.auto.Config
 * @see com.jian.auto.db.TableManager
 */
public class AutoCreateSpringBootMybatis extends AbstractAutoCreate implements AutoCreate {
	
	private Config config =  null;
	private TableManager manager = null;
	private ConfigDB dbConfig = null;
	private String dbPath = ""; //数据库配置
	private String dbPathSecond = ""; //数据库从库配置
	
	
	public AutoCreateSpringBootMybatis(Config config, ConfigDB dbConfig){
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
			tempName = "BaseDaoMM";
			fileName = "BaseDao";
		}else{
			tempName = "TempDaoM";
			fileName = table.getEntityName() + "Dao";
		}
		doCreateDao(packName, tempName, fileName, chartset);
	}
	
	@Override
	public void createDaoImpl(Table table){
//		String packName = config.getDaoImplPath(); //包路径
//		String chartset = config.getChartset(); //字符集
//		String tempName = ""; //模版名
//		String fileName = ""; //文件名
//		if(table == null){
//			tempName = "BaseDaoImpl";
//			fileName = "BaseDaoImpl";
//		}else{
//			tempName = "TempDaoImpl";
//			fileName = table.getEntityName() + "DaoImpl";
//		}
//		doCreateDao(packName, tempName, fileName, chartset);
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
			tempName = "BaseServiceM";
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
			tempName = "BaseServiceImplM";
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
		//doCreateConfig(packName, tempName, fileName, chartset);
		tempName = "Config"; //模版名
		fileName = "Config"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
		tempName = "Constant"; //模版名
		fileName = "Constant"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
	}

	@Override
	public void createUtils(){
		String packName = config.getUtilPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "Utils"; //模版名
		String fileName = "Utils"; //文件名
		doCreateUtils(packName, tempName, fileName, chartset);
		tempName = "UploadUtils"; //模版名
		fileName = "UploadUtils"; //文件名
		doCreateUtils(packName, tempName, fileName, chartset);
		tempName = "SnowflakeIdWorker"; //模版名
		fileName = "SnowflakeIdWorker"; //文件名
		doCreateUtils(packName, tempName, fileName, chartset);
		tempName = "TokenUtils"; //模版名
		fileName = "TokenUtils"; //文件名
//		doCreateUtils(packName, tempName, fileName, chartset); // TokenHandler 代替 TokenUtils
		if(!config.isApi()) {
			return;
		}
		packName = config.getBasePackge(); //包路径
		chartset = config.getChartset(); //字符集
		tempName = "Doc"; //模版名
		fileName = "Doc"; //文件名
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
		if(!config.isApi()) {
			return;
		}
		String packName = config.getControllerPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "APIController"; //模版名
		String fileName = "APIController"; //文件名
		doCreateApi(packName, tempName, fileName, chartset);
	}

	@Override
	public void createAspect(){
		//Annotation
		String packName = config.getAspectAnnotationPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "VerifySign"; //模版名
		String fileName = "VerifySign"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "VerifyLogin"; //模版名
		fileName = "VerifyLogin"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "VerifyAuth"; //模版名
		fileName = "VerifyAuth"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "SysLog"; //模版名
		fileName = "SysLog"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "SysLogType"; //模版名
		fileName = "SysLogType"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		//aspect
		packName = config.getAspectPath(); //包路径
		tempName = "VerifySignAspect"; //模版名
		fileName = "VerifySignAspect"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "VerifyLoginAspect"; //模版名
		fileName = "VerifyLoginAspect"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "VerifyAuthAspect"; //模版名
		fileName = "VerifyAuthAspect"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
		tempName = "SysLogAspect"; //模版名
		fileName = "SysLogAspect"; //文件名
		doCreateAspect(packName, tempName, fileName, chartset);
	}
	
	@Override
	public void createException(){
		String packName = config.getExceptionPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "ServiceException"; //模版名
		String fileName = "ServiceException"; //文件名
		doCreateException(packName, tempName, fileName, chartset);
		tempName = "GlobalExceptionHandler"; //模版名
		fileName = "GlobalExceptionHandler"; //文件名
		doCreateException(packName, tempName, fileName, chartset);
	}

	@Override
	public void createResult(){
		String packName = config.getResultPath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "RKey"; //模版名
		String fileName = "RKey"; //文件名
		doCreateResult(packName, tempName, fileName, chartset);
		tempName = "RTools"; //模版名
		fileName = "RTools"; //文件名
		doCreateResult(packName, tempName, fileName, chartset);
		tempName = "RBuilder"; //模版名
		fileName = "RBuilder"; //文件名
		doCreateResult(packName, tempName, fileName, chartset);
		if("Tips2".equals(config.getTipsVersion())){
			tempName = "Tips2";
			fileName = "Tips";
		}else{
			tempName = "Tips1";
			fileName = "Tips";
		}
		doCreateResult(packName, tempName, fileName, chartset);
	}

	@Override
	public void createHandle(){
		String packName = config.getHandlePath(); //包路径
		String chartset = config.getChartset(); //字符集
		String tempName = "DefaultTokenHandler"; //模版名
		String fileName = "DefaultTokenHandler"; //文件名
		doCreateHandle(packName, tempName, fileName, chartset);
		packName = config.getConfigPath(); //包路径
		chartset = config.getChartset(); //字符集
		tempName = "TokenConfig"; //模版名
		fileName = "TokenConfig"; //文件名
		doCreateConfig(packName, tempName, fileName, chartset);
	}


	
	private String commonFormat(String packName, String line) {
		//packge
		if(line.indexOf("package PK;") != -1){
			line = "package " + packName + ";";
		//import
		}else if(line.indexOf("import BaseDao;") != -1){
			line = "import " + config.getDaoPath() + ".BaseDao;"; //import xxxx.dao.BaseDao
		}else if(line.indexOf("import Utils;") != -1){
			line = "import " + config.getUtilPath() + ".Utils;"; //import xxxx.util.Utils
		}else if(line.indexOf("import VerifySign;") != -1){
			line = "import " + config.getAspectAnnotationPath()+ ".VerifySign;"; //xxxx.aspect.annotation.VerifySign
		}else if(line.indexOf("import VerifyLogin;") != -1){
			line = "import " + config.getAspectAnnotationPath()+ ".VerifyLogin;"; //xxxx.aspect.annotation.VerifyLogin
		}else if(line.indexOf("import VerifyAuth;") != -1){
			line = "import " + config.getAspectAnnotationPath()+ ".VerifyAuth;"; //xxxx.aspect.annotation.VerifyAuth
		}else if(line.indexOf("import SysLog;") != -1){
			line = "import " + config.getAspectAnnotationPath()+ ".SysLog;"; //xxxx.aspect.annotation.SysLog
		}else if(line.indexOf("import SysLogType;") != -1){
			line = "import " + config.getAspectAnnotationPath()+ ".SysLogType;"; //xxxx.aspect.annotation.SysLogType
		}else if(line.indexOf("import App;") != -1){
			line = "import " + config.getBasePackge()+ ".App;"; //import xxxx.App
		}else if(line.indexOf("import Config;") != -1){
			line = "import " + config.getBasePackge()+ ".config.Config;"; //import xxxx.config.Config
		}else if(line.indexOf("import Constant;") != -1){
			line = "import " + config.getBasePackge()+ ".config.Constant;"; //import xxxx.config.Constant
		}else if(line.indexOf("import ServiceException;") != -1){
			line = "import " + config.getExceptionPath()+ ".ServiceException;"; //import xxxx.exception.ServiceException
		}else if(line.indexOf("import Tips;") != -1){
			line = "import " + config.getResultPath()+ ".Tips;"; //import xxxx.result.Tips
		}else if(line.indexOf("import RKey;") != -1){
			line = "import " + config.getResultPath()+ ".RKey;"; //import xxxx.result.RKey
		}else if(line.indexOf("import RTools;") != -1){
			line = "import " + config.getResultPath()+ ".RTools;"; //import xxxx.result.RTools
		}else if(line.indexOf("import DefaultTokenHandler;") != -1){
			line = "import " + config.getHandlePath()+ ".DefaultTokenHandler;"; //import xxxx.handle.DefaultTokenHandler
		}
		//@Date
		if(line.trim().endsWith("@Date")){
			line += new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		//Tips 替换
		if("Tips2".equals(config.getTipsVersion())) {
			line = line
					.replace("Tips.ERROR101", "Tips.ERRORProxy1")
					.replace("Tips.ERROR108", "Tips.ERRORProxy2")
					.replace("Tips.ERROR110", "Tips.ERRORProxy3")
					.replace("Tips.ERROR114", "Tips.ERRORProxy4")
					.replace("Tips.ERROR206", "Tips.ERRORProxy5")
					.replace("Tips.ERROR211", "Tips.ERRORProxy6")
					.replace("Tips.ERROR212", "Tips.ERRORProxy7")
					.replace("Tips.ERROR213", "Tips.ERRORProxy8")
					.replace("Tips.ERROR214", "Tips.ERRORProxy9")
					.replace("Tips.ERROR200", "Tips.ERRORProxyA")
					.replace("Tips.ERROR210", "Tips.ERRORProxyB"); //先替换成中间变量（唯一）
			line = line
					.replace("Tips.ERRORProxy1", "Tips.ERROR2100")
					.replace("Tips.ERRORProxy2", "Tips.ERROR2200")
					.replace("Tips.ERRORProxy3", "Tips.ERROR2202")
					.replace("Tips.ERRORProxy4", "Tips.ERROR1100")
					.replace("Tips.ERRORProxy5", "Tips.ERROR1200")
					.replace("Tips.ERRORProxy6", "Tips.ERROR2000")
					.replace("Tips.ERRORProxy7", "Tips.ERROR2003")
					.replace("Tips.ERRORProxy8", "Tips.ERROR2300")
					.replace("Tips.ERRORProxy9", "Tips.ERROR2301")
					.replace("Tips.ERRORProxyA", "Tips.ERROR1201")
					.replace("Tips.ERRORProxyB", "Tips.ERROR2001"); //再从中间变量替换成结果
		}
		
		return line;
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
				//common
				line = commonFormat(packName, line);
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
				boolean hasPrimaryKey = false; //判断是否包含@PrimaryKey注解
				boolean hasPrimaryKeyType = false; //判断是否包含@PrimaryKeyType注解
				boolean hasExcel = false; //判断是否包含@Excel注解
				boolean hasDate = false; //判断是否包含时间
				int priCount = 0;
				int it = 0;
				List<Structure> sList = table.getTableInfo();
				for (int m = sList.size() - 1; m >= 0; m--) {
					Structure structure = sList.get(m);
					String cname = structure.getField();
					//转驼峰
					if(config.isCamelCase()) {
						int index = 0;
						while ((index = cname.indexOf("_")) != -1) {
							cname = cname.substring(0, index)+cname.substring(index+1, index+2).toUpperCase()+cname.substring(index+2);
						}
					}
					//解析
					for (int i = 0; i < content.size(); i++) {
						//field
						if("//field".equals(content.get(i).trim())){
							it = i;
							content.add(++it, "	");
							content.add(++it, "	/** "+structure.getComment().replace("\"", "")+" */");
							//主键
							if("PRI".equals(structure.getKey())){
								hasPrimaryKey = true;
							    //判断主键类型
								if("auto_increment".equals(structure.getExtra())) {
									hasPrimaryKeyType = true;
									content.add(++it, "	@PrimaryKey(type=PrimaryKeyType.AUTO_INCREMENT)");
								}else if(structure.getComment().toLowerCase().contains("uuid")){
									hasPrimaryKeyType = true;
									content.add(++it, "	@PrimaryKey(type=PrimaryKeyType.UUID)");
								}else if(priCount > 1){
									hasPrimaryKeyType = true;
									content.add(++it, "	@PrimaryKey(type=PrimaryKeyType.GROUP)");
								}else{
									content.add(++it, "	@PrimaryKey");
								}
								priCount++;
							}
							content.add(++it, "	@Column(\""+structure.getField()+"\")");
							//@Excel
							if(config.isExcel()) {
								hasExcel = true;
								content.add(++it, "	@Excel(name=\""+structure.getComment().replace("\"", "")
										+"\", sort="+m
										+ (Tools.isNullOrEmpty(structure.getDefaultValue()) ? "" : ", value=\""+structure.getDefaultValue()+"\"")
										+ ", length=\""+ structure.getLength()+ "\""
										+ ", isNull="+ ("YES".equalsIgnoreCase(structure.getIsNull()) ? 1 : 0) +" )");
							}
							//时间
							if("Timestamp".equals(structure.getType())) {
								hasDate = true;
								content.add(++it, "	@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")");
							}
							content.add(++it, "	private "+structure.getType()+" "+cname+";");

						//get set
						}else if("//get set".equals(content.get(i).trim())){
							it = i;
							content.add(++it, "	public "+structure.getType()+" get"+(cname.substring(0,1).toUpperCase() + cname.substring(1))+"() {");
							content.add(++it, "		return "+cname+";");
							content.add(++it, "	}");
							
							content.add(++it, "	public void set"+(cname.substring(0,1).toUpperCase() + cname.substring(1))+"("+structure.getType()+" "+cname+") {");
							content.add(++it, "		this."+cname+" = "+cname+";");
							content.add(++it, "	}");
						}
					}
				}
				//修正主键类型
				if(priCount > 1){
					for (int i = 0; i < content.size(); i++) {
						if("@PrimaryKey".equals(content.get(i).trim())) {
							hasPrimaryKeyType = true;
							content.set(i, "	@PrimaryKey(type=PrimaryKeyType.GROUP)");
						}
					}
				}
				//去除多余的标记、import
				for (int i = 0; i < content.size(); i++) {
					if("//field".equals(content.get(i).trim())){
						content.remove(i);
					}
					if("//get set".equals(content.get(i).trim())){
						content.remove(i);
					}
					if(!hasPrimaryKey) {
						if("import com.jian.annotation.PrimaryKey;".equals(content.get(i).trim())) {
							content.remove(i);
						}
					}
					if(!hasPrimaryKeyType) {
						if("import com.jian.annotation.PrimaryKeyType;".equals(content.get(i).trim())) {
							content.remove(i);
						}
					}
					if(!hasExcel) {
						if("import com.jian.annotation.Excel;".equals(content.get(i).trim())) {
							content.remove(i);
						}
					}
					if(!hasDate) {
						if("import java.sql.Timestamp;".equals(content.get(i).trim())) {
							content.remove(i);
						}
						if("import com.fasterxml.jackson.annotation.JsonFormat;".equals(content.get(i).trim())) {
							content.remove(i);
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
				//common
				line = commonFormat(packName, line);
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
				//common
				line = commonFormat(packName, line);
				//import
				String dn = fileName.replaceAll("Impl$", "").replaceAll("Service$", "Dao"); //dao name
				String sn = fileName.replaceAll("Impl$", ""); //service name
				String en = fileName.replaceAll("Impl$", "").replaceAll("Service$", ""); //entity name
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
				//common
				line = commonFormat(packName, line);
				//import
				String en = fileName.replaceAll("Controller$", ""); //entity name
				if(line.indexOf("import T;") != -1){
					line = "import " + packName.replace("controller", "entity."+en) + ";"; //import xxxx.entity.xxx
				}else if(line.indexOf("import Service;") != -1){
					line = "import " + packName.replace("controller", "service."+en+"Service") + ";"; //import xxxx.service.xxxService
				}else if(line.indexOf("import ServiceImpl;") != -1){
					line = "import " + packName.replace("controller", "service.impl."+en+"ServiceImpl") + ";"; //import xxxx.service.impl.xxxServiceImpl
				}else if(line.indexOf("import VerifyConfig;") != -1){
					line = "import " + packName.replace("controller", "config.VerifyConfig") + ";";//import xxxx.config.VerifyConfig
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
				//api
				if(line.indexOf("import com.jian.annotation.API;") != -1 && !config.isApi()){
					continue;
				}
				if(line.indexOf("@API") != -1 && !config.isApi()){
					continue;
				}
				content.add(line);
			}
			//api
			if(config.isApi()) {
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
				String codeDesc = "错误码，大于0表示成功，小于 0 表示失败。[错误码说明](#错误码说明)";
				String msgDesc = "错误消息 ，code 小于 0 时的具体错误信息。";
				/**
				 * 
				 * @param req
				 * <p>请求参数说明：参数	必选	类型	描述
				 * <pre>
				 * </pre>
				 * @return resp 
				 * <p>响应示例：
				 * <pre type="template">
				 * {
				 	"code": 1,
				    "msg": "成功",
				    "data": 1
				   }
				 * </pre>
				 * <p>参数说明：参数	描述
				 * <pre>
				 * </pre>
				 */
				for (int i = 0; i < content.size(); i++) {
					if("//add request".equals(content.get(i).trim())){
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
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
									content.add(i+4+count, "	 * "+structure.getField()+"	"+"是"+"	"+structure.getType()+"	"+structure.getComment());
								}
							}else{
								count++;
								content.add(i+4+count, "	 * "+structure.getField()+"	"+("YES".equalsIgnoreCase(structure.getIsNull()) ? "否" : "是")+"	"+structure.getType()+"	"+structure.getComment());
							}
						}
						content.add(i+4+count+1, "	 * </pre>");
						content.add(i+4+count+2, "	 * @return resp");
						content.add(i+4+count+3, "	 * <p>响应示例：");
						content.add(i+4+count+4, "	 * <pre type=\"template\">");
						content.add(i+4+count+5, "	 * {");
						content.add(i+4+count+6, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.DATA+"\": 1");
						content.add(i+4+count+9, "	 * }");
						content.add(i+4+count+10, "	 * </pre>");
						content.add(i+4+count+11, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+12, "	 * <pre>");
						content.add(i+4+count+13, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+14, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+15, "	 * "+ResultKey.DATA+"	"+"自增id或受影响条数。");
						content.add(i+4+count+16, "	 * </pre>");
						content.add(i+4+count+17, "	 */");
						
					}else if("//modify request".equals(content.get(i).trim())){
						//主键必填，其他选填
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
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
								content.add(i+4+count, "	 * "+structure.getField()+"	"+"是"+"	"+structure.getType()+"	"+structure.getComment());
							}else{
								count++;
								content.add(i+4+count, "	 * "+structure.getField()+"	"+"否"+"	"+structure.getType()+"	"+structure.getComment());
							}
						}
						content.add(i+4+count+1, "	 * </pre>");
						content.add(i+4+count+2, "	 * @return resp");
						content.add(i+4+count+3, "	 * <p>响应示例：");
						content.add(i+4+count+4, "	 * <pre type=\"template\">");
						content.add(i+4+count+5, "	 * {");
						content.add(i+4+count+6, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.DATA+"\": 1");
						content.add(i+4+count+9, "	 * }");
						content.add(i+4+count+10, "	 * </pre>");
						content.add(i+4+count+11, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+12, "	 * <pre>");
						content.add(i+4+count+13, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+14, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+15, "	 * "+ResultKey.DATA+"	"+"受影响条数。");
						content.add(i+4+count+16, "	 * </pre>");
						content.add(i+4+count+17, "	 */");
					}else if("//delete request".equals(content.get(i).trim())){
						//主键必填
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							//@PrimaryKey
							if("PRI".equals(structure.getKey())){
								count++;
								content.add(i+4+count, "	 * "+structure.getField()+"	"+"是"+"	"+structure.getType()+"	"+structure.getComment());
							}
						}
						content.add(i+4+count+1, "	 * </pre>");
						content.add(i+4+count+2, "	 * @return resp");
						content.add(i+4+count+3, "	 * <p>响应示例：");
						content.add(i+4+count+4, "	 * <pre type=\"template\">");
						content.add(i+4+count+5, "	 * {");
						content.add(i+4+count+6, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.DATA+"\": 1");
						content.add(i+4+count+9, "	 * }");
						content.add(i+4+count+10, "	 * </pre>");
						content.add(i+4+count+11, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+12, "	 * <pre>");
						content.add(i+4+count+13, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+14, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+15, "	 * "+ResultKey.DATA+"	"+"受影响条数。");
						content.add(i+4+count+16, "	 * </pre>");
						content.add(i+4+count+17, "	 */");
						
					}else if("//deleteBy request".equals(content.get(i).trim())){
						//条件不可同时为空
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count, "	 * "+structure.getField()+"	"+"否"+"	"+structure.getType()+"	"+structure.getComment());
						}
						content.add(i+4+count+1, "	 * </pre>");
						count++;
						content.add(i+4+count+1, "	 * <b style=\"color:#004b91;\">注意：</b>以上参数不可同时为空。");
						content.add(i+4+count+2, "	 * @return resp");
						content.add(i+4+count+3, "	 * <p>响应示例：");
						content.add(i+4+count+4, "	 * <pre type=\"template\">");
						content.add(i+4+count+5, "	 * {");
						content.add(i+4+count+6, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.DATA+"\": 1");
						content.add(i+4+count+9, "	 * }");
						content.add(i+4+count+10, "	 * </pre>");
						content.add(i+4+count+11, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+12, "	 * <pre>");
						content.add(i+4+count+13, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+14, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+15, "	 * "+ResultKey.DATA+"	"+"受影响条数。");
						content.add(i+4+count+16, "	 * </pre>");
						content.add(i+4+count+17, "	 */");
						
					}else if("//findPage request".equals(content.get(i).trim()) ){
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						content.add(i+5, "	 * page	是	int	页码，从1开始。");
						content.add(i+6, "	 * rows	是	int	每页条数。");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+6+count, "	 * "+structure.getField()+"	"+"否"+"	"+structure.getType()+"	"+structure.getComment());
						}
						content.add(i+6+count+1, "	 * </pre>");
						content.add(i+6+count+2, "	 * @return resp");
						content.add(i+6+count+3, "	 * <p>响应示例：");
						content.add(i+6+count+4, "	 * <pre type=\"template\">");
						content.add(i+6+count+5, "	 * {");
						content.add(i+6+count+6, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+6+count+7, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+6+count+8, "	 * 	\""+ResultKey.TOTAL+"\": 0,");
						content.add(i+6+count+9, "	 * 	\""+ResultKey.DATA+"\": [{");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+6+count+9, "	 * 		\""+structure.getField()+"\":\"\",");
						}
						
						content.add(i+6+count+10, "	 * 	},...]");
						content.add(i+6+count+11, "	 * }");
						content.add(i+6+count+12, "	 * </pre>");
						content.add(i+6+count+13, "	 * <p>参数说明：参数	描述");
						content.add(i+6+count+14, "	 * <pre>");
						content.add(i+6+count+15, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+6+count+16, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+6+count+17, "	 * "+ResultKey.TOTAL+"	"+"总条数");
						content.add(i+6+count+18, "	 * "+ResultKey.DATA+"	"+"数据集。");
						content.add(i+6+count+19, "	 * 数据字段");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+6+count+19, "	 * "+structure.getField()+"	"+structure.getComment());
						}
						
						content.add(i+6+count+20, "	 * </pre>");
						content.add(i+6+count+21, "	 */");
						
					}else if("//findAll request".equals(content.get(i).trim()) ){
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						content.add(i+5, "	 * </pre>");
						content.add(i+6, "	 * @return resp");
						content.add(i+7, "	 * <p>响应示例：");
						content.add(i+8, "	 * <pre type=\"template\">");
						content.add(i+9, "	 * {");
						content.add(i+10, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+11, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+12, "	 * 	\""+ResultKey.DATA+"\": [{");
						
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+12+count, "	 * 		\""+structure.getField()+"\":\"\",");
						}
						
						content.add(i+12+count+1, "	 * 	},...]");
						content.add(i+12+count+2, "	 * }");
						content.add(i+12+count+3, "	 * </pre>");
						content.add(i+12+count+4, "	 * <p>参数说明：参数	描述");
						content.add(i+12+count+5, "	 * <pre>");
						content.add(i+12+count+6, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+12+count+7, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+12+count+8, "	 * "+ResultKey.DATA+"	"+"数据集。");
						content.add(i+12+count+9, "	 * 数据字段");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+12+count+9, "	 * "+structure.getField()+"	"+structure.getComment());
						}
						
						content.add(i+12+count+10, "	 * </pre>");
						content.add(i+12+count+11, "	 */");
						
					}else if("//findOne request".equals(content.get(i).trim()) ){
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count, "	 * "+structure.getField()+"	"+"否"+"	"+structure.getType()+"	"+structure.getComment());
						}
						content.add(i+4+count+1, "	 * </pre>");
						content.add(i+4+count+2, "	 * <b style=\"color:#004b91;\">注意：</b>以上参数不可同时为空。");
						content.add(i+4+count+3, "	 * @return resp");
						content.add(i+4+count+4, "	 * <p>响应示例：");
						content.add(i+4+count+5, "	 * <pre type=\"template\">");
						content.add(i+4+count+6, "	 * {");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+9, "	 * 	\""+ResultKey.DATA+"\": {");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count+9, "	 * 		\""+structure.getField()+"\":\"\",");
						}
						
						content.add(i+4+count+10, "	 * 	}");
						content.add(i+4+count+11, "	 * }");
						content.add(i+4+count+12, "	 * </pre>");
						content.add(i+4+count+13, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+14, "	 * <pre>");
						content.add(i+4+count+15, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+16, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+17, "	 * "+ResultKey.DATA+"	"+"数据。");
						content.add(i+4+count+18, "	 * 数据字段");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count+18, "	 * "+structure.getField()+"	"+structure.getComment());
						}
						
						content.add(i+4+count+19, "	 * </pre>");
						content.add(i+4+count+20, "	 */");
						
					}else if("//findList request".equals(content.get(i).trim()) ){
						
						content.set(i, "	/**");
						content.add(i+1, "	 * ");
						content.add(i+2, "	 * @param req");
						content.add(i+3, "	 * <p>请求参数说明：参数	必选	类型	描述");
						content.add(i+4, "	 * <pre>");
						List<Structure> sList = table.getTableInfo();
						int count = 0;
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count, "	 * "+structure.getField()+"	"+"否"+"	"+structure.getType()+"	"+structure.getComment());
						}
						content.add(i+4+count+1, "	 * </pre>");
						content.add(i+4+count+2, "	 * <b style=\"color:#004b91;\">注意：</b>以上参数不可同时为空。");
						content.add(i+4+count+3, "	 * @return resp");
						content.add(i+4+count+4, "	 * <p>响应示例：");
						content.add(i+4+count+5, "	 * <pre type=\"template\">");
						content.add(i+4+count+6, "	 * {");
						content.add(i+4+count+7, "	 * 	\""+ResultKey.CODE+"\": 1,");
						content.add(i+4+count+8, "	 * 	\""+ResultKey.MSG+"\": \"成功\",");
						content.add(i+4+count+9, "	 * 	\""+ResultKey.DATA+"\": [{");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count+9, "	 * 		\""+structure.getField()+"\":\"\",");
						}
						
						content.add(i+4+count+10, "	 * 	},...]");
						content.add(i+4+count+11, "	 * }");
						content.add(i+4+count+12, "	 * </pre>");
						content.add(i+4+count+13, "	 * <p>参数说明：参数	描述");
						content.add(i+4+count+14, "	 * <pre>");
						content.add(i+4+count+15, "	 * "+ResultKey.CODE+"	"+codeDesc);
						content.add(i+4+count+16, "	 * "+ResultKey.MSG+"	"+msgDesc);
						content.add(i+4+count+17, "	 * "+ResultKey.DATA+"	"+"数据集。");
						content.add(i+4+count+18, "	 * 数据字段");
						
						for (int m = 0; m < sList.size(); m++) {
							Structure structure = sList.get(m);
							count++;
							content.add(i+4+count+18, "	 * "+structure.getField()+"	"+structure.getComment());
						}
						
						content.add(i+4+count+19, "	 * </pre>");
						content.add(i+4+count+20, "	 */");
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
				//common
				line = commonFormat(packName, line);
				
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
				//common
				line = commonFormat(packName, line);
				
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
				//common
				line = commonFormat(config.getBasePackge(), line);
				
				if(line.indexOf("{basePackage}") != -1){
					line = line.replace("{basePackage}", config.getBasePackge());
				}
				//api
				if(line.indexOf("//API") != -1) {
					if(!config.isApi()) {
						line = "";
					}
				}
				if(line.indexOf("Doc.api();") != -1) {
					if(!config.isApi()) {
						line = "";
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
				//common
				line = commonFormat(packName, line);
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
	
	private void doCreateAspect(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create aspect file... " +packName+" "+fileName);
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
				//common
				line = commonFormat(packName, line);

				//Controller
				if(line.indexOf("{Controller}") != -1){
					line = line.replace("{Controller}", config.getControllerPath());
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
		System.out.println(DateTools.formatDate()+":	end create aspect file... " +packName+" "+fileName);
	}
	
	private void doCreateException(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create exception file... " +packName+" "+fileName);
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
				//common
				line = commonFormat(packName, line);
				
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
		System.out.println(DateTools.formatDate()+":	end create exception file... " +packName+" "+fileName);
	}
	
	private void doCreateResult(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create exception file... " +packName+" "+fileName);
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
				//common
				line = commonFormat(packName, line);
				
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
		System.out.println(DateTools.formatDate()+":	end create exception file... " +packName+" "+fileName);
	}
	
	private void doCreateHandle(String packName, String tempName, String fileName, String chartset){
		System.out.println(DateTools.formatDate()+":	start create exception file... " +packName+" "+fileName);
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
				//common
				line = commonFormat(packName, line);
				
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
		System.out.println(DateTools.formatDate()+":	end create exception file... " +packName+" "+fileName);
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
		AutoCreateSpringBootMybatis test =  new AutoCreateSpringBootMybatis(new Config("com.testAuto"), new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator));
		test.start("s_app");
	}
	
}
