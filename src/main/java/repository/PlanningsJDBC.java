package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daos.Route;
import daos.RouteJob;
import daos.RouteWorker;
import daos.detail.RouteDetail;
import daos.detail.RouteJobDetail;
import daos.detail.RouteLocation;
import daos.detail.RouteWorkerDetail;
import dtos.response.Planning;

public class PlanningsJDBC {
	
	public static int getNumberForNoNamePlanning() throws SQLException{
		String query = "select count(*) from planning where name like 'Planificación sin título%'";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return (rs.getInt("count") + 1);
			}
		}
		return 0;
	}
	
	public static List<Planning> getAllPlannings() throws SQLException{
		List<Planning> plannings = new ArrayList<>();
		String query = "SELECT * FROM planning";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Planning planning = new Planning();
				planning.setId(rs.getString("id"));
				planning.setTitle(rs.getString("name"));
				planning.setStart(rs.getString("start"));
				planning.setEnd(rs.getString("end"));
				plannings.add(planning);
			}
		}
		return plannings;
	}
	
	public static void addPlanning(daos.Planning planning) throws SQLException {
		String query = "INSERT INTO planning VALUES (?, ?, ?, ?)";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			
			ps.setString(1, planning.getId());
			ps.setString(2, planning.getName());
			ps.setString(3, planning.getStart());
			ps.setString(4, planning.getEnd());
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void deletePlanning(String planningId) throws SQLException {
		String query = "DELETE FROM planning WHERE id = ?; DELETE FROM routes_jobs WHERE planning_id = ?; DELETE FROM routes WHERE planning_id = ?; DELETE FROM routes_workers WHERE planning_id = ?;";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, planningId);
			ps.setString(2, planningId);
			ps.setString(3, planningId);
			ps.setString(4, planningId);
			System.out.println(ps.toString());
			ps.execute();
		}
	}
	
	public static void addRoutes(List<Route> routes) throws SQLException {
		
		String query = "INSERT INTO routes VALUES ";
		
		for(int i=0; i<routes.size(); i++) {
			if(i<routes.size()-1) {
				query = query + "(?, ?, ?, ?, ?, ?, ?), ";
			}
			else {
				query = query + "(?, ?, ?, ?, ?, ?, ?)";
			}
		}
		
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			
			int i=1;
			for(Route route : routes) {
				ps.setString(i++, route.getPlanningId());
				ps.setString(i++, route.getId());
				ps.setString(i++, route.getDate());
				ps.setString(i++, route.getVehicle());
				ps.setString(i++, route.getDepot());
				ps.setLong(i++, route.getStart());
				ps.setLong(i++, route.getEnd());
			}
			System.out.println(ps.toString());
			ps.execute();
		}
			
	}
	
	public static void addWorkers(List<RouteWorker> workers) throws SQLException {
		
		String query = "INSERT INTO routes_workers VALUES ";
		
		for(int i=0; i<workers.size(); i++) {
			if(i<workers.size()-1) {
				query = query + "(?, ?, ?, ?, ?), ";
			}
			else {
				query = query + "(?, ?, ?, ?, ?)";
			}
		}
		
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			
			int i=1;
			for(RouteWorker worker : workers) {
				ps.setString(i++, worker.getPlanningId());
				ps.setString(i++, worker.getRouteId());
				ps.setString(i++, worker.getId().split("_")[0]);
				ps.setLong(i++, worker.getStart());
				ps.setLong(i++, worker.getEnd());
			}
			System.out.println(ps.toString());
			ps.execute();
		}
		
	}
	
	public static void addJobs(List<RouteJob> jobs) throws SQLException {
		
		String query = "INSERT INTO routes_jobs VALUES ";
		
		for(int i=0; i<jobs.size(); i++) {
			if(i<jobs.size()-1) {
				query = query + "(?, ?, ?, ?, ?), ";
			}
			else {
				query = query + "(?, ?, ?, ?, ?)";
			}
		}
		
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			
			int i=1;
			for(RouteJob job : jobs) {
				ps.setString(i++, job.getRouteId());
				ps.setString(i++, job.getId());
				ps.setLong(i++, job.getStart());
				ps.setLong(i++, job.getEnd());
				ps.setString(i++, job.getPlanningId());
			}
			System.out.println(ps.toString());
			ps.execute();
		}
		
	}
	
	public static String getPlanningTitle(String planningId) throws SQLException{
		String query = "SELECT name FROM planning WHERE id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, planningId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("name");
			}
		}
		return null;
	}
	
	public static List<RouteDetail> getRoutes(String planningId) throws SQLException{
		List<RouteDetail> routes = new ArrayList<>();
		String query = "SELECT r.route_id, r.route_date, r.route_vehicle, r.route_start, r.route_end, d.lat, d.lng, d.name, v.load_capacity, v.seats  FROM routes r, vehicles v, depots d WHERE d.depot_id = r.route_depot AND v.id = r.route_vehicle AND planning_id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, planningId);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RouteDetail route = new RouteDetail();
				route.setId(rs.getString("route_id"));
				route.setVehicle(rs.getString("route_vehicle"));
				route.setDate(rs.getString("route_date"));
				route.setStart(rs.getLong("route_start"));
				route.setEnd(rs.getLong("route_end"));
				RouteLocation depot = new RouteLocation();
				depot.setLatitude(rs.getString("lat"));
				depot.setLongitude(rs.getString("lng"));
				depot.setName(rs.getString("name"));
				route.setDepot(depot);
				route.setSeats(rs.getInt("seats"));
				route.setCapacity(rs.getInt("load_capacity"));
				routes.add(route);
			}
		}
		return routes;
	}
	
	public static List<RouteWorkerDetail> getWorkers(String planningId) throws SQLException{
		List<RouteWorkerDetail> workers = new ArrayList<>();
		String query = "select rw.route_id, w.name, rw.worker_id, rw.worker_start, rw.worker_end from routes_workers rw, workers w where  w.id = rw.worker_id ";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RouteWorkerDetail worker = new RouteWorkerDetail();
				worker.setId(rs.getString("worker_id"));
				worker.setRouteId(rs.getString("route_id"));
				worker.setName(rs.getString("name"));
				worker.setStart(rs.getLong("worker_start"));
				worker.setEnd(rs.getLong("worker_end"));
				workers.add(worker);
			}
		}
		return workers;
	}
	
	public static List<RouteJobDetail> getJobs(String planningId) throws SQLException{
		List<RouteJobDetail> jobs = new ArrayList<>();
		String query = "select rj.route_id, j.title, rj.job_id, rj.start, rj.end, j.description, j.load_needed, j.workers_needed, j.duration, j.earliest, j.lastest, l.latitude, l.longitude from routes_jobs rj, jobs j, jobs_locations l where  j.id = rj.job_id and l.location_id = j.location AND planning_id = ?";
		try(Connection connection = DataBase.getConnection(); PreparedStatement ps = connection.prepareStatement(query)){
			ps.setString(1, planningId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RouteJobDetail job = new RouteJobDetail();
				job.setRouteId(rs.getString("route_id"));
				job.setId(rs.getString("job_id"));
				job.setTitle(rs.getString("title"));
				job.setStart(rs.getLong("start"));
				job.setEnd(rs.getLong("end"));
				job.setEarliest(rs.getLong("earliest"));
				job.setLastest(rs.getLong("lastest"));
				job.setDuration(rs.getLong("duration"));
				job.setLoadNeeded(rs.getInt("load_needed"));
				job.setWorkersNeeded(rs.getInt("workers_needed"));
				RouteLocation location = new RouteLocation();
				location.setLatitude(rs.getString("latitude"));
				location.setLongitude(rs.getString("longitude"));
				job.setLocation(location);
				jobs.add(job);
			}
		}
		return jobs;
	}

}
