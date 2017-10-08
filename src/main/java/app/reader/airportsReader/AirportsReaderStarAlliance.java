package app.reader.airportsReader;

import java.util.List;
import app.entities.AirportsData;

public class AirportsReaderStarAlliance extends AirportsReader {

	private final String URL = "http://starmap.fltmaps.com/EN/data";

	public AirportsReaderStarAlliance() {
		super();
		headers.add("Host", "starmap.fltmaps.com");
		headers.add("Referer", "http://starmap.fltmaps.com/EN/");
	}

	public List<AirportsData> fetchDestinations() {
		String rawData = fetchFromHtml(URL);
		return parseFromJson(rawData);
	}

}
