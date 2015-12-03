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
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {

	private String nickname;
	private Socket socket;
	private List<Writer> listWriters;
	private List<String> nameList;
	public ChatServerThread() {

	}
	public String getNickname(){
		return this.nickname;
	}
	public ChatServerThread(Socket socket,List<Writer> listWriter,List<String> name) {
		this.socket = socket;
		this.listWriters=listWriter;
		this.nameList=name;
	}
	private void addWriter(Writer writer){
		synchronized(listWriters){
			listWriters.add(writer);
		}
	}
	private boolean addName(String name){
		for(String str:this.nameList){
			System.out.println("name:"+str);
			System.out.println("들어온 name:"+name);
			if(str.equals(name)){
				return false;
			}
		}
		synchronized(nameList){
			nameList.add(name);
		}
		return true;
	}
	private void broadcast(String data){
		synchronized(listWriters){
			for(Writer writer : listWriters){
				PrintWriter printWriter=(PrintWriter)writer;
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}
	private void removeWriter(Writer writer){
		for(int i=0 ; i< listWriters.size();i++){
			if(listWriters.get(i).equals(writer)){
				listWriters.remove(i);
			}
		}
	}

	private void doJoin(String nick, Writer writer){
		if(addName(nick)){
			this.nickname=nick;
			String data="join:"+nick+"님이 참여하였습니다."+"\r\n";
			broadcast(data);
			addWriter(writer);
			//ack
			PrintWriter printWriter=(PrintWriter)writer;
			printWriter.println("join:ok");
			printWriter.flush();
		}
	
	}
	private void doMessage(String message){
		String data="message:"+"["+this.nickname+"]"+message+"\r\n";
		broadcast(data);
	}
	private void doQuit(Writer writer){

		((PrintWriter)writer).println("quit:끝");
		removeWriter(writer);
		String data="message:"+this.nickname+"님이 퇴장하였습니다."+"\r\n";
		broadcast(data);
	
	}
	@Override
	public void run() {
		BufferedReader bufferedReader=null;
		PrintWriter printWriter=null;

		try {
		
			//1.Remote Host inFormation
			InetSocketAddress inetSocketAddress =(InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remotehostPort=inetSocketAddress.getPort();
			System.out.println("[클라이언트 연결됨] from "+remoteHostAddress+":"+remotehostPort);
			
			//2.스트림 얻기.
			bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream(),
							StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(), StandardCharsets.UTF_8));
			//3.요청 처리.
			while(true){
				String request=bufferedReader.readLine();
				
				if(request == null){
					System.out.println("클라이언트 연결 끊김");
					doQuit(printWriter);
					break;
				}
				//4.프로토콜 분석.
				String[] tokens=request.split(":");
				if("join".equals(tokens[0])){
					doJoin(tokens[1],printWriter);
				}else if("message".equals(tokens[0])){
					System.out.println(tokens[1]);
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])){
					doQuit(printWriter);
					break;
				}else{
					System.out.println(tokens[0]);
					System.out.println("[에러] 알수없는 요청"+"("+tokens[0]+")");
				}
			}
			
		} catch (IOException ex) {
			System.out.println("[서버 스레드] 에러: " + ex);
		} finally {
			try{
				if(bufferedReader != null){
					bufferedReader.close();
				}
				if(printWriter != null){
					printWriter.close();
				}
			}catch(IOException e){
				System.out.println("[서버] 닫기 에러: "+e);
			}
		
		}

	}
}
