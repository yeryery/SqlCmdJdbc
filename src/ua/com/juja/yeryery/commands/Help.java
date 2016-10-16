package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("help");
    }

    @Override
    public void process(String input) {
        view.write("List of commands:");

        view.write("\tconnect|database|username|password");
        view.write("\t\tConnect to Database");

        view.write("\tlist");
        view.write("\t\tList of tables");

        view.write("\tclear tableName");
        view.write("\t\tClear table 'tableName'");

        view.write("\texit");
        view.write("\t\tProgram exit");

        view.write("\thelp");
        view.write("\t\tAll commands");
    }

}
