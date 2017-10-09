package app.reader.airportsReader;

import java.util.List;

import app.entities.AirportsData;

public class AirportsReaderEtihad extends AirportsReader {

	private final String URL = "https://ey.fltmaps.com/en/destinations";

	public AirportsReaderEtihad() {
		super();
		headers.add("Host", "ey.fltmaps.com");
		headers.add("Referer", "https://ey.fltmaps.com/en");
	}

	public List<AirportsData> fetchDestinations() {
		String rawData = fetchFromHtml(URL);
		return parseFromJson(rawData);
	}
	
}
