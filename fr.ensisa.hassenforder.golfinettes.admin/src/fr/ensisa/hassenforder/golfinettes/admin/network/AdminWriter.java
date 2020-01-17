package fr.ensisa.hassenforder.golfinettes.admin.network;

import java.io.OutputStream;

import fr.ensisa.hassenforder.golfinettes.admin.model.Version;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class AdminWriter extends BasicAbstractWriter {

	public AdminWriter(OutputStream outputStream) {
		super(outputStream);
	}

	public void writeVersionSoftware(Version version) {
		this.writeInt(Protocol.SEND_UPDATE_SOFTWARE);
		this.writeString(version.getVersion());
		this.writeString(version.getFileContent1());
		int n = version.getFileContent2().length;
		this.writeInt(n);
		for (int i = 0; i < n; i++) {
			this.writeByte(version.getFileContent2()[i]);
		}

	}

	public void writeVersionMap(Version version) {
		this.writeInt(Protocol.SEND_UPDATE_MAP);
		this.writeString(version.getVersion());
		this.writeString(version.getFileContent1());
		int n = version.getFileContent2().length;
		this.writeInt(n);
		for (int i = 0; i < n; i++) {
			this.writeByte(version.getFileContent2()[i]);
		}

	}

	public void writeVersionUser(Version version) {
		this.writeInt(Protocol.SEND_UPDATE_USER);
		this.writeString(version.getVersion());
		this.writeString(version.getFileContent1());
		if (version.getFileContent2() != null) {
			int n = version.getFileContent2().length;
			this.writeInt(n);
			for (int i = 0; i < n; i++) {
				this.writeByte(version.getFileContent2()[i]);
			}
		} else {
			this.writeInt(0);
		}

	}
	
	public void writeEvent(long id, String kind) {
		this.writeInt(Protocol.RQ_WIFI_EVENT);
		this.writeLong(id);
		this.writeString(kind);
	}
	
	public void writeGolfinettes() {
		this.writeInt(Protocol.RQ_GOLFINETTES);
	}

}
