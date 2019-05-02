import java.util.concurrent.BlockingQueue;

public class Shelfer extends Thread{

	private BlockingQueue<Integer> shelferQueue;
	private Integer numpairs[];
	private static String colors[] = {"White", "Black", "Blue", "Grey"};
	private Integer sockpair;

	public Shelfer(BlockingQueue<Integer> sq){
		shelferQueue = sq;
		numpairs = new Integer[4];
		numpairs[0] = 0;
		numpairs[1] = 0;
		numpairs[2] = 0;
		numpairs[3] = 0;
	}

	public void displaystats(){
		System.out.println("Number of pairs shelved : ");
		for(int i=0;i<4;i++){
			System.out.println(colors[i].toString() + " - " + numpairs[i].toString());
		}
	}

	private void shelf() throws InterruptedException{
		sockpair = shelferQueue.take();
		System.out.println("Shelved a pair of " + colors[sockpair] + " colored socks.");
		numpairs[sockpair] = numpairs[sockpair] + 1;
	}

	public void run(){
		try{
			while(true)
				shelf();	
		}
		catch(InterruptedException e){
			displaystats();
			Thread.currentThread().interrupt();
		}
		
	}
}