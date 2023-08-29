package dtos.request;

public class Location {

  private String id;
  private int internalId;

  private double latitude;
  private double longitude;

  public Location() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

public int getInternalId() {
    return internalId;
  }

  public void setInternalId(int internalId) {
    this.internalId = internalId;
  }

}
