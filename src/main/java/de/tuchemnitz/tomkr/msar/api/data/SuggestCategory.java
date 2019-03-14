package de.tuchemnitz.tomkr.msar.api.data;

import java.util.ArrayList;
import java.util.List;

public class SuggestCategory {

	private String name;
	private List<SuggestItem> results;
	
	public SuggestCategory(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SuggestItem> getResults() {
		if(results == null) {
			results = new ArrayList<>();
		}
		return results;
	}
	public void setResults(List<SuggestItem> results) {
		this.results = results;
	}
}
