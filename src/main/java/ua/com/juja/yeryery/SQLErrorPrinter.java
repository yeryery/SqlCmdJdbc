package ua.com.juja.yeryery;

import java.sql.SQLException;

public class SQLErrorPrinter {

    private SQLException e;

    public SQLErrorPrinter(SQLException e) {
        this.e = e;
    }

    public void printSQLError() {
        String result = "SQL " + e.getMessage();

        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '\n') {
                result = result.substring(0, i);
                break;
            }
        }
        System.out.println(result + "!");
        System.out.println("Try again.");
    }
}
