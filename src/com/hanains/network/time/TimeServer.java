package com.hanains.network.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT=9009;
	private static final int BUFFER_SIZE=1024;
	
	public static void main(String[] args){
		DatagramSocket datagramSocket=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
		try{
			//1.UDP Socket 생성
			datagramSocket=new DatagramSocket(PORT);
			
			
			//2.수신 대기
			while(true){
				log("수신대기");
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE],BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				log("데이터 받음");
				String data=format.format(new Date());
				byte[] sendData=data.getBytes("UTF-8");
				//3.데이터 전송
				DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, receivePacket.getAddress(),receivePacket.getPort());
				
				datagramSocket.send(sendPacket);
			}
		
		}catch(Exception ex){
			log("error:"+ex);
		}finally{
			if(datagramSocket != null){
				datagramSocket.close();
			}
		}
	}
	public static void log(String message){
		System.out.println("[UDP TIME SERVER]" + message);
	}
}
