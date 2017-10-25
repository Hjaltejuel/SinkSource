/**
 * Created by Michelle on 25-10-2017.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Source creates an object which sends messages to the Connector which distribute this message to all active Sinks.
 */
public class Source {
        public Source() {
        }

        public static void main(String[] var0) throws IOException {
            Socket connectSocket = null;

            try {
                /**
                 * connectSocket creates a new socket which takes an IP-address and a port.
                 * Both of which is used to connect to a Connector.
                 */
                connectSocket = new Socket("10.26.44.186", 7000);
            } catch (IOException var9) {
                var9.printStackTrace();
            }

            try {
                /**
                 * Read input from System.in and continue to make strings based on this input until forcibly closed.
                 * Outputs a byte-array based on the string input. These are sent to the Connector via the connectSocket.
                 */
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream outputStream = new DataOutputStream(connectSocket.getOutputStream());

                while(true) {
                    String input = inputReader.readLine();
                    outputStream.writeBytes(input + '\n');
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            } finally {
                connectSocket.close();
            }

        }
    }

