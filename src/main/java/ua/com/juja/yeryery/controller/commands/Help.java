package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.view.View;

public class Help implements Command {

    private View view;
    private static final String ACTION = "help";

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        view.write("Content of commands:");

        view.write("\tconnect|database|username|password");
        view.write("\t\tConnect to Database");

        view.write("\ttables");
        view.write("\t\tDisplay a list of available tables");

        view.write("\tcreate");
        view.write("\t\tCreate new table");

        view.write("\tdisplay");
        view.write("\t\tDisplay records of the table");

        view.write("\tinsert");
        view.write("\t\tInsert new record in the table");

        view.write("\tdelete");
        view.write("\t\tFind required record and remove it from the table");

        view.write("\tupdate");
        view.write("\t\tFind required record and update it in the table");

        view.write("\tclear");
        view.write("\t\tClear the table after confirmation");

        view.write("\tdrop");
        view.write("\t\tDelete the table after confirmation");

        view.write("\texit");
        view.write("\t\tProgram exit");

        view.write("\thelp");
        view.write("\t\tPrint all available commands");
    }

}
