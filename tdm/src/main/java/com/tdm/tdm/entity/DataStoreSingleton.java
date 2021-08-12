package com.tdm.tdm.entity;

import org.springframework.stereotype.Component;

@Component
public class DataStoreSingleton {

	private static DataStoreSingleton Dataobj = null;

	public static String dataLoginUserName;
	public static String dataLoginEmailID;

	private DataStoreSingleton() {
	}

	public static synchronized DataStoreSingleton getInstance() {
		if (Dataobj == null) {
			
			throw new RuntimeException("Singleton Object is empty. Please relogin");
		}

		return Dataobj;
	}
	
	public static synchronized void setValue(String UserName, String Email)
	{
		dataLoginUserName =UserName;
		dataLoginEmailID=Email;
	}
}
