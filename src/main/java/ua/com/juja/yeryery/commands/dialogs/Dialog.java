package ua.com.juja.yeryery.commands.dialogs;

import java.util.Set;

public interface Dialog {
    String SelectTable(Set<String> names, String message);

    String NameTable(Set<String> names, String message);

    boolean isConfirmed(String warning);
}
