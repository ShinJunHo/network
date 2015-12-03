package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class ChatServerThread extends Thread {

	private String nickname;
	private Socket socket;
	private Map<String, Writer> nicknameMap;
	
	/*
	 *Map Object에 Writer를 넣고
	 *run()에 talk: 프로토콜 넣고,
	 *broadcast에서 할때와 처럼 nickname값 빼오면
	 *귓속말이 전송되겠다.
	 *
	 */
	
	public ChatServerThread() {

	}
	
	public ChatServerThread(Socket socket,Map<String,Writer> name) {
		this.socket = socket;
		this.nicknameMap = name;
	}
	
	private void addWriter(Writer writer) {
		synchronized (nicknameMap) {
			nicknameMap.put(nickname, writer);
		}
	}
	
	private void broadcast(String data) {
		synchronized (nicknameMap) {
			for (Map.Entry<String,Writer> map : nicknameMap.entrySet()) {
				PrintWriter printWriter = (PrintWriter) map.getValue();
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}
	
	private void removeWriter(Writer writer) {

		synchronized (nicknameMap) {
			for(Map.Entry<String,Writer> map: nicknameMap.entrySet()){
				if(map.getValue().equals(writer)){
					nicknameMap.remove(map.getKey(),writer);
					break;
				}
			}
		}

	}

	private void doJoin(String nick, Writer writer) {

		PrintWriter printWriter = (PrintWriter) writer;
		//닉네임 map으로 중복체크
		for (int i = 0; i < nicknameMap.size(); i++) {
			if (nicknameMap.containsKey(nick)) {
				System.out.println("join:fail:");
				printWriter.println("join:fail:");
				printWriter.flush();
				return;
			}
		}
		nicknameMap.put(nick, writer);
		this.nickname = nick;

		String data = "join:" + nick + "님이 참여하였습니다." + "\r\n";
		broadcast(data);
		addWriter(writer);
		// ack
		printWriter.println("join:ok");
		printWriter.flush();

	}
	//귓속말
	private void doTalk(String nick, String message){
		
		synchronized (nicknameMap) {
			PrintWriter printWriter = (PrintWriter) nicknameMap.get(nick);
			String data="message:" + "[" + this.nickname + "]" + message + "\r\n";
			printWriter.println(data);
			printWriter.flush();
		}
	
	}
	
	//메세지
	private void doMessage(String message) {
		String data = "message:" + "[" + this.nickname + "]" + message + "\r\n";
		broadcast(data);
	}
	
	//종료
	private void doQuit(Writer writer) {

		((PrintWriter) writer).println("quit:끝");
		removeWriter(writer);
		String data = "message:" + this.nickname + "님이 퇴장하였습니다." + "\r\n";
		broadcast(data);

	}

	@Override
	public void run() {
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;

		try {

			// 1.Remote Host inFormation
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress()
					.getHostAddress();
			int remotehostPort = inetSocketAddress.getPort();
			System.out.println("[클라이언트 연결됨] from " + remoteHostAddress + ":"
					+ remotehostPort);

			// 2.스트림 얻기.
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(), StandardCharsets.UTF_8));
			// 3.요청 처리.
			while (true) {
				String request = bufferedReader.readLine();

				if (request == null) {
					System.out.println("클라이언트 연결 끊김");
					doQuit(printWriter);
					break;
				}
				// 4.프로토콜 분석.
				String[] tokens = request.split(":");
				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter);
				} else if ("message".equals(tokens[0])) {
					System.out.println(tokens[1]);
					doMessage(tokens[1]);
				}else if("talk".equals(tokens[0])){
					doTalk(tokens[2],tokens[3]);
				} else if ("quit".equals(tokens[0])) {
					doQuit(printWriter);
					break;
				} else {
					System.out.println(tokens[0]);
					System.out.println("[에러] 알수없는 요청" + "(" + tokens[0] + ")");
				}
			}

		} catch (IOException ex) {
			System.out.println("[서버 스레드] 에러: " + ex);
		} finally {
			try {
				//자원 해제
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				System.out.println("[서버] 닫기 에러: " + e);
			}

		}

	}
}
