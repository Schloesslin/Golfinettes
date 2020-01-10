package fr.ensisa.hassenforder.golfinettes.admin.model;

public class Golfinette {

	private String text;

	public Golfinette(String text) {
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
