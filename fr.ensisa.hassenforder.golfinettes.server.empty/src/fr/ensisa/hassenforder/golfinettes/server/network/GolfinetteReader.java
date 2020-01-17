package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.model.Battery;
import fr.ensisa.hassenforder.golfinettes.server.model.Battery.BatteryMode;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Location;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.UsageState;
import fr.ensisa.hassenforder.network.BasicAbstractReader;

public class GolfinetteReader extends BasicAbstractReader {

	private Event event;

	public GolfinetteReader(byte[] data) {
		super(new ByteArrayInputStream(data));
	}

	private int readAsByte() {
		return (int) (readByte() & 0xFF);
	}
	
	private float readFloatAsByte() {
		ByteBuffer b = ByteBuffer.allocate(4);
		for (int i = 0; i < 3; i++) {
			b.put(this.readByte());
		}
		b.put((byte) 0);
		return b.getFloat(0);
	}

	public Event readSigFoxStd () throws IOException {
		System.out.println("test");
		long id = this.readLong();
		long timeStamp = this.readLong();
		Location loc = this.readLocation();
		int load = this.readAsByte();
		BatteryMode mode = null;
		switch(this.readAsByte()) {
		case 1:
			mode = BatteryMode.FAST_CHARGING;
			break;
		case 2:
			mode = BatteryMode.PLUGGED_ONLY;
			break;
		case 3:
			mode = BatteryMode.SLOW_CHARGING;
			break;
		case 4:
			mode = BatteryMode.UNPLUGGED;
			break;
		}
		Battery battery = new Battery(Integer.MIN_VALUE, load, -1, -1, mode);
		BorrowerEvent borrowerEvent = this.readBorrowerEvent();
		UsageState usageState = this.readUsageState();
		Usage usage = new Usage(-1, borrowerEvent, usageState, -1, -1);
		return new Event(id, timeStamp, "sigfox").withLocation(loc).withBattery(battery).withUsage(usage);
	}
	
	private Location readLocation() {
		float latitude = this.readFloatAsByte();
		float longitude = this.readFloatAsByte();
		int temperature = this.readAsByte();
		int humidity = this.readAsByte();
		return new Location(latitude, longitude, temperature, humidity);
	}
	
	private BorrowerEvent readBorrowerEvent() {
		switch (this.readByte()) {
		case 1:
			return BorrowerEvent.FREE;
		case 2:
			return BorrowerEvent.BORROW;
		case 3:
			return BorrowerEvent.RETURN;
		default:
			return null;
		}
	}
	
	private UsageState readUsageState() {
		switch (this.readByte()) {
		case 1:
			return UsageState.STEADY_NORMAL;
		case 2:
			return UsageState.MOVING_NORMAL;
		case 3:
			return UsageState.STEADY_LONG;
		case 4:
			return UsageState.MOVING_BACK;
		default:
			return null;
		}
	}

	public void receive() throws IOException {
		type = readInt();
		switch (type) {
		case 0:
			break;
		case Protocol.SIGFOX_STD:
			event = readSigFoxStd();
			break;
		}
	}

	public Event getEvent() {
		return event;
	}

}
