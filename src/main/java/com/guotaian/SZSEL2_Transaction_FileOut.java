package com.guotaian;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gta.qts.c2j.adaptee.structure.SZSEL2_Transaction;


public class SZSEL2_Transaction_FileOut extends AFileOutBase {
	public static Queue<SZSEL2_Transaction> QUEUE14 = new LinkedBlockingQueue<SZSEL2_Transaction>();
	public static FileOutThread14 FILE_THREAD;
	
	private static class FileOutThread14 implements Runnable {
		private boolean stopped;
		
		@Override
		public void run() {
			int freq = 30;
			boolean saveFile = true;
			Formatters fmt = new Formatters();
			String prevHHmm = null;
			String currHHmm = null;
			BufferedWriter bw = null;
			SZSEL2_Transaction data = null;
			while (!stopped) {
				data = QUEUE14.poll();
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

				currHHmm = getFileHHmm(data.TradeTime, freq);
				if ((bw == null) || !currHHmm.equals(prevHHmm)) {
					closeWriter(bw);
					bw = createWriter(getFileName(SZSEL2_Transaction.class, currHHmm), title());
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
			FILE_THREAD = new FileOutThread14();
			new Thread(FILE_THREAD).start();
		}
	}

	public static boolean isStopped() {
		return (FILE_THREAD == null) || FILE_THREAD.isStopped();
	}
	
	public static void printData(SZSEL2_Transaction data){
		if (!stopping) {
			QUEUE14.offer(data);
		}
	}
	
	public static BufferedWriter createSnapWriter() {
		String fileName = getSnapFileName(SZSEL2_Transaction.class);
		return createWriter(fileName, title());
	}
	
	public static void flushData(SZSEL2_Transaction data, BufferedWriter bw, Formatters fmt) {
		SimpleDateFormat sdf = fmt.getSdf();
		DecimalFormat df = fmt.getDf6();
		try {
		    bw.write(sdf.format(new java.util.Date()) + TAB);
			bw.write(data.LocalTimeStamp + TAB);
            bw.write(byteArr2StringAndTrim(data.QuotationFlag) + TAB);
			bw.write(unsigned2String(data.SetID) + TAB);
			bw.write(data.RecID + TAB);
			bw.write(data.BuyOrderID + TAB);
			bw.write(data.SellOrderID + TAB);
			bw.write(byteArr2StringAndTrim(data.Symbol) + TAB);
			bw.write(byteArr2StringAndTrim(data.SymbolSource) + TAB);
			bw.write(data.TradeTime + TAB);
			bw.write(double2String(df,data.TradePrice) + TAB);
			bw.write(double2String(df,data.TradeVolume) + TAB);
			bw.write(byteArr2StringAndTrim(new byte[]{data.TradeType}));
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
        .append("3SetID频道代码").append(TAB)
        .append("4RecID消息记录号").append(TAB)
        .append("5BuyOrderID买方委托索引").append(TAB)
        .append("6SellOrderID卖方委托索引").append(TAB)
        .append("7Symbol证券代码").append(TAB)
        .append("8SymbolSource证券代码源").append(TAB)
        .append("9TradeTime成交时间").append(TAB)
        .append("10TradePrice成交价格").append(TAB)
        .append("11TradeVolume成交数量").append(TAB)
        .append("12TradeType成交类型");
		return sb.toString();
    }
}
