package fr.ensisa.hassenforder.golfinettes.client.model;

public class Version {
	private String version;
	private String fileContent1;
	private byte fileContent2 [];
	
	public Version(String version, String fileContent1, byte fileContent2 []) {
		super();
		this.version = version;
		this.fileContent1 = fileContent1;
		this.fileContent2 = fileContent2;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getFileContent1() {
		return fileContent1;
	}

	public byte [] getFileContent2() {
		return fileContent2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(version);
		builder.append(" ==> ");
		builder.append(fileContent1 == null ? "null" : fileContent1);
		builder.append(" &&& ");
		builder.append(fileContent2 == null ? "null" : "content");
		return builder.toString();
	}

}
