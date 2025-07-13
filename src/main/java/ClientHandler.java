import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String inputLine = "";
            while ((inputLine = bufferedReader.readLine())!=null) {
                if (inputLine.startsWith("PING")) {
                    bufferedWriter.write("+PONG\r\n");
                    bufferedWriter.flush();
                }
                else if (inputLine.toUpperCase().startsWith("ECHO")) {
                    bufferedReader.readLine();
                    String line = bufferedReader.readLine();
                    bufferedWriter.write(String.format("$%d\r\n%s\r\n", line.length(), line));
                    bufferedWriter.flush();
                }
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
