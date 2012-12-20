package node;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import communicate.NodeServer;
import node.LamportsClock;

public class NodeServerImplementation implements NodeServer {

	private LamportsClock clock;
	private CopyOnWriteArrayList<InetSocketAddress> nodes;
	private Map<Integer, InetSocketAddress> requests;
	private InetSocketAddress ownAddress;
	private Log log;
	
	// pro potrebu serializace (marshalingu)
//	private static final long serialVersionUID = -1937161867341487386L;

	public NodeServerImplementation(int port, LamportsClock clock, Log log, InetSocketAddress socket) {
		super();
		this.clock = clock;
		this.log = log;
		this.nodes = new CopyOnWriteArrayList<InetSocketAddress>();
		this.requests = new HashMap<Integer, InetSocketAddress>();
		this.ownAddress = socket;
		this.nodes.add(this.ownAddress);
	}
	
	public CopyOnWriteArrayList<InetSocketAddress> getNodes() {
		return nodes;
	}

	@Override
	public List<InetSocketAddress> addNode(int timestamp, InetSocketAddress address) throws RemoteException {
		int logicTimeOfJoin = this.clock.event(timestamp);
		if (!this.nodes.contains(address))
			this.nodes.add(address);
		log.make("is joining", logicTimeOfJoin, address);
		return this.nodes;
	}

	@Override
	public List<InetSocketAddress> testAlive(int timestamp, InetSocketAddress address) throws RemoteException {
//		log.make("is testing me alive", clock.event(timestamp), address);
		return this.nodes;
	}

	@Override
	public void message(String msg, int timestamp, InetSocketAddress address) throws RemoteException {
		log.make("is sending message: " + msg, clock.event(timestamp), address);
	}

	@Override
	public int request(int timestamp, InetSocketAddress address) throws RemoteException {
		int logicTimeOfRequest = clock.event(timestamp);
		log.make("is requesting for critical section", logicTimeOfRequest, address);
		requests.put(logicTimeOfRequest, address);
		return logicTimeOfRequest;
	}

	@Override
	public void release(int timestamp, InetSocketAddress address, int timestampOfRequestAskey) throws RemoteException {
		//		requests.remove(timestampOfRequestAskey);

		Iterator<Entry<Integer, InetSocketAddress>> entries = requests.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Integer, InetSocketAddress> entry = (Map.Entry<Integer, InetSocketAddress>) entries.next();
			//			    Integer key = (Integer)entry.getKey();
			//			    InetSocketAddress value = (InetSocketAddress)entry.getValue();
			//			    System.out.println("Key = " + key + ", Value = " + value);
			if (address.equals((InetSocketAddress)entry.getValue()))
			{
				entries.remove();
				break;
			}
		}
		log.make("is sending release", clock.event(timestamp), address);
	}

	@Override
	public void logout(int timestamp, InetSocketAddress address) throws RemoteException {
		synchronized(nodes)
		{
			nodes.remove(address);
		}
		log.make("is logouting", clock.event(timestamp), address);
	}

	@Override
	public void addNodes(List<InetSocketAddress> nodesList) {
		for (InetSocketAddress a : nodesList)
		{
			if (!this.nodes.contains(a))
				this.nodes.add(a);	
		}		
	}

	@Override
	public LamportsClock getClock() {
		return this.clock;
	}

	@Override
	public boolean isOurRequestOnHeadOfQueue() {
		for ( Map.Entry<Integer, InetSocketAddress> entry : this.requests.entrySet() )
		{
			if (entry.getValue().equals(this.ownAddress))
				return true;
			break;
		}
		return false;
	}

	@Override
	public void removeNode(InetSocketAddress address) throws RemoteException {
			nodes.remove(address);
	}
}
