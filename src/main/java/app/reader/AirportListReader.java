package app.reader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import app.repository.AirportsData;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class AirportListReader {
	private RestTemplate restTemplate;
	private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	private final String URL = "http://starmap.fltmaps.com/EN/data";
	//private final String URL = "http://onw.fltmaps.com/en/data";

	public AirportListReader() {
		this.restTemplate = new RestTemplate();
		//headers.add("Host", "starmap.fltmaps.com");
		headers.add("Host", "onw.fltmaps.com");
		headers.add("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
		headers.add("Accept", "*/*");
		headers.add("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
		// headers.add("Accept-Encoding", "gzip, deflate");
		headers.add("Referer", "http://starmap.fltmaps.com/EN/");
		headers.add("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		headers.add("X-Requested-With", "XMLHttpRequest");
		headers.add("Content-Length", "300");
		headers.add("Cookie", "ROUTEID=.1");
		headers.add("DNT", "1");
		headers.add("Connection", "keep-alive");	
	}

	public List<AirportsData> fetchDestinations() {
		String rawData = fetchFromHtml(); 
		return parseFromJson(rawData);
	}

	private String fetchFromHtml() {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				body, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(URL,
				request, String.class);
		// HttpStatus statusCode = response.getStatusCode();
		MediaType contentType = response.getHeaders().getContentType();
		return trimHTML(response.getBody());
	}

	public String trimHTML(String response) {
		return response.substring(response.indexOf("Cities\":") + 8, response.indexOf(",\"DestinationsHeading"));
	}
	
	private List<AirportsData> parseFromJson(String responseJson) {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(AirportsData.class, new AirportsCustomDeserializer());
		mapper.registerModule(module);
		try {
			return Arrays.asList(mapper.readValue(responseJson,AirportsData[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}	
	}
	
}
