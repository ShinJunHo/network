package com.hanains.network.thread;

import java.util.ArrayList;
import java.util.List;

public class DigitThread extends Thread {
	private List list =new ArrayList();
	
	public DigitThread(){
		//생성자 만들어 주면 Default 생성자를 만들어 줄 필요
	}
	public DigitThread(List list){
		this.list =list;
	}
	
	//특별한 메소드를 하나 오버라이드 해줘야 한다.
	@Override //메소드의 부가정보를 알려주는.
	public void run() {
		for(int i=0; i<10;i++){
			System.out.print(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
