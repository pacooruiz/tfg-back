package dtos.response.route;

import java.util.List;

import daos.detail.RouteJobDetail;
import daos.detail.RouteLocation;
import daos.detail.RouteWorkerDetail;

public class Route {
	
	private String vehicleId;
	
	private String date;

	private List<RouteWorkerDetail> workers;
	
	private RouteLocation depot;
	
	private Long workTime;
	
	private Long workStartDateTime;
	
	private Long workEndDateTime;
	
	private List<RouteJobDetail> jobs;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public RouteLocation getDepot() {
		return depot;
	}

	public void setDepot(RouteLocation depot) {
		this.depot = depot;
	}

	public Long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}

	public Long getWorkStartDateTime() {
		return workStartDateTime;
	}

	public void setWorkStartDateTime(Long workStartDateTime) {
		this.workStartDateTime = workStartDateTime;
	}

	public Long getWorkEndDateTime() {
		return workEndDateTime;
	}

	public void setWorkEndDateTime(Long workEndDateTime) {
		this.workEndDateTime = workEndDateTime;
	}

	public List<RouteWorkerDetail> getWorkers() {
		return workers;
	}

	public void setWorkers(List<RouteWorkerDetail> workers) {
		this.workers = workers;
	}

	public List<RouteJobDetail> getJobs() {
		return jobs;
	}

	public void setJobs(List<RouteJobDetail> jobs) {
		this.jobs = jobs;
	}



}
