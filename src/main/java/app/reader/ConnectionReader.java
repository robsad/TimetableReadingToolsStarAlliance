package app.reader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Service
public class ConnectionReader {
	private RestTemplate restTemplate;
	private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	private final String URL = "http://starmap.fltmaps.com/EN/destinations";

	public ConnectionReader() {
		this.restTemplate = new RestTemplate();
		headers.add("Host", "starmap.fltmaps.com");
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

	public List<Connection> fetchDestinations(String airportCode) {
		String rawData = fetchFromHtml(airportCode);
		return parseFromJson(rawData);
	}

	private String fetchFromHtml(String airportCode) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("initialSearch", "");
		// body.add("Destinations.Page", "2");
		body.add("Destinations.From", "");
		body.add("Destinations.FromId", airportCode);
		body.add("Destinations.FromLocationType", "1");
		body.add("Destinations.CarrierId", "");
		body.add("Destinations.Connections", "false");
		body.add("Destinations.NonStop", "false");
		body.add("Destinations.CodeShares", "false");
		body.add("Destinations.ShowAsList", "false");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				body, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(URL,
				request, String.class);
		MediaType contentType = response.getHeaders().getContentType();
		System.out.println(contentType.toString());
		if (contentType.equals(MediaType.valueOf("text/html;charset=utf-8")))
			return trimHTML(response.getBody());
		else
			return response.getBody();
	}

	private String trimHTML(String response) {
		return response.substring(response.indexOf("rts\":[{") + 5,
				response.indexOf("}</textarea>"));
	}

	private List<Connection> parseFromJson(String responseJson) {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Connection.class,
				new ConnectionCustomDeserializer());
		mapper.registerModule(module);
		try {
			return Arrays.asList(mapper.readValue(responseJson,
					Connection[].class));
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
