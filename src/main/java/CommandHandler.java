public class CommandHandler {
    public String handle(Object parsedInput) {
        if (parsedInput == null) {
            return "$-1\r\n";
        }
        switch (parsedInput.getClass().getSimpleName()) {
            case "String":
                return "+" + "PONG" + "\r\n";
            case "String[]":
                String[] strings = (String[]) parsedInput;
                if (strings.length > 0 && "ECHO".equalsIgnoreCase(strings[0])) {
                    return "$" + strings[1].length() + "\r\n" + strings[1] + "\r\n";
                }
                return "+OK\r\n";
            case "Object[]":
                Object[] array = (Object[]) parsedInput;
                if (array.length > 0 && "ECHO".equalsIgnoreCase(array[0].toString())) {
                    return "$" + array[1].toString().length() + "\r\n" + array[1] + "\r\n";
                }
                return "*0\r\n";
            default:
                return "-ERR unknown type\r\n";
        }
    }
}
