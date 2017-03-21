import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Host.Distributor;
import Host.WordDownload;
import distributee.Distributee;
import distributee.NetworkDistributee;
import distributee.ThreadDistributee;

public class Driver {
	private final static int amountPerDistribution = 100;
	private final static int topCountOfWords = 100;
	private final static int port = 45678;
	private final static String ip = "localhost";
	private Distributor distributor;
	
	// take made up string of words hash it and compare to the given
	String toDecode = "47bbfccdaf6210c4f7b4693d9bce9d4eb5316e75e762c6867d5fc811db98ddc9".toLowerCase();
	List<String> fourLetterWords = new ArrayList<String>();
	List<String> fiveLetterWords = new ArrayList<String>();
	List<String> sentences = new ArrayList<String>();
	
	public static void main(String[] args) {
		List<String> words = WordDownload.getTopWords(topCountOfWords);
		Driver program = new Driver();
		program.seperateWords(words);
		System.out.println("finished seperating words into 4 and 5 letter words");
		program.begin();
		Scanner input = new Scanner(System.in);
		input.nextLine();
		System.out.println("the answer is: " + program.getAnswer());
		System.out.println("acheived after "+ program.getTime() + "ms");
		input.nextLine();
	}
	
	public void begin() {
		ArrayList<Distributee> distributees = new ArrayList<Distributee>();
		for(int i = 0; i < 4; i++) {
			distributees.add(new ThreadDistributee());
		}
		distributees.add(new NetworkDistributee(ip, port));
		this.distributor = new Distributor(distributees, this.fourLetterWords, this.fiveLetterWords, toDecode, amountPerDistribution);
		distributor.start();
	}
	
	public String getAnswer() {
		return this.distributor.getAnswer();
	}
	
	public long getTime() {
		return this.distributor.getCompletionTime();
	}
	
	private void seperateWords(List<String> words) {
		for(String word : words) {
			if(word.length() == 4) {
				fourLetterWords.add(word);
			}
			else if(word.length() == 5) {
				fiveLetterWords.add(word);
			}
		}
	}
}	
