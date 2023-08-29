package dtos.locationService;

import java.util.List;

public class GetTimesResponse {
	
	List<List<Long>> times;

	public List<List<Long>> getTimes() {
		return times;
	}

	public void setTimes(List<List<Long>> times) {
		this.times = times;
	}
		

}
