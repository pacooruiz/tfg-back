package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dtos.request.Worker;

public class WorkersJDBC {
	
	public static List<Worker> getAllWorkers() throws SQLException{
		
		List<Worker> workers = new ArrayList<>();
		
		String query = "SELECT * FROM workers WHERE status = 'A'";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Worker worker = new Worker();
				worker.setId(rs.getString("id"));
				worker.setName(rs.getString("name"));
				worker.setFixedCost(rs.getInt("fixed_cost"));
				worker.setVariableCost(rs.getInt("variable_cost"));
				worker.setStartTime(rs.getString("start_time"));
				worker.setEndTime(rs.getString("end_time"));
				workers.add(worker);
			}
		}
		
		return workers;
	}

	public static void addWorker(Worker worker) throws SQLException {
		String query = "INSERT INTO workers (id, fixed_cost, variable_cost, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, worker.getId());
			ps.setInt(2, worker.getFixedCost());
			ps.setInt(3, worker.getVariableCost());
			ps.setString(4, worker.getStartTime());
			ps.setString(5, worker.getEndTime());
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void deleteWorker(String id) throws SQLException {
		String query = "UPDATE workers SET status = ? WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, "I");
			ps.setString(2, id);
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void updateWorker(Worker worker) throws SQLException {
		String query = "UPDATE workers SET name = ?, fixed_cost = ?, variable_cost = ?, start_time = ?, end_time = ? WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, worker.getName());
			ps.setInt(2, worker.getFixedCost());
			ps.setInt(3, worker.getVariableCost());
			ps.setString(4, worker.getStartTime());
			ps.setString(5, worker.getEndTime());
			ps.setString(6, worker.getId());
			System.out.println(ps.toString());
			ps.execute();
		}
	}

}
