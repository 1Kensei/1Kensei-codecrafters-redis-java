import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandDispatcher {
    private Map<String, String> storage = new HashMap<>();
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public String dispatch(String[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        return dispatchInternalCommand(args);
    }

    public String dispatchInternalCommand(String[] args) {
        String command = args[0].toUpperCase();
        return switch (command) {
            case "PING" -> handleSimpleString("PONG");
            case "ECHO" -> handleBulkString(args[1]);
            case "SET" -> handleSetCommand(args);
            case "GET" -> handleGetCommand(args);
            default -> "";
        };
    }

    private String handleBulkString(String input) {
        return String.format("$%d\r\n%s\r\n", input.length(), input);
    }

    private String handleSimpleString(String input) {
        return String.format("+%s\r\n", input);
    }

    private String handleSetCommand(String[] args) {
        String key = args[1];
        String value = args[2];

        storage.put(key, value);
        if (args.length >= 4 &&"px".equalsIgnoreCase(args[3])) {
            long expiryTime = Long.parseLong(args[4]);
            scheduledExecutorService.schedule(() -> storage.remove(key), expiryTime, TimeUnit.MILLISECONDS);
        }

        return "+OK\r\n";
    }

    private String handleGetCommand(String[] args) {
        if (args.length < 2) {
            return "-ERR wrong number of arguments for GET\r\n";
        }
        String key = args[1];
        if (storage.containsKey(key)) {
            return handleBulkString(storage.get(key));
        } else {
            return "$-1\r\n";
        }
    }

}
