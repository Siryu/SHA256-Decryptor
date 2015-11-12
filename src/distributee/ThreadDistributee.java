package distributee;
import java.util.List;

import Host.Distributor;
import Host.SHAEncryption;

public class ThreadDistributee implements Distributee {
	private Thread thread;
	private boolean finished = false;
	
	@Override
	public void start(List<String> sentences, String solution, Distributor host) {
		thread = new Thread(()-> {
			for(String sentence : sentences) {
				if(SHAEncryption.makeShaHash(sentence).equals(solution)) {
					System.out.println("we found a winner");
					finished = true;
					host.reportSolution(sentence);
				}
				if(thread.isInterrupted()) {
					System.out.println("thread quit due to interrupt.");
					finished = true;
					break;
				}
			}
			if(!finished) {
				host.requestMoreWork(this);
			}
		});
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
	}
}