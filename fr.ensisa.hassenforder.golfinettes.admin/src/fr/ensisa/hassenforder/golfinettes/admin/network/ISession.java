package fr.ensisa.hassenforder.golfinettes.admin.network;

import java.util.List;

import fr.ensisa.hassenforder.golfinettes.admin.model.Event;
import fr.ensisa.hassenforder.golfinettes.admin.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.admin.model.Version;

/**
 *
 * @author hassenforder
 */
public interface ISession {

    boolean open ();

    boolean close ();

    String doSoftwareUpdate (Version version);
    String doMapUpdate (Version version);
    String doUsersUpdate (Version version);

	List<Golfinette> doGetGolfinetteList();

	List<Event> doGetEventList(long id, String kind);


}
