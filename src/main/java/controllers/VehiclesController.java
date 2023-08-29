package controllers;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtos.request.Vehicle;
import repository.VehiclesJDBC;

@Path("/vehicles")
public class VehiclesController {
	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicles() {
		try {
			return Response.ok().entity(VehiclesJDBC.getAllVehicles()).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
		
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVehicle(Vehicle vehicle) {
		try {
			VehiclesJDBC.addVehicle(vehicle);
			return Response.ok().build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}		
	}

	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVehicle(Vehicle vehicle) {
		try{
			VehiclesJDBC.updateVehicle(vehicle);
			return Response.ok().build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}	
		
	}
	
	@DELETE
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVehicle(@QueryParam("id") String id) {
		try{
			VehiclesJDBC.deleteVehicle(id);
			return Response.ok().build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}	
		
	}

}
