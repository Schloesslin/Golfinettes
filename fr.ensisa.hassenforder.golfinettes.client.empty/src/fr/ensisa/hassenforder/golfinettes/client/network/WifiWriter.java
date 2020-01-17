package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.golfinettes.client.model.Battery.BatteryMode;
import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.BorrowerEvent;
import fr.ensisa.hassenforder.golfinettes.client.model.Usage.UsageState;
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
		} else {
			this.writeString(v);
		}
	}

	public void writeUpdateMap(String v) {
		this.writeInt(Protocol.RQ_UPDATE_MAP);
		if (v == null) {
			this.writeString("");
		} else {
			this.writeString(v);
		}
	}

	public void writeUpdateUser(String v) {
		this.writeInt(Protocol.RQ_UPDATE_USER);
		if (v == null) {
			this.writeString("");
		} else {
			this.writeString(v);
		}
	}

	public void writeAllEvents(List<Event> events) {
		this.writeInt(Protocol.SEND_WIFI_EVENT);
		this.writeInt(events.size());
		for (Event e : events) {
			if (e != null) {
				this.writeLong(e.getId());
				this.writeLong(e.getTimestamp().getTime());
				this.writeFloat(e.getLocation().getLatitude());
				this.writeFloat(e.getLocation().getLongitude());
				this.writeInt(e.getLocation().getTemperature());
				this.writeInt(e.getLocation().getHumidity());
				BatteryMode bm = e.getBattery().getMode();
				switch (bm) {
				case UNPLUGGED:
					this.writeByte((byte) 0);
					break;
				case PLUGGED_ONLY:
					this.writeByte((byte) 1);
					break;
				case SLOW_CHARGING:
					this.writeByte((byte) 2);
					break;
				case FAST_CHARGING:
					this.writeByte((byte) 3);
					break;
				}
				this.writeInt(e.getBattery().getLoad());
				this.writeInt(e.getBattery().getLoadingCurrent());
				this.writeInt(e.getBattery().getDischargeCurrent());
				this.writeInt(e.getBattery().getTemperature());
				BorrowerEvent be = e.getUsage().getEvent();
				switch (be) {
				case FREE:
					this.writeByte((byte) 0);
					break;
				case BORROW:
					this.writeByte((byte) 1);
					this.writeLong(e.getUsage().getBorrower());
					break;
				case RETURN:
					this.writeByte((byte) 2);
					this.writeLong(e.getUsage().getBorrower());
					break;

				}
				UsageState us = e.getUsage().getUsage();
				switch (us) {
				case STEADY_NORMAL:
					this.writeByte((byte) 0);
					break;
				case STEADY_LONG:
					this.writeByte((byte) 1);
					break;
				case MOVING_NORMAL:
					this.writeByte((byte) 2);
					break;
				case MOVING_BACK:
					this.writeByte((byte) 3);
					break;
				}
				this.writeInt(e.getUsage().getDetail());
				this.writeInt(e.getUsage().getAlarm());
			}
		}
	}
}
