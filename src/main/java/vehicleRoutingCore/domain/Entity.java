package vehicleRoutingCore.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.core.api.domain.variable.NextElementShadowVariable;
import org.optaplanner.core.api.domain.variable.PreviousElementShadowVariable;

@PlanningEntity
public class Entity {
	
	@PlanningId
	private String id;
	
	private String type;
		
	// WORKER TYPE

	private String name;
	
	private Long workerStartDateTime;
	
	private Long workerEndDateTime;
	
	private int fixedCost;
	
	private int variableCost;
	
	private Long date;
	
	// JOB TYPE
	
	private String title;

	private Location location;

	private int workersNeeded;
	
	private int loadNeeded;

	private Long duration;

	private Long earliestStartDateTime;

	private Long lastestEndDateTime;
	
	// *********************************************************
	// Shadow variables

	@InverseRelationShadowVariable(sourceVariableName = "entities")
	private Vehicle vehicle;

	@PreviousElementShadowVariable(sourceVariableName = "entities")
	private Entity previousEntity;

	@NextElementShadowVariable(sourceVariableName = "entities")
	private Entity nextEntity;

	// *********************************************************
	
	public Boolean isWorker() {
		return (type.equals("worker"));
	}
	
	public Boolean isJob() {
		return (type.equals("job"));
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getWorkerStartDateTime() {
		return workerStartDateTime;
	}

	public void setWorkerStartDateTime(Long startDateTime) {
		this.workerStartDateTime = startDateTime;
	}

	public Long getWorkerEndDateTime() {
		return workerEndDateTime;
	}

	public void setWorkerEndDateTime(Long endDateTime) {
		this.workerEndDateTime = endDateTime;
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

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Entity getPreviousEntity() {
		return previousEntity;
	}

	public void setPreviousEntity(Entity previousEntity) {
		this.previousEntity = previousEntity;
	}

	public Entity getNextEntity() {
		return nextEntity;
	}

	public void setNextEntity(Entity nextEntity) {
		this.nextEntity = nextEntity;
	}
	
	public Long getJobStartDateTime() {
		
		Entity previousJob = getPreviousJob();
		if(previousJob == null) {
			return getWorkersStartDateTime() + vehicle.getDepot().getTimeToOtherLocations().get(this.location.getId());
			
		}
		else {
			return previousJob.getJobEndDateTime() + previousJob.getLocation().getTimeToOtherLocations().get(this.location.getId());
			
		}
		
	}
	
	public Entity getPreviousJob() {
		
		if(previousEntity==null) {
			return null;
		}
		else if(previousEntity.isJob()){
			return previousEntity;
		}
		else {
			return previousEntity.getPreviousJob();
		}
	}
	
	public Entity getNextJob() {
		
		if(nextEntity==null) {
			return null;
		}
		else if(nextEntity.isJob()){
			return nextEntity;
		}
		else {
			return nextEntity.getNextJob();
		}
	}
	
	public Long getJobEndDateTime() {
		return (getJobStartDateTime() + duration);
	}
	
	public Long getDepotArrivalTimeFromLastJob() {
		return (getJobStartDateTime() + duration + location.getTimeToOtherLocations().get(vehicle.getDepot().getId()));
	}
	
	public Long getDepotDistanceFromLastJob() {
		return location.getDistanceToOtherLocations().get(vehicle.getDepot().getId());
	}
	
	public Long getFirstJobDistanceFromDepot() {
		return vehicle.getDepot().getDistanceToOtherLocations().get(location.getId());
	}
	
	public Long getDistanceToNextJob() {
		return location.getDistanceToOtherLocations().get(getNextJob().getLocation().getId());
	}
	
	public Boolean isFirstJob() {
		
		if(isJob() && previousEntity == null) {
			return true;
		}
		else {
			return (isJob() && !areThereJobsAtPreviousEntities());
		}
		
	}
	
	public Boolean areThereJobsAtPreviousEntities() {
		
		if(previousEntity == null) {
			return false;
		}
		else {
			if(previousEntity.isJob()) {
				return true;
			}
			else {
				return previousEntity.areThereJobsAtPreviousEntities();
			}
		}
	}
	
	public Boolean isLastJob() {
		
		if(isJob() && nextEntity == null) {
			return true;
		}
		else {
			return (isJob() && !areThereJobsAtFollowingEntities());
		}
		
	}
	
	public Boolean areThereJobsAtFollowingEntities() {
		
		if(nextEntity == null) {
			return false;
		}
		else {
			if(nextEntity.isJob()) {
				return true;
			}
			else {
				return nextEntity.areThereJobsAtFollowingEntities();
			}
		}
	}
	
	public Long getWorkersStartDateTime() {
		List<Entity> workers = vehicle.getWorkers().collect(Collectors.toList());
		if(workers.size() > 0) {
			return workers.get(0).getWorkerStartDateTime();
		}
		return Long.MIN_VALUE;
	}
	
	public Long getWorkersEndDateTime() {
		List<Entity> workers = vehicle.getWorkers().collect(Collectors.toList());
		if(workers.size() > 0) {
			return workers.get(0).getWorkerEndDateTime();
		}
		return Long.MAX_VALUE;
	}
	
	public Entity getLastJob() {
		return vehicle.getEntities().stream().filter(e -> e.isLastJob()).collect(Collectors.toList()).get(0);
	}
	
	public Long getEndRouteDateTime() {
		
		Entity lastJob = getLastJob();
		return (lastJob.getJobEndDateTime() + lastJob.getLocation().getTimeToOtherLocations().get(this.vehicle.getDepot().getId()));
	}
	
	public Double getWorkerHoursOfWork() {
		return ((getEndRouteDateTime() - getWorkerStartDateTime())/3600000D);
	}
	
	public Long getFinalTimeVariableCost() {
		return Math.abs(Double.valueOf(getWorkerHoursOfWork() * variableCost * 100D).longValue());
	}
}
