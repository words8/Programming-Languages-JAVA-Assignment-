import java.util.concurrent.atomic.AtomicInteger;
import java.util.TimerTask;

public class TeaServer extends TimerTask{
	private AtomicInteger waittime;
	
	public TeaServer(AtomicInteger wt){
		this.waittime = wt;
	}

	public void run(){
		if(waittime.get()>0)
			waittime.decrementAndGet();
	}
}