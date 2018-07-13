Java开发工具包
============

### 常用工具类
统一在con.jian.tools.core包里。

#### Tools类
* 基础数据类型转换。
* md5、base64、unicode、Properties、随机字符串等。
* requset相关。
* 反射工具。

#### 日期工具类 DateTools
基于SimpleDateFormat的再封装。
用法：
```java
	DateTools.formatDate();
```

#### Http工具类 HttpTools
基于HttpClient 4。
用法：
```java
	String str = HttpTools.getInstance().sendHttpGet(url);
	System.out.println(str);
```

#### Map工具类 MapTools
用法：
```java
	Map<String, Object> map = MapTools.custom().put("test1", "test1").put("test2", "test2").put("test3", "test3").put("test1", "test4").build();
	Map<String, Object> map2 = MapTools.custom().build();
```
