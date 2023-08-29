package daos;

public class RouteWorker implements Comparable<RouteWorker>{

	String id;
	String planningId;
	String routeId;
	Long start;
	Long end;
	int variableCost;
	int fixedCost;
	int totalCost;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanningId() {
		return planningId;
	}
	public void setPlanningId(String planningId) {
		this.planningId = planningId;
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
	public int getVariableCost() {
		return variableCost;
	}
	public void setVariableCost(int variableCost) {
		this.variableCost = variableCost;
	}
	public int getFixedCost() {
		return fixedCost;
	}
	public void setFixedCost(int fixedCost) {
		this.fixedCost = fixedCost;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(int duration) {
		this.totalCost = fixedCost + (variableCost * duration);
	}
	
	@Override
	public int compareTo(RouteWorker worker) {
		return totalCost - worker.totalCost;
	}
	
}
