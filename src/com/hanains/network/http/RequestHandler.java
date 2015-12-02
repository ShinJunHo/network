package com.hanains.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler extends Thread {

	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;

		try {
			// get IOStream
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outputStream = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			SimpleHttpServer.consolLog("connected from "
					+ inetSocketAddress.getHostName() + ":"
					+ inetSocketAddress.getPort());

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// outputStream.write(
			// "Content-Type:text/html; charset=UTF-8\r\n".getBytes( "UTF-8" )
			// );
			// outputStream.write( "\r\n".getBytes() );
			// outputStream.write(
			// "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes(
			// "UTF-8" ) );

			String request = "";

			SimpleHttpServer
					.consolLog("======================== request information=========================");
			while (true) {
				String line = bufferedReader.readLine();// header정보를 알아내려고.
				if (line == null || "".equals(line)) {
					break; // / 바디가 시작되기 전까지 읽겠다.
				}
				if ("".equals(request)) {
					request = line;
					System.out.println("\t request: " + request);
				}
				SimpleHttpServer.consolLog(line);
				// 이제 line을 가지고 프로그램을 짜는거지.

			}
			SimpleHttpServer
					.consolLog("====================================================================");

			// 첫번째 라인만 받아서 요청 처리.
			String[] tokens = request.split(" ");// space

			System.out
					.println("\t tokens " + tokens[0] + tokens[1] + tokens[2]);
			if ("GET".equals(tokens[0])) {
				responseStaticResource(outputStream, tokens[1], tokens[2]);
				// Error가 여기로온다.
			} else {
				response400Error(outputStream, tokens[2]);
			}

		} catch (Exception ex) {
			SimpleHttpServer.consolLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}

				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException ex) {
				SimpleHttpServer.consolLog("error:" + ex);
			}
		}

	}

	private void response400Error(OutputStream outputStream, String protocol)
			throws IOException {
		/* get 이외의 프로토콜 처리시 */
		File file = new File("./webapp" + "/error/400.html");
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);
		outputStream.write((protocol + "400 Bad Request\r\n").getBytes());
		outputStream.write("Content-Type:text/html\r\n".getBytes());
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);
	}

	private void response404Error(OutputStream outputStream, String protocol)
			throws IOException {

		/* 해당 파일이 존재 안할때 */
		File file = new File("./webapp" + "/error/404.html");
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);

		outputStream.write((protocol + " 404 File Not Found\r\n").getBytes());
		outputStream.write("Content-Type:text/html\r\n".getBytes());
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);

	}

	private void responseStaticResource(OutputStream outputStream, String url,
			String protocol) throws IOException {
		// 여기를 작성해야한다.
		// '/' 이면 index.html로
		// default html처리.
		// SimpleHttpServer.consolLog( "url" + url );
		System.out.println("url = :" + url);
		if ("".equals(url) || "/".equals(url)) {
			url += "index.html";
			// SimpleHttpServer.consolLog( "default url: "+ url );
		}

		// file 존재여부를 check.

		// 파일 객체 생성.
		File file = new File("./webapp" + url);

		if (file.exists() == false) {
			//System.out.println("404 NOt Exsit");
			response404Error(outputStream, protocol);
			return;
		}
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);

		String mimeType = Files.probeContentType(path);
		System.out.println("\t mimeType" + mimeType);
		String tmpMime = "text/html";

		System.out.println("\t file exists() :" + file.exists());

		if ("text/css".equals(mimeType)) {
			tmpMime = "text/css";
		}
		if ("image/webp".equals(mimeType)) {
			tmpMime = "image/webp";
		}

		outputStream.write(("HTTP/1.1 200 OK\r\n").getBytes("UTF-8"));
		outputStream.write(("Content-Type:" + tmpMime + "; charset=UTF-8\r\n")
				.getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);

	}

}
