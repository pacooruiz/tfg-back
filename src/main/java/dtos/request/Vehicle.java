package dtos.request;

public class Vehicle {
	
	private String id;

	private int seats;
	
	private Location depot;
	
	private int maxLoad;
	
	private int fixedCost;
	
	private int variableCost;
	
	private boolean active;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Location getDepot() {
		return depot;
	}

	public void setDepot(Location depot) {
		this.depot = depot;
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

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
