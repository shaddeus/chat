package node;

public class LamportsClock {
	private int timestamp;

	public LamportsClock() {
		this.timestamp = 0;
	}
	
	public int get() {
		return this.timestamp;
	}
	
	public int getLastEvent() {
		return (this.timestamp > 0) ? this.timestamp-1 : 0;
	}
	
	public int event(int eventTimestamp)
	{
		if ( eventTimestamp > this.timestamp )
			this.timestamp = eventTimestamp;
		return (this.timestamp++);	
	}
	
	public int event()
	{
		return event(-1);
	}
}
