Java开发工具包
============
汇集日常常用工具。

### 常用工具类
统一在con.jian.tools.core包里。
* [基础的](#基本的)
* [特色的](#特色的)
* [自动生成代码](#自动生成代码)

### 基础的
#### Tools类
* 基础数据类型转换。
* md5、base64、unicode、Properties、随机字符串等。
* requset相关。
* 反射工具。

#### 日期工具类 DateTools
* 基于SimpleDateFormat的再封装。
* 用法：
```java
DateTools.formatDate();
```

#### Http工具类 HttpTools
* 基于HttpClient 4。
* 用法：
```java
String str = HttpTools.getInstance().sendHttpGet(url);
System.out.println(str);
```

#### Map工具类 MapTools
* 用法：
```java
Map<String, Object> map = MapTools.custom().put("test1", "test1").put("test2", "test2").put("test1", "test4").build();
Map<String, Object> map2 = MapTools.custom().build();
```

#### 文件工具类 FileTools
* 用法：
```java
FileTools.fileWrite(); //写
FileTools.fileReader(); //读
FileTools.fileWatch(); //检查更新
```

### 特色的

#### Json工具类 JsonTools
JsonTools 基于接口设计，可以实现自己的json实现。
* 接口：JsonInterface。
* 实现：JsonImpl 默认实现，基于jackson。
* 用法：
```java
JsonTools.toJsonString(); //Object转json字符串
JsonTools.jsonToObj(); //json字符串转Object
JsonTools.xmlToObj(); //xml字符串转Object
JsonTools.getIfs(); //返回工具类注册的实现。
```
* **自定义实现** 请实现 JsonInterface 接口。

工具包自动扫描，当前classes包里的类。如果没有类实现JsonInterface，会使用默认实现。如果有取第一个实现。以后可能会支持扫描jar。

#### 缓存工具类 CacheTools
CacheTools 基于接口设计，可以实现自己的缓存实现。比如：redis等。
* 对象：CacheObject 缓存对象。
* 接口：Cache。
* 抽象类：CacheAbstract。默认实现了缓存的自动回收，2小时一次，默认过期时间2小时。
* 实现：CacheImpl 默认实现，基于内存，map结构。
* 用法：
```java
CacheTools.setCacheObj(); //设置缓存。
CacheTools.getCacheObj(); //获取缓存
CacheTools.isTimeout(); //是否超时
CacheTools.clearCacheObj(); //清除缓存
CacheTools.getIfs(); //返回工具类注册的实现。
```
* **自定义实现** 你可以继承 CacheAbstract，这样比较简单。也可以实现 Cache 接口，这样更自由。

工具包自动扫描，当前classes包里的类。如果没有类继承 CacheAbstract或者实现Cache接口，会使用默认实现。如果有取第一个实现。以后可能会支持扫描jar。

#### AccessToken工具类 AccessTokenTools
AccessTokenTools 基于接口设计，可以实现自己的token实现。比如：auth2.0等。
* 对象：AccessToken token对象。
* 接口：AccessTokenInterface。
* 抽象类：AccessTokenAbstract。
* 实现：AccessTokenImpl 默认实现，基于内存，map结构。
* 用法：
```java
AccessTokenTools.createToken(); //创建token
AccessTokenTools.getToken(); //获取token
AccessTokenTools.getValue(); //获取保存的对象
AccessTokenTools.checkToken(); //检测是否有效
AccessTokenTools.clearToken(); //清除token
AccessTokenTools.getIfs(); //返回工具类注册的实现。
```
* **自定义实现** 你可以继承 AccessTokenAbstract，这样比较简单。也可以实现 AccessTokenInterface 接口，这样更自由。

工具包自动扫描，当前classes包里的类。如果没有类继承 AccessTokenAbstract或者实现AccessTokenInterface接口，会使用默认实现。如果有取第一个实现。以后可能会支持扫描jar。

### 自动生成代码
com.jian.auto包。通过读取数据库信息自动生成基础java类。可以生成如下项目：
* [工具包定义的项目](#工具包定义的项目)。
* [spring boot项目](#spring-boot项目)。
**注意：**这里使用c3p0做连接，需要引入其相关包。如果最后不想使用c3p0可以生成代码后，删除相关包。

* 示例：
```java
public class Demo {

	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/auth?characterEncoding=utf8";
		String user = "root";
		String password = "123456";
		String driverClass = "com.mysql.jdbc.Driver";
		String prefix = "s_";
		String separator = "_";
		//包配置
		Config config = new Config("com.testAuto");
		config.setOverWrite(true);
		//数据库配置
		ConfigDB cdb = new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator);
		AutoCreateManager test =  new AutoCreateManager(config, cdb, 1);
		test.start();
//		test.start(tableName);
		
	}
	
}
```
* 生成目录结构：
```
com.testAuto
com.testAuto.config
com.testAuto.controller
com.testAuto.dao
com.testAuto.dao.impl
com.testAuto.entity
com.testAuto.service
com.testAuto.service.impl
com.testAuto.util
```

* 介绍：
	* Config: 包配置。
	* ConfigDB: 数据库配置。
	* AutoCreateManager: 自动生成工具工厂。
	* AutoCreate: 自动生成工具接口。
	* AbstractAutoCreate: 自动生成工具抽象。
	* AutoCreateNormal: 自动生成工具的实现。
	* AutoCreateSpringBoot: 自动生成工具的实现。
	
* 自定义生成工具：继承AbstractAutoCreate或者实现AutoCreate接口。再new AutoCreateManager时注入你的实现即可。
* 注解：
	* API：可以生成接口文档。
	* Excel：用于导出excel。

#### 工具包定义的项目
通过META-INF/services/javax.servlet.ServletContainerInitializer方式启动，从而实现自己的注解机制。仿spring。
**注意:**由于怕不同类型项目之间有影响，所以没添加META-INF/services/javax.servlet.ServletContainerInitializer文件，如要使用该方式请自行添加。
内容：com.tools.web.ServletContainerInitializerImpl

#### spring boot项目
spring boot项目

**推荐使用**。由于后期项目基于spring boot所以推荐使用。

