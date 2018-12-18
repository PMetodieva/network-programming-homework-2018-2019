import java.net.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.io.*;

public class HTTPServer {

	private ServerSocket ss;
	
	public HTTPServer() throws IOException {
		this.ss = new ServerSocket(8888);
	}
	
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
	
	public static void main(String[] args) throws IOException {
		System.out.println("Test");
		HTTPServer server = new HTTPServer();
	}
}