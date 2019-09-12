
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler {
	Socket socket;
	String command;
	String[] splitCommand;
	private static String receivingFilePath;
	private static String receivingFileName;
	private static String bounceMessage;
	private static String defaultExitMessage = "Normal_Exit";
	private static String clientExitMessage;
	
	public ClientHandler(Socket socket) throws IOException{
		this.socket = socket;
	}
	
	/**
	 * to handle client command
	 * @throws IOException
	 */
	public void handleCommand() throws IOException{
		splitCommand = command.trim().split("\\s+"); //split string by space
		//when command = GET
		if(splitCommand[0].equals("GET")){
			receivingFileName = splitCommand[1];
			FileList fl = new FileList();
			ArrayList<String> fileList = fl.getFileLists();
			receivingFilePath = fl.getClientFilePath();
			
			if(fileList.contains(receivingFileName)){
				receiveFile(receivingFilePath + receivingFileName);
			}
			else{
				System.out.println("ERROR: no such file named " + receivingFileName);
			}
		}	
		//when command = BOUNCE
		else if (splitCommand[0].equals("BOUNCE")){
			bounceMessage = splitCommand[1];
			printMessage(bounceMessage);
		}		
		//when command = EXIT
		else if(splitCommand[0].equals("EXIT")){
			if(splitCommand.length == 1){
				exit(defaultExitMessage);
			}
			else{
				clientExitMessage = splitCommand[1];
				exit(clientExitMessage);
			}
		}
		//when command should be discarded
		else{
			System.out.println("Wrong command! Discard. Please retype command.");
		}
	}
	
	/**
	 * send command to server
	 * @throws IOException
	 */
	public void sendCommand() throws IOException{		
		System.out.print("Please type the command:"); 
		Scanner commandScanner = new Scanner(System.in);
		command = commandScanner.nextLine();
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		pw.println(command);
	}
	
	/**
	 * exit
	 * @param exitMessage: user exit message
	 * @throws IOException
	 */
	private void exit(String exitMessage) throws IOException{
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		pw.println(exitMessage);
		socket.close();
	}
	
	/**
	 * bounce
	 * @param message: user message
	 * @throws IOException
	 */
	private void printMessage(String message) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		pw.println(message); 
		System.out.println("Server says: " + in.readLine()); 
	}
	
	/**
	 * get file
	 * @param file
	 * @throws IOException
	 */
	private void receiveFile(String file) throws IOException{
		//create BufferedReader to get file size
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		 
		
		double fileSize = dis.readDouble();
		System.out.println("client file size:" + fileSize + "bytes");
		
		//create output stream for writing file
		File writtenFile = new File(file);
		FileOutputStream fos = new FileOutputStream(writtenFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
			
		//create buffer
		byte[] buffer = new byte[2048];
		
		//start receiving file
		double totalGetSize = 0.0;
		int number;
		while((number = dis.read(buffer)) >= 0 ){ //if the whole file is read
			bos.write(buffer, 0, number);
			bos.flush();
			totalGetSize += number;
			if(totalGetSize == fileSize){
				System.out.println("file received successfully");
				break;
			}
		}
	}
}
