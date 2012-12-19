package communicate;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import node.LamportsClock;

public interface NodeServer extends Remote {
	public List<InetSocketAddress> addNode(int timestamp, InetSocketAddress address) throws RemoteException;
	public boolean testAlive() throws RemoteException;
	public void message(String msg, int timestamp, InetSocketAddress address) throws RemoteException;
	public int request(int timestamp, InetSocketAddress address) throws RemoteException;
	public void release(int timestamp, InetSocketAddress address) throws RemoteException;
	public void logout(int timestamp, InetSocketAddress address) throws RemoteException;
	
	public void addNodes(List<InetSocketAddress> addNode) throws RemoteException;
	public List<InetSocketAddress> getNodes() throws RemoteException;
	public LamportsClock getClock() throws RemoteException;
	public boolean isOurRequestOnHeadOfQueue() throws RemoteException;
}