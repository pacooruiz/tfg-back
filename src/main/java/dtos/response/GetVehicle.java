package dtos.response;

public class GetVehicle {

	private String id;
	
	private String status;
	
	private Depot depot;
	
	private int seats;
	
	private int maxLoad;
	
	private int fixedCost;
	
	private int variableCost;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
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
	
}

