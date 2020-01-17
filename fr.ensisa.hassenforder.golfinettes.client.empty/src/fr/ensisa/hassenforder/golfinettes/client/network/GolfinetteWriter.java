package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import fr.ensisa.hassenforder.golfinettes.client.model.Battery;
import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Location;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class GolfinetteWriter extends BasicAbstractWriter {

	private String host;
	private int port;

	public GolfinetteWriter(String host, int port) {
		super(null);
		this.host = host;
		this.port = port;
	}

	private void writeAsByte(int value) {
		writeByte((byte) (value & 0xFF));
	}

	private void writeAsByte(float value) {
		byte[] b = ByteBuffer.allocate(4).putFloat(value).array();
		for (int i = 0; i < 3; i++) {
			writeByte(b[i]);
		}
	}
	
	private void writeLocation(Location location) { // 8
		this.writeAsByte(location.getLatitude());
		this.writeAsByte(location.getLongitude());
		this.writeAsByte(location.getTemperature());
		this.writeAsByte(location.getHumidity());
	}

	public void createSigFoxStd(Event lastEvent) {
		this.writeInt(1);
		this.writeLong(lastEvent.getId());
		this.writeLong(lastEvent.getTimestamp().getTime());
		this.writeLocation(lastEvent.getLocation()); // 8
		this.writeAsByte(lastEvent.getBattery().getLoad()); // 9
		//this.writeAsByte(lastEvent.getBattery().getTemperature()); // 10
		switch(lastEvent.getBattery().getMode()) {
			case FAST_CHARGING:
				this.writeAsByte(1);
				break;
			case PLUGGED_ONLY:
				this.writeAsByte(2);
				break;
			case SLOW_CHARGING:
				this.writeAsByte(3);
				break;
			case UNPLUGGED:
				this.writeAsByte(4);
				break;
			
		}
		switch (lastEvent.getUsage().getEvent()) { // 11
		case FREE:
			this.writeAsByte(1);
			break;
		case BORROW:
			this.writeAsByte(2);
			break;
		case RETURN:
			this.writeAsByte(3);
			break;
		}
		switch (lastEvent.getUsage().getUsage()) { // 12
		case STEADY_NORMAL:
			this.writeAsByte(1);
			break;
		case MOVING_NORMAL:
			this.writeAsByte(2);
			break;
		case STEADY_LONG:
			this.writeAsByte(3);
			break;
		case MOVING_BACK:
			this.writeAsByte(4);
			break;
		}
	}

	public void createMessageX(Event lastEvent) {

	}

	public void createAlarm(Event lastEvent) {
		
	}
	
	public void createMesaageY(Event lastEvent) {
		
	}
	
	@Override
	public void send() {
		byte[] message = baos.toByteArray();
		DatagramSocket socket = null;
		try {
			InetAddress target = InetAddress.getByName(host);
			DatagramPacket packet = new DatagramPacket(message, message.length, target, port);
			System.out.println("SIGFOX packet sent with : " + (message.length) + " bytes all inclusive");
			System.out.println(
					"SIGFOX packet sent with : " + (message.length - 20) + " bytes without type, id and timestamp");
			socket = new DatagramSocket();
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				socket.close();
		}
	}

}
