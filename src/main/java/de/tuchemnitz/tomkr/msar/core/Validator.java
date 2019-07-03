package de.tuchemnitz.tomkr.msar.core;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.utils.Result;

/**
 * Validation class used for validating json documents against json schema definitions.
 * 
 * @author Tom Kretzschmar
 *
 */
@Service
public class Validator {
	
	private static Logger LOG = LoggerFactory.getLogger(Validator.class);

	/**
	 * Check given json document against a schema.
	 * 
	 * @param schema The schema to check against.
	 * @param document The document which should be checked.
	 * @return The {@link Result} which contains the problems if occurring.
	 */
	public Result checkDocument(Schema schema, JSONObject document) {
		try {
			schema.validate(document);
		} catch (ValidationException e) {
			String errorMsg = "Error while validating json document: \n"
								+ e.getMessage()
								+ "\n---- Further information ----\n"
								+ e.toJSON().toString(4)
								+ "\n-----------------------------";
			LOG.error(errorMsg);
			return new Result(false, errorMsg);
		}
		return new Result(true, null);
	}
}
