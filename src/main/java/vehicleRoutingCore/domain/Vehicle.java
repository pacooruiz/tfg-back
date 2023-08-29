package vehicleRoutingCore.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

@PlanningEntity
public class Vehicle {

	@PlanningId
	private String id;
	
	private Long day;

	private int seats;
	
	private int capacity;

	private Location depot;
	
	private int fixedCost;
	
	private int variableCost;

	@PlanningListVariable
	private List<Entity> entities = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public Location getDepot() {
		return depot;
	}

	public void setDepot(Location depot) {
		this.depot = depot;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
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

	public Boolean hasJobs() {
		return (entities.stream().filter(e -> e.getType().equals("job")).count() > 0);
	}
	
	public Boolean hasWorkers() {
		return (entities.stream().filter(e -> e.getType().equals("worker")).count() > 0);
	}
	
	public Stream<Entity> getWorkers(){
		return entities.stream().filter(e -> e.getType().equals("worker"));
	}
	
	public Stream<Entity> getJobs(){
		return entities.stream().filter(e -> e.getType().equals("job"));
	}
	
	public int getNeededCapacity() {
		return getJobs().map(Entity::getLoadNeeded).collect(Collectors.summingInt(Integer::intValue));
	}
	
	public Boolean allWorkersHasSameSchedule() {

		if(hasWorkers() && hasJobs()) {
			
			if(getWorkers().map(Entity::getWorkerStartDateTime).distinct().count() == 1 && getWorkers().map(Entity::getWorkerEndDateTime).distinct().count() == 1) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	public Boolean thereAreEnoughWorkers() {
		
		if(hasJobs() && hasWorkers()) {
			
			int max = getJobs().map(Entity::getWorkersNeeded).max(Integer::compare).get();
			if(max > getWorkers().count()) {
				return false;
			}
			
		}
		return true;
	}
	
	public int getTotalDistanceInKm() {
		
		int total = 0;
		
		for(Entity job : getJobs().collect(Collectors.toList())) {
			
			Boolean firstJob = job.isFirstJob();
			Boolean lastJob = job.isLastJob();
			
			if(firstJob) {
				total = total + (int) (job.getFirstJobDistanceFromDepot() / 1000);
			}
			if(lastJob) {
				total = total + (int) (job.getDepotDistanceFromLastJob() / 1000);
			}
			if(!lastJob && !firstJob) {
				total = total + (int) (job.getDistanceToNextJob() / 1000);
			}
			
		}
		
		return total;
		
	}
	
	public Boolean hasDuplicatedWorkers() {
		return (getWorkers().map(Entity::getId).map(w -> w.split("_")[0]).collect(Collectors.toSet()).size() > getWorkers().count() );
	}

	public Long arrivalToDepot() {
		Entity lastJob = getJobs().filter(e -> e.isLastJob()).collect(Collectors.toList()).get(0);
		return lastJob.getDepotArrivalTimeFromLastJob();
	}
	
	public Long getWorkersEndDateTime() {
		return getWorkers().collect(Collectors.toList()).get(0).getWorkersEndDateTime();
	}
	
}
