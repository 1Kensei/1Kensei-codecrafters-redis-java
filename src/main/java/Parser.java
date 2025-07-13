import java.util.Arrays;

public class Parser {
    public Object parse(String input) {
        if(input == null) {
            throw new RuntimeException();
        }
        char type = input.charAt(0);
        return switch (type) {
            case '+' -> parseSimpleString(input);
            case '$' -> parseBulkString(input);
            case '*' -> parseArray(input);
            default -> "";
        };
    }

    private String parseSimpleString(String input) {
        return input.substring(1, input.length()-2);
    }

    private String[] parseBulkString(String input) {
        String[] arr = input.substring(1, input.length()-2).split("\r\n");
        int length = Integer.parseInt(arr[0]);
        if(length == -1) {
            return null;
        }
        return arr;
    }

    private Object[] parseArray(String input) {
        String[] arr = input.substring(1, input.length()-2).split("\r\n");
        int length = Integer.parseInt(arr[0]);
        if(length == -1) {
            return null;
        }
        Object[] parsedArr = new Object[length];
        int index = 1;
        for (int i = 0; i < length; i++) {
            String element = arr[index++];
            if (element.startsWith("$")) {
                int len = Integer.parseInt(element.substring(1));
                if (len == -1) {
                    parsedArr[i] = null;
                } else {
                    parsedArr[i] = arr[index++];
                }
            } else {
                parsedArr[i] = parse(element + "\r\n");
            }
        }
        return parsedArr;
    }

    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();
        System.out.println(commandHandler.handle(new Parser().parse("*2\r\n$4\r\necHO\r\n$3\r\nASDASD\r\n")));
    }
}
