package communicate;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import node.LamportsClock;

public interface NodeServer extends Remote {
	public List<InetSocketAddress> addNode(int timestamp, InetSocketAddress address) throws RemoteException;
	public List<InetSocketAddress> testAlive(int timestamp, InetSocketAddress address) throws RemoteException;
	public void message(String msg, int timestamp, InetSocketAddress address) throws RemoteException;
	public int request(int timestamp, InetSocketAddress address) throws RemoteException;
	public void release(int timestamp, InetSocketAddress address, int timestampOfRequestAskey) throws RemoteException;
	public void logout(int timestamp, InetSocketAddress address) throws RemoteException;
	
	public void addNodes(List<InetSocketAddress> addNode) throws RemoteException;
	public CopyOnWriteArrayList<InetSocketAddress> getNodes() throws RemoteException;
	public LamportsClock getClock() throws RemoteException;
	public boolean isOurRequestOnHeadOfQueue() throws RemoteException;
	public void removeNode(InetSocketAddress address) throws RemoteException;
}