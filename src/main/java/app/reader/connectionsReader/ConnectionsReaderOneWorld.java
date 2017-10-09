package app.reader.connectionsReader;

import java.util.List;

import app.entities.Connection;

public class ConnectionsReaderOneWorld extends ConnectionsReader {
	private final String URL = "http://onw.fltmaps.com/en/destinations";

	public ConnectionsReaderOneWorld() {
		super();
		headers.add("Host", "onw.fltmaps.com");
		headers.add("Referer", "http://onw.fltmaps.com/en/");
	}

	public List<Connection> fetchDestinations(String airportCode) {
		String rawData = fetchFromHtml(airportCode, URL);
		return parseFromJson(rawData);
	}
}
