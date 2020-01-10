package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.client.model.Battery;
import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Location;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class WifiWriter extends BasicAbstractWriter {

    public WifiWriter(OutputStream outputStream) {
        super(outputStream);
    }
    
    public void writeUpdateSoftware(String v) {
    	this.writeInt(Protocol.RQ_UPDATE_SOFTWARE);
    	if (v == null) {
    		this.writeString("");
    	}
    	else {
        	this.writeString(v);

    	}
    }

    public void writeUpdateMap(String v) {
    	this.writeInt(Protocol.RQ_UPDATE_MAP);
    	if (v == null) {
    		this.writeString("");
    	}
    	else {
        	this.writeString(v);

    	}
    }
    
    public void writeUpdateUser(String v) {
    	this.writeInt(Protocol.RQ_UPDATE_USER);
    	if (v == null) {
    		this.writeString("");
    	}
    	else {
        	this.writeString(v);

    	}
    }
}
