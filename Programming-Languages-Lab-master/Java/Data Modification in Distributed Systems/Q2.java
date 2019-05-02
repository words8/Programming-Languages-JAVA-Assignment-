import java.util.Scanner; 
import java.io.*;
import java.util.*;
public class Q2 {
	private static void init(Map records)
	{
		String fileName = "Stud_info.txt";
		String line = null;

		try {
	        // FileReader reads text files in the default encoding.
	        FileReader fileReader = new FileReader(fileName);
	        //wrapping fileReader in bufferedReader to read character based stream.
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        //while there is unread data
	        while((line = bufferedReader.readLine()) != null) {
	            String[] data = line.split("\\|");
	            //System.out.println((data[0]));
        		records.put(data[0], new Record(data[1], data[0], data[2], data[4], Integer.parseInt(data[3])));
	        }
	        //closing File
	        bufferedReader.close();         
	    }
	    catch(FileNotFoundException ex) {
	        System.out.println("Unable to open file '" + fileName + "'"); 
	    }
	    catch(IOException ex) {
        	System.out.println("Error reading file '"+ fileName + "'");
        }
	}
	public static void main(String args[]) {         
		Boolean run = true;
		Map records=new HashMap();
		init(records);
		while(run)
		{
			Files f1 = new Files("sort_name.txt");
			Files f2 = new Files("sort_roll.txt");
			Teacher t1 = new Teacher(records,f1,f2);
			Teacher t2 = new Teacher(records,f1,f2);
			
			Scanner input = new Scanner(System.in);
			int choose_S = 0;
			while(choose_S!=1 && choose_S!=2){
				System.out.print("USE 1.Synchronisation\n    2.Without Synchronisation\n");
				choose_S = input.nextInt();
			}
			t1.set_value(choose_S);
			t2.set_value(choose_S);

			choose_S = 0;
			while(choose_S!=1 && choose_S!=2){
				System.out.print("USE 1.File Level Operation\n    2.Record Level Operation\n");
				choose_S = input.nextInt();
			}
			
			t1.set_op(choose_S);
			t2.set_op(choose_S);

			t1.start();
			t2.start();
			try{
				t1.join();
				t2.join();
			}catch(InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			if(choose_S != 1)
			{
				f1.sort_name(records);
				f1.sort_roll(records);
			}
			int choose_C = 0;
			while(choose_C!=1 && choose_C!=2){
				System.out.println("Do you want to continue? 1. Yes\n \t\t\t 2. No");
				try{
					choose_C = input.nextInt();
				}
				catch(InputMismatchException e)
				{
					choose_C = 0;
					input.nextLine();
				}
				if(choose_C == 2)
					run = false;
			}
		}
	}
}
