package app.reader.connectionsReader;

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

import app.entities.Connection;
import app.reader.deserializers.ConnectionCustomDeserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class ConnectionsReader {
	protected RestTemplate restTemplate;
	protected MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

	public ConnectionsReader() {
		this.restTemplate = new RestTemplate();
		headers.add("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
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

	public abstract List<Connection> fetchDestinations(String airportCode);
	
	protected String fetchFromHtml(String airportCode, String URL) {
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
		// HttpStatus statusCode = response.getStatusCode();
		MediaType contentType = response.getHeaders().getContentType();
		if (contentType.equals(MediaType.valueOf("text/html;charset=utf-8"))) {
			return trimHTML(response.getBody());
		}
		return response.getBody();
	}

	protected String trimHTML(String response) {
		return response.substring(response.indexOf("rts\":[{") + 5, response.indexOf("}</textarea>"));
	}

	protected List<Connection> parseFromJson(String responseJson) {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Connection.class, new ConnectionCustomDeserializer());
		mapper.registerModule(module);
		//System.out.println(responseJson);
		try {
			return Arrays.asList(mapper.readValue(responseJson,Connection[].class));
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
