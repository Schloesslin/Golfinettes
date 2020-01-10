package fr.ensisa.hassenforder.golfinettes.server;

import java.net.ServerSocket;
import java.net.Socket;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.network.WifiSession;

public class WifiServer extends Thread {

	private ServerSocket server = null;
	private NetworkListener listener = null;

	public WifiServer(NetworkListener listener) {
		super();
		this.listener = listener;
	}

	public void run () {
		try {
			server = new ServerSocket (Protocol.GOLFINETTES_WIFI_PORT);
			while (true) {
				Socket connection = server.accept();
				WifiSession session = new WifiSession (connection, listener);
				session.start ();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
