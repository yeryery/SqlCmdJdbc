package ua.com.juja.yeryery.controller.commands.Utilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.controller.commands.TestTable;
import ua.com.juja.yeryery.controller.commands.Utility.CancelException;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.model.DataEntry;
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class DialogTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private Set<String> tableNames;

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = new Dialog(view, manager);
        tableNames = new LinkedHashSet<>(Arrays.asList("test", "users"));
    }

    @Test
    public void testSelectTableAndInputTableName() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test");

        //when
        String tableName = dialog.selectTable("action");

        //then
        assertEquals("test", tableName);
    }

    @Test
    public void testSelectTableAndInputNumberOfTable() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1");

        //when
        String tableName = dialog.selectTable("action");

        //then
        assertEquals("test", tableName);
    }

    @Test
    public void testSelectTableAndInputCancel() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        try {
            dialog.selectTable("action");
            fail("Expected CancelException!");
            //then
        } catch (CancelException e) {
            assertEquals("Command execution is canceled", e.getMessage());
        }
    }

    @Test
    public void testSelectTableAndInputZero() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        try {
            dialog.selectTable("action");
            fail("Expected CancelException!");
            //then
        } catch (CancelException e) {
            assertEquals("Command execution is canceled", e.getMessage());
        }
    }

    @Test
    public void testSelectTableAndInputNotExistingNumberOfTable() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("5").thenReturn("cancel");

        //when
        try {
            dialog.selectTable("action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Select the table you need for 'action' command, " +
                "1. test, " +
                "2. users, " +
                "0. cancel (to go back), " +
                //5
                "Error! There is no table with number 5\n" +
                "Try again, " +
                "Select the table you need for 'action' command, " +
                "1. test, " +
                "2. users, " +
                "0. cancel (to go back)]");
    }

    @Test
    public void testSelectTableAndInputNotExistingTableName() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("notExistingTableName").thenReturn("cancel");

        //when
        try {
            dialog.selectTable("action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Select the table you need for 'action' command, " +
                "1. test, " +
                "2. users, " +
                "0. cancel (to go back), " +
                //notExistingTableName
                "Error! Table with name 'notexistingtablename' doesn't exist\n" +
                "Try again, " +
                "Select the table you need for 'action' command, " +
                "1. test, " +
                "2. users, " +
                "0. cancel (to go back)]");
    }

    @Test
    public void testNameTableAndInputTableName() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTableName");

        //when
        String tableName = dialog.nameTable();

        //then
        assertEquals("newtablename", tableName);
    }

    @Test
    public void testNameTableAndInputCancel() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        try {
            dialog.nameTable();
            fail("Expected CancelException!");
            //then
        } catch (CancelException e) {
            assertEquals("Command execution is canceled", e.getMessage());
        }

    }

    @Test
    public void testNameTableAndInputNameThatStartsWithNotLetter() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1table").thenReturn("cancel");

        //when
        try {
            dialog.nameTable();
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //1table
                "Error! You have entered '1table' and name must begin with a letter\n" +
                "Try again, " +
                "Enter the name of your table or type 'cancel' to go back]");
    }

    @Test
    public void testNameTableAndInputNameThatAlreadyExists() {
        //given
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test").thenReturn("cancel");

        //when
        try {
            dialog.nameTable();
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //test
                "Error! Table with name 'test' already exists\n" +
                "[test, users]\n" +
                "Try again, " +
                "Enter the name of your table or type 'cancel' to go back]");
    }

    @Test
    public void testConfirmActionAndInputYes() {
        //given
        when(view.read()).thenReturn("y");

        //when
        try {
            dialog.confirmAction("action", "tableName");
        } catch (CancelException e) {
            fail("Unexpected CancelException!");
        }
    }

    @Test
    public void testConfirmActionAndInputNo() {
        //given
        when(view.read()).thenReturn("n");

        //when
        try {
            dialog.confirmAction("action", "tableName");
            fail("Unexpected CancelException!");
        } catch (CancelException e) {
            //do nothing
        }
    }

    @Test
    public void testConfirmActionAndInputNeitherYesOrNo() {
        //given
        when(view.read()).thenReturn("a").thenReturn("n");

        //when
        try {
            dialog.confirmAction("action", "tableName");
            fail("Expected CancelException!");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Are you sure you want to action table 'tableName'? (y/n), " +
                //a
                "Are you sure you want to action table 'tableName'? (y/n)]");
    }

    @Test
    public void testFindRowWithStringValue() {
        //given
        when(view.read()).thenReturn("name|Mike");
        when(manager.getTableColumns("test")).thenReturn(TestTable.getTableColumns());
        when(manager.getDataContent("test")).thenReturn(TestTable.getTableContent());

        //when
        DataEntry entry = dialog.findRow("test", "action");

        //then
        assertEquals("name", entry.getColumn());
        assertEquals("Mike", entry.getValue());
    }

    @Test
    public void testFindRowWithIntValue() {
        //given
        when(view.read()).thenReturn("id|1");
        when(manager.getTableColumns("test")).thenReturn(TestTable.getTableColumns());
        when(manager.getDataContent("test")).thenReturn(TestTable.getTableContent());

        //when
        DataEntry entry = dialog.findRow("test", "action");

        //then
        assertEquals("id", entry.getColumn());
        assertEquals(1, entry.getValue());
    }

    @Test
    public void testFindRowAndCancel() {
        //given
        when(view.read()).thenReturn("cancel");

        //when
        try {
            DataEntry entry = dialog.findRow("test", "action");
            fail("Expected CancelException!");
            //then
        } catch (CancelException e) {
            assertEquals("Command execution is canceled", e.getMessage());
        }
    }

    @Test
    public void testFindRowWithNotExistingColumn() {
        //given
        when(view.read()).thenReturn("login|Mike").thenReturn("cancel");
        when(manager.getTableColumns("test")).thenReturn(TestTable.getTableColumns());
        when(manager.getDataContent("test")).thenReturn(TestTable.getTableContent());

        //when
        try {
            DataEntry entry = dialog.findRow("test", "action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //login|Mike
                "Error! Table 'test' doesn't contain column 'login'\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back]");
    }

    @Test
    public void testFindRowWithNotExistingValue() {
        //given
        when(view.read()).thenReturn("name|Bob").thenReturn("cancel");
        when(manager.getTableColumns("test")).thenReturn(TestTable.getTableColumns());
        when(manager.getDataContent("test")).thenReturn(TestTable.getTableContent());

        //when
        try {
            DataEntry entry = dialog.findRow("test", "action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Bob
                "Error! Column 'name' doesn't contain value 'Bob'\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back]");
    }

    @Test
    public void testFindRowAndInputThreeArguments() {
        //given
        when(view.read()).thenReturn("argument1|argument2|argument3").thenReturn("cancel");

        //when
        try {
            DataEntry entry = dialog.findRow("test", "action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //argument1|argument2|argument3
                "Error! Wrong number of parameters. Expected 2 parameters, and you have entered 3\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to action: columnName|value\n" +
                "or type 'cancel' to go back]");
    }

    @Test
    public void testSplitConnectInput() {
        //given
        String input = "argument1|argument2|argument3|argument4";
        String sample = "connect|database|username|password";

        //when
        String[] actualSplitInput = dialog.splitConnectInput(input, sample);

        //then
        String[] expectedSplitInput = new String[]{"argument1", "argument2", "argument3", "argument4"};
        assertTrue(Arrays.equals(expectedSplitInput, actualSplitInput));
    }

    @Test
    public void testGetNewEntriesAndInputNewValues() {
        //given
        when(view.read()).thenReturn("id|1|name|Bob");
        when(manager.getTableColumns("test")).thenReturn(TestTable.getTableColumns());
        when(manager.getDataContent("test")).thenReturn(TestTable.getTableContent());

        //when
        DataSet actualDataSet = dialog.getNewEntries("test", "action");

        //then
        DataSet expectedDataSet = new DataSetImpl();
        expectedDataSet.put("id", 1);
        expectedDataSet.put("name", "Bob");

        assertEquals(expectedDataSet.getColumnNames(), actualDataSet.getColumnNames());
        assertEquals(expectedDataSet.getValues(), actualDataSet.getValues());
    }

    @Test
    public void testGetNewEntriesAndInputOddNumberOfArguments() {
        //given
        when(view.read()).thenReturn("argument1|argument2|argument3").thenReturn("cancel");

        //when
        try {
            dialog.getNewEntries("test", "action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the columnNames and its values of the row you want to action:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //argument1|argument2|argument3
                "Error! Wrong number of parameters. Expected even number of parameters (2, 4 and so on), and you have entered 3\n" +
                "Try again, Enter the columnNames and its values of the row you want to action:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back]");
    }

    @Test
    public void testGetNewColumns() {
        //given
        when(view.read()).thenReturn("column1|String|column2|int");

        //when
        DataSet actualDataSet = dialog.getNewColumns("action");

        //then
        DataSet expectedDataSet = new DataSetImpl();
        expectedDataSet.put("column1", "String");
        expectedDataSet.put("column2", "int");

        assertEquals(expectedDataSet.getColumnNames(), actualDataSet.getColumnNames());
        assertEquals(expectedDataSet.getValues(), actualDataSet.getValues());
    }

    @Test
    public void testGetNewColumnsInputIncorrectColumnNamesStartsWithNumber() {
        //given
        when(view.read()).thenReturn("9column1|String|column2|int").thenReturn("cancel");

        //when
        DataSet actualDataSet = null;
        try {
            actualDataSet = dialog.getNewColumns("action");
        } catch (CancelException e) {
            //do nothing
        }

        //then
        shouldPrint("[Enter the columnNames and its dataTypes of the table you want to action:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back, " +
                //9column1|String|column2|int
                "Error! You have entered '9column1' and name must begin with a letter\n" +
                "Try again, " +
                "Enter the columnNames and its dataTypes of the table you want to action:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back]");
    }

    @Test
    public void testGetConstraintColumn() {
        //given
        when(view.read()).thenReturn("id|int");

        //when
        DataEntry entry = dialog.getConstraintColumn();

        //then
        assertEquals("id", entry.getColumn());
        assertEquals("int", entry.getValue());
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
