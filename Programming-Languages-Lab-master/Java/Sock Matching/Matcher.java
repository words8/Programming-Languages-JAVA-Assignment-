import java.lang.Thread;
import java.util.concurrent.BlockingQueue;

public class Matcher extends Thread{
	private BlockingQueue<Integer> matcherQueue;
	private BlockingQueue<Integer> shelferQueue;
	private Integer numsocks[];
	private static String colors[] = {"White", "Black", "Blue", "Grey"};

	public Matcher(BlockingQueue<Integer> mq, BlockingQueue<Integer> sq){
		matcherQueue = mq;
		shelferQueue = sq;
		numsocks = new Integer[4];
		numsocks[0] = 0;
		numsocks[1] = 0;
		numsocks[2] = 0;
		numsocks[3] = 0;
	}

	private void match() throws InterruptedException{
		Integer sock = matcherQueue.take();
		if(numsocks[sock]==1){
			System.out.println("Matched a pair of " + colors[sock] + " colored socks.");
			numsocks[sock] = 0;
			shelferQueue.put(sock);	
		}
		else{
			numsocks[sock] = 1;
		}
	}

	public void run(){
		try{
			while(true)
				match();
		}
		catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}
	}
}
