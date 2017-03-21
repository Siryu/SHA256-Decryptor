package Host;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import distributee.Distributee;

public class Distributor {
	private List<Distributee> distributees;
	private List<String> fourLetterWords;
	private List<String> fiveLetterWords;
	private String solution;
	private int amountPerDistribution;
	private boolean finished;
	private String answer;
	private final Lock lock = new ReentrantLock();
	private int[] wordPlace = {0, 0, 0, 0, 0};
	private long totalWorked = 0;
	private Date startTime;
	private Date endTime;
	
	public Distributor(List<Distributee> distributees, List<String> fourLetterWords, List<String> fiveLetterWords, String solution, int amountPerDistribution) {
		this.distributees = distributees;
		this.fourLetterWords = fourLetterWords;
		this.fiveLetterWords = fiveLetterWords;
		this.solution = solution;
		this.amountPerDistribution = amountPerDistribution;
		this.finished = false;
	}
	
	public void start() {
		this.startTime = new Date();
		for(Distributee distributee : this.distributees) {
			distributee.start(this.getBunchOfSentences(), this.solution, this);
		}
	}
	
	public long getCompletionTime() {
		return this.endTime.getTime() - this.startTime.getTime();
	}

	public void reportSolution(String sentence) {
		this.endTime = new Date();
		System.out.println("The solution to the hash is: " + sentence);
		this.answer = sentence;
		this.finished = true;
		for(Distributee distributee : this.distributees) {
			distributee.stop();
		}
	}

	public void requestMoreWork(Distributee distributee) {
		lock.lock();
		System.out.println("Finished: " + totalWorked);
		try {
			if(!this.finished) {
				distributee.start(this.getBunchOfSentences(), this.solution, this);
			}
			else {
				distributee.stop();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	private List<String> getBunchOfSentences() {
		List<String> passedSentences = new ArrayList<String>();
		int counter = 0;
		int fourLetterSize = fourLetterWords.size();
		int fiveLetterSize = fiveLetterWords.size();
		boolean countFull = false;
		if(wordPlace[0] >= fourLetterSize) {
			this.finished = true;
		}
		while(wordPlace[0] < fourLetterSize && !countFull && !this.finished) {
			if(wordPlace[1] >= fiveLetterSize) {
				wordPlace[1] = 0;
				wordPlace[0]++;
				if(wordPlace[0] >= fourLetterSize) {
					this.finished = true;
				}
			}
			while(wordPlace[1] < fiveLetterSize && wordPlace[0] < fourLetterSize && !countFull && !this.finished) {
				if(wordPlace[2] >= fourLetterSize) {
					wordPlace[2] = 0;
					wordPlace[1]++;
				}
				while(wordPlace[2] < fourLetterSize && wordPlace[1] < fiveLetterSize && !countFull && !this.finished) {
					if(wordPlace[3] >= fiveLetterSize) {
						wordPlace[3] = 0;
						wordPlace[2]++;
					}
					while(wordPlace[3] < fiveLetterSize && wordPlace[2] < fourLetterSize && !countFull && !this.finished) {
						if(wordPlace[4] >= fourLetterSize) {
							wordPlace[4] = 0;
							wordPlace[3]++;
						}
						while(wordPlace[4] < fourLetterSize && wordPlace[3] < fiveLetterSize && !countFull && !this.finished) {
							passedSentences.add(fourLetterWords.get(wordPlace[0]) + " " + 
									fiveLetterWords.get(wordPlace[1]) + " " + 
									fourLetterWords.get(wordPlace[2]) + " " +
									fiveLetterWords.get(wordPlace[3]) + " " +
									fourLetterWords.get(wordPlace[4]));
							counter++;
							wordPlace[4]++;
							if(counter >= amountPerDistribution) {
								countFull = true;
							}
						}
					}
				}
			}
		}
		this.totalWorked += passedSentences.size();
		return passedSentences;
	}
}