package app.fetch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.tofile.SaveToCSVManager;
import app.repository.AirportsData;

@Component
public class ReadingManager {
	
	@Autowired
	private ConnectionScanService connectionScanService;
	@Autowired
	private AirportsScanService airportsScanService;
	@Autowired
	private SaveToCSVManager saveToCSVManager;

	
	public void airportsScan(String alliance) {
		airportsScanService.scanAirports(alliance);
		airportsScanService.saveAirportsToDataBase();
	}

	public void connectionsScan(String alliance) {
		List<AirportsData> airports = airportsScanService.getAirports();
		connectionScanService.makeQueue(airports);
		connectionScanService.scanConnections(alliance);
	}
	
	public void saveAirportsToFile(String alliance) {
		saveToCSVManager.saveAirportsToFile(alliance,airportsScanService.getAirports());
	}
	
	public void saveConnectionsToFile(String alliance) {
		saveToCSVManager.saveConnectionsToFile(alliance,connectionScanService.getConnectionsByOrigin());
	}

}
