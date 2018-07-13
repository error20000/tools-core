Java开发工具包
============
汇集日常常用工具。

### 常用工具类
统一在con.jian.tools.core包里。
* 基本的。
* 特色的。

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
* 实现：JsonImpl 默认基于jackson实现。
* 用法：
```java
JsonTools.toJsonString(); //Object转json字符串
JsonTools.jsonToObj(); //json字符串转Object
JsonTools.xmlToObj(); //xml字符串转Object
JsonTools.getIfs(); //返回工具类注册的实现。
```
* **自定义实现** 请实现 JsonInterface 接口。
JsonTools类会自动扫描，当前classes包里的类。如果没有类实现JsonInterface，会使用默认实现。如果有取第一个实现。以后可能会支持扫描jar。