import java.io.*;
import java.net.*;
import java.util.*;

// A class to track and measure latency among clients in a session.
// Will also be used to send relevant data to clients.

public class HostSync implements Runnable {

	public boolean keepRunning = true;
	public boolean keepReceivingClients = true;

	public int portNumber;
	public ServerSocket serverSocket;
	public NetworkLog networkLog;

	public int averageVideoPositionAmongClients = 0;
	public HashMap<String, ClientConnection> clients;

	// Probably want a hashmap from IP Addresses to relevant client data?

	public HostSync() {
		portNumber = Constants.Network.HOST_SYNC_PORT;
		networkLog = new NetworkLog();
		clients = new HashMap<String, ClientConnection>();
		
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveClients() {
		
		try {
			serverSocket.setSoTimeout(2000);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while(keepReceivingClients) {
			try {

				String clientData;
				Socket clientSocket = serverSocket.accept();

				InetAddress addr = clientSocket.getInetAddress();
				int port = clientSocket.getPort();
			} catch(SocketTimeoutException e) {
				// We timed out without receiving a client.
			} catch(IOException e) {

			} 

		}

	}

	// Talk to all connected clients and attempt to estimate RTT (Round-Trip-Time) of a message.
	public void ping() {

	}

	// Run:
	// Want to continue accepting connections. 
	// For every accepted connection, grab relevant network data.
	// Store relevant network data if appropriate option is selected

	@Override
	public void run() {

		receiveClients();

		// Continue accepting connections
		// For every accepted connection grab relevant network data.
		
		try {
			serverSocket.setSoTimeout(0);
		} catch(SocketException e) {
			e.printStackTrace();
		}
		

		while(keepRunning) {

			try {
				String clientData;
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				while((clientData = in.readLine()) != null) {
					// Process clientData and submit it to NetworkLog and
					// use it to measure relevant data points.
				}
			} catch(IOException e) {
				networkLog.registerError();
			}

		}
	}

	public void stop() {
		keepRunning = false;
	}

	// Log:
	// Permanently save stored network data.

}

// A wrapper class to contain some information about an individual client connection.
class ClientConnection {
	public InetAddress addr;
	public int port;
	public long latency;


	public ClientConnection(InetAddress addr, int port) {
		this.addr = addr;
		this.port = port;
	}
}