package node;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import communicate.NodeServer;
import node.LamportsClock;

public class NodeServerImplementation implements NodeServer {

	private LamportsClock clock;
	private List<InetSocketAddress> nodes;
	private Map<Integer, InetSocketAddress> requests;
	private InetSocketAddress ownAddress;
	private Log log;
	
	// pro potrebu serializace (marshalingu)
//	private static final long serialVersionUID = -1937161867341487386L;

	public NodeServerImplementation(int port, LamportsClock clock, Log log, InetSocketAddress socket) {
		super();
		this.clock = clock;
		this.log = log;
		this.nodes = new ArrayList<InetSocketAddress>();
		this.requests = new HashMap<Integer, InetSocketAddress>();
		this.ownAddress = socket;
		this.nodes.add(this.ownAddress);
	}
	
	public List<InetSocketAddress> getNodes() {
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
	public boolean testAlive() throws RemoteException {
		return true;
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

//	@Override
//	public int reply(int timestamp, InetSocketAddress address) throws RemoteException {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public void release(int timestamp, InetSocketAddress address) throws RemoteException {
		requests.remove(address);
		log.make("is sending release", clock.event(timestamp), address);
	}

	@Override
	public void logout(int timestamp, InetSocketAddress address) throws RemoteException {
		nodes.remove(address);
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

}
