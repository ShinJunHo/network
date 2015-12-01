package com.hanains.network.thread;

public class MultiThreadEx03 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread thread=new Thread(new AlphabetRunnableImple());
		//혼자 start를 못하기에 Thread에 넣어준다.
		thread.start();
		
		for (char c = 'A'; c <= 'Z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
