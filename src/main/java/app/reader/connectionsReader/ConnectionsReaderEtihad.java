package app.reader.connectionsReader;

import java.util.List;

import app.entities.Connection;

public class ConnectionsReaderEtihad extends ConnectionsReader {
	private final String URL = "https://ey.fltmaps.com/en/destinations";

	public ConnectionsReaderEtihad() {
		super();
		headers.add("Host", "ey.fltmaps.com");
		headers.add("Referer", "https://ey.fltmaps.com/en");
	}

	public List<Connection> fetchDestinations(String airportCode) {
		String rawData = fetchFromHtml(airportCode, URL);
		return parseFromJson(rawData);
	}
}
