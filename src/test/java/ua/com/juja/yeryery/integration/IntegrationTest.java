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
                "\tdelete\r\n" +
                "\t\tDelete data from require table\r\n" +
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
    public void testUpdateWithoutConnect() {
        //given
        in.add("update");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //create
                "You can`t use 'update' unless you are not connected.\r\n" +
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
    public void testDeleteWithoutConnect() {
        //given
        in.add("delete");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //delete
                "You can`t use 'delete' unless you are not connected.\r\n" +
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
        in.add("name|wrongType");
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
                "Please enter name|type of column 1:\n" +
                //name|wrongType
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
        in.add("name|text");
        in.add("age|int");
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
                "Please enter name|type of column 1:\n" +
                //name|text
                "Please enter name|type of column 2:\n" +
                //age|int
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
                "Table clearing canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndSelectCancel() {
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
    public void testUpdateStringValue() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|22");
        in.add("password|newPass");

        in.add("update");
        in.add("test");
        in.add("id|22");
        in.add("password|pass2");
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
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //password|newPass
                "You have successfully updated table 'test' at id = 22\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //password|pass2
                "You have successfully updated table 'test' at id = 22\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | newPass | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateIntValue() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("id|30");

        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("id|22");
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
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //id|30
                "You have successfully updated table 'test' at login = username2\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "You have successfully updated table 'test' at login = username2\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 30 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateSelectTableAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
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
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputThreeParameters() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|22|something");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22|something
                "You should enter two parameters!\n" +
                "Try again.\n" +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputNotExistingColumn() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("notExistingColumn|22");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //notExistingColumn|22
                "Table 'test' doesn't contain column 'notExistingColumn'!\n" +
                "Try again.\n" +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputNotExistingValue() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|notExistingValue");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|notExistingValue
                "Column 'id' doesn't contain value 'notExistingValue'!\n" +
                "Try again.\n" +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateDefineUpdatedRowAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|22");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateDefineUpdatedRowAndInputThreeParameters() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|22");
        in.add("password|pass2|pass3");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //password|pass2|pass3
                "You should enter an even number of parameters (2, 4 and so on): \n" +
                "Try again.\n" +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateInputNotExistingUpdatedColumn() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("id|22");
        in.add("notExistingColumn|pass2");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //id|22
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //notExistingColumn|pass2
                "Table 'test' doesn't contain column 'notExistingColumn'!\n" +
                "Try again.\n" +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateTwoRows() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("test");
        in.add("30"); in.add("username2"); in.add("pass2");

        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("password|pass5");

        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("password|pass2");

        in.add("delete");
        in.add("test");
        in.add("id|30");
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
                //test
                "Enter new values you require\n" +
                "id\n" +
                //30
                "login\n" +
                //username2
                "password\n" +
                //pass2
                "You have successfully entered new data into the table 'test'\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //password|pass5
                "You have successfully updated table 'test' at login = username2\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "| 30 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //update
                "Please enter the name or select number of table you want to update\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //password|pass2
                "You have successfully updated table 'test' at login = username2\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass5 | \n" +
                "| 30 | username2 | pass5 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //delete
                "Please enter the name or select number of table you want to delete\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //text
                "Enter columnName and value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back.\n" +
                //id|30
                "You have successfully deleted data from 'test'\n" +
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "| 30 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWithSQLException() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("id|notNumber");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //id|notNumber
                "SQL ERROR: column \"id\" is of type integer but expression is of type character varying!\n" +
                "Try again.\n" +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWithSQLExceptionDuplicateKey() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("update");
        in.add("test");
        in.add("login|username2");
        in.add("id|12");
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
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //login|username2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.\n" +
                //id|12
                "SQL ERROR: duplicate key value violates unique constraint \"test_pkey\"!\n" +
                "Try again.\n" +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back.\n" +
                //cancel
                "Table updating canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDelete() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("ttable");
        in.add("1"); in.add("Jack"); in.add("20");

        in.add("delete");
        in.add("ttable");
        in.add("name|Jack");
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
                "id\n" +
                //1
                "name\n" +
                //Jack
                "age\n" +
                //30
                "You have successfully entered new data into the table 'ttable'\n" +
                "Type command or 'help'\n" +
                //delete
                "Please enter the name or select number of table you want to delete\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter columnName and value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back.\n" +
                //name|Jack
                "You have successfully deleted data from 'ttable'\n" +
                "| id | name | age | \n" +
                "-------------------------\n" +
                "| 1 | Jack | 20 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }
}
