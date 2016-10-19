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
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestListWithoutConnect() {
        //given
        in.add("list");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'list' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestClearWithoutConnect() {
        //given
        in.add("clear");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'clear' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestCreateWithoutConnect() {
        //given
        in.add("create");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'create' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestDisplayWithoutConnect() {
        //given
        in.add("display");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'display' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestDropWithoutConnect() {
        //given
        in.add("drop");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'drop' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestInsertWithoutConnect() {
        //given
        in.add("insert");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "You can`t use 'insert' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandBeforeConnect() {
        //given
        in.add("somecomand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Unknown command: somecomand!\r\n" +
                "Try again.\r\n" +
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongNumberOfParameters() {
        //given
        in.add("connect|yeryery");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Error! Wrong number of parameters. Expected 4, and you have entered 2\r\n" +
                "Try again\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongParameter() {
        //given
        in.add("connect|yeryery|postgres|wrongpass");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Error! Can`t get connection! You have entered incorrect data.\r\n" +
                "Try again\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("somecomand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Success!\r\n" +
                "Unknown command: somecomand!\r\n" +
                "Try again.\r\n" +
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
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Success!\r\n" +
                "[test, ttable]\r\n" +
                "See you!\r\n", out.getData());
    }
}
