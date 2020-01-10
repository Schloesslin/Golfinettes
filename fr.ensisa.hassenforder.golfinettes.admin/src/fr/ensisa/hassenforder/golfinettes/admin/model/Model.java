package fr.ensisa.hassenforder.golfinettes.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {

	static Random rnd = new Random ();	
	private String softwareVersion;
	private String mapVersion;
	private String usersVersion;
	private List<Event> events;
	private List<Golfinette> golfinettes;
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	
	public String getMapVersion() {
		return mapVersion;
	}
	
	public void setMapVersion(String mapVersion) {
		this.mapVersion = mapVersion;
	}
	
	public String getUsersVersion() {
		return usersVersion;
	}
	
	public void setUsersVersion(String usersVersion) {
		this.usersVersion = usersVersion;
	}

	public void clearEvents() {
		if (this.events != null) this.events.clear();
	}
	
	public List<Event> getEvents() {
		if (this.events == null) this.events = new ArrayList<> ();
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void clearGolfinettes() {
		if (this.golfinettes != null) this.golfinettes.clear();
	}

	public List<Golfinette> getGolfinettes() {
		if (this.golfinettes == null) this.golfinettes = new ArrayList<> ();
		return this.golfinettes;
	}

	public void setGolfinettes(List<Golfinette> golfinettes) {
		this.golfinettes = golfinettes;
	}

	private Version generate (String head, double code) {
		String versionCode = Double.toString(code);
		StringBuilder content1 = new StringBuilder();
		content1.append(head);
		content1.append(" : ");
		content1.append(code);
		if (! "users".equals(head)) {
			return new Version (versionCode, content1.toString(), content1.toString().getBytes());
		} else {
			return new Version (versionCode, content1.toString(), null);
		}
	}
	
	public Version getNextUsersVersion() {
		Version version;
		if (usersVersion == null) {
			version = generate ("users", 1.0);
		} else {
			double v = Double.parseDouble(usersVersion);
			version = generate ("users", v+1.0);
		}
		return version;
	}

	public Version getNextMapVersion() {
		Version version;
		if (mapVersion == null) {
			version = generate ("map", 12.3);
		} else {
			double v = Double.parseDouble(mapVersion);
			version = generate ("map", v+0.1);
		}
		return version;
	}

	public Version getNextSoftwareVersion() {
		Version version;
		if (softwareVersion == null) {
			double v = rnd.nextDouble();
			v = ((int) (v*100))/ 10;
			version = generate ("software", v);
		} else {
			double v = Double.parseDouble(softwareVersion);
			double next = rnd.nextDouble();
			if (next < 0.5) v += 1.0;
			else v += 0.1;
			version = generate ("software", v);
		}
		return version;
	}
	
}
