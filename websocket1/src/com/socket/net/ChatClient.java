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
		label.setText("����:");
		
		Label lblNewLabel = new Label(shlClient, SWT.NONE);
		lblNewLabel.setBounds(10, 321, 43, 17);
		lblNewLabel.setText("��Ϣ:");
		
		final Text msg = new Text(shlClient, SWT.BORDER);
		msg.setBounds(55, 321, 386, 23);
		
		
		
		Button btnSend = new Button(shlClient, SWT.NONE);
		btnSend.setBounds(447, 321, 80, 27);
		btnSend.setText("����");
		
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String nameText=name.getText();
				String msgText=msg.getText();
				//�õ���������Ϣ��string
				if(!nameText.equals("")&&!msgText.equals("")){
					try {
						Socket s=new Socket(InetAddress.getByName(null),5555);
						//���ӷ�����,�������null,����localhost,���������ڱ����ĳ�IP
						OutputStream os=s.getOutputStream();
						ObjectOutputStream oos=new ObjectOutputStream(os);
						MessageData md=new MessageData(nameText,msgText);
						//��װһ��,�ȽϺô���
						oos.writeObject(md);
						//�������ݸ�������
						
						ObjectInputStream ois = new ObjectInputStream(
								s.getInputStream());
						md = (MessageData) ois.readObject();
						//���շ���������
						showMsg.append(md.name+"˵��\n");
						showMsg.append(md.msg+"\n");
						//��ʾ�������Text����
						
						oos.close();
						ois.close();
						s.close();
						//�ر���
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						MessageDialog.openError(shlClient, "����ʧ��", "��Ϣ����ʧ��");
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