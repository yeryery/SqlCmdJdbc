package ua.com.juja.yeryery;

import ua.com.juja.yeryery.commands.CancelException;

public class Parser {

    private int parsedInt;

    public int getParsedInt() {
        return parsedInt;
    }

    public boolean isParsable(String data) {
        boolean parsable = true;
        try {
            parsedInt = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    public Object defineType(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return data;
        }
    }

    private int count(String data, String delimiter) {
        String[] splitData = data.split(delimiter);
        return splitData.length;
    }

    public String[] splitData(String data, String sample, String delimiter) {
        int sampleSize = count(sample, delimiter);
        int dataSize = count(data, delimiter);

        if (data.equals("cancel")) {
            throw new CancelException();
        }

        if (dataSize != sampleSize) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s!", sampleSize, dataSize));
        }

        return data.split(delimiter);
    }

    public String[] splitByTwo(String data, String delimiter) {
        int dataSize = count(data, delimiter);

        if (data.equals("cancel")) {
            throw new CancelException();
        }

        if (dataSize % 2 != 0) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected even number of parameters (2, 4 and so on) and you have entered %s!", dataSize));
        }

        return data.split(delimiter);
    }
}
