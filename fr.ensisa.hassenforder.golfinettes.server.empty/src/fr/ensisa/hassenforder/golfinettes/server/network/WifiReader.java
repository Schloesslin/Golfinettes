package fr.ensisa.hassenforder.golfinettes.server.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.golfinettes.server.model.Battery;
import fr.ensisa.hassenforder.golfinettes.server.model.Battery.BatteryMode;
import fr.ensisa.hassenforder.golfinettes.server.model.Event;
import fr.ensisa.hassenforder.golfinettes.server.model.Location;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.server.model.Usage.UsageState;
import fr.ensisa.hassenforder.golfinettes.server.model.Version;
import fr.ensisa.hassenforder.network.BasicAbstractReader;

public class WifiReader extends BasicAbstractReader {

	private static String versionCodeSoftware;
	private static String versionCodeMap;
	private static String versionCodeUser;
	private List<Event> events;
	private static Version versionSoftware;
	private static Version versionMap;
	private static Version versionUser;
	private long id;	
	private String kind;
	
	public WifiReader(InputStream inputStream) {
		super (inputStream);
	}
	

	public void receive() {
		type = readInt ();
		switch (type) {
		case 0 : break;
		case Protocol.SEND_UPDATE_SOFTWARE :
			versionSoftware = this.readVersion();
			break;
		case Protocol.RQ_UPDATE_SOFTWARE :
			versionCodeSoftware = this.readVersionCode();
			break;
		case Protocol.SEND_UPDATE_MAP :
			versionMap = this.readVersion();
			break;
		case Protocol.RQ_UPDATE_MAP :
			versionCodeMap = this.readVersionCode();
			break;
		case Protocol.SEND_UPDATE_USER :
			versionUser = this.readVersion();
			break;
		case Protocol.RQ_UPDATE_USER :
			versionCodeUser = this.readVersionCode();
			break;
		
			//System.out.println(Float.parseFloat("2.5"));
		}
	}

	public String getVersionCodeSoftware() {
		return versionCodeSoftware;
	}

	public String getVersionCodeMap() {
		return versionCodeMap;
	}
	
	public String getVersionCodeUser() {
		return versionCodeUser;
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public Version getVersionSoftware() {
		return versionSoftware;
	}
	
	public Version getVersionMap() {
		return versionMap;
	}

	public Version getVersionUser() {
		return versionUser;
	}
	
	public long getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}
	
	public Version readVersion() {
		String versionCode = this.readString();
		String fileContent1 = this.readString();
		int n = this.readInt();
		if (n!=0) {
			byte[] fileContent2 = new byte[n];
			for (int i = 0; i<n; i++) {
				fileContent2[i]= this.readByte();
			}
			return new Version(versionCode, fileContent1, fileContent2);
		}
		else {
			return new Version(versionCode, fileContent1, null);
		}
		
		
	}
	
	public String readVersionCode() {
		return this.readString();
	}

}
