import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Map<String, String> map = new HashMap<>();

    private Socket socket;


    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        CommandDispatcher commandDispatcher = new CommandDispatcher();
        try {
            commandDispatcher.dispatch(RequestHandler.parseRespCommand(socket.getInputStream()));
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
