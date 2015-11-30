package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	
	private static final int PORT = 10001; //상수 대문자 관례.
	
	public static void main(String[] args){
		ServerSocket serverSocket=null;
		try{
		
		//1.Creating a server socket
		serverSocket = new ServerSocket();

		//2.바인딩
		//serverSocket.bind(new InetSocketAddress("",PORT));
		InetAddress inetAddress=InetAddress.getLocalHost();
		String localhost=inetAddress.getHostAddress();
		serverSocket.bind(new InetSocketAddress(localhost,PORT));
		System.out.println("[서버]바인딩 "+localhost+":"+PORT);
		
		//3.연결 요청대기(accept)
		Socket socket=serverSocket.accept();
		
		//socket port확인하고
		//read write해보기.
		
		//4.연결 성공 연결한 측의 정보를 알 수 있다. 찾아낼 수 있다.
		//자식으로 Down casting 한다
		//SocketAddress - > InetSocketAddress
		InetSocketAddress inetSocketAddress=(InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress(); // 어느놈이 찔럿냐~~
		int remoteHostPort = inetSocketAddress.getPort();
		System.out.println("[서버]연결됨 from "+remoteHostAddress+":"+remoteHostPort);
		
		
		//5.데이터 보내기.
		
		OutputStream os = socket.getOutputStream();
		String data="Hello HanaI&S";
		//os.write(data.getBytes("UTF-8"));
		//os.flush();
		//os.close();
		
		//6.데이터 받기. 데이터 보내기 코드와는 동시에 적용..
		InputStream is = socket.getInputStream();
		while(true){
			byte[] buffer = new byte[128];
			int readByteCount = is.read(buffer);
			//os.write(data.getBytes("UTF-8"));
			if(readByteCount <0){
				System.out.println("[서버] 클라이언트로 부터 연결 끊김. ");
				os.close();
				is.close();
				socket.close();
				break;
			}
			String readData = new String(buffer, 0, readByteCount,"UTF-8");
			System.out.println(readData);
			os.flush();
		}
		
		//7.socket 닫기
		if(socket.isClosed()== false){
			socket.close();
		}
		
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			//서버 소켓 닫기.
			if(serverSocket != null && serverSocket.isClosed() == false){
				try{
				serverSocket.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
