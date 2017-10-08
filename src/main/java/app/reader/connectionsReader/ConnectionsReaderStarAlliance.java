package app.reader.connectionsReader;

import java.util.List;
import app.entities.Connection;

public class ConnectionsReaderStarAlliance extends ConnectionsReader {
	private final String URL = "http://starmap.fltmaps.com/EN/destinations";

	public ConnectionsReaderStarAlliance() {
		super();
		headers.add("Host", "starmap.fltmaps.com");
		headers.add("Referer", "http://starmap.fltmaps.com/EN/");
	}

	public List<Connection> fetchDestinations(String airportCode) {
		String rawData = fetchFromHtml(airportCode, URL);
		return parseFromJson(rawData);
	}
}
