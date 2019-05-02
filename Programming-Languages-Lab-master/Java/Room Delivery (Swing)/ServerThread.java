import java.util.concurrent.ConcurrentHashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.lang.Iterable;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import java.util.Vector;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServerThread extends Thread{
	private Socket socket;
	private ConcurrentHashMap<String, Item> inventory;
	private DataOutputStream out;
	private DataInputStream in;
	private AtomicInteger waittime;
	private Vector restock;
	private Vector saleslist;
	public ServerThread(Socket socket, ConcurrentHashMap<String, Item> inventory, AtomicInteger wt, Vector rs, Vector sl){
		this.socket = socket;
		this.inventory = inventory;
		this.waittime = wt;
		this.restock = rs;
		this.saleslist = sl;
		try{
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private void sendinvdata() throws IOException{
		String str = "";
		for(Map.Entry m : inventory.entrySet()){
			str += ((Item)m.getValue()).name + "|";
			str += ((Item)m.getValue()).price + "|";
			str += ((Item)m.getValue()).quantity + "||";
		}
		out.writeUTF(str);
	}

	private void placeorder() throws IOException{
		String order = in.readUTF();
		String invoice = "";
		String[] orderlist = order.split("\\|\\|");
		Double total = 0.0;
		Integer tod = 2;
		for(int i = 2; i<orderlist.length;i++){
			String[] itemdesc = orderlist[i].split("\\|");
			Double price = inventory.get(itemdesc[0]).price;
			Integer reqquant = Integer.parseInt(itemdesc[1]);
			if(!(itemdesc[0].equals("Tea") || itemdesc[0].equals("Coffee"))){
				inventory.get(itemdesc[0]).quantity -= reqquant;
				Integer quantity = inventory.get(itemdesc[0]).quantity;
				if(quantity<0){
					for(int x = 2; x<=i; x++){
						String[] tmpdesc = orderlist[x].split("\\|");
						if(!(tmpdesc[0].equals("Tea") || tmpdesc[0].equals("Coffee")))
							inventory.get(tmpdesc[0]).quantity += Integer.parseInt(tmpdesc[1]);
					}
					out.writeUTF("FAIL");
					return;
				}
			}
			else{
				tod += reqquant;
			}
			Double netprice = price * reqquant;
			invoice += itemdesc[0] + "|" + itemdesc[1] + "|" + price.toString() + "|" + netprice.toString() + "||";
			total += netprice;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.now();

		for(int i=2 ; i<orderlist.length; i++){
			String[] itemdesc = orderlist[i].split("\\|");
			if(!(itemdesc[0].equals("Tea") || itemdesc[0].equals("Coffee"))){
				if(inventory.get(itemdesc[0]).tbp==false && inventory.get(itemdesc[0]).quantity<10){
					restock.add(itemdesc[0]);
					inventory.get(itemdesc[0]).tbp = true;
				}
			}
			Double price = inventory.get(itemdesc[0]).price;
			Integer reqquant = Integer.parseInt(itemdesc[1]);
			Double netprice = price * reqquant;
			String saleentry = formatter.format(date) + "|";
			saleentry += orderlist[1] + "|";
			saleentry += itemdesc[0] + "|";
			saleentry += price.toString() + "|";
			saleentry += itemdesc[1] + "|";
			saleentry += netprice.toString() + "|";
			saleslist.add(saleentry);
		}

		invoice += total.toString() + "||";
		invoice += Integer.toString(waittime.addAndGet(tod)) + "||";
		out.writeUTF("SUCCESS");
		out.writeUTF(invoice);
	}

	public void run(){
		try{
			String request = "";
			while(request!="STOP"){	
				switch(request){
					case "POLL" : sendinvdata();
								  break;
					case "ORDER": placeorder();
								  break;
				}
				request = in.readUTF();
			}
		}
		catch(IOException e){
			//e.printStackTrace();
		}
		
	}

}