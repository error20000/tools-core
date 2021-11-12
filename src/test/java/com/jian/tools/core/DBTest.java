package com.jian.tools.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jian.tools.core.Tools;
import com.testAuto.App;
import com.testAuto.entity.UserGroupCopy1;
import com.testAuto.service.UserGroupCopy1Service;
import com.testAuto.util.Utils;


@RunWith(SpringRunner.class)   
@SpringBootTest(classes = App.class)
public class DBTest {
	
	@Autowired
	private UserGroupCopy1Service service;
	
	@Autowired
	private com.testAuto.util.Test defs;
	
	@Autowired
	private com.testAuto.util.Test test;

    @Test
    public void testBean(){
		defs.test1();
		test.test1();

    }

    @Test
    public void testUtils(){
		System.out.println(JsonTools.toJsonString(Utils.getColumns(UserGroupCopy1.class)));
    }

    @Test
    public void testAddBatch() throws IOException, ClassNotFoundException{
    	long start = System.currentTimeMillis();
    	List<UserGroupCopy1> list = new ArrayList<UserGroupCopy1>();
    	UserGroupCopy1 user = null;
    	for (int i = 0; i < 5000; i++) {
    		user = new UserGroupCopy1();
        	user.setPid(Utils.newSnowflakeIdStr());
        	user.setUserId("test_" + i);
        	list.add(user);
		}
    	System.out.println("list time: " + (System.currentTimeMillis() - start) );

    	long start2 = System.currentTimeMillis();
		for (UserGroupCopy1 userGroupCopy1 : list) {
	    	service.add(userGroupCopy1);
		}
    	System.out.println("testAddBatch time: " + (System.currentTimeMillis() - start2) );
    	
//    	service.add(list);
//    	System.out.println("testAddBatch list time: " + (System.currentTimeMillis() - start2) );
    }

    @Test
    public void testAdd() throws IOException, ClassNotFoundException{
    	UserGroupCopy1 user = new UserGroupCopy1();
    	user.setPid(Utils.newSnowflakeIdStr());
    	user.setUserId("test");
//    	user.setGroupId("test2");
//    	user.setGroupid("test3");
    	service.add(user);
		System.out.println(" >>>>>>>>>>> " + JsonTools.toJsonString(user));

		Map<String, Object> map = Utils.objectToMap(user);
		for (String str : map.keySet()) {
			System.out.println(str + " --------- " + map.get(str));
		}
		
//		UserGroupCopy1 user2 = new UserGroupCopy1();
//		Utils.mapToObject(map, user2);

		UserGroupCopy1 user2 = service.findOne(MapTools.custom().put("pid", user.getPid()).build());
		System.out.println(" --------- " + user2.getPid());
		System.out.println(" --------- " + user2.getUserId());
		System.out.println(" --------- " + user2.getGroupId());
		System.out.println(" --------- " + user2.getGroupid());
		System.out.println(" --------- " + user2.getTestId());
		System.out.println(JsonTools.toJsonString(user2));
    }
    
    @Test
    public void testUpdate() throws IOException, ClassNotFoundException{
    	UserGroupCopy1 user = new UserGroupCopy1();
    	user.setPid("888851779389751296");
    	user.setUserId("test11");
    	user.setTestId(69);
    	user.setGroupid("");
//    	service.modify(user);
    	
		UserGroupCopy1 user2 = service.findOne(MapTools.custom().put("pid", user.getPid()).build());
		System.out.println(JsonTools.toJsonString(user2));
    }


    @Test
    public void testAddList() throws IOException, ClassNotFoundException{
    	List<UserGroupCopy1> list = new ArrayList<UserGroupCopy1>();
    	UserGroupCopy1 user = new UserGroupCopy1();
    	user.setPid(Utils.newSnowflakeIdStr());
    	user.setUserId("test");
    	user.setGroupId("test2");
    	user.setGroupid("test3");
    	list.add(user);
    	UserGroupCopy1 user2 = new UserGroupCopy1();
    	user2.setPid(Utils.newSnowflakeIdStr());
    	user2.setUserId("test2");
    	user2.setGroupId("test22");
    	user2.setGroupid("test23");
    	list.add(user2);
    	int res = service.add(list);
    	System.out.println(res);
    }
    
    @Test
    public void testUpdateList() throws IOException, ClassNotFoundException{
    	List<UserGroupCopy1> list = new ArrayList<UserGroupCopy1>();
    	UserGroupCopy1 user = new UserGroupCopy1();
    	user.setPid("888814101243887616");
    	user.setTestId(67);
    	user.setGroupid("");
    	list.add(user);
    	UserGroupCopy1 user2 = new UserGroupCopy1();
    	user2.setPid("888814101243887617");
    	user2.setTestId(68);
    	user2.setGroupid("");
    	list.add(user2);
    	int res = service.modify(list);
    	System.out.println(res);
    }
    
}
