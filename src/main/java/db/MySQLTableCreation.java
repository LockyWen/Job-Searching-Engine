package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLTableCreation {
	
	/**
	 * Test: After you run this main method, go to MySQL and connect to your RDS.
	 * Then print the commend line :
	 * 
	 * use YOUR_DB_NAME;
	 * show tables;
	 * 
	 * To see the data stored in your table 
	 * It should be 
	 * 
	 * 1111 | xxx | John | Smith 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL
			System.out.println("Connecting to " + MySQLInitialize.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(MySQLInitialize.URL);
			
			if(conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS keywords";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS history";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS items";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			//Step 3 Create new tables 
			sql = "CREATE TABLE items ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "address VARCHAR(255),"
					+ "image_url VARCHAR(255),"
					+ "url VARCHAR(255),"
					+ "contents text,"
					+ "PRIMARY KEY (item_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255),"
					+ "last_name VARCHAR(255),"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE keywords ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "keyword VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (item_id, keyword),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE history ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (user_id, item_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			// Step4 : insert fake user 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050','John', 'Smith')";
			statement.executeUpdate(sql);
			
			
			conn.close();
			System.out.println("Import done successfully");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
