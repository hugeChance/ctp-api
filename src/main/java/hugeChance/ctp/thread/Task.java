package hugeChance.ctp.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import hugeChance.ctp.swt.HugeChanceApp;

/** 
 * 用来处理Socket请求的 
*/  
public class Task implements Runnable {  
	
	static Logger logger = Logger.getLogger(Task.class);

   private Socket socket;  
   
   private HugeChanceApp hugeChanceApp;

     
   public Task(Socket socket, HugeChanceApp hugeChanceApp) {  
      this.socket = socket;  
      this.hugeChanceApp = hugeChanceApp;
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
      
      
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
      while (true) {
    	  String requestBody = br.readLine();
    	      	  
         logger.info("接收到请求报文："+requestBody);
         String[] param  = requestBody.split("\\|");
         String method = param[0];
         
         if(method.equals("reqUserLogin")){
             //登录
             hugeChanceApp.reqUserLogin(param[1], param[2]);
             Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    hugeChanceApp.getCoreAppText().append("reqUserLogin请求："+param[1]+"\r\n");
                }
            });
         }else if (method.equals("reqOrderInsert")) {
             //报单
            hugeChanceApp.reqOrderInsert(param[1], param[2]);
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    hugeChanceApp.getCoreAppText().append("reqOrderInsert请求："+param[1]+"\r\n");
                }
            });
        }else if (method.equals("reqOrderAction")) {
            //撤单
            hugeChanceApp.reqOrderAction(param[1], param[2]);
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    hugeChanceApp.getCoreAppText().append("reqOrderAction请求："+param[1]+"\r\n");
                }
            });
        }else if (method.equals("reqSettlementInfoConfirm")) {
            //结算单确认
            hugeChanceApp.reqSettlementInfoConfirm(param[1], param[2]);
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    hugeChanceApp.getCoreAppText().append("reqSettlementInfoConfirm请求："+param[1]+"\r\n");
                }
            });
          
        } else if (method.equals("reqUserLogout")) {
            //登出
            hugeChanceApp.reqUserLogout(param[1], param[2]);
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    hugeChanceApp.getCoreAppText().append("reqUserLogout请求："+param[1]+"\r\n");
                    hugeChanceApp.getCoreAppText().append(socket.getInetAddress()+"已断开连接"+"\r\n");
                }
            });
            break;
        }
         
      }
      
      
//      br.close();  
//      socket.close();  
   }  
}  
