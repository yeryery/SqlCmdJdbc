package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;

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

    public static Object defineType(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return data;
        }
    }

    private static int count(String data, String delimiter) {
        String[] splitData = data.split(delimiter);
        return splitData.length;
    }

    public static String[] splitData(String data, String sample, String delimiter) {
        int sampleSize = count(sample, delimiter);
        int dataSize = count(data, delimiter);

        if (data.equals("cancel")) {
            throw new CancelException();
        }

        if (dataSize != sampleSize) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s!", sampleSize, dataSize));
        }

        String[] inputData = data.split(delimiter);

        return inputData ;
    }

    public static DataSet splitByTwo(String input, String delimiter) {
        int dataSize = count(input, delimiter);

        if (input.equals("cancel")) {
            throw new CancelException();
        }

        if (dataSize % 2 != 0) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected even number of parameters (2, 4 and so on) and you have entered %s!", dataSize));
        }

        String[] splitInput = input.split(delimiter);
        DataSet splitDataSet = new DataSetImpl();

        for (int i = 0; i < dataSize; i++) {
            String columnName = splitInput[i];
            i++;
            Object value = defineType(splitInput[i]);
            splitDataSet.put(columnName, value);
        }

        return splitDataSet;
    }
}
