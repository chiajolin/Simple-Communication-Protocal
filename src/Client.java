import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author chiajo
 * arg[0] = hostname, arg[1]= port number
 */
public class Client {
	public static void main(String[] args) {
		if (args.length != 2) { // Test for correct num of arguments
			System.err.println("ERROR server host name AND port number not given");
			System.exit(1);
		}
		
		int port_num = Integer.parseInt(args[1]);
		
		try {
			Socket c_sock = new Socket(args[0], port_num); 
			
			while(!c_sock.isClosed()){
				ClientHandler cm = new ClientHandler(c_sock);
				cm.sendCommand();
				cm.handleCommand();
			}
		} 
		catch (IOException ex) { 
			ex.printStackTrace(); 
		}		
	}
}