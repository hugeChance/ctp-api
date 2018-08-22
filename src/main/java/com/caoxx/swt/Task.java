package com.caoxx.swt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.caoxx.entity.Instrument;


/** 
 * 用来处理Socket请求的 
*/  
public class Task implements Runnable {  
	
	static Logger logger = Logger.getLogger(Task.class);

   private Socket socket;  
   
   private mainControl mainControl;

     
   public Task(Socket socket, mainControl mainControl) {  
      this.socket = socket;  
      this.mainControl = mainControl;
   }  
     
   public void run() {  
      try {  
         handleSocket();  
      } catch (Exception e) {  
         e.printStackTrace();  
      }  
   }
   
   /** 
    * 跟客户端Socket进行通信 
   * @throws Exception 
    */  
   private void handleSocket() throws Exception {  
      
      StringBuilder sb = new StringBuilder();  
      String[] templist;
      String controlID;
      String subAccount;
      String strJson;
      String password;
      int index; 
      String riskstr;
      
//      MainAccountService mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
      while (true) {
    	  
    	  
    	  String temp = br.readLine();
    	  
    	  Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				if(temp != null) {
					mainControl.getOracleRet().append(temp+"\r\n");
					 String[] splitStr = temp.split("\\|");
					 if(splitStr[0].equals("MA20TO5M:")) {
						 Instrument instrument = mainControl.m20To1Map.get(splitStr[2]);
						 if(null != instrument){
							 instrument.setInstrumentId(splitStr[2]);
							 instrument.setMa20To1Value(splitStr[3]);
							 instrument.setUpdateFlg(1);
							 mainControl.m20To1Map.put(splitStr[2], instrument);
						 } else {
							 Instrument instrumentNew = new Instrument();
							 instrumentNew.setInstrumentId(splitStr[2]);
							 instrumentNew.setMa20To1Value(splitStr[3]);
							 instrumentNew.setUpdateFlg(1);
							 mainControl.m20To1Map.put(splitStr[2], instrumentNew);
						 }
					 }

				}
				
			}
		});
    	  
    	 
         try {
             
             if(temp == null || temp.equals("null")){
                 socket.close();
                 mainControl.removeClient(socket);
                 break;
             }

        } catch (Exception e) {
            logger.error("报文解析失败:"+temp+",远程地址："+socket.getRemoteSocketAddress()+","+socket.getInetAddress(),e);
        }
         
         
         
         
         
         
//         if ((index = temp.indexOf("")) != -1) {//遇到eof时就结束接收  
//        	//读完后写一句  
//        	    Writer writer = new OutputStreamWriter(socket.getOutputStream());  
//        	      writer.write("我是服务器，客户端你好");  
//        	      writer.write("eof\n");  
//        	      writer.flush();  
//        	      writer.close();  
//        	 
//        	 
//        	 
//          sb.append(temp.substring(0, index));  
////             break;  
//         }  
         //sb.append(temp);  
      }  
      
      
//      br.close();  
//      socket.close();  
   }  
}  
