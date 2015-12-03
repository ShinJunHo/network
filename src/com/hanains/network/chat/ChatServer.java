package com.hanains.network.chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
	private static final int PORT=9999;
	
	public static void main(String[] args){
		ServerSocket serverSocket =null;
		Map<String,Writer> nickname;

		try{
			//1.서버 소켓 생성
			serverSocket = new ServerSocket();
			nickname=new HashMap();
			
			String localhost=InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost,PORT));
			System.out.println("[서버] 바인딩: "+localhost+":"+PORT);
			
			while(true){
				Socket socket= serverSocket.accept();
				new ChatServerThread(socket,nickname).start();
			}
			
		}catch(IOException ex){
			System.out.println("[서버] 에러: "+ex);
			
		}finally{
			//자원해제
			if(serverSocket != null && serverSocket.isClosed() == false){
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("[서버] serverClose error : "+e);
				}
			}
		}
	}
}
