package com.guotaian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.gta.qts.c2j.adaptee.IGTAQTSApi;
import com.gta.qts.c2j.adaptee.IGTAQTSCallbackBase;
import com.gta.qts.c2j.adaptee.impl.GTAQTSApiBaseImpl;
import com.gta.qts.c2j.adaptee.structure.QTSDataType;
import com.gta.qts.c2j.adaptee.structure.QTSDataType.MsgType;
import com.gta.qts.c2j.adaptee.structure.QTSDataType.RetCode;
import com.gta.qts.c2j.adaptee.structure.SZSEL2_Quotation;
import com.gta.qts.c2j.adaptee.structure.StockSymbol;




public class Example {

    public static void main(String[] args) throws IOException {
        // 创建消息回调对象，用于接收实时数据
        IGTAQTSCallbackBase callback = new GTACallbackBase();
        
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
                System.out.println("GetStockList(SHFE,CZCE,DCE) error:" + ret);
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
    }

}
