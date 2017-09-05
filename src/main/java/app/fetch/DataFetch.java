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

@Component
public class DataFetch {
	
	@Autowired
	private AirportsDataRepository airportsDataRepository;
	@Autowired
	private ConnectionScanService connectionScanService;
	
	private List<AirportsData> airports;

	public void initAirports() {
		AirportListReader airportListReader = new AirportListReader();	
		airports = airportListReader.fetchDestinations();
	}
	
	public void saveAirports() {
		airportsDataRepository.save(airports);
		System.out.println("Saved Airports to database");
		saveToFileAirports();
	}
		
	public void initConnection() {
		//connectionScanService.makeQueue(airports); // gdy scan
		//connectionScanService.scanAndSave();   // gdy scan
		connectionScanService.readConnectionsFromDatabase();   //gdy do pliku
	}
	
	public void saveToFileAirports() {
		 try
	      {
	         FileOutputStream fileOut = new FileOutputStream("starAllianceAirports.cnt");
	         ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fileOut));
	         out.writeObject(airports);
	         out.close();
	         fileOut.close();
	      }catch(IOException i)
	      {
	    	  System.out.println("IOException (saving Airports): " + i);
	      }
		 System.out.println("Saved Airports to file");
	}
	
}

