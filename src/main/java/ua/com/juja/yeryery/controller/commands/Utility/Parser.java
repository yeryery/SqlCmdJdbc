package ua.com.juja.yeryery.controller.commands.Utility;

public class Parser {

    public static int parsedInt;

    public static boolean isParsable(String data) {
        boolean parsable = true;
        try {
            parsedInt = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    public static Object checkType(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return data;
        }
    }
}
