package app.fetch;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import app.reader.Connection;
import app.reader.ConnectionReader;
import app.repository.AirConnection;
import app.repository.AirConnectionRepository;
import app.repository.AirportsData;



@Service
public class ConnectionScanService {

	@Autowired
	private AirConnectionRepository airConnectionRepository;
	private ConnectionReader connectionReader;
	private Queue<AirportsData> queue;
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private String alliance;
	
	public void makeQueue(List<AirportsData> airports) {
		queue = new LinkedList<AirportsData>(airports);
	}
	
	//@Scheduled(initialDelay=10000, fixedRate=500)
	public void scanConnections(String alliance) {
		this.connectionReader = new ConnectionReader(alliance);
		this.alliance = alliance;
		AirportsData requestAirport;
		do {
		requestAirport = queue.peek();
		if (requestAirport != null) {
			List<Connection> connections = getConnectionsFromServer(requestAirport.getCityCode());
			connectionsByOrigin.put(requestAirport.getCityCode(), connections);
			showConnections(requestAirport, connections);
			saveAirConnections(requestAirport, connections);
			queue.remove();
			System.out.println("Readed connection from: " + requestAirport.getCityName());
		}
		} while (!queue.isEmpty());
		//Karpathos - puste
	}
	
	public Map<String, List<Connection>> getConnectionsByOrigin() {
		return connectionsByOrigin;
	}
	
	private void saveAirConnections(AirportsData origin, List<Connection> connections) {
		List<AirConnection> airConnections = new LinkedList<>();
		for(Connection connection : connections){
			airConnections.add(new AirConnection(origin, connection));
		}
		airConnectionRepository.save(airConnections);
	}
	
	private List<Connection> getConnectionsFromServer(String requestAirportCode){
		return connectionReader.fetchDestinations(requestAirportCode);
	}
	
	private void showConnections(AirportsData origin, List<Connection> connections) {
		System.out.println("Origin: " + origin.getCityCode());
		for (Connection connection : connections) {
			System.out.println(connection.toString());
		}
	}
	

	
}
