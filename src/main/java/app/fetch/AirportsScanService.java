package app.fetch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.AirportsData;
import app.reader.airportsReader.AirportsReader;
import app.reader.airportsReader.AirportsReaderEtihad;
import app.reader.airportsReader.AirportsReaderOneWorld;
import app.reader.airportsReader.AirportsReaderStarAlliance;
import app.repository.*;

@Service
public class AirportsScanService {
	
	@Autowired
	private AirportsDataRepository airportsDataRepository;
	private List<AirportsData> airportsDataList;


	public void scanAirports(String alliance) {
		AirportsReader airportsReader;
		switch (alliance) {
		case "StarAlliance":
			airportsReader = new AirportsReaderStarAlliance();
				break;
		case "OneWorld":
			airportsReader = new AirportsReaderOneWorld();	
				break;
		case "Etihad":
			airportsReader = new AirportsReaderEtihad();	
				break;
		default:
			airportsReader = new AirportsReaderStarAlliance();
		}
		airportsDataList = airportsReader.fetchDestinations();
	}
	
	public List<AirportsData> getAirports() {
		return airportsDataList;
	}
	
	public void saveAirportsToDataBase() {
		airportsDataRepository.save(airportsDataList);
	}
		
}

