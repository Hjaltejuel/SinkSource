import java.io.*;
import java.net.Socket;

/**
 * Sink recieves the messages from the connector that originates from a source
 */
public class Sink {
    public static void main(String[] args) throws IOException {
        Socket connectSocket = null;
        /**
         * connectSocket creates a new socket which takes an IP-address and a port.
         * Both of which is used to connect to a Connector.
         */
        try {

            connectSocket = new Socket("10.26.44.186", 7200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * infromserver recieves messages from the connector. the whileloop will run and output the messages sent to the terminal.
         * the connection terminates when the process is closed.
         */
        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
            while(true) {
                String output = inFromServer.readLine();
                System.out.println(output);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
