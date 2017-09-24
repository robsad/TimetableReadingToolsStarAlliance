package app.fetch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.reader.AirportListReader;
import app.repository.*;

@Service
public class AirportsScanService {
	
	@Autowired
	private AirportsDataRepository airportsDataRepository;
	private String alliance;
	private List<AirportsData> airportsDataList;


	public void scanAirports(String alliance) {
		this.alliance=alliance;
		AirportListReader airportListReader = new AirportListReader(alliance);	
		airportsDataList = airportListReader.fetchDestinations();
	}
	
	public List<AirportsData> getAirports() {
		return airportsDataList;
	}
	
	public void saveAirportsToDataBase() {
		airportsDataRepository.save(airportsDataList);
	}
		
}

