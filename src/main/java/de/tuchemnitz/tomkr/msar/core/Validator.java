package de.tuchemnitz.tomkr.msar.core;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.utils.Result;

/**
 * 
 * @author Kretzschmar
 *
 */
@Service
public class Validator {
	
	private static Logger LOG = LoggerFactory.getLogger(Validator.class);

	
	public Result checkDocument(Schema schema, JSONObject document) {
		try {
			schema.validate(document);
		} catch (ValidationException e) {
			LOG.error("ERROR VALIDATING JSON FILE" + e.getMessage());

			e.getCausingExceptions().stream().map(ValidationException::getMessage).forEach(System.out::println);

			System.out.println("######################");

			System.out.println(e.toJSON().toString(4));

			System.out.println("######################");
			return new Result(false, e.getMessage());
		}
		return new Result(true, null);
	}
}
