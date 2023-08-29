package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dtos.request.Vehicle;
import dtos.response.Depot;
import dtos.response.GetVehicle;

public class VehiclesJDBC {
	
	public static List<GetVehicle> getAllVehicles() throws SQLException{
		
		List<GetVehicle> vehicles = new ArrayList<>();
		
		String query = "SELECT * FROM vehicles v, depots d WHERE depot_id = v.depot AND v.status = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, "A");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				GetVehicle vehicle = new GetVehicle();
				vehicle.setId(rs.getString("id"));
				vehicle.setSeats(rs.getInt("seats"));
				vehicle.setStatus(rs.getBoolean("active") ? "a" : "b");
				vehicle.setMaxLoad(rs.getInt("load_capacity"));
				vehicle.setFixedCost(rs.getInt("fixed_cost"));
				vehicle.setVariableCost(rs.getInt("variable_cost"));
				Depot depot = new Depot();
				depot.setId(rs.getString("depot_id"));
				depot.setName(rs.getString("name"));
				depot.setLatitude(rs.getString("lat"));
				depot.setLongitude(rs.getString("lng"));
				vehicle.setDepot(depot);
				vehicles.add(vehicle);
			}
		}
		return vehicles;
	}
	
	public static void addVehicle(Vehicle vehicle) throws SQLException {
		String query = "INSERT INTO vehicles (id, load_capacity, seats, fixed_cost, variable_cost, depot) VALUES (?, ?, ?, ?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, vehicle.getId());
			ps.setInt(2, vehicle.getMaxLoad());
			ps.setInt(3, vehicle.getSeats());
			ps.setInt(4, vehicle.getFixedCost());
			ps.setInt(5, vehicle.getVariableCost());
			ps.setString(6, vehicle.getDepot().getId());
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void deleteVehicle(String id) throws SQLException {
		String query = "UPDATE vehicles SET status = ? WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, "I");
			ps.setString(2, id);
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void updateVehicle(Vehicle vehicle) throws SQLException {
		String query = "UPDATE vehicles SET load_capacity = ?, seats = ?, fixed_cost = ?, variable_cost = ?, depot = ?, active = ? WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setInt(1, vehicle.getMaxLoad());
			ps.setInt(2, vehicle.getSeats());
			ps.setInt(3, vehicle.getFixedCost());
			ps.setInt(4, vehicle.getVariableCost());
			ps.setString(5, vehicle.getDepot().getId());
			ps.setBoolean(6, vehicle.getActive());
			ps.setString(7, vehicle.getId());
			System.out.println(ps.toString());
			ps.execute();
		}
	}

}
