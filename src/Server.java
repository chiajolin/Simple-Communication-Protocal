import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author chiajo
 * args[0] = port number
 */
public class Server {
	public static void main(String[] args) {
		if (args.length != 1) { // Test for correct num of arguments
			System.err.println( "ERROR server port number not given");
	        System.exit(1);
		}
		
		int port_num = Integer.parseInt(args[0]);
		ServerSocket rv_sock = null;
		
		try {
			rv_sock = new ServerSocket(port_num); //server socket rv_sock remains open, waiting for new clients to connect
		} 
		catch (IOException ex) {
			ex.printStackTrace(); 
		}
		
		while (true) { // run forever, waiting for clients to connect
			System.out.println("\nWaiting for client to connect...");
        
			try {
				Socket s_sock = rv_sock.accept(); 
				while(!s_sock.isClosed()){
					ServerHandler sm = new ServerHandler(s_sock);
					sm.receivedCommand();
					sm.handleCommand();
				}
				
				//s_sock.close();
			} 
			catch (IOException ex) {
				ex.printStackTrace(); 
			}
		}
	} 
}