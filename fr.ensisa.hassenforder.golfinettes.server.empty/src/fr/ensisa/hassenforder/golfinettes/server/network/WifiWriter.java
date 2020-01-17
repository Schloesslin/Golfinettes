package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Golfinette;
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
	
	public void writeAllEvents(Collection<Event> events) {
		this.writeInt(Protocol.RP_WIFI_EVENT);
		this.writeInt(events.size());
		for (Event event : events) {
			this.writeString(event.toString());
		}
	}
	
	public void writeGolfinette(Collection<Golfinette> golfinette) {
		this.writeInt(Protocol.RP_GOLFINETTES);
		this.writeInt(golfinette.size());
		for (Golfinette g : golfinette) {
			this.writeString(g.toString());
		}
	}

}
