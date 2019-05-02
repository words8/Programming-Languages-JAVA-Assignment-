import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.util.*;
public class Files{

	private String fileName;
	private final ReentrantLock lock = new ReentrantLock();
	public void lockFile(){
		lock.lock();
	}
	public void unlockFile(){
		lock.unlock();
	}
	Files(String fileName)
	{
		this.fileName = fileName;
	}
	public void write_to_file(List list)
	{
		Iterator<Map.Entry> iter = list.iterator();
		String newLine = System.getProperty("line.separator");
		try{
			FileWriter fw = new FileWriter(fileName);
			while(iter.hasNext()){
				Map.Entry mpe = iter.next();
				fw.write(((Record) mpe.getValue()).roll +" " +((Record) mpe.getValue()).name+" " +((Record) mpe.getValue()).marks+" "+((Record) mpe.getValue()).teacher+" " +newLine);
				System.out.println(((Record) mpe.getValue()).roll +" " +((Record) mpe.getValue()).name+" " +((Record) mpe.getValue()).marks+" " +((Record) mpe.getValue()).teacher);
			}
			System.out.println(newLine);
			fw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	public void sort_name(Map records)
	{
		List list=new ArrayList(records.entrySet());
		Collections.sort(list,new Comparator(){
			public int compare(Object o1, Object o2) {
               String a = ((String)((Record) ((Map.Entry)o1).getValue()).name);
               String b = ((String)((Record) ((Map.Entry)o2).getValue()).name);
               int result = a.compareTo(b);
               return result;
            }
       });
		write_to_file(list);
		
	}

	public void sort_roll(Map records)
	{
		List list=new ArrayList(records.entrySet());
		Collections.sort(list,new Comparator(){
			public int compare(Object o1, Object o2) {
               String a = ((String)((Record) ((Map.Entry)o1).getValue()).roll);
               String b = ((String)((Record) ((Map.Entry)o2).getValue()).roll);
               int result = a.compareTo(b);
               return result;
            }
       });
		write_to_file(list);
		
	}	
	
}
