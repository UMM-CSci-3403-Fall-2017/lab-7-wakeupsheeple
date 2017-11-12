package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		//create and start client
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		try {
			//create socket from hostname and port
			Socket socket = new Socket("localhost", PORT_NUMBER);
			//create and start socket threads for both input and output
			inputThread inputThread = new inputThread(socket);
			outputThread outputThread = new outputThread(socket);
			inputThread.start();
			outputThread.start();
		} catch (IOException e) {
			System.out.println("thread creation failed.");
		}
	}	

}

class inputThread extends Thread{
	 //initialize vars
	 private Thread thread; 
	 private Socket socket;
	
	 //constructor
	 inputThread(Socket s){
		socket = s;
	 }
	 
	 public void start() {
		//thread initialization
		thread = new Thread(this);
		thread.start();
	 }

	 public void run() { 
		 try{	
			//open streams and write bytes from server to standard output
			InputStream socketInputStream = socket.getInputStream();
			OutputStream socketOutputStream = socket.getOutputStream();
			int socketByte;
			while((socketByte = socketInputStream.read()) != -1){
				System.out.write((byte) socketByte);
			}
			//flush streams and close socket
			System.out.flush();
			socketOutputStream.flush();
			socket.close();
	
		 } catch (IOException e) {
			 System.out.println("inputThread Exception");
		 }
	 }
}

class outputThread extends Thread{
         //initialize vars
	 private Thread thread;
         private Socket socket;
	
	 //constructor
         outputThread(Socket s){
			socket = s;
         }

        public void start() {
		//thread initialization
		thread = new Thread(this);
		thread.start();
	}

         public void run() {
                 try{   
			//open stream and write standard input to server
	                OutputStream socketOutputStream = socket.getOutputStream();
			int readByte;
			while((readByte = System.in.read()) != -1){
				socketOutputStream.write((byte) readByte);
			}
			//flush streams
			System.out.flush();
			socketOutputStream.flush();
			socket.shutdownOutput();
                 } catch (IOException e){
                         System.out.println("outputThread Exception");
                 }
	 }
}
