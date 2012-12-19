package node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;

import node.LamportsClock;

public class Log {
	
	private static  String DEFAULT_LOG_FILE = "/tmp/chatdsv.log";
	private LamportsClock clock;
	private String logFile;
	
	public Log(LamportsClock clock, String logFile)
	{
		this.clock = clock;
		this.logFile = logFile;
	}
	
	public Log(LamportsClock clock)
	{
		this(clock, DEFAULT_LOG_FILE);
	}
	
	public void make(String msg, int logicTime)
	{
		String str = "[" + String.valueOf(logicTime) + "] " + msg + "\n";
		System.out.print(str);

		try{
			File file = new File(logFile);

			//if file doesnt exists, then create it
			if(!file.exists()){
				file.createNewFile();
			}

			//true = append file
			FileWriter fileWritter = new FileWriter(file.getName(),true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(str);
			bufferWritter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void make(String msg)
	{
		this.make(msg, clock.get());
	}
	
	public void make(String msg, int logicTime, InetSocketAddress address)
	{
		this.make("Node " + address.getAddress().getCanonicalHostName() + ":" + address.getPort() + " " + msg, logicTime);
	}
}
