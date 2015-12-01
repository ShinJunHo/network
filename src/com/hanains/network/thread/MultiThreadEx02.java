package com.hanains.network.thread;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadEx02 {

	public static void main(String[] args) {
		
		List list = new ArrayList();
		
		Thread thread1=new DigitThread(list);
		Thread thread2=new LowerCaseAlphabetThread();
		Thread thread3=new DigitThread(list);
		
		thread1.start();
		thread2.start();
		thread3.start();
		//어떤 작업을 Thread에 태울까?~
		//중요한것도 있는데 동기화의 문제가 있는데.
		
	}

}
