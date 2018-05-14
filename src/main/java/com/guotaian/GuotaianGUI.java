package com.guotaian;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.gta.qts.c2j.adaptee.IGTAQTSApi;
import com.gta.qts.c2j.adaptee.IGTAQTSCallbackBase;
import com.gta.qts.c2j.adaptee.impl.GTAQTSApiBaseImpl;
import com.gta.qts.c2j.adaptee.structure.QTSDataType;
import com.gta.qts.c2j.adaptee.structure.QTSDataType.MsgType;
import com.gta.qts.c2j.adaptee.structure.QTSDataType.RetCode;
import com.gta.qts.c2j.adaptee.structure.StockSymbol;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class GuotaianGUI {

    private JFrame frame;
    
    public JTextArea textArea;
    
    public JTextArea textArea_1;
    
    public JTextArea textArea_2;
    
    public JTextArea textArea_3;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane_1;
    private JScrollPane scrollPane_2;
    private JScrollPane scrollPane_3;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GuotaianGUI window = new GuotaianGUI();
                    Thread marketThread = new Thread(new MarketThread(window));
                    marketThread.setDaemon(true);
                    marketThread.start();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GuotaianGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 755, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel label = new JLabel("上期所");
        label.setBounds(10, 10, 54, 15);
        frame.getContentPane().add(label);
        
        JLabel label_1 = new JLabel("郑商所");
        label_1.setBounds(398, 10, 54, 15);
        frame.getContentPane().add(label_1);
        
        JLabel label_2 = new JLabel("大商所L1");
        label_2.setBounds(10, 241, 54, 15);
        frame.getContentPane().add(label_2);
        
        JLabel label_3 = new JLabel("大商所L2");
        label_3.setBounds(398, 241, 54, 15);
        frame.getContentPane().add(label_3);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 29, 331, 202);
        frame.getContentPane().add(scrollPane);
        
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        scrollPane.setHorizontalScrollBarPolicy( 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
        scrollPane.setVerticalScrollBarPolicy( 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane_1.setBounds(398, 29, 331, 205);
        frame.getContentPane().add(scrollPane_1);
        
        textArea_1 = new JTextArea();
        scrollPane_1.setViewportView(textArea_1);
        
        scrollPane_2 = new JScrollPane();
        scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane_2.setBounds(10, 267, 331, 205);
        frame.getContentPane().add(scrollPane_2);
        
        textArea_2 = new JTextArea();
        scrollPane_2.setViewportView(textArea_2);
        
        scrollPane_3 = new JScrollPane();
        scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane_3.setBounds(398, 267, 331, 205);
        frame.getContentPane().add(scrollPane_3);
        
        textArea_3 = new JTextArea();
        scrollPane_3.setViewportView(textArea_3);
    }
    
    public static class MarketThread implements Runnable{
        
        private GuotaianGUI guotaian;
        
        public MarketThread(GuotaianGUI guotaian){
            this.guotaian = guotaian;
        }

        @Override
        public void run() {
            
            try {
                // 创建消息回调对象，用于接收实时数据
                IGTAQTSCallbackBase callback = new GTACallbackBase(guotaian);
                
                //创建API对象，与服务端交互使用
                IGTAQTSApi baseService = GTAQTSApiBaseImpl.getInstance().CreateInstance(callback);
                
                //基础API环境初始化，在开始使用API操作函数前，只调用一次
                baseService.BaseInit();
                //设置超时时间
                baseService.BaseSetTimeout(30);
                
        //注册FENS地址
                //***** 警告：实际生产环境使用时，从国泰安公司获取到的FENS地址，此处需要全部通过“RegisterService”函数接口注册，
                //*****       否则，在数据高可用方面，会大打折扣。
                //***** 如有4个FENS ip地址，需要如下调用：
                //      baseService.BaseRegisterService("192.168.105.136", (short)7777);
                //      baseService.BaseRegisterService("192.168.105.137", (short)7777);
                //      baseService.BaseRegisterService("192.168.105.138", (short)7777);
                //      baseService.BaseRegisterService("192.168.105.139", (short)7777);
                baseService.BaseRegisterService("119.147.211.219", (short)8866);
                baseService.BaseRegisterService("180.153.102.99", (short)8888);
                baseService.BaseRegisterService("119.147.211.220", (short)8866);
                baseService.BaseRegisterService("180.153.102.94", (short)8888);
                
                
                do{
                    //通过用户名与密码向服务器登陆
                    int ret = baseService.BaseLoginX("shjsxx", "5t455Q2N","NetType=0");
                    if ( QTSDataType.RetCode.Ret_Success != QTSDataType.RetCode.fetchByCode(ret) ){
                        System.out.println("Login error:" + ret);
                        break;
                    }
                    System.out.println("Login success");
                    
                    List<StockSymbol> outList = new ArrayList<StockSymbol>();
                    // 获取上交所和深交所代码列表，其中SSE表示上交所，SZSE表示深交所sse,
                    ret = baseService.BaseGetStockList("SHFE,CZCE,DCE", outList);
                    if ( QTSDataType.RetCode.Ret_Success != QTSDataType.RetCode.fetchByCode(ret) ){
                        System.out.println("GetStockList(sse,szse) error:" + ret);
                        break;
                    }
                    System.out.println("GetStockList success");
                    
                    //输出获取到的证券代码
                    System.out.print("StockList:" + outList.size() + "  ");
                    for (int idx = 0; idx < outList.size(); idx++) {
                        if (idx > 0) {
                            System.out.print(",");
                        }
                        byte[] bytesymbol = outList.get(idx).Symbol;
                        String Symbol = new String(bytesymbol,0,bytesymbol.length,"UTF-8").trim();
                        System.out.print(Symbol);
                    }
                    System.out.println("");
                    
                    List<Integer> msgtypeList = new ArrayList<Integer>();
                    
                    //获取用户权限列表
                    ret = baseService.BaseGetMsgTypeList(msgtypeList);
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("GetMsgTypeList error:" + ret);
                        break;
                    }
                    System.out.println("GetMsgTypeList success");
                    
                    //输出获取到的用户权限列表
                    System.out.print("MsgType:" + msgtypeList.size() + "  ");
                    for (int idx = 0; idx < msgtypeList.size(); idx++) {
                        MsgType msgType = MsgType.fetchByCode(msgtypeList.get(idx));
                        if (msgType != null) {
                            System.out.print(((idx > 0) ? ",Msg_" : "Msg_") + msgType.name());
                        }
                    }
                    System.out.println("");
                    
                 // 按代码订阅郑商所实时行情数据
                    ret = baseService.BaseSubscribe(MsgType.CZCEL1_Quotation.code, "MA809");
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Subscribe error:" + ret);
                        break;
                    }
                    System.out.println("Subscribe success");
                    
                    // 按代码订阅上期所实时行情数据
                    ret = baseService.BaseSubscribe(MsgType.SHFEL1_Quotation.code, "rb1810");
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Subscribe error:" + ret);
                        break;
                    }
                    System.out.println("Subscribe success");
                    
                    // 按代码订阅大商所实时行情数据
                    ret = baseService.BaseSubscribe(MsgType.DCEL1_Quotation.code, "i1809");
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Subscribe error:" + ret);
                        break;
                    }
                    System.out.println("Subscribe success");
                    
                    // 按代码订阅大商所实时行情数据
                    ret = baseService.BaseSubscribe(MsgType.DCEL2_Quotation.code, "i1809");
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Subscribe error:" + ret);
                        break;
                    }
                    System.out.println("Subscribe success");
                    
                    //订阅成功，启动处理数据线程
                    //SZSEL2_Quotation_FileOut.startThread();
                    
                    //等待客户输入后再继续执行
                    /*System.out.println("press Enter to continue...(Unsubscribe all data)");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    br.readLine();
                    
                    // 按代码取消深交所实时行情数据
                    ret = baseService.BaseUnsubscribe(MsgType.SZSEL2_Quotation.code, "000001");
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Unsubscribe error:" + ret);
                        break;
                    }
                    System.out.println("Unsubscribe 000001 success");
                    
                    // 取消深交所实时所有代码
                    ret = baseService.BaseUnsubscribe(MsgType.SZSEL2_Quotation.code, null);
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("Unsubscribe error:" + ret);
                        break;
                    }
                    System.out.println("Unsubscribe all success");
                    
                    List<SZSEL2_Quotation> snapList = new ArrayList<SZSEL2_Quotation>();
                    
                    //深交所实时行情快照查询
                    ret = baseService.QuerySnap_SZSEL2_Quotation("000001,000002,000003", snapList);
                    if ( RetCode.Ret_Success != RetCode.fetchByCode(ret) ){
                        System.out.println("QuerySnap_SZSEL2_Quotation error:" + ret);
                        break;
                    }
                    System.out.println("QuerySnap_SZSEL2_Quotation success");
                    
                    //输出查询快照结果
                    System.out.println("QuerySnap_SZSEL2_Quotation : count=" + snapList.size());
                    for (int idx = 0; idx < snapList.size(); idx++) {
                        byte[] symbol = snapList.get(idx).Symbol;
                        String strSymbol = new String(symbol,0,symbol.length,"UTF-8").trim();
                        System.out.println("LocalTimeStamp=" + snapList.get(idx).LocalTimeStamp
                                + " Symbol=" + strSymbol
                                + " OpenPrice=" + snapList.get(idx).OpenPrice
                                + " TotalAmount=" + snapList.get(idx).TotalAmount
                                );
                    }*/
                    
                }while(false);
                
                System.out.print("press Enter to end...");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine().trim();
                
                //基础API环境反初始化
                baseService.BaseUninit();
                
                System.out.println("GTA API exit");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
    }
}
