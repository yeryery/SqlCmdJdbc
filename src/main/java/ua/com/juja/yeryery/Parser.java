package ua.com.juja.yeryery;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private int parsedInt;

    public int getParsedInt() {
        return parsedInt;
    }

    public boolean isParsable(String input) {
        boolean parsable = true;
        try {
            parsedInt = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    public Object defineType(String input) {
        if (isParsable(input)) {
            return parsedInt;
        } else {
            return input;
        }
    }

    public List<Object> parseByDelimiter(String input, String delimiter) {
        List<Object> result = new LinkedList<>();

        Scanner scanner = new Scanner(input).useDelimiter(delimiter);

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                result.add(scanner.nextInt());
            } else {
                result.add(scanner.next());
            }
        }

        scanner.close();
        return result;
    }

    public int count(String input, String delimiter) {
        String[] data = input.split("\\|");
        return data.length;
    }
}
