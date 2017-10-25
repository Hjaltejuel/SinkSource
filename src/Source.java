/**
 * Created by Michelle on 25-10-2017.
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

    public class Source {
        public Source() {
        }

        public static void main(String[] var0) throws IOException {
            Socket connectSocket = null;

            try {
                connectSocket = new Socket("localhost", 7000);
            } catch (IOException var9) {
                var9.printStackTrace();
            }

            try {
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

