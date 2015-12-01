package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.IntPredicate;

public class TCPClient {
	
	//서버 IP 클라이언트가 접속에 필요한. IP보단 domain 이름으로 접근이 
	private static final String SERVER_IP="192.168.1.7";
	private static final int SERVER_PORT=10001;
	public static void main(String[] args){
	
	Socket socket =null;
	InputStream inputStream=null;
	OutputStream outputStream= null;
	
	try{
		//1.소켓생성.
		socket =new Socket();
		//2.서버 연결
		socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
		System.out.println("[클라이언트] 서버연결 성공");
	
		//3.IOStream 받아오기

		inputStream=socket.getInputStream();
		outputStream =socket.getOutputStream();
		
		//4.쓰기/읽기
		String data="Hello World";
		outputStream.write(data.getBytes("UTF-8"));
		
		//
		byte[] buffer=new byte[256];
		int readByteCount=inputStream.read(buffer);
		
		data=new String(buffer,0,readByteCount,"UTF-8");
		System.out.println(">>"+data);
	}
	catch(IOException ex){
		System.out.println("[클라이언트]에러: "+ex);
	}finally{
		try{
			if(inputStream !=null){
				inputStream.close();
			}
			if(outputStream !=null){
				outputStream.close();
			}
			if(socket != null && socket.isClosed() == false){
					socket.close();
			}
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}
	}
}
