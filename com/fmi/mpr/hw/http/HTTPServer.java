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
					"<h1>Hello</h1>" + 
					"<form method=\"POST\" action=\"/\">" +
						"<input type=\"text\" name=\"a\"/>" +
						"<input type=\"text\" name=\"b\"/>" +
						"<input type=\"text\" name=\"oper\"/>" +
						"<input type=\"submit\" value=\"Send\">" +
					"</form>" +
					"<h2>" + (response == null || response.trim().isEmpty() ? "" : response) + "</h2>" +
					"</body>\n" + 
					"</html>");
		}
	}
	
	//To Be Done
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

		String[] lines = request.split("\n");
		
		String firstHeader = lines[0];
		String uri = firstHeader.split(" ")[1];
		
		return null;
	}

	private void sendVideo(PrintStream ps) throws IOException {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Content-Type: video/mp4");
		ps.println();
		
		try (FileInputStream fis = new FileInputStream(new File("video.mp4"))) {
			
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			
			while ((bytesRead = fis.read(buffer, 0, 8192)) > 0) {
				ps.write(buffer, 0, bytesRead);
			}
		}
		System.out.println("Send video");
	}

	//To Be Done
	private void sendPic(PrintStream ps) throws IOException {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Content-Type: image");
		ps.println();
		
		try (FileInputStream fis = new FileInputStream(new File("video.mp4"))) {
			
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			
			while ((bytesRead = fis.read(buffer, 0, 8192)) > 0) {
				ps.write(buffer, 0, bytesRead);
			}
		}
		System.out.println("Send video");
	}
	
	//To Be Done
	private void sendText(PrintStream ps) throws IOException {
		ps.println("HTTP/1.0 200 OK");
		ps.println("Content-Type: video/mp4");
		ps.println();
		
		try (FileInputStream fis = new FileInputStream(new File("video.mp4"))) {
			
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			
			while ((bytesRead = fis.read(buffer, 0, 8192)) > 0) {
				ps.write(buffer, 0, bytesRead);
			}
		}
		System.out.println("Send video");
	}
	
	//To Be Done
	private String parseBody(String body) {
		
		if (body != null && !body.trim().isEmpty()) {
			
		}
		return null;
	}

	
	public static void main(String[] args) throws IOException {
		System.out.println("Test");
		HTTPServer server = new HTTPServer();
		server.start();
	}
}