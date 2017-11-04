package app.fetch;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.AirConnection;
import app.entities.AirportsData;
import app.entities.Connection;
import app.reader.connectionsReader.ConnectionsReader;
import app.reader.connectionsReader.ConnectionsReaderOneWorld;
import app.reader.connectionsReader.ConnectionsReaderStarAlliance;
import app.repository.AirConnectionRepository;

@Service
public class ConnectionScanService {

	@Autowired
	private AirConnectionRepository airConnectionRepository;
	private ConnectionsReader connectionsReader;
	private Queue<AirportsData> queue;
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private String alliance;

	public void makeQueue(List<AirportsData> airports) {
		queue = new LinkedList<AirportsData>(airports);
	}

	// @Scheduled(initialDelay=10000, fixedRate=500)
	public void scanConnections(String alliance) {
		switch (alliance) {
		case "StarAlliance":
			connectionsReader = new ConnectionsReaderStarAlliance();
			break;
		case "OneWorld":
			connectionsReader = new ConnectionsReaderOneWorld();
			break;
		default:
			connectionsReader = new ConnectionsReaderStarAlliance();
		}
		AirportsData requestAirport;
		airConnectionRepository.deleteAll();
		do {
			requestAirport = queue.peek();
			if (requestAirport != null) {
				List<Connection> connections = getConnectionsFromServer(requestAirport
						.getCityCode());
				connectionsByOrigin.put(requestAirport.getCityCode(),
						connections);
				showConnections(requestAirport, connections);
				saveAirConnections(requestAirport, connections);
				queue.remove();
				System.out.println("Readed connection from: "
						+ requestAirport.getCityName());
			}
		} while (!queue.isEmpty());
		// Karpathos - puste
	}

	public Map<String, List<Connection>> getConnectionsByOrigin() {
		return connectionsByOrigin;
	}

	private List<Connection> getConnectionsFromServer(String requestAirportCode) {
		return connectionsReader.fetchDestinations(requestAirportCode);
	}
	
	private void saveAirConnections(AirportsData origin,
			List<Connection> connections) {
		List<AirConnection> airConnections = new LinkedList<>();
		for (Connection connection : connections) {
			airConnections.add(new AirConnection(origin, connection));
		}
		airConnectionRepository.save(airConnections);
	}

	private void showConnections(AirportsData origin,
			List<Connection> connections) {
		System.out.println("Origin: " + origin.getCityCode());
		for (Connection connection : connections) {
			System.out.println(connection.toString());
		}
	}

}
