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
		stop.setText("关闭服务器");
		
		final Button start = new Button(shlChatServer, SWT.NONE);
		start.setBounds(48, 58, 95, 48);
		start.setText("开启服务器");
		
		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//开启按钮
				stopFlag = false;
				start.setEnabled(false);
				stop.setEnabled(true);
				//设置按钮是否可用
				try {
					ss = new ServerSocket(5555);
					//监听端口5555
					new Thread(){
						public void run(){
							while (!stopFlag) {
								Socket s;
								try {
									s = ss.accept();
									//接受客户端连接
									new ServerThread(s).start();
									//收到连接后开启新线程给客户端服务
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
				//关闭服务器按钮
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
		//SWT的内容,不解释
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
			//以上顺利不可倒,必须先out,再in,否则可能出现死锁,服务器跟客户端都等待对方发送数据
			MessageData md = (MessageData) ois.readObject();
			//得到客户端发送的数据
			String key = s.getInetAddress().getHostAddress()+":"+s.getPort(); 
			System.out.println(s+"-----------------"+md);
 
			oos.writeObject(md);
			//发回给客户端
			oos.close();
			ois.close();
			s.close();
			//关闭流
			}catch(Exception e){
				
			}
 
	}
}