package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public interface Dialog {
    String askUser(DatabaseManager manager, View view);
}
