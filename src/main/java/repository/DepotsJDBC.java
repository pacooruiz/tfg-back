package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dtos.response.Depot;

public class DepotsJDBC {

	public static List<Depot> getAllDepots() throws SQLException{
		List<Depot> depots = new ArrayList<>();
		String query = "SELECT * FROM depots";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Depot depot = new Depot();
				depot.setId(rs.getString("depot_id"));
				depot.setName(rs.getString("name"));
				depot.setLatitude(rs.getString("lat"));
				depot.setLongitude(rs.getString("lng"));
				depots.add(depot);
			}
		}
		return depots;
	}
	
	public static void addDepot(Depot depot) throws SQLException {
		String query = "INSERT INTO depots (depot_id, name, lat, lng) VALUES (?, ?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, depot.getName());
			ps.setString(3, depot.getLatitude());
			ps.setString(4, depot.getLongitude());
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void deleteDepot(String id) throws SQLException {
		String query = "DELETE FROM depots WHERE depot_id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, id);
			System.out.println(ps.toString());
			ps.execute();
		}
	}
}
