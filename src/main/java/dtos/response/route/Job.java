package dtos.response.route;

import java.util.ArrayList;
import java.util.List;

public class Job {
	
	private String id;
	
	private String title;

	private Location location;

	private int workersNeeded;

	private Long duration;

	private Long earliestStartDateTime;

	private Long lastestEndDateTime;
	
	private Long startTime;
	
	private Long endTime;
	
	private String description;
	
	private int loadNeeded;
	
	private List<String> plannings = new ArrayList<>();

	public int getLoadNeeded() {
		return loadNeeded;
	}

	public void setLoadNeeded(int loadNeeded) {
		this.loadNeeded = loadNeeded;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getWorkersNeeded() {
		return workersNeeded;
	}

	public void setWorkersNeeded(int workersNeeded) {
		this.workersNeeded = workersNeeded;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getEarliestStartDateTime() {
		return earliestStartDateTime;
	}

	public void setEarliestStartDateTime(Long earliestStartDateTime) {
		this.earliestStartDateTime = earliestStartDateTime;
	}

	public Long getLastestEndDateTime() {
		return lastestEndDateTime;
	}

	public void setLastestEndDateTime(Long lastestEndDateTime) {
		this.lastestEndDateTime = lastestEndDateTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public List<String> getPlannings() {
		return plannings;
	}

	public void setPlannings(List<String> plannings) {
		this.plannings = plannings;
	}
	
	public void addPlanning(String planning) {
		plannings.add(planning);
	}

}
