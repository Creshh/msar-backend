package de.tuchemnitz.tomkr.msar.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class holding information for the suggestion feature.
 * Includes the {@link #name} of the field the suggestions relate to and the {@link #results} itself. 
 * 
 * @author Tom Kretzschmar
 *
 */
public class SuggestCategory {

	/**
	 * The name of the field the suggestion relates to. 
	 */
	private String name;
	
	/**
	 * The suggestion results.
	 */
	private List<SuggestItem> results;
	
	/**
	 * Constructor
	 * @param name The field name the suggestions relate to
	 */
	public SuggestCategory(String name) {
		this.name = name;
	}
	
	/**
	 * Getter.
	 * @return The field name the suggestions relate to.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter.
	 * @param name The field name the suggestions relate to.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter.
	 * @return The resulting suggestions.
	 */
	public List<SuggestItem> getResults() {
		if(results == null) {
			results = new ArrayList<>();
		}
		return results;
	}
}
