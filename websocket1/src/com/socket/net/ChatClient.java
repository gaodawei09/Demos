package com.socket.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
 
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
 
public class ChatClient {
 
 
	public static void main(String[] args) throws UnknownHostException, IOException {
		Display display = Display.getDefault();
		final Shell shlClient = new Shell();
		shlClient.setSize(563, 406);
		shlClient.setText("Client");
		
		final Text showMsg = new Text(shlClient, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		showMsg.setEditable(false);
		showMsg.setBounds(10, 10, 527, 243);
 
		
		final Text name = new Text(shlClient, SWT.BORDER);
		name.setBounds(58, 267, 129, 23);
		
		Label label = new Label(shlClient, SWT.NONE);
		label.setBounds(10, 270, 61, 17);
		label.setText("姓名:");
		
		Label lblNewLabel = new Label(shlClient, SWT.NONE);
		lblNewLabel.setBounds(10, 321, 43, 17);
		lblNewLabel.setText("信息:");
		
		final Text msg = new Text(shlClient, SWT.BORDER);
		msg.setBounds(55, 321, 386, 23);
		
		
		
		Button btnSend = new Button(shlClient, SWT.NONE);
		btnSend.setBounds(447, 321, 80, 27);
		btnSend.setText("发送");
		
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String nameText=name.getText();
				String msgText=msg.getText();
				//拿到姓名和信息的string
				if(!nameText.equals("")&&!msgText.equals("")){
					try {
						Socket s=new Socket(InetAddress.getByName(null),5555);
						//连接服务器,后面参数null,就是localhost,服务器不在本机改成IP
						OutputStream os=s.getOutputStream();
						ObjectOutputStream oos=new ObjectOutputStream(os);
						MessageData md=new MessageData(nameText,msgText);
						//封装一下,比较好处理
						oos.writeObject(md);
						//发送数据给服务器
						
						ObjectInputStream ois = new ObjectInputStream(
								s.getInputStream());
						md = (MessageData) ois.readObject();
						//接收服务器返回
						showMsg.append(md.name+"说：\n");
						showMsg.append(md.msg+"\n");
						//显示在上面的Text框中
						
						oos.close();
						ois.close();
						s.close();
						//关闭流
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						MessageDialog.openError(shlClient, "发送失败", "信息发送失败");
						e1.printStackTrace();
					}
				}
			}
		});
 
 
		shlClient.open();
		shlClient.layout();
		while (!shlClient.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
class MessageData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String msg;
	MessageData(String name,String msg){
		this.name=name;
		this.msg=msg;
	}
	@Override
	public String toString() {
		return "MessageData [msg=" + msg + ", name=" + name + "]";
	}
}