package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP="192.168.1.7";
	private static final int SERVER_PORT=10002;
	public static void main(String[] args){
		Socket socket=null;
		InputStream inputStream=null;
		OutputStream outputStream=null;
		String data;
		Scanner scan=new Scanner(System.in);
		try{
			//1.소캣생성
			socket=new Socket();
			//2.서버연결
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			System.out.println("[클라이언트] 서버연결 성공");
			//3.IOStream받아오기
			inputStream=socket.getInputStream();
			outputStream=socket.getOutputStream();
			//4.read / write
			
			while(true){
				data=scan.nextLine();
				outputStream.write(data.getBytes("UTF-8"));
				byte[] buffer=new byte[256];
				int readByteCount=inputStream.read(buffer);
				data=new String(buffer,0,readByteCount,"UTF-8");
				System.out.println(">>"+data);
				if("exit".equals(data)){
					break;
				}
			}
			
		}catch(IOException ex){
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
