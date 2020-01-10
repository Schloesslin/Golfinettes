package fr.ensisa.hassenforder.golfinettes.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.server.model.Model;
import fr.ensisa.hassenforder.golfinettes.server.model.Version;

public class Application implements NetworkListener {

	private GolfinetteServer golfinette = null;
	private WifiServer wifi = null;
	private Model model = null;
	
	public void start () {
		model = new Model ();
		model.populate();
		golfinette = new GolfinetteServer(this);
		golfinette.start();
		wifi = new WifiServer(this);
		wifi.start();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application m = new Application ();
		m.start();
	}

	@Override
	public Version getSoftwareVersion() {
		return model.getSoftwareVersion();
	}

	@Override
	public Version getMapVersion() {
		return model.getMapVersion();
	}

	@Override
	public Version getUsersVersion() {
		return model.getUsersVersion();
	}

	@Override
	public boolean addEvents(List<Event> events) {
		for (Event e : events) {
			model.addEvent (e);
		}
		return true;
	}

	@Override
	public boolean addEvent(Event event) {
		model.addEvent (event);
		return true;
	}

	@Override
	public boolean putSoftwareVersion(Version version) {
		model.setSoftwareVersion(version);
		return true;
	}

	@Override
	public boolean putMapVersion(Version version) {
		model.setMapVersion(version);
		return true;
	}

	@Override
	public boolean putUsersVersion(Version version) {
		model.setUsersVersion(version);
		return true;
	}

	@Override
	public Collection<Event> getEvents(long id, String kind) {
		Map<Long, List<Event>> all = model.getEvents();
		List<Event> events = new ArrayList<Event>();
		if (kind == null) kind = "";
		if (id == -1 && kind.equals("")) {
			for (List<Event> some : all.values()) {
				events.addAll(some);
			}
		} else if (id == -1 && ! kind.equals("")) {
			for (List<Event> some : all.values()) {
				for (Event one : some) {
					if (one.getKind().contains(kind)) events.add(one);
				}
			}
		} else if (id != -1 && kind.equals("")) {
			if (all.containsKey(id)) {
				events.addAll(all.get(id));
			}
		} else if (id != -1 && ! kind.equals("")) {
			if (all.containsKey(id)) {
				for (Event one : all.get(id)) {
					if (one.getKind().contains(kind)) events.add(one);
				}
			}
		}
		Collections.sort(events, new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				if (o1.getTimestamp().getTime() < o2.getTimestamp().getTime()) return -1;
				if (o1.getTimestamp().getTime() > o2.getTimestamp().getTime()) return 1;
				return 0;
			}
		});
		return events;
	}

	@Override
	public Collection<Golfinette> getGolfinettes() {
		Map<Long, List<Event>> all = model.getEvents();
		List<Golfinette> golfinettes = new ArrayList<Golfinette>();
		for (Entry<Long, List<Event>> entry : all.entrySet()) {
			Map<String, Integer> counts = new TreeMap<>();
			for (Event event : entry.getValue()) {
				int count = 0;
				if (counts.containsKey(event.getKind())) {
					count = counts.get(event.getKind());
				}
				++count;
				counts.put(event.getKind(), count);
			}
			Golfinette golfinette = new Golfinette (entry.getKey(), counts);
			golfinettes.add(golfinette);
		}
		return golfinettes;
	}

}
