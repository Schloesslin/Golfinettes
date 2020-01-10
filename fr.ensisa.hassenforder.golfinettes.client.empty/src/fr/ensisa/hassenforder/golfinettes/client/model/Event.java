package fr.ensisa.hassenforder.golfinettes.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

	private long id;
	private Date timestamp;
	private Location location;
	private Battery battery;
	private Usage usage;
	
	public Event(long id) {
		super();
		this.id = id;
		this.timestamp = new Date();
	} 
	
	public Event withLocation(Location location) {
		this.location = location;
		return this;
	}

	public Event withBattery(Battery battery) {
		this.battery = battery;
		return this;
	}

	public Event withUsage(Usage usage) {
		this.usage = usage;
		return this;
	}

	public long getId() {
		return id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public Location getLocation() {
		return location;
	}
	public Battery getBattery() {
		return battery;
	}
	public Usage getUsage() {
		return usage;
	}

	static private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(" | ");
		builder.append(sdf.format(timestamp));
		builder.append(location);
		builder.append(battery);
		builder.append(usage);
		return builder.toString();
	}
	
}
