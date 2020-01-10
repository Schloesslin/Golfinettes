package fr.ensisa.hassenforder.golfinettes.server.model;

import java.util.Map;

public class Golfinette {
	
	private long id;
	private Map<String, Integer> counts;
	
	public Golfinette(long id, Map<String, Integer> counts) {
		super();
		this.id = id;
		this.counts = counts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Golfinette [id=");
		builder.append(id);
		builder.append(", counts=");
		builder.append(counts);
		builder.append("]");
		return builder.toString();
	}
	
}
