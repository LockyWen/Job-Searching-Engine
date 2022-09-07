package db;

public class MySQLInitialize {
	// Firstly create an account in AWS, then create your Security group and make sure it opens to all connect request
	// Then create a database in RDS, making sure that its master username is "admin" and be public. 
	// Then replace INSTANCE, DB_NAME and PASSWORD with your personal information. 
	private static final String INSTANCE = "doordash-instance.cgrnwubmysd5.us-east-2.rds.amazonaws.com";
	private static final String PORT_NUM = "3306";
	private static final String DB_NAME = "doordash";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "19981001locky";
	
	public static final String URL = "jdbc:mysql://" + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD + "&autoReconnect=true&serverTimezone=UTC";
}
