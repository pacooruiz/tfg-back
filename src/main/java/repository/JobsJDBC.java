package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dtos.response.route.Job;
import dtos.response.route.Location;

public class JobsJDBC {
	
	DepotsJDBC depotsJDBC;
	
	public static List<Job> getAllJobs() throws SQLException{
		List<Job> jobs = new ArrayList<>();
		String query = "SELECT * FROM jobs j, jobs_locations l WHERE j.location = l.location_id AND j.status = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, "A");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Job job = new Job();
				job.setId(rs.getString("id"));
				job.setTitle(rs.getString("title"));
				job.setDuration(rs.getLong("duration"));
				job.setWorkersNeeded(rs.getInt("workers_needed"));
				job.setEarliestStartDateTime(rs.getLong("earliest"));
				job.setLastestEndDateTime(rs.getLong("lastest"));
				job.setDescription(rs.getString("description"));
				job.setLoadNeeded(rs.getInt("load_needed"));
				Location location = new Location();
				location.setId(rs.getString("location_id"));
				location.setLatitude(rs.getString("latitude"));
				location.setLongitude(rs.getString("longitude"));
				job.setLocation(location);
				jobs.add(job);
			}
		}
		return jobs;
	}
	
	public static void deleteJob(String id) throws SQLException {
		String query = "UPDATE jobs SET status = ? WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, "I");
			ps.setString(2, id);
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void addJob(Job job) throws SQLException {
		
		job.getLocation().setId(UUID.randomUUID().toString());
		addLocation(job.getLocation());
		
		String query = "INSERT INTO jobs (id, title, description, workers_needed, load_needed, earliest, lastest, location, status, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, job.getTitle());
			ps.setString(3, job.getDescription());
			ps.setInt(4, job.getWorkersNeeded());
			ps.setInt(5,  job.getLoadNeeded());
			ps.setLong(6, job.getEarliestStartDateTime());
			ps.setLong(7, job.getLastestEndDateTime());
			ps.setString(8, job.getLocation().getId());
			ps.setString(9,  "A");
			ps.setLong(10, job.getDuration());
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void addLocation(Location location) throws SQLException {
		String query = "INSERT INTO jobs_locations (id, lat, lng) VALUES (?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, location.getId());
			ps.setString(2, location.getLatitude());
			ps.setString(3, location.getLongitude());
			System.out.println(ps.toString());
			ps.execute();
		}
	
	}
}
