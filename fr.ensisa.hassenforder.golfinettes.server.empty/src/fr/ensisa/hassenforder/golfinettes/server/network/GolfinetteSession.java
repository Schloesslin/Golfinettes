package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.IOException;
import java.net.DatagramPacket;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.NetworkListener;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;

public class GolfinetteSession extends Thread {

	private DatagramPacket packet;
	private NetworkListener listener;
	
	public GolfinetteSession(DatagramPacket packet, NetworkListener listener) {
		this.packet = packet;
		this.listener = listener;
		if( listener == null) throw new RuntimeException("listener cannot be null");
	}

	public void close () {
		this.interrupt();
		packet = null;
	}

	private void processSigFoxStd(GolfinetteReader reader) {
		Event event = reader.getEvent();
		listener.addEvent(event);
	}

	public void operate() {
		try {
			GolfinetteReader reader = new GolfinetteReader (packet.getData());
			reader.receive ();
			switch (reader.getType ()) {
			case Protocol.SIGFOX_STD		: processSigFoxStd (reader); break;
			}
		} catch (IOException e) {
		}
	}

	public void run() {
		operate();
		packet = null;
	}

}
