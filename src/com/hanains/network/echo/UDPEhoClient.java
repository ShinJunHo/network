package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEhoClient {
	
	private static String HOST_ADDRESS="127.0.0.1"; //루프백 주소
	private static int PORT = 9009;
	private static final int BUFFER_SIZE= 1024;
	
	public static void main(String[] args) {
		DatagramSocket datagramSocket=null;
		Scanner scan = new Scanner(System.in);
		try {
			//1.UDP 소켓 생성
			datagramSocket = new DatagramSocket();

			
			//2.전송 패킷 생성
			//String data="Hello World";
			while(true){
				System.out.println(">>");
				String data=scan.nextLine();
				byte[] sendData = data.getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,new InetSocketAddress(HOST_ADDRESS,PORT));
			
			
			//3.데이터 전송.
			datagramSocket.send(sendPacket);
			
			//4.데이터 수신.
			log("수신대기");
			DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE],BUFFER_SIZE);
			datagramSocket.receive(receivePacket);
			
			//5.데이터 출력
			data=new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
			log("데이터 수신 :"+data);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log("error: "+e);
		}finally{
			if(datagramSocket != null){
				datagramSocket.close();
			}
		}
		
	}
	public static void log(String message){
		System.out.println("[UDP Echo Client]"+message);
	}
}
