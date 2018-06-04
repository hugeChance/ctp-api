package com.caoxx.thread;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.caoxx.swt.CaoxxApp;
import com.caoxx.utils.ApplicationConfig;

/**
 * 监听客户端socket的接入
 * @author caoxx
 */
public class Listener implements Runnable {
    
    static Logger logger = Logger.getLogger(Listener.class);
    
    private static final int port = Integer.parseInt(ApplicationConfig.getProperty("port"));
    
    private CaoxxApp CaoxxApp;
    
    public Listener(CaoxxApp CaoxxApp) {
        this.CaoxxApp = CaoxxApp;
    }

    @Override
    public void run() {
        logger.info("监听客户端线程启动...");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket client = serverSocket.accept();
                
                if(CaoxxApp.getSocket() == null){
                    
                    CaoxxApp.setSocket(client);
                    new Thread(new Task(client,CaoxxApp)).start(); 
                    logger.info("客户端已接入："+client.getInetAddress()+"\r\n");
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            CaoxxApp.getCoreAppText().append("客户端已接入："+client.getInetAddress()+"\r\n");
                        }
                    });
                    
                }else {
                    logger.info("阻止非法客户端接入："+client.getInetAddress()+"\r\n");
                    
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            CaoxxApp.getCoreAppText().append("阻止非法客户端接入："+client.getInetAddress()+"\r\n");
                        }
                    });
                }
                
            }
        } catch (Exception e) {
            logger.error("创建socket服务端失败",e);
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    MessageBox box = new MessageBox(CaoxxApp.shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("创建socket服务失败！");
                    box.setText("错误");
                    box.open();
                    CaoxxApp.shell.dispose();
                    
                }
            });
        } 
    }
}
