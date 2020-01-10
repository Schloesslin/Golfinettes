package fr.ensisa.hassenforder.golfinettes.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.network.GolfinetteSession;

public class GolfinetteServer extends Thread {

	private DatagramSocket server = null;
	private NetworkListener listener = null;

	public GolfinetteServer(NetworkListener listener) {
		super();
		this.listener = listener;
	}

	public void run () {
		try {
			server = new DatagramSocket(Protocol.GOLFINETTES_SIGFOX_PORT);
			while (true) {
				byte [] buffer = new byte [1500];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				server.receive(packet);
				GolfinetteSession session = new GolfinetteSession (packet, listener);
				session.start ();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
