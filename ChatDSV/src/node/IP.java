//package node;
//
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.Enumeration;
//import java.util.List;
//
//public class IP {
//	public InetAddress getOwnIP()
//	{
//		return( new InetSocketAddress("10.0.0.3", 2010).getAddress());
//		
//		
//		try
//		{
//		for (Enumeration<NetworkInterface> ifaces = 
//	               NetworkInterface.getNetworkInterfaces();
//	             ifaces.hasMoreElements(); )
//	        {
//	            NetworkInterface iface = ifaces.nextElement();
//	            System.out.println(iface.getName() + ":");
//	            for (Enumeration<InetAddress> addresses =
//	                   iface.getInetAddresses();
//	                 addresses.hasMoreElements(); )
//	            {
//	                InetAddress address = addresses.nextElement();
//	                System.out.println("  " + address);
//	            }
//	        }
//		}
//		catch (SocketException e)
//		{
//			System.out.println("asdasdasd " + e.getMessage());
//		}
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		InetAddress localhost;
//		try {
//			localhost = InetAddress.getLocalHost();
//			InetAddress[] a = InetAddress.getAllByName(localhost.getCanonicalHostName());
//			for(int i=0; i < a.length;i++)
//				System.out.println(i + ": " + a[i].getCanonicalHostName());
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		
//		
//		
//		InetAddress addr = null;
//		try {
//
//			for (InetAddress a : InetAddress.getAllByName(null))
//			{
//				System.out.println(a.getHostAddress());
//			}
//			
//			// Replace eth0 with your interface name
//			NetworkInterface i = NetworkInterface.getByName("eth0");
//
//			if (i != null) {
//
//				Enumeration<InetAddress> iplist = i.getInetAddresses();
//				
//				System.out.println(iplist.toString());
//				
//	
//
//				while (iplist.hasMoreElements()) {
//					InetAddress ad = iplist.nextElement();
//					byte bs[] = ad.getAddress();
//					if (bs.length == 4 && bs[0] != 127) {
//						addr = ad;
//						// You could also display the host name here, to 
//						// see the whole list, and remove the break.
//						break;
//					}
//				}
//
//				if (addr != null) {
//					System.out.println( addr.getCanonicalHostName() );
//				}
//			}
//			else
//			{
//				System.out.println("IP: spatne rozhrani");				
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			System.out.println("getOwnIP: " + e.getMessage());
//			return null;
//		}
//		return addr;
//	}
//}
