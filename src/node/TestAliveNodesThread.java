package node;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import communicate.NodeServer;

public class TestAliveNodesThread extends Thread {

	private final Integer INTERVAL = 5000;

	private Log log;
	private LamportsClock clock;
	private NodeServer nodeServer;
	private InetSocketAddress socket;
	private String RMI_NAME;

	public TestAliveNodesThread(String str, Log log, LamportsClock clock, NodeServer nodeServer, InetSocketAddress socket, String RMI_NAME)
	{
		super(str);
		this.log = log;
		this.clock = clock;
		this.nodeServer = nodeServer;
		this.socket = socket;
		this.RMI_NAME = RMI_NAME;
	}
	public void run()
	{
		while(true)
		{
			List<InetSocketAddress> remoteNodesListNodes = new ArrayList<InetSocketAddress>();

			try {
				for (InetSocketAddress address : nodeServer.getNodes())
				{
					if (address.equals(socket))
						continue;

					int logicTimeOfTestAlive = clock.event();
					Registry registry;
					NodeServer remoteNode;
					try {
						registry = LocateRegistry.getRegistry(address.getAddress().getCanonicalHostName(), address.getPort());
						remoteNode = (NodeServer) registry.lookup(RMI_NAME);
						remoteNodesListNodes.addAll(remoteNode.testAlive(logicTimeOfTestAlive, socket));
					} catch (Exception e) {
						// neco je spatne
						// nelze navazat spojeni se vzdalenym RMI serverem, tudiz uzel asi spadl/...
						nodeServer.removeNode(address);
						log.make("Keep Alive Test detect dead node " + address.getAddress().getCanonicalHostName() + ":" + address.getPort());
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("TestAliveNodesThread: " + e.getMessage());
			}

			// Projdeme uzly nasich sousedu a zjistime, zda je mame i v nasem seznamu
			// pokud budou aktivni, pridame si je do naseho seznamu uzlu
			for ( InetSocketAddress address : remoteNodesListNodes )
			{
				if (address.equals(socket))
					continue;

				try {
					if (!nodeServer.getNodes().contains(address))
					{
						Registry registry;
						NodeServer remoteNode;
						try {
							registry = LocateRegistry.getRegistry(address.getAddress().getCanonicalHostName(), address.getPort());
							remoteNode = (NodeServer) registry.lookup(RMI_NAME);
							int logicTimeOfTestAlive = clock.event();
							remoteNode.testAlive(logicTimeOfTestAlive, socket);
							// s uzlem lze normalne komunikovat, pridame jsi jej do naseho seznamu uzlu
							int logicTimeofDiscoveredNode = clock.event();
							nodeServer.addNode(logicTimeofDiscoveredNode, address);
							log.make("Keep Alive Test detect new node " + address.getAddress().getCanonicalHostName() + ":" + address.getPort());
						} catch (Exception e1) {
							// uzel je asi take mrtvy, ten si k sobe pridavat nebudeme
							continue;
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					System.out.println("TestAliveNodesThread: " + e.getMessage());
				}
			}

			try {
				Thread.currentThread();
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("TestAliveNodesThread: " + e.getMessage());
			}
		}
	}
}
