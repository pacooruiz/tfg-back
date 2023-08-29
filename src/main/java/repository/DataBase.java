package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Singleton;

@Singleton
public class DataBase {
	
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:postgresql://localhost:5432/tfg";
		String username = "postgres";
		String password = "postgres";
		
		return DriverManager.getConnection(url, username, password);
	}

}
