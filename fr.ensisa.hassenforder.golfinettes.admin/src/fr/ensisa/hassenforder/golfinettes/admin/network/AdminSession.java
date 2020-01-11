package fr.ensisa.hassenforder.golfinettes.admin.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.admin.model.Event;
import fr.ensisa.hassenforder.golfinettes.admin.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.admin.model.Version;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;

public class AdminSession implements ISession {

    private Socket wifi;

    public AdminSession() {
    }

    @Override
    synchronized public boolean close() {
        try {
            if (wifi != null) {
                wifi.close();
            }
            wifi = null;
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    synchronized public boolean open() {
        this.close();
        try {
            wifi = new Socket("localhost", Protocol.GOLFINETTES_WIFI_PORT);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

	@Override
	public String doSoftwareUpdate(Version version) {
		try {
			AdminWriter aw = new AdminWriter(wifi.getOutputStream());
			aw.writeVersionSoftware(version);
			aw.send();
			return version.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String doMapUpdate(Version version) {
		try {
			AdminWriter aw = new AdminWriter(wifi.getOutputStream());
			aw.writeVersionMap(version);
			aw.send();
			return version.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String doUsersUpdate(Version version) {
		try {

			AdminWriter aw = new AdminWriter(wifi.getOutputStream());
			aw.writeVersionUser(version);
			aw.send();
			return version.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Golfinette> doGetGolfinetteList() {
		try {
			

			AdminWriter aw = new AdminWriter(this.wifi.getOutputStream());
			aw.writeGolfinettes();
			aw.send();
			
			AdminReader ar = new AdminReader(this.wifi.getInputStream());
			ar.receive();

			List<Golfinette> golfinettes = ar.getGolfinettes();

			return golfinettes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Event> doGetEventList(long id, String kind) {
		try {
			AdminWriter aw = new AdminWriter(this.wifi.getOutputStream());
			aw.writeEvent();
			aw.send();
			AdminReader ar = new AdminReader(this.wifi.getInputStream());
			ar.receive();
			List<Event> allEvents = ar.getEvents();
			List<Event> events = new ArrayList<Event>();
			System.out.println(kind);

			for (Event e : allEvents) {
				
				if ((id == -1 || e.getId() == id) && (e.getKind().equals(kind) || kind.equals(""))) {

					events.add(e);
				}
				
			}
			//System.out.println(events);
			//System.out.println(events.size());
			return events;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

 }
