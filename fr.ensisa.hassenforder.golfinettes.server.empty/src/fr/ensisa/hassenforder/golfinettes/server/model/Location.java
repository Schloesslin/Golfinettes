package fr.ensisa.hassenforder.golfinettes.server.model;

public class Location {
	
	private float latitude, longitude;
	private int temperature, humidity;
	
	public Location(float latitude, float longitude, int temperature, int humidity) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public int getTemperature() {
		return temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (latitude == 0 && longitude == 0 && temperature == 0 && humidity == 0) return builder.toString();
		builder.append(" @ ");
		builder.append(latitude);
		builder.append(", ");
		builder.append(longitude);
		if (temperature == 0 && humidity == 0) return builder.toString();
		builder.append(" ");
		builder.append(temperature);
		builder.append("°C ");
		builder.append(humidity);
		builder.append("%");
		return builder.toString();
	}

}
