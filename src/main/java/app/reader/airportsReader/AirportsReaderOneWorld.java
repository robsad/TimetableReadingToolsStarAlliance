package app.reader.airportsReader;

import java.util.List;

import app.entities.AirportsData;

public class AirportsReaderOneWorld extends AirportsReader {

	private final String URL = "http://onw.fltmaps.com/en/data";

	public AirportsReaderOneWorld() {
		super();
		headers.add("Host", "onw.fltmaps.com");
		headers.add("Referer", "http://onw.fltmaps.com/en/");
	}

	public List<AirportsData> fetchDestinations() {
		String rawData = fetchFromHtml(URL);
		return parseFromJson(rawData);
	}
	
}
