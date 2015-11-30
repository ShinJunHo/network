package com.hanains.network.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	public static void main(String[] args){
		Socket socket=null;
		try{
			socket = new Socket();
			System.out.println("[클라이언트] 연결 요청");
			socket.connect(new InetSocketAddress("192.168.1.7",10001));
			System.out.println("[클라이언트] 연결 성공");
			
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			if(socket != null && socket.isClosed() == false){
				try{
					socket.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
