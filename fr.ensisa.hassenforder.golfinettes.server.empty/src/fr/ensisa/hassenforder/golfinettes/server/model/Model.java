package fr.ensisa.hassenforder.golfinettes.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author hassenforder
 */
public class Model {

	private Version softwareVersion;
	private Version mapVersion;
	private Version usersVersion;
	private Map<Long, List<Event>> events;
	
	public void populate () {
	}

	public Version getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(Version softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public Version getMapVersion() {
		return mapVersion;
	}

	public void setMapVersion(Version mapVersion) {
		this.mapVersion = mapVersion;
	}

	public Version getUsersVersion() {
		return usersVersion;
	}

	public void setUsersVersion(Version usersVersion) {
		this.usersVersion = usersVersion;
	}

	public Map<Long, List<Event>> getEvents() {
		if (events == null) events = new TreeMap<>();
		return events;
	}

	public void addEvent(Event event) {
		Map<Long, List<Event>> all = getEvents();
		if (all.containsKey(event.getId()))  {
			List<Event> events = all.get(event.getId());
			events.add(event);
		} else {
			List<Event> events = new ArrayList<Event>();
			events.add(event);
			all.put(event.getId(), events);
		}
	}

}
