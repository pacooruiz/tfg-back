package daos.detail;

public class RouteJobDetail {
	
	String id;
	String routeId;
	String title;
	Long start;
	Long end;
	Long earliest;
	Long lastest;
	Long duration;
	int loadNeeded;
	int workersNeeded;
	RouteLocation location;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getEnd() {
		return end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	public Long getEarliest() {
		return earliest;
	}
	public void setEarliest(Long earliest) {
		this.earliest = earliest;
	}
	public Long getLastest() {
		return lastest;
	}
	public void setLastest(Long lastest) {
		this.lastest = lastest;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public int getLoadNeeded() {
		return loadNeeded;
	}
	public void setLoadNeeded(int loadNeeded) {
		this.loadNeeded = loadNeeded;
	}
	public int getWorkersNeeded() {
		return workersNeeded;
	}
	public void setWorkersNeeded(int workersNeeded) {
		this.workersNeeded = workersNeeded;
	}
	public RouteLocation getLocation() {
		return location;
	}
	public void setLocation(RouteLocation location) {
		this.location = location;
	}

}
