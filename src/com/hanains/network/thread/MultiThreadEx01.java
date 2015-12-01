package com.hanains.network.thread;

public class MultiThreadEx01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread thread1= new DigitThread();
		
		thread1.start();
		for (char c = 'A'; c <= 'Z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//한개를 Thread로 돌려버리면 ?..
	
	}

}
