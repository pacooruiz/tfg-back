package dtos.request;

public class Worker {
	private String id;
	
	private String startTime;
	
	private String endTime;
	
	private int fixedCost;
	
	private int variableCost;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(int fixedCost) {
		this.fixedCost = fixedCost;
	}

	public int getVariableCost() {
		return variableCost;
	}

	public void setVariableCost(int variableCost) {
		this.variableCost = variableCost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
