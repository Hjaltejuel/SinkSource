import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Michelle on 25-10-2017.
 */

public class Source {
    public static void main(String[] args) throws IOException {
        Socket connectSocket = null;
        try {
            connectSocket = new Socket("localhost",7000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            String input;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToServer = new DataOutputStream(connectSocket.getOutputStream());
            while(true){
                input = inFromUser.readLine();
                outToServer.writeBytes(input+ '\n');

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectSocket.close();
        }
    }
}
