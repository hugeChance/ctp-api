package com.caoxx.swt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServerThread implements Runnable {
	
	static Logger logger = Logger.getLogger(ServerThread.class);
	
    private static final String ip = "localhost";
//    private static final int port = 3366; 是caoxx
    //3377 是caoxx2
    private static final int port = 3377;

	
	private mainControl mainControl;
	

	
	public ServerThread(mainControl mainControl) {
		this.mainControl = mainControl;
		
	}

	@Override
	public void run() {

		logger.info("监听客户端线程启动...");
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true){
				Socket client = serverSocket.accept();
				new Thread(new Task(client,mainControl)).start(); 
				logger.info("客户端已接入："+client.getInetAddress());
				mainControl.addClient(client);
//				Display.getDefault().syncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						
//					}
//				});
			}
		} catch (IOException e) {
			logger.error("创建socket服务端失败",e);
		}
	}
	
	     
}
	  
	 

