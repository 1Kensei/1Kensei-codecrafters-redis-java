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
        while (socket.isConnected()) {
            try {
                String inputLine;
                Parser parser = new Parser();
                CommandHandler commandHandler = new CommandHandler();
                while ((inputLine = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(commandHandler.handle(parser.parse(inputLine)));
                        bufferedWriter.flush();
                }
            }
            catch (IOException e) {
                throw new RuntimeException();
            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
