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

	public Event readSigFoxStd() throws IOException {
		long id = this.readLong();
		long timeStamp = this.readLong();
		Location loc = this.readLocation();
		Usage usage = this.readUsage();
		return new Event(id, timeStamp, "sigfox").withLocation(loc).withUsage(usage);
	}

	public Event readMessageX() throws IOException {
		long id = this.readLong();
		long timeStamp = this.readLong();
		Location loc = this.readLocation();
		Battery battery = this.readBattery();
		return new Event(id, timeStamp, "sigfox").withLocation(loc).withBattery(battery);
	}

	public Event readMessageY() throws IOException {
		long id = this.readLong();
		long timeStamp = this.readLong();
		Battery battery = this.readBattery();
		Usage usage = this.readUsage();
		int temperature = this.readShort();
		int humidity = this.readShort();
		Location loc = new Location(Float.NaN, Float.NaN, temperature, humidity);
		return new Event(id, timeStamp, "sigfox").withLocation(loc).withBattery(battery).withUsage(usage);
	}

	private Event readAlarme() throws IOException {
		long id = this.readLong();
		long timeStamp = this.readLong();
		int alarm = this.readAsByte();
		float latitude = this.readFloatAsByte();
		float longitude = this.readFloatAsByte();
		int temperature = this.readAsByte();
		Location loc = new Location(latitude, longitude, temperature, -1);
		int temperatureBattery = this.readAsByte();
		int load = this.readAsByte();
		int dischargeCurrent = this.readAsByte();
		int b = this.readAsByte();
		int modeValue = (b >> 4) & 0xf;
		BatteryMode mode = null;
		switch (modeValue) {
		case 0:
			mode = BatteryMode.FAST_CHARGING;
			break;
		case 1:
			mode = BatteryMode.PLUGGED_ONLY;
			break;
		case 2:
			mode = BatteryMode.SLOW_CHARGING;
			break;
		case 3:
			mode = BatteryMode.UNPLUGGED;
			break;
		}
		int borrowerValue = (b >> 2) & 0x3;
		BorrowerEvent borrowerEvent = null;
		switch (borrowerValue) {
		case 0:
			borrowerEvent = BorrowerEvent.BORROW;
			break;
		case 1:
			borrowerEvent = BorrowerEvent.FREE;
			break;
		case 2:
			borrowerEvent = BorrowerEvent.RETURN;
			break;
		}
		int usageValue = b & 0x3;
		UsageState usageState = null;
		switch (usageValue) {
		case 0:
			usageState = UsageState.MOVING_BACK;
			break;
		case 1:
			usageState = UsageState.MOVING_NORMAL;
			break;
		case 2:
			usageState = UsageState.STEADY_LONG;
			break;
		case 3:
			usageState = UsageState.STEADY_NORMAL;
			break;
		}
		Battery battery = new Battery(temperatureBattery, load, -1, dischargeCurrent, mode);
		Usage usage = new Usage(-1, borrowerEvent, usageState, 0, alarm);
		return new Event(id, timeStamp, "sigfox").withLocation(loc).withBattery(battery).withUsage(usage);
	}

	private Battery readBattery() {
		int temperature = this.readAsByte();
		int load = this.readAsByte();
		int dischargeCurrent = this.readAsByte();
		int b = this.readAsByte();
		int loadingCurrent = (b >> 4) & 0xf;
		int modeValue = b & 0xf;
		BatteryMode mode = null;
		switch (modeValue) {
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
		return new Battery(temperature, load, loadingCurrent, dischargeCurrent, mode);
	}

	private Location readLocation() {
		float latitude = this.readFloatAsByte();
		float longitude = this.readFloatAsByte();
		int temperature = this.readAsByte();
		int humidity = this.readAsByte();
		return new Location(latitude, longitude, temperature, humidity);
	}

	private Usage readUsage() {
		int b = this.readAsByte();
		int eventValue = b & 0xf;
		int usageValue = (b >> 4) & 0xf;
		UsageState usage = null;
		switch (usageValue) {
		case 1:
			usage = UsageState.STEADY_NORMAL;
			break;
		case 2:
			usage = UsageState.STEADY_LONG;
			break;
		case 3:
			usage = UsageState.MOVING_NORMAL;
			break;
		case 4:
			usage = UsageState.MOVING_BACK;
			break;
		}
		long borrower = 0;
		BorrowerEvent event = null;
		switch (eventValue) {
		case 1:
			event = BorrowerEvent.FREE;
			break;
		case 2:
			event = BorrowerEvent.BORROW;
			borrower = (long) this.readShort();
			break;
		case 3:
			event = BorrowerEvent.RETURN;
			borrower = (long) this.readShort();
			break;
		}
		int detail = this.readAsByte();
		return new Usage(borrower, event, usage, detail, 0);
	}

	public void receive() throws IOException {
		type = readInt();
		switch (type) {
		case 0:
			break;
		case Protocol.SIGFOX_STD:
			event = readSigFoxStd();
			break;
		case Protocol.MESSAGE_X:
			event = readMessageX();
			break;
		case Protocol.MESSAGE_Y:
			event = readMessageY();
			break;
		case Protocol.ALARME:
			event = readAlarme();
			break;
		}
	}

	public Event getEvent() {
		return event;
	}

}
