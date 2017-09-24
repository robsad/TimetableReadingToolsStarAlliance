package app.repository;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


@Entity
@NamedQuery(name="AirportsData.findAll", query="SELECT a FROM AirportsData a")
public class AirportsData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String cityCode;

	private String cityName;
	
	private String countryCode;

	private Timestamp created;

	private double lat;

	private double lon;

	private Timestamp modified;

	@OneToMany(mappedBy="airportsData")
	private List<AirConnection> airConnections;

	public AirportsData(){
	}
	
	public AirportsData(String cityCode, String cityName, String countryCode,
			double lat, double lon) {
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.countryCode = countryCode;
		this.lat = lat;
		this.lon = lon;
		this.airConnections = null;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public double getLon() {
		return this.lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public Timestamp getModified() {
		return this.modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public List<AirConnection> getAirConnections() {
		return this.airConnections;
	}

	public void setAirConnections(List<AirConnection> airConnections) {
		this.airConnections = airConnections;
	}

	public AirConnection addAirConnection(AirConnection airConnection) {
		getAirConnections().add(airConnection);
		airConnection.setAirportsData(this);
		return airConnection;
	}

	public AirConnection removeAirConnection(AirConnection airConnection) {
		getAirConnections().remove(airConnection);
		airConnection.setAirportsData(null);
		return airConnection;
	}
	
	public String toString(){
		return "Code: " + cityCode + ", Name: " + cityName + ", Country: " + countryCode;
	}

}

