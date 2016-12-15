package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    private final String tableName = "test";

    private final String column1 = "id";
    private final int value11 = 1;
    private final int value12 = 2;
    private final String column2 = "name";
    private final String value21 = "John";
    private final String value22 = "Mike";
    private final String column3 = "password";
    private final String value31 = "pass1";
    private final String value32 = "pass2";

    private final int newValue12 = 5;
    private final String newValue22 = "newName";
    private final String newValue32 = "newPass";

    private Set<String> tableColumns;

    private DataSet dataSet1;
    private DataSet dataSet2;

    private List<DataSet> tableContent;
    private DataSet input;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Update(view, manager);

        tableColumns = new LinkedHashSet<>();
        tableColumns.add(column1);
        tableColumns.add(column2);
        tableColumns.add(column3);

        dataSet1 = new DataSetImpl();
        dataSet1.put(column1, value11);
        dataSet1.put(column2, value21);
        dataSet1.put(column3, value31);

        dataSet2 = new DataSetImpl();
        dataSet2.put(column1, value12);
        dataSet2.put(column2, value22);
        dataSet2.put(column3, value32);

        tableContent = new LinkedList<DataSet>();
        tableContent.add(dataSet1);
        tableContent.add(dataSet2);

        input = new DataSetImpl();
        input.put(column2, newValue22);
        input.put(column3, newValue32);
    }

    @Test
    public void testUpdateStringValues() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column1;
        Object definingValue = value12;
        String updatedColumn1 = column2;
        Object newValue1 = newValue22;
        String updatedColumn2 = column3;
        Object newValue2 = newValue32;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(updatedColumn1 + "|" + newValue1 + "|" + updatedColumn2 + "|" + newValue2);
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);
        doNothing().when(manager).update(tableName, input, columnName);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //id|2
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //name|Mike|password|newPass
                "You have successfully updated table 'test' at id = 2, " +
                "| id | name | password | , " +
                "-------------------------, " +
                "| 1 | John | pass1 | \n" +
                "| 2 | Mike | pass2 | \n" +
                "------------------------]");
    }

    @Test
    public void testUpdateIntValue() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String updatedColumn = column1;
        Object newValue = newValue12;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(updatedColumn + "|" + newValue);
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        doNothing().when(manager).update(tableName, input, columnName);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|Mike
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //id|5
                "You have successfully updated table 'test' at name = Mike, " +
                "| id | name | password | , " +
                "-------------------------, " +
                "| 1 | John | pass1 | \n" +
                "| 2 | Mike | pass2 | \n" +
                "------------------------]");
    }

    @Test
    public void testUpdateAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateSelectTableAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn(tableName).thenReturn("cancel");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateAndInputThreeParameters() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String excessParameter = "something";

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue + "|" + excessParameter).
                thenReturn("cancel");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|Mike|something
                "You should enter two parameters!\n" +
                "Try again., " +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateAndInputNotExistingColumn() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = "notExistingColumn";
        Object definingValue = value22;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //notExistingColumn|Mike
                "Table 'test' doesn't contain column 'notExistingColumn'!, " +
                "Try again., " +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateAndInputNotExistingValueString() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = "notExistingValue";

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|notExistingValue
                "Column 'name' doesn't contain value 'notExistingValue'!, " +
                "Try again., " +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateAndInputNotExistingValueInt() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column1;
        Object definingValue = 5;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //id|5
                "Column 'id' doesn't contain value '5'!, " +
                "Try again., " +
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateDefineUpdatedRowAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|Mike
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateDefineUpdatedRowAndInputThreeParameters() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String parameter1 = column1;
        Object parameter2 = newValue12;
        String parameter3 = column3;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(parameter1 + "|" + parameter2 + "|" + parameter3).thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|Mike " +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //id|2|password
                "You should enter an even number of parameters (2, 4 and so on): \n" +
                "Try again., " +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateInputNotExistingUpdatedColumn() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String updatedColumn = "notExistingColumn";
        Object newValue = newValue12;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(updatedColumn + "|" + newValue).thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                //name|Mike
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //notExistingColumn|2
                "Table 'test' doesn't contain column 'notExistingColumn'!, " +
                "Try again., " +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Table updating canceled]");
    }

    @Test
    public void testUpdateNewValueEqualsToUpdated() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String updatedColumn = column1;
        Object newValue = value12;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(updatedColumn + "|" + newValue).thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
                "or type 'cancel' to go back., " +
                "Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back., " +
                "The new values are equivalent to the updated]");
    }

    @Test
    public void testUpdateSQLException() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        String updatedColumn = column1;
        Object newValue = "notNumber";

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue).
                thenReturn(updatedColumn + "|" + newValue).thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        doThrow(new SQLException()).when(manager).update(tableName, input, columnName);

        try {
            manager.update("test", input, columnName);
        } catch (SQLException e) {
            view.write("SQL ERROR: column \"id\" is of type integer but expression is of type character varying!\n" +
                        "Try again.");
        }

        //when
        command.process("update");

        //then
        verify(view).write("SQL ERROR: column \"id\" is of type integer but expression is of type character varying!\n" +
                    "Try again.");
//        shouldPrint("[Please enter the name or select number of table you want to update, " +
//                "1. test, " +
//                "2. ttable, " +
//                "0. cancel (to go back), " +
//                //test
//                "Enter columnName and defining value of updated row: columnName|definingValue\n" +
//                "or type 'cancel' to go back., " +
//                "Enter columnNames and its new values for updated row: \n" +
//                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
//                "or type 'cancel' to go back., " +
//                //id|2
//                "Column 'id' already contains value '2' in required row!\n, " +
//                "Try again., " +
//                "Enter columnNames and its new values for updated row: \n" +
//                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
//                "or type 'cancel' to go back., " +
//                //cancel
//                "Table updating canceled]");
        //TODO SQLException
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessUpdate() {
        //when
        boolean canProcess = command.canProcess("update");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWrongInput() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }
}
