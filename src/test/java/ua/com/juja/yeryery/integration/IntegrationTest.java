package ua.com.juja.yeryery.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.yeryery.Main;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.model.JdbcManager;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;
    private static String database = "testbase";
    //put here your own username and password
    private static String username = "postgres";
    private static String password = "postgrespass";

    @BeforeClass
    public static void createTestDatabase() {
        DatabaseManager testManager = new JdbcManager();
        Preparator preparator = new Preparator(database, username, password);
        preparator.setupDatabase(testManager);
    }

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
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
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
    public void testContentWithoutConnect() {
        //given
        in.add("content");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //content
                "Error! You can`t use 'content' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testClearWithoutConnect() {
        //given
        in.add("clear");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //clear
                "Error! You can`t use 'clear' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithoutConnect() {
        //given
        in.add("create");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //create
                "Error! You can`t use 'create' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWithoutConnect() {
        //given
        in.add("update");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //create
                "Error! You can`t use 'update' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayWithoutConnect() {
        //given
        in.add("display");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //display
                "Error! You can`t use 'display' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropWithoutConnect() {
        //given
        in.add("drop");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //drop
                "Error! You can`t use 'drop' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertWithoutConnect() {
        //given
        in.add("insert");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //insert
                "Error! You can`t use 'insert' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDeleteWithoutConnect() {
        //given
        in.add("delete");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //delete
                "Error! You can`t use 'delete' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUnknownCommandBeforeConnect() {
        //given
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //someCommand
                "Unknown command: someCommand!\n" +
                "Try again.\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectWithWrongNumberOfParameters() {
        //given
        in.add(String.format("connect|%s", database));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect|testbase
                "Error! Wrong number of parameters. Expected 4 parameters, and you have entered 2\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectWithWrongPassword() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, "wrongPass"));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                "Error! org.postgresql.util.PSQLException: FATAL: password authentication failed for user \"postgres\"\n" +
                "Try again.\n" +
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectWithWrongUsername() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, "wrongName", password));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                "Error! org.postgresql.util.PSQLException: FATAL: password authentication failed for user \"wrongName\"\n" +
                "Try again.\n" +
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectToWrongDatabase() {
        //given
        in.add(String.format("connect|%s|%s|%s", "wrongBase", username, password));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                "Error! org.postgresql.util.PSQLException: FATAL: database \"wrongBase\" does not exist\n" +
                "Try again.\n" +
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUnknownCommandAfterConnect() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //someCommand
                "Unknown command: someCommand!\n" +
                "Try again.\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testContentAfterConnect() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select table number 1
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayAndSelectTableName() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select test
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayChooseWrongNumber() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //-1
                "Error! There is no table with number -1\nTry again\n" +
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAndCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithExistingName() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //test
                "Error! Table with name 'test' already exists\n" +
                "[test, ttable]\n" +
                "Try again\n" +
                "Enter the name of your table or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAndCancelAfterNaming() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //somename
                "Enter the columnNames and its datatypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back\n" +
                //0
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWhenNameIsNumeric() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //1name
                "Error! You have entered '1name' and name must begin with a letter\n" +
                "Try again\n" +
                "Enter the name of your table or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateWithSqlErrorWhenDataTypeDoesntExist() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("create");
        in.add("somename");
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //somename
                "Enter the columnNames and its datatypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back\n" +
                //name|wrongType
                "ERROR: type \"wrongtype\" does not exist\n" +
                "  Position: 57\n" +
                "Try again.\n" +
                "Enter the name of your table or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testCreateAfterConnectAndDrop() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("create");
        in.add("somename");
        in.add("name|text|age|int");
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
                "Enter the name of your table or type 'cancel' to go back\n" +
                //somename
                "Enter the columnNames and its datatypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back\n" +
                //name|text|age|int
                "Your table 'somename' have successfully created!\n" +
                "Type command or 'help'\n" +
                //display
                "Select the table you need for 'display' command\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. ttable\n" +
                "0. cancel (to go back)\n" +
                //somename
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //drop
                "Select the table you need for 'drop' command\n" +
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
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertSQLExceptionAndExit() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("insert");
        in.add("ttable");
        in.add("id|notNumber|name|Mike|age|25");
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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|notNumber|name|Mike|age|25
                "ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 41\n" +
                "Try again.\n" +
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertSQLExceptionTryAgainAndDelete() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("insert");
        in.add("ttable");
        in.add("id|notNumber|name|Mike|age|25");
        in.add("ttable");
        in.add("id|10|name|Mike|age|25");
        in.add("delete");
        in.add("ttable");
        in.add("id|10");
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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|notNumber|name|Mike|age|25
                "ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 41\n" +
                "Try again.\n" +
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|10|name|Mike|age|25
                "You have successfully entered new data into the table 'ttable'\n" +
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|10
                "You have successfully deleted data from 'ttable'\n" +
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "|10|Mike|25 |\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertDataClearAndConfirm() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("insert");
        in.add("ttable");
        in.add("id|10|name|Mike|age|25");
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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|10|name|Mike|age|25
                "You have successfully entered new data into the table 'ttable'\n" +
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //display
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "|10|Mike|25 |\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //clear
                "Select the table you need for 'clear' command\n" +
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
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'drop' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropAndSelectNull() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'drop' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropSelectTableAndNotConfirm() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("drop");
        in.add("ttable");
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
                //drop
                "Select the table you need for 'drop' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Are you sure you want to drop table 'ttable'? (y/n)\n" +
                //n
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testClearSelectTableNotConfirm() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("clear");
        in.add("ttable");
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
                "Select the table you need for 'clear' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Are you sure you want to clear table 'ttable'? (y/n)\n" +
                //n
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndSelectCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateStringValue() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //password|newPass
                "You have successfully updated table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //update
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //password|pass2
                "You have successfully updated table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|newPass |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateIntValue() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //login|username2
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|30
                "You have successfully updated table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //update
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //login|username2
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "You have successfully updated table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|30|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateSelectTableAndCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputThreeParameters() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22|something
                "Error! Wrong number of parameters. Expected 2 parameters, and you have entered 3\n" +
                "Try again\n" +
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputNotExistingColumn() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //notExistingColumn|22
                "Error! Table 'test' doesn't contain column 'notExistingColumn'\n" +
                "Try again\n" +
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateAndInputNotExistingValue() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|notExistingValue
                "Error! Column 'id' doesn't contain value 'notExistingValue'\n" +
                "Try again\n" +
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateDefineUpdatedRowAndCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateDefineUpdatedRowAndInputThreeParameters() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //password|pass2|pass3
                "Error! Wrong number of parameters. Expected even number of parameters (2, 4 and so on), and you have entered 3\n" +
                "Try again\n" +
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateInputNotExistingUpdatedColumn() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //id|22
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //notExistingColumn|pass2
                "Error! Table 'test' doesn't contain column 'notExistingColumn'\n" +
                "Try again\n" +
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertUpdateAndDeleteTwoRows() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("insert");
        in.add("test");
        in.add("id|30|login|username3|password|pass3");
        in.add("insert");
        in.add("test");
        in.add("id|31|login|username4|password|pass3");

        in.add("update");
        in.add("test");
        in.add("password|pass3");
        in.add("login|username10");

        in.add("delete");
        in.add("test");
        in.add("password|pass3");
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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|30|login|username3|password|pass3
                "You have successfully entered new data into the table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //insert
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|31|login|username4|password|pass3
                "You have successfully entered new data into the table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "|30|username3|pass3   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //update
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //password|pass3
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //login|username10
                "You have successfully updated table 'test'\n" +
                "+--+---------+--------+\n" +
                "|id|login    |password|\n" +
                "+--+---------+--------+\n" +
                "|12|username1|pass1   |\n" +
                "+--+---------+--------+\n" +
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "|30|username3|pass3   |\n" +
                "+--+---------+--------+\n" +
                "|31|username4|pass3   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //password|pass3
                "You have successfully deleted data from 'test'\n" +
                "+--+----------+--------+\n" +
                "|id|login     |password|\n" +
                "+--+----------+--------+\n" +
                "|12|username1 |pass1   |\n" +
                "+--+----------+--------+\n" +
                "|22|username2 |pass2   |\n" +
                "+--+----------+--------+\n" +
                "|30|username10|pass3   |\n" +
                "+--+----------+--------+\n" +
                "|31|username10|pass3   |\n" +
                "+--+----------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWithSQLException() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //login|username2
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|notNumber
                "ERROR: column \"id\" is of type integer but expression is of type character varying\n" +
                "  Hint: You will need to rewrite or cast the expression.\n" +
                "  Position: 20\n" +
                "Try again.\n" +
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateWithSQLExceptionDuplicateKey() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
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
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to update: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //login|username2
                "Enter the columnNames and its values of the row you want to update:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|12
                "ERROR: duplicate key value violates unique constraint \"test_pkey\"\n" +
                "  Detail: Key (id)=(12) already exists.\n" +
                "Try again.\n" +
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertAndDelete() {
        //given
        in.add(String.format("connect|%s|%s|%s", database, username, password));
        in.add("insert");
        in.add("ttable");
        in.add("id|1|name|Jack|age|20");

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
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //id|1|name|Jack|age|20
                "You have successfully entered new data into the table 'ttable'\n" +
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //name|Jack
                "You have successfully deleted data from 'ttable'\n" +
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "|1 |Jack|20 |\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }
}
