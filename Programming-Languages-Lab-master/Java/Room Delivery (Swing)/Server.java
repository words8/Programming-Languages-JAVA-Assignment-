import java.util.concurrent.ConcurrentHashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Timer;
import java.util.Iterator;
import java.util.Vector;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Server extends Thread{
	static final int port = 4000;
	private Vector restock;
	private Vector saleslist;
	public Server(Vector rs, Vector sl){
		this.restock = rs;
		this.saleslist = sl;
	}
	public static String padRight(String s, int n) {
    	return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);  
	}

	public void run(){
		Scanner in = new Scanner(System.in);
		try{
			while(true){
				int choice=0;
				
				while(choice!=1 && choice !=2){
					System.out.println("Choose an option : ");
					System.out.println("1. Restock List");
					System.out.println("2. Sales Report");
					choice = in.nextInt();	
				}
				if(choice==1){
					Iterator<String> iter = restock.iterator();
					while(iter.hasNext()){
						System.out.println(iter.next());
					}	
				}
				else{
					int numdays = 0;
					while(numdays<1){
						System.out.println("Enter the number of days (>0): ");
						numdays = in.nextInt();
					}
					System.out.println("Date      |Name                |Item      |Rate    |Quantity|Price   |");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate date = LocalDate.now().minusDays(numdays);
					String gtdt = formatter.format(date);
					Double totbusiness = 0.0;
					Iterator<String> iter = saleslist.iterator();
					while(iter.hasNext()){ 
						String saleentry = iter.next();
						String[] saleparams = saleentry.split("\\|");
						if(saleparams[0].compareTo(gtdt) > 0){
							String output = saleparams[0] + "|";
							output += padRight(saleparams[1],20) + "|";
							output += padRight(saleparams[2],10) + "|";
							output += padLeft(saleparams[3],8) + "|";
							output += padLeft(saleparams[4],8) + "|";
							output += padLeft(saleparams[5],8) + "|";
							System.out.println(output);
							totbusiness += Double.parseDouble(saleparams[5]);
						}
					}
					System.out.println("Total Business over this period : "+totbusiness.toString());
				}
			}
		}
		catch(Exception e){
			System.out.println("Shutting Down Server");
		}
	}

	public static void loadinv(ConcurrentHashMap<String, Item> inventory){
		inventory.put("Tea", new Item("Tea", 6.00, -1, false));
		inventory.put("Coffee", new Item("Coffee", 8.00, -1, false));
		inventory.put("Cookies", new Item("Cookies", 5.00, 10, false));
		inventory.put("Chocolates", new Item("Chocolates", 5.00, 10, false));
		inventory.put("Chips", new Item("Chips", 10.00, 10, false));
	}

	public static void main(String[] args){
		ConcurrentHashMap<String, Item> inventory = new ConcurrentHashMap<String, Item>();
		Vector restock = new Vector();
		Vector saleslist = new Vector();
		AtomicInteger waittime = new AtomicInteger(0);
		loadinv(inventory);
		ServerSocket serverSocket = null;
		Socket socket = null;
		Timer timer = new Timer();
		timer.schedule(new TeaServer(waittime),0,60000);
		try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Server(restock,saleslist).start();
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new ServerThread(socket, inventory, waittime, restock, saleslist).start();
        }
	}
}