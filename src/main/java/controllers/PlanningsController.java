package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import daos.detail.RouteDetail;
import daos.detail.RouteJobDetail;
import daos.detail.RouteWorkerDetail;
import dtos.request.SolveRequestBody;
import dtos.response.GeneratePlanningResponse;
import dtos.response.route.Day;
import dtos.response.route.PlanningResponse;
import dtos.response.route.Route;
import repository.PlanningsJDBC;
import services.PlanningsService;

@Path("/plannings")
public class PlanningsController {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlannings() {
		try {
			return Response.ok().entity(PlanningsJDBC.getAllPlannings()).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

  @POST
  @Path("/generate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response generatePlanning(SolveRequestBody body) {

	  
    try {
    	GeneratePlanningResponse response = new PlanningsService().requestToProblem(body);
       
       return Response.ok().entity(response).build();
      
      
    } catch (Exception e) {
    	if(!e.getMessage().equals("ERR")) {
    		e.printStackTrace();
    	}
    	
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

  }
  
  	@GET
	@Produces(MediaType.APPLICATION_JSON)
  	@Path("/{id}")
	public Response getPlanning(@PathParam("id") String planningId) {
	  
	  try {
		  
		PlanningResponse response = new PlanningResponse();
		
		List<RouteDetail> routes = PlanningsJDBC.getRoutes(planningId);
		List<RouteJobDetail> jobs = PlanningsJDBC.getJobs(planningId);
		List<RouteWorkerDetail> workers = PlanningsJDBC.getWorkers(planningId);
		
		Set<String> dates = routes.stream().map(RouteDetail::getDate).collect(Collectors.toSet());
		
		List<Day> responseDates = new ArrayList<>();
		
		for(String date : dates) {
			
			List<Route> dayRoutes = new ArrayList<>();
			
			for(RouteDetail routeDB : routes.stream().filter(r -> r.getDate().equals(date)).collect(Collectors.toList())) {
				
				Route route = new Route();
				route.setVehicleId(routeDB.getVehicle());
				route.setDate(date);
				route.setWorkStartDateTime(routeDB.getStart());
				route.setWorkEndDateTime(routeDB.getEnd());
				route.setDepot(routeDB.getDepot());
				route.setJobs(jobs.stream().filter(j -> j.getRouteId().equals(routeDB.getId())).collect(Collectors.toList()));
				route.setWorkers(workers.stream().filter(w -> w.getRouteId().equals(routeDB.getId())).collect(Collectors.toList()));
				dayRoutes.add(route);
			}
			
			Day day = new Day();
			day.setDate(date);
			day.setRoutes(dayRoutes);
			
			responseDates.add(day);
		}
		
		response.setTitle(PlanningsJDBC.getPlanningTitle(planningId));
		
		response.setDays(responseDates);
		response.setId(planningId);
		
		return Response.ok().entity(response).build();
		
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  
		return Response.status(Response.Status.NO_CONTENT).build();
		
	}
  
  @DELETE
  public Response deletePlanning(@QueryParam("id") String planningId) {
	  try {
		  PlanningsJDBC.deletePlanning(planningId);
		  return Response.ok().build();
	  } catch (SQLException e) {
		  e.printStackTrace();
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	  }
  }

}
