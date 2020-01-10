package fr.ensisa.hassenforder.golfinettes.server;

import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.server.model.Version;

public interface NetworkListener {

	Version getSoftwareVersion();
	Version getMapVersion();
	Version getUsersVersion();
	boolean putSoftwareVersion(Version version);
	boolean putMapVersion(Version version);
	boolean putUsersVersion(Version version);
	
	boolean addEvents(List<Event> event);
	boolean addEvent(Event event);
	Collection<Event> getEvents (long id, String kind);
	Collection<Golfinette> getGolfinettes ();
	
}
