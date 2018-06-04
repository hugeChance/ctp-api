package com.caoxx.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import swing2swt.layout.BorderLayout;
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
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.caoxx.api.MyTraderSpiTest;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class mainControl {

	static Logger logger = Logger.getLogger(CaoxxApp.class);
	JCTPTraderApi traderApi;
    JCTPTraderSpi traderSpi;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	
	private Text console;

	public String url;
    public String brokenId;
    public String investorNo;
    public String passwd;
	
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
			mainControl window = new mainControl("tcp://180.168.146.187:10000","9999","105839","caojiactp1");
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
		shell.setLayout(null);
		shell.setText("控制台");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 500, 46);
		
		Button button = new Button(composite, SWT.NONE);
		button.setLocation(420, 10);
		button.setSize(80, 27);
		button.setText("启    动");
		
		text = new Text(composite, SWT.BORDER);
		text.setLocation(105, 10);
		text.setSize(283, 23);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLocation(0, 10);
		lblNewLabel.setSize(97, 17);
		lblNewLabel.setText("CTP前置机地址：");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 47, 500, 207);
		
		text_1 = new Text(composite_1, SWT.BORDER);
		text_1.setBounds(0, 22, 241, 185);
		
		Label lblCtp = new Label(composite_1, SWT.NONE);
		lblCtp.setBounds(0, 0, 82, 17);
		lblCtp.setText("CTP行情接收：");
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setBounds(249, 0, 61, 17);
		label.setText("推送指标：");
		
		text_2 = new Text(composite_1, SWT.BORDER);
		text_2.setBounds(247, 22, 253, 185);
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setBounds(0, 260, 500, 143);
		
		Label label_1 = new Label(composite_2, SWT.NONE);
		label_1.setBounds(0, 0, 82, 17);
		label_1.setText("客户端接入：");
		
		text_3 = new Text(composite_2, SWT.BORDER);
		text_3.setBounds(0, 23, 500, 110);

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
                console.append(LocalDateTime.now()+"发送用户登录请求\r\n");
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
                console.append(LocalDateTime.now()+"发送确人结算清单请求 \r\n");
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
        
        console.append(LocalDateTime.now()+"发送报单请求\r\n");
        
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
                    console.append(LocalDateTime.now()+"收到登录响应： "+JSON.toJSONString(pRspUserLogin)+JSON.toJSONString(pRspInfo)+"\r\n");
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
                console.append("onRspUserLogout响应： "+JSON.toJSONString(pUserLogout)+JSON.toJSONString(pRspInfo)+"\r\n");
            }
        });
    }
    
    /**
     * 报单通知
     * @param pOrder
     */
    public void onRtnOrder(CThostFtdcOrderField pOrder) {
        
        
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    console.append(LocalDateTime.now()+"收到报单响应，报单状态 :"+pOrder.getStatusMsg()+"\r\n");
                }
            });
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
        
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    console.append(LocalDateTime.now()+"收到报单错误响应 "+JSON.toJSONString(pRspInfo)+"\r\n");
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
                    console.append("onRspOrderAction响应： "+JSON.toJSONString(pInputOrderAction)+"\r\n");
                }
            });
    }
    
    /**
     * 成交通知
     * @param pTrade
     */
    public void onRtnTrade(CThostFtdcTradeField pTrade) {
        
            
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                console.append(LocalDateTime.now()+"收到成交响应\r\n");
            }
        });
        
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
                    console.append("onRspQryInvestorPositionDetail响应： "+JSON.toJSONString(pInvestorPositionDetail)+"\r\n");
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
                    console.append("onRspQryInvestorPosition响应： "+JSON.toJSONString(pInvestorPosition)+"\r\n");
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
                    console.append("收到结算清单 确认响应\r\n");
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
                    console.append("onRspError响应： "+JSON.toJSONString(pRspInfo)+"\r\n");
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
                    console.append(LocalDateTime.now()+"收到报单录入错误响应： "+JSON.toJSONString(pRspInfo)+"\r\n");
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
                    console.append("onRspQryTradingAccount响应： "+JSON.toJSONString(pTradingAccount)+"\r\n");
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
                console.append("查询合约返回"+JSON.toJSONString(pInstrument)+"\r\n");
            }
        });
    }
}
