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
    public void testExit() {
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
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //help
                "Content of commands:\r\n" +
                "\tconnect|database|username|password\r\n" +
                "\t\tConnect to Database\r\n" +
                "\tcontent\r\n" +
                "\t\tContent of tables\r\n" +
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
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void testListWithoutConnect() {
        //given
        in.add("content");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //content
                "You can`t use 'content' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void testClearWithoutConnect() {
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
    public void testCreateWithoutConnect() {
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
    public void testDisplayWithoutConnect() {
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
    public void testDropWithoutConnect() {
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
    public void testInsertWithoutConnect() {
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
    public void testUnknownCommandBeforeConnect() {
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
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void testConnectWithWrongNumberOfParameters() {
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
    public void testConnectWithWrongParameter() {
        //given
        in.add("connect|yeryery|postgres|wrongpass");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Error! org.postgresql.util.PSQLException: FATAL: password authentication failed for user \"postgres\"\r\n" +
                "Try again.\r\n" +
                "See you!\r\n", out.getData());
    }

    @Test
    public void testUnknownCommandAfterConnect() {
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
                "Type command or 'help'\r\n" +
                //someCommand
                "Unknown command: someCommand!\r\n" +
                "Try again.\r\n" +
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("content");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //content
                "[test, ttable]\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayAndSelectNumberOfTable() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("1");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select table number 1
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayAndSelectTablename() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("test");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select test
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayChooseWrongNumber() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("-1");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //-1
                "There is no table with this number! Try again.\n" +
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Table displaying canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //cancel
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithExistingName() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("test");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //test
                "Table with name 'test' already exists!\n" +
                "[test, ttable]\n" +
                "Try again.\n" +
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //cancel
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAndCancelAfterNaming() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //0
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithNegativeTableSize() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("-1");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //-1
                "Number must be positive!\n" +
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //0
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWhenNameIsNumeric() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("1name");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //1name
                "Table name must begin with a letter! Try again.\n" +
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //cancel
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithSqlErrorWhenDataTypeDoesntExist() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("1");
        in.add("name");
        in.add("wrongType");

        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //1
                "name of column 1:\n" +
                //name
                "type of column 1:\n" +
                //wrongType
                "SQL ERROR: type \"wrongtype\" does not exist!\n" +
                "Try again.\n" +
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //cancel
                "Table creating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAfterConnectAndDrop() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("2");
        in.add("name");
        in.add("text");
        in.add("age");
        in.add("int");
        in.add("display");
        in.add("somename");
        in.add("drop");
        in.add("somename");
        in.add("y");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or 'cancel' to go back\n" +
                //2
                "name of column 1:\n" +
                //name
                "type of column 1:\n" +
                //text
                "name of column 2:\n" +
                //age
                "type of column 2:\n" +
                //int
                "Your table 'somename' have successfully created!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. ttable\n" +
                "0. cancel (to go back)\n" +
                //somename
                "| id | name | age | \n" +
                "-------------------------\n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you want to drop\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. ttable\n" +
                "0. cancel (to go back)\n" +
                //somename
                "Are you sure you want to drop table 'somename'? (y/n)\n" +
                //y
                "Table 'somename' successfully dropped!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you want to insert\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Table inserting canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertWrongSql() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("ttable");
        in.add("notnumber");
        in.add("somename");
        in.add("25");

        in.add("1");
        in.add("somename");
        in.add("25");
        in.add("clear");
        in.add("ttable");
        in.add("y");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you want to insert\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter new values you require\n" +
                //notnumber
                "id\n" +
                //somename
                "name\n" +
                //25
                "age\n" +
                "SQL ERROR: invalid input syntax for integer: \"notnumber\"!\n" +
                "Try again.\n" +
                "Enter new values you require\n" +
                "id\n" +
                "name\n" +
                "age\n" +
                "You have successfully entered new data into the table 'ttable'\n" +
                "Type command or 'help'\n" +
                "Please enter the name or select number of table you want to clear\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                "Are you sure you want to clear table 'ttable'? (y/n)\n" +
                "Table 'ttable' successfully cleared!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertAndClear() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("ttable");
        in.add("10");
        in.add("somename");
        in.add("25");
        in.add("display");
        in.add("ttable");
        in.add("clear");
        in.add("ttable");
        in.add("y");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you want to insert\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter new values you require\n" +
                //10
                "id\n" +
                //somename
                "name\n" +
                //25
                "age\n" +
                "You have successfully entered new data into the table 'ttable'\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "| id | name | age | \n" +
                "-------------------------\n" +
                "| 10 | somename | 25 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //clear
                "Please enter the name or select number of table you want to clear\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Are you sure you want to clear table 'ttable'? (y/n)\n" +
                //y
                "Table 'ttable' successfully cleared!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("drop");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you want to drop\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Table dropping canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropAndSelectNull() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("drop");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you want to drop\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Table dropping canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndSelectNull() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWrongSql() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("notNumber|password|newPass");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter id you want to update, columnName and its new values: id|columnName1|newValue1|columnName2|newValue2...\n" +
                "or type 'cancel' to go back.\n" +
                "Error! For input string: \"notNumber\"\n" +
                "Try again.\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWrongNumberOfArguments() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("22|password|newPass|smth");
        in.add("22|password|pass2");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter id you want to update, columnName and its new values: id|columnName1|newValue1|columnName2|newValue2...\n" +
                "or type 'cancel' to go back.\n" +
                //22 password newPass smth
                "You should enter an odd number of parameters (3 or more)!\n" +
                "Try again.\n" +
                "Enter id you want to update, columnName and its new values: id|columnName1|newValue1|columnName2|newValue2...\n" +
                "or type 'cancel' to go back.\n" +
                //22 password pass2
                "You have successfully updated table 'test' at id = 22\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdate() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("22|password|newPass");
        in.add("display");
        in.add("test");
        in.add("update");
        in.add("test");
        in.add("22|password|pass2");
        in.add("display");
        in.add("test");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter id you want to update, columnName and its new values: id|columnName1|newValue1|columnName2|newValue2...\n" +
                "or type 'cancel' to go back.\n" +
                //22 password newPass
                "You have successfully updated table 'test' at id = 22\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | newPass | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter id you want to update, columnName and its new values: id|columnName1|newValue1|columnName2|newValue2...\n" +
                "or type 'cancel' to go back.\n" +
                //22 password pass2
                "You have successfully updated table 'test' at id = 22\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you want to display\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testClearSelectNotConfirmSelectAgainAndConfirm() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("clear");
        in.add("2");
        in.add("n");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //clear
                "Please enter the name or select number of table you want to clear\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //2
                "Are you sure you want to clear table 'ttable'? (y/n)\n" +
                //n
                "The clearing of table 'ttable' is cancelled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }
}
