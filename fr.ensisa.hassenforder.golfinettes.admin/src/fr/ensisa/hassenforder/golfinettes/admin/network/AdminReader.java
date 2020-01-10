package fr.ensisa.hassenforder.golfinettes.admin.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.admin.model.Event;
import fr.ensisa.hassenforder.golfinettes.admin.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractReader;

public class AdminReader extends BasicAbstractReader {

	private String version;
	private List<Event> events;
	private List<Golfinette> golfinettes;

	public AdminReader(InputStream inputStream) {
		super(inputStream);
	}

	public void receive() {
		type = readInt();
		version = null;
		events = null;
		golfinettes = null;
		switch (type) {
		case Protocol.RP_WIFI_EVENT:
			this.events = this.readAllEvents();
			break;
		}
	}

	public String getVersion() {
		return version;
	}

	public List<Event> getEvents() {
		return events;
	}

	public List<Golfinette> getGolfinettes() {
		return golfinettes;
	}

	public List<Event> readAllEvents() {
		List<Event> events = new ArrayList<Event>();
		int n = this.readInt();
		for (int i = 0; i < n; i++) {
			events.add(this.readEvent());
		}
		return events;
	}
	
	public long getEventId(Event e) {
		return this.readLong();
	}

	public Event readEvent() {
		return new Event(this.readString());
	}

}
