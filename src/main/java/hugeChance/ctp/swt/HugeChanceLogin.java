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

public class HugeChanceLogin {

    protected Shell shlctp;
    private Text investorText;
    private Text passwdText;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            HugeChanceLogin window = new HugeChanceLogin();
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
        
        Combo combo = new Combo(shlctp, SWT.READ_ONLY);
        combo.add("tcp://180.168.146.187:10000",0);
        combo.add("tcp://180.168.146.187:10001",1);
        combo.add("tcp://218.202.237.33:10002",2);
        combo.setBounds(189, 45, 110, 23);
        
        /*Label investorNo = new Label(shlctp, SWT.NONE);
        investorNo.setAlignment(SWT.RIGHT);
        investorNo.setText("投资者账号");
        investorNo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        investorNo.setBounds(54, 103, 100, 22);
        
        investorText = new Text(shlctp, SWT.BORDER);
        investorText.setBounds(189, 102, 110, 23);
        
        Label passwd = new Label(shlctp, SWT.NONE);
        passwd.setAlignment(SWT.RIGHT);
        passwd.setText("密码");
        passwd.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
        passwd.setBounds(54, 157, 100, 22);
        
        passwdText = new Text(shlctp, SWT.BORDER);
        passwdText.setBounds(189, 157, 110, 22);*/
        
        Button loginButton = new Button(shlctp, SWT.NONE);
        loginButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HugeChanceApp hugeChanceApp = new HugeChanceApp();
                if(combo.getText()!= null && !combo.getText().trim().equals("")){
                    hugeChanceApp.setFrontAddr(combo.getText());
                }
                shlctp.dispose();
                hugeChanceApp.open();
            }
        });
        loginButton.setBounds(54, 203, 88, 33);
        loginButton.setText("启动");
        
        Button signOut = new Button(shlctp, SWT.NONE);
        signOut.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shlctp.dispose();
            }
        });
        signOut.setText("退出");
        signOut.setBounds(211, 203, 88, 33);

    }
}
