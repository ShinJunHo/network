package com.hanains.network.util;

import java.util.Scanner;

public class NSLookup {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while(true){
			String hostName =scanner.nextLine();
			if("exit".equals(hostName)){
				break;
			}
		}
	}
}
