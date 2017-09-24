package app.tofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import app.repository.AirportsData;
import app.reader.Connection;

@Component
public class SaveToCSVManager {

	private final char SEPARATOR = ';';
	List<AirportsData> airportsDataList;
	Map<String, List<Connection>> connectionsByOrigin;

	public void saveAirportsToFile(String alliance,List<AirportsData> airportsDataList) {
		this.airportsDataList = airportsDataList;
		airportsDataToFile(alliance+"_airports.csv");
	}
	
	public void saveConnectionsToFile(String alliance,Map<String, List<Connection>> connectionsByOrigin) {
		this.connectionsByOrigin = connectionsByOrigin;
		connectionsToFile(alliance+"_connections.csv");
	}

	private void airportsDataToFile(String filename) {
		try {
			PrintWriter pw = new PrintWriter(new File(filename));
			for (AirportsData airport : airportsDataList) {
				String line = airportsDataToLine(airport);
				pw.write(line);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Airports writed to file CSV");
	}

	private void connectionsToFile(String filename) {
		try {
			PrintWriter pw = new PrintWriter(new File(filename));
			for (Map.Entry<String, List<Connection>> cursor : connectionsByOrigin
					.entrySet()) {
				for (Connection connection : cursor.getValue()) {
					String line = connectionsByOriginToLine(cursor.getKey(),
							connection);
					pw.write(line);
				}
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Connections writed to file CSV");
	}

	private String airportsDataToLine(AirportsData airport) {
		String lat = String.valueOf(airport.getLat());
		String lon = String.valueOf(airport.getLon());
		return airport.getCityCode() + SEPARATOR + airport.getCityName()
				+ SEPARATOR + airport.getCountryCode() + SEPARATOR + lat
				+ SEPARATOR + lon + '\n';
	}

	private String connectionsByOriginToLine(String origin,
			Connection connection) {
		return origin + SEPARATOR + connection.getDestination() + SEPARATOR
				+ connection.getAirlinecode() + SEPARATOR
				+ connection.getStops() + '\n';
	}

}