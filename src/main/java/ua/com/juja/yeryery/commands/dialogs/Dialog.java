package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public interface Dialog {
    String askUser(Set<String> names, View view, String act);

    boolean isConfirmed(String name, View view, String act);
}
