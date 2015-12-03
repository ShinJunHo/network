package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientReceiveThread extends Thread {
	private BufferedReader bufferedReader;

	public ChatClientReceiveThread() {
	}

	public ChatClientReceiveThread(BufferedReader buffer) {
		this.bufferedReader = buffer;
	}

	@Override
	public void run() {
		// reader를 통해 읽은 데이터 콘솔에 출력하기 (Message 처리)
		while (true) {
			try {
				
				//입력
				String request = bufferedReader.readLine();
				if(request == null){
					System.out.println("클라이언트 연결 끊김");
					break;
				}
				String[] tokens=request.split(":");
				//4.프로토콜 분석.
				if("join".equals(tokens[0])){
					System.out.println(tokens[1]);
				}else if("message".equals(tokens[0])){
					System.out.println(tokens[1]);
				}else if("quit".equals(tokens[0])){
					System.out.println(tokens[1]);
					break;
				}else if("talk".equals(tokens[0])){
					System.out.println(tokens[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

	}
}
