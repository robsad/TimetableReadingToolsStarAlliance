package app.repository;

import java.io.Serializable;

import javax.persistence.*;

import app.reader.Connection;

@Entity
@NamedQuery(name = "AirConnection.findAll", query = "SELECT a FROM AirConnection a")
public class AirConnection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String airlineCode;

	private String destination;

	private String stops;

	// bi-directional many-to-one association to Airportsdata
	@ManyToOne
	private AirportsData airportsData;

	public AirConnection() {
	}

	public AirConnection(AirportsData origin, Connection connection) {
		this.airportsData = origin;
		this.destination = connection.getDestination();
		this.stops = connection.getStops();
		this.airlineCode = connection.getAirlinecode();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAirlineCode() {
		return this.airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
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

	public AirportsData getAirportsData() {
		return this.airportsData;
	}

	public void setAirportsData(AirportsData airportsData) {
		this.airportsData = airportsData;
	}

}
