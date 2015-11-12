package distributee;

import java.util.List;

import Host.Distributor;

public interface Distributee {
	void start(List<String> sentences, String solution, Distributor host);
	void stop();
}
