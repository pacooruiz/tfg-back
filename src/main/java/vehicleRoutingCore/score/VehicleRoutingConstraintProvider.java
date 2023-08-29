package vehicleRoutingCore.score;

import java.util.stream.Collectors;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import vehicleRoutingCore.domain.Entity;
import vehicleRoutingCore.domain.Vehicle;


public class VehicleRoutingConstraintProvider implements ConstraintProvider{

	@Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
        		notOnlyOneEntityType(factory),
        		allWorkersInAVehicleHaveSameSchedule(factory),
        		workersNumberNeededIsMoreThanWorkersInVehicle(factory),
        		numberOfWorkersInVehicleIsMoreThanSeats(factory),
        		loadNeededIsMoreThanVehicleCapacity(factory),
        		arrivalBeforeEarliestTime(factory),
        		endAfterLastestEndTime(factory),
        		workTimeIsExceeded(factory),
        		timeVariableCost(factory),
        		timeFixedCost(factory),
        		vehiclesFixedCost(factory),
        		vehiclesVariableCost(factory),
        		minorDaysBest(factory),
        		vehicleHasDuplicatedWorkers(factory)
        };
    }
	
	Constraint notOnlyOneEntityType(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && !v.hasWorkers())
				.penalizeLong(HardSoftLongScore.ONE_HARD, v -> v.getJobs().collect(Collectors.toList()).size())
				.asConstraint("Only one type entities");
	}
	
	Constraint allWorkersInAVehicleHaveSameSchedule(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> !v.allWorkersHasSameSchedule())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("All work mates must start and end at same time");
	}
	
	Constraint workersNumberNeededIsMoreThanWorkersInVehicle(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> !v.thereAreEnoughWorkers())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("The number of workers in the vehicle is not enough to all jobs");
	}
	
	Constraint numberOfWorkersInVehicleIsMoreThanSeats(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && (v.getWorkers().count() > v.getSeats()))		
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("The number of workers can't be more than the vehicle's seats");
	}
	
	Constraint loadNeededIsMoreThanVehicleCapacity(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.getNeededCapacity() > v.getCapacity())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("The sum of all the jobs' load must not be more than the vehicle's max load");
	}
	
	Constraint arrivalBeforeEarliestTime(ConstraintFactory factory) {
		return factory.forEach(Entity.class)
				.filter(e -> e.isJob() && e.getVehicle() != null)
				.filter(e -> e.getJobStartDateTime() < e.getEarliestStartDateTime())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("A vehicle mustn't arrive at a job before it's earliest start date");
	}
	
	Constraint endAfterLastestEndTime(ConstraintFactory factory) {
		return factory.forEach(Entity.class)
				.filter(e -> e.isJob() && e.getVehicle() != null)
				.filter(e -> e.getJobEndDateTime() > e.getLastestEndDateTime())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("A vehicle mustn't end a job after it's lastest end date");
	}
	
	Constraint workTimeIsExceeded(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && v.hasWorkers()) 
				.filter(v -> v.arrivalToDepot() > v.getWorkersEndDateTime())
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("A route mustn't end after the workers' end date");
	}
	
	Constraint vehicleHasDuplicatedWorkers(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && v.hasDuplicatedWorkers()) 
				.penalize(HardSoftLongScore.ONE_HARD)
				.asConstraint("A vehicle can not have the same worker more than once");
	}
	
	Constraint minorDaysBest(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && v.hasWorkers())
				.penalizeLong(HardSoftLongScore.ONE_SOFT, v -> v.getDay())
				.asConstraint("Minor Days is Best");
	}
	
	Constraint timeVariableCost(ConstraintFactory factory) {
		return factory.forEach(Entity.class)
				.filter(e -> e.isWorker() && e.getVehicle() != null && e.getVehicle().hasJobs())
				.penalizeLong(HardSoftLongScore.ONE_SOFT, w -> w.getFinalTimeVariableCost())
				.asConstraint("Time variable cost");
	}
	
	Constraint timeFixedCost(ConstraintFactory factory) {
		return factory.forEach(Entity.class)
				.filter(e -> e.isWorker() && e.getVehicle() != null && e.getVehicle().hasJobs())
				.penalizeLong(HardSoftLongScore.ONE_SOFT, w -> w.getFixedCost())
				.asConstraint("Time fixed cost");
	}
	
	Constraint vehiclesFixedCost(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && v.hasWorkers())
				.penalizeLong(HardSoftLongScore.ONE_SOFT, v -> v.getFixedCost())
				.asConstraint("Vehicles fixed cost");
	}
	
	Constraint vehiclesVariableCost(ConstraintFactory factory) {
		return factory.forEach(Vehicle.class)
				.filter(v -> v.hasJobs() && v.hasWorkers())
				.penalizeLong(HardSoftLongScore.ONE_SOFT, v -> (v.getTotalDistanceInKm() * v.getVariableCost()))
				.asConstraint("Vehicles variable cost");
	}


}
