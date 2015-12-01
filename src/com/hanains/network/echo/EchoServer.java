package com.hanains.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int PORT = 10002;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		Thread thread1;
		Thread thread2;

		try {
			serverSocket = new ServerSocket();

			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			System.out.println("[서버]바인딩 " + localhost + ":" + PORT);
			Socket socket = serverSocket.accept();
			Socket socket1 = serverSocket.accept();
			System.out.println("[서버] Accpet");
			// 4.연결 성공한거 넘기기.
			//두명의 클라이언트를 받기 위한 방법으로 thread 2개 선언하는것을 선택.
			thread1 = new EchoServerReceiveThread(socket);
			thread1.start();
			thread2 = new EchoServerReceiveThread(socket1);
			thread2.start();

		} catch (IOException ex) {
			System.out.println("[서버] 에러:" + ex);
		} finally {
			// 서버 소켓 닫기.
			if (serverSocket != null && serverSocket.isClosed() == false) {
				try {
					serverSocket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		}
	}
}
