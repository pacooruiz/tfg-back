package daos;

public class RouteJob {
	String id;
	String planningId;
	String routeId;
	Long start;
	Long end;
	int workersNeeded;
	public String getPlanningId() {
		return planningId;
	}
	public void setPlanningId(String planningId) {
		this.planningId = planningId;
	}
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
	public int getWorkersNeeded() {
		return workersNeeded;
	}
	public void setWorkersNeeded(int workersNeeded) {
		this.workersNeeded = workersNeeded;
	}
	
}
