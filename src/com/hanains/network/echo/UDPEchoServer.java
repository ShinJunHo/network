package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPEchoServer {
	private static final int PORT = 9009;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;

		try {
			// 1.UDP Socket 생성
			datagramSocket = new DatagramSocket(PORT);

			// 2.수신 대기
			// 저쪽 send 패킷이 여기는 recv 패킷
			// 버퍼 버퍼사이즈
			// 수신과 받음 부분만 while로 돌면. 다 받을 수 있겠다.
			while (true) {
				log("수신대기");
				DatagramPacket receivePacket = new DatagramPacket(
						new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				// 응답할때는 receivePacket에 다 들어있는 Address 랑 포트도 지금 대기상태.

				// 3.데이터확인
				log("데이터 받음");
				String data = new String(receivePacket.getData(), 0,
						receivePacket.getLength(), "UTF-8");
				log("데이터 수신" + data);
				
				if(data == null){
					log("클라이언트 연결 끊김.");
					break;
				}
				// 4.데이터 전송.
				DatagramPacket sendPacket = new DatagramPacket(
						receivePacket.getData(), receivePacket.getLength(),
						receivePacket.getAddress(), receivePacket.getPort());
				datagramSocket.send(sendPacket);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log("error:" + e);

		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}

	public static void log(String message) {
		System.out.println("[UDP Echo Server]" + message);
	}
}
