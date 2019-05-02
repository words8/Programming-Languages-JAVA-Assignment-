import java.util.Comparator;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Record {
	public String name;
	public String roll;
	public String mail;
	public String teacher;
	public int marks;
	private final ReentrantLock lock = new ReentrantLock();
	public void lockRecord(){
		lock.lock();
	}
	public void unlockRecord(){
		lock.unlock();
	}
	Record(String name, String roll, String mail, String teacher, int marks){
		this.name = name;
		this.roll = roll;
		this.marks = marks;
		this.mail = mail;
		this.teacher=teacher;
		System.out.println(this.name +" " + this.roll + " " +this.marks);
	}
	public void changeRecord(String roll, int marks, int choose_M, String ttype, Map map)
	{
		
		int new_marks;
		if((ttype.compareTo("CC") == 0)  || ((this.teacher).compareTo("CC") != 0)){
			if(choose_M==1){
				this.marks = this.marks+marks;
				this.teacher = ttype;
			}
			else{
				this.marks = this.marks-marks;
				this.teacher = ttype;
			}
		}
	
	}

}