package distributee;

import java.util.List;
import java.io.Serializable;

public class Package implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<String> sentences;
	private String solution;
	
	public Package(List<String> sentences, String solution) {
		this.sentences = sentences;
		this.solution = solution;
	}
	
	public List<String> getSentences() {
		return this.sentences;
	}
	
	public String getSolution() {
		return this.solution;
	}
}