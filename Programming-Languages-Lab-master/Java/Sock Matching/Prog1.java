import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Prog1{
	public static void main(String[] args){
		BlockingQueue<Integer> mq = new LinkedBlockingDeque<>(10);
		BlockingQueue<Integer> sq = new LinkedBlockingDeque<>(10);
		List socklist = new ArrayList<Integer>();
		Integer numarms = 0;

		System.out.println("Enter the sock heap (White - 0, Black - 1, Blue - 2, Grey - 3, any other characters will be ignored): ");
		Scanner in = new Scanner(System.in);
		String sockh = in.nextLine();
		for(char ch : sockh.toCharArray()) socklist.add(Character.getNumericValue(ch));
		
		System.out.println("Enter the number of arms : ");
		numarms = in.nextInt();
		
		Matcher m = new Matcher(mq,sq);
		Shelfer s = new Shelfer(sq);

		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				s.displaystats();
			}
		});
		s.start();
		m.start();
		while(numarms>0){
			(new Arm("Arm "+numarms.toString(), mq, socklist)).start();
			numarms--;
		}
	}
}
