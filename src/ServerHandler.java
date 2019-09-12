
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler {
	Socket socket;
	String command;
	private static String sendingFilePath;
	private static String sendingFileName;
	public ServerHandler(Socket socket) throws IOException{
		this.socket = socket;
	}
	
	/**
	 * to handle server command
	 * @throws IOException
	 */
	public void handleCommand() throws IOException{
		System.out.println("Client's command: " + command); //print command
		
		String[] splitString = command.trim().split("\\s+"); //split string by space	
		
		if(splitString[0].equals("GET")){
			sendingFileName = splitString[1];
			FileList fl = new FileList();
			ArrayList<String> fileList = fl.getFileLists();
			sendingFilePath = fl.getServerFilePath();
			
			if(fileList.contains(sendingFileName)){
				sendFile(sendingFilePath + sendingFileName);
			}
			
			else{
				System.out.println("ERROR: no such file named " + sendingFileName);
			}
		}	
		
		else if(splitString[0].equals("BOUNCE")){
			printMessage();
		}
		
		else if(splitString[0].equals("EXIT")){
			exit();
			socket.close();
		}
		else{
			System.out.println("Wrong command! Discard.");
		}
	}
	
	/**
	 * receive command from client
	 * @throws IOException
	 */
	public void receivedCommand() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		command = in.readLine();
	}
	
	/**
	 * exit
	 * @throws IOException
	 */
	private void exit() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Exit message: " + in.readLine());
	}
	
	/**
	 * bounce
	 * @throws IOException
	 */
	public void printMessage() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		out.println("I got your message"); 
		System.out.println("Client's message: " + in.readLine()); 	
	}
	
	/**
	 * send file
	 * @param file: file name
	 * @throws IOException
	 */
	public void sendFile(String file) throws IOException{
		//create input stream for reading file
		File readFile = new File(file); 		
		FileInputStream fis = new FileInputStream(readFile); 
		BufferedInputStream bis = new BufferedInputStream(fis);	
					
		//create output stream for transmitting file
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	
		dos.writeDouble(readFile.length()); //fileSize
		
		System.out.println("server file size:" + readFile.length() + "bytes");
		
		//create buffer
		byte[] buffer = new byte[2048]; 
		
		//start sending file
		int number;
		
		if(file.substring(file.length() - 3).equals("txt")){ //if file is text, print content
			System.out.println("This is a txt file, file contents:");
		}

		while((number = bis.read(buffer)) >= 0){ //if the whole file is read
			if(file.substring(file.length() - 3).equals("txt")){ //if file is text, print content			
				System.out.println(new String(buffer, 0, number));				
			}		
			dos.write(buffer, 0, number);
			dos.flush();
		}
		System.out.println("file sent successfully");
	}
}
