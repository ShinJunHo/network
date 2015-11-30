package com.hanains.network.util;

import java.util.Scanner;

public class MainClass {
	/*some changes*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scan = new Scanner(System.in);
		String host;
		System.out.println("[c:\\~]$ nslookup");
		while(true){
			System.out.println(">>");
			host=scan.nextLine();
			if("exit".equals(host)){
				break;
			}
			new NslookupImplement().resolve(host);
		}
	}
}
