package fr.ensisa.hassenforder.golfinettes.client.network;

import java.util.List;

import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Version;

/**
 *
 * @author hassenforder
 */
public interface ISession {

    boolean open ();

    boolean close ();

    Version doSoftwareUpdate (String versionCode);
    Version doMapUpdate (String versionCode);
    Version doUsersUpdate (String versionCode);

	void doSigFox(Event lastEvent);
	boolean doWifi(List<Event> events);

}
