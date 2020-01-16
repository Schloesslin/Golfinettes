package fr.ensisa.hassenforder.golfinettes.admin.model;

public class Event {

	private String text;
	
	public Event(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
