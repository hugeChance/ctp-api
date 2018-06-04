package com.caoxx.swt;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradingAccountField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caoxx.api.MyTraderSpi;
import com.caoxx.thread.Listener;

import swing2swt.layout.BorderLayout;

public class CaoxxApp {
    
    static Logger logger = Logger.getLogger(CaoxxApp.class);

    public Shell shell;
    
    JCTPTraderApi traderApi;
    JCTPTraderSpi traderSpi;
    
    //private String frontAddr = "tcp://180.169.116.120:41205";
    //simnow 
    private String frontAddr = "tcp://180.168.146.187:10000";
    
    private Text ctpText;
    private Text coreAppText;
    private Socket socket;
    private PrintWriter out;
    

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }

    public Text getCoreAppText() {
        return coreAppText;
    }

    public void setCoreAppText(Text coreAppText) {
        this.coreAppText = coreAppText;
    }

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            CaoxxApp window = new CaoxxApp();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        
        Thread thread = new Thread(new ConnectCTP());
        thread.setDaemon(true);
        thread.start();
        
        Thread serverThread = new Thread(new Listener(this));
        serverThread.setDaemon(true);
        serverThread.start();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(737, 467);
        shell.setText("CTP连接机");
        shell.setLayout(new BorderLayout(0, 0));
        
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(BorderLayout.CENTER);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        
        Composite composite_2 = new Composite(composite, SWT.NONE);
        composite_2.setLayout(new BorderLayout(0, 0));
        
        Label lblCtp = new Label(composite_2, SWT.NONE);
        lblCtp.setLayoutData(BorderLayout.NORTH);
        lblCtp.setText("CTP通讯");
        
        ctpText = new Text(composite_2, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        ctpText.setLayoutData(BorderLayout.CENTER);
        
        Composite composite_3 = new Composite(composite, SWT.NONE);
        composite_3.setLayout(new BorderLayout(0, 0));
        
        Label lblNewLabel = new Label(composite_3, SWT.NONE);
        lblNewLabel.setLayoutData(BorderLayout.NORTH);
        lblNewLabel.setText("后台通讯");
        
        coreAppText = new Text(composite_3, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        coreAppText.setLayoutData(BorderLayout.CENTER);
        
        Composite composite_1 = new Composite(shell, SWT.NONE);
        composite_1.setLayoutData(BorderLayout.EAST);
        

    }
    
    public String getFrontAddr() {
        return frontAddr;
    }

    public void setFrontAddr(String frontAddr) {
        this.frontAddr = frontAddr;
    }




    public class ConnectCTP implements Runnable{

        @Override
        public void run() {
            
            String dataPath = "ctpdata/test/";
            
            traderApi = JCTPTraderApi.createFtdcTraderApi();
            traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);

            traderSpi = new MyTraderSpi(traderApi,CaoxxApp.this);
            
            //注册traderpi
            traderApi.registerSpi(traderSpi);
            //注册公有流
            traderApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册私有流
            traderApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册前置机地址
            traderApi.registerFront(frontAddr);
            
            traderApi.init();
            traderApi.join();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //回收api和JCTP
            traderApi.release();
        }
        
    }
    
    public void ReqQrySettlementInfo(){
    	
    }
    
    
    /**
     * 用户登录请求
     * 
     * @param pReqUserLoginField
     * @param nRequestID
     * @return 请求发送状态
     */
    public int reqUserLogin(String requestBody, String nRequestID) {
        
        logger.info("用户登录请求入参："+requestBody+"; 请求id："+nRequestID);
        JSONObject json = JSON.parseObject(requestBody);
        
        CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
        userLoginField.setBrokerID(json.getString("brokerID"));
        userLoginField.setUserID(json.getString("userID"));
        userLoginField.setPassword(json.getString("password"));
        int result = traderApi.reqUserLogin(userLoginField, Integer.parseInt(nRequestID));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("reqUserLogin请求： "+result+JSON.toJSONString(userLoginField)+"\r\n");
            }
        });
        return result;
    }
    
    /**
     * 
     * 
     * @param pReqUserLoginField
     * @param nRequestID
     * @return 请求发送状态
     */
    public int reqSettlementInfoConfirm(String requestBody, String nRequestID) {
        
        logger.info("结算单确认入参："+requestBody+"; 请求id："+nRequestID);
        JSONObject json = JSON.parseObject(requestBody);
        
        CThostFtdcSettlementInfoConfirmField settlementInfoConfirmField = new CThostFtdcSettlementInfoConfirmField();
        settlementInfoConfirmField.setBrokerID(json.getString("brokerID"));

		settlementInfoConfirmField.setInvestorID(json.getString("investorID"));
        int result = traderApi.reqSettlementInfoConfirm(settlementInfoConfirmField, Integer.parseInt(nRequestID));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("reqSettlementInfoConfirm请求： "+result+JSON.toJSONString(settlementInfoConfirmField)+"\r\n");
            }
        });
        return result;
    }
    
    /**
     * 登出请求
     * 
     * @param pUserLogout
     * @param nRequestID
     * @return 请求发送状态
     */
    public int reqUserLogout(String requestBody, String nRequestID) {
        
        logger.debug("登出请求入参:"+requestBody);
        CThostFtdcUserLogoutField pUserLogout = new CThostFtdcUserLogoutField();
        
        JSONObject json = JSON.parseObject(requestBody);
        try {
            BeanUtils.copyProperties(pUserLogout, json);
        } catch (Exception e) {
            logger.error("复制属性失败",e);
        }
        
        return traderApi.reqUserLogout(pUserLogout, Integer.parseInt(nRequestID));
    }
    
    /**
     * 撤单请求
     * 
     * @param pInputOrderAction
     * @param nRequestID
     * @return
     */
    public int reqOrderAction(String requestBody, String nRequestID) {

        logger.info("撤单请求入参："+requestBody+"; 请求id："+nRequestID);
        JSONObject json = JSON.parseObject(requestBody);
        CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
        pInputOrderAction.setBrokerID(json.getString("brokerID"));
        pInputOrderAction.setInvestorID(json.getString("investorID"));
        
      pInputOrderAction.setOrderRef(json.getString("orderRef"));
      pInputOrderAction.setFrontID(Integer.valueOf(json.getString("frontID")));
      pInputOrderAction.setSessionID(Integer.valueOf(json.getString("sessionID")));

        pInputOrderAction.setInstrumentID(json.getString("instrumentID"));
        pInputOrderAction.setActionFlag(json.getString("actionFlag").toCharArray()[0]);
        
        //orderSysID+exchangeID 当做撤单标识
//        pInputOrderAction.setOrderSysID(json.getString("orderSysID"));
//        pInputOrderAction.setExchangeID(json.getString("exchangeID"));
        
        int result = traderApi.reqOrderAction(pInputOrderAction, Integer.parseInt(nRequestID));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("reqOrderAction请求： "+result+JSON.toJSONString(pInputOrderAction)+"\r\n");
            }
        });
        
        return result;
    }
    
    
    /**
     * 报单录入请求
     * 
     * @param pInputOrder
     * @param nRequestID
     * @return
     */
    public int reqOrderInsert(String requestBody, String nRequestID) {
        
        JSONObject json = JSON.parseObject(requestBody);
        
        logger.info("报单请求入参："+requestBody+"; 请求id："+nRequestID);
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        //期货公司代码
        inputOrderField.setBrokerID(json.getString("brokerID"));
        //投资者代码
        inputOrderField.setInvestorID(json.getString("investorID"));
        // 合约代码
        inputOrderField.setInstrumentID(json.getString("instrumentID"));
        ///报单引用     
        inputOrderField.setOrderRef(json.getString("orderRef"));
        // 操作人代码
        inputOrderField.setUserID(json.getString("userID"));
        // 报单价格条件
        inputOrderField.setOrderPriceType(json.getString("orderPriceType").toCharArray()[0]);
        // 买卖方向
        inputOrderField.setDirection(json.getString("direction").toCharArray()[0]);
        // 组合开平标志
        inputOrderField.setCombOffsetFlag(json.getString("combOffsetFlag"));
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag(json.getString("combHedgeFlag"));
        // 价格
        inputOrderField.setLimitPrice(json.getDoubleValue("limitPrice"));
        // 数量
        inputOrderField.setVolumeTotalOriginal(json.getIntValue("volumeTotalOriginal"));
        // 有效期类型
        inputOrderField.setTimeCondition(json.getString("timeCondition").toCharArray()[0]);
        // GTD日期
        inputOrderField.setGTDDate(json.getString("GTDDate"));
        // 成交量类型
        inputOrderField.setVolumeCondition(json.getString("volumeCondition").toCharArray()[0]);
        // 最小成交量
        inputOrderField.setMinVolume(json.getIntValue("minVolume"));
        // 触发条件
        inputOrderField.setContingentCondition(json.getString("contingentCondition").toCharArray()[0]);
        // 止损价
        inputOrderField.setStopPrice(json.getDoubleValue("stopPrice"));
        // 强平原因
        inputOrderField.setForceCloseReason(json.getString("forceCloseReason").toCharArray()[0]);
        // 自动挂起标志
        inputOrderField.setIsAutoSuspend(json.getIntValue("isAutoSuspend"));
        
        int result = traderApi.reqOrderInsert(inputOrderField, Integer.parseInt(nRequestID));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("reqOrderInsert请求： "+result+JSON.toJSONString(inputOrderField)+"\r\n");
            }
        });
        
        return result;
    }
    
    
    
    /**-----------------------------------------------------------------------------------------------------------------------------**/
    /**
     * 以下为响应
     */
    
    
    /**
     * 登录请求响应
     * @param pRspUserLogin
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast){
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspUserLogin|");
            //响应返回参数
            sb.append(JSON.toJSONString(pRspUserLogin)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }


            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspUserLogin响应： "+JSON.toJSONString(pRspUserLogin)+JSON.toJSONString(pRspInfo)+"\r\n");
                    coreAppText.append("onRspUserLogin响应： "+sb.toString()+"\r\n");
                }
            });
            
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
        
    }
    
    /**
     * 登出请求响应
     * @param pUserLogout
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("onRspUserLogout响应： "+JSON.toJSONString(pUserLogout)+JSON.toJSONString(pRspInfo)+"\r\n");
            }
        });
    }
    
    /**
     * 报单通知
     * @param pOrder
     */
    public void onRtnOrder(CThostFtdcOrderField pOrder) {
        
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRtnOrder|");
            //响应返回参数
            sb.append(JSON.toJSONString(pOrder));
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRtnOrder响应： "+JSON.toJSONString(pOrder)+"\r\n");
                    coreAppText.append("onRtnOrder响应： "+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 报单录入请求响应
     * @param pInputOrder
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspOrderInsert|");
            //响应返回参数
            sb.append(JSON.toJSONString(pInputOrder)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("响应：报单 "+JSON.toJSONString(pInputOrder)+"\r\n");
                    coreAppText.append("响应：撤单 "+sb.toString()+"\r\n");
                }
            });
            
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 报单操作请求响应
     * @param pInputOrderAction
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspOrderAction(
            CThostFtdcInputOrderActionField pInputOrderAction,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspOrderAction|");
            //响应返回参数
            sb.append(JSON.toJSONString(pInputOrderAction)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspOrderAction响应： "+JSON.toJSONString(pInputOrderAction)+"\r\n");
                    coreAppText.append("onRspOrderAction响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 成交通知
     * @param pTrade
     */
    public void onRtnTrade(CThostFtdcTradeField pTrade) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRtnTrade|");
            //响应返回参数
            sb.append(JSON.toJSONString(pTrade));
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRtnTrade响应： "+JSON.toJSONString(pTrade)+"\r\n");
                    coreAppText.append("onRtnTrade响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
        
    }
    
    /**
     * 请求查询投资者持仓明细响应
     * @param pInvestorPositionDetail
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspQryInvestorPositionDetail(
            CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspQryInvestorPositionDetail|");
            //响应返回参数
            sb.append(JSON.toJSONString(pInvestorPositionDetail)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspQryInvestorPositionDetail响应： "+JSON.toJSONString(pInvestorPositionDetail)+"\r\n");
                    coreAppText.append("onRspQryInvestorPositionDetail响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
        
    }
    
    /**
     * 请求查询投资者持仓响应
     * @param pInvestorPosition
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspQryInvestorPosition(
            CThostFtdcInvestorPositionField pInvestorPosition,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspQryInvestorPosition|");
            //响应返回参数
            sb.append(JSON.toJSONString(pInvestorPosition)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspQryInvestorPosition响应： "+JSON.toJSONString(pInvestorPosition)+"\r\n");
                    coreAppText.append("onRspQryInvestorPosition响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 投资者结算结果确认响应
     * @param pSettlementInfoConfirm
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspSettlementInfoConfirm(
            CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspSettlementInfoConfirm|");
            //响应返回参数
            sb.append(JSON.toJSONString(pSettlementInfoConfirm)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspSettlementInfoConfirm响应： "+JSON.toJSONString(pSettlementInfoConfirm)+"\r\n");
                    coreAppText.append("onRspSettlementInfoConfirm响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 错误应答
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
            boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspError|");
            //响应返回参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspError响应： "+JSON.toJSONString(pRspInfo)+"\r\n");
                    coreAppText.append("onRspError响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
        
    }
    
    /**
     * 报单录入错误回报
     * @param pInputOrder
     * @param pRspInfo
     */
    public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            System.out.println("DDDDDDDDDDDDDD"+pRspInfo.getErrorMsg());
        	
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onErrRtnOrderInsert|");
            //响应返回参数
            sb.append(JSON.toJSONString(pInputOrder)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo));
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onErrRtnOrderInsert响应： "+JSON.toJSONString(pInputOrder)+"\r\n");
                    coreAppText.append("onErrRtnOrderInsert响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
        
    }

    /**
     * 请求查询资金账户响应
     * @param pTradingAccount
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        try {
            //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
            
            StringBuffer sb = new StringBuffer();
            //响应方法名
            sb.append("onRspQryTradingAccount|");
            //响应返回参数
            sb.append(JSON.toJSONString(pTradingAccount)+"|");
            //响应错误参数
            sb.append(JSON.toJSONString(pRspInfo)+"|");
            //nRequestID
            sb.append(nRequestID+"|");
            //bIsLast
            sb.append(bIsLast);
            if(out != null){
                out.println(sb.toString());
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    ctpText.append("onRspQryTradingAccount响应： "+JSON.toJSONString(pTradingAccount)+"\r\n");
                    coreAppText.append("onRspQryTradingAccount响应："+sb.toString()+"\r\n");
                }
            });
        } catch (Exception e) {
            logger.error("获取输出流失败",e);
        }
    }
    
    /**
     * 查询合约信息返回
     * @param pInstrument
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo,
            int nRequestID, boolean bIsLast) {
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ctpText.append("查询合约返回"+JSON.toJSONString(pInstrument)+"\r\n");
            }
        });
    }
    
    
}
