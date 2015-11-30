package com.hanains.network.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NslookupImplement{

	public void resolve(String host){
		try{
			InetAddress[] inetAddress=InetAddress.getAllByName(host);
			//System.out.println("Host:"+inetAddress.getHostName());
			/*for(InetAddress ipaddress:inetAddress){
				System.out.println("Ip Address: "+ipaddress.getHostAddress());
			}
			*/
			for(int i=0; i< inetAddress.length;i++){
				System.out.println(inetAddress[i].getHostName()+" : "+inetAddress[i].getHostAddress());
			}
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
	}
	
}
