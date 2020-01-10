package fr.ensisa.hassenforder.golfinettes.admin.ui;

public interface GUIListener {

	// networks
	void onGetGolfinetteList();
	void onGetEventList(long id, String kind);
	void onVersionSoftware();
	void onVersionMap();
	void onVersionUsers();

	// GUI
	void onClearGolfinetteList();
	void onClearEventList();
	
}
