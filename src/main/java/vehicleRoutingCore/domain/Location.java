package vehicleRoutingCore.domain;

import java.util.HashMap;

public class Location {

	private int id;

	private double latitude;
	private double longitude;

	private HashMap<Integer, Long> timeToOtherLocations = new HashMap<>();
	private HashMap<Integer, Long> distanceToOtherLocations = new HashMap<>();
	
	public Location() {}
	
	public Location(int id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public HashMap<Integer, Long> getTimeToOtherLocations() {
		return timeToOtherLocations;
	}

	public void setTimeToOtherLocations(HashMap<Integer, Long> timeToOtherLocations) {
		this.timeToOtherLocations = timeToOtherLocations;
	}

    public HashMap<Integer, Long> getDistanceToOtherLocations() {
      return distanceToOtherLocations;
    }
  
    public void setDistanceToOtherLocations(HashMap<Integer, Long> distanceToOtherLocations) {
      this.distanceToOtherLocations = distanceToOtherLocations;
    }

}
