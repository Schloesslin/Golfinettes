package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import fr.ensisa.hassenforder.golfinettes.client.model.Battery;
import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Location;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.UsageState;
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

	private void writeFloatAsBytes(float value) {
		byte[] b = ByteBuffer.allocate(4).putFloat(value).array();
		for (int i = 0; i < 3; i++) {
			writeByte(b[i]);
		}
	}

	private void writeIntsAsByte(int a, int b) {
		writeAsByte(((a & 0xF) << 4) | (b & 0xF));
	}

	private void writeBattery(Battery battery) {
		this.writeAsByte(battery.getTemperature()); // 1
		this.writeAsByte(battery.getLoad()); // 2
		this.writeAsByte(battery.getDischargeCurrent()); // 3
		switch (battery.getMode()) { // 4
		case FAST_CHARGING:
			this.writeIntsAsByte(battery.getLoadingCurrent(), 1);
			break;
		case PLUGGED_ONLY:
			this.writeIntsAsByte(battery.getLoadingCurrent(), 2);
			break;
		case SLOW_CHARGING:
			this.writeIntsAsByte(battery.getLoadingCurrent(), 3);
			break;
		case UNPLUGGED:
			this.writeIntsAsByte(battery.getLoadingCurrent(), 4);
			break;
		}
	}

	private void writeLocation(Location location) {
		this.writeFloatAsBytes(location.getLatitude()); // 3
		this.writeFloatAsBytes(location.getLongitude()); // 6
		this.writeAsByte(location.getTemperature()); // 7
		this.writeAsByte(location.getHumidity()); // 8
	}

	private void writeUsage(Usage usage) {
		int usageState = 0;
		switch (usage.getUsage()) {
		case STEADY_NORMAL:
			usageState = 1;
			break;
		case STEADY_LONG:
			usageState = 2;
			break;
		case MOVING_NORMAL:
			usageState = 3;
			break;
		case MOVING_BACK:
			usageState = 4;
			break;
		}
		switch (usage.getEvent()) { // 3
		case FREE:
			this.writeIntsAsByte(usageState, 1);
			break;
		case BORROW:
			this.writeIntsAsByte(usageState, 2);
			this.writeShort((short) usage.getBorrower());
			break;
		case RETURN:
			this.writeIntsAsByte(usageState, 3);
			this.writeShort((short) usage.getBorrower());
			break;
		}
		this.writeAsByte(usage.getDetail()); // 4
	}

	public void createSigFoxStd(Event lastEvent) {
		this.writeInt(Protocol.SIGFOX_STD);
		this.writeLong(lastEvent.getId());
		this.writeLong(lastEvent.getTimestamp().getTime());
		this.writeLocation(lastEvent.getLocation());
		this.writeUsage(lastEvent.getUsage());
	}

	public void createMessageX(Event lastEvent) {
		this.writeInt(Protocol.MESSAGE_X);
		this.writeLong(lastEvent.getId());
		this.writeLong(lastEvent.getTimestamp().getTime());
		this.writeLocation(lastEvent.getLocation());
		this.writeBattery(lastEvent.getBattery());
	}

	public void createMessageY(Event lastEvent) {
		this.writeInt(Protocol.MESSAGE_Y);
		this.writeLong(lastEvent.getId());
		this.writeLong(lastEvent.getTimestamp().getTime());
		this.writeBattery(lastEvent.getBattery());
		this.writeUsage(lastEvent.getUsage());
		this.writeShort((short) lastEvent.getLocation().getTemperature());
		this.writeShort((short) lastEvent.getLocation().getHumidity());
	}

	public void createAlarm(Event lastEvent) {
		this.writeInt(Protocol.ALARME);
		this.writeLong(lastEvent.getId());
		this.writeLong(lastEvent.getTimestamp().getTime());
		this.writeAsByte(lastEvent.getUsage().getAlarm());
		this.writeFloatAsBytes(lastEvent.getLocation().getLatitude());
		this.writeFloatAsBytes(lastEvent.getLocation().getLongitude());
		Battery battery = lastEvent.getBattery();
		this.writeAsByte(lastEvent.getLocation().getTemperature());
		this.writeAsByte(battery.getTemperature()); // 1
		this.writeAsByte(battery.getLoad()); // 2
		this.writeAsByte(battery.getDischargeCurrent()); // 3
		int modeValue = 0;
		switch (lastEvent.getBattery().getMode()) { // 4
		case FAST_CHARGING:
			modeValue = 0;
			break;
		case PLUGGED_ONLY:
			modeValue = 1;
			break;
		case SLOW_CHARGING:
			modeValue = 2;
			break;
		case UNPLUGGED:
			modeValue = 3;
			break;
		}
		modeValue <<= 2;
		switch (lastEvent.getUsage().getEvent()) {
		case BORROW:
			modeValue += 0;
			break;
		case FREE:
			modeValue += 1;
			break;
		case RETURN:
			modeValue += 2;
			break;
		}
		modeValue <<= 2;
		switch (lastEvent.getUsage().getUsage()) {
		case MOVING_BACK:
			modeValue += 0;
			break;
		case MOVING_NORMAL:
			modeValue += 1;
			break;
		case STEADY_LONG:
			modeValue += 2;
			break;
		case STEADY_NORMAL:
			modeValue += 3;
			break;
		}
		this.writeAsByte(modeValue);
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
