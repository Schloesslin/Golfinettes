package fr.ensisa.hassenforder.golfinettes.client.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private Version softwareVersion;
	private Version mapVersion;
	private Version usersVersion;
	private List<Event> events;
	
	public Version getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(Version softwareVersion) {
		if (softwareVersion == null) return;
		if (this.softwareVersion == null) this.softwareVersion = softwareVersion;
		if (softwareVersion.getVersion().equals(this.softwareVersion.getVersion())) return;
		this.softwareVersion = softwareVersion;
	}
	
	public Version getMapVersion() {
		return mapVersion;
	}
	
	public void setMapVersion(Version mapVersion) {
		if (mapVersion == null) return;
		if (this.mapVersion == null) this.mapVersion = mapVersion;
		if (mapVersion.getVersion().equals(this.mapVersion.getVersion())) return;
		this.mapVersion = mapVersion;
	}
	
	public Version getUsersVersion() {
		return usersVersion;
	}
	
	public void setUsersVersion(Version usersVersion) {
		if (usersVersion == null) return;
		if (this.usersVersion == null) this.usersVersion = usersVersion;
		if (usersVersion.getVersion().equals(this.usersVersion.getVersion())) return;
		this.usersVersion = usersVersion;
	}

	public List<Event> getEvents() {
		if (events == null) events = new ArrayList<> ();
		return events;
	}

	public void clearEvents() {
		if (events != null) events.clear();
	}
	
	public Event getLastEvent() {
		if (events == null) return null;
		return events.get(0);
	}
	
	public void addEvent (Event event) {
		if (events == null) events = new ArrayList<> ();
		events.add(0, event);
	}
	
	public void deleteEvent (Event event) {
		if (events == null) return;
		events.remove(event);
	}
	
}
