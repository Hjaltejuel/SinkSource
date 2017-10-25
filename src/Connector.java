import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by Michelle on 25-10-2017.
 */
public class Connector {
    public static void main(String[] args) {
            ConcurrentLinkedQueue<Socket> sinks = new ConcurrentLinkedQueue<>();

            Thread SinkSpawner = new Thread(() -> {
                ServerSocket sinksInc = null;
                try {
                    sinksInc = new ServerSocket(7200);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Socket sink = sinksInc.accept();
                        sinks.add(sink);
                        System.out.println("hello" + sinks.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            Thread SourceSpawner = new Thread(() -> {
                ServerSocket sourcesInc = null;
                try {
                    sourcesInc = new ServerSocket(7000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Socket source = sourcesInc.accept();

                        new Thread(() -> {
                            try {
                                BufferedReader inFromSources =
                                        new BufferedReader(new InputStreamReader(source.getInputStream()));
                                while (true) {

                                    String input = inFromSources.readLine();
                                    System.out.println("I HAVE RECIEVED :" + input + " AND I AM NOW SENDING TO SINKS");
                                    int i = 0;
                                    for (Socket socket : sinks) {
                                        i++;
                                        System.out.println(i);
                                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                        out.writeBytes(input + '\n');
                                    }
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        SinkSpawner.start();
        SourceSpawner.start();

        }
    }

