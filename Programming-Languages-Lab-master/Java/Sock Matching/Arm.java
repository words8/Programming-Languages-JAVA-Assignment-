import java.util.Random;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.List;

public class Arm extends Thread{
	private String name;
	private static String colors[] = {"White", "Black", "Blue", "Grey"};
	private BlockingQueue<Integer> matcherQueue;
	private List<Integer> sockheap;
	public Arm(String s, BlockingQueue<Integer> q, List<Integer> sh){
		name = s;
		matcherQueue = q;
		sockheap = sh;
	}
	
	private void addsocktomatcher(){
		Random rand = new Random();
		Integer sockidx = rand.nextInt(sockheap.size());
		synchronized(sockheap.get(sockidx)){
			Integer sidx = sockheap.get(sockidx);
			if(sidx<4){
				String sock = colors[sidx];
				try{
					System.out.println(name + " picked " + sock);
					sockheap.set(sockidx,4);
					matcherQueue.put(sidx);	
					sleep(1000);
				}
				catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}	
			}	
		}
	}
	
	public void run(){
		while(true) addsocktomatcher();
	}
}