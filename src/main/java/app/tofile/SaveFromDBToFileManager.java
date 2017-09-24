package app.tofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.reader.Connection;
import app.repository.AirConnection;
import app.repository.AirConnectionRepository;
import app.repository.AirportsData;
import app.repository.AirportsDataRepository;

@Component
public class SaveFromDBToFileManager {

	private final char SEPARATOR = ';';
	@Autowired
	private AirportsDataRepository airportsDataRepository;
	@Autowired
	private AirConnectionRepository airConnectionRepository;
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private List<AirportsData> airportsDataList = new ArrayList<AirportsData>();

	public void readAirportsFromDatabaseToFile(String alliance) {
		transferAirports();
		try {
			PrintWriter pw = new PrintWriter(new File(alliance
					+ "_airports.csv"));
			for (AirportsData airport : airportsDataList) {
				String line = airportsDataToLine(airport);
				pw.write(line);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Airports writed from database to file CSV");
	}

	private void transferAirports() {
		Iterable<AirportsData> dataBaseAirports;
		dataBaseAirports = airportsDataRepository.findAll();
		System.out.println(dataBaseAirports);
		for (AirportsData airportsData : dataBaseAirports) {
			airportsDataList.add(airportsData);
			System.out.println(airportsData);
		}
	}

	public void readConnectionsFromDatabaseToFile(String alliance) {
		transferFromDatabase();
		try {
			PrintWriter pw = new PrintWriter(new File(alliance
					+ "_connections.csv"));
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
		System.out.println("Connections writed from database to file CSV");
	}

	private void transferFromDatabase() {
		Iterable<AirConnection> dataBaseAirConnection;
		dataBaseAirConnection = airConnectionRepository.findAll();
		for (AirConnection airConnection : dataBaseAirConnection) {
			AirportsData originAirport = airConnection.getAirportsData();
			String originCode = originAirport.getCityCode();
			Connection connection = new Connection(airConnection);
			List<Connection> tempConnections = new LinkedList<>();
			if (connectionsByOrigin.containsKey(originCode)) {
				tempConnections = connectionsByOrigin.get(originCode);
			}
			tempConnections.add(connection);
			connectionsByOrigin.put(originCode, tempConnections);
		}
		System.out.println(connectionsByOrigin);
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
