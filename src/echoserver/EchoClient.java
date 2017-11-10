package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6014;
	public static Socket socket;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		socket = new Socket("localhost", PORT_NUMBER);
		client.start();
	}

	private void start() throws IOException {
		try {
			inputThread inputThread = new inputThread(socket);
			outputThread outputThread = new outputThread(socket);
			inputThread.start();
			outputThread.start();
			System.out.flush();
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			System.out.println("thread creation failed.");
		}
	}	

}

class inputThread extends Thread{
	 private Thread thread; 
	 private Socket socket;

	 inputThread(Socket s){
			socket = s;
	 }
	 
	 public void start() {
		thread = new Thread(this);
		thread.start();
	 }

	 public void run() { 
		 try{
			InputStream socketInputStream = socket.getInputStream();
			int readByte;
			while((readByte = System.in.read()) != -1){
				int socketByte = socketInputStream.read();
				System.out.write(socketByte);
			}
		 } catch (IOException e) {
			 System.out.println("inputThread Exception");
		 }
	 }
}

class outputThread extends Thread{
         private Thread thread;
         private Socket socket;

         outputThread(Socket s){
			socket = s;
         }

        public void start() {
		thread = new Thread(this);
		thread.start();
	}

         public void run() {
                 try{
                        OutputStream socketOutputStream = socket.getOutputStream();
			int readByte;
			while((readByte = System.in.read()) != -1){
				socketOutputStream.write(readByte);
			}
			socketOutputStream.flush();
			socket.shutdownOutput();
                 } catch (IOException e){
                         System.out.println("outputThread Exception");
                 }
	 }

}
