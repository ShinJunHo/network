package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {
	private Socket socket = null;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;

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
			 * Thread 2개 test while(true){
			 * System.out.println(remoteHostAddress); try { Thread.sleep(1000);
			 * } catch (InterruptedException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } }
			 */
			// 5.IOS Stream받아오기
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			// 6.데이터 읽기.
			byte[] buffer = new byte[256];
			while (true) {
				int readByteCount = inputStream.read(buffer);
				if (readByteCount < 0) {
					System.out.println("[서버] 클라이언트로부터 연결 끊김");
					break;
				}
				String data = new String(buffer, 0, readByteCount);
				System.out.println("[서버] 수신 데이터:" + data);
				// 7.데이터 보내기
				outputStream.write(data.getBytes("UTF-8"));
				outputStream.flush();

			}
		} catch (IOException e) {
			System.out.println("Server Thread error" + e);

		} finally {
			// 8.자원정리
			try {
				if (inputStream != null) {
					inputStream.close();
				}
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
