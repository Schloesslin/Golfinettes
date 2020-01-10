package fr.ensisa.hassenforder.golfinettes.admin.model;

import java.util.List;

public interface ModelListener {

	void notifyStatusChanged(String status);

	void notifyUsersVersionChanged(String version);

	void notifySoftwareVersionChanged(String version);

	void notifyMapVersionChanged(String version);

	void notifyEventListChanged(List<Event> events);

	void notifyGolfinetteListChanged(List<Golfinette> golfinettes);

}
