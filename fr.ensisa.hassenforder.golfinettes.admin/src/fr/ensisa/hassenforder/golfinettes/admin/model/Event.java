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
	
	public long getId() {
		String[] result = this.text.split("\\s");
		return Long.parseLong(result[0]);
	}

	public String getKind() {
		String[] result = this.text.split("\\s");
		return result[1];
	}
	
	@Override
	public String toString() {
		return text;
	}

}
