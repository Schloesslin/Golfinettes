package fr.ensisa.hassenforder.golfinettes.client.model;

import java.util.List;

public interface ModelListener {

	void notifyStatusChanged(String status);

	void notifyUsersVersionChanged(String version);

	void notifySoftwareVersionChanged(String version);

	void notifyMapVersionChanged(String version);

	void notifyEventListChanged(List<Event> events);

	void notifyPositionChanged(float latitude, float longitude);

	void notifyAmbientChanged(int temperature, int humidity);

	void notifyPowerChanged(int temperature, int load);

}
