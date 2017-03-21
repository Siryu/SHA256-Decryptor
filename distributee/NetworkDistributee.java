package distributee;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import Host.Distributor;

public class NetworkDistributee implements Distributee {
	private Socket socket;
	private boolean finished = false;
	
	public NetworkDistributee(String IP, int port) {
		Socket client = null;
		try {
			client = new Socket(IP, port);
		} catch (UnknownHostException e) {
			System.err.println("There is nothing listening on the other side.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.socket = client;
	}

	@Override
	public void start(List<String> sentences, String solution, Distributor host) {
		new Thread(() -> {
			System.out.println("sending work out on network");
			Package outboundPackage = new Package(sentences, solution);
			try {
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(outboundPackage);
				outputStream.flush();
			}
			catch(Exception ex) {
				System.err.println("Error writing to the networked computer");
			}
			
			String received = "";
			try {
				if(!this.finished) {
					ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
					received = (String)inputStream.readObject();
					System.out.println("received work from network");
				}
			}
			catch(Exception ex) {
				
			}
			
			if(received.length() > 0) {
				this.finished = true;
				host.reportSolution(received);
			}
			
			if(!this.finished) {
				host.requestMoreWork(this);
			}
//			else {
//				try {
//					//this.socket.close();
//				} 
//				catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}).start();
	}

	@Override
	public void stop() {
		this.finished = true;
	}
}