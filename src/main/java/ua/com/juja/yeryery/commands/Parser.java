package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;

public class Parser {

    public static int parsedInt;
    private static String delimiter = "\\|";

    public static boolean isParsable(String data) {
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

    private static int count(String data) {
        String[] splitData = data.split(delimiter);
        return splitData.length;
    }

    public static String[] splitBySample(String input, String sample) {
        String[] splitData = splitInput(input);
        assertSize(input, sample);

        return splitData ;
    }

    private static void assertSize(String input, String sample) {
        int sampleSize = count(sample);
        int dataSize = count(input);

        if (dataSize != sampleSize) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s", sampleSize, dataSize));
        }
    }

    public static DataSet splitByPairs(String input) {
        //TODO refactor
        String[] splitData = splitInput(input);
        int dataSize = splitData.length;

        if (dataSize % 2 != 0) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected even number of parameters (2, 4 and so on) and you have entered %s", dataSize));
        }

        DataSet splitDataSet = new DataSetImpl();

        for (int i = 0; i < dataSize; i++) {
            String columnName = splitData[i];
            i++;
            Object value = defineType(splitData[i]);
            splitDataSet.put(columnName, value);
        }

        return splitDataSet;
    }

    public static String[] splitInput(String input) {
        if (input.equals("cancel")) {
            throw new CancelException();
        }
        return input.split(delimiter);
    }
}
