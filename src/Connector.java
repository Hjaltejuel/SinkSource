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
            //Actiate the Sink thread
            activateSinks();
            //Activate the sources
            activateSources();
    }

    /**
     * Starts the sinkRecieven thread which will keep adding sinks to a queue
     */
    public static void activateSinks(){
        //Make a new thread
        Thread SinkSpawner = new Thread(() -> {

            //Initialize the serversocket
            ServerSocket sinksInc = null;
            try {
                sinksInc = new ServerSocket(7200);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Start adding the incoming sinks
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
        //Start the thread
        SinkSpawner.start();
    }

    /**
     * Start the activateSources method which will recieve sources and start a new thread for each of them
     */
        public static void activateSources(){
            //Start the source thread
            Thread SourceSpawner = new Thread(() -> {
                ServerSocket sourcesInc = null;
                try {
                    sourcesInc = new ServerSocket(7000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        //Activate new sources and start a thread for them to run on
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

    /**
     * Starts the individual source thread
     * @param source
     */
    public static void activateSourceThread(Socket source){
            //Start the thread
            new Thread(() -> {
                //Initialises the inputreader and call the sendmessages method which will put the thread in recieving mode
                Socket currentSocket = null;
                BufferedReader inFromSources = null;
                try {
                    inFromSources = new BufferedReader(new InputStreamReader(source.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Keep sending messages everytime the client sends something new
                while (true) {
                   sendMessages(inFromSources,currentSocket);
                }

            }).start();
        }

    /**
     * Listens for incoming messages and when it gets one send it to all sinks
     * @param inFromSources
     * @param currentSocket
     */
        public static void sendMessages(BufferedReader inFromSources,Socket currentSocket){
            //initialize the input
            String input = null;
            try {
                input = inFromSources.readLine();
            } catch (IOException e) {
                //If a source has clossed handle it by returning out of the while loop and ending the thread
                System.out.println("Source has been closed" + '\n');
                return;
            }
            //Check for input errors
            if (input != null) {
                if (!input.equals("null")) {
                    System.out.println("A message has been sent from a source, now sending to sinks" + '\n');
                    //Go trough every sink and send to them
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
                            //If a conection isnt there handle it (remove from list)
                            sinks.remove(currentSocket);
                            System.out.println("A sink has been closed, and will be removed" + '\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

