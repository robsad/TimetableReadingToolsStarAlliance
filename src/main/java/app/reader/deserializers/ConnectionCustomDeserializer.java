package app.reader.deserializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.entities.Connection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ConnectionCustomDeserializer extends StdDeserializer<Connection> {

	public ConnectionCustomDeserializer() {
		this(null);
	}

	public ConnectionCustomDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Connection deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		List<String> stopsList = new ArrayList<String>();
		JsonNode node = jp.getCodec().readTree(jp);
		ArrayNode wps = (ArrayNode) node.get("wps");
		String destination = wps.get(wps.size()-1).get("id").asText();
		wps.remove(wps.size()-1);
		//System.out.println("DESTINATION: " + destination);
		for (JsonNode i : wps) {
			stopsList.add(i.get("id").asText());
		    //System.out.println("destination: " + destination + " stop :" + i.get("id").asText());
		    }
		String stops = stopsList.toString();
		ArrayNode rc = (ArrayNode) node.get("rc");
		String airlineCode = rc.get(4).get("Code").asText();
		return new Connection(destination, stops, airlineCode);
	}
}
