package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.NetworkListener;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.server.model.Version;


public class WifiSession extends Thread {

	private Socket connection;
	private NetworkListener listener;
	public WifiSession(Socket connection, NetworkListener listener) {
		this.connection = connection;
		this.listener = listener;
		if( listener == null) throw new RuntimeException("listener cannot be null");
	}

	public void close () {
		this.interrupt();
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

	public boolean operate() {
		try {
			WifiWriter writer = new WifiWriter (connection.getOutputStream());
			WifiReader reader = new WifiReader (connection.getInputStream());
			reader.receive ();
			//System.out.println(reader.getType());
			switch (reader.getType ()) {
			case 0 : return false; // socket closed
			case 666 : break; // to remove, inserted to hide error
			case Protocol.RQ_UPDATE_SOFTWARE:
				writer.writeVersionSoftware(listener.getSoftwareVersion());
				break;
			case Protocol.RQ_UPDATE_MAP :
				writer.writeVersionMap(listener.getMapVersion());
				break;
			case Protocol.RQ_UPDATE_USER :
				writer.writeVersionUser(listener.getUsersVersion());
				break;
			case Protocol.SEND_UPDATE_SOFTWARE:
				listener.putSoftwareVersion(reader.getVersion());
				break;
			case Protocol.SEND_UPDATE_MAP :
				listener.putMapVersion(reader.getVersion());
				break;
			case Protocol.SEND_UPDATE_USER :
				listener.putUsersVersion(reader.getVersion());
				break;
			case Protocol.SEND_WIFI_EVENT:
				return true;
			case Protocol.RQ_WIFI_EVENT:
				
				writer.writeAllEvents(reader.getEvents());
				break;
			case Protocol.RQ_GOLFINETTES:
				//System.out.println(reader.getGolfinettes());
				writer.writeGolfinette(listener.getGolfinettes());
				break;
			default: return false; // connection jammed
			}
			writer.send ();
			return true;
		} catch (IOException e) {
			return false;
		}
	}


	public void run() {
		while (true) {
			if (! operate())
				break;
		}
		try {
			if (connection != null) connection.close();
		} catch (IOException e) {
		}
	}

}
