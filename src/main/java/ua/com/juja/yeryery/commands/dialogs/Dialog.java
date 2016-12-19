package ua.com.juja.yeryery.commands.dialogs;

public interface Dialog {
    String SelectTable(String message);

    String NameTable(String message);

    boolean isConfirmed(String warning);

    String[] findRow(String tableName, String action, String sample);
}
