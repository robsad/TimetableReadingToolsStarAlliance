package app.reader.deserializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.entities.AirportsData;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class AirportsCustomDeserializer extends StdDeserializer<AirportsData> {

	public AirportsCustomDeserializer() {
		this(null);
	}

	public AirportsCustomDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public AirportsData deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		// System.out.println("DESTINATION: " + destination);
		return new AirportsData(
				node.get("Code").asText(),
				node.get("Name").asText(),
				node.get("CountryCode").asText(),
				node.get("Lat").floatValue(),
				node.get("Lon").floatValue()		
			);
		}

		//"Code":"ADD",
		//"CountryCode":"ET",
		//"Name":"Addis Ababa",
		//"Lat":9.02,
		//"Lon":38.75,
		
	}
