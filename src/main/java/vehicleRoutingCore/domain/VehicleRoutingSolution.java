package vehicleRoutingCore.domain;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@PlanningSolution
public class VehicleRoutingSolution {

	@ValueRangeProvider
	@ProblemFactCollectionProperty
	private List<Entity> entities = new ArrayList<>();
	
	@PlanningEntityCollectionProperty
	private List<Vehicle> vehicles = new ArrayList<>();
	
	@PlanningScore
	private HardSoftLongScore score;

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public HardSoftLongScore getScore() {
		return score;
	}

	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

}
