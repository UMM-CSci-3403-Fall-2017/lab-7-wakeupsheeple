package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		//create and start server	
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {	
		//open server socket and create thread for accepted connection
		try(ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)){				
			System.out.println("listening...");
			while (true) {	
				multiThread m = new multiThread(serverSocket);
			        m.start();	
			}
		} catch(IOException e) {
			System.out.println("ServerSocket creation failed"); 
		}
	}
}

class multiThread extends Thread {
	//initialize vars
	private Thread thread;
	private Socket socket;

	multiThread(ServerSocket s){
		try{	
			//accept connection
			System.out.println("Thread created...");
			socket = s.accept();
		} catch (IOException e){
			System.out.println("multiThread exception");
		}

	}

	public void run(){
		try{
			//open streams and write bytes from input to output
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			int b;
			while((b = inputStream.read()) != -1){
				outputStream.write(b);
			}
			//flush streams and close socket due to end of input
			System.out.flush();
			outputStream.flush();
			socket.close();
		} catch (IOException e){
			System.out.println("run exception");
		}
	}

	public void start(){
		//thread constructor
		thread = new Thread(this);
		thread.start();
	}
}
