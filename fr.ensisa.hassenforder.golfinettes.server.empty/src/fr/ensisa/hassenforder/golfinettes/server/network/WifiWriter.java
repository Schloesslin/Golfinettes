package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.model.Battery.BatteryMode;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Golfinette;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.UsageState;
import fr.ensisa.hassenforder.golfinettes.server.model.Version;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class WifiWriter extends BasicAbstractWriter {

	public WifiWriter(OutputStream outputStream) {
		super (outputStream);
	}
	
	
	public void writeVersionSoftware(Version version) {
    	this.writeInt(Protocol.RP_UPDATE_SOFTWARE);
    	this.writeString(version.getVersion());
    	this.writeString(version.getFileContent1());
    	this.writeInt(version.getFileContent2().length);
    	for (byte b : version.getFileContent2()) {
    		this.writeByte(b);
    	}
    	
    }
	
	public void writeVersionMap(Version version) {
    	this.writeInt(Protocol.RP_UPDATE_MAP);
    	this.writeString(version.getVersion());
    	this.writeString(version.getFileContent1());
    	this.writeInt(version.getFileContent2().length);
    	for (byte b : version.getFileContent2()) {
    		this.writeByte(b);
    	}
    	
    }

	public void writeVersionUser(Version version) {
    	this.writeInt(Protocol.RP_UPDATE_USER);
    	this.writeString(version.getVersion());
    	this.writeString(version.getFileContent1());
    	if (version.getFileContent2() != null) {
    		int n = version.getFileContent2().length;
        	this.writeInt(n);
        	for (int i = 0; i < n; i++) {
        		this.writeByte(version.getFileContent2()[i]);
        	}
    	}
    	else {
    		this.writeInt(0);
    	}
    	
    }
	/*
	public void writeEvent(Event e) {
		this.writeInt(Protocol.RP_WIFI_EVENT);
    	if (e != null) {
    		this.writeLong(e.getId());
    		this.writeLong(e.getTimestamp().getTime());
    		this.writeFloat(e.getLocation().getLatitude());
    		this.writeFloat(e.getLocation().getLongitude());
    		this.writeInt(e.getLocation().getTemperature());
    		this.writeInt(e.getLocation().getHumidity());
    		BatteryMode bm = e.getBattery().getMode();
    		switch(bm) {
    		case UNPLUGGED:
    			this.writeByte((byte) 0);
    			break;
    		case PLUGGED_ONLY :
    			this.writeByte((byte) 1);
    			break;
    		case SLOW_CHARGING :
    			this.writeByte((byte) 2);
    			break;
    		case FAST_CHARGING :
    			this.writeByte((byte) 3);
    			break;
    		}
    		this.writeInt(e.getBattery().getLoad());
    		this.writeInt(e.getBattery().getLoadingCurrent());
    		this.writeInt(e.getBattery().getDischargeCurrent());
    		this.writeInt(e.getBattery().getTemperature());
    		BorrowerEvent be = e.getUsage().getEvent();
    		switch(be) {
    		case FREE:
    			this.writeByte((byte) 0);
    			break;
    		case BORROW :
    			this.writeByte((byte) 1);
    			break;
    		case RETURN :
    			this.writeByte((byte) 2);
    			break;
    		
    		}
    		UsageState us = e.getUsage().getUsage();
    		switch(us) {
    		case STEADY_NORMAL:
    			this.writeByte((byte) 0);
    			break;
    		case STEADY_LONG :
    			this.writeByte((byte) 1);
    			break;
    		case MOVING_NORMAL :
    			this.writeByte((byte) 2);
    			break;
    		case MOVING_BACK :
    			this.writeByte((byte) 3);
    			break;
    		}
    		this.writeInt(e.getUsage().getDetail());
    		this.writeInt(e.getUsage().getAlarm());
    	}
	*/
	
	public void writeAllEvents(List<Event> events) {
		this.writeInt(Protocol.RP_WIFI_EVENT);
		this.writeInt(events.size());
		for (Event event : events) {
			this.writeString(event.toString());
		}
	}
	
	public void writeGolfinette(List<Golfinette> golfinette) {
		//System.out.println("iciiii");
		this.writeInt(Protocol.RP_GOLFINETTES);
		this.writeInt(golfinette.size());
		for (Golfinette g : golfinette) {
			this.writeString(g.toString());
		}
		
	}

}
