package vehicleRoutingCore.domain;

import java.util.List;

public class Solution {
	
	private VehicleRoutingSolution vehicleRoutingSolution;
	private List<String> errors;
	
	public Solution(VehicleRoutingSolution vehicleRoutingSolution, List<String> errors) {
		this.vehicleRoutingSolution = vehicleRoutingSolution;
		this.errors = errors;
	}
	
	public VehicleRoutingSolution getVehicleRoutingSolution() {
		return vehicleRoutingSolution;
	}
	public void setVehicleRoutingSolution(VehicleRoutingSolution vehicleRoutingSolution) {
		this.vehicleRoutingSolution = vehicleRoutingSolution;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
