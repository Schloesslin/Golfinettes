package fr.ensisa.hassenforder.golfinettes.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import fr.ensisa.hassenforder.golfinettes.client.model.Event;
import fr.ensisa.hassenforder.golfinettes.network.Protocol;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class GolfinetteWriter extends BasicAbstractWriter {

	private String host;
	private int port;
	
	public GolfinetteWriter(String host, int port) {
        super(null);
        this.host = host;
        this.port = port;
    }

	private void writeAsByte(int value) {
        writeByte((byte) (value & 0xFF));
	}

	public void createSigFoxStd(Event lastEvent) {
		
	}

	public void createAlarm(Event lastEvent) {
		
	}
	
	public void createMesaageX(Event lastEvent) {
		
	}
	public void createMesaageY(Event lastEvent) {
		
	}
	
	@Override
	public void send() {
        byte[] message = baos.toByteArray();
    	DatagramSocket socket = null;
        try {
        	InetAddress target = InetAddress.getByName(host);
        	DatagramPacket packet = new DatagramPacket(message, message.length, target, port);
        	System.out.println("SIGFOX packet sent with : "+(message.length)+" bytes all inclusive");
        	System.out.println("SIGFOX packet sent with : "+(message.length-20)+" bytes without type, id and timestamp");
        	socket = new DatagramSocket();
        	socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			if (socket != null) socket.close();
		}
	}

}
