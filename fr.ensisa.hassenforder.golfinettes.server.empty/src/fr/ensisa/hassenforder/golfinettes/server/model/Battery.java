package fr.ensisa.hassenforder.golfinettes.server.model;

public class Battery {

	public enum BatteryMode {
		UNPLUGGED,
		PLUGGED_ONLY,
		SLOW_CHARGING,
		FAST_CHARGING,
	}
	
	private int temperature, load;
	private int loadingCurrent, dischargeCurrent;
	private BatteryMode mode;
	
	public Battery(int temperature, int load, int loadingCurrent, int dischargeCurrent, BatteryMode mode) {
		super();
		this.temperature = temperature;
		this.load = load;
		this.loadingCurrent = loadingCurrent;
		this.dischargeCurrent = dischargeCurrent;
		this.mode = mode;
	}

	public int getTemperature() {
		return temperature;
	}

	public int getLoad() {
		return load;
	}

	public int getLoadingCurrent() {
		return loadingCurrent;
	}

	public int getDischargeCurrent() {
		return dischargeCurrent;
	}

	public BatteryMode getMode() {
		return mode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" Power");
		switch (mode) {
		case UNPLUGGED:		builder.append(" U "); break;
		case PLUGGED_ONLY: 	builder.append(" P "); break;
		case SLOW_CHARGING: builder.append(" S "); break;
		case FAST_CHARGING: builder.append(" F "); break;
		}
		builder.append(load);
		builder.append("%");
		if (loadingCurrent == 0 && dischargeCurrent == 0 && temperature == 0) return builder.toString();
		builder.append(" =>");
		builder.append(loadingCurrent);
		builder.append("A <=");
		builder.append(dischargeCurrent);
		builder.append("A ");
		builder.append(temperature);
		builder.append("°C");
		return builder.toString();
	}

	
}
