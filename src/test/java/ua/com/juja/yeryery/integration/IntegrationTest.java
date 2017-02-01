package ua.com.juja.yeryery.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.yeryery.Main;

import java.io.PrintStream;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private static final String DATABASE = Preparator.DATABASE;
    private static final String USERNAME = Preparator.USERNAME;
    private static final String PASSWORD = Preparator.PASSWORD;

    private ConfigurableInputStream in;
    private LogOutputStream out;

    @BeforeClass
    public static void setupTestDB() {
        Preparator.setupDB();
    }

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterClass
    public static void clearAfterTests() {
        Preparator.deleteDB();
    }
    //TODO убрать перед отправлением на тестирование

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
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //help
                "List of commands:\n" +
                "\tconnect|database|username|password\n" +
                "\t\tConnect to Database\n" +
                "\ttables\n" +
                "\t\tDisplay a list of available tables\n" +
                "\tcreate\n" +
                "\t\tCreate new table\n" +
                "\tdisplay\n" +
                "\t\tDisplay records of the table\n" +
                "\tinsert\n" +
                "\t\tInsert new record in the table\n" +
                "\tdelete\n" +
                "\t\tFind required record and remove it from the table\n" +
                "\tupdate\n" +
                "\t\tFind required record and update it in the table\n" +
                "\tclear\n" +
                "\t\tClear the table after confirmation\n" +
                "\tdrop\n" +
                "\t\tDelete the table after confirmation\n" +
                "\texit\n" +
                "\t\tProgram exit\n" +
                "\thelp\n" +
                "\t\tPrint all available commands\n" +
                "Type command or 'help'\n" +
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
                "Error! You can`t use 'someCommand' unless you are not connected\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectWithWrongPassword() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, "wrongPass"));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect|testBase|postgres|wrongPass
                "Error! FATAL: password authentication failed for user \"postgres\"\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectWithWrongUsername() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, "wrongName", PASSWORD));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect|testBase|wrongName|postgrespass
                "Error! FATAL: password authentication failed for user \"wrongName\"\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testConnectToNotExistsDatabase() {
        //given
        in.add(String.format("connect|%s|%s|%s", "notExistsDB", USERNAME, PASSWORD));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect|notExistsDB|postgres|postgrespass
                "Error! FATAL: database \"notExistsDB\" does not exist\n" +
                "Try again\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUnknownCommandAfterConnect() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "Error! Unknown command: someCommand\n" +
                "Try again\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testTablesAfterConnect() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //tables
                "[test, users]\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDisplayAndSelectTableName() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
    public void testCreateWithSqlErrorWhenDataTypeDoesntExist() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("create");
        in.add("someName");
        in.add("id|int");
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
                //someName
                "Enter the name of PRIMARY KEY column and its dataType: columnName|dataType\n" +
                "or type 'cancel' to go back\n" +
                //id|int
                "Enter the columnNames and its dataTypes of the table you want to create:\n" +
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
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("create");
        in.add("someName");
        in.add("id|int");
        in.add("name|text|age|int");
        in.add("display");
        in.add("someName");
        in.add("drop");
        in.add("someName");
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
                //someName
                "Enter the name of PRIMARY KEY column and its dataType: columnName|dataType\n" +
                "or type 'cancel' to go back\n" +
                //id|int
                "Enter the columnNames and its dataTypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back\n" +
                //name|text|age|int
                "Your table 'somename' have successfully created!\n" +
                "Type command or 'help'\n" +
                //display
                "Select the table you need for 'display' command\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. users\n" +
                "0. cancel (to go back)\n" +
                //someName
                "+--+----+---+\n" +
                "|id|name|age|\n" +
                "+--+----+---+\n" +
                "Type command or 'help'\n" +
                //drop
                "Select the table you need for 'drop' command\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. users\n" +
                "0. cancel (to go back)\n" +
                //someName
                "Are you sure you want to drop table 'somename'? (y/n)\n" +
                //y
                "Table 'somename' successfully dropped!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertSQLExceptionAndExit() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("insert");
        in.add("users");
        in.add("code|notNumber|name|Mike|age|25");
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
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //code|notNumber|name|Mike|age|25
                "ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 42\n" +
                "Try again.\n" +
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. users\n" +
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
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("insert");
        in.add("users");
        in.add("code|notNumber|name|Mike|age|25");
        in.add("users");
        in.add("code|10|name|Mike|age|25");
        in.add("delete");
        in.add("users");
        in.add("code|10");
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
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //code|notNumber|name|Mike|age|25
                "ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 42\n" +
                "Try again.\n" +
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //code|10|name|Mike|age|25
                "You have successfully entered new data into the table 'users'\n" +
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "|10  |Mike|25 |\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //code|10
                "You have successfully deleted data from 'users'\n" +
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testInsertDataClearAndConfirm() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("insert");
        in.add("users");
        in.add("code|10|name|Mike|age|25");
        in.add("display");
        in.add("users");
        in.add("clear");
        in.add("users");
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
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //code|10|name|Mike|age|25
                "You have successfully entered new data into the table 'users'\n" +
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "|10  |Mike|25 |\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //display
                "Select the table you need for 'display' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "|10  |Mike|25 |\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //clear
                "Select the table you need for 'clear' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Are you sure you want to clear table 'users'? (y/n)\n" +
                //y
                "Table 'users' successfully cleared!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testDropSelectTableAndNotConfirm() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("drop");
        in.add("users");
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
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Are you sure you want to drop table 'users'? (y/n)\n" +
                //n
                "Command execution is canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateIntValueAndUpdateAgain() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
                "|30|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //update
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. users\n" +
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
                "|22|username2|pass2   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }

    @Test
    public void testUpdateSelectTableAndCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
    public void testUpdateDefineUpdatedRowAndCancel() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
    public void testUpdateInputNotExistingUpdatedColumn() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
                "|30|username3|pass3   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //insert
                "Select the table you need for 'insert' command\n" +
                "1. test\n" +
                "2. users\n" +
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
                "|31|username4|pass3   |\n" +
                "+--+---------+--------+\n" +
                "Type command or 'help'\n" +
                //update
                "Select the table you need for 'update' command\n" +
                "1. test\n" +
                "2. users\n" +
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
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //password|pass3
                "You have successfully deleted data from 'test'\n" +
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
    public void testUpdateWithSQLExceptionTryToUpdateIntegerValueWithString() {
        //given
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
                "2. users\n" +
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
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
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
                "2. users\n" +
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
                "2. users\n" +
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
        in.add(String.format("connect|%s|%s|%s", DATABASE, USERNAME, PASSWORD));
        in.add("insert");
        in.add("users");
        in.add("code|1|name|Jack|age|20");

        in.add("delete");
        in.add("users");
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
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back\n" +
                //code|1|name|Jack|age|20
                "You have successfully entered new data into the table 'users'\n" +
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "|1   |Jack|20 |\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //delete
                "Select the table you need for 'delete' command\n" +
                "1. test\n" +
                "2. users\n" +
                "0. cancel (to go back)\n" +
                //users
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back\n" +
                //name|Jack
                "You have successfully deleted data from 'users'\n" +
                "+----+----+---+\n" +
                "|code|name|age|\n" +
                "+----+----+---+\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r", ""));
    }
}
