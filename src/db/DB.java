package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class DB {
	private static Connection conn = null;
	private static Statement stmt = null;
	private static String db = null;
	private static String username = null;
	private static String password = null;
	
	public DB(){
		try {
			File file = new File("C:\\Users\\yura\\db.txt");
			if(!file.exists()) {
				System.err.println("db.txt is not existed");
				return;
			}
			
			BufferedReader inFile = new BufferedReader(new FileReader(file));
			db = inFile.readLine();
			username = inFile.readLine();
			password = inFile.readLine();
			if(db == null || username == null || password == null) {
				System.err.println("username or password is not existed");
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(db, username, password);
			if(conn != null) {
				System.out.println("DB connection success");
			}
			stmt = conn.createStatement();
			
			inFile.close();
		}
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<String[]> getList(String filter, boolean asc) {
		Vector<String[]> items = new Vector<String[]>();
		
		
			try {
			String sql = getListQuery(filter, asc);
			ResultSet result = null;
			
			result = stmt.executeQuery(sql);
			while(result.next()) {
				Item item = parseQueryResult(result);
				items.add(item.getData());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	private String getListQuery(String filter, boolean asc) {
		String sql = "SELECT * FROM gifts ORDER BY";
		
		switch(filter) {
			case "type":
				sql += " type";
				break;
			case "company":
				sql += " company";
				break;
			case "expiredDate":
				sql += " expired_date";
				break;
			case "storedLocation":
				sql += " stored_location";
				break;
			default:
				sql += " expired_date";
		}
		if(asc == true) sql += " ASC;";
		else sql += " DESC;";
		
		return sql;
	}
	
	private Item parseQueryResult (ResultSet result) {
		Item item = null;
		
		try {
			int id = result.getInt(1);
			String type = result.getString(2);
			String company = result.getString(3);
			String name = result.getString(4);
			String expiredDate = result.getString(5).split(" ")[0]; 
			String storedLocation = result.getString(6);
			
			item = new Item(id, type, company, name, expiredDate, storedLocation);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return item;
	}
	
	public boolean insertItem(Item item) {
		try {
			String sql = "INSERT INTO gifts(type, company, name, expired_date, stored_location) VALUES ('";
			String[] data = item.getData();
			
			sql += data[1]+"','";
			sql += data[2]+"','";
			sql += data[3]+"','";
			sql += data[4]+"','";
			sql += data[5]+"');";
			
			int result = stmt.executeUpdate(sql);
			if(result != 1) return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;		
	}
	
	public boolean modifyItem(Item item) {
		try {
			String sql = "UPDATE gifts SET";
			String[] data = item.getData();
			
			sql += " type='"+data[1];
			sql += "', company='"+data[2];
			sql += "', name='"+data[3];
			sql += "', expired_date='"+data[4];
			sql += "', stored_location='"+data[5];
			sql += "' WHERE id="+data[0]+";";
			
			if(stmt.executeUpdate(sql) != 1) return false;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean deleteItem(int id) {
		try {
			String sql = "DELETE FROM gifts WHERE id=";
			sql += id+";";
			
			if(stmt.executeUpdate(sql) != 1) return false;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}