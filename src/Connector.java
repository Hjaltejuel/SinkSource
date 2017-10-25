import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Michelle on 25-10-2017.
 */
public class Connector {
    public static void main(String[] args) {
        try {
            ServerSocket sourcesInc = new ServerSocket(7000);
            ServerSocket sinksInc = new ServerSocket(7200);
            Socket sources = sourcesInc.accept();
            Socket sinks = sinksInc.accept();
            System.out.println(sinks.getInputStream());
            System.out.println(sources.getInputStream());
            BufferedReader inFromSources =
                    new BufferedReader(new InputStreamReader(sources.getInputStream()));
            DataOutputStream outToSinks = new DataOutputStream(sinks.getOutputStream());
            while(true){
                String input = inFromSources.readLine();
                System.out.println("I HAVE RECIEVED AN ANSWER " + input);
                outToSinks.writeBytes(input + '\n');

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
