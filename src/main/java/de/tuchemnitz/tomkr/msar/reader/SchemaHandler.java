package de.tuchemnitz.tomkr.msar.reader;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SchemaHandler {

	private static Logger LOG = LoggerFactory.getLogger(SchemaHandler.class);

	public void validateNew() {
		try {
			JSONObject rawSchema = JsonHelper.loadJSONFromFile("D:\\ws\\eclipse_ws\\metaapp\\src\\main\\resources\\schema\\metaData.json");
			JSONObject rawFile = JsonHelper.loadJSONFromFile("D:\\ws\\eclipse_ws\\metaapp\\src\\test\\resources\\jsonExamples\\locationExample2.json");

			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(rawFile); 
		} catch (ValidationException e) {
			LOG.error("ERROR VALIDATING JSON FILE" + e.getMessage());
			
			  e.getCausingExceptions().stream()
		      .map(ValidationException::getMessage)
		      .forEach(System.out::println);
			
			  System.out.println("######################");
			  
			  System.out.println(e.toJSON().toString(4));
			  
			  System.out.println("######################");
		}
	}
	
	public void parseSchema() {
		
	}
}
