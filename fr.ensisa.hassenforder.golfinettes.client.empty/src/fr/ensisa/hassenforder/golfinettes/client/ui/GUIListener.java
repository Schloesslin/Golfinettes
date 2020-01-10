package fr.ensisa.hassenforder.golfinettes.client.ui;

import fr.ensisa.hassenforder.golfinettes.client.model.Event;

public interface GUIListener {

	// networks
	void onDoSoftwareUpdate();
	void onDoUsersUpdate();
	void onDoMapUpdate();
	void onDoSigFox();
	void onDoWifi();

	// model
	void onVersionSoftware();
	void onVersionMap();
	void onVersionUsers();

	// model/events
	void onClearEventList();
	void onAddEvent(Event event);
	void onDeleteEvent(Event event);
	
	// helpers
	void onNextPosition();
	void onNextAmbient();
	void onNextPower();

}
