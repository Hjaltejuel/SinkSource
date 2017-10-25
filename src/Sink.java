import java.io.*;
import java.net.Socket;

/**
 * Created by Michelle on 25-10-2017.
 */
public class Sink {
    public static void main(String[] args) throws IOException {
        Socket connectSocket = null;
        try {
            connectSocket = new Socket("localhost", 7200);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
