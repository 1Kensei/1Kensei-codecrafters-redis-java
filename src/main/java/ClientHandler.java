import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Map<String, String> map = new HashMap<>();

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
                } else if (inputLine.toUpperCase().startsWith("SET"))  {
                    bufferedReader.readLine();
                    String key = bufferedReader.readLine();
                    bufferedReader.readLine();
                    String value = bufferedReader.readLine();
                    map.put(key,value);
                    bufferedWriter.write("+OK\r\n");
                    bufferedWriter.flush();
                }
                else if (inputLine.toUpperCase().startsWith("GET")) {
                    bufferedReader.readLine();
                    String key = bufferedReader.readLine();
                    if(map.containsKey(key)) {
                        bufferedWriter.write(String.format("$%d\r\n%s\r\n", map.get(key).length(), map.get(key)));
                    }
                    else {
                        bufferedWriter.write("$-1\r\n");
                    }
                    bufferedWriter.flush();
                }
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
