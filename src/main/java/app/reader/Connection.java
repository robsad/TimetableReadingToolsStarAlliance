package app.reader;

import java.io.Serializable;

import app.repository.AirConnection;

public class Connection implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String airlinecode;

	private String destination;

	private String stops;

	public Connection (AirConnection airConnection){
		this.destination = airConnection.getDestination();
		this.stops = airConnection.getStops();
		this.airlinecode = airConnection.getAirlineCode();
	}
	
	public Connection (String destination, String stops, String airlinecode){
		this.destination = destination;
		this.stops = stops;
		this.airlinecode = airlinecode;
	}

	public String getAirlinecode() {
		return this.airlinecode;
	}

	public void setAirlinecode(String airlinecode) {
		this.airlinecode = airlinecode;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStops() {
		return this.stops;
	}

	public void setStops(String stops) {
		this.stops = stops;
	}
	
	@Override
	public String toString(){
		return " to: " + this.destination + ", by: " + this.stops + ", with: " + this.airlinecode;
	}
}
