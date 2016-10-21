package ua.com.juja.yeryery.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yeryery.Main;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void TestExit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //help
                "List of commands:\r\n" +
                "\tconnect|database|username|password\r\n" +
                "\t\tConnect to Database\r\n" +
                "\tlist\r\n" +
                "\t\tList of tables\r\n" +
                "\tcreate\r\n" +
                "\t\tCreate new table\r\n" +
                "\tdisplay\r\n" +
                "\t\tDisplay require table\r\n" +
                "\tinsert\r\n" +
                "\t\tInsert new data in require table\r\n" +
                "\tclear\r\n" +
                "\t\tClear require table\r\n" +
                "\tdrop\r\n" +
                "\t\tDrop require table\r\n" +
                "\texit\r\n" +
                "\t\tProgram exit\r\n" +
                "\thelp\r\n" +
                "\t\tAll commands\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //list
                "You can`t use 'list' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestClearWithoutConnect() {
        //given
        in.add("clear");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //clear
                "You can`t use 'clear' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestCreateWithoutConnect() {
        //given
        in.add("create");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //create
                "You can`t use 'create' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestDisplayWithoutConnect() {
        //given
        in.add("display");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //display
                "You can`t use 'display' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestDropWithoutConnect() {
        //given
        in.add("drop");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //drop
                "You can`t use 'drop' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestInsertWithoutConnect() {
        //given
        in.add("insert");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //insert
                "You can`t use 'insert' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandBeforeConnect() {
        //given
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //someCommand
                "Unknown command: someCommand!\r\n" +
                "Try again.\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongNumberOfParameters() {
        //given
        in.add("connect|yeryery");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //connect|yeryery
                "Error! Wrong number of parameters. Expected 4, and you have entered 2\r\n" +
                "Try again.\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongParameter() {
        //given
        in.add("connect|yeryery|postgres|wrongpass");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Error! Can`t get connection! You have entered incorrect data.\r\n" +
                "Try again.\r\n" +
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //connect
                "Success!\r\n" +
                //someCommand
                "Unknown command: someCommand!\r\n" +
                "Try again.\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestListAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                //list
                "[ttable, test]\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("2");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                //display
                "Please select number of table you need or '0' to exit\n" +
                "1. ttable\n" +
                "2. test\n" +
                "0. Exit\n" +
                //select number
                "| id | name | age | \n" +
                "-----------------------------\n" +
                "| 1 | Jack | 24 | \n" +
                "| 2 | Michael | 29 | \n" +
                "-------------------------\n" +
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayChooseWrongNumber() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("-1");
        in.add("0");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                //display
                "Please select number of table you need or '0' to exit\n" +
                "1. ttable\n" +
                "2. test\n" +
                "0. Exit\n" +
                //-1
                "There is no table with this number! Try again.\n" +
                "Please select number of table you need or '0' to exit\n" +
                "1. ttable\n" +
                "2. test\n" +
                "0. Exit\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayChooseNotNumber() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("notNumber");
        in.add("0");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                //display
                "Please select number of table you need or '0' to exit\n" +
                "1. ttable\n" +
                "2. test\n" +
                "0. Exit\n" +
                //notNumber
                "This is not a number! Try again.\n" +
                "Please select number of table you need or '0' to exit\n" +
                "1. ttable\n" +
                "2. test\n" +
                "0. Exit\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }
}
