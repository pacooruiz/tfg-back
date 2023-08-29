package dtos.request;

public class Job {
	
	private String id;
	
	private String title;
	
	private Location location;
	
	private int workersNeeded;
	
	private int loadNeeded;
	
	private Long duration;

	private Long earliestStartDateTime;

	private Long lastestEndDateTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getLoadNeeded() {
		return loadNeeded;
	}

	public void setLoadNeeded(int loadNeeded) {
		this.loadNeeded = loadNeeded;
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
	
}
