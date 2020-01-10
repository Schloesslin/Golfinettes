package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
	
	public GolfinetteReader(byte [] data) {
		super (new ByteArrayInputStream(data));
	}

	private int readAsByte() {
        return (int) (readByte() & 0xFF);
	}

	public Event readSigFoxStd () throws IOException {
		return null;
	}

	public void receive() throws IOException {
		type = readInt ();
		switch (type) {
		case 0 : break;
		case Protocol.SIGFOX_STD: 		event = readSigFoxStd(); break;
		}
	}

	public Event getEvent() {
		return event;
	}

}
