import java.util.Scanner; 
import java.util.*;

public class Teacher extends Thread{
	private String type;
	private String roll;
	private int choose_M;
	private int marks;
	private int sync;
	private Map map;
	private int operation;
	private Files f1;
	private Files f2;

	Teacher(Map map, Files f1, Files f2)
	{
		sync = 0;
		operation=0;
		Scanner input = new Scanner(System.in);
		System.out.print("Enter type of Teacher: \t");
		type = input.nextLine();
		roll = "-1";
		while(((Record)map.get(roll)) == null){
			System.out.print("Enter Roll Number :\t");
			roll = input.nextLine();
		}
		choose_M = 0;
		while(choose_M!=1 && choose_M!=2){
			System.out.print(choose_M + "Choose 1. Increase\n\t  2. Decrease\n");
			choose_M = input.nextInt();
		}
		if(choose_M == 1){
			System.out.print("\nEnter Marks to Increase:\t");
		}
		else
			System.out.print("\nEnter Marks to Decrease:\t");
		marks = input.nextInt();
		this.map = map;
		this.f1 = f1;
		this.f2 = f2;
	}
	void set_value(int sync)
	{
		this.sync = sync;
	}
	void set_op(int optype)
	{
		this.operation = optype;
	}
	
	public void run()
	{
		System.out.println("Running Teacher...." + type);
		Record toupdate = ((Record) map.get(roll));
		if(sync == 1){
			toupdate.lockRecord();
			System.out.println("Acquired Lock");
		}
		try{
			toupdate.changeRecord(roll, marks, choose_M, type, map);
			if(operation == 1)
			{
				f1.lockFile();
				System.out.println("Locked first File\n");
				try{
					f1.sort_name(map);
				}finally{
					f1.unlockFile();
					System.out.println("UnLocked first File\n");
				}
				
				f2.lockFile();
				System.out.println("Locked Second File\n");
				try{
					f2.sort_roll(map);
				}finally{
					f2.unlockFile();
					System.out.println("UnLocked Second File\n");
				}
			}
		}
		finally{
			if(sync == 1){
				toupdate.unlockRecord();
				System.out.println("Lock Given");
			}
		}
		System.out.println(toupdate.marks);

	}
}
