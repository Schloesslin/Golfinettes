package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.UsageState;
import fr.ensisa.hassenforder.golfinettes.client.model.Version;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;

public class GolfinetteSession implements ISession {

    private Socket wifi;

    public GolfinetteSession() {
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
	public Version doSoftwareUpdate(String versionCode) {
		try {
			WifiWriter ww = new WifiWriter(wifi.getOutputStream());
			ww.writeUpdateSoftware(versionCode);
			ww.send();
			WifiReader wr = new WifiReader(wifi.getInputStream());
			wr.receive();
			return wr.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Version doMapUpdate(String versionCode) {
		try {
			WifiWriter ww = new WifiWriter(wifi.getOutputStream());
			ww.writeUpdateMap(versionCode);
			ww.send();
			WifiReader wr = new WifiReader(wifi.getInputStream());
			wr.receive();
			return wr.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Version doUsersUpdate(String versionCode) {
		try {
			WifiWriter ww = new WifiWriter(wifi.getOutputStream());
			ww.writeUpdateUser(versionCode);
			ww.send();
			WifiReader wr = new WifiReader(wifi.getInputStream());
			wr.receive();
			return wr.getVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean doWifi(List<Event> events) {
		return false;
	}

	private enum Decision {
		SIGFOX_STD,
	}

	private Decision decisionTaker (Event last) {
		return Decision.SIGFOX_STD;
	}

	@Override
	public void doSigFox(Event lastEvent) {
//        try {
            GolfinetteWriter w = new GolfinetteWriter("localhost", Protocol.GOLFINETTES_SIGFOX_PORT);
            switch (decisionTaker(lastEvent)) {
            case SIGFOX_STD		: w.createSigFoxStd (lastEvent); break;
            }
            w.send();
//        } catch (IOException e) {
//        }
	}

 }
