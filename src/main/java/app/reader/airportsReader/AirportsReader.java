package app.reader.airportsReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import app.entities.AirportsData;
import app.reader.deserializers.AirportsCustomDeserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


public abstract class AirportsReader {
	protected RestTemplate restTemplate;
	protected MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

	public AirportsReader() {
		this.restTemplate = new RestTemplate();
		headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
		headers.add("Accept", "*/*");
		headers.add("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
		// headers.add("Accept-Encoding", "gzip, deflate");
		headers.add("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		headers.add("X-Requested-With", "XMLHttpRequest");
		headers.add("Content-Length", "300");
		headers.add("Cookie", "ROUTEID=.1");
		headers.add("DNT", "1");
		headers.add("Connection", "keep-alive");	
	}
	
	public abstract List<AirportsData> fetchDestinations();

	protected String fetchFromHtml(String URL) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				body, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(URL,
				request, String.class);
		// HttpStatus statusCode = response.getStatusCode();
		MediaType contentType = response.getHeaders().getContentType();
		return trimHTML(response.getBody());
	}

	protected String trimHTML(String response) {
		System.out.println("response: "+response);
		int startPosition = response.indexOf("Cities\":") + 8;
		int endPosition = response.indexOf(",\"DestinationsHeading");
		System.out.println("startPosition: "+startPosition+" endPosition: " +endPosition);
		return response.substring(startPosition, endPosition);
	}
	
	protected List<AirportsData> parseFromJson(String responseJson) {
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


