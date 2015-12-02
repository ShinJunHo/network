package com.hanains.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {
	private Socket socket = null;
//	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	private BufferedReader bufferedReader=null;
	private PrintWriter printWriter=null;

	public EchoServerReceiveThread() {

	}

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress()
					.getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[서버]연결됨 from " + remoteHostAddress + ":"
					+ remoteHostPort);
			/*
			 * Thread 2개 test 
			 * while(true){
			 * System.out.println(remoteHostAddress); try { Thread.sleep(1000);
			 * } catch (InterruptedException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } }
			 */
			// 5.IOS Stream받아오기
			bufferedReader =new BufferedReader (new InputStreamReader(socket.getInputStream()));
			
			//inputStream = socket.getInputStream();
			//outputStream = socket.getOutputStream();
			printWriter=new PrintWriter(socket.getOutputStream());

			// 6.데이터 읽기.
			//byte[] buffer = new byte[256];
			//char[] cbuf=new char[256];
			while (true) {
				//int readByteCount =inputStream.read(cbuf);
				//int readByteCount = inputStream.read(buffer);
				
				//데이터 읽기.
				String data=bufferedReader.readLine(); // \r\n 개행문자 를 기준으로 나눔. 
				/*if (readByteCount < 0) {
					System.out.println("[서버] 클라이언트로부터 연결 끊김");
					break;
				}*/
				//끊김 처리.
				if(data==null){
					System.out.println("클라이언트 연결 끊김.");
					break;
				}
				//String data = new String(cbuf, 0, readByteCount);
				//String data = new String(buffer, 0, readByteCount);
				System.out.println("[서버] 수신 데이터:" + data);
				// 7.데이터 보내기
				printWriter.print(data);
				//printWriter.println(data); Client에서 READLINE을 할때는 이렇게 ln 붙인다.
				printWriter.flush();
				//outputStream.write(data.getBytes("UTF-8"));
				//outputStream.flush();

			}
		} catch (IOException e) {
			System.out.println("Server Thread error" + e);

		} finally {
			// 8.자원정리
			try {
				/*
				if (inputStream != null) {
					inputStream.close();
				}*/
			/*	if(printWriter!=null){
					pritWriter.close();
				}*/
				if (outputStream != null) {
					outputStream.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
