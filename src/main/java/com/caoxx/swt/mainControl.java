package com.caoxx.swt;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_AF_Delete;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
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
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caoxx.api.MyMdSpi;
import com.caoxx.api.MyTraderSpiTest;
import com.caoxx.dao.FutureMarketDao;
import com.caoxx.dao.Minute1MA20Dao;
import com.caoxx.dao.OrderDetail;
import com.caoxx.dao.PositionsDetail2;
import com.caoxx.entity.Instrument;
import com.caoxx.entity.Order;
import com.caoxx.entity.Trade;



public class mainControl {
	
	public int sessionID = 0;
	public  int frontID = 0;

	 static final String MARKET_IP = "tcp://180.168.146.187";
	 static final int MARKET_PORT = 10001;
	static Logger logger = Logger.getLogger(CaoxxApp.class);
	public JCTPTraderApi traderApi;
	public JCTPTraderSpi traderSpi;
    public static JCTPMdApi mdApi;
	protected Shell shell;
	private Text oracleRet;
	
	private List<Socket> clients;
	
	private Map<String, PositionsDetail2> mapHoldContractMemorySave;
	
	private Map<String, OrderDetail> mapOrderMemorySave;
	
	public static AtomicInteger atomicInteger;
	
	public int nRequestID = 0;
	public Text getOracleRet() {
		return oracleRet;
	}

	public void setOracleRet(Text oracleRet) {
		this.oracleRet = oracleRet;
	}
	private Combo combo;
	
	private Table marketTable; 
	
	private Socket marketSocket;

	public String url;
    public String brokenId;
    public String investorNo;
    public String passwd;
    public Text console;
    public double nProfit = 6;
    
    public double adverseValue = 0.3;
    
    public double pingcangshu = 3;
    
    public double maxProfitValue = 0;
    
    public Map<String,Instrument> m20To1Map = new HashMap<String,Instrument>();
//    public Map<String,Instrument> m20To3Map = new HashMap<String,Instrument>();
//    public Map<String,Instrument> m20To5Map = new HashMap<String,Instrument>();
    
    public String newrb1810;
    

	public String getNewrb1810() {
		return newrb1810;
	}

	public void setNewrb1810(String newrb1810) {
		this.newrb1810 = newrb1810;
	}

	public String getNewm1809() {
		return newm1809;
	}

	public void setNewm1809(String newm1809) {
		this.newm1809 = newm1809;
	}
	
	static ArrayList<String> HYname = new ArrayList<String>();
	
    public static ArrayList<String> HYnameAM1 = new ArrayList<String>();
	
	public static ArrayList<String> HYnamePM23 = new ArrayList<String>();
	
	public static ArrayList<String> HYnamePM2330 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameAM230 = new ArrayList<String>();
	
	public static ArrayList<String> HYname000 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameMinValue1 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameMinValue2 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameMinValue5 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameMinValue10 = new ArrayList<String>();
	
	public static ArrayList<String> HYnameMinValue005 = new ArrayList<String>();

	public String newm1809;
    
    public static FutureMarketDao futureMarketDao;
    
    public static Minute1MA20Dao minute1MA20Dao;
    private Text ctpConsole;
    private Table table;
    private Text orderRetText;
	
	 public Text getOrderRetText() {
		return orderRetText;
	}

	public void setOrderRetText(Text orderRetText) {
		this.orderRetText = orderRetText;
	}

	public mainControl(String url, String brokenId, String investorNo, String passwd){
	        this.url = url;
	        this.brokenId = brokenId;
	        this.investorNo = investorNo;
	        this.passwd = passwd;
	    }
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			//上海合约
			HYname.add("cu");
			HYname.add("al");
			HYname.add("zn");
			HYname.add("pb");
			HYname.add("ru");
			HYname.add("fu");
			HYname.add("rb");
			HYname.add("wr");
			HYname.add("au");
			HYname.add("ag");
			HYname.add("bu");
			HYname.add("hc");
			HYname.add("ni");
			HYname.add("sn");
			
			//以下是三家交易所合约开盘时间整理
			//AM1:00点收盘的
			HYnameAM1.add("cu");
			HYnameAM1.add("al");
			HYnameAM1.add("zn");
			HYnameAM1.add("pb");
			HYnameAM1.add("ni");
			HYnameAM1.add("sn");
			
			//PM23:00点收盘的
			HYnamePM23.add("rb");
			HYnamePM23.add("hc");
			HYnamePM23.add("bu");
			HYnamePM23.add("ru");
			
			//PM23:30点收盘的
			HYnamePM2330.add("m");
			HYnamePM2330.add("y");
			HYnamePM2330.add("a");
			HYnamePM2330.add("b");
			HYnamePM2330.add("p");
			HYnamePM2330.add("j");
			HYnamePM2330.add("jm");
			HYnamePM2330.add("i");
			HYnamePM2330.add("sr");
			HYnamePM2330.add("cf");
			HYnamePM2330.add("cy");
			HYnamePM2330.add("zc");
			HYnamePM2330.add("fg");
			HYnamePM2330.add("ta");
			HYnamePM2330.add("ma");
			HYnamePM2330.add("oi");
			HYnamePM2330.add("rm");
			HYnamePM2330.add("cf");
			
			
			//AM2:30点收盘的
			HYnameAM230.add("au");
			HYnameAM230.add("ag");
			
			//无夜盘的
			HYname000.add("c");
			HYname000.add("cs");
			HYname000.add("jd");
			HYname000.add("bb");
			HYname000.add("fb");
			HYname000.add("l");
			HYname000.add("v");
			HYname000.add("wh");
			HYname000.add("pm");
			HYname000.add("ri");
			HYname000.add("lr");
			HYname000.add("jr");
			HYname000.add("rs");
			HYname000.add("sf");
			HYname000.add("sm");
			HYname000.add("ap");
			
			//合约最小变动单位
			HYnameMinValue1.add("fg");
			HYnameMinValue1.add("rb");
			HYnameMinValue1.add("wr");
			HYnameMinValue1.add("a");
			HYnameMinValue1.add("b");
			HYnameMinValue1.add("c");
			HYnameMinValue1.add("m");
			HYnameMinValue1.add("ws");
			HYnameMinValue1.add("wt");
			HYnameMinValue1.add("sr");
			HYnameMinValue1.add("er");
			
			
			HYnameMinValue10.add("cu");
			HYnameMinValue10.add("ni");
			
			HYnameMinValue5.add("al");
			HYnameMinValue5.add("zn");
			HYnameMinValue5.add("ru");
			HYnameMinValue5.add("v");
			HYnameMinValue5.add("cf");
			HYnameMinValue5.add("l");
			
			
			HYnameMinValue2.add("y");
			HYnameMinValue2.add("p");
			HYnameMinValue2.add("ta");
			HYnameMinValue2.add("ro");
			
			HYnameMinValue005.add("au");
			
			
			
			AbstractApplicationContext context =
	                new ClassPathXmlApplicationContext("spring-jdbc.xml");
			futureMarketDao = (FutureMarketDao) context.getBean("futureMarketDao");
			minute1MA20Dao = (Minute1MA20Dao) context.getBean("minute1MA20Dao");
			
			atomicInteger = new AtomicInteger(200);

			
			
			
						
			mainControl window = new mainControl("tcp://180.168.146.187:10000","9999","119835","cao830107");
			Thread ctp = new Thread(new Ctp(window));
	        ctp.setDaemon(true);
	        ctp.start();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkSHPosition(String HyName) {
		//check 持仓是否是上海的。只有上海要平今平昨
		boolean retFlg = false;
		if(HyName.length() == 6){			
			for (String hyName : HYname) {
				if(HyName.substring(0, 2).equals(hyName)){
					//上海
					return true;
				}
			}
			
		}
		return retFlg;
		
	}
    public static class Ctp implements Runnable{
        
        private mainControl mainControl;
        
        public Ctp(mainControl mainControl){
            this.mainControl = mainControl;
        }

        @Override
        public void run() {
        	 mdApi = JCTPMdApi.createFtdcTraderApi();
            
            JCTPMdSpi mdSpi = new MyMdSpi(mdApi,mainControl);
            //注册spi
            mdApi.registerSpi(mdSpi);
            //注册前置机地址
            mdApi.registerFront("tcp://180.168.146.187:10010");
            mdApi.Init();
            
            mdApi.Join();
            
//          TimeUnit.SECONDS.sleep(5);
            mdApi.Release();
            
            
        }
        
    }

	/**
	 * Open the window.
	 */
	public void open() {
		
		mapHoldContractMemorySave = new HashMap<String, PositionsDetail2>();
		
		mapOrderMemorySave = new HashMap<String, OrderDetail>();
		
		
		// 服务端线程
		Thread serverThread = new Thread(new ServerThread(this));
		serverThread.setDaemon(true);
		serverThread.start();
		
		
		Display display = Display.getDefault();
		createContents();
		shell.open();
		
		
        
        //行情接收线程
		
		
		shell.layout();
		
		
		Thread thread = new Thread(new ConnectCTP());
        thread.setDaemon(true);
        thread.start();
		
		
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
		shell.setSize(741, 529);
		shell.setLayout(null);
		shell.setText("指标机控制台");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 10, 500, 207);
		
		Label lblCtp = new Label(composite_1, SWT.NONE);
		lblCtp.setBounds(0, 0, 82, 17);
		lblCtp.setText("CTP行情接收：");
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setBounds(249, 0, 61, 17);
		label.setText("推送指标：");
		
		oracleRet = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		oracleRet.setBounds(247, 22, 253, 185);
		
		console = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		console.setBounds(0, 21, 243, 186);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setBounds(0, 223, 500, 113);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(0, 0, 77, 17);
		lblNewLabel.setText("接入信息：");
		
		ctpConsole = new Text(composite_3, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		ctpConsole.setBounds(0, 20, 490, 83);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 342, 500, 138);
		
		table = new Table(composite, SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 3) {
					int colnum =0;
					//得到一行
					TableItem item = table.getItem(new Point(e.x, e.y)); 
					//得到一个单元格
					item.getText(colnum);
				}
			}
		});
		table.setBounds(0, 0, 500, 128);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("合约名");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("指标名");
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("最新值");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("最新时间");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("开关");
		
		orderRetText = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		orderRetText.setBounds(506, 32, 219, 448);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(506, 10, 61, 17);
		label_1.setText("下单回报：");

	}
	
	
	public class ConnectCTP implements Runnable{

        @Override
        public void run() {
            
            String dataPath = "ctpdata/guitest/";
            
            traderApi = JCTPTraderApi.createFtdcTraderApi();
            traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);

            traderSpi = new MyTraderSpiTest(traderApi,mainControl.this);
            
            //注册traderpi
            traderApi.registerSpi(traderSpi);
            //注册公有流
            traderApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册私有流
            traderApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册前置机地址
            traderApi.registerFront(url);
            
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
	
	
	
	
	/**
     * 用户登录请求
     * 
     * @param pReqUserLoginField
     * @param nRequestID
     * @return 请求发送状态
     */
    public int reqUserLogin() {
        
        CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
        userLoginField.setBrokerID(brokenId);
        userLoginField.setUserID(investorNo);
        userLoginField.setPassword(passwd);
        int result = traderApi.reqUserLogin(userLoginField, 0);
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
            	ctpConsole.append(LocalDateTime.now()+"发送用户登录请求\r\n");
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
            	ctpConsole.append(LocalDateTime.now()+"发送确人结算清单请求 \r\n");
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
    public int reqUserLogout() {
        
        CThostFtdcUserLogoutField pUserLogout = new CThostFtdcUserLogoutField();
        pUserLogout.setBrokerID(brokenId);
        pUserLogout.setUserID(investorNo);
        
        return traderApi.reqUserLogout(pUserLogout, 0);
    }
    
    /**
     * 撤单请求
     * 
     * @param pInputOrderAction
     * @param nRequestID
     * @return
     */
    public int reqOrderAction() {

        /*CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
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
                console.append("reqOrderAction请求： "+result+JSON.toJSONString(pInputOrderAction)+"\r\n");
            }
        });*/
        
        return 0;
    }
    
    
    /**
     * 报单录入请求
     * 
     * @param pInputOrder
     * @param nRequestID
     * @return
     */
    public int reqOrderInsert() {
        
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        //期货公司代码
        inputOrderField.setBrokerID(brokenId);
        //投资者代码
        inputOrderField.setInvestorID(investorNo);
        // 合约代码
//        inputOrderField.setInstrumentID(instrumentText.getText());
        inputOrderField.setInstrumentID("");
        ///报单引用     
        //inputOrderField.setOrderRef(json.getString("orderRef"));
        // 操作人代码
        inputOrderField.setUserID(investorNo);
        // 报单价格条件   限价
        inputOrderField.setOrderPriceType('2');
        // 买卖方向
//        inputOrderField.setDirection(combo.getText().equals("买") ? '0' : '1');
        inputOrderField.setDirection('1');
        // 组合开平标志
        inputOrderField.setCombOffsetFlag("0");
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag("1");
        // 价格
//        inputOrderField.setLimitPrice(Double.parseDouble(priceText.getText()));
        inputOrderField.setLimitPrice(0);
        // 数量
        inputOrderField.setVolumeTotalOriginal(1);
        // 有效期类型    当日有效
        inputOrderField.setTimeCondition('3');
        // GTD日期
        inputOrderField.setGTDDate("");
        // 成交量类型  任意数量
        inputOrderField.setVolumeCondition('1');
        // 最小成交量
        inputOrderField.setMinVolume(1);
        // 触发条件   立刻
        inputOrderField.setContingentCondition('1');
        // 止损价
        //inputOrderField.setStopPrice(0);
        // 强平原因
        inputOrderField.setForceCloseReason('0');
        // 自动挂起标志
        //inputOrderField.setIsAutoSuspend(json.getIntValue("isAutoSuspend"));
        
        int result = traderApi.reqOrderInsert(inputOrderField, 0);
        
        ctpConsole.append(LocalDateTime.now()+"发送报单请求\r\n");
        
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
        
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append(LocalDateTime.now()+"收到登录响应： "+JSON.toJSONString(pRspUserLogin)+JSON.toJSONString(pRspInfo)+"\r\n");
                }
            });
            
        
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
            	ctpConsole.append("onRspUserLogout响应： "+JSON.toJSONString(pUserLogout)+JSON.toJSONString(pRspInfo)+"\r\n");
            }
        });
    }
    
    
//    public void onRtnOrder(CThostFtdcOrderField pOrder) {
//        
//        
//            Display.getDefault().syncExec(new Runnable() {
//                
//                @Override
//                public void run() {
//                	ctpConsole.append(LocalDateTime.now()+"收到报单响应，报单状态 :"+pOrder.getStatusMsg()+"\r\n");
//                }
//            });
//    }
    
    /**
     * 报单录入请求响应
     * @param pInputOrder
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append(LocalDateTime.now()+"收到报单错误响应 "+JSON.toJSONString(pRspInfo)+"\r\n");
                }
            });
            
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
        
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("onRspOrderAction响应： "+JSON.toJSONString(pInputOrderAction)+"\r\n");
                }
            });
    }
    
    /**
     * 成交通知
     * @param pTrade
     */
    public void onRtnTrade(CThostFtdcTradeField pTrade) {
        
    	logger.info("成交");
		logger.info(JSON.toJSONString(pTrade));
		
		// 如果该报单由交易所进行了撮合成交，交易所再次返回该报单的状态（已成交）。并通过此函数返回该笔成 交。
				// 报单成交之后，一个报单回报（OnRtnOrder）和一个成交回报（OnRtnTrade）会被发送到客户端，报单回报
				// 中报单的状态为“已成交”。但是仍然建议客户端将成交回报作为报单成交的标志，因为 CTP 的交易核心在 收到 OnRtnTrade
				// 之后才会更新该报单的状态。如果客户端通过报单回报来判断报单成交与否并立即平仓，有 极小的概率会出现在平仓指令到达 CTP
				// 交易核心时该报单的状态仍未更新，就会导致无法平仓。
				
		Instrument 	instrument = m20To1Map.get(pTrade.getInstrumentID());

		instrument.setStatus("2");
		instrument.setTradeValue(String.valueOf(pTrade.getPrice()));
		instrument.setDirection(pTrade.getDirection());
		Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
            	getOrderRetText().append("成交|" + pTrade.getInstrumentID() + "|" + String.valueOf(pTrade.getPrice()) +"|"+pTrade.getTradingDay() +"\r\n");
            }
        });
		
		//合约已成交 进入成交判断模式
		

				logger.info("成交step1");
				// 插入T_TRADE数据
				Trade trade = new Trade();
				trade.setBrokerid(pTrade.getBrokerID());
				trade.setInvestorid(pTrade.getInvestorID());
				trade.setInstrumentid(pTrade.getInstrumentID());
				trade.setOrderref(pTrade.getOrderRef());
				trade.setUserid(pTrade.getUserID());
				trade.setExchangeid(pTrade.getExchangeID());
				trade.setTradeid(pTrade.getTradeID());
				trade.setDirection(String.valueOf(pTrade.getDirection()));
				trade.setOrdersysid(pTrade.getOrderSysID());
				trade.setParticipantid(pTrade.getParticipantID());
				trade.setClientid(pTrade.getClientID());
				trade.setTradingrole(String.valueOf(pTrade.getTradingRole()));
				trade.setExchangeinstid(pTrade.getExchangeInstID());
				trade.setOffsetflag(String.valueOf(pTrade.getOffsetFlag()));
				trade.setHedgeflag(String.valueOf(pTrade.getHedgeFlag()));
				trade.setPrice(new BigDecimal(String.valueOf(pTrade.getPrice())));
				trade.setVolume(Long.valueOf(String.valueOf(pTrade.getVolume())));
				trade.setTradedate(pTrade.getTradeDate());
				trade.setTradetime(pTrade.getTradeTime());
				trade.setTradetype(String.valueOf(pTrade.getTradeType()));
				trade.setPricesource(String.valueOf(pTrade.getPriceSource()));
				trade.setTraderid(pTrade.getTraderID());
				trade.setOrderlocalid(pTrade.getOrderLocalID());
				trade.setClearingpartid(pTrade.getClearingPartID());
				trade.setBusinessunit(pTrade.getBusinessUnit());
				trade.setSequenceno(Long.valueOf(pTrade.getSequenceNo()));
				trade.setTradingday(pTrade.getTradingDay());
				trade.setSettlementid(Long.valueOf(String.valueOf(pTrade.getSettlementID())));
				trade.setBrokerorderseq(Long.valueOf(String.valueOf(pTrade.getBrokerOrderSeq())));
				trade.setTradesource(String.valueOf(pTrade.getTradeSource()));
				trade.setFrontid(new BigDecimal(frontID));
				trade.setSessionid(new BigDecimal(sessionID));
  
        
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
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("onRspQryInvestorPositionDetail响应： "+JSON.toJSONString(pInvestorPositionDetail)+"\r\n");
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
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("onRspQryInvestorPosition响应： "+JSON.toJSONString(pInvestorPosition)+"\r\n");
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
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("收到结算清单 确认响应\r\n");
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
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("onRspError响应： "+JSON.toJSONString(pRspInfo)+"\r\n");
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
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append(LocalDateTime.now()+"收到报单录入错误响应： "+JSON.toJSONString(pRspInfo)+"\r\n");
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
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	ctpConsole.append("onRspQryTradingAccount响应： "+JSON.toJSONString(pTradingAccount)+"\r\n");
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
            	ctpConsole.append("查询合约返回"+JSON.toJSONString(pInstrument)+"\r\n");
            }
        });
    }
    
    public Socket getSocket() {
        return marketSocket;
    }

    public void setSocket(Socket socket) {
        this.marketSocket = socket;
    }
    
    public void recreateSocket(){
        
        if(marketSocket != null){
            try {
                marketSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            this.marketSocket = new Socket(MARKET_IP,10001);
        } catch (Exception e) {
            logger.error("与行情服务器通信失败",e);
        }
    }
    
    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
    
    public void initMarketSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(marketSocket == null || !(marketSocket.isConnected() == true && marketSocket.isClosed() == false)){
            try {
                logger.info("=====================开始与行情服务器建立连接==============");
                marketSocket = new Socket(MARKET_IP,MARKET_PORT);
                marketSocket.setKeepAlive(false);
            } catch (Exception e) {
                logger.error("与行情服务器通信失败",e);
            }
        }
    }
    
    public int insertFutureMarket(CThostFtdcDepthMarketDataField cThostFtdcDepthMarketDataField){

    	
    	
    	futureMarketDao.insert(cThostFtdcDepthMarketDataField);
    	
    	futureMarketDao.insertBak(cThostFtdcDepthMarketDataField);
    	
    	return 0;
    	
    }
    
    public synchronized void addClient(Socket client) {
		if (clients == null) {
			clients = new ArrayList<Socket>();
		}
		clients.add(client);
	}
	
	public synchronized void removeClient(Socket client){
	    
	    if (clients != null) {
	        clients.remove(client);
	    }
	}
	
	 public static double formatDouble1(double d) {  
	        return (double)Math.round(d);  
	    }  
	
	public void calaMA20(String calaStr){
		String[] splitStr = calaStr.split("\\|");
		String seqID = splitStr[1];
		String instrumentId = splitStr[2];
		String openPrice = splitStr[3];
		String closePrice = splitStr[4];
		String highestPrice = splitStr[5];
		String lowestPrice = splitStr[6];
		String avgPrice = splitStr[7];
		String insertTime = splitStr[8];
		
		//MA20 取得当前 seqid 取前20个数据 取收盘价
		List<String> listTest =  minute1MA20Dao.select1MA20Avg(Integer.parseInt(seqID));
		String str = listTest.get(0);
		double ma20Num = Double.valueOf(listTest.get(0));
		ma20Num = formatDouble1(ma20Num);
		
		// 先撤挂单 根据合约区分
		OrderDetail orderDetail = new OrderDetail();
		orderDetail = mapOrderMemorySave.get(instrumentId);
		if(orderDetail != null | orderDetail.getOrderStatus().equals("")  ){
			subOrderAction(orderDetail.getOrderRef(),orderDetail.getFrontId(),orderDetail.getSessionId());
			
		}
		
		
		
		if(instrumentId.trim().equals("rb1810")) {
			// if 当前值 > MA20值 买开 MA20 价位
			if(ma20Num > Double.valueOf(getNewrb1810())){
				subOrder(instrumentId,'1',"0",ma20Num);
			}
			
			if(ma20Num < Double.valueOf(getNewrb1810())){
				subOrder(instrumentId,'0',"0",ma20Num);
			}
		}
		
		if(instrumentId.trim().equals("m1809")) {
			// if 当前值 > MA20值 买开 MA20 价位
			if(ma20Num > Double.valueOf(getNewm1809())){
				subOrder(instrumentId,'1',"0",ma20Num);
			}
			
			if(ma20Num < Double.valueOf(getNewm1809())){
				subOrder(instrumentId,'0',"0",ma20Num);
			}
		}
		
	}
	
	public void subOrderAction(String orderRef,int frontId,int sessionId) {

		
		CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
		logger.info("撤单操作");

		pInputOrderAction.setBrokerID(brokenId);
		pInputOrderAction.setInvestorID(investorNo);
		// caoxx2 start orderSysID + exchangeID 撤单
		pInputOrderAction.setOrderRef(orderRef);
		pInputOrderAction.setFrontID(frontId);
		pInputOrderAction.setSessionID(sessionId);
		
		// caoxx2 end
		// 补丁
		/// 操作标志
		pInputOrderAction.setActionFlag(THOST_FTDC_AF_Delete);

		traderApi.reqOrderAction(pInputOrderAction, ++nRequestID);

	}
	
	public void subOrder(String instrumentId,char direction,String offset,double openPrice) {
		logger.info("下单操作");
		
		int tmpint = atomicInteger.incrementAndGet();
		String order = String.valueOf(tmpint);

		//下单操作
		CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
		//期货公司代码
		inputOrderField.setBrokerID(brokenId);
		//投资者代码
		inputOrderField.setInvestorID(investorNo);
		// 合约代码
		inputOrderField.setInstrumentID(instrumentId);
		///报单引用
		inputOrderField.setOrderRef(order);
		// 用户代码
		inputOrderField.setUserID(investorNo);
		// 报单价格条件
		inputOrderField.setOrderPriceType('2');
		// 买卖方向
		inputOrderField.setDirection(direction);
		// 组合开平标志
		inputOrderField.setCombOffsetFlag(offset);
		// 组合投机套保标志
		inputOrderField.setCombHedgeFlag("1");
		// 价格
		inputOrderField.setLimitPrice(openPrice);
		// 数量
		inputOrderField.setVolumeTotalOriginal(1);
		// 有效期类型
		inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
		// GTD日期
		inputOrderField.setGTDDate("");
		// 成交量类型
		inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
		// 最小成交量
		inputOrderField.setMinVolume(0);
		// 触发条件
		inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
		// 止损价
		inputOrderField.setStopPrice(0);
		// 强平原因
		inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
		// 自动挂起标志
		inputOrderField.setIsAutoSuspend(0);
		
		traderApi.reqOrderInsert(inputOrderField, ++nRequestID);
		
//		PositionsDetail2 positionsDetail2 = new PositionsDetail2();
		
		OrderDetail orderDetail = new OrderDetail();
		
		String instrumentidStr = "";
		instrumentidStr = instrumentId;
		orderDetail.setInstrumentid(instrumentidStr);
		orderDetail.setDirection(String.valueOf(direction));
		orderDetail.setVolume(Long.valueOf(1));
		orderDetail.setOpenPrice(openPrice);
		orderDetail.setOrderRef(order);
		
		
		logger.info("挂单记录");
//		mapHoldContractMemorySave.put(instrumentidStr, positionsDetail2);
		mapOrderMemorySave.put(instrumentidStr, orderDetail);
	
	}
	
	/**
     * 报单通知
     * @param pOrder
     */
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
		// 交易系统返回的报单状态，每次报单状态发生变化时被调用。一次报单过程中会被调用数次：交易系统将报 单向交易所提交时（上述流程 8 中的第 2
		// 个过程），交易所撤销或接受该报单时，该报单成交时。
		// 8.2， 交易核心向交易前置发送了第一个报单回报后，立即产生向交易所申请该报单插入的申请报文，该报文 被报盘管理订阅。
		
		// pOrder.getOrderStatus 状态
		// 'b' 尚未触发 预埋单等尚未到触发条件下单条件，客户端还未执行下单动作
		// 'a' 未知
		// '0' 全部成交 已全部成交
		// '1' 部分成交还在队列中 部分成交，剩余部分等待成交
		// '2' 部分成交还在队列中 部分成交，剩余部分已撤单
		// '3' 未成交还在队列中 报单已发往交易所正在等待成交
		// '4' 未成交还在队列中 报单未发往交易所
		// '5' 撤单 已全部撤单
		
		
		if(pOrder.getOrderStatus() == '3'){
			//挂单
			Instrument Instrument = m20To1Map.get(pOrder.getInstrumentID());

			Instrument.setFrontID(pOrder.getFrontID());
			Instrument.setSessionID(pOrder.getSessionID());
			Instrument.setOrderRef(pOrder.getOrderRef());
		}
		
		if(pOrder.getOrderStatus() == '5'){
			//挂单
//			Instrument Instrument = m20To1Map.get(pOrder.getInstrumentID());
//			Instrument.setStatus("");
			
		}
		
		///全部成交
//		#define TSHFE_FTDC_OST_AllTraded '0'
//		///部分成交还在队列中
//		#define TSHFE_FTDC_OST_PartTradedQueueing '1'
//		///部分成交不在队列中
//		#define TSHFE_FTDC_OST_PartTradedNotQueueing '2'
//		///未成交还在队列中
//		#define TSHFE_FTDC_OST_NoTradeQueueing '3'
//		///未成交不在队列中
//		#define TSHFE_FTDC_OST_NoTradeNotQueueing '4'
//		///撤单
//		#define TSHFE_FTDC_OST_Canceled '5'
//		///未知
//		#define TSHFE_FTDC_OST_Unknown 'a'
//		///尚未触发
//		#define TSHFE_FTDC_OST_NotTouched 'b'
//		///已触发
//		#define TSHFE_FTDC_OST_Touched 'c'
//		///错单//自定义添加
//		#define TSHFE_FTDC_OST_Error 'e'
		
		

		// 插入T_ORDER数据
//		Order order = new Order();
//		order.setBrokerid(pOrder.getBrokerID());
//		order.setInvestorid(pOrder.getInvestorID());
//		order.setInstrumentid(pOrder.getInstrumentID());
//		order.setOrderref(pOrder.getOrderRef());
//		order.setUserid(pOrder.getUserID());
//		order.setOrderpricetype(String.valueOf(pOrder.getOrderPriceType()));
//		order.setDirection(String.valueOf(pOrder.getDirection()));
//		order.setComboffsetflag(pOrder.getCombOffsetFlag());
//		order.setCombhedgeflag(pOrder.getCombHedgeFlag());
//		order.setLimitprice(new BigDecimal(String.valueOf(pOrder.getLimitPrice())));
//		order.setVolumetotaloriginal(Long.valueOf(String.valueOf(pOrder.getVolumeTotalOriginal())));
//		order.setTimecondition(String.valueOf(pOrder.getTimeCondition()));
//		order.setGtddate(pOrder.getGTDDate());
//		order.setVolumecondition(String.valueOf(pOrder.getVolumeCondition()));
//		order.setMinvolume(Long.valueOf(String.valueOf(pOrder.getMinVolume())));
//		order.setContingentcondition(String.valueOf(pOrder.getContingentCondition()));
//		order.setStopprice(new BigDecimal(pOrder.getStopPrice()));
//		order.setForceclosereason(String.valueOf(pOrder.getForceCloseReason()));
//		order.setIsautosuspend(Long.valueOf(String.valueOf(pOrder.getIsAutoSuspend())));
//		order.setBusinessunit(pOrder.getBusinessUnit());
//		order.setRequestid(Long.valueOf(String.valueOf(pOrder.getRequestID())));
//		order.setOrderlocalid(pOrder.getOrderLocalID());
//		order.setExchangeid(pOrder.getExchangeID());
//		order.setParticipantid(pOrder.getParticipantID());
//		order.setClientid(pOrder.getClientID());
//		order.setExchangeinstid(pOrder.getExchangeInstID());
//		order.setTraderid(pOrder.getTraderID());
//		order.setInstallid(Long.valueOf(String.valueOf(pOrder.getInstallID())));
//		order.setOrdersubmitstatus(String.valueOf(pOrder.getOrderSubmitStatus()));
//		order.setNotifysequence(Long.valueOf(String.valueOf(pOrder.getNotifySequence())));
//		order.setTradingday(pOrder.getTradingDay());
//		order.setSettlementid(Long.valueOf(String.valueOf(pOrder.getSettlementID())));
//		order.setOrdersysid(pOrder.getOrderSysID());
//		order.setOrdersource(String.valueOf(pOrder.getOrderSource()));
//		order.setOrderstatus(String.valueOf(pOrder.getOrderStatus()));
//		order.setOrdertype(String.valueOf(pOrder.getOrderType()));
//		order.setVolumetraded(Long.valueOf(String.valueOf(pOrder.getVolumeTraded())));
//		order.setVolumetotal(Long.valueOf(String.valueOf(pOrder.getVolumeTotal())));
//		order.setInsertdate(pOrder.getInsertDate());
//		order.setInserttime(pOrder.getInsertTime());
//		order.setActivetime(pOrder.getActiveTime());
//		order.setSuspendtime(pOrder.getSuspendTime());
//		order.setUpdatetime(pOrder.getUpdateTime());
//		order.setCanceltime(pOrder.getCancelTime());
//		order.setActivetraderid(pOrder.getActiveTraderID());
//		order.setClearingpartid(pOrder.getClearingPartID());
//		order.setSequenceno(Long.valueOf(pOrder.getSequenceNo()));
//		order.setFrontid(new BigDecimal(pOrder.getFrontID()));
//		order.setSessionid(new BigDecimal(pOrder.getSessionID()));
//		order.setUserproductinfo(pOrder.getUserProductInfo());
//		order.setStatusmsg(pOrder.getStatusMsg());
//		order.setUserforceclose(Long.valueOf(String.valueOf(pOrder.getUserForceClose())));
//		order.setActiveuserid(pOrder.getActiveUserID());
//		order.setBrokerorderseq(Long.valueOf(String.valueOf(pOrder.getBrokerOrderSeq())));
//		order.setRelativeordersysid(pOrder.getRelativeOrderSysID());
//		order.setZcetotaltradedvolume(Long.valueOf(String.valueOf(pOrder.getZCETotalTradedVolume())));
//		order.setIsswaporder(Long.valueOf(String.valueOf(pOrder.getIsSwapOrder())));
//
//		
//		//确定是哪一个合约
//		OrderDetail orderDetail = new OrderDetail();
//		OrderDetail orderDetailnew = new OrderDetail();
//		orderDetail = mapOrderMemorySave.get(pOrder.getInstrumentID());
//		
//		if(orderDetail != null ){
//			orderDetailnew.setDirection(orderDetail.getDirection());
//			orderDetailnew.setFrontId(pOrder.getFrontID());
//			orderDetailnew.setInstrumentid(pOrder.getInstrumentID());
//			orderDetailnew.setOpenPrice(orderDetail.getOpenPrice());
//			orderDetailnew.setOrderRef(orderDetail.getOrderRef());
//			orderDetailnew.setOrderStatus(String.valueOf(pOrder.getOrderStatus()));
//			orderDetailnew.setSessionId(pOrder.getSessionID());
//			orderDetailnew.setVolume(orderDetail.getVolume());
//			mapOrderMemorySave.put(pOrder.getInstrumentID(), orderDetailnew);
//		}
		
		

//		try {
//			orderService.saveOrder(order);
//			//
//			e.printStackTrace();
//		}
		
	}
}
