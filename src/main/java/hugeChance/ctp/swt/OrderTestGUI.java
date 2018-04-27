package hugeChance.ctp.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class OrderTestGUI {

    protected Shell shlctp;
    private Text investorText;
    private Text passwdText;
    private Text text;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            OrderTestGUI window = new OrderTestGUI();
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
        shlctp.open();
        shlctp.layout();
        while (!shlctp.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlctp = new Shell();
        shlctp.setSize(389, 316);
        shlctp.setText("登录CTP前置机");
        shlctp.setLayout(null);
        
        Label exchange = new Label(shlctp, SWT.NONE);
        exchange.setAlignment(SWT.RIGHT);
        exchange.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        exchange.setBounds(54, 45, 100, 22);
        exchange.setText("前置机地址");
        //tcp://180.168.146.187:10000
        
        Combo combo = new Combo(shlctp, SWT.NONE);
        combo.add("tcp://180.168.146.187:10000",0);
        combo.setBounds(189, 45, 110, 23);
        
        Label lblBrokeid = new Label(shlctp, SWT.NONE);
        lblBrokeid.setText("brokerID");
        lblBrokeid.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        lblBrokeid.setAlignment(SWT.RIGHT);
        lblBrokeid.setBounds(54, 93, 100, 22);
        
        text = new Text(shlctp, SWT.BORDER);
        text.setBounds(189, 93, 110, 23);
        
        Label investorNo = new Label(shlctp, SWT.NONE);
        investorNo.setAlignment(SWT.RIGHT);
        investorNo.setText("投资者账号");
        investorNo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        investorNo.setBounds(54, 143, 100, 22);
        
        investorText = new Text(shlctp, SWT.BORDER);
        investorText.setBounds(189, 142, 110, 23);
        
        Label passwd = new Label(shlctp, SWT.NONE);
        passwd.setAlignment(SWT.RIGHT);
        passwd.setText("密码");
        passwd.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        passwd.setBounds(54, 183, 100, 22);
        
        passwdText = new Text(shlctp, SWT.BORDER);
        passwdText.setBounds(189, 182, 110, 22);
        
        Button loginButton = new Button(shlctp, SWT.NONE);
        loginButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                GuiTest guiTest = new GuiTest();
                
                shlctp.dispose();
                guiTest.open();
            }
        });
        loginButton.setBounds(54, 235, 88, 33);
        loginButton.setText("连接服务器");
        
        Button signOut = new Button(shlctp, SWT.NONE);
        signOut.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shlctp.dispose();
            }
        });
        signOut.setText("退出");
        signOut.setBounds(211, 235, 88, 33);
        
    }
}
