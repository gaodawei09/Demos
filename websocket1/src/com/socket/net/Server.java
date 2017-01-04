package com.socket.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Server {
 
	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @throws IOException
	 */
	static ServerSocket ss;
	static boolean stopFlag = false;
 
	public static void main(String[] args) throws IOException {
		Display display = Display.getDefault();
		Shell shlChatServer = new Shell();
		shlChatServer.setSize(368, 244);
		shlChatServer.setText("Chat Server");
 
		final Button stop = new Button(shlChatServer, SWT.NONE);
		stop.setBounds(186, 58, 95, 48);
		stop.setText("�رշ�����");
		
		final Button start = new Button(shlChatServer, SWT.NONE);
		start.setBounds(48, 58, 95, 48);
		start.setText("����������");
		
		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//������ť
				stopFlag = false;
				start.setEnabled(false);
				stop.setEnabled(true);
				//���ð�ť�Ƿ����
				try {
					ss = new ServerSocket(5555);
					//�����˿�5555
					new Thread(){
						public void run(){
							while (!stopFlag) {
								Socket s;
								try {
									s = ss.accept();
									//���ܿͻ�������
									new ServerThread(s).start();
									//�յ����Ӻ������̸߳��ͻ��˷���
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}.start();
 
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
 
			}
		});
 
 
		stop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//�رշ�������ť
				stopFlag = true;
				stop.setEnabled(false);
				start.setEnabled(true);
				try {
					ss.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 
			}
		});
 
 
		shlChatServer.open();
		shlChatServer.layout();
		while (!shlChatServer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//SWT������,������
	}
 
 
}
class ServerThread extends Thread{
	Socket s;
	ServerThread(Socket s){
		this.s=s;
	}
	public void run(){
		try{
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(
					os);
 
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(
					is);
			//����˳�����ɵ�,������out,��in,������ܳ�������,���������ͻ��˶��ȴ��Է���������
			MessageData md = (MessageData) ois.readObject();
			//�õ��ͻ��˷��͵�����
			String key = s.getInetAddress().getHostAddress()+":"+s.getPort(); 
			System.out.println(s+"-----------------"+md);
 
			oos.writeObject(md);
			//���ظ��ͻ���
			oos.close();
			ois.close();
			s.close();
			//�ر���
			}catch(Exception e){
				
			}
 
	}
}