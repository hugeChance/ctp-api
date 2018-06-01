package com.guotaian;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gta.qts.c2j.adaptee.structure.QTSDataType;
import com.gta.qts.c2j.adaptee.structure.SZSEL2_Order;
import com.gta.qts.c2j.adaptee.structure.SZSEL2_Quotation;


public class SZSEL2_Quotation_FileOut extends AFileOutBase {
	public static Queue<SZSEL2_Quotation> QUEUE11 = new LinkedBlockingQueue<SZSEL2_Quotation>();
	public static FileOutThread11 FILE_THREAD;
	
	private static class FileOutThread11 implements Runnable {
		private boolean stopped;
		
		@Override
		public void run() {
			int freq = 30;
			boolean saveFile = true;
			Formatters fmt = new Formatters();
			String prevHHmm = null;
			String currHHmm = null;
			BufferedWriter bw = null;
			SZSEL2_Quotation data = null;
			while (!stopped) {
				data = QUEUE11.poll();
				if (data == null) {
					if (stopping) {
						if (saveFile) {
							closeWriter(bw);
						}
						stopped = true;
						break;
					}
					else {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
				}
				
				if (!saveFile) {
					continue;
				}

				currHHmm = getFileHHmm(data.Time, freq);
				if ((bw == null) || !currHHmm.equals(prevHHmm)) {
					closeWriter(bw);
					bw = createWriter(getFileName(SZSEL2_Quotation.class, currHHmm), title());
					prevHHmm = currHHmm;
				}

				flushData(data, bw, fmt);
			}
			
		}

		public boolean isStopped() {
			return stopped;
		}
	}
	
	public static void startThread() {
		if (FILE_THREAD == null) {
			FILE_THREAD = new FileOutThread11();
			new Thread(FILE_THREAD).start();
		}
	}

	public static boolean isStopped() {
		return (FILE_THREAD == null) || FILE_THREAD.isStopped();
	}
	
	public static void printData(SZSEL2_Quotation data){
		if (!stopping) {
			QUEUE11.offer(data);
		}
	}
	
	public static BufferedWriter createSnapWriter() {
		String fileName = getSnapFileName(SZSEL2_Quotation.class);
		return createWriter(fileName, title());
	}

	public static void flushData(SZSEL2_Quotation data, BufferedWriter bw, Formatters fmt) {
		SimpleDateFormat sdf = fmt.getSdf();
		DecimalFormat df = fmt.getDf6();
		try {
			bw.write(sdf.format(new java.util.Date()) + TAB);
			bw.write(data.LocalTimeStamp + TAB);
			bw.write(byteArr2StringAndTrim(data.QuotationFlag) + TAB);
			bw.write(data.Time + TAB);
			bw.write(byteArr2StringAndTrim(data.Symbol) + TAB);
			bw.write(byteArr2StringAndTrim(data.SymbolSource) + TAB);
			bw.write(double2String(df,data.PreClosePrice) + TAB);
			bw.write(double2String(df,data.OpenPrice) + TAB);
			bw.write(double2String(df,data.LastPrice) + TAB);
			bw.write(double2String(df,data.HighPrice) + TAB);
			bw.write(double2String(df,data.LowPrice) + TAB);
			bw.write(double2String(df,data.PriceUpLimit) + TAB);
			bw.write(double2String(df,data.PriceDownLimit) + TAB);
			bw.write(double2String(df,data.PriceUpdown1) + TAB);
			bw.write(double2String(df,data.PriceUpdown2) + TAB);
			bw.write(data.TotalNo + TAB);
			bw.write(double2String(df,data.TotalVolume) + TAB);
			bw.write(double2String(df,data.TotalAmount) + TAB);
			bw.write(double2String(df,data.ClosePrice) + TAB);
			bw.write(byteArr2StringAndTrim(data.SecurityPhaseTag) + TAB);
			bw.write(double2String(df,data.PERatio1) + TAB);
			bw.write(double2String(df,data.NAV) + TAB);
			bw.write(double2String(df,data.PERatio2) + TAB);
			bw.write(double2String(df,data.IOPV) + TAB);
			bw.write(double2String(df,data.PremiumRate) + TAB);
			bw.write(double2String(df,data.TotalSellOrderVolume) + TAB);
			bw.write(double2String(df,data.WtAvgSellPrice) + TAB);
			bw.write(unsigned2String(data.SellLevelNo) + TAB);
			bw.write(szseLevelInfo3ToStringWithTab(df,data.SellLevel));
			bw.write(unsigned2String(data.SellLevelQueueNo01) + TAB);
			for(int i=0;i<50;i++) {
				bw.write(double2String(df,data.SellLevelQueue[i]) + TAB);
			}
			bw.write(double2String(df,data.TotalBuyOrderVolume) + TAB);
			bw.write(double2String(df,data.WtAvgBuyPrice) + TAB);
			bw.write(unsigned2String(data.BuyLevelNo) + TAB);
			bw.write(szseLevelInfo3ToStringWithTab(df,data.BuyLevel));
			bw.write(unsigned2String(data.BuyLevelQueueNo01) + TAB);
			for(int i=0;i<50;i++) {
				bw.write(double2String(df,data.BuyLevelQueue[i]) + TAB);
			}
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static String title() {
		StringBuilder sb = new StringBuilder();
		sb.append(AFileOutBase.getLocalTimeTitle()).append(TAB);
		sb.append("1LocalTimeStamp采集时间").append(TAB)
        .append("2QuotationFlag行情源标志").append(TAB)
        .append("3Time数据生成时间").append(TAB)
        .append("4Symbol证券代码").append(TAB)
        .append("5SymbolSource证券代码源").append(TAB)
        .append("6PreClosePrice昨收价").append(TAB)
        .append("7OpenPrice开盘价").append(TAB)
        .append("8LastPrice现价").append(TAB)
        .append("9HighPrice最高价").append(TAB)
        .append("10LowPrice最低价").append(TAB)
        .append("11PriceUpLimit涨停价").append(TAB)
        .append("12PriceDownLimit跌停价").append(TAB)
        .append("13PriceUpdown1升跌一").append(TAB)
        .append("14PriceUpdown2升跌二").append(TAB)
        .append("15TotalNo成交笔数").append(TAB)
        .append("16TotalVolume成交总量").append(TAB)
        .append("17TotalAmount成交总额").append(TAB)
        .append("18ClosePrice今收盘价").append(TAB)
        .append("19SecurityPhaseTag当前品种交易状态").append(TAB)
        .append("20PERatio1市盈率1").append(TAB)
        .append("21NAV基金T-1日净值").append(TAB)
        .append("22PERatio2市盈率2").append(TAB)
        .append("23IOPV基金实时参考净值").append(TAB)
        .append("24PremiumRate权证溢价率").append(TAB)
        .append("25TotalSellOrderVolume委托卖出总量").append(TAB)
        .append("26WtAvgSellPrice加权平均委卖价").append(TAB)
        .append("27SellLevelNo卖盘价位数").append(TAB)
        .append("28SellPrice01申卖价").append(TAB)
        .append("29SellVolume01申卖量").append(TAB)
        .append("30TotalSellOrderNo01卖出总委托笔数").append(TAB)
        .append("31SellPrice02申卖价").append(TAB)
        .append("32SellVolume02申卖量").append(TAB)
        .append("33TotalSellOrderNo02卖出总委托笔数").append(TAB)
        .append("34SellPrice03申卖价").append(TAB)
        .append("35SellVolume03申卖量").append(TAB)
        .append("36TotalSellOrderNo03卖出总委托笔数").append(TAB)
        .append("37SellPrice04申卖价").append(TAB)
        .append("38SellVolume04申卖量").append(TAB)
        .append("39TotalSellOrderNo04卖出总委托笔数").append(TAB)
        .append("40SellPrice05申卖价").append(TAB)
        .append("41SellVolume05申卖量").append(TAB)
        .append("42TotalSellOrderNo05卖出总委托笔数").append(TAB)
        .append("43SellPrice06申卖价").append(TAB)
        .append("44SellVolume06申卖量").append(TAB)
        .append("45TotalSellOrderNo06卖出总委托笔数").append(TAB)
        .append("46SellPrice07申卖价").append(TAB)
        .append("47SellVolume07申卖量").append(TAB)
        .append("48TotalSellOrderNo07卖出总委托笔数").append(TAB)
        .append("49SellPrice08申卖价").append(TAB)
        .append("50SellVolume08申卖量").append(TAB)
        .append("51TotalSellOrderNo08卖出总委托笔数").append(TAB)
        .append("52SellPrice09申卖价").append(TAB)
        .append("53SellVolume09申卖量").append(TAB)
        .append("54TotalSellOrderNo09卖出总委托笔数").append(TAB)
        .append("55SellPrice10申卖价").append(TAB)
        .append("56SellVolume10申卖量").append(TAB)
        .append("57TotalSellOrderNo10卖出总委托笔数").append(TAB)
        .append("58SellLevelQueueNo01卖一档揭示委托笔数").append(TAB);
        for (int idx = 1; idx <= 50; idx++) {
            sb.append(58 + idx).append("SellLevelQueue" + idx + "卖1档队列").append(TAB);
        }
        sb.append("109TotalBuyOrderVolume委托买入总量").append(TAB)
        .append("110WtAvgBuyPrice加权平均买入价").append(TAB)
        .append("111BuyLevelNo买盘价位数").append(TAB)
        .append("112BuyPrice01申买价").append(TAB)
        .append("113BuyVolume01申买量").append(TAB)
        .append("114TotalBuyOrderNo01买入总委托笔数").append(TAB)
        .append("115BuyPrice02申买价").append(TAB)
        .append("116BuyVolume02申买量").append(TAB)
        .append("117TotalBuyOrderNo02买入总委托笔数").append(TAB)
        .append("118BuyPrice03申买价").append(TAB)
        .append("119BuyVolume03申买量").append(TAB)
        .append("120TotalBuyOrderNo03买入总委托笔数").append(TAB)
        .append("121BuyPrice04申买价").append(TAB)
        .append("122BuyVolume04申买量").append(TAB)
        .append("123TotalBuyOrderNo04买入总委托笔数").append(TAB)
        .append("124BuyPrice05申买价").append(TAB)
        .append("125BuyVolume05申买量").append(TAB)
        .append("126TotalBuyOrderNo05买入总委托笔数").append(TAB)
        .append("127BuyPrice06申买价").append(TAB)
        .append("128BuyVolume06申买量").append(TAB)
        .append("129TotalBuyOrderNo06买入总委托笔数").append(TAB)
        .append("130BuyPrice07申买价").append(TAB)
        .append("131BuyVolume07申买量").append(TAB)
        .append("132TotalBuyOrderNo07买入总委托笔数").append(TAB)
        .append("133BuyPrice08申买价").append(TAB)
        .append("134BuyVolume08申买量").append(TAB)
        .append("135TotalBuyOrderNo08买入总委托笔数").append(TAB)
        .append("136BuyPrice09申买价").append(TAB)
        .append("137BuyVolume09申买量").append(TAB)
        .append("138TotalBuyOrderNo09买入总委托笔数").append(TAB)
        .append("139BuyPrice10申买价").append(TAB)
        .append("140BuyVolume10申买量").append(TAB)
        .append("141TotalBuyOrderNo10买入总委托笔数").append(TAB)
        .append("142BuyLevelQueueNo01买一档揭示委托笔数").append(TAB);
        for (int idx = 1; idx <= 50; idx++) {
            sb.append(142 + idx).append("BuyLevelQueue" + idx + "买1档队列");
            if (idx <= 49) {
                sb.append(TAB);
            }
        }
		return sb.toString();
    }
}
