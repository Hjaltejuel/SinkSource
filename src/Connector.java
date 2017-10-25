import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * Created by Michelle on 25-10-2017.
 */
public class Connector {
    static ConcurrentLinkedQueue<Socket> sinks = new ConcurrentLinkedQueue<>();
    public static void main(String[] args) {

            activateSinks();

            activateSources();

        }
        public static void activateSources(){
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
                        System.out.println("A source Process is starting" + '\n');
                        activateSourceThread(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            SourceSpawner.start();
        }
        public static void activateSourceThread(Socket source){
            new Thread(() -> {
                Socket currentSocket = null;
                BufferedReader inFromSources = null;
                try {
                    inFromSources = new BufferedReader(new InputStreamReader(source.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                   sendMessages(inFromSources,currentSocket);
                }

            }).start();
        }
        public static void sendMessages(BufferedReader inFromSources,Socket currentSocket){
            String input = null;
            try {
                input = inFromSources.readLine();
            } catch (IOException e) {
                System.out.println("Source has been closed" + '\n');
                return;
            }
            if (input != null) {
                if (!input.equals("null")) {
                    System.out.println("A message has been sent from a source, now sending to sinks" + '\n');
                    for (Socket socket : sinks) {
                        currentSocket = socket;
                        DataOutputStream out = null;
                        try {
                            out = new DataOutputStream(socket.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.writeBytes(input + '\n');
                        } catch (SocketException e) {
                            sinks.remove(currentSocket);
                            System.out.println("A sink has been closed, and will be removed" + '\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        public static void activateSinks(){

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
                        System.out.println("A sink process has joined"+'\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            SinkSpawner.start();
        }
    }

