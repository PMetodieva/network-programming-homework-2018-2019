package com.fmi.mpr.hw.http;

import java.net.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.io.*;

public class HTTPServer {

	private ServerSocket ss;
	private boolean isRunning;
	private String fileName;
	
	public HTTPServer() throws IOException {
		this.ss = new ServerSocket(8888);
	}
	
	public void start() {
		
		if (!isRunning) {
			this.isRunning = true;
			run();
		}
	}
	
	private void run() {
		
		while(isRunning) {
			
			try {
				listen();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void listen() throws IOException {
		
		Socket client = null;
		try {
			client = ss.accept();
			System.out.println(client.getInetAddress() + " connected..!");
			
			processClient(client);
		
			System.out.println("Connection to " + client.getInetAddress() + " closed..!");
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
	private void processClient(Socket client) throws IOException {
		
		try (BufferedInputStream br = new BufferedInputStream(client.getInputStream());
			 PrintStream ps = new PrintStream(client.getOutputStream(), true)) {
			
			String response = read(ps, br);
			write(ps, response);		
		}
		
	}
	
	//To Be Done
	private void write(PrintStream ps, String response) {
		
		if (ps != null) {
			ps.println("HTTP/1.0 200 OK");
			ps.println();

			ps.println("<!DOCTYPE html>\n" + 
					   "<html>\n" + 
					   "<head>\n" + 
					   "	<title></title>\n" + 
					   "</head>\n" + 
					   "<body>\n" + 
					   "<form action=\"/action_page.php\">\n" + 
					   "			  <input type=\"file\" name=\"pic\" accept=\"image/*\">\n" + 
					   "			  <input type=\"submit\">\n" + 
					   "			</form> " +
					   "</body>\n" + 
					   "</html>");
		}
	}
	

	private String read(PrintStream ps, BufferedInputStream bis) throws IOException {
		
		if (bis != null) {
			StringBuilder request = new StringBuilder();
			
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			
			while ((bytesRead = bis.read(buffer, 0, 1024)) > 0) {
				request.append(new String(buffer, 0, bytesRead));
				
				if (bytesRead < 1024) {
					break;
				}
			}
			
			return parseRequest(ps, request.toString());
		}
		return "Error";
	}
	
	private String parseRequest(PrintStream ps, String request) throws IOException {
		
		System.out.println(request);

		String firstHeader =  request.split("\n")[0];
		String type = firstHeader.split(" ")[0];
		String uri= firstHeader.split(" ")[1];
		fileName = uri.substring(1);
		
		String typeOfExtension = uri.split("\\.")[1];
		
		if(type.equals("GET")){
			return get(ps, typeOfExtension);
		}
		else if(type.equals("POST")){
			return post(ps, typeOfExtension);
		}
		
		return null;
	}
	
	private String get(PrintStream ps, String typeOfExtension) throws IOException {
	
		ps.println("HTTP/1.1 200 OK");
		ps.println();
		
		if (typeOfExtension.equals("mp4") || typeOfExtension.equals("avi")) {
	
			try {
				
				sendVideo(ps);
			} catch (IOException e) {

				ps.println("<!DOCTYPE html>\n" + 
						   "<html>\n" + 
						   "<head>\n" + 
						   "	<title></title>\n" + 
						   "</head>\n" + 
						   "<body>\n" + 
						   "<form action=\"/action_page.php\">\n" + 
						   "			  <input type=\"file\" name=\"pic\" accept=\"image/*\">\n" + 
						   "			  <input type=\"submit\">\n" + 
						   "			</form> " +
						   "</body>\n" + 
						   "</html>");
			}
		}
		
		else if(typeOfExtension.equals("png") || typeOfExtension.equals("jpg") || typeOfExtension.equals("bmp")) {
			
			try {
				sendPic(ps);
			} catch (IOException e) {

				ps.println("<!DOCTYPE html>\n" + 
						   "<html>\n" + 
						   "<head>\n" + 
						   "	<title></title>\n" + 
						   "</head>\n" + 
						   "<body>\n" + 
						   "<form action=\"/action_page.php\">\n" + 
						   "			  <input type=\"file\" name=\"pic\" accept=\"image/*\">\n" + 
						   "			  <input type=\"submit\">\n" + 
						   "			</form> " +
						   "</body>\n" + 
						   "</html>");
			}
		}
		
		else if(typeOfExtension.equals("txt")) {

			try {
				
				sendTxt(ps);
			} catch (IOException e) {

				ps.println("<!DOCTYPE html>\n" + 
						   "<html>\n" + 
						   "<head>\n" + 
						   "	<title></title>\n" + 
						   "</head>\n" + 
						   "<body>\n" + 
						   "<form action=\"/action_page.php\">\n" + 
						   "			  <input type=\"file\" name=\"pic\" accept=\"image/*\">\n" + 
						   "			  <input type=\"submit\">\n" + 
						   "			</form> " +
						   "</body>\n" + 
						   "</html>");
			}
		}
		return null;
	}

	private String post(PrintStream ps,  String typeOfExtension) throws IOException {

		return null;
	}
	

	private void sendVideo(PrintStream ps) throws IOException {
		
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\valio\\mpr\\network-programming-homework-2018-2019\\com\\fmi\\mpr\\hw\\http\\" + fileName));
		
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		
		while ((bytesRead = fis.read(buffer, 0, 8192)) > 0) {
			ps.write(buffer, 0, bytesRead);
		}
		
		ps.flush();
		System.out.println("Send video");
		fis.close();
	}

	private void sendPic(PrintStream ps) throws IOException {
		
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\valio\\mpr\\network-programming-homework-2018-2019\\com\\fmi\\mpr\\hw\\http\\" + fileName));
		
		int bytesRead = 0;
		byte[] buffer = new byte[4096];
		
		while ((bytesRead = fis.read(buffer, 0, 4096)) > 0) {
			ps.write(buffer, 0, bytesRead);
		}
		
		ps.flush();
		System.out.println("Send pic");
		fis.close();
	}
	
	private void sendTxt(PrintStream ps) throws IOException {
		
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\valio\\mpr\\network-programming-homework-2018-2019\\com\\fmi\\mpr\\hw\\http\\" + fileName));
		
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		
		while ((bytesRead = fis.read(buffer, 0, 8192)) > 0) {
			ps.write(buffer, 0, bytesRead);
		}
		
		ps.flush();
		System.out.println("Send txt");
		fis.close();
	}
	
	//To Be Done
	private String parseBody(String body) {
		
		if (body != null && !body.trim().isEmpty()) {
			
		}
		return null;
	}

	
	public static void main(String[] args) throws IOException {
		//System.out.println("Test");
		HTTPServer server = new HTTPServer();
		server.start();
	}
}