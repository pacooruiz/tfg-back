package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import daos.Planning;
import daos.Route;
import daos.RouteJob;
import daos.RouteWorker;
import dtos.request.Job;
import dtos.request.Location;
import dtos.request.SolveRequestBody;
import dtos.request.Vehicle;
import dtos.request.Worker;
import dtos.response.GeneratePlanningResponse;
import dtos.response.route.PlanningResponse;
import repository.PlanningsJDBC;
import vehicleRoutingCore.ProblemSolver;

public class PlanningsService {
	
	HashMap<String, Integer> locationsExtIntId = new HashMap<>();
	
	HashMap<Integer, String> locationsIntExtId = new HashMap<>();

    HashMap<Integer, Location> allLocations = new HashMap<>();
    
    List<Location> depots = new ArrayList<>();

  public GeneratePlanningResponse requestToProblem(SolveRequestBody request) throws Exception {
	  
	if(request.getTitle()==null || request.getTitle().isBlank()) {
		request.setTitle("Planificación sin título");
	}

    //
    List<String> depotsId = request.getVehicles().stream().map(Vehicle::getDepot)
        .map(Location::getId).distinct().collect(Collectors.toList());

    depots = request.getVehicles().stream().map(Vehicle::getDepot)
        .filter(d -> depotsId.contains(d.getId())).collect(Collectors.toList());

    List<Location> jobsLocations =
        request.getJobs().stream().map(Job::getLocation).collect(Collectors.toList());

    int i = 0;
    for (Location jobLocation : jobsLocations) {
    	locationsExtIntId.put(jobLocation.getId(), i);
    	locationsIntExtId.put(i, jobLocation.getId());
    	allLocations.put(i, jobLocation);
    	i++;
    }

    for (Location vehicleDepot : depots) {
    	locationsExtIntId.put(vehicleDepot.getId(), i);
    	locationsIntExtId.put(i, vehicleDepot.getId());
    	allLocations.put(i, vehicleDepot);
    	i++;
    }

    String coordinates = new String();
    for (int x = 0; x < allLocations.keySet().size(); x++) {
    	
    	coordinates = coordinates.concat(String.valueOf(allLocations.get(x).getLongitude())).concat(",").concat(String.valueOf(allLocations.get(x).getLatitude()));
        
    	if (x != allLocations.keySet().size() - 1) {
        	coordinates = coordinates.concat(";");
        }
    }

    int matrixSize = allLocations.keySet().size();
    Long[][] durationMatrix = new Long[matrixSize][matrixSize];
    Long[][] distanceMatrix = new Long[matrixSize][matrixSize];

    String osrmRresponse = getOsrmResponse(coordinates);

    JsonObject jsonResponse = JsonParser.parseString(osrmRresponse).getAsJsonObject();
    JsonArray distances = jsonResponse.get("distances").getAsJsonArray();
    JsonArray durations = jsonResponse.get("durations").getAsJsonArray();
    for (int x = 0; x < distances.size(); x++) {
      
      JsonArray distancesFromX = distances.get(x).getAsJsonArray();
      JsonArray durationFromX = durations.get(x).getAsJsonArray();
      System.out.println("DISTANCES FROM " + x + ": " + distancesFromX);
      System.out.println("DURATIONS FROM " + x + ": " + durationFromX);
      
      for(int y=0; y< distancesFromX.size(); y++) {
        Long distance = distancesFromX.get(y).getAsLong();
        Long duration = durationFromX.get(y).getAsLong();
        distanceMatrix[x][y] = distance;
        durationMatrix[x][y] = duration*1000;
      }
        
    }
    
    HashMap<Integer, vehicleRoutingCore.domain.Location> domainLocations = new HashMap<>();
    
    for(int j=0; j< matrixSize; j++) {
    	vehicleRoutingCore.domain.Location location = new vehicleRoutingCore.domain.Location();
    	location.setId(j);
    	HashMap<Integer, Long> timeToOtherLocations = new HashMap<>();
    	HashMap<Integer, Long> distanceToOtherLocations = new HashMap<>();
    	for(int k=0; k<matrixSize; k++) {
    		timeToOtherLocations.put(k, durationMatrix[j][k]);
    		distanceToOtherLocations.put(k, distanceMatrix[j][k]);
    	}
    	location.setDistanceToOtherLocations(distanceToOtherLocations);
    	location.setTimeToOtherLocations(timeToOtherLocations);
    	domainLocations.put(j, location);
    }
    
    // MAPEAMOS LOS TRABAJOS
    
    List<vehicleRoutingCore.domain.Entity> entities = dtoJobsToDomainJobs(request.getJobs(), domainLocations, locationsExtIntId);    
    
    // MAPEAMOS VEHÍCULOS Y TRABAJADORES
    
    List<vehicleRoutingCore.domain.Vehicle> vehicles = new ArrayList<>();
    
    List<LocalDate> listOfDates = calcDatesBetweenTwoDates(request.getStartDate(), request.getEndDate());
    
    for(LocalDate date : listOfDates) {
    	
    	Long dateInMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    	
    	for(Vehicle vehicle : request.getVehicles()) {
    		vehicleRoutingCore.domain.Vehicle domainVehicle = new vehicleRoutingCore.domain.Vehicle();
    		domainVehicle.setCapacity(vehicle.getMaxLoad());
    		domainVehicle.setDay(dateInMillis);
    		domainVehicle.setId(vehicle.getId() + "_" + date.toString());
    		domainVehicle.setSeats(vehicle.getSeats());
    		domainVehicle.setFixedCost(vehicle.getFixedCost());
    		domainVehicle.setVariableCost(vehicle.getVariableCost());
    		domainVehicle.setDepot(domainLocations.get(locationsExtIntId.get(vehicle.getDepot().getId())));
    		vehicles.add(domainVehicle);
    	}
    	
    	for(Worker worker : request.getWorkers()) {
    		vehicleRoutingCore.domain.Entity domainWorker = new vehicleRoutingCore.domain.Entity();
    		domainWorker.setType("worker");
    		domainWorker.setDate(dateInMillis);
    		domainWorker.setId(worker.getId() + "_" + date.toString());
    		domainWorker.setWorkerStartDateTime(getDateTime(date, worker.getStartTime()));
    		domainWorker.setWorkerEndDateTime(getDateTime(date, worker.getEndTime()));
    		domainWorker.setFixedCost(worker.getFixedCost());
    		domainWorker.setVariableCost(worker.getVariableCost());
    		entities.add(domainWorker);
    		
    		
    		
    	}
    }
    
    vehicleRoutingCore.domain.VehicleRoutingSolution problem = new vehicleRoutingCore.domain.VehicleRoutingSolution();
    problem.setEntities(entities);
    problem.setVehicles(vehicles);
    
    ProblemSolver solver = new ProblemSolver();
    solver.configSolver(request.getTime());
    
    vehicleRoutingCore.domain.VehicleRoutingSolution solution = solver.solve(problem);
    
    if(solution.getScore().getHardScore() != 0) {
    	
    	PlanningResponse response = new PlanningResponse();
		
		throw new Exception("ERR");
	}
    
	Planning planning = new Planning();
	planning.setId(UUID.randomUUID().toString());
	planning.setName(request.getTitle());    
	
	List<String> dates = new ArrayList<>();
	
	List<Route> routes = new ArrayList<>();
	List<RouteWorker> workers = new ArrayList<>();
	List<RouteJob> jobs = new ArrayList<>();
	  
	  
	  for(vehicleRoutingCore.domain.Vehicle domainVehicle : solution.getVehicles()) {
		  
		  if(domainVehicle.hasJobs() && domainVehicle.hasWorkers()) {
			  
			  String vehicleDate = domainVehicle.getId().substring(domainVehicle.getId().length()-10, domainVehicle.getId().length());
			  if(!dates.contains(vehicleDate)) {
				  dates.add(vehicleDate);
			  }
			  
			  Route route = new Route();
			  route.setId(UUID.randomUUID().toString());
			  
			  String vehicleId = domainVehicle.getId().substring(0, domainVehicle.getId().length()-11);
			  String date = domainVehicle.getId().substring(domainVehicle.getId().length()-10, domainVehicle.getId().length());
			  
			  route.setVehicle(vehicleId);
			  route.setDate(date);
			  Location depot =  depots.stream().filter(d -> d.getId().equals(locationsIntExtId.get(domainVehicle.getDepot().getId()))).collect(Collectors.toList()).get(0);
			  route.setDepot(depot.getId());
			  route.setPlanningId(planning.getId());
		  
			  routes.add(route);
			  
			  List<RouteWorker> routeWorkers = new ArrayList<>();
			  
			  for(vehicleRoutingCore.domain.Entity entity : domainVehicle.getEntities()) {
				  
				  if(entity.isWorker()) {
					  
					  route.setStart(entity.getWorkersStartDateTime());
					  
					  RouteWorker worker = new RouteWorker();
					  worker.setId(entity.getId());
					  worker.setRouteId(route.getId());
					  worker.setPlanningId(planning.getId());
					  worker.setStart(entity.getWorkerStartDateTime());
					  worker.setEnd(entity.getWorkerEndDateTime());
					  worker.setFixedCost(entity.getFixedCost());
					  worker.setVariableCost(entity.getVariableCost());
					  routeWorkers.add(worker);
					  
				  }
				  else if(entity.isJob()) {
					  
					  if(entity.isLastJob()) {
						  route.setEnd(entity.getDepotArrivalTimeFromLastJob());
					  }
					  
					  RouteJob job = new RouteJob();
					  job.setId(entity.getId());
					  job.setPlanningId(planning.getId());
					  job.setRouteId(route.getId());
					  job.setStart(entity.getJobStartDateTime());
					  job.setEnd(entity.getJobEndDateTime());
					  job.setWorkersNeeded(entity.getWorkersNeeded());
					  jobs.add(job);
				  }
				  
			  }
			  
			  int maxWorkersNeeded = jobs.stream().map(RouteJob::getWorkersNeeded).mapToInt(Integer::intValue).max().getAsInt();
			  
			  if(routeWorkers.size() > maxWorkersNeeded) {
				  
				  int totalTime = (int) (route.getEnd() - route.getStart());
				  routeWorkers.stream().forEach(w -> w.setTotalCost(totalTime));
				  Collections.sort(routeWorkers);
				  List<RouteWorker> optimizedWorkers = new ArrayList<>();
				  for(int w=0; w<maxWorkersNeeded; w++) {
					  optimizedWorkers.add(routeWorkers.get(w));
				  }
				  routeWorkers = optimizedWorkers;
			  }
			  
			  workers.addAll(routeWorkers);
			  
		  }  
	  }
	  
	  List<Integer> intDates = dates.stream().map(d -> Integer.valueOf(d.replace("-", ""))).collect(Collectors.toList());
	  int maxDate =  Collections.max(intDates);
	  int minDate =  Collections.min(intDates);
	  planning.setStart(formatIntDate(minDate));
	  planning.setEnd(formatIntDate(maxDate));
	  
	  PlanningsJDBC.addPlanning(planning);
	  PlanningsJDBC.addRoutes(routes);
	  PlanningsJDBC.addWorkers(workers);
	  PlanningsJDBC.addJobs(jobs);
	  
	  
	  GeneratePlanningResponse response = new GeneratePlanningResponse();
	  response.setId(planning.getId());
	  
	  return response;
    
  }
  
  private String formatIntDate(int date) {
	  
	  String stringNumber = String.valueOf(date);
	  String day = stringNumber.substring(6, 8);
	  String month = stringNumber.substring(4, 6);;
	  String year = stringNumber.substring(0, 4);;;
	  return day + "/" + month + "/" + year;
  }
  

  
  private Long getDateTime(LocalDate date, String time) {
	  int hour = Integer.parseInt(time.substring(0, 2));
	  int minutes = Integer.parseInt(time.substring(3, 5));
	  return date.atTime(hour, minutes).toInstant(ZoneOffset.UTC).toEpochMilli();
  }
  
  private List<LocalDate> calcDatesBetweenTwoDates(Long startDateMillis, Long endDateMillis){
	  LocalDate startDate = Instant.ofEpochMilli(startDateMillis).atZone(ZoneId.systemDefault()).toLocalDate();
	  LocalDate endDate = Instant.ofEpochMilli(endDateMillis + 86400000).atZone(ZoneId.systemDefault()).toLocalDate();
	  return startDate.datesUntil(endDate).collect(Collectors.toList());
  }
  
  private List<vehicleRoutingCore.domain.Entity> dtoJobsToDomainJobs(List<Job> jobs, HashMap<Integer, vehicleRoutingCore.domain.Location> domainLocations, HashMap<String, Integer> locationsExtIntId){
	  
	  List<vehicleRoutingCore.domain.Entity> domainJobs = new ArrayList<>();
	  for(Job job : jobs) {
	    	vehicleRoutingCore.domain.Entity domainJob = new vehicleRoutingCore.domain.Entity();
	    	domainJob.setType("job");
	    	domainJob.setId(job.getId());
	    	domainJob.setTitle(job.getTitle());
	    	domainJob.setDuration(job.getDuration());
	    	domainJob.setLoadNeeded(job.getLoadNeeded());
	    	domainJob.setWorkersNeeded(job.getWorkersNeeded());
	    	domainJob.setEarliestStartDateTime(job.getEarliestStartDateTime());
	    	domainJob.setLastestEndDateTime(job.getLastestEndDateTime());
	    	domainJob.setLocation(domainLocations.get(locationsExtIntId.get(job.getLocation().getId())));
	    	domainJobs.add(domainJob);
	    }
	  return domainJobs;
  }

  private String getOsrmResponse(String coordinates) throws Exception {
    try {
      
      URL url = new URL("http://localhost:5000/table/v1/driving/".concat(coordinates).concat("?annotations=distance,duration"));
      System.out.println("REQUEST URL: " + url);
      System.out.println("REQUEST METHOD: GET");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      int status = con.getResponseCode();
      System.out.println("RESPONSE STATUS: " + status);
      BufferedReader in = null;
      if(status > 299) {
        in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      else {
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      }
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      con.disconnect();
      System.out.println("RESPONSE BODY: " + content.toString());
      return content.toString();

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }


}
